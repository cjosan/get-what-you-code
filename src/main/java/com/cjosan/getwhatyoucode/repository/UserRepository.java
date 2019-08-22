package com.cjosan.getwhatyoucode.repository;

import com.cjosan.getwhatyoucode.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

	UserEntity getUserByUsername(String username);
	UserEntity getUserByEmail(String email);
	boolean existsUserByEmail(String email);
	UserEntity getUserEntityByEmailVerificationToken(String token);

}
