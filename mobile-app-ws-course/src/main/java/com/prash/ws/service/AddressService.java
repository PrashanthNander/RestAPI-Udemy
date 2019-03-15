package com.prash.ws.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prash.ws.dto.AddressDto;
import com.prash.ws.entity.AddressEntity;
import com.prash.ws.entity.UserEntity;
import com.prash.ws.repository.AddressRepository;
import com.prash.ws.repository.UserRepository;

@Service
public class AddressService {
	
	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	UserRepository userRepository;

	public List<AddressDto> getAddresses(String userId) {
		List<AddressDto> returnValue = new ArrayList<AddressDto>();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null) return returnValue;
		
		Iterable<AddressEntity> addresses =  addressRepository.findAllByUserDetails(userEntity);
		
		for (AddressEntity addressEntity : addresses) {
			returnValue.add(new ModelMapper().map(addressEntity, AddressDto.class));
		}
		
		
		return returnValue;
	}

	public AddressDto getAddress(String addressId) {
		// TODO Auto-generated method stub
		AddressDto returnValue = null;
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		
		if (addressEntity != null) {
			returnValue = new ModelMapper().map(addressEntity, AddressDto.class);
		}
		
		return returnValue;
	}


}
