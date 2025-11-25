package com.napier.sem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

@SpringBootApplication
@RestController
public class App {
    public static void main(String[] args)
    {
        // Connect to database
        if (args.length < 1)
        {
            connect("localhost:33060");
        }
        else
        {
            connect(args[0]);
        }

        SpringApplication.run(App.class, args);
    }

    /**
     * Connection to MySQL database.
     */
    private static Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public static void connect(String location) {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(3000);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + location
                                + "/employees?allowPublicKeyRetrieval=true&useSSL=false",
                        "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " +                                  Integer.toString(i));
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public static void disconnect() {
        if (con != null) {
            try {
                // Close connection
                con.close();
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * Get a single employee record.
     * @param ID emp_no of the employee record to get.
     * @return The record of the employee with emp_no or null if no employee exists.
     */
    @RequestMapping("employee")
    public Employee getEmployee(@RequestParam(value = "id") String ID) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, title, salary, dept_name,  "
                            + "CONCAT(manager.first_name, ' ', manager.last_name) AS manager "
                            + "FROM employees "
                            + "JOIN titles ON titles.emp_no=employees.emp_no "
                            + "JOIN salaries ON salaries.emp_no=employees.emp_no "
                            + "JOIN dept_emp ON dept_emp.emp_no=employees.emp_no "
                            + "JOIN departments ON departments.dept_no=dept_emp.dept_no "
                            + "JOIN dept_manager ON dept_manager.dept_no=departments.dept_no "
                            + "JOIN employees AS manager ON manager.emp_no = dept_manager.emp_no "
                            + "WHERE dept_emp.to_date = '9999-01-01' AND employees.emp_no = " + ID;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            if (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.title = rset.getString("title");
                emp.salary = rset.getInt("salary");

                emp.manager = new Employee();
                emp.manager.first_name = rset.getString("manager");

                emp.dept = new Department();
                emp.dept.dept_no = rset.getString("dept_name");
                return emp;
            } else
                return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    public void displayEmployee(Employee emp) {
        if (emp != null) {
            System.out.println(
                    emp.emp_no + " "
                            + emp.first_name + " "
                            + emp.last_name + "\n"
                            + emp.title + "\n"
                            + "Salary:" + emp.salary + "\n"
                            + emp.dept + "\n"
                            + "Manager: " + emp.manager + "\n");
        }
        else{
            System.out.println("Employee is null");
        }
    }

    public void displayDepartment(Department dept) {
        if (dept != null) {
            System.out.println(
                    dept.dept_no + "\n" +
                            dept.dept_name + "\n" +
                            dept.manager.emp_no);
        }
        else{
            System.out.println("Department is null");
        }
    }

    /**
     * Gets all the current employees and salaries.
     *
     * @return A list of all employees and salaries, or null if there is an error.
     */
    @RequestMapping("salaries")
    public ArrayList<Employee> getAllSalaries() {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                            + "FROM employees, salaries "
                            + "WHERE employees.emp_no = salaries.emp_no AND salaries.to_date = '9999-01-01' "
                            + "ORDER BY employees.emp_no ASC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Employee> employees = new ArrayList<Employee>();
            while (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                employees.add(emp);
            }
            return employees;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }
    }

    /**
     * Prints a list of employees.
     *
     * @param employees The list of employees to print.
     */
    public void printSalaries(ArrayList<Employee> employees)
    {
        // Check employees is not null
        if (employees == null)
        {
            System.out.println("No employees");
            return;
        }
        // Print header
        System.out.println(String.format("%-10s %-15s %-20s %-8s", "Emp No", "First Name", "Last Name", "Salary"));
        // Loop over all employees in the list
        for (Employee emp : employees)
        {
            if (emp == null)
                continue;
            String emp_string =
                    String.format("%-10s %-15s %-20s %-8s",
                            emp.emp_no, emp.first_name, emp.last_name, emp.salary);
            System.out.println(emp_string);
        }
    }

    @RequestMapping("department")
    public Department getDepartment(@RequestParam(value = "dept") String dept_name) {
        try{
            Statement stmt = con.createStatement();

            String strSelect =
                    "SELECT departments.dept_no, departments.dept_name, dept_manager.emp_no " +
                            "FROM departments " +
                            "JOIN dept_manager ON dept_manager.dept_no = departments.dept_no " +
                            "WHERE dept_name = '" + dept_name + "' " +
                            "ORDER BY dept_no ASC";

            ResultSet rset = stmt.executeQuery(strSelect);
            Department dept = new Department();

            if(rset.next()){
                dept.dept_no = rset.getString("departments.dept_no");
                dept.dept_name = rset.getString("departments.dept_name");

                dept.manager = new Employee();
                dept.manager.emp_no = rset.getInt("dept_manager.emp_no");
            }
            return dept;
        } catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("Failed to get department details");
            return null;
        }
    }

    @RequestMapping("salaries_department")
    public ArrayList<Employee> getSalariesByDepartment(@RequestParam(value = "dept") String dept_name) {
        try{
            Statement stmt = con.createStatement();

            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                            + "FROM employees, salaries, dept_emp, departments "
                            + "WHERE employees.emp_no = salaries.emp_no " +
                            "AND employees.emp_no = dept_emp.emp_no " +
                            "AND salaries.to_date = '9999-01-01' " +
                            "AND departments.dept_name = '" + dept_name + "' "
                            + "ORDER BY employees.emp_no ASC";

            ResultSet rset = stmt.executeQuery(strSelect);
            ArrayList<Employee> employees = new ArrayList<Employee>();
            // Extract employee information
            while (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                employees.add(emp);
            }
            return employees;

        } catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }
    }

