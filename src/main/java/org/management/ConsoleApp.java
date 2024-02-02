package org.management;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;


public class ConsoleApp {
    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            actions:
            while (true) {
                System.out.println(menu());
                System.out.print("Enter your choice: ");
                String choice = scanner.next();
                ActionType operation = ActionType.getActionFromValue(choice);
                if (operation == null) {
                    System.out.println("Invalid input. Try again");
                    continue;
                }
                switch (operation) {
                    case CREATE_NEW_DEPARTMENT -> createDepartment(session);
                    case UPDATE_THE_DEPARTMENT -> updateDepartment(session);
                    case DELETE_THE_DEPARTMENT -> deleteDepartment(session);
                    case ASSIGN_AN_EMPLOYEE_TO_THE_DEPARTMENT -> assignEmployeeToDepartment(session);
                    case REASSIGN_THE_EMPLOYEE_FROM_THE_DEPARTMENT -> reassignEmployeeFromDepartment(session);
                    case CREATE_NEW_PROJECT -> createProject(session);
                    case UPDATE_THE_PROJECT -> updateProject(session);
                    case DELETE_THE_PROJECT -> deleteProject(session);
                    case ASSIGN_AN_EMPLOYEE_TO_THE_PROJECT -> assignEmployeeToProject(session);
                    case REASSIGN_THE_EMPLOYEE_FROM_THE_PROJECT -> reassignEmployeeFromProject(session);
                    case ASSIGN_A_MANAGER_TO_THE_DEPARTMENT -> assignManagerToDepartment(session);
                    case REASSIGN_THE_MANAGER_FROM_THE_DEPARTMENT -> reassignManagerFromDepartment(session);
                    case CREATE_NEW_EMPLOYEE -> createEmployee(session);
                    case UPDATE_THE_EMPLOYEE -> updateEmployee(session);
                    case DELETE_THE_EMPLOYEE -> deleteEmployee(session);
                    case CREATE_NEW_MANAGER -> createManager(session);
                    case EXIT -> {
                        System.out.println("Exiting");
                        return;
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }

    public String menu() {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("Choose action you want to execute from list bellow.\n")
                .append("1. Create new department\n")
                .append("2. Update the department\n")
                .append("3. Delete the department\n")
                .append("4. Assign an employee to the department\n")
                .append("5. Reassign the employee from the department\n")
                .append("6. Create new project\n")
                .append("7. Update the project\n")
                .append("8. Delete the project\n")
                .append("9. Assign an employee to the project\n")
                .append("10. Reassign the employee from the project\n")
                .append("11. Assign a manager to the department\n")
                .append("12. Reassign the manager from the department\n")
                .append("13. Create new employee\n")
                .append("14. Update information of employee\n")
                .append("15. Delete employee\n")
                .append("16: Create new manager\n")
                .append("0: Exit.")
                .toString();
    }

    private void createDepartment(Session session) {
        Transaction transaction = session.beginTransaction();
        Department department = new Department();
        setDepartmentName(department);
        setDepartmentLocation(department);
        session.persist(department);
        session.flush();
        transaction.commit();
    }

    private void updateDepartment(Session session) {
        Department department = getDepartmentFromID(session);
        if(department == null) {
            System.out.println("There is no department with given identifier.");
            return;
        }
        Transaction transaction = session.beginTransaction();
        updating: while (true) {
            System.out.println("Choose column you want to update");
            System.out.println("1: name");
            System.out.println("2: location");
            System.out.println("0: exit");
            String choice = scanner.next();
            switch (choice) {
                case "1" -> setDepartmentName(department);
                case "2" -> setDepartmentLocation(department);
                case "0" -> {
                    break updating;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
        session.persist(department);
        session.flush();
        session.getSessionFactory().getCache().evict(Department.class, department.getDepartmentID());
        System.out.println("Information was successfully updated.");
        transaction.commit();
    }

    private void deleteDepartment(Session session) {
        Department department = getDepartmentFromID(session);
        if(department == null) {
            System.out.println("There is no department with given identifier.");
            return;
        }
        Transaction transaction = session.beginTransaction();
        session.delete(department);
        session.getSessionFactory().getCache().evict(Department.class, department.getDepartmentID());
        transaction.commit();
    }

    private void setDepartmentName(Department department) {
        String name;
        System.out.print("Enter name of department: ");
        name = scanner.next();
        department.setDepartmentName(name);
    }

    private void setDepartmentLocation(Department department) {
        String location;
        System.out.print("Enter location of department: ");
        location = scanner.next();
        department.setLocation(location);
    }

    private void assignEmployeeToDepartment(Session session) {
        Department department = getDepartmentFromID(session);
        if (department == null) {
            System.out.println("There is no department with given identifier.");
            return;
        }
        Employee employee = getEmployeeFromID(session);
        if (employee == null) {
            System.out.println("There is no employee with given identifier.");
            return;
        }
        Transaction transaction = session.beginTransaction();
        if(!department.addEmployee(employee)) {
            System.out.println("Employee is already assigned to department.");
            transaction.rollback();
            return;
        }
        session.persist(employee);
        session.flush();
        session.persist(department);
        session.flush();
        session.getSessionFactory().getCache().evict(Employee.class, employee.getEmployeeID());
        session.getSessionFactory().getCache().evict(Department.class, department.getDepartmentID());
        System.out.println("Employee is successfully assigned to department.");
        transaction.commit();
    }

    private void reassignEmployeeFromDepartment(Session session) {
        Department department = getDepartmentFromID(session);
        if (department == null) {
            System.out.println("There is no department with given identifier.");
            return;
        }
        Employee employee = getEmployeeFromID(session);
        if (employee == null) {
            System.out.println("There is no employee with given identifier.");
            return;
        }
        Transaction transaction = session.beginTransaction();
        if(!department.deleteEmployee(employee)) {
            System.out.println("Employee is not assigned to department.");
            transaction.rollback();
            return;
        }
        session.persist(employee);
        session.flush();
        session.persist(department);
        session.flush();
        session.getSessionFactory().getCache().evict(Employee.class, employee.getEmployeeID());
        session.getSessionFactory().getCache().evict(Department.class, department.getDepartmentID());
        System.out.println("Employee is successfully reassigned from department.");
        transaction.commit();
    }

    private void createProject(Session session) {
        Transaction transaction = session.beginTransaction();
        Project project = new Project();
        setProjectName(project);
        setProjectDuration(project);
        setProjectBudget(project);
        session.persist(project);
        session.flush();
        transaction.commit();
    }
    private void updateProject(Session session) {
        Project project = getProjectFromID(session);
        if(project == null) {
            System.out.println("There is no project with given identifier.");
            return;
        }
        Transaction transaction = session.beginTransaction();
        updating: while (true) {
            System.out.println("Choose information you want to update");
            System.out.println("1: name");
            System.out.println("2: durations");
            System.out.println("3: budget");
            System.out.println("0: exit");
            String choice = scanner.next();
            switch (choice) {
                case "1" -> setProjectName(project);
                case "2" -> setProjectDuration(project);
                case "3" -> setProjectBudget(project);
                case "0" -> {
                    break updating;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
        session.persist(project);
        session.flush();
        System.out.println("Information was successfully updated.");
        transaction.commit();
    }

    private void deleteProject(Session session) {
        Project project = getProjectFromID(session);
        if(project == null) {
            System.out.println("There is no project with given identifier.");
            return;
        }
        Transaction transaction = session.beginTransaction();
        session.delete(project);
        transaction.commit();
    }

    private void setProjectName(Project project) {
        System.out.println("Enter the name of project.");
        String name = scanner.next();
        project.setProjectName(name);
    }

    private void setProjectDuration(Project project) {
        LocalDate startDate;
        LocalDate endDate;
        while(true) {
            try {
                System.out.println("Enter start date of project in year-month-day format.");
                String start = scanner.next();
                startDate = LocalDate.parse(start);
                System.out.println("Enter end date of project in year-month-day format.");
                String end = scanner.next();
                endDate = LocalDate.parse(end);
                if (ChronoUnit.DAYS.between(startDate, endDate) > 0
                        && ChronoUnit.DAYS.between(LocalDate.now(), endDate) > 0) {
                    break;
                } else {
                    System.out.println("Invalid time period.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Input was wrong, try again.");
            }
        }
        project.setStartDate(startDate);
        project.setEndDate(endDate);
    }

    private void setProjectBudget(Project project) {
        BigDecimal budget;
        while (true) {
            try {
                System.out.println("Enter budget of project.");
                String budgetInput = scanner.next();
                budget = new BigDecimal(budgetInput);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Not valid decimal number. Try again.");
            }
        }
        project.setBudget(budget);
    }

    private void assignEmployeeToProject(Session session) {
        Project project = getProjectFromID(session);
        if(project == null) {
            System.out.println("There is no project with given identifier.");
            return;
        }
        Employee employee = getEmployeeFromID(session);
        if (employee == null) {
            System.out.println("There is no employee with given identifier.");
            return;
        }
        Transaction transaction = session.beginTransaction();
        if(!project.addEmployee(employee)) {
            System.out.println("Employee is already assigned to project.");
            transaction.rollback();
            return;
        }
        session.persist(employee);
        session.flush();
        session.persist(project);
        session.flush();
        session.getSessionFactory().getCache().evict(Employee.class, employee.getEmployeeID());
        System.out.println("Employee is successfully assigned to project.");
        transaction.commit();
    }

    private void reassignEmployeeFromProject(Session session) {
        Project project = getProjectFromID(session);
        if(project == null) {
            System.out.println("There is no project with given identifier.");
            return;
        }
        Employee employee = getEmployeeFromID(session);
        if (employee == null) {
            System.out.println("There is no employee with given identifier.");
            return;
        }
        Transaction transaction = session.beginTransaction();
        if(!project.deleteEmployee(employee)) {
            System.out.println("Employee is not assigned to department.");
            transaction.rollback();
            return;
        }
        session.persist(employee);
        session.flush();
        session.persist(project);
        session.flush();
        session.getSessionFactory().getCache().evict(Employee.class, employee.getEmployeeID());
        System.out.println("Employee is successfully reassigned from project.");
        transaction.commit();
    }

    private void assignManagerToDepartment(Session session) {
        Department department = getDepartmentFromID(session);
        if (department == null) {
            System.out.println("There is no department with given identifier.");
            return;
        }
        Manager manager;
        try {
            manager = (Manager) getEmployeeFromID(session);
            if (manager == null) {
                System.out.println("There is no manager with given identifier.");
                return;
            }
        } catch (ClassCastException e) {
            System.out.println("There is no manager with given identifier.");
            return;
        }
        Transaction transaction = session.beginTransaction();
        department.setDepartmentHead(manager);
        session.persist(manager);
        session.flush();
        session.persist(department);
        session.flush();
        session.getSessionFactory().getCache().evict(Manager.class, manager.getEmployeeID());
        session.getSessionFactory().getCache().evict(Department.class, department.getDepartmentID());
        System.out.println("Manager is successfully assigned to department.");
        transaction.commit();
    }

    private void reassignManagerFromDepartment(Session session) {
        Department department = getDepartmentFromID(session);
        if (department == null) {
            System.out.println("There is no department with given identifier.");
            return;
        }
        Manager manager;
        try {
            manager = (Manager) getEmployeeFromID(session);
            if (manager == null) {
                System.out.println("There is no manager with given identifier.");
                return;
            }
        } catch (ClassCastException e) {
            System.out.println("There is no manager with given identifier.");
            return;
        }
        Transaction transaction = session.beginTransaction();
        department.setDepartmentHead(null);
        session.persist(manager);
        session.flush();
        session.persist(department);
        session.flush();
        session.getSessionFactory().getCache().evict(Manager.class, manager.getEmployeeID());
        session.getSessionFactory().getCache().evict(Department.class, department.getDepartmentID());
        System.out.println("Manager is successfully reassigned from department.");
        transaction.commit();
    }

    private void createEmployee(Session session) {
        Transaction transaction = session.beginTransaction();
        Employee employee = new Employee();
        setEmployeeName(employee);
        setEmployeeEmail(employee);
        setEmployeePhoneNumber(employee);
        setEmployeeJobTitle(employee);
        session.persist(employee);
        session.flush();
        transaction.commit();
    }
    private void updateEmployee(Session session) {
        var employee = getEmployeeFromID(session);
        if(employee == null) {
            System.out.println("There is no employee with given identifier.");
            return;
        }
        Transaction transaction = session.beginTransaction();
        updating: while (true) {
            System.out.println("Choose column you want to update");
            System.out.println("1: name");
            System.out.println("2: email");
            System.out.println("3: phone number");
            System.out.println("4: job title");
            System.out.println("0: exit");
            String choice = scanner.next();
            switch (choice) {
                case "1" -> setEmployeeName(employee);
                case "2" -> setEmployeeEmail(employee);
                case "3" -> setEmployeePhoneNumber(employee);
                case "4" -> setEmployeeJobTitle(employee);
                case "0" -> {
                    break updating;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
        session.persist(employee);
        session.flush();
        session.getSessionFactory().getCache().evict(Employee.class, employee.getEmployeeID());
        System.out.println("Information was successfully updated.");
        transaction.commit();
    }

    private void deleteEmployee(Session session) {
        Employee employee = getEmployeeFromID(session);
        if(employee == null) {
            System.out.println("There is no employee with given identifier.");
            return;
        }
        Transaction transaction = session.beginTransaction();
        session.delete(employee);
        session.getSessionFactory().getCache().evict(Employee.class, employee.getEmployeeID());
        transaction.commit();
    }

    private void setEmployeeName(Employee employee) {
        System.out.println("Input name of employee.");
        String newName = scanner.next();
        employee.setName(newName);
    }

    private void setEmployeeEmail(Employee employee) {
        String email;
        while (true) {
            System.out.print("Enter the email of new employee: ");
            email = scanner.nextLine();
            if (isValidEmail(email)) {
                break;
            }
            System.out.println("Not valid email was given. Try again.");
        }
        employee.setEmail(email);
    }

    private void setEmployeePhoneNumber(Employee employee) {
        String phoneNumber;
        while (true) {
            System.out.print("Enter the phone number of new employee: ");
            phoneNumber = scanner.nextLine();
            if (isValidPhoneNumber(phoneNumber)) {
                break;
            }
            System.out.println("Not valid phone number was given. Try again.");
        }
        employee.setPhoneNumber(phoneNumber);
    }

    private void setEmployeeJobTitle(Employee employee) {
        String jobTitle;
        System.out.print("Enter the job title of new employee: ");
        jobTitle = scanner.nextLine();
        employee.setJobTitle(jobTitle);
    }

    private void createManager(Session session) {
        Transaction transaction = session.beginTransaction();
        Manager manager = new Manager();
        setEmployeeName(manager);
        setEmployeeEmail(manager);
        setEmployeePhoneNumber(manager);
        setEmployeeJobTitle(manager);
        setManagerLevel(manager);
        session.persist(manager);
        session.flush();
        transaction.commit();
    }

    private void setManagerLevel(Manager manager) {
        ManagementLevel level;
        while (true) {
            System.out.println("Select from following levels:");
            System.out.println("1: Top level");
            System.out.println("2: Mid level");
            System.out.println("3: First level.");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            level = ManagementLevel.getLevelFromValue(choice);
            if (level != null) {
                break;
            }
            System.out.println("Invalid input. Try again");
        }
        manager.setManagementLevel(level);
    }

    private Department getDepartmentFromID(Session session) {
        int id;
        while (true) {
            System.out.println("Enter id of the department.");
            if (scanner.hasNextInt()) {
                id = scanner.nextInt();
                break;
            }
            scanner.nextLine();
            System.out.println("ID should be an integer number.");
        }
        Query<Department> query = session.createNamedQuery("findDepartmentFromID", Department.class);
        return query.setParameter("id", id).uniqueResultOptional().orElse(null);
    }

    private Employee getEmployeeFromID(Session session) {
        int id;
        while (true) {
            System.out.println("Enter id of the employee.");
            if (scanner.hasNextInt()) {
                id = scanner.nextInt();
                break;
            }
            scanner.nextLine();
            System.out.println("ID should be an integer number.");
        }
        Query<Employee> query = session.createNamedQuery("findEmployeeFromID", Employee.class);
        return query.setParameter("id", id).uniqueResultOptional().orElse(null);
    }

    private Project getProjectFromID(Session session) {
        int id;
        while (true) {
            System.out.println("Enter id of the project.");
            if (scanner.hasNextInt()) {
                id = scanner.nextInt();
                break;
            }
            scanner.nextLine();
            System.out.println("ID should be an integer number.");
        }
        Query<Project> query = session.createNamedQuery("findProjectFromID", Project.class);
        return query.setParameter("id", id).uniqueResultOptional().orElse(null);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneNumberRegex = "^[0-9]+$";
        return phoneNumber.matches(phoneNumberRegex);
    }
}