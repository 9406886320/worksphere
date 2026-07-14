package com.worksphere.department.service.impl;

import com.worksphere.common.exception.ResourceNotFoundException;
import com.worksphere.department.dto.DepartmentPageResponse;
import com.worksphere.department.dto.DepartmentResponse;
import com.worksphere.department.entity.Department;
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

        Department department = new Department();

        department.setDepartmentName(request.departmentName());
        department.setDepartmentCode(request.departmentCode());
        department.setDepartmentHead(request.departmentHead());
        department.setLocation(request.location());

        Department savedDepartment = departmentRepository.save(department);

        return new DepartmentResponse(
                savedDepartment.getId(),
                savedDepartment.getDepartmentName(),
                savedDepartment.getDepartmentCode(),
                savedDepartment.getDepartmentHead(),
                savedDepartment.getLocation()
        );
    }
    @Override
    public DepartmentResponse getDepartmentById(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->  new ResourceNotFoundException(
                        "Department",
                        "id",
                        id
                ));

        return new DepartmentResponse(
                department.getId(),
                department.getDepartmentName(),
                department.getDepartmentCode(),
                department.getDepartmentHead(),
                department.getLocation()
        );
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
                .map(department -> new DepartmentResponse(
                        department.getId(),
                        department.getDepartmentName(),
                        department.getDepartmentCode(),
                        department.getDepartmentHead(),
                        department.getLocation()
                ))
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
        department.setDepartmentName(request.departmentName());
        department.setDepartmentCode(request.departmentCode());
        department.setDepartmentHead(request.departmentHead());
        department.setLocation(request.location());

        Department updatedDepartment = departmentRepository.save(department);

        return new DepartmentResponse(
                updatedDepartment.getId(),
                updatedDepartment.getDepartmentName(),
                updatedDepartment.getDepartmentCode(),
                updatedDepartment.getDepartmentHead(),
                updatedDepartment.getLocation()
        );
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