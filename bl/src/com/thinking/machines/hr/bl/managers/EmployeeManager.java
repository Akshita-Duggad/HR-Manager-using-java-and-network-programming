package com.thinking.machines.hr.bl.managers;
import com.thinking.machines.common.*;
import com.thinking.machines.hr.bl.interfaces.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;
import java.math.*;
import java.text.*;
import java.io.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
public class EmployeeManager implements EmployeeManagerInterface
{
private static EmployeeManagerInterface employeeManager;
private static Map<Integer,List<EmployeeInterface>> designationWiseEmployee;
private static List<EmployeeInterface> employeeIdWiseOrderedList;
private EmployeeManager()
{
this.employeeManager=null;
}
public static EmployeeManagerInterface getInstance() throws BLException
{
if(employeeManager==null)
{
employeeManager=new EmployeeManager();
((EmployeeManager)employeeManager).populateDataStructure();
}
return employeeManager; 
}
private static void populateDataStructure() throws BLException
{
designationWiseEmployee=new HashMap<>();
employeeIdWiseOrderedList=new LinkedList<>();
int designationCode;
String gender;
try
{
EmployeeDAO employeeDAO;
employeeDAO=new EmployeeDAO();
EmployeeInterface employee;
DesignationInterface designation;
DesignationManagerInterface designationManager;
designationManager=DesignationManager.getInstance();
List<EmployeeDTOInterface> elEmployeeDTOs;
elEmployeeDTOs=new LinkedList<>();
elEmployeeDTOs=employeeDAO.getAll();
String employeeId;
for(EmployeeDTOInterface elEmployeeDTO:elEmployeeDTOs)
{
employee=new com.thinking.machines.hr.bl.pojo.Employee();
designationCode=elEmployeeDTO.getDesignationCode();
designation=new Designation();
designation=designationManager.getByCode(designationCode);
POJOCopier.copy(employee,elEmployeeDTO);
employee.setDesignation(designation);
employee.setBasicSalary(elEmployeeDTO.getBasicSalary());
employee.setDateOfBirth(elEmployeeDTO.getDateOfBirth());
gender=elEmployeeDTO.getGender();
if(gender.equals("M")) employee.setGender(EmployeeInterface.MALE);
if(gender.equals("F")) employee.setGender(EmployeeInterface.FEMALE);
employee.isIndian(elEmployeeDTO.isIndian());
if(designationWiseEmployee.containsKey(designationCode)==true)
{
List list=designationWiseEmployee.get(designationCode);
list.add(employee);
designationWiseEmployee.replace(new Integer(designationCode),list);
}
else
{
List<EmployeeInterface> list=new LinkedList<>();
list.add(employee);
designationWiseEmployee.put(designationCode,list);
}
employeeIdWiseOrderedList.add(employee);
}
Collections.sort(employeeIdWiseOrderedList,new Comparator<EmployeeInterface>(){
public int compare (EmployeeInterface left,EmployeeInterface right)
{
String leftEmployeeId=left.getEmployeeId().toUpperCase();
String rightEmployeeId=right.getEmployeeId().toUpperCase();
return leftEmployeeId.compareTo(rightEmployeeId);
}
});
}catch(DAOException daoException)
{
throw new BLException(daoException.getMessage());
}
}
public void add(EmployeeInterface employee) throws BLException
{
BLException blException=new BLException();
String employeeId;
String gender;
String name;
Date dateOfBirth;
BigDecimal basicSalary;
boolean isIndian;
String panNumber;
String aadharCardNumber;
DesignationInterface designation;
designation=new Designation();
try
{
employeeId=employee.getEmployeeId();
gender=employee.getGender();
name=employee.getName();
dateOfBirth=employee.getDateOfBirth();
basicSalary=employee.getBasicSalary();
isIndian=employee.isIndian();
panNumber=employee.getPANNumber();
aadharCardNumber=employee.getAadharCardNumber();
DesignationManagerInterface designationManager;
designationManager=DesignationManager.getInstance();
POJOCopier.copy(designation,employee.getDesignation());
int designationCode=designation.getCode();
if(gender.equals(null)) blException.addException("gender","Invalid gender");
if(name.equals(null)) blException.addException("name","Invald name");
if(basicSalary==null)
{
blException.addException("Basic Salary","Invalid Basic Salary");
} 
if(basicSalary.intValue()==0)
{
blException.addException("Basic Salary","Invalid Basic Salary");
}
if(panNumber.equals(null)) blException.addException("panNumber","Invalid PAN Number");
if(aadharCardNumber.equals(null)) blException.addException("aadharCardNumber","Invalid Aadhar card number");
if(designation.getCode()==0) blException.addException("code","Invalid code");
if(designation.getTitle().equals("null")) blException.addException("title","Invalid title");
if(!designationManager.codeExists(designation.getCode())) blException.addException("Designation code","Designation code doesnot exists");
if(!designationManager.titleExists(designation.getTitle())) blException.addException("Designation title","Designation title doesnot exists");
if(blException.getExceptionCount()>0)
{
throw blException;
}
EmployeeDTOInterface employeeDTO;
employeeDTO=new EmployeeDTO();
POJOCopier.copy(employeeDTO,employee);
employeeDTO.setDesignationCode(employee.getDesignation().getCode());
String gender1=employee.getGender();
employeeDTO.setBasicSalary(employee.getBasicSalary());
employeeDTO.setDateOfBirth(employee.getDateOfBirth());
if(gender1.equals("M")) employeeDTO.setGender(EmployeeDTOInterface.MALE);
if(gender1.equals("F")) employeeDTO.setGender(EmployeeDTOInterface.FEMALE);
employeeDTO.isIndian(employee.isIndian());
EmployeeDAO employeeDAO;
employeeDAO=new EmployeeDAO();
employeeDAO.add(employeeDTO);
EmployeeInterface employeeInterface;
employeeInterface=new com.thinking.machines.hr.bl.pojo.Employee();
employeeId=employeeDTO.getEmployeeId();
employee.setEmployeeId(employeeId);
POJOCopier.copy(employeeInterface,employee);
employeeInterface.setBasicSalary(employee.getBasicSalary());
employeeInterface.setDateOfBirth(employee.getDateOfBirth());
if(gender1.equals("M")) employeeDTO.setGender(EmployeeDTOInterface.MALE);
if(gender1.equals("F")) employeeDTO.setGender(EmployeeDTOInterface.FEMALE);
employeeInterface.isIndian(employee.isIndian());
if(designationWiseEmployee.containsKey(designationCode))
{
List list=designationWiseEmployee.get(designationCode);
list.add(employeeInterface);
designationWiseEmployee.replace(new Integer(designationCode),list);
}
else
{
List<EmployeeInterface> list;
list=new LinkedList<>();
list.add(employeeInterface);
designationWiseEmployee.put(new Integer(designationCode),list);
}
employeeIdWiseOrderedList.add(employeeInterface);
}catch(DAOException daoException)
{
blException.addException("dao",daoException.getMessage());
}
}
public void update(EmployeeInterface employee) throws BLException
{
BLException blException=new BLException();
String employeeId;
String gender;
String name;
Date dateOfBirth;
BigDecimal basicSalary;
boolean isIndian;
String panNumber;
String aadharCardNumber;
DesignationInterface designation;
designation=new Designation();
try
{
employeeId=employee.getEmployeeId();
gender=employee.getGender();
name=employee.getName();
dateOfBirth=employee.getDateOfBirth();
basicSalary=employee.getBasicSalary();
isIndian=employee.isIndian();
panNumber=employee.getPANNumber();
aadharCardNumber=employee.getAadharCardNumber();
POJOCopier.copy(designation,employee.getDesignation());
int designationCode=designation.getCode();
if(employeeId==null)
blException.addException(employeeId,"Invalid EmployeeId");
if(gender==null)
blException.addException(gender,"Invalid gender");
if(name==null)
blException.addException(name,"Invald name");
if(basicSalary==null) 
blException.addException("basic salary","Invalid Basic Salary");
if(basicSalary.intValue()==0) blException.addException("basic salary","Invalid Basic Salary");
if(panNumber==null) 
blException.addException(panNumber,"Invalid PAN Number");
if(aadharCardNumber==null) 
blException.addException(aadharCardNumber,"Invalid Aadhar card number");
if(designation.getCode()==0) 
blException.addException("code","Invalid code");
if(designation.getTitle()==null) 
blException.addException("title","Invalid title");
if(!employeeIdExists(employeeId)) blException.addException("Employee Id","EmployeeId does not exists");
DesignationManagerInterface designationManager;
designationManager=DesignationManager.getInstance();
if(!designationManager.codeExists(designationCode)) blException.addException("Designation code","Designation code doesnot exists");
if(!designationManager.titleExists(designation.getTitle())) blException.addException("Designation title","Designation title doesnot exists");
if(blException.getExceptionCount()>0)
{
throw blException;
}
EmployeeDTOInterface employeeDTO;
employeeDTO=new EmployeeDTO();
POJOCopier.copy(employeeDTO,employee);
employeeDTO.setDesignationCode(designationCode);
employeeDTO.setDateOfBirth(employee.getDateOfBirth());
employeeDTO.setBasicSalary(employee.getBasicSalary());
if(employee.getGender().equals("M")) employeeDTO.setGender(EmployeeDTOInterface.MALE);
if(employee.getGender().equals("F")) employeeDTO.setGender(EmployeeDTOInterface.FEMALE);
employeeDTO.isIndian(employee.isIndian());
EmployeeDAOInterface employeeDAO;
employeeDAO=new EmployeeDAO();
employeeDAO.update(employeeDTO);
EmployeeInterface elEmployee;
elEmployee=new com.thinking.machines.hr.bl.pojo.Employee();
POJOCopier.copy(elEmployee,employee);
elEmployee.setBasicSalary(employee.getBasicSalary());
elEmployee.setDateOfBirth(employee.getDateOfBirth());
if(employee.getGender().equals("M")) elEmployee.setGender(EmployeeInterface.MALE);
if(employee.getGender().equals("F")) elEmployee.setGender(EmployeeInterface.FEMALE);
elEmployee.isIndian(employee.isIndian());
int i=0;
int code=0;
for(EmployeeInterface e:employeeIdWiseOrderedList)
{
if(employeeId.equals(e.getEmployeeId()))
{
code=e.getDesignation().getCode();
employeeIdWiseOrderedList.remove(i);
break;
}
i++;
}
employeeIdWiseOrderedList.add(i,elEmployee);

List<EmployeeInterface> list=designationWiseEmployee.get(designationCode);
if(list==null)
{
List<EmployeeInterface> l=designationWiseEmployee.get(code);
int j=0;
for(EmployeeInterface ei:l)
{
if(employeeId.equals(ei.getEmployeeId()))
{
l.remove(j);
designationWiseEmployee.replace(new Integer(designationCode),l);
}
j++;
}
list=new LinkedList<>();
list.add(elEmployee);
designationWiseEmployee.put(new Integer(designationCode),list);
}
else
{
for(EmployeeInterface e:list)
{
if(employeeId.equals(e.getEmployeeId()))
{
list.remove(e);
list.add(elEmployee);
designationWiseEmployee.replace(new Integer(designationCode),list);
break;
}
}
}
}catch(DAOException daoException)
{
blException.addException("dao",daoException.getMessage()); 
}
}
public void delete(String employeeId) throws BLException
{
BLException blException;
blException=new BLException();
try
{
if(employeeId==null) blException.addException("employeeId","Invalid Employee Id");
if(employeeIdExists(employeeId)==false) blException.addException("employeeId","Employee Id does not exists");
if(blException.getExceptionCount()>0)
{
throw blException;
}
EmployeeDAOInterface employeeDAO;
employeeDAO=new EmployeeDAO();
employeeDAO.delete(employeeId);
String fEmployeeId;
int i=0;
int code=0;
for(EmployeeInterface e:employeeIdWiseOrderedList)
{
fEmployeeId=e.getEmployeeId();
if(fEmployeeId.equals(employeeId))
{
code=e.getDesignation().getCode();
employeeIdWiseOrderedList.remove(i);
break;
}
i++;
}
int j=0;
List<EmployeeInterface> list;
list=designationWiseEmployee.get(code);
for(EmployeeInterface ei:list)
{
fEmployeeId=ei.getEmployeeId();
if(fEmployeeId.equals(employeeId))
{
list.remove(j);
break;
}
j++;
}
designationWiseEmployee.replace(new Integer(code),list);
}catch(DAOException daoException)
{
blException.addException("dao",daoException.getMessage());
}
}
public EmployeeInterface getByEmployeeId(String employeeId) throws BLException
{
BLException blException;
blException=new BLException();
EmployeeInterface employee;
employee=new com.thinking.machines.hr.bl.pojo.Employee();
try
{
if(employeeId==null) 
{
blException.addException("employee id","Invalid employee id");
}
if(employeeIdExists(employeeId)==false)
{
blException.addException("employee id","employee id does not exists");
}
if(blException.getExceptionCount()>0)
{
throw blException;
}
String fEmployeeId;
for(EmployeeInterface e:employeeIdWiseOrderedList)
{
fEmployeeId=e.getEmployeeId();
if(fEmployeeId.equals(employeeId))
{
POJOCopier.copy(employee,e);
employee.setDesignation(e.getDesignation());
employee.setBasicSalary(e.getBasicSalary());
employee.setDateOfBirth(e.getDateOfBirth());
employee.isIndian(e.isIndian());
if(e.getGender().equals("M")) employee.setGender(EmployeeInterface.MALE);
if(e.getGender().equals("F")) employee.setGender(EmployeeInterface.FEMALE);
break;
}
}
return employee;
}catch(RuntimeException runtimeException)
{
blException.addException("re",runtimeException.getMessage());
throw blException;
}
}
public EmployeeInterface getByPANNumber(String panNumber) throws BLException
{
BLException blException;
blException=new BLException();
try
{
if(panNumber==null)
{
blException.addException("panNumber","Enter PAN Number");
}
if(panNumberExists(panNumber)==false)
{
blException.addException("panNumber","PAN Number does not exists");
}
String fPanNumber;
EmployeeInterface employee;
employee=new com.thinking.machines.hr.bl.pojo.Employee();
for(EmployeeInterface e:employeeIdWiseOrderedList)
{
fPanNumber=e.getPANNumber();
if(fPanNumber.equals(panNumber))
{
POJOCopier.copy(employee,e);
employee.setDesignation(e.getDesignation());
employee.setBasicSalary(e.getBasicSalary());
employee.setDateOfBirth(e.getDateOfBirth());
employee.isIndian(e.isIndian());
if(e.getGender().equals("M")) employee.setGender(EmployeeInterface.MALE);
if(e.getGender().equals("F")) employee.setGender(EmployeeInterface.FEMALE);
break;
}
}
return employee;
}catch(RuntimeException runtimeException)
{
blException.addException("re",runtimeException.getMessage());
throw blException;
}
}
public EmployeeInterface getByAadharCardNumber(String aadharCardNumber) throws BLException
{
BLException blException;
blException=new BLException();
try
{
if(aadharCardNumber==null) 
{
blException.addException("Aadhar card number","Invlaid Aadhar CardNumber");
}
if(aadharCardNumberExists(aadharCardNumber)==false)
{
blException.addException("Aadhar card number","AadharCardNumber doesnot exists");
}
String fAadharCardNumber;
EmployeeInterface employee;
employee=new com.thinking.machines.hr.bl.pojo.Employee();
for(EmployeeInterface e:employeeIdWiseOrderedList)
{
fAadharCardNumber=e.getAadharCardNumber();
if(fAadharCardNumber.equals(aadharCardNumber))
{
POJOCopier.copy(employee,e); 
employee.setDesignation(e.getDesignation());
employee.setBasicSalary(e.getBasicSalary());
employee.setDateOfBirth(e.getDateOfBirth());
employee.isIndian(e.isIndian());
if(e.getGender().equals("M")) employee.setGender(EmployeeInterface.MALE);
if(e.getGender().equals("F")) employee.setGender(EmployeeInterface.FEMALE);
break;
}
}
return employee;
}catch(RuntimeException runtimeException)
{
blException.addException("re",runtimeException.getMessage());
throw blException;
}
}
public List<EmployeeInterface> getAll()
{
try
{
List<EmployeeInterface> list;
list=new LinkedList<>();
EmployeeInterface e;
e=new com.thinking.machines.hr.bl.pojo.Employee();
Iterator<EmployeeInterface> employeesIterator=employeeIdWiseOrderedList.iterator();
while(employeesIterator.hasNext())
{
EmployeeInterface employee;
employee=new com.thinking.machines.hr.bl.pojo.Employee();
e=employeesIterator.next();
employee.setEmployeeId(e.getEmployeeId());
employee.setName(e.getName());
employee.setDesignation(e.getDesignation());
employee.setDateOfBirth(e.getDateOfBirth());
employee.setBasicSalary(e.getBasicSalary());
if(e.getGender()=="M")
{
employee.setGender(EmployeeInterface.MALE);
}
if(e.getGender()=="F")
{
employee.setGender(EmployeeInterface.FEMALE);
}
employee.isIndian(e.isIndian());
employee.setPANNumber(e.getPANNumber());
employee.setAadharCardNumber(e.getAadharCardNumber());
list.add(employee);
}
return list;
}catch(RuntimeException runtimeException)
{
throw new RuntimeException(runtimeException.getMessage());
}
}
public List<EmployeeInterface> getByDesignation(int designationCode) throws BLException
{
List<String> l=new ArrayList<>();
BLException blException;
blException=new BLException();
try
{
if(designationCode==0) blException.addException("code"," Invalid code");
if(designationWiseEmployee.containsKey(designationCode)==false) 
{
blException.addException("code","Code doesnot exists");
}
if(blException.getExceptionCount()>0)
{
throw blException;
}
List<EmployeeInterface> list;
list=new LinkedList<>();
list=designationWiseEmployee.get(designationCode);
return list;
}catch(RuntimeException runtimeException)
{
blException.addException("re",runtimeException.getMessage());
throw blException;
}
}
public List<EmployeeInterface> getByDateOfBirth(java.util.Date dateOfBirth) throws BLException
{
BLException blException;
blException=new BLException();
try
{
if(dateOfBirth==null) blException.addException("date of birth","Invalid date of birth");
if(blException.getExceptionCount()>0) throw blException;
java.util.Date fDateOfBirth;
List<EmployeeInterface> list;
list=new LinkedList<>();
EmployeeInterface employee;
employee=new com.thinking.machines.hr.bl.pojo.Employee();
for(EmployeeInterface e:employeeIdWiseOrderedList)
{
fDateOfBirth=e.getDateOfBirth();
if(fDateOfBirth.equals(dateOfBirth)) 
{
POJOCopier.copy(employee,e);
employee.setDesignation(e.getDesignation());
employee.setBasicSalary(e.getBasicSalary());
employee.setDateOfBirth(e.getDateOfBirth());
employee.isIndian(e.isIndian());
if(e.getGender().equals("M")) employee.setGender(EmployeeInterface.MALE);
if(e.getGender().equals("F")) employee.setGender(EmployeeInterface.FEMALE);
list.add(employee);
}
}
return list;
}catch(RuntimeException runtimeException)
{
blException.addException("re",runtimeException.getMessage());
throw blException;
}
}
public int getCount()
{
try
{
return employeeIdWiseOrderedList.size();
}catch(RuntimeException runtimeException)
{
throw new RuntimeException(runtimeException.getMessage());
}
}
public boolean employeeIdExists(String employeeId)
{
try
{
String fEmployeeId;
for(EmployeeInterface employee:employeeIdWiseOrderedList)
{
fEmployeeId=employee.getEmployeeId();
if(fEmployeeId.equals(employeeId))
{
return true;
} 
}
return false;
}catch(RuntimeException runtimeException)
{
throw new RuntimeException(runtimeException.getMessage());
}
}
public boolean panNumberExists(String panNumber)
{
try
{
String fPanNumber;
for(EmployeeInterface employee:employeeIdWiseOrderedList)
{
fPanNumber=employee.getPANNumber();
if(fPanNumber.equals(panNumber))
{
return true;
} 
}
return false;
}catch(RuntimeException runtimeException)
{
throw new RuntimeException(runtimeException.getMessage());
}
}
public boolean aadharCardNumberExists(String aadharCardNumber)
{
try
{
String fAadharCardNumber;
for(EmployeeInterface employee:employeeIdWiseOrderedList)
{
fAadharCardNumber=employee.getAadharCardNumber();
if(fAadharCardNumber.equals(aadharCardNumber))
{
return true;
} 
}
return false;
}catch(RuntimeException runtimeException)
{
throw new RuntimeException(runtimeException.getMessage());
}
}
public boolean isDesignationAllocated(int designationCode)
{
if(designationWiseEmployee.containsKey(designationCode)) return true;
return false;
}
}