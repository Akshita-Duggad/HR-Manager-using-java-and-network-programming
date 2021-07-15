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
class EmployeeDeleteTestCase 
{
public static void main(String gg[])
{
try
{
String employeeId;
employeeId=Keyboard.getString("Enter EmployeeId :");
EmployeeManagerInterface employeeManager;
employeeManager=EmployeeManager.getInstance();
employeeManager.delete(employeeId);
System.out.println("Employee deleted");
}catch(BLException blException)
{
System.out.println(blException.getMessage());
}
}
}