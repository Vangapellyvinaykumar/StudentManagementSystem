package spec;

public class SpecialNeedsStudent extends Student {
	private boolean needsWC,isVI;
	
	public SpecialNeedsStudent() {
		super();
		this.isVI = this.needsWC = false;
	}
	
	public SpecialNeedsStudent(String rollNumber, String name, boolean needsWC, boolean isVI) {
		super(rollNumber,name);
		this.isVI = isVI;
		this.needsWC = needsWC;
	}
	public void displayData() {
		super.displayData();
		if(!super.name.equals("--removed--")) {
		System.out.println("--Special Needs--");
		if(this.isVI) {
			System.out.println("Student is visually impacted ");
		}
		else {
			System.out.println("Student is not visually impacted ");

		}
		if(this.needsWC) {
			System.out.println("Student needs a wheelchair ");
		}
		else {
			System.out.println("Student doesn't  need a wheelchair ");

		}
		}
	}
}
