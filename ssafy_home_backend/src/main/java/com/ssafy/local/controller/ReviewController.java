package com.ssafy.local.controller;

import com.ssafy.local.dto.ReviewDto;
import com.ssafy.local.oauth.CustomOAuth2User;
import com.ssafy.local.service.FileStorageService;
import com.ssafy.local.service.ReviewService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final FileStorageService fileStorageService;
	

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createReview(
            Principal principal,
            @RequestParam("apt_id")  String aptId,
            @RequestParam("score")   int score,
            @RequestParam("content") String content,
            @RequestPart(name = "image", required = false) MultipartFile image
    ) {
        String userId = principal.getName();
        String imageUrl = null;
        try {
            if (image != null && !image.isEmpty()) {
                imageUrl = fileStorageService.store(image);
            }
            ReviewDto dto = ReviewDto.builder()
                .user_id(userId)
                .apt_id(aptId)
                .score(score)
                .content(content)
                .image_file(imageUrl)
                .build();

            boolean ok = reviewService.createReview(dto);
            return ok
                ? ResponseEntity.ok("Review created")
                : ResponseEntity.status(500).body("Failed to create review");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
    // 특정 아파트에 대한 모든 리뷰 조회 API
    @GetMapping("/{apartmentId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByApartmentId(@PathVariable String apartmentId) {
        List<ReviewDto> reviews = reviewService.getReviewsByApartmentId(apartmentId);
        if (reviews != null && !reviews.isEmpty()) {
            return ResponseEntity.ok(reviews);
        }
        return ResponseEntity.status(404).body(null); // 리뷰가 없는 경우 404
    }

    // 리뷰 삭제 API
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable int reviewId,
            Principal principal) {

        boolean ok = reviewService.deleteReview(reviewId, principal.getName());
        if (ok) {
            return ResponseEntity.ok("삭제 성공");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body("삭제 권한이 없습니다.");
        }
    }
}