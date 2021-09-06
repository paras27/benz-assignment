package demo.employee.service;

import java.util.ArrayList;
import java.util.List;

import demo.interfaces.grpc.Employee;

public class EmployeeResourceProvider {
	
	/**
	 * In order to simplify the solution, use this static list as source of
	 * Employees
	 * 
	 * @return Employee List
	 */
	public static List<Employee> getEmployeeListfromEmployeeSource() {
		return new ArrayList<Employee>() {
			{
				add(Employee.newBuilder().setEmployeeID(1l).setEmployeeFirstName("Paras")
						.setEmployeeLastName("Charpe").setEmployeeDateOfBirth(709324200000l)
						.setEmployeeWorkingYears(30).setFileType("CSV").build());
				add(Employee.newBuilder().setEmployeeID(2l).setEmployeeFirstName("Thought")
						.setEmployeeLastName("Focus").setEmployeeDateOfBirth(677701800000l)
						.setEmployeeWorkingYears(40).setFileType("CSV").build());
				add(Employee.newBuilder().setEmployeeID(3l).setEmployeeFirstName("Reserve")
						.setEmployeeLastName("Bank").setEmployeeDateOfBirth(828124200000l)
						.setEmployeeWorkingYears(35).setFileType("CSV").build());

				add(Employee.newBuilder().setEmployeeID(1l).setEmployeeFirstName("Paras")
						.setEmployeeLastName("Charpe").setEmployeeDateOfBirth(709324200000l)
						.setEmployeeWorkingYears(30).setFileType("XML").build());
				add(Employee.newBuilder().setEmployeeID(2l).setEmployeeFirstName("Thought")
						.setEmployeeLastName("Focus").setEmployeeDateOfBirth(677701800000l)
						.setEmployeeWorkingYears(40).setFileType("XML").build());
				add(Employee.newBuilder().setEmployeeID(3l).setEmployeeFirstName("Reserve")
						.setEmployeeLastName("Bank").setEmployeeDateOfBirth(828124200000l)
						.setEmployeeWorkingYears(35).setFileType("XML").build());
			}
		};
	}
}
