package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import application.model.Classes;
import application.model.Grade;
import application.model.Name;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class FXController {
	MainClass mc;
	SQLiteJDBC sql;
	
	@FXML private TableView<Name> nameTable;
	@FXML private TableView<Classes> classTable;
    @FXML private TextField nameField;
    @FXML private TextField classField;
    @FXML private TextField emailField;
    @FXML private TextField numberField;
    @FXML private Label averageField;
    
    @FXML private TextField classnameField;
    @FXML private TextField gradField;
    @FXML private TextField masterField;
    @FXML private Label studentsField;
    @FXML private Label classAvgField;
    
    @FXML private TableView<Grade> gradeTable;
    
    @FXML private TabPane tabPane; 
    
    @FXML private SplitPane splitPane;
    
    @FXML private Stage primaryStage;
    @FXML private ChoiceBox choiceBox;
    @FXML private TextField subjectField;
	
    
    public void initialize(){
    	if(nameTable != null){
    		sql = new SQLiteJDBC();
    		fillTables();
    		nameTable.getSelectionModel().selectedItemProperty().addListener(new ClickNamesListener());
    		classTable.getSelectionModel().selectedItemProperty().addListener(new ClickClassListener());

    		
    	}
    	
    }
    
    @FXML
	private void onCloseFunction(ActionEvent e){
		Platform.exit();
	}
	@FXML
	private void onResetDatabase(ActionEvent e){
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Reset database!");
		alert.setHeaderText("This deletes all your data.");
		alert.setContentText("Are you sure?");
		 
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK){
			mc = new MainClass();
			File f = new File("gradebooík.db");
			f.delete();
			
			mc.setupDatabase();
			fillTables();
		}
		
		
		
	}
	@FXML
	private void onAbout(ActionEvent e){
			
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setHeaderText("GradeBook 1.0");
		alert.setContentText("This is an application for grading students and keeping track of their data");

		alert.showAndWait();
		
		
	}
	
	
		@FXML
		private void onClassSaveClick(ActionEvent event){
		
			if(classnameField.getText().equals("") || masterField.getText().equals("")){
				return;
			}
			
			String grad;
			if(gradField.getText().equals("")){
				grad = "null";
			}else{
				grad = gradField.getText();
			}
			
			if(classTable.getSelectionModel().getSelectedItem() == null){
				String[] data = {(Integer.parseInt(getLastId("Class"))+1)+"" , "\""+classnameField.getText()+"\"","\""+masterField.getText()+"\"","0",grad};
				sql.insertData("Class", MainClass.classes, data);
				
				ObservableList<Classes> dataList = classTable.getItems();
		        dataList.add(new Classes((Integer.parseInt(getLastId("Class"))+1)+"",classnameField.getText(),masterField.getText()));
			}else{
				String[] data = {classTable.getSelectionModel().getSelectedItem().getId(), "\""+classnameField.getText()+"\"","\""+masterField.getText()+"\"",grad};
				sql.updateData("Class", MainClass.classes, data);
				
				ObservableList<Classes> dataList = classTable.getItems();
				dataList.add(new Classes(classTable.getSelectionModel().getSelectedItem().getId(),classnameField.getText(),masterField.getText()));
				dataList.remove(classTable.getSelectionModel().getSelectedIndex());
		       
				classTable.getSelectionModel().selectLast();	
			}
			
		}
		    
	
	
	
	    @FXML
	    private void onNameSaveClick(ActionEvent event){
	    	    	
	    	
	    	Name selection = nameTable.getSelectionModel().getSelectedItem();
	    	String id = null;
	    	if(selection != null){
	    		id = selection.getId();
	    	}
	    	if(id == null){
		        	        
		        ArrayList<String> classid = sql.getData("Class", "ID", "CLASS_NAME = \"" + classField.getText() + "\"");
		        
		      if(classid.size() == 0){
		    	  Alert alert = new Alert(AlertType.ERROR);
		    	  alert.setTitle("Class not found");
		    	  alert.setHeaderText("No such class: " + classField.getText());
		    	  alert.setContentText("Please choose another class. If you have trouble getting existing classes, please refer to the table in the upper-right corner.");

		    	  alert.showAndWait();
		    	  return;
		        	
		        }
		        ObservableList<Name> data = nameTable.getItems();
		        data.add(new Name("" + (Integer.parseInt(getLastId("Names"))+1),nameField.getText(), classField.getText()));
		        
		      
		        Object[] values = {(Object)( Integer.parseInt(getLastId("Names"))+1),
		        		classid.get(0),
		        		"\"" + nameField.getText() + "\"",
		        		"\"" + emailField.getText()+ "\"",
		        		"\"" + numberField.getText() + "\""};
		        sql.insertData("Names", MainClass.names, values);
		        String[] classData = {classid.get(0),"\"" + sql.getData("Class", "CLASS_NAME", "ID = " + classid.get(0)).get(0)+"\"","\"" + sql.getData("Class", "CLASS_MASTER", "ID = " + classid.get(0)).get(0)+"\"",sql.getData("Class", "EXPECTED_GRAD_YEAR", "ID = " + classid.get(0)).get(0) };
		        sql.updateData("Class", MainClass.classes,classData );
		        
		        
	      	}else{
	      		 ArrayList<String> className = sql.getData("Class", "ID", "CLASS_NAME = \"" + classField.getText() + "\"");
			        
			      if(className.size() == 0){
			    	  Alert alert = new Alert(AlertType.ERROR);
			    	  alert.setTitle("Class not found");
			    	  alert.setHeaderText("No such class: " + classField.getText());
			    	  alert.setContentText("Please choose another class. If you have trouble getting existing classes, please refer to the table in the upper-right corner.");

			    	  alert.showAndWait();
			    	  return;
			        	
			        }
			       
			      
			        Object[] values = {id,
			        		className.get(0),
			        		"\"" + nameField.getText() + "\"",
			        		"\"" + emailField.getText()+ "\"",
			        		"\"" + numberField.getText() + "\""};
			        sql.updateData("Names", MainClass.names, values);
			        ObservableList<Name> data = nameTable.getItems();
			       
			        
			        data.add(new Name(id,nameField.getText(), classField.getText()));
			        data.remove(nameTable.getSelectionModel().getSelectedIndex());
	      	}
	    	
	    	
	        
	        
	        
	    }
	    
		private String getLastId(String table) {
			if(sql.getData(table, "MAX(ID)", "").get(0) == null){
				return -1+"";
			}
			return sql.getData(table, "MAX(ID)", "").get(0);
		}
		
		
		public void fillUpTable(ArrayList<String[]> names, ArrayList<String[]> classes) {
			
			ObservableList<Name> namesData = nameTable.getItems();
	        for (int i = 0; i < names.size(); i++) {
				namesData.add(new Name(names.get(i)[0], names.get(i)[1], names.get(i)[2]));
			}
	        
	        ObservableList<Classes> classData = classTable.getItems();
	        for (int i = 0; i < classes.size(); i++) {
				classData.add(new Classes(classes.get(i)[0], classes.get(i)[1], classes.get(i)[2]));
			}
	        
			
			
		}
		 public void fillTables(){
			 
			 ArrayList<String[]> alsaNames = new ArrayList<>();
			 ArrayList<String[]> alsaClass = new ArrayList<>();
			 
			 
			 for (int i = 0; i < Integer.parseInt(sql.getData("Names", "COUNT(*)", "").get(0)); i++) {
				String id = sql.getData("Names", "ID", "").get(i);
				String name = sql.getData("Names", "NAME", "").get(i);
				String classId = sql.getData("Names", "CLASS_ID", "").get(i);
				String className = sql.getData("Class", "CLASS_NAME", "ID = " + classId).get(0);
				String[] props = new String[3];
				props[0] = id;
				props[1] = name;
				props[2] = className;
				alsaNames.add(props);
			}
			 for (int i = 0; i < Integer.parseInt(sql.getData("Class", "COUNT(*)", "").get(0)); i++) {
					String id = sql.getData("Class", "ID", "").get(i);
					String name = sql.getData("Class", "CLASS_NAME", "").get(i);
					String master = sql.getData("Class", "CLASS_MASTER", "").get(i);
					String[] props = new String[3];
					props[0] = id;
					props[1] = name;
					props[2] = master;
					alsaClass.add(props);
				}
				fillUpTable(alsaNames, alsaClass);
			
			 
		 }
		 
		 
	
		 class ClickNamesListener implements ChangeListener{

			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				
				Name selection = (Name) newValue;
			    String id = null;
			    
			    if(selection == null){
			    	nameField.setText("");
			    	classField.setText("");
			    	emailField.setText("");
			    	numberField.setText("");
			    	return;
			    } 
				
				ArrayList<String[]> alsaGrades = new ArrayList<>();
				 for (int i = 0; i < Integer.parseInt(sql.getData("Subject", "COUNT(*)", "").get(0)); i++) {
						id = sql.getData("Subject", "ID", "").get(i);
						String subject = sql.getData("Subject", "SUBJECT_NAME", "").get(i);
						String grades = "";
						double avg = 0;
						int j;
						for(j = 0; j < Integer.parseInt(sql.getData("Grade", "COUNT(*)", "SUBJECT_ID = "+id + " AND NAME_ID = " + ((Name)newValue).getId()).get(0));j++){
							grades = grades + ", " + sql.getData("Grade", "GRADE", "SUBJECT_ID = " + id + " AND NAME_ID = " + ((Name)newValue).getId()).get(j);
							avg = avg + Integer.parseInt(sql.getData("Grade", "GRADE", "SUBJECT_ID = " + id + " AND NAME_ID = " + ((Name)newValue).getId()).get(j));
						}
						if(!grades.equals(""))grades = grades.substring(2);
						
						if(j != 0)avg = avg/((double)j);
						
						String[] props = new String[4];
						props[0] = id;
						props[1] = subject;
						props[2] = grades;
						props[3] = Double.toString(avg).substring(0, 3);
						alsaGrades.add(props);
					}
				 
				 ObservableList<Grade> gradesData = gradeTable.getItems();
				 Grade[] toRemove = new Grade[gradesData.size()];
				 for(int i = 0; i< toRemove.length; i++){
					 toRemove[i] = gradesData.get(i);
				 }
				 gradesData.removeAll(toRemove);
			        for (int i = 0; i < alsaGrades.size(); i++) {
						gradesData.add(new Grade(alsaGrades.get(i)[0], alsaGrades.get(i)[1], alsaGrades.get(i)[2], alsaGrades.get(i)[3]));
					}
				
			    tabPane.getSelectionModel().select(0);
			    
			    
			    
			    
			    id = selection.getId();
				String name = sql.getData("Names", "NAME", "ID = " + id).get(0);
				nameField.setText(name);
				
				ArrayList<String> className = sql.getData("Class", "CLASS_NAME", "ID = "+ sql.getData("Names", "CLASS_ID", "ID = " + id).get(0));
				
				if(className.size() == 0){
			    	  Alert alert = new Alert(AlertType.ERROR);
			    	  alert.setTitle("Class not found");
			    	  alert.setHeaderText("No such class: " + className.get(0));
			    	  alert.setContentText("Please choose another class. If you have trouble getting existing classes, please refer to the table in the upper-right corner.");

			    	  alert.showAndWait();
			    	  return;
			        	
			    } else {
			    	String classString = className.get(0);
			    	classField.setText(classString);
			    }

				if(sql.getData("Names", "EMAIL_ADD", "ID = " + id).get(0) != null){
				String email = sql.getData("Names", "EMAIL_ADD", "ID = " + id).get(0);
				emailField.setText(email);
				} else {
					emailField.setText("");
				}
				
				if(sql.getData("Names", "EMERGENCY_NUMBER", "ID = " + id).get(0) != null){
				String number = sql.getData("Names", "EMERGENCY_NUMBER", "ID = " + id).get(0);
				numberField.setText(number);
				} else {
					numberField.setText("");
				}
				
				String avg = sql.getData("Grade", "AVG(GRADE)", "NAME_ID = "+id).get(0); 
				averageField.setText(avg);
			    
			}
			
		 }
		 
		 class ClickClassListener implements ChangeListener{

				@Override
				public void changed(ObservableValue observable, Object oldValue, Object newValue) {
					
					Classes selected = (Classes) newValue;
					if(selected == null){
						classnameField.setText("");
				    	masterField.setText("");
				    	studentsField.setText("");
				    	gradField.setText("");
						return;
					}
					tabPane.getSelectionModel().select(1);
					
					
					String id = selected.getId();
					classnameField.setText(sql.getData("Class", "CLASS_NAME", "ID = " + id).get(0));
					masterField.setText(sql.getData("Class", "CLASS_MASTER", "ID = " + id).get(0));
					if(sql.getData("Names", "COUNT(*)", "CLASS_ID = " + id).get(0) != null){
						studentsField.setText(sql.getData("Names", "COUNT(*)", "CLASS_ID = " + id).get(0));
					} else {
						studentsField.setText("");
					}
					if(sql.getData("Class", "EXPECTED_GRAD_YEAR", "ID = " + id).get(0) != null){
						gradField.setText(sql.getData("Class", "EXPECTED_GRAD_YEAR", "ID = " + id).get(0));
					} else {
						gradField.setText("");
					}
					
					double avg = 0;
					int i = 0;
					int j = 0;
					for(i = 0; i < Integer.parseInt(sql.getData("Names", "COUNT(*)", "CLASS_ID = " + id).get(0));i++){
						String nameId =  sql.getData("Names", "ID", "CLASS_ID = " + id).get(i);
						
						ArrayList<String> als = sql.getData("Grade", "AVG(GRADE)", "NAME_ID = " + nameId);
						if(als.get(0) != null){
							avg = avg + Double.parseDouble(als.get(0));
							j++;
						}
						
						
					}
					if(j != 0) avg = avg/(double)j;
					
					classAvgField.setText(Double.toString(avg));
					
				}

		 }		
		 
		 
		 @FXML
		 public void onNameDeleteClick(){
			 
			 Name selected = (Name) nameTable.getSelectionModel().getSelectedItem();
			 if(selected == null){
				 return;
			 }
			 
			 String id = selected.getId();
			 sql.deleteRecord("Names", id);
			 
			 ObservableList<Name> data = nameTable.getItems();
		     data.remove(nameTable.getSelectionModel().getSelectedIndex());
		     
		     		 
		 }
		 
		 @FXML
		 public void onClassDeleteClick(){
			 
			 Classes selected = (Classes) classTable.getSelectionModel().getSelectedItem();
			 if(selected == null){
				 return;
			 }
			 String id = selected.getId();
			 sql.deleteRecord("Class", id);
			 
		     ObservableList<Classes> data = classTable.getItems();
		     data.remove(classTable.getSelectionModel().getSelectedIndex());

			 
		 }
		 
		 @FXML
		 public void onNewGrade(ActionEvent event){
			 
			 Name selected = (Name) nameTable.getSelectionModel().getSelectedItem();
			 if(selected == null){
				 return;
			 }
			 
			 Grade selectedGrade = (Grade) gradeTable.getSelectionModel().getSelectedItem();
			 if(selectedGrade == null){
				 return;
			 }
			 
			 String subjectName = selectedGrade.getSubjectName();
			 String subjectId = sql.getData("Subject", "ID", "SUBJECT_NAME = \"" + subjectName + "\"").get(0);
			 
			 TextInputDialog dialog = new TextInputDialog("");
			
			 dialog.setTitle("New grade");
			 dialog.setHeaderText(selected.getName()+"\'s new grade for " + subjectName);
			 dialog.setContentText("Please enter the grade:");
			 
			 Optional<String> result = dialog.showAndWait();
			 result.ifPresent(grade -> {
				 	String[] record = {(Integer.parseInt(getLastId("Grade"))+1)+"",selected.getId(),subjectId, grade};
				 	sql.insertData("Grade",MainClass.grades,record);});
			 
			 ArrayList<String[]> alsaGrades = new ArrayList<>();
			 
			 String id = selected.getId();
			 for (int i = 0; i < Integer.parseInt(sql.getData("Subject", "COUNT(*)", "").get(0)); i++) {
					id = sql.getData("Subject", "ID", "").get(i);
					String subject = sql.getData("Subject", "SUBJECT_NAME", "").get(i);
					String grades = "";
					double avg = 0;
					int j;
					for(j = 0; j < Integer.parseInt(sql.getData("Grade", "COUNT(*)", "SUBJECT_ID = "+id + " AND NAME_ID = " + selected.getId()).get(0));j++){
						grades = grades + ", " + sql.getData("Grade", "GRADE", "SUBJECT_ID = " + id + " AND NAME_ID = " + selected.getId()).get(j);
						avg = avg + Integer.parseInt(sql.getData("Grade", "GRADE", "SUBJECT_ID = " + id + " AND NAME_ID = " + selected.getId()).get(j));
					}
					if(!grades.equals(""))grades = grades.substring(2);
					
					if(j != 0)avg = avg/((double)j);
					
					String[] props = new String[4];
					props[0] = id;
					props[1] = subject;
					props[2] = grades;
					props[3] = Double.toString(avg).substring(0, 3);
					alsaGrades.add(props);
				}
			 
			 ObservableList<Grade> gradesData = gradeTable.getItems();
			 
			 
			 Grade[] grades = new Grade[gradesData.size()];
			 for(int i = 0; i<gradesData.size(); i++){
				 grades[i] = gradesData.get(i);
			 }
			 gradesData.removeAll(grades);
			 
		        for (int i = 0; i < alsaGrades.size(); i++) {
					gradesData.add(new Grade(alsaGrades.get(i)[0], alsaGrades.get(i)[1], alsaGrades.get(i)[2], alsaGrades.get(i)[3]));
				}
			 
		 }
		 @FXML
		 public void onDeleteSubject(ActionEvent event){
			 
			 Grade selected = (Grade) gradeTable.getSelectionModel().getSelectedItem();
			 if(selected == null){
				 return;
			 }
			 Alert alert = new Alert(AlertType.CONFIRMATION);
			 alert.setTitle("Delete Subject");
			 alert.setHeaderText("Deleting " + selected.getSubjectName());
			 alert.setContentText("Are you sure?");
			 
			 Optional<ButtonType> result = alert.showAndWait();
			 if(result.get() == ButtonType.OK){
				 sql.deleteRecord("Subject", sql.getData("Subject", "ID", "SUBJECT_NAME = \"" + selected.getSubjectName()+"\"").get(0));
				 ObservableList<Grade> gradesData = gradeTable.getItems();
				 
				gradesData.remove(gradeTable.getSelectionModel().getSelectedIndex());
			 }
			 
		 }
		
		 
		 @FXML
		 public void onNewSubject(ActionEvent event){
			 
			 TextInputDialog dialog = new TextInputDialog("");
				
			 dialog.setTitle("New Subject");
			 dialog.setHeaderText("A new subject");
			 dialog.setContentText("Please enter the name of the subject:");
			 
			 Optional<String> result = dialog.showAndWait();
			 result.ifPresent(name -> {
				 String[] data = {Integer.parseInt(getLastId("Subject"))+1+"","\"" +name+"\""};
				 sql.insertData("Subject", MainClass.subjects, data);
				 ObservableList<Grade> gradesData = gradeTable.getItems();
				 gradesData.add(new Grade(Integer.parseInt(getLastId("Grade"))+1+"",name,"","0.0"));});
			 
			 
			 
		 }
		 @FXML
		 public void onNewClass(ActionEvent e){
			 classTable.getSelectionModel().clearSelection();
		 }
		 
		 @FXML
		 public void onNewStudent(ActionEvent e){
			 nameField.setText("");
			 classField.setText("");
			 
			 nameTable.getSelectionModel().clearSelection();
			 
		 }
	

}

