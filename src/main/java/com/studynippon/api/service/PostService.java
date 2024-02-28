package com.studynippon.api.service;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.studynippon.api.dto.request.PostCreate;
import com.studynippon.api.dto.request.PostSearch;
import com.studynippon.api.dto.response.PageResponse;
import com.studynippon.api.dto.response.PostDetail;
import com.studynippon.api.entity.Post;
import com.studynippon.api.exception.PostNotFound;
import com.studynippon.api.repository.PostRepository;
import com.studynippon.api.validation.enums.PageSize;
import com.studynippon.api.validation.enums.SortParam;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;

	/**
	 * 게시글 작성 메서드
	 */
	public ResponseEntity<Void> writePost(PostCreate postCreate) {

		Post post = Post.builder()
			.title(postCreate.getTitle())
			.content(postCreate.getContent())
			.build();

		postRepository.save(post);

		return ResponseEntity
			.status(CREATED)
			.build();
	}

	/**
	 * 게시글 단일 조회 메서드
	 */
	public ResponseEntity<PostDetail> getPost(Long postId) {

		Post post = getPostById(postId);

		PostDetail postDetail = PostDetail.builder()
			.title(post.getTitle())
			.content(post.getContent())
			.build();

		return ResponseEntity
			.status(OK)
			.body(postDetail);
	}

	/**
	 * 게시글 삭제 메서드
	 */
	public ResponseEntity<Void> deletePost(Long postId) {

		Post post = getPostById(postId);

		postRepository.delete(post);

		return ResponseEntity
			.status(OK)
			.build();
	}

	// post find 메서드
	private Post getPostById(Long postId) {
		return postRepository.findById(postId).orElseThrow(PostNotFound::new);
	}

	/**
	 * 게시글 페이징 조회 메서드
	 */
	public ResponseEntity<PageResponse> getPostList(PostSearch postSearch) {

		// variables for pageable
		int pageNumber = Math.max(postSearch.getPageNumber() - 1, 0); // pageable index 0부터 시작
		int pageSize = PageSize.valueOf(postSearch.getPageSize()).getSizeValue();
		Sort.Direction sortValue = Sort.Direction.valueOf(postSearch.getPageSort());
		String sortParam = SortParam.valueOf(postSearch.getSortParam()).getSortParamValue();

		// pageable by PageRequest
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortValue, sortParam));

		// page
		Page<Post> page = postRepository.findAll(pageable);

		// postList from page
		List<PostDetail> postList = page.getContent().stream().map(
			post -> PostDetail.builder()
				.title(post.getTitle())
				.content(post.getContent())
				.build()).toList();

		// page to dto
		PageResponse pageResponse = PageResponse.builder()
			.firstPage(page.isFirst())
			.lastPage(page.isLast())
			.totalPages(page.getTotalPages())
			.postDtoList(postList)
			.build();

		return ResponseEntity
			.status(OK)
			.body(pageResponse);
	}
}