    @RequestMapping("salaries_title")
    public ArrayList<Employee> getSalariesByTitle(@RequestParam(value = "title") String role) {
        try{
            Statement stmt = con.createStatement();

            String strSelect =
                   "SELECT employees.emp_no, employees.first_name, employees.last_name, " +
                           "titles.title, salaries.salary, departments.dept_name, dept_manager.emp_no " +
                           "FROM employees, salaries, titles, departments, dept_emp, dept_manager " +
                           "WHERE employees.emp_no = salaries.emp_no " +
                           "AND salaries.to_date = '9999-01-01' " +
                           "AND titles.emp_no = employees.emp_no " +
                           "AND titles.to_date = '9999-01-01' " +
                           "AND dept_emp.emp_no = employees.emp_no " +
                           "AND dept_emp.to_date = '9999-01-01' " +
                           "AND departments.dept_no = dept_emp.dept_no " +
                           "AND dept_manager.dept_no = dept_emp.dept_no " +
                           "AND dept_manager.to_date = '9999-01-01' " +
                           "AND titles.title = '" + role + "';";

            ResultSet rset = stmt.executeQuery(strSelect);
            ArrayList<Employee> employees = new ArrayList<Employee>();
            // Extract employee information
            while (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                emp.dept = new Department();
                emp.dept.dept_name = rset.getString("departments.dept_name");
                employees.add(emp);
            }
            return employees;

        } catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }
    }

    public void addEmployee(Employee emp)
    {
        try
        {
            Statement stmt = con.createStatement();
            String strUpdate =
                    "INSERT INTO employees (emp_no, first_name, last_name, birth_date, gender, hire_date) " +
                            "VALUES (" + emp.emp_no + ", '" + emp.first_name + "', '" + emp.last_name + "', " +
                            "'9999-01-01', 'M', '9999-01-01')";
            stmt.execute(strUpdate);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to add employee");
        }
    }

    /**
     * Outputs to Markdown
     *
     * @param employees
     */
    public void outputEmployees(ArrayList<Employee> employees, String filename) {
        // Check employees is not null
        if (employees == null) {
            System.out.println("No employees");
            return;
        }

        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| Emp No | First Name | Last Name | Title | Salary | Department |                    Manager |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- | --- |\r\n");
        // Loop over all employees in the list
        for (Employee emp : employees) {
            if (emp == null) continue;
            sb.append("| " + emp.emp_no + " | " +
                    emp.first_name + " | " + emp.last_name + " | " +
                    emp.title + " | " + emp.salary + " | "
                    + emp.dept.dept_name + " | " + emp.manager + " |\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new                                 File("./reports/" + filename)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}