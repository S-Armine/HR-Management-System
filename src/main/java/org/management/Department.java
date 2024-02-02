package org.management;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(
                name = "allDepartments",
                query = "SELECT d FROM Department d",
                cacheable = true
        ),
        @org.hibernate.annotations.NamedQuery(
                name = "findDepartmentFromID",
                query = "SELECT d FROM Department d WHERE departmentID = :id",
                cacheable = true
        )
})
public class Department{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Integer departmentID;
    @Column(name = "department_name")
    private String departmentName;
    @Column(name = "location")
    private String location;
    @OneToOne()
    @JoinColumn(name = "head_id")
    private Manager departmentHead;
    @OneToMany(mappedBy = "department")
    private List<Employee> employees = new ArrayList<>();

    public Department() {
    }

    public Department(String departmentName, String location) {
        this.departmentName = departmentName;
        this.location = location;
    }

    public Department(String departmentName, String location, Manager departmentHead, List<Employee> employees) {
        this.departmentName = departmentName;
        this.location = location;
        this.departmentHead = departmentHead;
        this.employees = employees;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDepartmentHead(Manager departmentHead) {
        if (this.departmentHead!= null) {
            this.departmentHead.setManagedDepartment(null);
        }
        this.departmentHead = departmentHead;
        if (departmentHead != null && departmentHead.getManagedDepartment() != this) {
            departmentHead.setDepartment(this);
        }
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public Integer getDepartmentID() {
        return departmentID;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getLocation() {
        return location;
    }

    public Manager getDepartmentHead() {
        return departmentHead;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public boolean addEmployee(Employee employee) {
        if (employee == null || employees.contains(employee)) {
            return false;
        }
        if(employee.getDepartment() != this) {
            employee.setDepartment(this);
        }
        employees.add(employee);
        return true;
    }

    public boolean deleteEmployee(Employee employee) {
        if (!employees.contains(employee)) {
            return false;
        }
        employee.setDepartment(null);
        employees.remove(employee);
        return true;
    }
}
