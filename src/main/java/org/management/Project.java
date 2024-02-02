package org.management;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "projects")
@NamedQueries({
        @NamedQuery(
                name = "allProjects",
                query = "SELECT p FROM Project p"
        ),
        @NamedQuery(
                name = "findProjectFromID",
                query = "SELECT p FROM Project p WHERE p.projectID = :id"
        )
})
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Integer projectID;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "budget")
    private BigDecimal budget;
    @ManyToMany(mappedBy = "projects")
    private List<Employee> employees = new ArrayList<>();

    public Project() {
    }

    public Project(String projectName, LocalDate startDate, LocalDate endDate, BigDecimal budget) {
        this.projectName = projectName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
    }

    public Project(String projectName, LocalDate startDate, LocalDate endDate, BigDecimal budget, List<Employee> employees) {
        this.projectName = projectName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.employees = employees;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public boolean addEmployee(Employee employee) {
        if (employee == null || employees.contains(employee)) {
            return false;
        }
        employees.add(employee);
        if (!employee.getProjects().contains(this)) {
            employee.addProject(this);
        }
        return true;
    }

    public boolean deleteEmployee(Employee employee) {
        if (employee == null || !employees.contains(employee)) {
            return false;
        }
        employees.remove(employee);
        if (employee.getProjects().contains(this)) {
            employee.deleteProject(this);
        }
        return true;
    }
}
