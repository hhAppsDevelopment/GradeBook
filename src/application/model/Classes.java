package application.model;


import javafx.beans.property.SimpleStringProperty;

public class Classes {
  private final SimpleStringProperty name = new SimpleStringProperty("");
  private final SimpleStringProperty master = new SimpleStringProperty("");
  private final SimpleStringProperty id = new SimpleStringProperty("");

   public Classes() {
       this("", "","");
   }

   public Classes(String id, String name,String master) {
       setName(name);
       setMaster(master);
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
       
   public String getMaster() {
       return master.get();
   }
   
   public void setMaster(String Master) {
       master.set(Master);
   }
   
}