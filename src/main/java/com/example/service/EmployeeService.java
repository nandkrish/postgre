package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Employee;
import com.example.repository.EmployeeRepository;

@Service("employeeService")
public class EmployeeService {
	
	@Autowired
	  private EmployeeRepository empRepo;
	       
	   
	    public List<Employee> getAllEmps() {
	        List<Employee> listStudents = empRepo.findAll();
	        
	           
	        return listStudents;
	    }

}
