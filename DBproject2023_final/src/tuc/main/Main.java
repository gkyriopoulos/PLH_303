package tuc.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InputMismatchException;

import tuc.main.menu.Menu;

public class Main {
	
	/* WARNING 0:
	 * Some globals in order to be able to connect to the database,
	 * you will most likely need to change these.
	 */
	
    static String USERNAME = "postgres";
    static String PASSWORD = "1234";
    static String DATABASE_NAME = "DBproject2023_final";
    
    /*
     * WARNING 1:
	 * In order to run the program successfully you need to link the JDBC library
	 * to the project.
	 * I have included the JDBC .jar in the project files.
	 * 
	 * WARNING 2: In order to be able to display Greek characters at the console you have to follow this process:
	 * 1) Window > Preferences > General > Content Types > Text.
	 * 2) Set "UTF-8" as the Default Encoding.
	 * 3)
     */
    
	/**
	 * Main function it just prints the menu.
	 * @param args
	 */
    
    public static void main(String[] args) {
   	
        try {
            Class.forName("org.postgresql.Driver");
            Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + DATABASE_NAME , USERNAME, PASSWORD);
            if (c != null) {
        		System.out.println("********************************************************\n");
                System.out.println("Connection with Database initialized successfully! :)");
            }
            
            Menu.getInstance().mainMenu(c);
            
            c.close();
            
        } catch (Exception e) {
        	exceptionHandler(e);
        }
    }
   
    /**
     * A simple Exception Handler.
     * @param e
     */
    public static void exceptionHandler(Exception e) {
    	if( e instanceof SQLException ) {
    		System.out.println("\nError while retrieving data from the Database :( .");
    	}
    	else if( e instanceof InputMismatchException) {
    		System.out.println("\nThe input can only be an int (1,2,3..).");
    	}else {
    		System.out.println("\nUnknow exception :( .");
    	}
    	e.printStackTrace();
    }
}

