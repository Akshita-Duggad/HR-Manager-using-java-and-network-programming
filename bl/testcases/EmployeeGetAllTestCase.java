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
class EmployeeGetAllTestCase
{
public static void main(String gg[])
{ 
try
{
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
List<EmployeeInterface> employees;
EmployeeInterface employee;
employee=new com.thinking.machines.hr.bl.pojo.Employee();
EmployeeManagerInterface em;
em=EmployeeManager.getInstance();
employees=em.getAll();
Iterator<EmployeeInterface> employeesIterator=employees.iterator();
int vDesignationCode;
while(employeesIterator.hasNext())
{
employee=employeesIterator.next();
System.out.println("Employee Id : "+employee.getEmployeeId());
System.out.println("Name : "+employee.getName());
System.out.println("Designation Code : "+employee.getDesignation().getCode());
System.out.println("Designation : "+employee.getDesignation().getTitle());
System.out.println("Date of birth : "+sdf.format(employee.getDateOfBirth()));
System.out.println("Basic Salary : "+employee.getBasicSalary().toPlainString());
System.out.println("Gender : "+employee.getGender());
System.out.println("Is Indian : "+employee.isIndian());
System.out.println("PAN Number : "+employee.getPANNumber());
System.out.println("Aadhar card number : "+employee.getAadharCardNumber());
}
}catch(BLException blException)
{
System.out.println(blException.getMessage());
}
}
}