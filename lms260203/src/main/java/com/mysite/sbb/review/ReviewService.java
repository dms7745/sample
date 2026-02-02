package com.mysite.sbb.review;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.classes.Classes;
import com.mysite.sbb.enrollment.Enrollment;
import com.mysite.sbb.enrollment.EnrollmentRepository;
import com.mysite.sbb.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final EnrollmentRepository enrollmentRepository;

    // 리뷰 생성
    @Transactional
    public Review create(Classes classes, User user, int rating, String content) {
        // 수강 완료 여부 확인
        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByClassesAndUser(classes, user);
        if (enrollmentOpt.isEmpty() || !enrollmentOpt.get().isCompleted()) {
            throw new IllegalStateException("수강을 완료한 강의에만 리뷰를 작성할 수 있습니다.");
        }
        
        // 이미 리뷰를 작성했는지 확인
        if (reviewRepository.existsByClassesAndUser(classes, user)) {
            throw new IllegalStateException("이미 이 강의에 리뷰를 작성하셨습니다.");
        }
        
        // 별점 유효성 검사
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("별점은 1~5 사이여야 합니다.");
        }
        
        Review review = new Review();
        review.setClasses(classes);
        review.setUser(user);
        review.setRating(rating);
        review.setContent(content);
        
        return reviewRepository.save(review);
    }

    // 리뷰 조회 (ID)
    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new DataNotFoundException("리뷰를 찾을 수 없습니다."));
    }

    // 특정 강의의 모든 리뷰 조회
    public List<Review> getReviewsByClasses(Classes classes) {
        return reviewRepository.findByClassesOrderByCreatedDateDesc(classes);
    }

    // 특정 강의의 리뷰 페이징 조회
    public Page<Review> getReviewsByClasses(Classes classes, Pageable pageable) {
        return reviewRepository.findByClasses(classes, pageable);
    }

    // 특정 사용자의 특정 강의 리뷰 조회
    public Optional<Review> getReviewByClassesAndUser(Classes classes, User user) {
        return reviewRepository.findByClassesAndUser(classes, user);
    }

    // 특정 사용자가 작성한 모든 리뷰
    public List<Review> getReviewsByUser(User user) {
        return reviewRepository.findByUserOrderByCreatedDateDesc(user);
    }

    // 리뷰 수정
    @Transactional
    public Review update(Review review, int rating, String content) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("별점은 1~5 사이여야 합니다.");
        }
        review.setRating(rating);
        review.setContent(content);
        return reviewRepository.save(review);
    }

    // 리뷰 삭제
    @Transactional
    public void delete(Review review) {
        reviewRepository.delete(review);
    }

    // 특정 강의의 평균 별점
    public Double getAverageRating(Classes classes) {
        Double avg = reviewRepository.getAverageRatingByClasses(classes);
        return avg != null ? Math.round(avg * 10) / 10.0 : 0.0;
    }

    // 특정 강의의 리뷰 개수
    public long getReviewCount(Classes classes) {
        return reviewRepository.countByClasses(classes);
    }

    // 사용자가 해당 강의에 리뷰를 작성할 수 있는지 확인
    public boolean canWriteReview(Classes classes, User user) {
        if (user == null) return false;
        
        // 수강 완료 여부 확인
        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByClassesAndUser(classes, user);
        if (enrollmentOpt.isEmpty() || !enrollmentOpt.get().isCompleted()) {
            return false;
        }
        
        // 이미 리뷰 작성 여부
        return !reviewRepository.existsByClassesAndUser(classes, user);
    }
}
