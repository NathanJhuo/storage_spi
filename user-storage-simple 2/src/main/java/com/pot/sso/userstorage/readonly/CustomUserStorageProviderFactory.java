package com.pot.sso.userstorage.readonly;

import javax.naming.InitialContext;

import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

import com.pot.sso.userstorage.service.AuthService;

public class CustomUserStorageProviderFactory implements UserStorageProviderFactory<ILocalCustomUserStorageProvider>{

	public static final String PROVIDER_NAME = "customer-service";
    private static final Logger logger = Logger.getLogger(UserStorageProviderFactory.class);
//    private AuthService authService;
	@Override
	public ILocalCustomUserStorageProvider create(KeycloakSession session, ComponentModel model) {

		try {
			InitialContext ctx = new InitialContext();
			ILocalCustomUserStorageProvider provider = (ILocalCustomUserStorageProvider) ctx
					.lookup("java:global/pot-user-storage-properties/" + CustomUserStorageProvider.class.getSimpleName());
			provider.setModel(model);
			provider.setSession(session);
			return provider;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	@Override
	public String getId() {
		return PROVIDER_NAME;
	}
	
	

}
