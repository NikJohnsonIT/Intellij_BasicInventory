package model;

/**
  This class models a part. The classes Outsourced and InHouse extend this class.
  @author Nicholas Johnson
 */
public abstract class Part {

    private int id, stock, min, max;
    private String partName;
    private double price;

    public Part(int id, int stock, int min, int max, String partName, double price) {
        this.id = id;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.partName = partName;
        this.price = price;
    }

    /**
      getter for part ID. This is a unique and automatically generated value that the user cannot manually change.
      @return the ID.
     */
    public int getId() {
        return id;
    }

    /**
      Setter for ID. This sets a new unique value for a part ID.
      @param id the ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
      Getter for inventory level of the part. Also referred to as stock throughout the code.
      @return the inventory level.
     */
    public int getStock() {
        return stock;
    }

    /**
      Setter for inventory level. This sets the inventory level, checks exist to ensure valid values.
      @param stock the inventory level to be set.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
      Getter for minimum stock requirement. Checks exist to ensure valid values are entered.
      @return minimum stock.
     */
    public int getMin() {
        return min;
    }

    /**
      Setter for minimum stock. Sets the minimum inventory level which is a checked value.
      @param min minimum stock to be set.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
      Getter for maximum stock requirements. This is the maximum stock the retailer will require for a given part.
      @return maximum stock.
     */
    public int getMax() {
        return max;
    }

    /**
      Setter for maximum stock requirement. This value introduces a bound for verious checks throughout the code to ensure valid values are being used.
      @param max max stock to be set.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
      Getter for partName. This part name can be used later to search for a part.
      @return name of the part.
     */
    public String getPartName() {
        return partName;
    }

    /**
      Setter for partName. Sets the name of the part.
      @param partName part name to be set.
     */
    public void setPartName(String partName) {
        this.partName = partName;
    }

    /**
      Getter for the price of the part. Current part prices are represented in USD.
      @return price of the part.
     */
    public double getPrice() {
        return price;
    }

    /**
      setter for the price of the part. All prices are in USD.
      @param price price to be set.
     */
    public void setPrice(double price) {
        this.price = price;
    }
}
