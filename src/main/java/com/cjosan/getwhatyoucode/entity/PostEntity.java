package com.cjosan.getwhatyoucode.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "posts")
public class PostEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private String postId;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private String image;

	@Column(nullable = false)
	private LocalDate createdAt;

	@ManyToOne
	@JoinColumn(name = "users_id")
	private UserEntity author;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public UserEntity getAuthor() {
		return author;
	}

	public void setAuthor(UserEntity author) {
		this.author = author;
	}
}
