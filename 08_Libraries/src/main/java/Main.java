import javax.persistence.*;
import java.util.Properties;
import java.util.List;

import entities.*;

public class Main {
  private EntityManager entityManager;

  public Main(EntityManager em) {
    this.entityManager = em;
  }

  public long createProduct(String name, String category, double price) {
    entityManager.getTransaction().begin();
    Product newProduct = new Product(name, category, price);
    entityManager.persist(newProduct);
    entityManager.getTransaction().commit();
    return newProduct.getProdID();
  }

  public void updatePrice(long id, double newPrice) {
    entityManager.getTransaction().begin();
    Query query = entityManager.createQuery("select p from Product p");
    List<Product> products = query.getResultList();
    for (Product product: products) {
      if (product.getProdID() == id) {
        product.setPrice(newPrice);
        break;
      }
    }
    entityManager.getTransaction().commit();
  }

  public void deleteAllProducts() {
    entityManager.getTransaction().begin();
    entityManager.createQuery("delete from Product p").executeUpdate();
    entityManager.getTransaction().commit();
  }

  public void deleteAllTeams() {
    entityManager.getTransaction().begin();
    entityManager.createQuery("delete from Team t").executeUpdate();
    entityManager.getTransaction().commit();
  }

  public void deleteAllEmployees() {
    entityManager.getTransaction().begin();
    entityManager.createQuery("delete from Employee e").executeUpdate();
    entityManager.getTransaction().commit();
  }

  public void deleteAllProjects() {
    entityManager.getTransaction().begin();
    entityManager.createQuery("delete from Project p").executeUpdate();
    entityManager.getTransaction().commit();
  }

  public void createTeam(String teamName,
                         String projectName,
                         String[] teamMembers) {
    entityManager.getTransaction().begin();
    Team team = new Team(teamName);
    entityManager.persist(team);

    Project project = new Project(projectName);
    entityManager.persist(project);

    project.setTeam(team);
    team.setCurrentProject(project);

    for (String name: teamMembers) {
      Employee teamMember = new Employee(name);
      entityManager.persist(teamMember);

      team.getTeamMembers().add(teamMember);
      teamMember.setTeam(team);
    }

    entityManager.getTransaction().commit();
  }

  public void findEmployees(String keyword) {
    TypedQuery<Employee> query = entityManager.createQuery("select e from Employee e where e.name like concat('%', :keyword, '%')",
                                                           Employee.class);
    query.setParameter("keyword", keyword);
    List<Employee> results = query.getResultList();
    for (Employee employee: results) {
      System.out.println(employee.getName());
    }
  }

  public Employee findEmployeeByName(String name) {
    TypedQuery<Employee> query = entityManager.createNamedQuery("Employee.findByName",
                                                                Employee.class);
    query.setParameter("name", name);
    Employee employee = query.getSingleResult();
    return employee;
  }

  public static void main(String[] args) {
    Properties props = new Properties();

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa_demo",
                                                                      props);
    EntityManager entityManager = emf.createEntityManager();
    Main app = new Main(entityManager);

    // test Products entity
    app.deleteAllProducts();
    app.createProduct("Hot Dog", "Sandwich", 3.99);
    app.createProduct("Hamburger", "Sandwich", 4.49);
    app.createProduct("Coffee", "Beverage", 1.49);
    long colaId = app.createProduct("Cola", "Beverage", 1.49);
    app.updatePrice(colaId, 1.99);

    // test relationships
    app.deleteAllEmployees();
    app.deleteAllTeams();
    app.deleteAllProjects();
    String[] teamMembers = new String[] {
      "Ralph Wiggum",
      "Lisa Simpson",
      "Mr. Smithers",
      "Disco Stu",
      "Marge Simpson"
    };
    app.createTeam("The Elite Team", "Revamp the Contabulator", teamMembers);
    app.findEmployees("imps");
    Employee stu = app.findEmployeeByName("Disco Stu");
    System.out.println("Disco Stu was found? " + stu.getName());

    entityManager.close();
  }







}
