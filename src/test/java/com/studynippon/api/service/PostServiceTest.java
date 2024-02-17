package com.studynippon.api.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.studynippon.api.domain.Post;
import com.studynippon.api.dto.request.PostCreate;
import com.studynippon.api.dto.response.PostDetail;
import com.studynippon.api.repository.PostRepository;

@SpringBootTest
class PostServiceTest {

	@Autowired
	PostService postService;

	@Autowired
	PostRepository postRepository;

	@BeforeEach
	void clean() {
		postRepository.deleteAll();
	}

	@Test
	@DisplayName("게시글 작성 후 1개 검증")
	void writePostTest() {

		// given
		PostCreate postCreate = PostCreate.builder()
			.title("제목")
			.content("내용")
			.build();

		// when
		postService.writePost(postCreate);

		// expected
		assertThat(postRepository.count()).isEqualTo(1L);
		assertThat(postRepository.findAll().get(0).getTitle()).isEqualTo("제목");
		assertThat(postRepository.findAll().get(0).getContent()).isEqualTo("내용");
	}

	@Test
	@DisplayName("게시글 1개 조회 검증")
	void getPostTest() {
		// given
		Post post = Post.builder()
			.title("제목2")
			.content("내용2")
			.build();

		postRepository.save(post);

		// when
		PostDetail postDetail = postService.getPost(post.getId()).getBody();

		// then
		assertThat(postDetail).isNotNull();
		assertThat(postDetail.getTitle()).isEqualTo("제목2");
		assertThat(postDetail.getContent()).isEqualTo("내용2");
	}

	@Test
	@DisplayName("게시글 삭제 검증")
	void deletePostTest() {
		// given
		Post post = Post.builder()
			.title("제목")
			.content("내용")
			.build();

		postRepository.save(post);

		// when
		postService.deletePost(post.getId());

		// then
		assertThat(postRepository.count()).isEqualTo(0L);
	}

}