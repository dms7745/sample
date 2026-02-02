package com.mysite.sbb;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.mysite.sbb.quiz.Quiz;
import com.mysite.sbb.quiz.QuizRepository;
import com.mysite.sbb.quiz_attempt.QuizAttempt;
import com.mysite.sbb.quiz_attempt.QuizAttemptService;
import com.mysite.sbb.user.User;
import com.mysite.sbb.user.UserRole;
import com.mysite.sbb.user.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final QuizRepository quizRepository;
    private final QuizAttemptService quizAttemptService;

    // Static resources - 완전히 Security 무시
    @Bean
    @Order(0)
    SecurityFilterChain staticResourcesSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/css/**", "/js/**", "/images/**", "/bootstrap.css", "/bootstrap.js", "/*.css", "/*.js", "/static/**")
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.disable())
            .sessionManagement(session -> session.disable())
            .securityContext(context -> context.disable())
            .requestCache(cache -> cache.disable());
        return http.build();
    }

    @Bean
    @Order(1)
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // 공개 페이지
                .requestMatchers("/", "/mainpage", "/user/**", "/h2-console/**").permitAll()
                // classes 관련 공개 페이지
                .requestMatchers("/classes/list", "/classes/list/**").permitAll()
                .requestMatchers("/classes/instructor/**").permitAll()
                .requestMatchers("/classes/{classesId}").permitAll()
                // 나머지는 인증 필요
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
            .headers(headers -> headers.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
            .formLogin(formLogin -> formLogin
                .loginPage("/user/login")
                .usernameParameter("userid")
                .successHandler(customAuthenticationSuccessHandler())
                .failureHandler((request, response, exception) -> {
                    String errorMessage;
                    if (exception.getMessage().contains("관리자 승인")) {
                        errorMessage = "관리자 승인 시 로그인 및 이용 가능합니다.";
                    } else {
                        errorMessage = "아이디 또는 비밀번호를 확인하세요.";
                    }
                    request.getSession().setAttribute("errorMsg", errorMessage);
                    new DefaultRedirectStrategy().sendRedirect(request, response, "/user/login?error=true");
                })
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true));
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String userId = authentication.getName();
            User user = userService.getUser(userId);
            RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

            if (user.getRole() == UserRole.ROLE_INSTRUCTOR || user.getRole() == UserRole.ROLE_ADMIN) {
                redirectStrategy.sendRedirect(request, response, "/mainpage");
                return;
            }

            if (user.getRole() == UserRole.ROLE_LEARNER) {
                try {
                    Optional<Quiz> levelTestQuizOpt = quizRepository.findByQuizType("LEVEL_TEST");
                    if (levelTestQuizOpt.isPresent()) {
                        Quiz levelTestQuiz = levelTestQuizOpt.get();
                        Optional<QuizAttempt> attemptOpt = quizAttemptService.findLatestAttemptByUserAndQuiz(user, levelTestQuiz);
                        if (attemptOpt.isPresent() && attemptOpt.get().getScore() == 0) {
                            redirectStrategy.sendRedirect(request, response, "/quiz_attempt/exam/" + attemptOpt.get().getAttemptId());
                            return;
                        }
                    }
                } catch (Exception e) {
                    // ignore
                }
            }
            redirectStrategy.sendRedirect(request, response, "/mainpage");
        };
    }

    @Bean
    AuthenticationManager am(AuthenticationConfiguration ac) throws Exception {
        return ac.getAuthenticationManager();
    }
}
