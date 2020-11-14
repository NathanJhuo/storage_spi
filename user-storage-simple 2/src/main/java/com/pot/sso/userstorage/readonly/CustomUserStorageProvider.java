package com.pot.sso.userstorage.readonly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.cache.CachedUserModel;
import org.keycloak.storage.StorageId;

import com.fasterxml.jackson.databind.ObjectMapper;

@Stateful(passivationCapable=false)
public class CustomUserStorageProvider implements ILocalCustomUserStorageProvider {
	private static final Logger logger = Logger.getLogger(CustomUserStorageProvider.class);
	private static final String host=System.getenv("CS_HOST");
	protected ComponentModel model;
	protected KeycloakSession kcSession;
//	private AuthService authService;

	@Override
	public void setModel(ComponentModel model) {
		this.model = model;
	}

	@Override
	public void setSession(KeycloakSession session) {
		this.kcSession = session;
	}

	@Override
	public void preRemove(RealmModel realm) {

	}

	@Override
	public void preRemove(RealmModel realm, GroupModel group) {

	}

	@Override
	public void preRemove(RealmModel realm, RoleModel role) {

	}

	@Remove
	@Override
	public void close() {
	}

	private UserEntity prepareUserEntity(final Map<String, Object> userMap) {
		return new UserEntity((String) userMap.get("id"), (String) userMap.get("username"), (String) userMap.get("email"),
				(String)userMap.get("fullName"),(String) userMap.get("loginName"), (String)userMap.get("idno"));
	}

	@Override
	public UserModel getUserById(String id, RealmModel realm) {
		logger.info("getUserById: " + id);
		String persistenceId = StorageId.externalId(id);
		String requestUri=host+"/auth/user?id=";
		try {
			
			// create HTTP Client
			HttpClient httpClient = HttpClientBuilder.create().build();
 
			// Create new getRequest with below mentioned URL
			HttpGet getRequest = new HttpGet(requestUri+persistenceId);
 
			// Add additional header to getRequest which accepts application/xml data
			getRequest.addHeader("accept", "application/json");
 
			// Execute your request and catch response
			HttpResponse response = httpClient.execute(getRequest);
 
			// Check for HTTP response code: 200 = success
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
 
			// Get-Capture Complete application/xml body response
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output;
			StringBuilder sb = new StringBuilder();
			logger.info("============Output:============");
 
			// Simply iterate through XML response and show on console.
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = mapper.readValue(sb.toString(), Map.class);
			logger.info(map);
			
			if(map.get("success").equals(true)) {
				String userJsonString=mapper.writeValueAsString(map.get("data"));
				ObjectMapper dataMapper = new ObjectMapper();
				Map<String, Object> dataMap = dataMapper.readValue(userJsonString, Map.class);
				return new UserAdapter(kcSession, realm, model, prepareUserEntity(dataMap));
			}else {
				return null;
			}
			
 
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public UserModel getUserByUsername(String username, RealmModel realm) {
		String requestUri=host+"/auth/user?username=";
		String queryUri=null;
		try {
			String[] strArgs=username.split("_");
			if(strArgs.length==2) {
				requestUri=host+"/auth/user?id=";
				String idno = strArgs[0];
				queryUri=requestUri+idno.toUpperCase();
			}else {
				queryUri=requestUri+username;
			}
			
			// create HTTP Client
			HttpClient httpClient = HttpClientBuilder.create().build();
 
			// Create new getRequest with below mentioned URL
			HttpGet getRequest = new HttpGet(queryUri);
 
			// Add additional header to getRequest which accepts application/xml data
			getRequest.addHeader("accept", "*/*");
 
			// Execute your request and catch response
			HttpResponse response = httpClient.execute(getRequest);
 
			// Check for HTTP response code: 200 = success
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
 
			// Get-Capture Complete application/xml body response
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output;
			StringBuilder sb = new StringBuilder();
 
			// Simply iterate through XML response and show on console.
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			
			logger.info(sb.toString());
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = mapper.readValue(sb.toString(), Map.class);
			
			if(map.get("success").equals(true)) {
				String userJsonString=mapper.writeValueAsString(map.get("data"));
				ObjectMapper dataMapper = new ObjectMapper();
				Map<String, Object> dataMap = dataMapper.readValue(userJsonString, Map.class);
				dataMap.put("username", username);
				return new UserAdapter(kcSession, realm, model, prepareUserEntity(dataMap));
			}else {
				return null;
			}
			
 
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public UserModel getUserByEmail(String email, RealmModel realm) {
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onCache(RealmModel realm, CachedUserModel user, UserModel delegate) {
		String password = ((UserAdapter) delegate).getPassword();
		if (password != null) {
			user.getCachedWith().put(PASSWORD_CACHE_KEY, password);
		}
	}

	@Override
	public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
		logger.info("isConfiguredFor: " + (supportsCredentialType(credentialType) && getPassword(user) != null));
		return supportsCredentialType(credentialType) && getPassword(user) != null;
	}

	public String getPassword(UserModel user) {
		String password = null;
		if (user instanceof CachedUserModel) {
			password = (String) ((CachedUserModel) user).getCachedWith().get(PASSWORD_CACHE_KEY);
		} else if (user instanceof UserAdapter) {
			password = ((UserAdapter) user).getPassword();
		}
		return password;
	}

	@Override
	public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
		logger.info("custom isValid  start");
		
		if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel))
			return false;

		UserCredentialModel cred = (UserCredentialModel) input;

//		Map loginMsg = this.checkValidUsernamePassword(user.getUsername(), cred.getValue(), user.getFirstAttribute("idno"));
//		return loginMsg.get("success").equals("true");
		return true;
	}
	@Override
	public int getUsersCount(RealmModel realm) {
		return 0;
	}

	@Override
	public List<UserModel> getUsers(RealmModel realm) {
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults) {
		return null;
	}

	@Override
	public List<UserModel> searchForUser(String search, RealmModel realm) {
//		return searchForUser(search, realm, -1, -1);
		return Collections.emptyList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UserModel> searchForUser(String userName, RealmModel realm, int firstResult, int maxResults) {
		List<UserModel> userList = new ArrayList<>();
		String requestUri=host+"/auth/user?username=";
		try {
			
			// create HTTP Client
			HttpClient httpClient = HttpClientBuilder.create().build();
 
			// Create new getRequest with below mentioned URL
			HttpGet getRequest = new HttpGet(requestUri+userName);
 
			// Add additional header to getRequest which accepts application/xml data
			getRequest.addHeader("accept", "*/*");
 
			// Execute your request and catch response
			HttpResponse response = httpClient.execute(getRequest);
 
			// Check for HTTP response code: 200 = success
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
 
			// Get-Capture Complete application/xml body response
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output;
			StringBuilder sb = new StringBuilder();
			logger.info("============Output:============");
 
			// Simply iterate through XML response and show on console.
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = mapper.readValue(sb.toString(), Map.class);
			
			if(map.get("success").equals(true)) {
				String userJsonString=mapper.writeValueAsString(map.get("data"));
				ObjectMapper dataMapper = new ObjectMapper();
				Map<String, Object> dataMap = dataMapper.readValue(userJsonString, Map.class);
				userList.add(new UserAdapter(kcSession, realm, model, prepareUserEntity(dataMap)));
				return userList;
			}else {
				return Collections.emptyList();
			}
			
 
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	@Override
	public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm) {
		return Collections.emptyList();
	}

	@Override
	public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm, int firstResult,
			int maxResults) {
		return Collections.emptyList();
	}

	@Override
	public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group, int firstResult, int maxResults) {
		return Collections.emptyList();
	}

	@Override
	public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group) {
		return Collections.emptyList();
	}

