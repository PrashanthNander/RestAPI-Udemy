package com.prash.ws.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.prash.ws.entity.AddressEntity;
import com.prash.ws.entity.UserEntity;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long>{

	List<AddressEntity> findAllByUserDetails(UserEntity userEntity); 
	AddressEntity findByAddressId(String addressId);

}
