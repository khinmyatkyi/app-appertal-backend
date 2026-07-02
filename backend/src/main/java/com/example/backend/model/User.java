package com.example.backend.model;

import javax.persistence.*;

import com.example.backend.enums.Gender;
import com.example.backend.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter @Setter
public class User extends BaseEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "user_code", unique = true)
    private String userCode;

    @Column(unique = true)
    private String username;

    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role; // to change
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String nationality;
    private String email;
    private String phone;

}
