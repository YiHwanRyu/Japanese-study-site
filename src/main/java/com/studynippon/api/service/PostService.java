package com.studynippon.api.service;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.studynippon.api.domain.Post;
import com.studynippon.api.dto.request.PostCreate;
import com.studynippon.api.dto.response.PostDetail;
import com.studynippon.api.repository.PostRepository;

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

		Post post = postRepository.findById(postId).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
		);

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
}
