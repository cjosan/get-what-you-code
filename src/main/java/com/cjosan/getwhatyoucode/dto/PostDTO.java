package com.cjosan.getwhatyoucode.dto;

import com.cjosan.getwhatyoucode.dto.response.UserResponseDTO;

import java.time.LocalDate;

public class PostDTO {

	private String postId;
	private String title;
	private String content;
	private String image;
	private LocalDate createdAt;
	private UserResponseDTO author;

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public UserResponseDTO getAuthor() {
		return author;
	}

	public void setAuthor(UserResponseDTO author) {
		this.author = author;
	}
}
