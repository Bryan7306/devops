# USE CASE: 4 Produce a Report on the Salary of Employees of a Given Role

## CHARACTERISTIC INFORMATION

### Goal in Context

As an *HR advisor* I want *to delete an employee's details* so that *the company is compliant with data retention legislation.*

### Scope

Company.

### Level

Primary task.

### Preconditions

Database contains current employee data.

### Success End Condition

Employee data is successfully deleted from database.

### Failed End Condition

Employee data still exists within database.

### Primary Actor

HR Advisor.

### Trigger

A request for employee data removal is sent to HR.

## MAIN SUCCESS SCENARIO

1. A request for employee data removal is sent to HR.
2. HR advisor captures name of the employee to get information for.
3. HR advisor deletes information of given employee from the database
4. HR advisor provides report to given employee.

## EXTENSIONS

3. **Employee data does not exist**:
    1. HR advisor informs employee no data exists.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 1.0[use-case-4.md](use-case-4.md)