public class Student {
    private int sid;
    private String firstName;
    private String lastName;
    private double gpa;
    
    public Student(int sid, String firstName, String lastName, double gpa) {
        this.sid = sid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gpa = gpa;
    }
    
    public int getSid() { return this.sid; }
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public double getGpa() { return this.gpa; }
    
    public void setSid(int sid) { this.sid = sid; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setGpa(double gpa) { this.gpa = gpa; }
}