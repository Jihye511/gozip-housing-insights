package com.ssafy.local.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificationDto {
    private Integer certification_id;
    private String user_id;
    private String pdf_file;
    private String apt_seq;
    private String approval;
}
