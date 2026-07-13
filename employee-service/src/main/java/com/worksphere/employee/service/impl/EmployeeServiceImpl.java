package com.worksphere.employee.service.impl;

import com.worksphere.common.exception.ResourceNotFoundException;
import com.worksphere.employee.dto.EmployeePageResponse;
import com.worksphere.employee.dto.EmployeeRequest;
import com.worksphere.employee.dto.EmployeeResponse;
import com.worksphere.employee.entity.Employee;
import com.worksphere.employee.repository.EmployeeRepository;
import com.worksphere.employee.service.EmployeeService;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    private static final Logger log =
            LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        log.info("Creating employee with email: {}", request.email());

        Employee employee = new Employee();

        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setEmail(request.email());
        employee.setSalary(request.salary());

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with ID: {}", savedEmployee.getId());
        return new EmployeeResponse(
                savedEmployee.getId(),
                savedEmployee.getFirstName(),
                savedEmployee.getLastName(),
                savedEmployee.getEmail(),
                savedEmployee.getSalary()
        );
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {

        log.info("Fetching employee with ID: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee",
                                "id",
                                id
                        ));
        log.info("Employee found with ID: {}", id);

        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getSalary()
        );
    }


    @Override
    public EmployeePageResponse getAllEmployees(
            int page,
            int size,
            String sortBy,
            String sortDir) {

        log.info("Fetching employees. Page={}, Size={}, SortBy={}, SortDir={}",
                page, size, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        List<EmployeeResponse> employees = employeePage.getContent()
                .stream()
                .map(employee -> new EmployeeResponse(
                        employee.getId(),
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getEmail(),
                        employee.getSalary()
                ))
                .toList();


        log.info("Successfully fetched {} employees",
                employeePage.getNumberOfElements());

        return new EmployeePageResponse(
                employees,
                employeePage.getNumber(),
                employeePage.getSize(),
                employeePage.getTotalElements(),
                employeePage.getTotalPages(),
                employeePage.isLast()
        );
    }


    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {

        log.info("Updating employee with ID: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee",
                                "id",
                                id));

        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setEmail(request.email());
        employee.setSalary(request.salary());

        Employee updatedEmployee = employeeRepository.save(employee);

        log.info("Employee updated successfully with ID: {}", id);

        return new EmployeeResponse(
                updatedEmployee.getId(),
                updatedEmployee.getFirstName(),
                updatedEmployee.getLastName(),
                updatedEmployee.getEmail(),
                updatedEmployee.getSalary()
        );
    }

    @Override
    public void deleteEmployee(Long id) {
        log.info("Deleting employee with ID: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee",
                                "id",
                                id));

        employeeRepository.delete(employee);

        log.info("Employee deleted successfully with ID: {}", id);
    }
}
