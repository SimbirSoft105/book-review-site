package com.simbirsoft.practice.bookreviewsite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewAdditionReturnDTO {

    private Long id;
    private LocalDateTime createdAt;
    private float rate;

}
