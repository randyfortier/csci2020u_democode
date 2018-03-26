package entities;

import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;

@Entity
public class Team {
   @Id
   @SequenceGenerator(name="team_id", sequenceName="teams_id_seq", allocationSize=1)
   @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="team_id")
   private long id;
   private String name;

   @OneToOne
   private Project currentProject;

   @OneToMany(mappedBy = "team")
   private List<Employee> teamMembers = new ArrayList<Employee>();

   public Team() {}

   public Team(String name) {
     this.name = name;
   }

   public String getName() { return name; }
   public void setName(String name) { this.name = name; }

   public List<Employee> getTeamMembers() { return teamMembers; }
   public void setTeamMembers(List<Employee> members) {
     this.teamMembers = members;
   }

   public Project getCurrentProject() { return currentProject; }
   public void setCurrentProject(Project project) { this.currentProject = project; }
}
