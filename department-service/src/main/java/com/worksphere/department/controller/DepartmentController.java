package com.worksphere.department.controller;

import com.worksphere.department.dto.DepartmentPageResponse;
import com.worksphere.department.dto.DepartmentRequest;
import com.worksphere.department.dto.DepartmentResponse;
import com.worksphere.department.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Operation(
            summary = "Create Department",
            description = "Creates a new department."
    )
    @PostMapping
    public DepartmentResponse createDepartment(
            @Valid @RequestBody DepartmentRequest request) {

        return departmentService.createDepartment(request);
    }

    @Operation(
            summary = "Get Department By Id",
            description = "Returns a department by id."
    )
    @GetMapping("/{id}")
    public DepartmentResponse getDepartmentById(
            @PathVariable Long id) {

        return departmentService.getDepartmentById(id);
    }

    @Operation(
            summary = "Get All Departments",
            description = "Returns paginated list of departments."
    )
    @GetMapping
    public DepartmentPageResponse getAllDepartments(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size,

            @RequestParam(defaultValue = "departmentName")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String sortDir) {

        return departmentService.getAllDepartments(
                page,
                size,
                sortBy,
                sortDir);
    }

    @Operation(
            summary = "Update Department",
            description = "Updates an existing department."
    )
    @PutMapping("/{id}")
    public DepartmentResponse updateDepartment(

            @PathVariable Long id,

            @Valid
            @RequestBody
            DepartmentRequest request) {

        return departmentService.updateDepartment(id, request);
    }

    @Operation(
            summary = "Delete Department",
            description = "Deletes a department."
    )
    @DeleteMapping("/{id}")
    public void deleteDepartment(
            @PathVariable Long id) {

        departmentService.deleteDepartment(id);
    }
}