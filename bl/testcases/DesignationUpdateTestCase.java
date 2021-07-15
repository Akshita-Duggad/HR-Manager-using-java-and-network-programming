import java.util.*;
import com.thinking.machines.hr.bl.interfaces.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.common.*;
class DesignationUpdateTestCase
{
public static void main(String gg[])
{
try
{
int code=Keyboard.getInt("Enter Code:");
String title=Keyboard.getString("Enter Title:");
DesignationInterface designation;
designation=new Designation();
designation.setCode(code);
designation.setTitle(title);
DesignationManagerInterface designationManager;
designationManager=DesignationManager.getInstance();
designationManager.update(designation);
System.out.println("Desigantion Updated");
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

}
}
