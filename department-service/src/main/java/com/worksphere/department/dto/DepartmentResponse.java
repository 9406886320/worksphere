package com.worksphere.department.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Department Response")
public record DepartmentResponse(

        Long id,

        @Schema(example = "Engineering")
        String departmentName,

        @Schema(example = "ENG001")
        String departmentCode,

        @Schema(example = "Sakshi Agrawal")
        String departmentHead,

        @Schema(example = "Indore")
        String location
) {
}