package application.model;


import javafx.beans.property.SimpleStringProperty;

public class Grade {
  private final SimpleStringProperty id = new SimpleStringProperty("");
  private final SimpleStringProperty subjectName = new SimpleStringProperty("");
  private final SimpleStringProperty gradesList = new SimpleStringProperty("");
  private final SimpleStringProperty average = new SimpleStringProperty("");

   public Grade() {
       this("", "","","");
   }

   public Grade(String id, String subject,String grades,String average) {
       setId(id);
       setSubjectName(subject);
       setGradesList(grades);
       setAverage(average);
   }
   
   public void setAverage(String avg) {
       average.set(avg);
   }
       
   public String getAverage() {
       return average.get();
   }  
   
   
   public void setId(String Id) {
       id.set(Id);
   }
       
   public String getId() {
       return id.get();
   }  
   

   public String getSubjectName() {
       return subjectName.get();
   }

   public void setSubjectName(String sName) {
       subjectName.set(sName);
   }
       
   public String getGradesList() {
       return gradesList.get();
   }
   
   public void setGradesList(String grades) {
       gradesList.set(grades);
   }
   
}