package com.mysite.sbb.review;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mysite.sbb.classes.Classes;
import com.mysite.sbb.classes.ClassesService;
import com.mysite.sbb.user.User;
import com.mysite.sbb.user.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ClassesService classesService;
    private final UserService userService;

    // 리뷰 작성
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{classesId}")
    public String createReview(@PathVariable("classesId") Long classesId,
                               @RequestParam("rating") int rating,
                               @RequestParam("content") String content,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        try {
            Classes classes = classesService.getClassById(classesId);
            User user = userService.getUser(principal.getName());
            
            reviewService.create(classes, user, rating, content);
            redirectAttributes.addFlashAttribute("successMsg", "리뷰가 등록되었습니다.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "리뷰 등록에 실패했습니다.");
        }
        
        return "redirect:/classes/" + classesId;
    }

    // 리뷰 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/edit/{reviewId}")
    public String editReview(@PathVariable("reviewId") Long reviewId,
                             @RequestParam("rating") int rating,
                             @RequestParam("content") String content,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {
        Review review = reviewService.getReview(reviewId);
        User user = userService.getUser(principal.getName());
        
        // 작성자 확인
        if (!review.getUser().getUno().equals(user.getUno())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }
        
        try {
            reviewService.update(review, rating, content);
            redirectAttributes.addFlashAttribute("successMsg", "리뷰가 수정되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "리뷰 수정에 실패했습니다.");
        }
        
        return "redirect:/classes/" + review.getClasses().getClassesId();
    }

    // 리뷰 삭제
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{reviewId}")
    public String deleteReview(@PathVariable("reviewId") Long reviewId,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        Review review = reviewService.getReview(reviewId);
        User user = userService.getUser(principal.getName());
        Long classesId = review.getClasses().getClassesId();
        
        // 작성자 또는 관리자 확인
        if (!review.getUser().getUno().equals(user.getUno()) && 
            !user.getRole().name().equals("ROLE_ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");
        }
        
        reviewService.delete(review);
        redirectAttributes.addFlashAttribute("successMsg", "리뷰가 삭제되었습니다.");
        
        return "redirect:/classes/" + classesId;
    }
}
