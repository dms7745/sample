package com.mysite.sbb.memo;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.sbb.classes.Classes;
import com.mysite.sbb.classes.ClassesService;
import com.mysite.sbb.user.User;
import com.mysite.sbb.user.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/memo")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;
    private final ClassesService classesService;
    private final UserService userService;

    // 메모 목록 조회 (AJAX용) - DTO로 반환
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list/{classesId}")
    @ResponseBody
    public ResponseEntity<List<MemoDTO>> getMemoList(@PathVariable("classesId") Long classesId,
                                                      Principal principal) {
        Classes classes = classesService.getClassById(classesId);
        User user = userService.getUser(principal.getName());
        
        List<Memo> memos = memoService.getMemosByClassesAndUser(classes, user);
        List<MemoDTO> memoDTOs = memos.stream()
                .map(MemoDTO::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(memoDTOs);
    }

    // 메모 생성 (AJAX용)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{classesId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createMemo(@PathVariable("classesId") Long classesId,
                                                          @RequestBody Map<String, Object> request,
                                                          Principal principal) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Classes classes = classesService.getClassById(classesId);
            User user = userService.getUser(principal.getName());
            
            String content = (String) request.get("content");
            Integer videoTimestamp = request.get("videoTimestamp") != null ? 
                    Integer.parseInt(request.get("videoTimestamp").toString()) : null;
            
            Memo memo = memoService.create(classes, user, content, videoTimestamp);
            
            response.put("success", true);
            response.put("memoId", memo.getMemoId());
            response.put("memo", new MemoDTO(memo));
            response.put("message", "메모가 저장되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "메모 저장에 실패했습니다: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 메모 수정 (AJAX용) - 내용만 수정, 영상 시간은 고정
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/edit/{memoId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> editMemo(@PathVariable("memoId") Long memoId,
                                                        @RequestBody Map<String, Object> request,
                                                        Principal principal) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Memo memo = memoService.getMemo(memoId);
            User user = userService.getUser(principal.getName());
            
            // 작성자 확인
            if (!memo.getUser().getUno().equals(user.getUno())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
            }
            
            String content = (String) request.get("content");
            // videoTimestamp는 수정하지 않음 (고정)
            
            memoService.update(memo, content);
            
            response.put("success", true);
            response.put("message", "메모가 수정되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "메모 수정에 실패했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 메모 삭제 (AJAX용)
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete/{memoId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteMemo(@PathVariable("memoId") Long memoId,
                                                          Principal principal) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Memo memo = memoService.getMemo(memoId);
            User user = userService.getUser(principal.getName());
            
            // 작성자 확인
            if (!memo.getUser().getUno().equals(user.getUno())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");
            }
            
            memoService.delete(memo);
            
            response.put("success", true);
            response.put("message", "메모가 삭제되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "메모 삭제에 실패했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
