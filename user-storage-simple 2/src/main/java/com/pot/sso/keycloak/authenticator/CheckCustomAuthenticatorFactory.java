package com.pot.sso.keycloak.authenticator;

import java.util.List;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.ConfigurableAuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.provider.ProviderConfigProperty;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class CheckCustomAuthenticatorFactory implements AuthenticatorFactory,  ConfigurableAuthenticatorFactory {

    public static final String PROVIDER_ID = "CheckCustomAuthenticator";
    //  public static final UsernamePasswordForm SINGLETON = new UsernamePasswordForm();
//    private AuthService authService;
    
    @Override
    public Authenticator create(KeycloakSession session) {
//    	return new CheckPwasswordDateForm(authService);
    	return new CheckCustomAuthenticator();
    }

    @Override
    public void init(Config.Scope config) {
//    	String authEndPoint = config.get("authEndPoint");
//    	authService = new AuthService(authEndPoint);
//    	authService = new AuthService();
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getReferenceCategory() {
        return UserCredentialModel.PASSWORD;
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }
    public static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED
    };

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }
   
    @Override
    public String getDisplayType() {
        return "Check Custom Autheticator";
    }

    @Override
    public String getHelpText() {
        return "Validates a username and password from auth api.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return null;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return true;
    }

}
