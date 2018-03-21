package entities;

import javax.persistence.*;

@Entity
@Table(name="products")
public class Product {
  @Id
  @Column(name="id")
  @SequenceGenerator(name="product_id",
                     sequenceName="products_seq_id",
                     allocationSize=1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE,
                  generator="product_id")
  private long prodId;
  private String name;
  private String category;
  private double price;

  public long getProdId() { return prodId; }
  public String getName() { return name; }
  public String getCategory() { return category; }
  public double getPrice() { return price; }

  public void setProdId(long newVal) { this.prodId = newVal; }
  public void setName(String newVal) { this.name = newVal; }
  public void setCategory(String newVal) { this.category = newVal; }
  public void setPrice(double newVal) { this.price = newVal; }
}
