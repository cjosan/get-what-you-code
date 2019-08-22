package com.cjosan.getwhatyoucode.service;

import com.cjosan.getwhatyoucode.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

	List<UserDTO> listUsers();
	UserDTO getUserByUsername(String username);
	UserDTO createUser(UserDTO userDetails);
	UserDTO updateUser(String username, UserDTO userDetails);
	void deleteUser(String username);

	boolean verifyEmailToken(String token);
	boolean requestPasswordReset(String username);
	boolean resetPassword(String token, String password);
}
