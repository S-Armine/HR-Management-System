package org.management;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "employee_type")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(
                name = "allEmployees",
                query = "SELECT e FROM Employee e",
                cacheable = true
        ),
        @org.hibernate.annotations.NamedQuery(
                name = "findEmployeeFromID",
                query = "SELECT e FROM Employee e WHERE employeeID = :id",
                cacheable = true
        )
})
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Integer employeeID;
    @Column(name = "name")
    private String name;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "hire_date")
    private LocalDate hireDate;
    @Column(name = "job_title")
    private String jobTitle;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    @ManyToMany
    @JoinTable(
            name = "employee_project",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Project> projects = new ArrayList<>();

    public Employee() {
    }

    public Employee(String name, String email, String phoneNumber, LocalDate hireDate, String jobTitle, Department department, List<Project> projects) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.hireDate = hireDate;
        this.jobTitle = jobTitle;
        this.department = department;
        this.projects = projects;
    }

    public Employee(String name, String email, String phoneNumber, LocalDate hireDate, String jobTitle) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.hireDate = hireDate;
        this.jobTitle = jobTitle;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setDepartment(Department department) {
        this.department = department;
        if(department != null && !department.getEmployees().contains(this)) {
            department.addEmployee(this);
        }
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public Department getDepartment() {
        return department;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public boolean addProject(Project project) {
        if (project == null || projects.contains(project)) {
            return false;
        }
        projects.add(project);
        if (!project.getEmployees().contains(this)) {
            project.addEmployee(this);
        }
        return true;
    }

    public boolean deleteProject(Project project) {
        if (project == null || !projects.contains(project)) {
            return false;
        }
        projects.remove(project);
        if (project.getEmployees().contains(this)) {
            project.deleteEmployee(this);
        }
        return true;
    }
}
