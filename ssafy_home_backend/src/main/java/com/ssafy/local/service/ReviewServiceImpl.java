package com.ssafy.local.service;

import com.ssafy.local.dto.ReviewDto;
import com.ssafy.local.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repo;

    // 리뷰 생성
    @Override
    public boolean createReview(ReviewDto reviewDto) {
        try {
            int result = repo.createReview(reviewDto);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 특정 아파트에 대한 모든 리뷰 조회
    @Override
    public List<ReviewDto> getReviewsByApartmentId(String apartmentId) {
        return repo.getReviewsByAptId(apartmentId);
    }

    // 리뷰 삭제
    @Override
    public boolean deleteReview(int reviewId) {
        try {
            int result = repo.deleteReviewById(reviewId);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
