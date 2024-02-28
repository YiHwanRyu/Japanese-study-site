package com.studynippon.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studynippon.api.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
