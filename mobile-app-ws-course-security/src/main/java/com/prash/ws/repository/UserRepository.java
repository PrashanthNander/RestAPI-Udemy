package com.prash.ws.repository;

import org.springframework.data.repository.CrudRepository;

import com.prash.ws.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long>{
	UserEntity findByEmail(String email);
}
