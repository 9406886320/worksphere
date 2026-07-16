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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private static final Logger log =
            LoggerFactory.getLogger(DepartmentServiceImpl.class);

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest request) {

        log.info("Creating department with code: {}", request.departmentCode());

        Department department = DepartmentMapper.toEntity(request);

        Department savedDepartment = departmentRepository.save(department);

        log.info("Department created successfully with id: {}", savedDepartment.getId());

        return DepartmentMapper.toResponse(savedDepartment);
    }
    @Override
    public DepartmentResponse getDepartmentById(Long id) {

        log.info("Fetching department with id: {}", id);


        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Department not found with id: {}", id);

                    return new ResourceNotFoundException(
                            "Department",
                            "id",
                            id
                    );
                });

        return DepartmentMapper.toResponse(department);
    }

    @Override
    public DepartmentPageResponse getAllDepartments(
            int page,
            int size,
            String sortBy,
            String sortDir) {

        log.info("Fetching all departments. Page: {}, Size: {}", page, size);

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Department> departmentPage = departmentRepository.findAll(pageable);

        log.info("Retrieved {} departments", departmentPage.getNumberOfElements());

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
        log.info("Updating department with id: {}", id);
        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department",
                                "id",
                                id
                        ));
        DepartmentMapper.updateEntity(department, request);

        Department updatedDepartment = departmentRepository.save(department);
        log.info("Department updated successfully with id: {}", updatedDepartment.getId());
        return DepartmentMapper.toResponse(updatedDepartment);
    }


    @Override
    public void deleteDepartment(Long id) {
        log.info("Deleting department with id: {}", id);
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> {log.warn("Department not found with id: {}", id);
                       return  new ResourceNotFoundException(
                                "Department",
                                "id",
                                id
                       );
                });

        departmentRepository.delete(department);
    }
}