package model;

/**
  models an InHouse Part. Inhouse parts are unique from outsourced parts in that they have a machine ID field instead of a Company Name field.
  @author Nicholas Johnson
 */
public class InHouse extends Part {

    /**
      machine ID for the part. This value is unique to Inhouse parts.
     */
    int machineId;

    /**
      Constructor for new InHouse object. The object contains a unique parameter for machineID and also has an automatically generated Part ID number.
      @param id ID for the part.
      @param stock inventory level of the part.
      @param min minimum stock requirement for part.
      @param max maximum stock requirement for part.
      @param partName name of the part.
      @param price price of the part.
      @param machineId machine ID for the part.
     */
    public InHouse(int id, int stock, int min, int max, String partName, double price, int machineId) {
        super(id, stock, min, max, partName, price);
        setMachineId(machineId);
    }


    /**
      Getter for the machine ID. This is a value only relevant to Inhouse parts.
      @return the parts machineId.
     */
    public int getMachineId()
    {
    return machineId;
    }

    /**
      Setter for machine ID. Only Inhouse parts have this method.
      @param machineId machineId for the part.
     */
    public void setMachineId(int machineId)
    {
     this.machineId = machineId;

    }
}
