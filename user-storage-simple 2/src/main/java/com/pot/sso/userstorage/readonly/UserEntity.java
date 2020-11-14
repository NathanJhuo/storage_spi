package com.pot.sso.userstorage.readonly;

public class UserEntity {
	private String id;
	private String username;
	private String email;
	private String password;
	private String phone;
	private String lastName;
	private String firstName;
	private String idno;

	public UserEntity(String id, String username, String email,String lastName, String firstame, String idno) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.lastName = lastName;
		this.firstName = firstame;
		this.idno = idno;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	

	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password
				+ ", phone=" + phone + "]";
	}
}
