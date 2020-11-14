package com.pot.sso.userstorage.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateful;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

//@Stateful
public class AuthService {

//	public static final String LOGIN_PASSWORD_EXPIRED = "2";
//	public static final String LOGIN_SUCCESS = "1";
//	public static final String LOGIN_FAIL = "0";
//	public static final String UNKNOWN = "NULL";
	
	private static final Logger logger = Logger.getLogger(AuthService.class);

//	private CustomUserStorageProvider customUserStorageProvider;

//	public String checkValidUsernamePassword(String username, String password) {
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			
//			
//			String customServeiceHostURL="http://mimosa.customer.118.163.91.247.nip.io";
//			String userUri=customServeiceHostURL+"/auth/user/"+idno+"/valid";
//			// create HTTP Client
//			HttpClient httpClient = HttpClientBuilder.create().build();
//			
//			// Create new getRequest with below mentioned URL
//			HttpPost getRequest = new HttpPost(userUri);
//			
//			// Add additional header to getRequest which accepts application/xml data
//			getRequest.addHeader("accept", "application/json");
//			getRequest.addHeader("X-Forwarded-For", "192.168.1.1, 192.186.1.121, 192.168.2.122");
//			String json = "{\"username\":\""+username+"\",\"password\":\""+password+"\"}";
//		    StringEntity entity = new StringEntity(json);
//		    entity.setContentType("application/json");
//		    getRequest.setEntity(entity);
// 
//			// Execute your request and catch response
//			HttpResponse response = httpClient.execute(getRequest);
// 
//			// Check for HTTP response code: 200 = success
//			if (response.getStatusLine().getStatusCode() != 200) {
//				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
//			}
// 
//			// Get-Capture Complete application/xml body response
//			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
//			String output;
//			StringBuilder sb = new StringBuilder();
//			System.out.println("============Output:============");
// 
//			// Simply iterate through XML response and show on console.
//			while ((output = br.readLine()) != null) {
//				sb.append(output);
//			}
//			ObjectMapper mapper = new ObjectMapper();
//			map = mapper.readValue(sb.toString(), Map.class);
////			System.out.println(map.get("success"));
////			System.out.println(map.get("data"));
//
// 
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
// 
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return map;
//
//	}
	
	public Map checkValidUsernamePassword(String username, String password, String idno) {
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

	private boolean supportsCredentialType(String type) {
		// TODO Auto-generated method stub
		return false;
	}
}
