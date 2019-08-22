package com.cjosan.getwhatyoucode.service;

import com.cjosan.getwhatyoucode.dto.UserDTO;
import com.cjosan.getwhatyoucode.entity.PasswordResetTokenEntity;
import com.cjosan.getwhatyoucode.entity.UserEntity;
import com.cjosan.getwhatyoucode.exception.custom.UserAlreadyExistsException;
import com.cjosan.getwhatyoucode.exception.custom.UserNotFoundException;
import com.cjosan.getwhatyoucode.repository.PasswordResetTokenRepository;
import com.cjosan.getwhatyoucode.repository.UserRepository;
import com.cjosan.getwhatyoucode.utils.MailUtils;
import com.cjosan.getwhatyoucode.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;

	private PasswordResetTokenRepository passwordResetTokenRepository;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private MailSenderService mailSenderService;

	public UserServiceImpl(UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository, BCryptPasswordEncoder bCryptPasswordEncoder, MailSenderService mailSenderService) {
		this.userRepository = userRepository;
		this.passwordResetTokenRepository = passwordResetTokenRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.mailSenderService = mailSenderService;
	}

	@Override
	public List<UserDTO> listUsers() {
		Iterable<UserEntity> usersFromDb = userRepository.findAll();
		List<UserDTO> usersList = new ArrayList<>();

		usersFromDb.forEach(userEntity -> {
			UserDTO userDTO = new UserDTO();
			BeanUtils.copyProperties(userEntity, userDTO);

			usersList.add(userDTO);
		});

		return usersList;
	}

	@Override
	public UserDTO getUserByUsername(String username) {
		UserDTO user = new UserDTO();

		UserEntity userEntity = userRepository.getUserByUsername(username);

		if (userEntity == null) {
			throw new UserNotFoundException("User with username " + username + " was not found in the database");
		}

		BeanUtils.copyProperties(userEntity, user);

		return user;
	}

	@Override
	public UserDTO createUser(UserDTO userDetails) {
		UserEntity userEntity = new UserEntity();
		UserDTO user = new UserDTO();

		if (userRepository.existsUserByEmail(userDetails.getEmail())) {
			throw new UserAlreadyExistsException("Email " + userDetails.getEmail() + " is already in use. " +
					"Please enter a valid email address or sign-in with your existing account.");
		}

		UserEntity userFromDb = userRepository.getUserByUsername(userDetails.getUsername());
		if (userFromDb != null) {
			throw new UserAlreadyExistsException("User with username " + userDetails.getUsername() + " already exists in the database. Please try another one.");
		}

		BeanUtils.copyProperties(userDetails, userEntity);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
		userEntity.setEmailVerificationToken(TokenUtils.generateToken(userDetails.getUsername()));
		userEntity.setEmailVerificationStatus(false);

		mailSenderService.sendHtmlContent(userEntity.getEmail(),
				"Welcome to GetWhatYouCode!",
				MailUtils.getAccountConfirmationMessage(userDetails.getUsername(), userEntity.getEmailVerificationToken()));

		UserEntity savedUserEntity = userRepository.save(userEntity);

		BeanUtils.copyProperties(savedUserEntity, user);

		return user;
	}

	@Override
	public UserDTO updateUser(String username, UserDTO userDetails) {
		UserDTO user = new UserDTO();

		UserEntity userEntity = userRepository.getUserByUsername(username);

		if (userEntity == null) {
			throw new UserNotFoundException("User with username " + username + " was not found in the database");
		}

		BeanUtils.copyProperties(userDetails, userEntity);

		UserEntity updatedUserEntity = userRepository.save(userEntity);
		BeanUtils.copyProperties(updatedUserEntity, user);

		return user;
	}

	@Override
	public void deleteUser(String username) {
		UserEntity userEntity = userRepository.getUserByUsername(username);

		if (userEntity == null) {
			throw new UserNotFoundException("User with username " + username + " was not found in the database");
		}

		userRepository.delete(userEntity);
	}

	@Override
	public boolean verifyEmailToken(String token) {
		boolean isVerified = false;

		UserEntity userEntity = userRepository.getUserEntityByEmailVerificationToken(token);

		if (userEntity != null) {
			boolean isTokenExpired = TokenUtils.isTokenExpired(token);

			if (!isTokenExpired) {
				userEntity.setEmailVerificationToken(null);
				userEntity.setEmailVerificationStatus(true);
				userRepository.save(userEntity);
				isVerified = true;
			}
		}

		return isVerified;
	}

	@Override
	public boolean requestPasswordReset(String username) {
		UserEntity userEntity = userRepository.getUserByUsername(username);

		if (userEntity == null) {
			return false;
		}

		String token = TokenUtils.generateToken(username);

		PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
		passwordResetTokenEntity.setToken(token);
		passwordResetTokenEntity.setUser(userEntity);
		passwordResetTokenRepository.save(passwordResetTokenEntity);

		mailSenderService.sendHtmlContent(userEntity.getEmail(), "Password Reset Request", MailUtils.getPasswordResetMessage(username, token));

		return true;
	}

	@Override
	public boolean resetPassword(String token, String password) {
		boolean result = false;

		if (TokenUtils.isTokenExpired(token)) {
			return false;
		}

		PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);

		if (passwordResetTokenEntity == null) {
			return false;
		}

		String encodedPassword = bCryptPasswordEncoder.encode(password);

		UserEntity userEntity = passwordResetTokenEntity.getUser();
		userEntity.setEncryptedPassword(encodedPassword);
		UserEntity updatedUserEntity = userRepository.save(userEntity);

		if (updatedUserEntity != null && updatedUserEntity.getEncryptedPassword().equalsIgnoreCase(encodedPassword)) {
			result = true;
		}

		passwordResetTokenRepository.delete(passwordResetTokenEntity);

		return result;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.getUserByUsername(username);

		if (userEntity == null) {
			throw new UsernameNotFoundException("User with username " + username + " was not found in the database");
		}

		return new User(userEntity.getUsername(), userEntity.getEncryptedPassword(), userEntity.isEmailVerificationStatus(),
				true, true, true, new ArrayList<>());
	}
}
