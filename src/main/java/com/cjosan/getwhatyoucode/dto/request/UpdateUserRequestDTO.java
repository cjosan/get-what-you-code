package com.cjosan.getwhatyoucode.dto.request;

import javax.validation.constraints.NotBlank;

public class UpdateUserRequestDTO {

	@NotBlank(message = "Username cannot be null")
	private String username;

	@NotBlank(message = "Email cannot be null")
	private String email;

	@NotBlank(message = "First name cannot be null")
	private String firstName;

	@NotBlank(message = "Last name cannot be null")
	private String lastName;

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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}