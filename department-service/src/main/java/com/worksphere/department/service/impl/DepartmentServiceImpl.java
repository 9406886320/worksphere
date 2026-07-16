package com.worksphere.department.service.impl;

import com.worksphere.common.exception.ResourceNotFoundException;
import com.worksphere.department.dto.DepartmentPageResponse;
import com.worksphere.department.dto.DepartmentResponse;
import com.worksphere.department.entity.Department;
import com.worksphere.department.mapper.DepartmentMapper;
import com.worksphere.department.repository.DepartmentRepository;
import com.worksphere.department.service.DepartmentService;
import org.springframework.stereotype.Service;
import com.worksphere.department.dto.DepartmentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest request) {

        Department department = DepartmentMapper.toEntity(request);

        Department savedDepartment = departmentRepository.save(department);

        return DepartmentMapper.toResponse(savedDepartment);
    }
    @Override
    public DepartmentResponse getDepartmentById(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->  new ResourceNotFoundException(
                        "Department",
                        "id",
                        id
                ));

        return DepartmentMapper.toResponse(department);
    }

    @Override
    public DepartmentPageResponse getAllDepartments(
            int page,
            int size,
            String sortBy,
            String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Department> departmentPage = departmentRepository.findAll(pageable);

        List<DepartmentResponse> departments = departmentPage.getContent()
                .stream()
                .map(DepartmentMapper::toResponse)
                .toList();

        return new DepartmentPageResponse(
                departments,
                departmentPage.getNumber(),
                departmentPage.getSize(),
                departmentPage.getTotalElements(),
                departmentPage.getTotalPages(),
                departmentPage.isLast()
        );
    }

    @Override
    public DepartmentResponse updateDepartment(
            Long id,
            DepartmentRequest request) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department",
                                "id",
                                id
                        ));
        DepartmentMapper.updateEntity(department, request);

        Department updatedDepartment = departmentRepository.save(department);

        return DepartmentMapper.toResponse(updatedDepartment);
    }


    @Override
    public void deleteDepartment(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department",
                                "id",
                                id
                        ));

        departmentRepository.delete(department);
    }
}