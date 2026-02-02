package com.mysite.sbb.memo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.sbb.classes.Classes;
import com.mysite.sbb.user.User;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    // 특정 사용자가 특정 강의에 작성한 메모 조회 (최신순)
    List<Memo> findByClassesAndUserOrderByCreatedDateDesc(Classes classes, User user);
    
    // 특정 사용자가 특정 강의에 작성한 메모 조회 (타임스탬프순)
    List<Memo> findByClassesAndUserOrderByVideoTimestampAsc(Classes classes, User user);
    
    // 특정 사용자가 작성한 모든 메모 조회
    List<Memo> findByUserOrderByCreatedDateDesc(User user);
    
    // 특정 강의의 특정 사용자 메모 개수
    long countByClassesAndUser(Classes classes, User user);
}
