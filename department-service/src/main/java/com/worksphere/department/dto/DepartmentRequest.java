package com.worksphere.department.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record DepartmentRequest(

        @Schema(
                description = "Department Name",
                example = "Engineering"
        )
        @NotBlank(message = "Department name is required")
        String departmentName,

        @Schema(
                description = "Department Code",
                example = "ENG001"
        )
        @NotBlank(message = "Department code is required")
        String departmentCode,

        @Schema(
                description = "Department Head",
                example = "Sakshi Agrawal"
        )
        @NotBlank(message = "Department head is required")
        String departmentHead,

        @Schema(
                description = "Department Location",
                example = "Indore"
        )
        @NotBlank(message = "Department location is required")
        String location
) {
}