package com.prash.ws.controller;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prash.ws.dto.AddressDto;
import com.prash.ws.dto.UserDto;
import com.prash.ws.exception.UserServiceException;
import com.prash.ws.reponse.AddressResponse;
import com.prash.ws.reponse.ErrorMessages;
import com.prash.ws.reponse.OperationStatus;
import com.prash.ws.reponse.OperationStatusResponse;
import com.prash.ws.reponse.UserDetailsResponse;
import com.prash.ws.request.UserDetailsRequest;
import com.prash.ws.service.AddressService;
import com.prash.ws.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;
	
	@GetMapping
	public List<UserDetailsResponse> getAllUsers() {
		List<UserDetailsResponse> returnValue = new ArrayList<UserDetailsResponse>();
		List<UserDto> usersDto = userService.getAllUsers();
		for (UserDto user : usersDto) {
			UserDetailsResponse resp = new UserDetailsResponse();
			//BeanUtils.copyProperties(user, resp);
			new ModelMapper().map(user, resp);
			returnValue.add(resp);
		}
		//BeanUtils.copyProperties(usersDto, returnValue);
		return returnValue;
	}
	
	//This method get only specified no of users, not all users
	@GetMapping(path="/paginated", produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public List<UserDetailsResponse> getFewUsers(@RequestParam(value="page", defaultValue="0") int page,
												 @RequestParam(value="limit", defaultValue="2") int limit) {
		List<UserDetailsResponse> returnValue = new ArrayList<UserDetailsResponse>();
		List<UserDto> usersDto = userService.getFewUsers(page, limit);
		for (UserDto user : usersDto) {
			UserDetailsResponse resp = new UserDetailsResponse();
			BeanUtils.copyProperties(user, resp);
			returnValue.add(resp);
		}
		//BeanUtils.copyProperties(usersDto, returnValue);
		return returnValue;
	}
	
	@GetMapping(path="/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public UserDetailsResponse getUser(@PathVariable String id) {
		UserDetailsResponse returnValue = new UserDetailsResponse();
		UserDto userDto = userService.getUserByUserId(id);
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.map(userDto, returnValue);
		//BeanUtils.copyProperties(userDto, returnValue);		
		return returnValue;
	}
	
	@PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
			     consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, path="/old")
	public UserDetailsResponse createUserOld(@RequestBody UserDetailsRequest userDetails) throws Exception{		
		UserDetailsResponse userResponse = new UserDetailsResponse();
		if (userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELDS.getErrorMessage());
		UserDto userDto = new UserDto();		
		BeanUtils.copyProperties(userDetails, userDto);		
		UserDto createdUser = userService.createUser(userDto);
		BeanUtils.copyProperties(createdUser, userResponse);
		return userResponse;
	}
	
	@PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
		     consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public UserDetailsResponse createUser(@RequestBody UserDetailsRequest userDetails) throws Exception{		
		UserDetailsResponse userResponse = new UserDetailsResponse();
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);		
		UserDto createdUser = userService.createUser(userDto);
		userResponse = modelMapper.map(createdUser, UserDetailsResponse.class);		
		return userResponse;
	}
	
	@PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
		     	consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
		     	path="/{id}")
	public UserDetailsResponse updateUser(@RequestBody UserDetailsRequest userDetails, @PathVariable String userId) {
		UserDetailsResponse userResponse = new UserDetailsResponse();
		//if (userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELDS.getErrorMessage());
		UserDto userDto = new UserDto();		
		BeanUtils.copyProperties(userDetails, userDto);		
		UserDto updatedUser = userService.updateUser(userDto, userId);
		BeanUtils.copyProperties(updatedUser, userResponse);
		return userResponse;
	}
	
	@DeleteMapping(path="/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public OperationStatusResponse deleteUser(@PathVariable(name="id") String userId) {
		System.out.println("I am inside....................................");
		OperationStatusResponse returnValue = new OperationStatusResponse();
		returnValue.setOperationName(OperationStatus.DELETE.name());
		userService.deleteUser(userId);
		returnValue.setOperationResult(OperationStatus.SUCCESS.name());
		return returnValue;
	}
	
	@GetMapping(path="/{id}/addresses", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public List<AddressResponse> getUserAddresses(@PathVariable String id) {
		
		List<AddressResponse> returnValue = new ArrayList<AddressResponse>();
		
		List<AddressDto> addressesDto = addressService.getAddresses(id);
		
		if (addressesDto != null && !addressesDto.isEmpty()) {
			Type listType = new TypeToken<List<AddressResponse>>() {}.getType();
			returnValue = new ModelMapper().map(addressesDto, listType);
		}
		
		
		return returnValue;
	}
	
	
	@GetMapping(path="/{id}/addresses/{addressId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public AddressResponse getUserAddress(@PathVariable String id, @PathVariable String addressId) {
		
		AddressDto addressDto = addressService.getAddress(addressId);	
		
		
		return new ModelMapper().map(addressDto, AddressResponse.class);
	}

}
