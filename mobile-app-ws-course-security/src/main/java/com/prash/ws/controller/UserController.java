package com.prash.ws.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prash.ws.dto.UserDto;
import com.prash.ws.reponse.UserDetailsResponse;
import com.prash.ws.request.UserDetailsRequest;
import com.prash.ws.service.UserService;

@RestController
@RequestMapping("users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping
	public String getUser() {
		return "";
	}
	
	@PostMapping
	public UserDetailsResponse createUser(@RequestBody UserDetailsRequest userDetails) {
		
		UserDetailsResponse userResponse = new UserDetailsResponse();
		UserDto userDto = new UserDto();
		
		BeanUtils.copyProperties(userDetails, userDto);
		
		UserDto createdUser = userService.createUser(userDto);
		BeanUtils.copyProperties(createdUser, userResponse);
		return userResponse;
	}
	
	@PutMapping
	public String updateUser() {
		return "";
	}
	
	@DeleteMapping
	public String deleteUser() {
		return "";
	}

}
