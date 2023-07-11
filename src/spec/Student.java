package spec;

public class Student {
	
	//Declaring variables as protected since its a super class
	protected String name, rollNumber;
	
	
	//Default Constructor with no arguments
	public Student() {
		this.name = this.rollNumber ="undefined";
	}
	
	
	//Constructor Overloading with 2 arguments
	public Student(String rollNumber, String name) {
		this.rollNumber = rollNumber;
		this.name = name;
	}
	
	//Method to display Student Data
	public void displayData() {
		System.out.println("Roll no: " + this.rollNumber + "\tName: " + this.name);
	}
	
	//GETTER Functions for getting roll number and name
	public String getRollNumber() {
		return this.rollNumber;
	}
	
	public String getName() {
		return this.name;
	}
	
	//SETTER functions for setting name 
	public void setName(String name) {
		this.name = name;
	}
	
	//Removing a Student
	public void removeStudent() {
		this.name = "--removed--";
		
	}
}