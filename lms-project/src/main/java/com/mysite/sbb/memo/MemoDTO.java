package com.mysite.sbb.memo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemoDTO {
    private Long memoId;
    private String content;
    private Integer videoTimestamp;
    private String formattedTimestamp;
    private String createdDate;
    private String modifiedDate;
    
    @JsonProperty("isModified")
    private boolean modified;
    
    public MemoDTO(Memo memo) {
        this.memoId = memo.getMemoId();
        this.content = memo.getContent();
        this.videoTimestamp = memo.getVideoTimestamp();
        this.formattedTimestamp = formatTimestamp(memo.getVideoTimestamp());
        this.createdDate = memo.getCreatedDate() != null ? 
            memo.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "";
        this.modifiedDate = memo.getModifiedDate() != null ? 
            memo.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "";
        
        // 수정 여부 판단 (생성일과 수정일이 다르면 수정됨)
        this.modified = memo.getModifiedDate() != null && 
                        memo.getCreatedDate() != null &&
                        !isSameTime(memo.getCreatedDate(), memo.getModifiedDate());
    }
    
    private boolean isSameTime(LocalDateTime created, LocalDateTime modified) {
        // 1초 이내 차이는 동일 시간으로 간주
        return Math.abs(java.time.Duration.between(created, modified).getSeconds()) <= 1;
    }
    
    private String formatTimestamp(Integer seconds) {
        if (seconds == null) return "00:00";
        int hours = seconds / 3600;
        int mins = (seconds % 3600) / 60;
        int secs = seconds % 60;
        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, mins, secs);
        }
        return String.format("%02d:%02d", mins, secs);
    }
}
