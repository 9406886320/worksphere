package com.worksphere.department.service;

import com.worksphere.department.dto.DepartmentPageResponse;
import com.worksphere.department.dto.DepartmentRequest;
import com.worksphere.department.dto.DepartmentResponse;

public interface DepartmentService {

    DepartmentResponse createDepartment(DepartmentRequest request);

    DepartmentResponse getDepartmentById(Long id);

    DepartmentPageResponse getAllDepartments(
            int page,
            int size,
            String sortBy,
            String sortDir
    );

    DepartmentResponse updateDepartment(
            Long id,
            DepartmentRequest request
    );

    void deleteDepartment(Long id);
}