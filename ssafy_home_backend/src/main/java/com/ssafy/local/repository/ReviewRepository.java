package com.ssafy.local.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.local.dto.ReviewDto;

@Mapper
public interface ReviewRepository {
    
    // 리뷰 생성
    int createReview(ReviewDto reviewDto) throws Exception;
    
    // 아파트 ID에 해당하는 모든 리뷰 조회
    List<ReviewDto> getReviewsByAptId(String aptId);
    
    // 리뷰 삭제 (리뷰 ID로 삭제)
    int deleteReviewById(int reviewId);
    
    String findUserIdByReviewId(int reviewId);

}
