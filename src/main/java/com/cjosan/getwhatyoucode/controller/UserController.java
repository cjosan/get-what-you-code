package com.cjosan.getwhatyoucode.controller;

import com.cjosan.getwhatyoucode.dto.UserDTO;
import com.cjosan.getwhatyoucode.dto.request.users.CreateUserRequestDTO;
import com.cjosan.getwhatyoucode.dto.request.users.PasswordResetDTO;
import com.cjosan.getwhatyoucode.dto.request.users.ResetPasswordRequestDTO;
import com.cjosan.getwhatyoucode.dto.request.users.UpdateUserRequestDTO;
import com.cjosan.getwhatyoucode.dto.response.UserResponseDTO;
import com.cjosan.getwhatyoucode.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public List<UserResponseDTO> listUsers() {
		List<UserDTO> userList = userService.listUsers();

		return userList.stream()
				.map(userDTO -> {
					UserResponseDTO userResponseDTO = new UserResponseDTO();
					BeanUtils.copyProperties(userDTO, userResponseDTO);

					return userResponseDTO;
				})
				.collect(Collectors.toList());
	}

	@GetMapping("/{username}")
	public UserResponseDTO getUserByUsername(@PathVariable(name = "username") String username) {
		UserResponseDTO userResponse = new UserResponseDTO();

		UserDTO userDTO = userService.getUserByUsername(username);

		BeanUtils.copyProperties(userDTO, userResponse);

		return userResponse;
	}

	@PostMapping
	public UserResponseDTO createUser(@Valid @RequestBody CreateUserRequestDTO createUserRequestDTO) {
		UserResponseDTO userResponse = new UserResponseDTO();
		UserDTO userDTO = new UserDTO();

		BeanUtils.copyProperties(createUserRequestDTO, userDTO);
		UserDTO createdUser = userService.createUser(userDTO);
		BeanUtils.copyProperties(createdUser, userResponse);

		return userResponse;
	}

	@PutMapping("/{username}")
	public UserResponseDTO updateUser(@PathVariable(name = "username") String username, @Valid @RequestBody UpdateUserRequestDTO updateUserRequestDTO) {
		UserResponseDTO userResponse = new UserResponseDTO();
		UserDTO updatedUser = userService.updateUser(username, updateUserRequestDTO);
		BeanUtils.copyProperties(updatedUser, userResponse);

		return userResponse;
	}

	@DeleteMapping("/{username}")
	public void deleteUser(@PathVariable(name = "username") String username) {
		userService.deleteUser(username);
	}

	@GetMapping("/email-verification")
	public boolean verifyEmail(@RequestParam(value = "token") String token) {
		return userService.verifyEmailToken(token);
	}

	@PostMapping("/password-reset-request")
	public boolean requestPasswordReset(@RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO) {
		return userService.requestPasswordReset(resetPasswordRequestDTO.getUsername());
	}

	@PostMapping("/password-reset")
	public boolean resetPassword(@RequestBody PasswordResetDTO passwordResetDTO) {
		return userService.resetPassword(passwordResetDTO.getToken(), passwordResetDTO.getPassword());
	}

}
