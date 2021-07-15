import com.thinking.machines.hr.bl.interfaces.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;
class CodeExistsTestCase
{
public static void main(String gg[])
{
try
{
DesignationManagerInterface designationManager;
designationManager=DesignationManager.getInstance();
boolean codeExists=designationManager.codeExists(13);
if(codeExists==true)
{
System.out.println("Code exists");
}
else
{
System.out.println("Code does not exists");
}

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