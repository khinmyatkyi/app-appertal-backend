package com.example.backend.controller;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend.constants.ErrorCodes;
import com.example.backend.dto.UserDTO;
import com.example.backend.exception.CustomException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/auth")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	UserService userService;
	
	
	@PostMapping("/user")
	public ResponseEntity<UserDTO> save(@RequestBody UserDTO userDTO)
	{		
		Optional<User> usr = userRepo.findByUsername(userDTO.getUsername());
		if(usr.isPresent())
			throw new CustomException(ErrorCodes.USERNAME_ALREADY_EXISTS,
					"Username already exist!");
		UserDTO usrDTO = userService.save(userDTO);
		return new ResponseEntity<>(usrDTO, HttpStatus.CREATED);			
	}
	
	@PreAuthorize("hasRole('ADMIN') OR hasRole('GUEST_ADMIN')")
	@PutMapping("/user/{id}")
	public ResponseEntity<UserDTO> update(@PathVariable("id") Long id, @RequestBody UserDTO usrDTO)
	{	
		Optional<User> usrOpt = userRepo.findById(id);
		if(!usrOpt.isPresent())
			throw new CustomException(ErrorCodes.USER_ID_NOTFOUND,
					"User Not Found!");
		UserDTO userDTO = userService.update(usrDTO);
		return new ResponseEntity<>(userDTO, HttpStatus.CREATED);		
	}
	
	@PreAuthorize("hasRole('ADMIN') OR hasRole('GUEST_ADMIN')")
	@GetMapping("/user")
	public ResponseEntity<List<UserDTO>> getAll(
			@RequestParam String username,
	        @RequestParam String role){
		List<UserDTO> userDTOList = new ArrayList<UserDTO>();
		userDTOList = userService.getAll(username, role);
		return new ResponseEntity<>(userDTOList, HttpStatus.ACCEPTED);
	}
	
	@PreAuthorize("hasRole('ADMIN') OR hasRole('GUEST_ADMIN')")
	@GetMapping("/user/{id}")
	public ResponseEntity<UserDTO> getById(@PathVariable("id") Long id)
	{
		UserDTO usrDTO = new UserDTO();
		usrDTO = userService.getById(id);
		return new ResponseEntity<>(usrDTO, HttpStatus.ACCEPTED);
	}
	
//	@PreAuthorize("hasRole('ADMIN')")
//	@GetMapping("/userByUsernameAndRole")
//	public ResponseEntity<List<UserDTO>> getUser(
//	        @RequestParam String username,
//	        @RequestParam String role) {
//
//	    List<UserDTO> usrDTO = userService.getByUsernameAndRole(username, role);
//	    return new ResponseEntity<>(usrDTO, HttpStatus.OK);
//	}

}
