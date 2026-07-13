package com.worksphere.employee.service;
import com.worksphere.employee.dto.EmployeePageResponse;
import com.worksphere.employee.dto.EmployeeRequest;
import com.worksphere.employee.dto.EmployeeResponse;
import org.springframework.data.domain.Page;


public interface EmployeeService {

  EmployeeResponse createEmployee(EmployeeRequest request);

    EmployeeResponse  getEmployeeById(Long id);

  EmployeePageResponse getAllEmployees(int page,
                                       int size,
                                       String sortBy,
                                       String sortDir);

  EmployeeResponse updateEmployee(Long id, EmployeeRequest request);

  void deleteEmployee(Long id);


}
