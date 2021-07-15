import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.managers.*;
import java.util.*;
class aaa
{
public static void main(String gg[])
{
try
{
DesignationManagerInterface designationManager;
designationManager=DesignationManager.getInstance();
List<DesignationInterface> list=designationManager.getDesignations(DesignationInterface.TITLE);
System.out.println(list.size());
for(DesignationInterface designation:list)
{
System.out.printf("Code %d,Title %s\n",designation.getCode(),designation.getTitle());
}
}catch(Exception e)
{
System.out.println(e);
}
}
}