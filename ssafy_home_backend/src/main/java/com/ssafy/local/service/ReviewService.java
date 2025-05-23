package com.ssafy.local.service;

import com.ssafy.local.dto.ReviewDto;

import java.util.List;

public interface ReviewService {

	// 리뷰 생성
	boolean createReview(ReviewDto reviewDto);

	// 특정 아파트에 대한 모든 리뷰 조회
	List<ReviewDto> getReviewsByApartmentId(String apartmentId);

	// 리뷰 삭제 (리뷰 ID + 요청자 ID)
	boolean deleteReview(int reviewId, String requesterUserId);
}
