package com.studynippon.api.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.studynippon.api.dto.request.PostCreate;
import com.studynippon.api.dto.request.PostSearch;
import com.studynippon.api.dto.response.PageResponse;
import com.studynippon.api.dto.response.PostDetail;
import com.studynippon.api.entity.Post;
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

	@Test
	@DisplayName("게시글 첫 페이지, 5개씩 등록기준 내림차순 조회 검증")
	void getPostListFirstPageTest() {

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
			.pageNumber(0)
			.pageSize("PAGE_SIZE_5")
			.pageSort("DESC")
			.sortParam("SORT_BY_DATE")
			.build();

		// when
		PageResponse pageResponse = postService.getPostList(postSearch).getBody();

		// then
		assertThat(pageResponse).isNotNull();
		assertThat(pageResponse.getPostDtoList()).hasSize(5);
		assertThat(pageResponse.getPostDtoList().get(0).getTitle()).isEqualTo("제목30");
		assertThat(pageResponse.getPostDtoList().get(0).getContent()).isEqualTo("내용30");
		assertThat(pageResponse.getTotalPages()).isEqualTo(30 / 5);
		assertThat(pageResponse.isFirstPage()).isTrue();
		assertThat(pageResponse.isLastPage()).isFalse();

	}

	@Test
	@DisplayName("게시글 마지막 페이지, 10개씩 등록기준 오름차순 조회 검증")
	void getPostListLastPageTest() {

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
			.pageNumber(3)
			.pageSize("PAGE_SIZE_10")
			.pageSort("ASC")
			.sortParam("SORT_BY_DATE")
			.build();

		// when
		PageResponse pageResponse = postService.getPostList(postSearch).getBody();

		// then
		assertThat(pageResponse).isNotNull();
		assertThat(pageResponse.getPostDtoList()).hasSize(10);
		assertThat(pageResponse.getPostDtoList().get(0).getTitle()).isEqualTo("제목21");
		assertThat(pageResponse.getPostDtoList().get(0).getContent()).isEqualTo("내용21");
		assertThat(pageResponse.getTotalPages()).isEqualTo(30 / 10);
		assertThat(pageResponse.isFirstPage()).isFalse();
		assertThat(pageResponse.isLastPage()).isTrue();

	}

}