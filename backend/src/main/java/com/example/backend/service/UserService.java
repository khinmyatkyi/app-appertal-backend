package com.example.backend.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.backend.constants.ErrorCodes;
import com.example.backend.dto.UserDTO;
import com.example.backend.enums.Gender;
import com.example.backend.enums.Role;
import com.example.backend.exception.CustomException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repo;    

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = repo.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())   // BCrypt
                .roles(user.getRole().name())
                .build();
    }
    
    public UserDTO save(UserDTO userDTO) { 	

    	String username = "default_guest";   	
    	
    	// Check and Get authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated()) {        	    
        	username = auth.getName();         	
        }        
        
        // Encode password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
        
        User lastUser = repo.findTopByOrderByIdDesc();

        long nextId = (lastUser == null) ? 1 : lastUser.getId() + 1;

        String userCode = String.format("USR%06d", nextId);
        
        User user = new User();
        user.setUserCode(userCode);
        user.setUsername(userDTO.getUsername());
        user.setRole(userDTO.getRole());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getGender() != null && !userDTO.getGender().toString().isEmpty()) {
            user.setGender(Gender.OTHER);
        }
        user.setNationality(userDTO.getNationality());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setPassword(hashedPassword);
        user.setCreatedBy(username);        
        
    	user = repo.save(user);
    	UserDTO usrDTO = new UserDTO(user);
    	return usrDTO;
    }
    
    public UserDTO update(UserDTO usrDTO) {
    	// Get authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = repo.findById(usrDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setId(usrDTO.getId());
        user.setUsername(usrDTO.getUsername());
        user.setFirstName(usrDTO.getFirstName());
        user.setLastName(usrDTO.getLastName());
        user.setGender(usrDTO.getGender());
        user.setNationality(usrDTO.getNationality());
        user.setEmail(usrDTO.getEmail());
        user.setPhone(usrDTO.getPhone());
        user.setRole(usrDTO.getRole());
        user.setUpdatedBy(username);
        
    	user = repo.save(user);
    	UserDTO userDTO = new UserDTO(user);
    	return userDTO;
    }
    
    public List<UserDTO> getAll(String username, String role) {

        List<User> userList;

        if (username != null && !username.isEmpty() &&
            role != null && !role.isEmpty()) {

            Role roleEnum = Role.valueOf(role.toUpperCase());
            userList = repo.findByUsernameAndRole(username, roleEnum);

        } else if (username != null && !username.isEmpty()) {

        	userList = repo.findByUsername(username)
                    .map(Collections::singletonList)
                    .orElse(new ArrayList<>());

        } else if (role != null && !role.isEmpty()) {

            Role roleEnum = Role.valueOf(role.toUpperCase());
            userList = repo.findByRole(roleEnum);

        } else {
            userList = repo.findAll();
        }

        return userList.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());

    }


    
    public UserDTO getById(long id) {    	
    	Optional<User> usrOpt = repo.findById(id);
    	if(!usrOpt.isPresent()) {
    		throw new CustomException(ErrorCodes.USER_ID_NOTFOUND,
					"User Not Found!");
    	}
    	UserDTO usrDTO = new UserDTO(usrOpt.get());
    	return usrDTO;
    }
    
}

