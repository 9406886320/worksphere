package com.worksphere.department.mapper;

import com.worksphere.department.dto.DepartmentRequest;
import com.worksphere.department.dto.DepartmentResponse;
import com.worksphere.department.entity.Department;

public class DepartmentMapper {

    private DepartmentMapper() {
    }

    public static Department toEntity(DepartmentRequest request) {

        Department department = new Department();

        department.setDepartmentName(request.departmentName());
        department.setDepartmentCode(request.departmentCode());
        department.setDepartmentHead(request.departmentHead());
        department.setLocation(request.location());

        return department;
    }

    public static DepartmentResponse toResponse(Department department) {

        return new DepartmentResponse(
                department.getId(),
                department.getDepartmentName(),
                department.getDepartmentCode(),
                department.getDepartmentHead(),
                department.getLocation()
        );
    }

    public static void updateEntity(
            Department department,
            DepartmentRequest request) {

        department.setDepartmentName(request.departmentName());
        department.setDepartmentCode(request.departmentCode());
        department.setDepartmentHead(request.departmentHead());
        department.setLocation(request.location());
    }
}