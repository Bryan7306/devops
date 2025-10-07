# USE CASE: 4 Produce a Report on the Salary of Employees of a Given Role

## CHARACTERISTIC INFORMATION

### Goal in Context

As an *HR advisor* I want *to add a new employee's details* so that *I can ensure the new employee is paid.*

### Scope

Company.

### Level

Primary task.

### Preconditions

We know the role.  Database exists.

### Success End Condition

Employee details are added to database. 

### Failed End Condition

Employee details are not added to database. Employee is not paid.

### Primary Actor

HR Advisor.

### Trigger

A request to add new employee details is sent to HR.

## MAIN SUCCESS SCENARIO

1. Finance request salary information for a given role.
2. HR advisor captures information of employee.
3. HR advisor inputs captures employee information into database.
4. HR advisor provides report to employee.

## EXTENSIONS

3. **Information is invalid**:
    1. HR advisor informs employee information is invalid.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 1.0[use-case-4.md](use-case-4.md)