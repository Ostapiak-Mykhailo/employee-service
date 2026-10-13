package service;

import enums.DepartmentType;
import enums.Position;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

public class ConsoleReader {
    private final static BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));

    public String getInputFromUser() throws IOException {
        return READER.readLine();
    }

    public void SuggestOptions() {
        String string = """
                Please choose action you would like to do:
                Enter '0' to exit the program
                Enter '1' to create new employee
                Enter '2' to create new department
                Enter '3' to see the list of employees
                Enter '4' to see the list of departments
                Enter '5' to see employee by id
                Enter '6' to see department and it`s employee list by id
                Enter '7' to update information about employee
                Enter '8' to delete employee by id
                Enter '9' to delete department by id
                """;
        System.out.println(string);
    }

    public int getNumberFromUser() throws IOException {
        int number = -1;
        while (number == -1) {
            try {
                number = Integer.parseInt(getInputFromUser());
            } catch (NumberFormatException e) {
                System.out.println("incorrect input");
            }
        }
        return number;
    }

    public Position getPositionFromCustomer() throws IOException {
        System.out.println("What`s employee`s position? choose from: DEVELOPER, QA ENGINEER, PROJECT MANAGER, " +
                "BUSINESS ANALYST, TEAM LEAD");
        while (true) {
            String position = getInputFromUser();
            position = position.toUpperCase();
            position = position.replace(" ", "_");
            try {
                return Position.valueOf(position);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. There is no such position in our company, please try again");
            }
        }
    }

    public DepartmentType getDepartmentTypeFromCustomer() throws IOException {
        System.out.println("Enter the name of department. Choose from: IT, HR, SALES, MARKETING");
        while (true) {
            String departmentType = getInputFromUser();
            departmentType = departmentType.toUpperCase();
            try {
                return DepartmentType.valueOf(departmentType);
            } catch (IllegalArgumentException e) {
                System.out.println("There`s no such department in our company, please try again");
            }
        }
    }

    public Integer getOptionFromCustomer() throws IOException {
        int number = getNumberFromUser();
        while (!Set.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).contains(number)) {
            System.out.println("Incorrect input, please try again");
            number = getNumberFromUser();
        }
        return number;
    }

    public String getFieldToUpdate() throws IOException {
        System.out.println("Which field would you like to edit? Select from 'first name', 'last name', 'position'," +
                "or 'department'. Enter '0' to get back to the previous menu");
        String fieldToUpdate = getInputFromUser();
        while (!"0".equals(fieldToUpdate) && !Set.of("first name", "last name", "position", "department").contains(fieldToUpdate)) {
            System.out.println("Incorrect input, please try again");
            fieldToUpdate = getInputFromUser();
        }
        return fieldToUpdate;
    }

    public String getConfirmingFromCustomer() throws IOException {
        String message = """
                Are you sure you want to delete the department? This will also delete all employees.
                Enter 'yes' if you`re sure or enter 'no' to get back to the previous menu
                """;
        System.out.println(message);
        String answer = getInputFromUser();
        while (!Set.of("yes", "no").contains(answer)) {
            System.out.println("Incorrect input. Please try again");
            answer = getInputFromUser();
        }
        return answer;
    }
}
