package com.cjosan.getwhatyoucode.service;

import com.cjosan.getwhatyoucode.dto.PostDTO;
import com.cjosan.getwhatyoucode.dto.request.posts.UpdatePostRequestDTO;

import java.util.List;

public interface PostService {

	List<PostDTO> getUserPosts(String username);
	PostDTO getPostById(String postId);
	PostDTO createPost(String username, PostDTO postDTO);
	PostDTO updatePost(String postId, UpdatePostRequestDTO postDTO);
	void deletePost(String postId);

}
