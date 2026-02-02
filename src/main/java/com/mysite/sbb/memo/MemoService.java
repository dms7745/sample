package com.mysite.sbb.memo;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.classes.Classes;
import com.mysite.sbb.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;

    // 메모 생성
    @Transactional
    public Memo create(Classes classes, User user, String content, Integer videoTimestamp) {
        Memo memo = new Memo();
        memo.setClasses(classes);
        memo.setUser(user);
        memo.setContent(content);
        memo.setVideoTimestamp(videoTimestamp);
        
        return memoRepository.save(memo);
    }

    // 메모 조회 (ID)
    public Memo getMemo(Long memoId) {
        return memoRepository.findById(memoId)
                .orElseThrow(() -> new DataNotFoundException("메모를 찾을 수 없습니다."));
    }

    // 특정 사용자가 특정 강의에 작성한 메모 조회 (최신순)
    public List<Memo> getMemosByClassesAndUser(Classes classes, User user) {
        return memoRepository.findByClassesAndUserOrderByCreatedDateDesc(classes, user);
    }

    // 특정 사용자가 특정 강의에 작성한 메모 조회 (타임스탬프순)
    public List<Memo> getMemosByClassesAndUserByTimestamp(Classes classes, User user) {
        return memoRepository.findByClassesAndUserOrderByVideoTimestampAsc(classes, user);
    }

    // 특정 사용자가 작성한 모든 메모
    public List<Memo> getMemosByUser(User user) {
        return memoRepository.findByUserOrderByCreatedDateDesc(user);
    }

    // 메모 수정 (내용만 수정, 영상 시간은 고정)
    @Transactional
    public Memo update(Memo memo, String content) {
        memo.setContent(content);
        // videoTimestamp는 수정하지 않음 (고정)
        return memoRepository.save(memo);
    }

    // 메모 삭제
    @Transactional
    public void delete(Memo memo) {
        memoRepository.delete(memo);
    }

    // 특정 강의의 특정 사용자 메모 개수
    public long getMemoCount(Classes classes, User user) {
        return memoRepository.countByClassesAndUser(classes, user);
    }
}
