package com.cjosan.getwhatyoucode.repository;

import com.cjosan.getwhatyoucode.entity.PostEntity;
import com.cjosan.getwhatyoucode.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<PostEntity, Long> {

	List<PostEntity> getByAuthor(UserEntity author);
	PostEntity getByPostId(String postId);

}
