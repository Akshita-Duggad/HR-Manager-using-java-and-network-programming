import java.util.*;
import com.thinking.machines.hr.bl.interfaces.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.common.*;
class DesignationAddTestCase
{
public static void main(String gg[])
{
try
{
String title=Keyboard.getString("Enter Title:");
DesignationInterface designation;
designation=new Designation();
designation.setTitle(title);
DesignationManagerInterface designationManager;
designationManager=DesignationManager.getInstance();
designationManager.add(designation);
System.out.println(title+" added with code as : "+designation.getCode());
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
