package com.pot.sso.userstorage.readonly;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;


public class UserAdapter extends AbstractUserAdapterFederatedStorage{
	private static final Logger logger = Logger.getLogger(UserAdapter.class);
	protected UserEntity entity;
	protected String keycloakId;

	public UserAdapter(KeycloakSession session, RealmModel realm, ComponentModel model, UserEntity entity) {
		super(session, realm, model);
		this.entity = entity;
		keycloakId = StorageId.keycloakId(model, entity.getId());
	}

	public String getPassword() {
		logger.info("In UserAdapter..Getting password from UserEntity - " + entity.getPassword());
		return entity.getPassword();
	}

	public void setPassword(String password) {
		logger.info("In UserAdapter..Setting password to UserEntity");
		entity.setPassword(password);
	}

	@Override
	public String getUsername() {
		logger.info("In UserAdapter..Getting username from UserEntity - " + entity.getUsername());
		return entity.getUsername();
	}

	@Override
	public void setUsername(String username) {
		logger.info("In UserAdapter..Setting username to UserEntity");
		entity.setUsername(username);

	}

	@Override
	public void setEmail(String email) {
		logger.info("In UserAdapter..Setting email to UserEntity");
		entity.setEmail(email);
	}

	@Override
	public String getEmail() {
		logger.info("In UserAdapter..Getting email from UserEntity - " + entity.getEmail());
		return entity.getEmail();
	}

	@Override
	public String getId() {
		return keycloakId;
	}

	@Override
	public void setSingleAttribute(String name, String value) {
		if (name.equals("idno")) {
			entity.setIdno(value);
		} else {
			super.setSingleAttribute(name, value);
		}
	}

	@Override
	public void removeAttribute(String name) {
		if (name.equals("idno")) {
			entity.setIdno(null);
		} else {
			super.removeAttribute(name);
		}
	}

	@Override
	public void setAttribute(String name, List<String> values) {
		if (name.equals("idno")) {
			entity.setIdno(values.get(0));
		} else {
			super.setAttribute(name, values);
		}
	}

	@Override
	public String getFirstAttribute(String name) {
		if (name.equals("idno")) {
			return entity.getIdno();
		} else {
			return super.getFirstAttribute(name);
		}
	}

	@Override
	public Map<String, List<String>> getAttributes() {
		Map<String, List<String>> attrs = super.getAttributes();
		MultivaluedHashMap<String, String> all = new MultivaluedHashMap<>();
		all.putAll(attrs);
		all.add("idno", entity.getIdno());
		return all;
	}

	@Override
	public List<String> getAttribute(String name) {
		if (name.equals("idno")) {
			List<String> idno = new LinkedList<>();
			idno.add(entity.getIdno());
			return idno;
		} else {
			return super.getAttribute(name);
		}
	}
	
	@Override
    public String getFirstName() {
        return entity.getFirstName();
    }

    /**
     * Stores as attribute in federated storage.
     * FIRST_NAME_ATTRIBUTE
     *
     * @param firstName
     */
    @Override
    public void setFirstName(String firstName) {
        entity.setFirstName(firstName);

    }

    @Override
    public String getLastName() {
        return entity.getLastName();
    }

    /**
     * Stores as attribute in federated storage.
     * LAST_NAME_ATTRIBUTE
     *
     * @param lastName
     */
    @Override
    public void setLastName(String lastName) {
        entity.setLastName(lastName);

    }
    
	
}
