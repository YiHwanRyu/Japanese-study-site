package com.studynippon.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studynippon.api.domain.Post;
import com.studynippon.api.dto.request.PostCreate;
import com.studynippon.api.repository.PostRepository;
import com.studynippon.api.service.PostService;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

	@Autowired
	PostService postService;

	@Autowired
	PostRepository postRepository;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@BeforeEach
	void clean() {
		postRepository.deleteAll();
	}

	@Test
	@DisplayName("게시글 작성 컨트롤러 테스트")
	void writePostTest() throws Exception {

		// given
		PostCreate postCreate = PostCreate.builder()
			.title("게시글 제목")
			.content("게시글 내용")
			.build();

		String jsonValue = objectMapper.writeValueAsString(postCreate);

		// expected
		mockMvc.perform(post("/api/v1/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonValue))
			.andExpect(status().isCreated())
			.andDo(print());
	}

	@Test
	@DisplayName("게시글 단일 조회 컨트롤러 테스트")
	void getPostTest() throws Exception {
		// given
		Post post = Post.builder()
			.title("게시글 제목입니다.")
			.content("게시글 내용입니다.")
			.build();

		postRepository.save(post);

		// expected
		mockMvc.perform(get("/api/v1/posts/{postId}", post.getId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("게시글 제목입니다."))
			.andExpect(jsonPath("$.content").value("게시글 내용입니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("게시글 작성 시 제목은 필수 입력")
	void titleValidationTest() throws Exception {

		// given
		PostCreate postCreate = PostCreate.builder()
			.title("")
			.content("게시글 내용")
			.build();

		String jsonValue = objectMapper.writeValueAsString(postCreate);

		// expected
		mockMvc.perform(post("/api/v1/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonValue))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.errorMessage").value("잘못된 요청입니다."))
			.andExpect(jsonPath("$.validationList[0].errorField").value("title"))
			.andExpect(jsonPath("$.validationList[0].errorFieldMessage").value("제목은 필수입력입니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("게시글 단일 조회 실패 테스트")
	void getPostFailTest() throws Exception {
		// given
		Post post = Post.builder()
			.title("게시글 제목입니다.")
			.content("게시글 내용입니다.")
			.build();

		postRepository.save(post);

		// expected
		mockMvc.perform(get("/api/v1/posts/{postId}", post.getId() + 1L))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.statusCode").value("404"))
			.andExpect(jsonPath("$.errorMessage").value("게시글을 찾을 수 없습니다."))
			.andExpect(jsonPath("$.validationList").isEmpty())
			.andDo(print());
	}

	@Test
	@DisplayName("게시글 삭제 컨트롤러 테스트")
	void deletePostTest() throws Exception {
		// given
		Post post = Post.builder()
			.title("게시글 제목입니다.")
			.content("게시글 내용입니다.")
			.build();

		postRepository.save(post);

		// expected
		mockMvc.perform(delete("/api/v1/posts/{postId}", post.getId()))
			.andExpect(status().isOk())
			.andDo(print());
	}

}