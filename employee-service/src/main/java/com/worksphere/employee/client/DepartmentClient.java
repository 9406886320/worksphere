package com.worksphere.employee.client;

import com.worksphere.employee.dto.DepartmentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class DepartmentClient {

    private final RestClient restClient;

    @Value("${department-service.base-url}")
    private String departmentServiceUrl;

    public DepartmentClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public DepartmentResponse getDepartment(Long departmentId) {

        return restClient.get()
                .uri(departmentServiceUrl + "/api/v1/departments/{id}", departmentId)
                .retrieve()
                .body(DepartmentResponse.class);
    }
}