	@Override
	public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue, RealmModel realm) {
		return Collections.emptyList();
	}

	@Override
	public boolean supportsCredentialType(String credentialType) {
		return CredentialModel.PASSWORD.equals(credentialType);
	}
	
	
	private Map checkValidUsernamePassword(String username, String password, String idno, String xForwardFor) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			
			
			String customServeiceHostURL="http://mimosa.customer.118.163.91.247.nip.io";
			String userUri=customServeiceHostURL+"/auth/user/"+idno+"/valid";
			// create HTTP Client
			HttpClient httpClient = HttpClientBuilder.create().build();
			
			// Create new getRequest with below mentioned URL
			HttpPost getRequest = new HttpPost(userUri);
			
			// Add additional header to getRequest which accepts application/xml data
			getRequest.addHeader("accept", "application/json");
			getRequest.addHeader("X-Forwarded-For", "192.168.1.1, 192.186.1.121, 192.168.2.122");
			String json = "{\"username\":\""+username+"\",\"password\":\""+password+"\"}";
		    StringEntity entity = new StringEntity(json);
		    entity.setContentType("application/json");
		    getRequest.setEntity(entity);
 
			// Execute your request and catch response
			HttpResponse response = httpClient.execute(getRequest);
 
			// Check for HTTP response code: 200 = success
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
 
			// Get-Capture Complete application/xml body response
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output;
			StringBuilder sb = new StringBuilder();
			System.out.println("============Output:============");
 
			// Simply iterate through XML response and show on console.
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			ObjectMapper mapper = new ObjectMapper();
			map = mapper.readValue(sb.toString(), Map.class);
//			System.out.println(map.get("success"));
//			System.out.println(map.get("data"));

 
		} catch (ClientProtocolException e) {
			e.printStackTrace();
 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
}