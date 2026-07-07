package com.worksphere.employee.service.impl;

import com.worksphere.common.exception.ResourceNotFoundException;
import com.worksphere.employee.dto.EmployeeRequest;
import com.worksphere.employee.dto.EmployeeResponse;
import com.worksphere.employee.entity.Employee;
import com.worksphere.employee.repository.EmployeeRepository;
import com.worksphere.employee.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {

        Employee employee = new Employee();

        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setEmail(request.email());
        employee.setSalary(request.salary());

        Employee savedEmployee = employeeRepository.save(employee);

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

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee",
                                "id",
                                id
                        ));

        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getSalary()
        );
    }


    @Override
    public List<EmployeeResponse> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(employee -> new EmployeeResponse(
                        employee.getId(),
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getEmail(),
                        employee.getSalary()
                ))
                .toList();
    }


    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {

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

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee",
                                "id",
                                id));

        employeeRepository.delete(employee);
    }
}
