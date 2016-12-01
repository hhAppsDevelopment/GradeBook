package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLiteJDBC{
  
	Connection c = null;
    Statement stmt = null;

	public void setupDatabaseConnection() {		
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:gradebook.db");
	      
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	  
	
	}
	
	public void createTable(String name, String[] columns){
		try {
			setupDatabaseConnection();
			stmt = c.createStatement();
			
			String columnsstring = "";
		      
		    for(int i = 0; i<columns.length; i++){
		    	columnsstring = columnsstring + columns[i] + ", ";
		    }
		    columnsstring = columnsstring.substring(0, columnsstring.length()-2);
		      
		      
		    String sql = "CREATE TABLE " + name + " (" + columnsstring + ") ";
		    stmt.executeUpdate(sql);
		    
		    
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				stmt.close();
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		    
		}
	      
	     
	}
	public void insertData(String table, String[] fields, Object[] values){
		
		
		try{
		setupDatabaseConnection();
		
		
		stmt = c.createStatement();
		c.setAutoCommit(false);
	    String field = "";
	    String value = "";
	    for(int i = 0; i<fields.length; i++){
	    	field = field + fields[i] +",";
	    	value = value + values[i].toString() +",";
	    }
	    field = field.substring(0,field.length() - 1);
	    value = value.substring(0, value.length() - 1);
	      
	      
	    String sql = "INSERT INTO " + table + " ("+field+") " +
	                   "VALUES ("+ value +");"; 
	    stmt.executeUpdate(sql);
	    c.commit();
	    
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				stmt.close();
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		    
		}
		
	}
	
	
	public ArrayList<String> getData(String table, String field, String clause){
		ArrayList<String> als = new ArrayList<>();
		try{
			ResultSet rs = null;
			setupDatabaseConnection();
			
			c.setAutoCommit(false);
			
			String where = "";

			if(!clause.equals("")){
				where = " WHERE " + clause;
			}
			
			stmt = c.createStatement();
		    rs =  stmt.executeQuery( "SELECT "+field+" FROM "+table + where + ";" );
		    
		    while(rs.next()){
		    		als.add(rs.getString(rs.findColumn(field)));
		    }
		    
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				stmt.close();
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		    
		}
		
		return als;
	}

	public void updateData(String table, String[] fields, Object[] values) {
		  try {
			  
						
			  
			for (int i = 0; i < fields.length; i++) {
				setupDatabaseConnection();
				stmt = c.createStatement();
				c.setAutoCommit(false);
				String sql = "UPDATE " + table + " set " + fields[i] +" = " + values[i] +" where ID="+values[0]+";";
				stmt.executeUpdate(sql);
				c.commit();
			}
			 
			  
			  
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				stmt.close();
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		    
		}
		
	}
	
	public void deleteRecord(String table, String id){
		
		try {
			setupDatabaseConnection();
			stmt = c.createStatement();
			c.setAutoCommit(false);
			String sql = "DELETE FROM " + table + " WHERE ID = " + id + ";";		
			stmt.executeUpdate(sql);
			c.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				stmt.close();
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		    
		}
		
	}
	
}





