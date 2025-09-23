package com.lopezcampos.dto.response;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ApiErrorResponse {
    @Builder.Default
    private boolean success = false;
    private String message;
    private int errorType;
    private List<String> validations;
}
