package com.worksphere.department.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String departmentName;

    @Column(nullable = false, unique = true)
    private String departmentCode;

    @Column(nullable = false)
    private String departmentHead;

    @Column(nullable = false)
    private String location;

    public Department() {
    }

    public Department(Long id,
                      String departmentName,
                      String departmentCode,
                      String departmentHead,
                      String location) {
        this.id = id;
        this.departmentName = departmentName;
        this.departmentCode = departmentCode;
        this.departmentHead = departmentHead;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentHead() {
        return departmentHead;
    }

    public void setDepartmentHead(String departmentHead) {
        this.departmentHead = departmentHead;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}