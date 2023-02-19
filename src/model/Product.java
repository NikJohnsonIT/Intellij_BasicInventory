package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
  Models a product that may contain associated parts. Associated parts are not rrequired and must be removed before a product can be deleted.
  @author Nicholas Johnson
 */
public class Product {

    /**
      list of parts associated with the product. This list will appear empty if there are no associated parts with the product.
     */
    private final ObservableList<Part> associatedParts;
    /**
      Product ID. This is a unique value that is automatically generated.
     */
    private int id;
    /**
      Name of Product. This can be used as well as the product ID to search for various products.
     */
    private String productName;
    /**
      Price of product. Currently represented in USD.
     */
    private double price;
    /**
      Inventory level of product. Checks exist to ensure valid entries are used during program execution.
     */
    private int stock;
    /**
      Minimum number of products for inventory. Checks exist to ensure valid values are used during execution of the program.
     */
    private int min;
    /**
      Maximum number of products for inventory. This is the maximum number of products the retailer wants on hand at a given time.
     */
    private int max;

    /**
      Constructor for new product object. The project can have associated parts but they are not required.

      @param id          Product ID.
      @param productName Product's name.
      @param price       Product price.
      @param stock       Inventory level of product.
      @param min         Minimum number of products for inventory.
      @param max         Maximum number of products for inventory.
     */
    public Product(int id, String productName, double price, int stock, int min, int max) {
        this.associatedParts = FXCollections.observableArrayList();
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
      Getter for product name. The product name can be used in searches to return matching products.

      @return Product name.
     */
    public String getProductName() {
        return productName;
    }

    /**
      Setter for product name. Products can also be identified by unique ID if the product names become too similar.

      @param productName the name of the product.
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
      Getter for price of product. Current prices are represented in USD.

      @return price of the product.
     */
    public double getPrice() {
        return price;
    }

    /**
      Setter for price of product. The price of the product is represented in USD.

      @param price price of product.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
      Getter for product inventory level. The inventory or stock is the number of the product currently on hand.

      @return product inventory level.
     */
    public int getStock() {
        return stock;
    }

    /**
      Setter for Inventory level of product. Sets the stock or inventory level for the product.

      @param stock product inventory level.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
      Getter for the minimum number of product. Checks exist in the program to ensure a valid value is entered.

      @return minimum number of product.
     */
    public int getMin() {
        return min;
    }

    /**
      Setter for the minimum number of product. If the minimum stock entry is invalid an alert requires the user to correct or cancel changes made to the product.

      @param min minimum number of product.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
      Getter for the maximum number of product. The max stock of a product that the retailer wants to have on hand.

      @return maximum number of product.
     */
    public int getMax() {
        return max;
    }

    /**
      Setter for the maximum number of product. The number of products should equal or be less than the max, tests in the code ensure this is the case.

      @param max maximum number of product.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
      Getter for product ID. Product ID are automatically assigned and are unique.

      @return product ID.
     */
    public int getId() {
        return id;
    }

    /**
      Setter for the product ID. Product ID numbers increment by 1 for each new product and start at 10000

      @param id product ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
      Adds part from all parts to associated parts list for the product. A product does not need associated parts to be saved.

      @param part the part to add.
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
      Gets the list of associated parts for the product. For a product to be deleted it must not contain associated parts.

      @return list of parts.
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

    /**
      Delete a part from the products associated parts list. Removes the part from the associated parts list for a given product. All such parts must be removed for a product to be deleted.

      @param selectedAssocPart part to delete.
      @return a boolean indicator for the status of the delete.
     */
    public boolean deleteAssociatedPart(Part selectedAssocPart) {
        try {
            associatedParts.remove(selectedAssocPart);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}