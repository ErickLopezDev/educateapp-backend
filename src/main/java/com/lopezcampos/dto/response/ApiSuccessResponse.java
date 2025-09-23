package com.lopezcampos.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ApiSuccessResponse<T> {
    @Builder.Default
    private boolean success = true;
    private String message;
    private T data;
}
