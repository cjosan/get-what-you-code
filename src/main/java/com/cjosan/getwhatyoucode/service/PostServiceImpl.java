package com.cjosan.getwhatyoucode.service;

import com.cjosan.getwhatyoucode.dto.PostDTO;
import com.cjosan.getwhatyoucode.dto.request.posts.UpdatePostRequestDTO;
import com.cjosan.getwhatyoucode.dto.response.UserResponseDTO;
import com.cjosan.getwhatyoucode.entity.PostEntity;
import com.cjosan.getwhatyoucode.entity.UserEntity;
import com.cjosan.getwhatyoucode.exception.custom.PostNotFoundException;
import com.cjosan.getwhatyoucode.exception.custom.UserNotFoundException;
import com.cjosan.getwhatyoucode.repository.PostRepository;
import com.cjosan.getwhatyoucode.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

	private PostRepository postRepository;
	private UserRepository userRepository;
	private ModelMapper modelMapper = new ModelMapper();

	public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
	}

	@Override
	public List<PostDTO> getUserPosts(String username) {
		UserEntity userEntity = userRepository.getUserByUsername(username);
		if (userEntity == null) {
			throw new UserNotFoundException("Cannot find any posts. User " + username + " does not exist in the database.");
		}

		List<PostEntity> postsFromDb = postRepository.getByAuthor(userEntity);
		List<PostDTO> posts = new ArrayList<>();

		postsFromDb.forEach(postEntity -> {
			PostDTO postDTO = new PostDTO();
			modelMapper.map(postEntity, postDTO);

			posts.add(postDTO);
		});

		return posts;
	}

	@Override
	public PostDTO getPostById(String postId) {
		PostEntity postEntity = postRepository.getByPostId(postId);

		if (postEntity == null) {
			throw new PostNotFoundException("Post was not found in the database");
		}

		PostDTO postDTO = new PostDTO();
		modelMapper.map(postEntity, postDTO);

		return postDTO;
	}

	@Override
	public PostDTO createPost(String username, PostDTO postDTO) {
		UserEntity userEntity = userRepository.getUserByUsername(username);
		if (userEntity == null) {
			throw new UserNotFoundException("Cannot create the post. User " + username + " does not exist in the database.");
		}

		PostEntity postEntity = new PostEntity();
		BeanUtils.copyProperties(postDTO, postEntity);

		postEntity.setPostId(RandomStringUtils.randomAlphanumeric(10));
		postEntity.setCreatedAt(LocalDate.now());
		postEntity.setAuthor(userEntity);

		PostEntity savedPost = postRepository.save(postEntity);

		PostDTO returnValue = new PostDTO();
		BeanUtils.copyProperties(savedPost, returnValue);

		UserResponseDTO userDTO = new UserResponseDTO();
		BeanUtils.copyProperties(userEntity, userDTO);
		returnValue.setAuthor(userDTO);

		return returnValue;
	}

	@Override
	public PostDTO updatePost(String postId, UpdatePostRequestDTO postDTO) {
		PostEntity postEntity = postRepository.getByPostId(postId);

		if (postEntity == null) {
			throw new PostNotFoundException("Post was not found in the database");
		}

		BeanUtils.copyProperties(postDTO, postEntity);

		PostEntity updatedPost = postRepository.save(postEntity);

		PostDTO returnValue = new PostDTO();
		modelMapper.map(updatedPost, returnValue);

		return returnValue;
	}

	@Override
	public void deletePost(String postId) {
		PostEntity postEntity = postRepository.getByPostId(postId);

		if (postEntity == null) {
			throw new PostNotFoundException("Post was not found in the database");
		}

		postRepository.delete(postEntity);
	}
}
