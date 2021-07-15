package com.thinking.machines.hr.bl.managers;
import com.thinking.machines.hr.bl.interfaces.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.pojo.*;
import java.util.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.interfaces.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.common.*;
public class DesignationManager implements DesignationManagerInterface 
{
private static DesignationManagerInterface designationManager;
Map<Integer,DesignationInterface>codeWiseMap;
Map<String,DesignationInterface>titleWiseMap;
List<DesignationInterface>codeWiseOrderedList;
List<DesignationInterface>titleWiseOrderedList;
private DesignationManager()
{
this.designationManager=null;
}
public static DesignationManagerInterface getInstance()throws BLException
{
if(designationManager==null)
{
designationManager=new DesignationManager();
((DesignationManager)designationManager).populateDataStructures();
}
return designationManager;
}
private void populateDataStructures() throws BLException
{
codeWiseMap=new HashMap<>();
titleWiseMap=new HashMap<>();
codeWiseOrderedList=new LinkedList<>();
titleWiseOrderedList=new LinkedList<>();
try
{
DesignationDAOInterface designationDAO;
designationDAO=new DesignationDAO();
List<DesignationDTOInterface>dlDesignations=designationDAO.getAll();
DesignationInterface designation;
int code;
String title;
for(DesignationDTOInterface dlDesignation:dlDesignations)
{
designation=new Designation();
POJOCopier.copy(designation,dlDesignation);
code=designation.getCode();
title=designation.getTitle();
codeWiseMap.put(new Integer(code),designation);
titleWiseMap.put(title.toUpperCase(),designation);
codeWiseOrderedList.add(designation);
titleWiseOrderedList.add(designation);
}
Collections.sort(codeWiseOrderedList);
Collections.sort(titleWiseOrderedList,new Comparator<DesignationInterface>(){
public int compare (DesignationInterface left,DesignationInterface right)
{
String leftTitle=left.getTitle().toUpperCase();
String rightTitle=right.getTitle().toUpperCase();
return leftTitle.compareTo(rightTitle);
}
});
}catch(DAOException dao)
{
throw new BLException(dao.getMessage());
}
}
public void add(DesignationInterface designation)throws BLException
{
BLException blException=new BLException();
try
{
if(designation==null)
{
blException.addException("designation","designation should not be null");
}
if(designation.getTitle()==null)
{
blException.addException("title","title should not be null");
}
if(designation.getTitle().trim().length()==0)
{
blException.addException("title","No title found");
}
if(designation.getTitle().length()>35)
{
blException.addException("title","Length should not exceed 35 characters");
}
if(titleWiseMap.containsKey(designation.getTitle().toUpperCase()))
{
blException.addException("title","Title already exists");
}
if(blException.getExceptionCount()>0) throw blException;
DesignationDAOInterface designationDAO;
designationDAO=new DesignationDAO();
DesignationDTOInterface designationDTO=new DesignationDTO();
designationDTO.setTitle(designation.getTitle());
designationDAO.add(designationDTO);
designation.setCode(designationDTO.getCode());
DesignationInterface dsDesignation=new Designation();
POJOCopier.copy(dsDesignation,designation);
int code;
code=dsDesignation.getCode();
String title;
title=dsDesignation.getTitle();
codeWiseMap.put(new Integer(code),dsDesignation);
titleWiseMap.put(title.toUpperCase(),dsDesignation);
int lb;
int ub;
int mid;
if(codeWiseOrderedList.size()==0)
{
titleWiseOrderedList.add(dsDesignation);
codeWiseOrderedList.add(dsDesignation);
return;
}
lb=0;
ub=codeWiseOrderedList.size()-1;
while(lb<=ub)
{
mid=(ub+lb)/2;
if(code<codeWiseOrderedList.get(mid).getCode())
{
ub=mid-1;
continue;
}
else
{
lb=mid+1;
continue;
}
}
codeWiseOrderedList.add(lb,dsDesignation);
lb=0;
ub=titleWiseOrderedList.size()-1;
while(lb<=ub)
{
mid=(ub+lb)/2;
if(title.compareTo(titleWiseOrderedList.get(mid).getTitle())<0)
{
ub=mid-1;
continue;
}
else
{
lb=mid+1;
continue;
}
}
titleWiseOrderedList.add(lb,dsDesignation);
}catch(DAOException dao)
{
blException.addException("dao",dao.getMessage());
throw blException;
}
}
public void delete(int code)throws BLException
{
BLException blException;
blException=new BLException();
try
{
if(code<=0) blException.addException("code","Invalid code");
DesignationDAOInterface designationDAO;
designationDAO=new DesignationDAO();
if(codeWiseMap.containsKey(code)==false) 
{
blException.addException("code","Code does not exists");
}
if(isAttachedToAnEmployee(code)) blException.addException("code","Code exists against Employee");
if(blException.getExceptionCount()>0) throw blException;
DesignationInterface designation;
designation=codeWiseMap.get(code);
designationDAO.delete(code);
int fCode;
for(int x=0;x<codeWiseOrderedList.size();x++)
{
fCode=codeWiseOrderedList.get(x).getCode();
if(code==fCode)
{
codeWiseOrderedList.remove(x);
break;
}
}
for(int x=0;x<titleWiseOrderedList.size();x++)
{
fCode=titleWiseOrderedList.get(x).getCode();
if(code==fCode)
{
titleWiseOrderedList.remove(x);
break;
}
}
codeWiseMap.remove(code);
titleWiseMap.remove(designation.getTitle().toUpperCase());
}catch(DAOException daoException)
{
blException.addException("dao",daoException.getMessage());
throw blException;
}
}
public void update(DesignationInterface designation)throws BLException
{
BLException blException=new BLException();
try
{
int code=designation.getCode();
String title=designation.getTitle();
if(designation==null)
{
blException.addException("designation","designation should not be null");
}
if(title==null)
{
blException.addException("title","title should not be null");
}
if(code<=0)
{
blException.addException("code","should be greater than 0");
}
if(codeWiseMap.containsKey(code)==false)
{
blException.addException("code","Code does not exists");
}
if(title.trim().length()==0)
{
blException.addException("title","No title found");
}
if(title.length()>35)
{
blException.addException("title","Length should not exceed 35 characters");
}
DesignationDAOInterface designationDAO;
designationDAO=new DesignationDAO();
DesignationInterface d;
d=new Designation();
DesignationDTOInterface designationDTO;
designationDTO=new DesignationDTO();
if(titleWiseMap.containsKey(title.toUpperCase())==true)
{
d=getByTitle(title);
if(d.getCode()!=code) blException.addException("Title","Title already exists against other code");
}
if(blException.getExceptionCount()>0) throw blException;
POJOCopier.copy(designationDTO,designation);
designationDAO.update(designationDTO);
codeWiseMap.replace(code,designation);
titleWiseMap.replace(title.toUpperCase(),designation);
int fCode=0;
String fTitle="";
for(int i=0;i<codeWiseOrderedList.size();i++)
{
fCode=codeWiseOrderedList.get(i).getCode();
if(fCode==code)
{
codeWiseOrderedList.get(i).setTitle(title);
break;
}
}
for(int j=0;j<titleWiseOrderedList.size();j++)
{
fCode=titleWiseOrderedList.get(j).getCode();
fTitle=titleWiseOrderedList.get(j).getTitle();
if(fCode==code)
{
titleWiseOrderedList.remove(j);
break;
}
}
boolean smaller=false;
for(int k=0;k<titleWiseOrderedList.size();k++)
{
fTitle=titleWiseOrderedList.get(k).getTitle();
if(fTitle.compareTo(title)>0)
{
smaller=true;
titleWiseOrderedList.add(k,designation);
break;
}
}
if(smaller==false)
{
titleWiseOrderedList.add(designation);
}
}catch(DAOException dao)
{
blException.addException("dao",dao.getMessage());
throw blException;
}
}
public List<DesignationInterface>getDesignations(DesignationInterface.ATTRIBUTE ...OrderBy)
{
List<DesignationInterface>list;
DesignationInterface designation;
list=new LinkedList<>();
if(OrderBy.length==0 || OrderBy[0]==DesignationInterface.TITLE)
{
for(DesignationInterface d:titleWiseOrderedList)
{
designation=new Designation();
POJOCopier.copy(designation,d);
list.add(designation);
}
}
else
{
for(DesignationInterface d:codeWiseOrderedList)
{
designation=new Designation();
POJOCopier.copy(designation,d);
list.add(designation);
}
}
return list;
}
public DesignationInterface getByCode(int code)throws BLException
{
BLException blException=new BLException();
if(code<=0)
{
blException.addException("code","Should be greater than zero");
}
if(codeWiseMap.containsKey(code)==false)
{
blException.addException("code","Code does not exists");
}
if(blException.getExceptionCount()>0)
{
throw blException;
}
DesignationInterface ds=new Designation();
DesignationInterface designation=new Designation();
ds=codeWiseMap.get(new Integer(code));
POJOCopier.copy(designation,ds);
return designation;
}
public DesignationInterface getByTitle(String title)throws BLException
{
BLException blException;
blException=new BLException();
if(title.trim()==null) blException.addException("title","Invalid title");
if(title.trim().length()<0 || title.trim().length()>35) blException.addException("title","Title should not exceed 35 letters");
if(titleWiseMap.containsKey(title.toUpperCase())==false) blException.addException("title","Title doesnot exists");
DesignationInterface designation;
designation=new Designation();
if(blException.getExceptionCount()>0) throw blException;
POJOCopier.copy(designation,titleWiseMap.get(title.trim().toUpperCase()));
return designation;
}
public boolean codeExists(int code)
{
return codeWiseMap.containsKey(code);
}
public boolean titleExists(String title)
{
return titleWiseMap.containsKey(title.toUpperCase());
}
public int getCount()
{
return codeWiseOrderedList.size();
}
public boolean isAttachedToAnEmployee(int code)
{
boolean b=false;
try
{
EmployeeManagerInterface employeeManager;
employeeManager=EmployeeManager.getInstance();
b=employeeManager.isDesignationAllocated(code);
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
return b;
}
public int getEmployeeCountWithDesignation(int code)
{
int size=0;
try
{
EmployeeManagerInterface employeeManager;
employeeManager=EmployeeManager.getInstance();
List<EmployeeInterface> list;
list=employeeManager.getByDesignation(code);
size=list.size();
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

return size;
}
}