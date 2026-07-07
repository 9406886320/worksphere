package com.worksphere.employee.dto;

public record EmployeeResponse(

        Long id,
        String firstName,
        String lastName,
        String email,
        Double salary

) {
}