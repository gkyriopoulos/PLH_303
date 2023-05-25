package tuc.main.menu;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
	
	private static Menu menu;
	
	private static int  EXIT_MENU = 5;
    private static int AM_SIZE = 10;
    private static int COURSE_CODE_SIZE = 7;
    private static int MAX_GRADE = 10;
    private static int MIN_GRADE = 0;
    private static int MAX_REC_PER_PAGE = 5;
    
	private static String[] mainMenuOptions = {"1) Retrive Student's Grade(AM , CourseCode).", 
												"2) Alter Student's Grade(AM, CourseCode , SerialNumber).", 
												"3) Find Person(Surname).", 
												"4) Retrive Student's Grades(AM) - sorted.", "5) Exit."};
	
	private Menu(){
	}
	
	/**
	 * Function used to get the singleton instance.
	 * @return menu 
	 */
	public static Menu getInstance() {
		if(menu == null) {
			return new Menu();
		}
		return menu;
	}
	
	/**
	 * Function used in order to print all the menu choices.
	 * @return 1 if everything is OK.
	 */
	private int printMainMenu() {
		System.out.println("\n*************** Hello Mr.Kazasis uWu :3 ****************\n");
		for(String option : mainMenuOptions) {
			System.out.println(option);
		}
		System.out.println("\n********************************************************");
		return 1;
	}
	
	/**
	 * The main menu function. It just chooses stuff.
	 * @param c 
	 * @return 1 if everything is OK.
	 * @throws SQLException
	 * @throws InputMismatchException
	 */
	public int mainMenu(Connection c) throws SQLException, InputMismatchException {
		
		 Scanner sc = new Scanner(System.in);
		 int selected_option = 0;
		 
		 while(selected_option != EXIT_MENU) {
			
			printMainMenu();
			selected_option = sc.nextInt();
			
			switch(selected_option) {
				case 1:
					firstChoice(sc,c);
					break;
				case 2:
					secondChoice(sc,c);
					break;
				case 3:
					thirdChoice(sc,c);
					break;
				case 4:
					fourthChoice(sc,c);
					break;
				case 5:
					break;
				default:
					testChoice(c);
					System.out.println("Unexpected choice. Try again.");
					break;
			}
		 }
		
		System.out.println("Program exited successfully!");
		System.out.println("\n********************************************************");

		sc.close();
		return 1;
	}
	
	/**
	 * A simple test choice. Will be deleted at a later stage.	
	 * @param c
	 * @return 1 if everything is OK.
	 * @throws SQLException
	 */
	private int testChoice(Connection c) throws SQLException {

        String test = "{call get_lab_staff_job_hours()}";
        CallableStatement cs = c.prepareCall(test);
        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getString("amka") + " " + rs.getString("name") 
            		+ " " + rs.getString("surname")
                    + " " + rs.getInt("job_hours"));
        }
        
        cs.close();
        rs.close();
        
		return 1;
	}
	
	/**
	 * Function for the first question of the exercise.
	 * Some testing values are: (AM: '2010000001', CourseCode: 'ΑΓΓ 101')
	 * @param sc
	 * @param c
	 * @return 1,0 depending if there is an output or not anything else
	 * is NOT OK.
	 * @throws SQLException
	 */
	private int firstChoice(Scanner sc, Connection c) throws SQLException {
		
		System.out.println("\nEnter Student's AM: ");
		String am = sc.next();
		sc.nextLine();  // consume newline left-over
		
		if(!checkSize(am,AM_SIZE)) {
			System.out.println("\nAM's size is wrong. Try again.");
			return 0;
		}
		
		System.out.println("\nEnter Course's Code: ");
		String course_code = sc.nextLine();
		
		if(!checkSize(course_code,COURSE_CODE_SIZE)) {
			System.out.println("\nCourse Code's size is wrong. Try again.");
			return 0;
		}
		
		String query = "SELECT final_grade FROM public.\"Register\" r "
				+ "JOIN public.\"Student\" s ON r.amka = s.amka "
				+ "WHERE s.am = " + "\'" + am + "\'" 
				+ " AND r.course_code = " + "\'"  + course_code + "\'" + ";";
		
        PreparedStatement ps = c.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
		
        if(rs.next() == false) {
			System.out.println("\nCouldn't find a (Student,Course) pair given the parameters entered. Try again.");
	        ps.close();
	        rs.close();
			return 0;
		}else {
			do {
				 int grade = rs.getInt("final_grade");
		            System.out.println("\n"+ "'" + am + "'" + " "
		            		+ "'" + course_code + "'" + " "
		            		+ "'" + grade + "'");
			}while(rs.next());
		} 
        
        ps.close();
        rs.close();
        
		return 1;
	}
	
	/**
	 * Function for the second question of the exercise 
	 * Some testing values are: (AM: '2010000001', CourseCode: 'ΑΓΓ 101', SerialNum : '1')
	 * @param sc
	 * @param c
	 * @return
	 * @throws SQLException
	 */
	private int secondChoice(Scanner sc, Connection c) throws SQLException {
		
		System.out.println("\nEnter Student's AM: ");
		String am = sc.next();
		sc.nextLine();  // consume newline left-over
		
		if(!checkSize(am,AM_SIZE)) {
			System.out.println("\nAM's size is wrong. Try again.");
			return 0;
		}
		
		System.out.println("\nEnter Course's Code: ");
		String course_code = sc.nextLine();
		
		if(!checkSize(course_code,COURSE_CODE_SIZE)) {
			System.out.println("\nCourse Code's size is wrong. Try again.");
			return 0;
		}
		
		System.out.println("\nEnter Serial Number: ");
		String serial_num = sc.nextLine();
		
		if(!checkRange(Integer.parseInt(serial_num),0,Integer.MAX_VALUE)) {
			System.out.println("\nThe Serial Number entered is out of range. Try again.");
			return 0;
		}
		
		System.out.println("\nEnter Grade: ");
		String grade = sc.nextLine();
		
		if(!checkRange(Integer.parseInt(grade), MIN_GRADE, MAX_GRADE)) {
			System.out.println("\nThe Grade entered is out of range. Try again.");
			return 0;
		}
		
		
		String query = "UPDATE public.\"Register\"" + " "
						+ "SET final_grade = " + grade + " "
						+ "WHERE amka = (" + " "
						+ "SELECT amka" + " "
						+ "FROM public.\"Student\"" + " "
						+ "WHERE am = "  + "\'" + am + "\'" + ")" + " "
						+ "AND course_code = " + "\'" + course_code + "\'" + " "
					    + "AND serial_number = " + serial_num + ";";
		
		
		PreparedStatement ps = c.prepareStatement(query);
	    int rs = ps.executeUpdate();
	   
	    if(rs == 0) {
	    	System.out.println("\nUpdate failed. Try again.");
		   	ps.close();
		   	return 0;
	    }
		
	    
	    System.out.println("\nChanged Student's " + "\'" + am + "\'" + " Grade at " 
	    		+ "\'" + course_code + "\'" + " with Serial Number " + "\'" + serial_num + "\'"
	    		+ " to " + "\'" +  grade + "\'");
	    
	    ps.close();
   
		return 1;
	}
	
	//TODO: thirdChoice
	private int thirdChoice(Scanner sc, Connection c) throws SQLException {
		
		System.out.println("\nEnter Person's surname initials: ");
		String initials = sc.next();
		sc.nextLine();  // consume newline left-over
		
		int rs_size = 0;
		int max_rec_per_page = 0;
		int max_pages = 1;
		int i = 0;
		int n = 0;
		
		String query = "SELECT surname,name,amka,email FROM public.\"Person\"" + " "
				+ "WHERE surname" + " "
				+ "LIKE " + "\'" + initials + "%'" + " "
				+ "ORDER BY surname;";
		
		PreparedStatement ps = c.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = ps.executeQuery();
		
		if (rs != null) 
		{
		  rs.last();   // move to the end
		  rs_size = rs.getRow(); // get row
		  rs.beforeFirst();; // move to the beginning
		}
		
		if( rs_size > MAX_REC_PER_PAGE ) {
			System.out.println("\nEnter number of recs per page: ");
			max_rec_per_page = sc.nextInt();
			max_pages = (int) Math.ceil(rs_size/max_rec_per_page) + 1;
		}
		
		String[][] results = new String [max_pages][max_rec_per_page];
		
		if(rs.next() == false) {
	    	System.out.println("\nNo Person matching the initials entered in the Database.");
		    ps.close();
		    rs.close();
			return 0;
		}else {
			do {
				
				String surname = rs.getString("surname");
				String name = rs.getString("name");
				String amka = rs.getString("amka");
				String email = rs.getString("email");
				
				results[i/max_rec_per_page][i%max_rec_per_page] = surname + " " + name + " " + amka + " " + email;
				
				i++;
				
			}while(rs.next());
		} 
		
		System.out.println("\nMax number of pages(0-indexed): " + max_pages);
		
		do {
			printPage(results,n);
			System.out.println("\nEnter page(or n for next page): ");
			String option = sc.next();
			if(option.equals("n")) {
				n = n + 1;
			}else {
				n = Integer.parseInt(option);
			}
		}while(n < max_pages);
		
		
		return 1;
	}
	
	/**
	 * Function for the fourth question of the exercise.
	 *Some testing values are: (AM: '2010000004')
	 * @param sc
	 * @param c
	 * @return 1 or 0 everything else is NOT OK.
	 * @throws SQLException
	 */
	private int fourthChoice(Scanner sc, Connection c) throws SQLException {
		
		System.out.println("\nEnter Student's AM: ");
		String am = sc.next();
		sc.nextLine();  // consume newline left-over
		
		if(!checkSize(am,AM_SIZE)) {
			System.out.println("\nAM's size is wrong. Try again.");
			return 0;
		}
		
		String query = "SELECT serial_number, course_code, final_grade" + " "
				+ "FROM public.\"Register\" WHERE amka = (" + " "
				+ "SELECT amka" + " "
				+ "FROM public.\"Student\"" + " "
				+ "WHERE am = " + "\'" + am + "\'" + " "
				+ ")" + " "
				+ "ORDER BY serial_number;";
		
		System.out.println(query);
		
		PreparedStatement ps = c.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
			
	    if(rs.next() == false) {
	    	System.out.println("\nCouldn't find Student in the Database.");
		    ps.close();
		    rs.close();
			return 0;
		}else {
			do {
				int serial_number = rs.getInt("serial_number");
				String course_code = rs.getString("course_code");
				String final_grade = rs.getString("final_grade");
				
				System.out.println("\nSemester's Serial Number: " + "\'" + serial_number + "\'" + 
				"\nCourse Code: " + "\'" + course_code + "\'" 
				+ "\nFinal Grade: " + "\'"+ final_grade + "\'");
			}while(rs.next());
		} 
	        
	    ps.close();
	    rs.close();
		
	    return 1;
	}
	
	private boolean checkSize(String test, int size) {
		return (test.length() == size) ? true : false; 
	}
	
	private boolean checkRange(int i, int min, int max) {
		return ( (i >= min) && ( i<= max ) ) ? true : false;
	}
	
	private int printPage(String[][] results, int i) {
		
		System.out.println("\n");
		
		if(i >= 0) {
			for (String rec : results[i] ) {
				System.out.println(rec);
			}
		}else {
			System.out.println("Negative page index. Try again.");
			return 0;
		}
		
		return 1;
	}
}

//TODO FIX 4 according to email plus the data format while printing
