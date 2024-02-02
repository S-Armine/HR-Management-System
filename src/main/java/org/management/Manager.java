package org.management;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "manager")
@DiscriminatorValue("manager")
public class Manager extends Employee{
    @OneToOne(mappedBy = "departmentHead")
    private Department managedDepartment;
    @Enumerated(EnumType.STRING)
    @Column(name = "management_level")
    private ManagementLevel managementLevel;

    public Manager() {
    }

    public Manager(Department managedDepartment, ManagementLevel managementLevel) {
        this.managedDepartment = managedDepartment;
        this.managementLevel = managementLevel;
    }

    public Manager(String name, String email, String phoneNumber, LocalDate hireDate, String jobTitle) {
        super(name, email, phoneNumber, hireDate, jobTitle);
    }

    public Manager(String name, String email, String phoneNumber, LocalDate hireDate, String jobTitle, Department managedDepartment, ManagementLevel managementLevel) {
        super(name, email, phoneNumber, hireDate, jobTitle);
        this.managedDepartment = managedDepartment;
        this.managementLevel = managementLevel;
    }

    public void setManagedDepartment(Department managedDepartment) {
        if (this.managedDepartment != null) {
            this.managedDepartment.setDepartmentHead(null);
        }
        this.managedDepartment = managedDepartment;
        if (managedDepartment != null && managedDepartment.getDepartmentHead() != this) {
            managedDepartment.setDepartmentHead(this);
        }
    }

    public void setManagementLevel(ManagementLevel managementLevel) {
        this.managementLevel = managementLevel;
    }

    public Department getManagedDepartment() {
        return managedDepartment;
    }

    public ManagementLevel getManagementLevel() {
        return managementLevel;
    }
}
