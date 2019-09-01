package com.cjosan.getwhatyoucode.controller;

import com.cjosan.getwhatyoucode.dto.PostDTO;
import com.cjosan.getwhatyoucode.dto.request.posts.CreatePostRequestDTO;
import com.cjosan.getwhatyoucode.dto.request.posts.UpdatePostRequestDTO;
import com.cjosan.getwhatyoucode.dto.response.PostResponseDTO;
import com.cjosan.getwhatyoucode.service.PostService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostController {

	private PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping("/author/{username}")
	public List<PostResponseDTO> getUserPosts(@PathVariable("username") String username) {
		List<PostDTO> userPosts = postService.getUserPosts(username);

		return userPosts.stream()
				.map(postDTO -> {
					PostResponseDTO postResponseDTO = new PostResponseDTO();
					BeanUtils.copyProperties(postDTO, postResponseDTO);

					return postResponseDTO;
				})
				.collect(Collectors.toList());
	}

	@GetMapping("/{postId}")
	public PostResponseDTO getPostById(@PathVariable("postId") String postId) {
		PostDTO postById = postService.getPostById(postId);
		PostResponseDTO postResponseDTO = new PostResponseDTO();
		BeanUtils.copyProperties(postById, postResponseDTO);

		return postResponseDTO;
	}

	@PostMapping("/{username}")
	public PostResponseDTO createPost(@PathVariable("username") String username, @Valid @RequestBody CreatePostRequestDTO createPostRequestDTO) {
		PostDTO postDTO = new PostDTO();
		BeanUtils.copyProperties(createPostRequestDTO, postDTO);

		PostDTO createdPost = postService.createPost(username, postDTO);
		PostResponseDTO postResponseDTO = new PostResponseDTO();
		BeanUtils.copyProperties(createdPost, postResponseDTO);

		return postResponseDTO;
	}

	@PutMapping("/{postId}")
	public PostResponseDTO updatePost(@PathVariable("postId") String postId, @Valid @RequestBody UpdatePostRequestDTO updatePostRequestDTO) {
		PostDTO updatedPost = postService.updatePost(postId, updatePostRequestDTO);
		PostResponseDTO postResponseDTO = new PostResponseDTO();
		BeanUtils.copyProperties(updatedPost, postResponseDTO);

		return postResponseDTO;
	}

	@DeleteMapping("/{postId}")
	public void deletePost(@PathVariable("postId") String postId) {
		postService.deletePost(postId);
	}

}
