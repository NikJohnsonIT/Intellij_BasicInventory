package model;

/**
  Models an Outsourced part.
  @author Nicholas Johnson
 */

public class Outsourced extends Part {

    /**
      Company Name for the part. This is unique to outsourced parts.
     */
    private String companyName;

    /**
      Constructor for new Outsourced object. This object contains a Company Name, where Inhouse objects instead have machine ID.
      @param id id for the part.
      @param stock inventory level for the part.
      @param min minimum stock requirements for part.
      @param max maximum stock requirements for part.
      @param partName Name of the part.
      @param price price for the part.
      @param companyName name of the company that makes the part.
     */
    public Outsourced(int id, int stock, int min, int max, String partName, double price, String companyName) {
        super(id, stock, min, max, partName, price);
        setCompanyName(companyName);
    }


    /**
      Getter for the companyName. This returns the name of the manufacturer of the part.
      @return name of the company.
     */
    public String getCompanyName()
    {
        return companyName;
    }

    /**
      Setter for the companyName. This sets the value of the campany name to the desired manufacturer.
      @param companyName name of the company.
     */
    public void setCompanyName(String companyName)
    {
       this.companyName =  companyName;
    }
}
