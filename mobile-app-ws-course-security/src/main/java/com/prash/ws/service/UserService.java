package com.prash.ws.service;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.prash.ws.dto.UserDto;
import com.prash.ws.entity.UserEntity;
import com.prash.ws.repository.UserRepository;
import com.prash.ws.shared.Utils;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	UserRepository userRepositroty;
	
	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserDto createUser(UserDto userDto) {
		
		if (userRepositroty.findByEmail(userDto.getEmail()) != null) {
			throw new RuntimeException("Record Already Exists");
		}
		
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(userDto, userEntity);
		
		String publicUserId = utils.generateUserId(30);
		userEntity.setEncriptedPassword(bCryptPasswordEncoder.encode(userDto.getEmail()));
		userEntity.setUserId(publicUserId);
		System.out.println("Encripted------"+ bCryptPasswordEncoder.encode(userDto.getEmail()));
		System.out.println("Encripted------"+ userEntity.getEncriptedPassword());
		UserEntity createdUser = userRepositroty.save(userEntity);
		BeanUtils.copyProperties(createdUser, userDto);
		return userDto; 
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		System.out.println("User Email First=" + email);
		UserEntity userEntity = userRepositroty.findByEmail(email);
		System.out.println("User Email=" + userEntity.getEmail());
		System.out.println("User Password=" + userEntity.getEncriptedPassword());
		System.out.println("User Entity=" + userEntity);	
		if (userEntity == null ) throw new UsernameNotFoundException(email);
		System.out.println("Success-------");
		User user = new User(userEntity.getEmail(), userEntity.getEncriptedPassword(), new ArrayList<>());
		
		System.out.println("New Email=" + user.getUsername());
		System.out.println("New password=" + user.getPassword());
		return new User(userEntity.getEmail(), userEntity.getEncriptedPassword(), new ArrayList<>());
	}
	
	

}
