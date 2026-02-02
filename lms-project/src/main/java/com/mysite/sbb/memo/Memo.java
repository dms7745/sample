package com.mysite.sbb.memo;

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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "memo")
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private Long memoId;

    @ManyToOne
    @JoinColumn(name = "classes_id", nullable = false)
    private Classes classes; // FK: 강의

    @ManyToOne
    @JoinColumn(name = "uno", nullable = false)
    private User user; // FK: 작성자

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content; // 메모 내용

    @Column(name = "video_timestamp")
    private Integer videoTimestamp; // 영상 타임스탬프 (초 단위, 선택)

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate; // 작성일

    @UpdateTimestamp
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate; // 수정일
}
