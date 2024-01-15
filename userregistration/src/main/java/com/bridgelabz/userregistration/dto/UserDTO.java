package com.bridgelabz.userregistration.dto;

import lombok.Data;

@Data
public class UserDTO {	
	private String firstName;
	private String lastName;
	private String email;
	private String address;
	private String password;
	
	public UserDTO() {
		super();
	}
}
