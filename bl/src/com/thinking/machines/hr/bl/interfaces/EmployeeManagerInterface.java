package com.thinking.machines.hr.bl.interfaces;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;
public interface EmployeeManagerInterface
{
public boolean isDesignationAllocated(int code);
public void add(EmployeeInterface employee) throws BLException;
public void update(EmployeeInterface employee) throws BLException;
public void delete(String employeeId) throws BLException;
public EmployeeInterface getByEmployeeId(String employeeId) throws BLException;
public EmployeeInterface getByPANNumber(String panNumber) throws BLException;
public EmployeeInterface getByAadharCardNumber(String aadharCardNumber) throws BLException;
public List<EmployeeInterface>getAll();
public List<EmployeeInterface>getByDesignation(int designationCode) throws BLException;
public List<EmployeeInterface>getByDateOfBirth(java.util.Date dateOfBirth) throws BLException;
public int getCount();
public boolean employeeIdExists(String employeeId);
public boolean panNumberExists(String panNumber);
public boolean aadharCardNumberExists(String aadharCardNumber);
}