package entities;

import javax.persistence.*;

@Entity
@Table(name="products")
public class Product {
   @Id
   @Column(name="id")
   @SequenceGenerator(name="product_id", sequenceName="products_id_seq", allocationSize=1)
   @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="product_id")
   private long prodID;
   private String name;
   private String category;
   private double price;

   public Product() {}

   public Product(String name,
                  String category,
                  double price) {
     this.name = name;
     this.category = category;
     this.price = price;
   }

   public long getProdID() { return prodID; }
   public String getName() { return name; }
   public String getCategory() { return category; }
   public double getPrice() { return price; }

   public void setProdID(long id) { this.prodID = id; }
   public void setName(String name) { this.name = name; }
   public void setCategory(String category) { this.category = category; }
   public void setPrice(double price) { this.price = price; }
}
