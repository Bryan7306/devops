//package com.napier.sem;
//
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.boot.SpringApplication;
//
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class AppIntegrationTest
//{
//    static App app;
//
//    @BeforeAll
//    static void init()
//    {
//        // Connect to database
//
//
//    }
//
//    @Test
//    void testGetEmployee()
//    {
//        Employee emp = (255530);
//        assertEquals(emp.emp_no, 255530);
//        assertEquals(emp.first_name, "Ronghao");
//        assertEquals(emp.last_name, "Garigliano");
//    }
//
//    @Test
//    void testGetDepartment()
//    {
//        Department dept = app.getDepartment("Sales");
//        assertEquals(dept.dept_no, "d007");
//        assertEquals(dept.manager.emp_no, 111035);
//    }
//
////    @Test
////    void testAddEmployee()
////    {
////        Employee emp = new Employee();
////        emp.emp_no = 1;
////        emp.first_name = "Kevin";
////        emp.last_name = "Chalmers";
////        app.addEmployee(emp);
////
////        Employee testEmp = app.getEmployee(1);
////        assertEquals(testEmp.emp_no, 1);
////        assertEquals(testEmp.first_name, "Kevin");
////        assertEquals(testEmp.last_name, "Chalmers");
////    }
//}