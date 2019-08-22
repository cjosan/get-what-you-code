package com.cjosan.getwhatyoucode.entity;

import javax.persistence.*;

@Entity(name = "password_reset_tokens")
public class PasswordResetTokenEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String token;

	@OneToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
}
