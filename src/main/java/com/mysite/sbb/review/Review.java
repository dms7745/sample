package com.mysite.sbb.review;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.mysite.sbb.classes.Classes;
import com.mysite.sbb.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "review",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"classes_id", "uno"}) // 한 강의당 한 리뷰만
       })
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "classes_id", nullable = false)
    private Classes classes; // FK: 강의

    @ManyToOne
    @JoinColumn(name = "uno", nullable = false)
    private User user; // FK: 작성자

    @Column(name = "rating", nullable = false)
    private int rating; // 별점 (1~5)

    @Column(name = "content", columnDefinition = "TEXT")
    private String content; // 리뷰 내용

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate; // 작성일

    @UpdateTimestamp
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate; // 수정일
}
