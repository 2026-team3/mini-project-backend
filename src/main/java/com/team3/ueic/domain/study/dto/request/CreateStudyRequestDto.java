package com.team3.ueic.domain.study.dto.request;

import com.team3.ueic.domain.user.entity.AvailableTime;
import com.team3.ueic.domain.user.entity.PreferredMode;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreateStudyRequestDto {

    @NotBlank(message = "스터디 이름은 필수입니다.")
    private String studyName;

    @NotNull(message = "온/오프라인 여부는 필수입니다.")
    private PreferredMode preferredMode;

    @NotNull(message = "최대 모집 인원은 필수입니다.")
    @Min(value = 2, message = "최대 모집 인원은 2명 이상이어야 합니다.")
    @Max(value = 20, message = "최대 모집 인원은 20명 이하여야 합니다.")
    private Integer maxMembers;

    @NotNull(message = "목표 점수는 필수입니다.")
    private Integer targetScore;

    @NotEmpty(message = "가능 시간대는 최소 1개 이상 선택해야 합니다.")
    private List<AvailableTime> availableTimes;

    @NotBlank(message = "스터디 스타일 설명은 필수입니다.")
    private String studyStyleDescription;
}
