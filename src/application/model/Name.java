package application.model;


import javafx.beans.property.SimpleStringProperty;

public class Name {
  private final SimpleStringProperty name = new SimpleStringProperty("");
  private final SimpleStringProperty className = new SimpleStringProperty("");
  private final SimpleStringProperty id = new SimpleStringProperty("");

   public Name() {
       this("", "","");
   }

   public Name(String id, String name,String className) {
       setName(name);
       setClassName(className);
       setId(id);
   }
   
   public void setId(String Id) {
       id.set(Id);
   }
       
   public String getId() {
       return id.get();
   }  
   

   public String getName() {
       return name.get();
   }

   public void setName(String sName) {
       name.set(sName);
   }
       
   public String getClassName() {
       return className.get();
   }
   
   public void setClassName(String classname) {
       className.set(classname);
   }
   
}