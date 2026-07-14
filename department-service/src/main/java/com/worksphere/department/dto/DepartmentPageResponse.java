package com.worksphere.department.dto;

import java.util.List;

public record DepartmentPageResponse(

        List<DepartmentResponse> content,

        int pageNumber,

        int pageSize,

        long totalElements,

        int totalPages,

        boolean last
) {
}