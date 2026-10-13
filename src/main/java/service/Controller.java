package service;

import dao.DepartmentDao;
import dao.EmployeeDao;
import entities.Department;
import entities.Employee;
import enums.DepartmentType;
import enums.Position;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class Controller {

    private final ConsoleReader reader = new ConsoleReader();
    private final EmployeeDao employeeDao = new EmployeeDao();
    private final DepartmentDao departmentDao = new DepartmentDao();

    public void runApp() throws IOException {
        boolean running = true;
        while (running) {
            reader.SuggestOptions();
            int number = reader.getOptionFromCustomer();
            if (number == 0) {
                running = false;
            }
            findOption(number);
        }
    }

    private void findOption(Integer number) throws IOException {
        switch (number) {
            case 1 -> {
                if (isDepartmentListEmpty()) {
                    log.warn("First you should create at least one department");
                    return;
                }
                createEmployee();
            }
            case 2 -> createNewDepartment();
            case 3 -> printListOfEmployees();
            case 4 -> printListOfDepartments();
            case 5 -> printEmployeeById();
            case 6 -> printDepartmentEmployeesById();
            case 7 -> updateEmployee();
            case 8 -> deleteEmployeeById();
            case 9 -> deleteDepartmentById();
        }
    }

    private void deleteEmployeeById() throws IOException {
        if (isEmployeeListEmpty()) {
            log.warn("The Employee list is empty yet");
            return;
        }
        System.out.println("Choose the id of the employee you want to delete.");
        printListOfEmployees();
        int id = reader.getNumberFromUser();
        Employee employeeToDelete = employeeDao.getEmployeeById(id);
        employeeDao.deleteEmployee(employeeToDelete);
        log.info("Employee by id {} was deleted successfully", id);
    }

    private void deleteDepartmentById() throws IOException {
        System.out.println("Choose the id of the department you want to delete.");
        printListOfDepartments();
        int id = reader.getNumberFromUser();
        if (!confirmDepartmentDeleting()) {
            return;
        }
        Department departmentToDelete = departmentDao.getDepartmentById(id);
        departmentDao.deleteDepartment(departmentToDelete);
        log.info("Department {} by id {} was deleted successfully", departmentToDelete.getDepartmentType(), id);
    }

    private void createEmployee() throws IOException {
        Employee employee = new Employee();
        System.out.println("Enter employee`s first name");
        String firstName = reader.getInputFromUser();
        employee.setFirstName(firstName);

        System.out.println("Enter employee`s last name");
        String lastName = reader.getInputFromUser();
        employee.setLastName(lastName);

        Position position = reader.getPositionFromCustomer();
        employee.setPosition(position);

        System.out.println("In which department the employee works? " +
                "Select the department number from the available options:");
        printListOfDepartments();
        int departmentId = reader.getNumberFromUser();

        if (!isDepartmentExist(departmentId)) {
            log.warn("There`s no such department number");
            return;
        }

        Department department = departmentDao.getDepartmentById(departmentId);
        employee.setDepartment(department);
        employeeDao.saveEmployee(employee);
        log.info("New employee has been created successfully. First name: {}; Last name: {}: Position: {}; Department: {}"
                , firstName, lastName, position, department);
    }

    private void updateEmployee() throws IOException {
        if (isEmployeeListEmpty()) {
            log.warn("The Employee list is empty yet");
            return;
        }
        System.out.println("You can see id numbers of all employees. Select the employee number you want to update");
        printListOfEmployees();
        int id = reader.getNumberFromUser();
        if (!isEmployeeExist(id)) {
            log.warn("Employee with id {} does not exist", id);
            throw new IllegalArgumentException();
        }
        Employee employeeForUpdate = employeeDao.getEmployeeById(id);

        String fieldToUpdate = reader.getFieldToUpdate();

        if ("0".equals(fieldToUpdate)) {
            return;
        }
        switch (fieldToUpdate) {
            case "first name" -> updateEmployeeFirstName(employeeForUpdate);
            case "last name" -> updateEmployeeLastName(employeeForUpdate);
            case "position" -> updateEmployeePosition(employeeForUpdate);
            case "department" -> updateEmployeeDepartment(employeeForUpdate);
        }
        employeeDao.updateEmployee(employeeForUpdate);
        log.info("field {} employee by id {} was updated successfully", fieldToUpdate, id);
    }

    private void updateEmployeeFirstName(Employee employeeForUpdate) throws IOException {
        System.out.println("Please enter new value");
        String newFirstName = reader.getInputFromUser();
        employeeForUpdate.setFirstName(newFirstName);
    }

    private void updateEmployeeLastName(Employee employeeForUpdate) throws IOException {
        System.out.println("Please enter new value");
        String newLastName = reader.getInputFromUser();
        employeeForUpdate.setLastName(newLastName);
    }

    private void updateEmployeePosition(Employee employeeForUpdate) throws IOException {
        System.out.println("Please enter new value");
        Position newPosition = reader.getPositionFromCustomer();
        employeeForUpdate.setPosition(newPosition);
    }

    private void updateEmployeeDepartment(Employee employeeForUpdate) throws IOException {
        printListOfDepartments();
        System.out.println("Enter department ID:");
        int departmentId = reader.getNumberFromUser();

        Department department = departmentDao.getDepartmentById(departmentId);
        if (department == null) {
            System.out.println("Department not found");
            return;
        }
        employeeForUpdate.setDepartment(department);
    }

    private void printEmployeeById() throws IOException {
        if (isEmployeeListEmpty()) {
            System.out.println("The Employee list is empty yet");
            return;
        }
        List<Integer> employeesId = employeeDao.getAllEmployeeId();
        System.out.println("You can see id numbers of all employees. Select the employee number for whom you want " +
                "to see information.");
        System.out.println(employeesId);
        int employeeId = reader.getNumberFromUser();
        Employee employee = employeeDao.getEmployeeById(employeeId);
        System.out.println(employee);
    }

    private void printDepartmentEmployeesById() throws IOException {
        if (isDepartmentListEmpty()) {
            System.out.println("The Department list is empty yet");
            return;
        }
        System.out.println("You can see a list of departments. Select the department number for which you want " +
                "to see the list of employees.");
        printListOfDepartments();
        int id = reader.getNumberFromUser();
        Department department = departmentDao.getDepartmentById(id);
        List<Employee> employees = departmentDao.getDepartmentEmployeeListById(id);
        System.out.println(department);
        for (Employee employee : employees) {
            System.out.println(employee);
        }

    }

    private void createNewDepartment() throws IOException {
        Department department = new Department();
        DepartmentType departmentType = reader.getDepartmentTypeFromCustomer();
        if (isDepartmentTypeExist(departmentType)) {
            log.warn("Department {} has already been created", departmentType);
            return;
        }
        department.setDepartmentType(departmentType);
        departmentDao.saveDepartment(department);
        log.info("Department {} has been created successfully", departmentType);
    }

    private void printListOfDepartments() {
        if (isDepartmentListEmpty()) {
            log.warn("The Department list is empty yet");
            return;
        }
        List<Department> departments = departmentDao.getAllDepartmentsAsList();
        for (Department department : departments) {
            System.out.println(department);
        }
    }

    private void printListOfEmployees() {
        if (isEmployeeListEmpty()) {
            log.warn("The Employee list is empty yet");
            return;
        }
        List<Employee> employees = employeeDao.getAllEmployeesAsList();
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }

    private Boolean confirmDepartmentDeleting() throws IOException {
        String answer = reader.getConfirmingFromCustomer();
        return "yes".equals(answer);
    }

    private boolean isDepartmentExist(Integer id) {
        List<Integer> departmentId = departmentDao.getAllDepartmentId();
        return departmentId.contains(id);
    }

    private Boolean isDepartmentListEmpty() {
        List<Integer> departmentId = departmentDao.getAllDepartmentId();
        return departmentId.isEmpty();
    }

    private Boolean isDepartmentTypeExist(DepartmentType departmentType) {
        List<DepartmentType> departmentTypes = departmentDao.getAllDepartmentTypes();
        return departmentTypes.contains(departmentType);
    }

    private Boolean isEmployeeListEmpty() {
        List<Integer> employeeId = employeeDao.getAllEmployeeId();
        return employeeId.isEmpty();
    }

    private Boolean isEmployeeExist(int id) {
        List<Integer> employeeId = employeeDao.getAllEmployeeId();
        return employeeId.contains(id);
    }
}
