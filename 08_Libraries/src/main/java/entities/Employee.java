package entities;

import javax.persistence.*;

@Entity
public class Employee {
   @Id
   @SequenceGenerator(name="employee_id", sequenceName="employees_id_seq", allocationSize=1)
   @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="employee_id")
   private long id;
   private String name;

   @ManyToOne
   private Team team;

   public Employee() {}

   public Employee(String name) {
     this.name = name;
   }

   public String getName() { return name; }
   public void setName(String name) { this.name = name; }

   public Team getTeam() { return team; }
   public void setTeam(Team team) { this.team = team; }
}
