package com.pot.sso.keycloak.authenticator;

import java.util.List;
import java.util.Locale;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

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

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class CheckEmployeeAuthenticator extends AbstractDirectGrantAuthenticator implements Authenticator {

	private static final Logger logger = Logger.getLogger(CheckEmployeeAuthenticator.class);

	
	public CheckEmployeeAuthenticator() {

	}

	@Override
	public void action(AuthenticationFlowContext context) {
		logger.info("employee authenticator action start");

        
	}

	@Override
	public void authenticate(AuthenticationFlowContext context) {
		logger.info("employee authenticator start");
		MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();

		String username = formData.getFirst("username");
        
		String regex= System.getenv("EMPLOYEE_REGEX");
		
		//set default regex value 
		if(null==regex) {
			regex="[a-zA-Z0-9]+";
		}
		
		
		if(username.matches(regex)) {
			logger.info("employee authenticate success");
			context.success();
		}else {
			logger.error("format error, username:"+username);
			Response challengeResponse = null;

			challengeResponse = this.errorResponse(417, "Invalid User", "Username Format not match");
			context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR,Response.fromResponse(challengeResponse).language(Locale.TAIWAN).encoding("UTF-8").header("Transfer-Encoding","chunked").build() );
		}
			
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
