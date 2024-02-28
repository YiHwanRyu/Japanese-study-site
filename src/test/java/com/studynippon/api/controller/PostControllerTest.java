package com.studynippon.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studynippon.api.dto.request.PostCreate;
import com.studynippon.api.dto.request.PostSearch;
import com.studynippon.api.entity.Post;
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

	@Test
	@DisplayName("게시글 리스트 페이징 첫 페이지 조회 테스트")
	void getPostListTest() throws Exception {
		// given
		List<Post> postRequestList = IntStream.range(1, 31)
			.mapToObj(
				i -> Post.builder()
					.title("제목" + i)
					.content("내용" + i)
					.build()
			).toList();

		postRepository.saveAll(postRequestList);

		PostSearch postSearch = PostSearch.builder()
			.pageNumber(1)
			.pageSize("PAGE_SIZE_5")
			.pageSort("DESC")
			.sortParam("SORT_BY_DATE")
			.build();

		String jsonValue = objectMapper.writeValueAsString(postSearch);

		// expected
		mockMvc.perform(get("/api/v1/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonValue)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.postDtoList.length()").value(5))
			.andExpect(jsonPath("$.postDtoList[0].title").value("제목30"))
			.andExpect(jsonPath("$.postDtoList[0].content").value("내용30"))
			.andExpect(jsonPath("$.postDtoList[1].title").value("제목29"))
			.andExpect(jsonPath("$.postDtoList[1].content").value("내용29"))
			.andExpect(jsonPath("$.firstPage").value(true))
			.andExpect(jsonPath("$.lastPage").value(false))
			.andExpect(jsonPath("$.totalPages").value(30/5))
			.andDo(print());
	}

	@Test
	@DisplayName("게시글 리스트 페이징 validation 테스트(custom enum 일 때)")
	void getPostListValidationTest() throws Exception {
		// given
		List<Post> postRequestList = IntStream.range(1, 31)
			.mapToObj(
				i -> Post.builder()
					.title("제목" + i)
					.content("내용" + i)
					.build()
			).toList();

		postRepository.saveAll(postRequestList);

		PostSearch postSearch = PostSearch.builder()
			.pageNumber(1)
			.pageSize("")
			.pageSort("DESC")
			.sortParam("SORT_BY_DATE")
			.build();

		String jsonValue = objectMapper.writeValueAsString(postSearch);

		// expected
		mockMvc.perform(get("/api/v1/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonValue)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.errorMessage").value("잘못된 요청입니다."))
			.andExpect(jsonPath("$.validationList[0].errorField").value("pageSize"))
			.andExpect(jsonPath("$.validationList[0].errorFieldMessage").value("조회 가능한 게시글 수가 아닙니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("게시글 리스트 페이징 validation 테스트(기존 존재 enum 일 때)")
	void getPostListValidationExistedEnumTest() throws Exception {
		// given
		List<Post> postRequestList = IntStream.range(1, 31)
			.mapToObj(
				i -> Post.builder()
					.title("제목" + i)
					.content("내용" + i)
					.build()
			).toList();

		postRepository.saveAll(postRequestList);

		PostSearch postSearch = PostSearch.builder()
			.pageNumber(1)
			.pageSize("PAGE_SIZE_5")
			.pageSort("")
			.sortParam("SORT_BY_DATE")
			.build();

		String jsonValue = objectMapper.writeValueAsString(postSearch);

		// expected
		mockMvc.perform(get("/api/v1/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonValue)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.errorMessage").value("잘못된 요청입니다."))
			.andExpect(jsonPath("$.validationList[0].errorField").value("pageSort"))
			.andExpect(jsonPath("$.validationList[0].errorFieldMessage").value("오름차순, 내림차순만 가능합니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("게시글 리스트 페이징 validation 테스트(null)")
	void getPostListValidationNullTest() throws Exception {
		// given
		List<Post> postRequestList = IntStream.range(1, 31)
			.mapToObj(
				i -> Post.builder()
					.title("제목" + i)
					.content("내용" + i)
					.build()
			).toList();

		postRepository.saveAll(postRequestList);

		PostSearch postSearch = PostSearch.builder()
			.pageNumber(1)
			.pageSize("PAGE_SIZE_5")
			.pageSort("ASC")
			.sortParam(null)
			.build();

		String jsonValue = objectMapper.writeValueAsString(postSearch);

		// expected
		mockMvc.perform(get("/api/v1/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonValue)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.errorMessage").value("잘못된 요청입니다."))
			.andExpect(jsonPath("$.validationList[0].errorField").value("sortParam"))
			.andExpect(jsonPath("$.validationList[0].errorFieldMessage").value("등록순, 조회수, 좋아요 순만 정렬 가능합니다."))
			.andDo(print());
	}
}