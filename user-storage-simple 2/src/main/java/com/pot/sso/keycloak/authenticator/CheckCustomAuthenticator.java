package com.pot.sso.keycloak.authenticator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.authenticators.directgrant.AbstractDirectGrantAuthenticator;
import org.keycloak.models.AuthenticationExecutionModel.Requirement;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.services.ServicesLogger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.common.StringUtils;
import com.pot.sso.userstorage.readonly.CustomeResponse;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class CheckCustomAuthenticator extends AbstractDirectGrantAuthenticator implements Authenticator {
	protected static ServicesLogger log = ServicesLogger.LOGGER;
	private static final Logger logger = Logger.getLogger(CheckCustomAuthenticator.class);
    private static final String host = System.getenv("CS_HOST");
//	private AuthService authService;

//	public CheckPwasswordDateForm(AuthService authService) {
//		this.authService = authService;
//	}
	
	public CheckCustomAuthenticator() {
//		this.authService = authService;
	}

	@Override
	public void action(AuthenticationFlowContext context) {

		logger.info("custom action start");
//		MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
//		if (formData.containsKey("cancel")) {
//			context.cancelLogin();
//			return;
//		}
//
//		String username = formData.getFirst("username");
//        String password = formData.getFirst(CredentialRepresentation.PASSWORD);
//        String idno = formData.getFirst("idno");
//        Map<String, Object> responseMap = this.checkValidUsernamePassword(username,password,idno);
//		if(responseMap.get("success").equals("true")) {
//			context.success();
//		} else {
//			Response challengeResponse = context.form().setError(responseMap.get("error").toString()).createCode();
//			context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR, challengeResponse);
//			return;
//		}
        
	}

	@Override
	public void authenticate(AuthenticationFlowContext context) {
		logger.info("custom authenticate start");
		MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
		if (formData.containsKey("cancel")) {
			context.cancelLogin();
			return;
		}

		String username = formData.getFirst("username");
        String password = formData.getFirst(CredentialRepresentation.PASSWORD);
        String idno=formData.getFirst("idno");
        String xForwardFor= null;
        if(null !=context.getHttpRequest().getHttpHeaders()) {
        	xForwardFor = context.getHttpRequest().getHttpHeaders().getHeaderString("X-Forwarded-For");
        }else {
        	xForwardFor="192.168.0.0";
        }
            
        String[] strArgs=username.split("_");
 
        String loginId = username;
        if(strArgs.length==2) {
        	idno= strArgs[0];
        	loginId= strArgs[1];
        }
        
        CustomeResponse responseMap = this.checkValidUsernamePassword(loginId,password,idno, xForwardFor);
		if(responseMap.getSuccess().equals("true")) {
			logger.info("custom authenticate success");
			context.success();
		} else {
			ObjectMapper om=new ObjectMapper();
			String errorDescription = null;
			try {
				errorDescription = om.writeValueAsString(responseMap.getError());
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String error=null;
			error=responseMap.getError().get("code").toString();
			
			Response challengeResponse = null;

			challengeResponse = this.errorResponse(417, error, errorDescription);

			
			
			context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR,Response.fromResponse(challengeResponse).language(Locale.TAIWAN).encoding("UTF-8").header("Transfer-Encoding","chunked").build() );
			return;
		}
//		String loginHint = context.getAuthenticationSession().getClientNote(OIDCLoginProtocol.LOGIN_HINT_PARAM);
//
//		String rememberMeUsername = AuthenticationManager.getRememberMeUsername(context.getRealm(),
//				context.getHttpRequest().getHttpHeaders());
//
//		if (loginHint != null || rememberMeUsername != null) {
//			if (loginHint != null) {
//				formData.add(AuthenticationManager.FORM_USERNAME, loginHint);
//			} else {
//				formData.add(AuthenticationManager.FORM_USERNAME, rememberMeUsername);
//				formData.add("rememberMe", "on");
//			}
//		}
		return;
	}

	@Override
	public boolean requiresUser() {
		return true;
	}

	@Override
	public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
		// never called
		return true;
	}

	@Override
	public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
		// never called
	}

	@Override
	public void close() {

	}
	
	public CustomeResponse checkValidUsernamePassword(String loginId, String password, String idno, String xForwardFor) {
		CustomeResponse map = new CustomeResponse();
		try {
			
			
			String userUri=host+"/auth/user/"+idno+"/valid";
			
			// create HTTP Client
			HttpClient httpClient = HttpClientBuilder.create().build();
			
			// Create new getRequest with below mentioned URL
			HttpPost getRequest = new HttpPost(userUri);
			
			// Add additional header to getRequest which accepts application/xml data
			getRequest.addHeader("accept", "application/json");
			getRequest.addHeader("X-Forwarded-For", xForwardFor);
			String json = "{\"username\":\""+loginId+"\",\"password\":\""+password+"\"}";
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
			logger.info(sb.toString());
			ObjectMapper mapper = new ObjectMapper();
			
			map =  mapper.readValue(sb.toString(), CustomeResponse.class);
//			System.out.println(map.get("success"));
//			System.out.println(map.get("data"));

 
		} catch (ClientProtocolException e) {
			e.printStackTrace();
 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReferenceCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isConfigurable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Requirement[] getRequirementChoices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUserSetupAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getHelpText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProviderConfigProperty> getConfigProperties() {
		// TODO Auto-generated method stub
		return null;
	}
}
