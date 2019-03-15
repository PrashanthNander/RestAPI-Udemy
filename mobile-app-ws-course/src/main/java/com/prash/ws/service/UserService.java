package com.prash.ws.service;


import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.prash.ws.dto.AddressDto;
import com.prash.ws.dto.UserDto;
import com.prash.ws.entity.UserEntity;
import com.prash.ws.exception.UserServiceException;
import com.prash.ws.reponse.ErrorMessages;
import com.prash.ws.repository.UserRepository;
import com.prash.ws.shared.Utils;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepositroty;
	
	@Autowired
	Utils utils;
	
	public List<UserDto> getAllUsers() {
		List<UserDto> userList = new ArrayList<UserDto>();
		List<UserEntity> users = (List<UserEntity>) userRepositroty.findAll();
		//userList.addAll(userList);
		for (UserEntity user : users) {
			UserDto dto = new UserDto();
			BeanUtils.copyProperties(user, dto);
			userList.add(dto);
		}
		//BeanUtils.copyProperties(users, userList);
		return userList;
	}
	

	public UserDto createUser(UserDto user) {
		
		if (userRepositroty.findByEmail(user.getEmail()) != null) {
			throw new RuntimeException("Record Already Exists");
		}
		
		for (int i=0; i<user.getAddresses().size(); i ++) {
			AddressDto address = user.getAddresses().get(i);
			address.setUserDetails(user);
			address.setAddressId(utils.generateAddressId(30));
			user.getAddresses().set(i, address);
		}
		
		UserEntity userEntity = new UserEntity();
		ModelMapper modelMapper = new ModelMapper();
		userEntity = modelMapper.map(user, UserEntity.class);
		
		
		String publicUserId = utils.generateUserId(30);
		userEntity.setEncriptedPassword(user.getEmail());
		userEntity.setUserId(publicUserId);
		
		UserEntity createdUser = userRepositroty.save(userEntity);
		//BeanUtils.copyProperties(createdUser, userDto);
		UserDto returnValue = modelMapper.map(createdUser, UserDto.class);
		return returnValue; 
	}


	public UserDto getUserByUserId(String userId)  {
		// TODO Auto-generated method stub
		UserDto userDto = new UserDto();
		UserEntity userEntity = userRepositroty.findByUserId(userId);
		if (userEntity == null)
			try {
				throw new Exception("User with ID" +userId + " not found.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		BeanUtils.copyProperties(userEntity, userDto);
		return userDto;
	}
	
	public void deleteUser(String userId) {
		UserEntity userEntity = userRepositroty.findByUserId(userId);
		if (userEntity == null) {
				throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}
		//userRepositroty.deleteByUserId(userId);
		userRepositroty.delete(userEntity);
	}


	public UserDto updateUser(UserDto userDto, String userId) {
		// TODO Auto-generated method stub
		UserDto returnVal = new UserDto();
		UserEntity userEntity = userRepositroty.findByUserId(userId);
		if (userEntity == null) {
			try {
				throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());
		UserEntity updatedUserDetails = userRepositroty.save(userEntity);
			
		BeanUtils.copyProperties(updatedUserDetails, returnVal);
		return returnVal;
	}


	public List<UserDto> getFewUsers(int page, int limit) {
		if (page > 0) page -= 1;
		List<UserDto> returnValue = new ArrayList<UserDto>();
		Pageable pageableRequest = PageRequest.of(page, limit);
		Page<UserEntity> usersPage = userRepositroty.findAll(pageableRequest);
		List<UserEntity> users = usersPage.getContent();
		
		for (UserEntity user : users) {
			UserDto dto = new UserDto();
			BeanUtils.copyProperties(user, dto);
			returnValue.add(dto);
		}
		
		return returnValue;
	}


	
	
	

}
