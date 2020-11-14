package com.pot.sso.userstorage.readonly;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.keycloak.models.ClientModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.RoleModel;

public class GroupAdapter implements GroupModel {

	@Override
	public Set<RoleModel> getRealmRoleMappings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<RoleModel> getClientRoleMappings(ClientModel app) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasRole(RoleModel role) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void grantRole(RoleModel role) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<RoleModel> getRoleMappings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRoleMapping(RoleModel role) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "policyholder";
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSingleAttribute(String name, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAttribute(String name, List<String> values) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAttribute(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getFirstAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<String>> getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GroupModel getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<GroupModel> getSubGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(GroupModel group) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addChild(GroupModel subGroup) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeChild(GroupModel subGroup) {
		// TODO Auto-generated method stub

	}

}
