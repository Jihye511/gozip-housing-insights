package com.ssafy.local.controller;

import com.ssafy.local.dto.ReviewDto;
import com.ssafy.local.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 리뷰 생성 API (이미지 base64 포함)
    @PostMapping
    public ResponseEntity<String> createReview(@RequestBody ReviewDto reviewDto) {
        boolean isCreated = reviewService.createReview(reviewDto);
        if (isCreated) {
            return ResponseEntity.ok("Review created successfully");
        }
        return ResponseEntity.status(500).body("Failed to create review");
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
    public ResponseEntity<String> deleteReview(@PathVariable int reviewId) {
        boolean isDeleted = reviewService.deleteReview(reviewId);
        if (isDeleted) {
            return ResponseEntity.ok("Review deleted successfully");
        }
        return ResponseEntity.status(500).body("Failed to delete review");
    }
}
