import java.util.*;
import java.text.*;
import java.math.*;
import com.thinking.machines.common.*;
import com.thinking.machines.hr.bl.interfaces.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.dl.interfaces.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
class EmployeeAddTestCase 
{
public static void main(String gg[])
{
try
{
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
String name;
String title;
java.util.Date dateOfBirth;
BigDecimal basicSalary;
String gender;
boolean isIndian;
String panNumber;
String aadharCardNumber;
int code;
name=Keyboard.getString("Enter Name :");
title=Keyboard.getString("Enter title :");
DesignationManagerInterface designationManager;
designationManager=DesignationManager.getInstance();
DesignationInterface designation;
designation=new Designation();
designation=designationManager.getByTitle(title);
dateOfBirth=sdf.parse(Keyboard.getString("Enter date of birth(dd/mm/yyyy): "));
basicSalary=new BigDecimal(Keyboard.getString("Enter basic salary: "));
gender=Keyboard.getString("Enter gender(M/F): ");
if(gender.equals("M")!=true && gender.equals("F")!=true)
{
throw new RuntimeException("Invalid gender: "+gender);
}
String in=Keyboard.getString("Is Indian (Y/N): ");
if(in.equals("Y")!=true && in.equals("N")!=true)
{
throw new RuntimeException("Invalid Indian factor: "+in);
}
isIndian=(in.equals("Y"))?true:false;
panNumber=Keyboard.getString("Enter the PAN number :");
aadharCardNumber=Keyboard.getString("Enter the Aadhar card number :");
EmployeeInterface employee;
employee=new com.thinking.machines.hr.bl.pojo.Employee();
employee.setName(name);
employee.setDesignation(designation);
employee.setDateOfBirth(dateOfBirth);
employee.setBasicSalary(basicSalary);
if(gender.equals("M")) employee.setGender(EmployeeInterface.MALE);
if(gender.equals("F")) employee.setGender(EmployeeInterface.FEMALE);
employee.isIndian(isIndian);
employee.setPANNumber(panNumber);
employee.setAadharCardNumber(aadharCardNumber);
EmployeeManagerInterface employeeManager;
employeeManager=EmployeeManager.getInstance();
employeeManager.add(employee);
System.out.println("Employee added with employeeId as :"+employee.getEmployeeId());
}catch(BLException blException)
{
if(blException.hasGenericException()) System.out.println(blException.getGenericException());
if(blException.getExceptionCount()>0)
{
Set<String> set=new HashSet<>();
String exception;
set=blException.getProperties();
if(set.size()>0)
{
Iterator<String> i=set.iterator();
while(i.hasNext())
{
exception=i.next();
System.out.println(blException.getException(exception));
}
}
}

}
catch(ParseException parseException)
{
System.out.println(parseException.getMessage());
}
}
}