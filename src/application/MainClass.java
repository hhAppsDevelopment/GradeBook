package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainClass extends Application {
	 
	private Stage primaryStage;
	private BorderPane rootLayout;
	
	SQLiteJDBC sqlite;
	FXController fxc;
	
	File f;
	
	public static String[] names = {"ID", "CLASS_ID","NAME","EMAIL_ADD","EMERGENCY_NUMBER"};
	public static String[] grades = {"ID", "NAME_ID", "SUBJECT_ID","GRADE"};
	public static String[] subjects = {"ID","SUBJECT_NAME"};
	public static String[] classes = {"ID", "CLASS_NAME","CLASS_MASTER","EXPECTED_GRAD_YEAR"};
	public static void main(String[] args) {
		launch(args);
	}
	

	public void start(Stage primaryStage) {
		f = new File("gradebook.db");
		if(!f.exists())setupDatabase();
		
		this.primaryStage = primaryStage;
        this.primaryStage.setTitle("GradeBook");
        initRootLayout();
        contentSwitcher(MainClass.class.getResource("view/MainContent.fxml"));
        
	}

	public void initRootLayout() {
		try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("view/BorderLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setResizable(false);
           
            primaryStage.setScene(scene);
        	
            primaryStage.show();
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
	
	public Scene getCurrentScene(){
		return primaryStage.getScene();
		
	}
	
	
	 public void contentSwitcher(URL r) {
	        try {
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(r);
	            AnchorPane mainContent = (AnchorPane) loader.load();

	            
	            rootLayout.setCenter(mainContent);
	            
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	
	 
	 public void setupDatabase() {
			sqlite = new SQLiteJDBC();	
			
			String[] names = {"ID INT PRIMARY KEY NOT NULL", "CLASS_ID INT NOT NULL","NAME TEXT NOT NULL","EMAIL_ADD TEXT","EMERGENCY_NUMBER TEXT"};
			
			sqlite.createTable("Names", names);
			
			
			String[] classes = {"ID INT PRIMARY KEY NOT NULL", "CLASS_NAME TEXT NOT NULL","CLASS_MASTER TEXT  ","EXPECTED_GRAD_YEAR INT  "};
			
			sqlite.createTable("Class", classes);
			
			
			String[] subjects = {"ID INT PRIMARY KEY NOT NULL", "SUBJECT_NAME TEXT NOT NULL"};
			
			sqlite.createTable("Subject", subjects);
			
			
			String[] grades = {"ID INT PRIMARY KEY NOT NULL", "NAME_ID INT NOT NULL", "SUBJECT_ID INT NOT NULL", "GRADE INT NOT NULL"};
					
			sqlite.createTable("Grade", grades);

			
			
		}
	
	 
	
}
