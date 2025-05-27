package com.ssafy.local.repository;

import com.ssafy.local.dto.CertificationDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CertificationRepository {
    // 모든 인증 요청 조회
    List<CertificationDto> selectAllRequests();

    // 인증 상태 업데이트
    int updateApproval(
        @Param("certification_id") String certificationId,
        @Param("approval") String approval
    );
    // 특정 사용자·아파트의 인증 요청 조회
    CertificationDto selectByUserAndApt(@Param("user_id") String userId,
                                        @Param("apt_seq")  String aptSeq);
    
    
    int insertRequest(CertificationDto dto);
    
    CertificationDto selectById(@Param("certification_id") String certificationId);
}