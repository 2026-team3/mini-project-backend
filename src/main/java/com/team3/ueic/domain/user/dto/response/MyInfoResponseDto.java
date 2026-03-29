package com.team3.ueic.domain.user.dto.response;

import com.team3.ueic.domain.test.enums.WeakType;
import com.team3.ueic.domain.user.entity.PreferredMode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyInfoResponseDto {

    private Long userId;
    private String email;
    private String name;
    private String phoneNumber;

    private Integer targetScore;
    private WeakType weakType;
    private PreferredMode preferredMode;
}
