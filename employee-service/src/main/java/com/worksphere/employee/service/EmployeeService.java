package com.worksphere.employee.service;
import com.worksphere.employee.dto.EmployeeRequest;
import com.worksphere.employee.dto.EmployeeResponse;

import java.util.List;


public interface EmployeeService {

  EmployeeResponse createEmployee(EmployeeRequest request);

    EmployeeResponse  getEmployeeById(Long id);

  List<EmployeeResponse> getAllEmployees();

  EmployeeResponse updateEmployee(Long id, EmployeeRequest request);

  void deleteEmployee(Long id);


}
