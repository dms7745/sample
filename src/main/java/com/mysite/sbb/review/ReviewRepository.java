package com.mysite.sbb.review;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mysite.sbb.classes.Classes;
import com.mysite.sbb.user.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 특정 강의의 모든 리뷰 조회 (최신순)
    List<Review> findByClassesOrderByCreatedDateDesc(Classes classes);
    
    // 특정 강의의 리뷰 페이징 조회
    Page<Review> findByClasses(Classes classes, Pageable pageable);
    
    // 특정 사용자가 특정 강의에 작성한 리뷰 조회 (중복 방지용)
    Optional<Review> findByClassesAndUser(Classes classes, User user);
    
    // 특정 사용자가 작성한 모든 리뷰 조회
    List<Review> findByUserOrderByCreatedDateDesc(User user);
    
    // 특정 강의의 평균 별점 계산
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.classes = :classes")
    Double getAverageRatingByClasses(@Param("classes") Classes classes);
    
    // 특정 강의의 리뷰 개수
    long countByClasses(Classes classes);
    
    // 특정 사용자가 특정 강의에 리뷰를 작성했는지 확인
    boolean existsByClassesAndUser(Classes classes, User user);
}
