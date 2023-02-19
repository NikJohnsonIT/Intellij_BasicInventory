package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Part;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/**
Inventory of Parts and Products.
Data is persistent for application.
*@author Nicholas Johnson
 */
public class Inventory {


    /**
      The list of all parts in the Inventory. This includes Inhouse and Outsourced Parts.
     */
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    /**
      The list of all products in the Inventory. If there are no default entries, the table will appear empty.
     */
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();
    /**
      List of filtered parts. Used when the user searches for a part.
     */
    private static final ObservableList<Part> filteredParts = FXCollections.observableArrayList();
    /**
      List of filtered products. Used when the user searches for a product.
     */
    private static final ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
    /**
      Starting value for part ID numbers. As new parts are added, the value will increment.
     */
    private static final AtomicInteger partsIdGenerator = new AtomicInteger(1);
    /**
      Starting value for product ID numbers. As new products are added the value will increment.
     */
    private static final AtomicInteger productsIdGenerator = new AtomicInteger(10000);


    /**
      Getter for the Part ID number. This method returns the next ID number.
      @return A new ID number that is one higher than the most recent entry. The system allows for 9999 parts.
     */
    public static AtomicInteger getPartsIdGenerator() {
        return partsIdGenerator;
    }

    /**
     Getter for the Product ID number. This method returns the next ID number.
     @return A new ID number that is one higher than the most recent entry. The system begins product numbers at 10000.
     */
    public static AtomicInteger getProductsIdGenerator(){
        return productsIdGenerator;
    }
    /**
      Adds part to Inventory. The part will appear in the all parts table views.
      @param newPart Part object being added to Inventory.
     */
    public static void addPart(Part newPart) {
        if (newPart != null) allParts.add(newPart);
    }

    /**
      Adds product to Inventory. Product will now appear in the products table views.
      @param newProduct Product object being added to Inventory.
     */
    public static void addProduct(Product newProduct){
        if (newProduct != null) allProducts.add(newProduct);
    }


    /**
      Searches product list by ID. If a match exists it is returned.
      @param productId unique product ID.
      @return the product if found, else null is returned.
     */
    public static Product lookupProduct(int productId){
        ObservableList<Product> productObservableList = Inventory.getAllProducts();
        for (Product product : productObservableList) {
            if(product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
      Searches parts list by ID. If a match exists it is returned.
      @param partId unique product ID.
      @return the part if a matching record is found, else null is returned.
     */
    public static Part lookupPart(int partId) {
        for (Part part : Inventory.getAllParts()) {
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }


    /**
      Searches products list by Product Name. If one or multiple matches are found they are returned to the user.
      @param productName name of the product.
      @return a list of products matching the string input.
     */
    public static ObservableList<Product> lookupProduct(String productName){
       ObservableList<Product> productsFound = FXCollections.observableArrayList();
       for (Product product : Inventory.getAllProducts()) {
           if (product.getProductName().toLowerCase().contains(productName.toLowerCase())) {
               productsFound.add(product);
           }
       }
       return productsFound;
    }

    /**
      Searches parts list by Part Name. If one or multiple matches are found they are returned to the user.
      @param partName the name of the part.
      @return a list of parts matching the string input.
     */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        for (Part part : Inventory.getAllParts()) {
            if (part.getPartName().toLowerCase().contains(partName.toLowerCase())) {
                partsFound.add(part);
            }
        }
        return partsFound;
    }

    /**
      Gets a list of all parts in the Inventory. This includes Inhouse and Outsourced parts.
      @return List of all parts.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
      Gets a list of all products in the Inventory. The user can then add or modify products as necessary.
      @return List of all products.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }


    /**
      Replaces product in the products list. The product will now appear in the products tableviews in the place of the old product.
      @param index Index of the product that will be replaced.
      @param selectedProduct replacement product.
     */
    public static void updateProduct(int index, Product selectedProduct){
        allProducts.set(index, selectedProduct);
    }

    /**
      Replaces part in the parts list. The new part will now appear in the old one's place.
      @param index Index of part that will be replaced.
      @param selectedPart the replacement part.
     */
    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    /**
      Deletes part from the parts list. The part will no longer appear in the parts table views.
      @param selectedPart the part that will be removed.
      @return boolean indicator of part removal status.
     */
    public static boolean deletePart(Part selectedPart){
        try {
            allParts.remove(selectedPart);
        } catch (Exception e){
            return false;
        }
            return true;
    }

    /**
      Swaps part from main menu with the modified part after the part is saved. The new part will now appear in the part table views.
      @param selectedPart the part to be swapped.
      @return boolean indicator of swap status.
     */
    public static boolean swapPartModifyMenu(Part selectedPart){
        if(allParts.contains(selectedPart)){
            allParts.remove(selectedPart);
            return true;
        } else {
            return false;
        }
    }

    /**
      Swaps product from main menu with the modified product after the product is saved. The product will now appear in the product table views.
      @param selectedProduct the part to be swapped.
      @return boolean indicator of swap status.
     */
    public static boolean swapProductModifyMenu(Product selectedProduct){
        if(allProducts.contains(selectedProduct)){
            allProducts.remove(selectedProduct);
            return true;
        } else {
            return false;
        }
    }

    /**
      Removes product from products list. The product will no longer appear in the tableviews.
      @param selectedProduct product that will be removed.
      @return boolean indicator of product removal status.
     */
    public static boolean deleteProduct(Product selectedProduct){
        try {
            allProducts.remove(selectedProduct);
        }catch (Exception e) {
            return false;
        }
        return true;
    }

}
