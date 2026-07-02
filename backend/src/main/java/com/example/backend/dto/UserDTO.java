package com.example.backend.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;

import com.example.backend.enums.Gender;
import com.example.backend.enums.Role;
import com.example.backend.model.User;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
public class UserDTO {
	private Long id;
    private String userCode;
	private String username;

	private Role role;
	private String firstName;
	private String lastName;
    private Gender gender;
	private String nationality;
    private String email;
    private String phone;
    private String password;
    
    private LocalDateTime createdAt;
    private String createdBy;
    
    private LocalDateTime updatedAt;
    private String updatedBy;
	
	public UserDTO(User entity) {
        this.id = entity.getId();
        this.userCode = entity.getUserCode();
        this.username = entity.getUsername();
        this.role = entity.getRole();
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.gender = entity.getGender();
        this.nationality = entity.getNationality();
        this.email = entity.getEmail();
        this.phone = entity.getPhone();
        this.createdAt = entity.getCreatedAt();
        this.createdBy = entity.getCreatedBy();
        this.updatedAt = entity.getUpdatedAt();
        this.updatedBy = entity.getUpdatedBy();
    }

}
