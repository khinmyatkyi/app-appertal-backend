package com.example.backend.dto;

import lombok.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter @Setter
public class LoginResponse {   

	private String token;
	private UserDTO user;
	
	public LoginResponse(String token2) {
		token = token2;
	}
}

