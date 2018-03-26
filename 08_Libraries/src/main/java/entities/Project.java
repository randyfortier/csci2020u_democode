package entities;

import javax.persistence.*;

@Entity
public class Project {
   @Id
   @SequenceGenerator(name="project_id", sequenceName="projects_id_seq", allocationSize=1)
   @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="project_id")
   private long id;
   private String name;

   @OneToOne(mappedBy = "currentProject")
   private Team team;

   public Project() {}

   public Project(String name) {
     this.name = name;
   }

   public String getName() { return name; }
   public void setName(String name) { this.name = name; }

   public Team getTeam() { return team; }
   public void setTeam(Team team) { this.team = team; }
}
