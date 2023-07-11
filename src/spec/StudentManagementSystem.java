package spec;

import java.util.Scanner;
import java.sql.*;

public class StudentManagementSystem {

	public static void main(String[] args) {
		try {
			//Establishing Connection with DB
			String url = "jdbc:mysql://localhost:3306/spec",
					user = "root", password = "root";
			
			Connection conn = DriverManager.getConnection(url, user, password);
			//Checking if connection was successful
			if(!conn.isClosed()) {
				System.out.println("DB Connection Success");
			}
			
			//Creating a Statement to execute Queries
			Statement stmt = conn.createStatement();
			
			//Executing query to get the total number of students
			String sql = "SELECT COUNT(*) AS count FROM student_data";
			ResultSet studentCount = stmt.executeQuery(sql);
			studentCount.next(); //Only one .next() since we know there is only one row
			int numberOfStudents = studentCount.getInt("count");
			
			//Declaring Variables
			Scanner sc = new  Scanner(System.in);
			String rno,nm,sql2;
			int choice,res;char cont = 'N',flag = 'N';
			Student[] s = new Student[numberOfStudents];
			Student[] sCopy;
			char isVI = 'N', needsWC = 'N';
			
			
			//Creating objects of existing students
			sql = "SELECT S.name, S.roll_number, SP.needs_wc, SP.is_vi "
				+ "FROM student_data AS S "
				+ "LEFT JOIN special_needs AS SP ON SP.student_id = S.id";
			ResultSet studentData = stmt.executeQuery(sql);
			int j = 0;
			while(studentData.next()) {
				rno = studentData.getString("roll_number");
				nm = studentData.getString("name");
				if(studentData.getString("needs_wc") == null || studentData.getString("is_vi") == null) {
					s[j++] = new Student(rno,nm);
				}
				else {
					needsWC  = studentData.getString("needs_wc").charAt(0);
					isVI = studentData.getString("is_vi").charAt(0);
					s[j++] = new SpecialNeedsStudent(rno , nm , (needsWC == 'Y' || needsWC == 'y') ? true : (false), (isVI == 'Y' || isVI == 'y') ? true : (false));
				}
			}
			
			// APP FLOW BEGIN - Menu Start
			System.out.println("<<< Welcome to Student Management System >>>");
			System.out.println("\nEnter an option from menu below -");
			do {
				//Initializing flag to 'N' so that student doesn't exist warning shows
				flag = 'N';
				
				//Printing menu functions and getting choice
				System.out.println("----MENU----");
				System.out.println("\t1.List All Students");
				System.out.println("\t2.Find Student Data");
				System.out.println("\t3.Edit Student Data");
				System.out.println("\t4.Remove Student Data");
				System.out.println("\t5.Add a new Student");
				System.out.print("Enter your choice: ");
				choice = sc.nextInt();
				
				//Adding functionalities based on user choice
				switch(choice) {
					case 1:
						System.out.println("Listing " + numberOfStudents + " Students: " );
						for(int i = 0; i < numberOfStudents; i++) {
							s[i].displayData();
							System.out.println();
						}
						break;
					case 2:
						System.out.print("Enter roll number: ");
						rno = sc.next();
						for(int i = 0; i < numberOfStudents; i++) {
							if(s[i].getRollNumber().equals(rno)) {
								System.out.println("The name of student with roll number " + rno + " is " + s[i].getName());
								flag = 'Y';
							}
						
						}
						if(flag == 'N' ) {
							System.out.println("The name of student with roll number " + rno + " doesn't exist");
						}
						break;
					case 3:
						System.out.print("Enter roll number: ");
						rno = sc.next();
						for(int i = 0; i < numberOfStudents; i++) {
							if(s[i].getRollNumber().equals(rno)) {
								System.out.print("Enter new name for student: ");
								nm = sc.next();
								s[i].setName(nm);
								sql = "UPDATE student_data SET name = '" + nm + "' WHERE roll_number LIKE '" + rno + "'";
								res = stmt.executeUpdate(sql);
								System.out.println("Name Updated.");
								flag = 'Y';
							}
						}
						if(flag == 'N' ) {
							System.out.println("The name of student with roll number " + rno + " doesn't exist");
						}
						break;
					case 4:  
						System.out.print("Enter roll number: ");
						rno = sc.next();
						for(int i = 0; i < numberOfStudents; i++) {
							if(s[i].getRollNumber().equals(rno)) {
								s[i].removeStudent();
								sql  = "DELETE FROM special_needs WHERE student_id IN (SELECT id FROM student_data WHERE roll_number = '" + rno + "' )";
								sql2 = "UPDATE student_data SET name = '--removed--' WHERE roll_number = '" + rno + "'";
								int res2 = stmt.executeUpdate(sql);
								res2 = stmt.executeUpdate(sql2);
								System.out.println("Removed.");
								flag = 'Y';
							}
						
						}
						if(flag == 'N' ) {
							System.out.println("The name of student with roll number " + rno + " doesn't exist");
						}
						break;
					case 5:
						sCopy = new Student[numberOfStudents+1];
						for(int i = 0; i < numberOfStudents; i++) {
							sCopy[i] = s[i];
						}
						System.out.print("Enter Roll Number: ");
						rno = sc.next();
						System.out.print("Enter Name: ");
						nm = sc.next();
						sql = "INSERT INTO student_data (roll_number,name) VALUES ('" + rno + "','" + nm + "')";
						res = stmt.executeUpdate(sql);
						System.out.print("Does the student have any special needs? (y/n): ");
						cont = sc.next().charAt(0);
						if(cont == 'Y' || cont == 'y') {
							System.out.print("Is the student visually impaired? (y/n): ");
							isVI = sc.next().charAt(0);
							System.out.print("Does the student require a wheelchair? (y/n): ");
							needsWC = sc.next().charAt(0);
							sql = "SELECT id FROM student_data WHERE roll_number = '" + rno + "'";
							ResultSet stuId = stmt.executeQuery(sql);
							stuId.next();
							int id = stuId.getInt("id");
							sql = "INSERT INTO special_needs (student_id,needs_wc,is_vi) VALUES (" + id + ",'" + needsWC + "','" + isVI + "')";
							id =stmt.executeUpdate(sql);
							sCopy[numberOfStudents] = new SpecialNeedsStudent(rno , nm , (needsWC == 'Y' || needsWC == 'y') ? true : (false), (isVI == 'Y' || isVI == 'y') ? true : (false));
						}
						else {
							sCopy[numberOfStudents] = new Student(rno,nm);
						}
						s = sCopy;
						numberOfStudents++;
						System.out.println("New Student created. Total Students: " + numberOfStudents);
						break;
					default:
						System.out.println("ERROR! Enter value betweeen 1 and 2");
				}
				//Asking if the menu has to be looped again
				System.out.print("Do you want to continue? (y/n): ");
				cont = sc.next().charAt(0);
			}while(cont != 'N' && cont != 'n');
			System.out.println("Thank You.");
		}
		catch(Exception e) {
			System.out.println("ERROR!! \nMessage:\n" + e.getMessage()) ;
		}
		
	}
}


















