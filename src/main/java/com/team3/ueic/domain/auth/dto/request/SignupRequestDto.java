package com.team3.ueic.domain.auth.dto.request;

import com.team3.ueic.domain.user.entity.AvailableTime;
import com.team3.ueic.domain.user.entity.PreferredMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SignupRequestDto {

    @Email(message = "올바른 이메일 형식이어야 합니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String passwordConfirm;

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수입니다.")
    private String phoneNumber;

    @NotNull(message = "선호 학습 방식은 필수입니다.")
    private PreferredMode preferredMode;

    @NotEmpty(message = "가능 시간대는 최소 1개 이상 선택해야 합니다.")
    private List<AvailableTime> availableTimes;

    @NotNull(message = "목표 점수는 필수입니다.")
    private Integer targetScore;
}
