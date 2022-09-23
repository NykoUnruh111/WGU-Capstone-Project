package Models;

public class Component {

    String name;
    float price;
    int componentID;
    int Quantity;

    /**
     * Constructor for component
     * @param componentID
     * @param name
     * @param price
     */
    public Component(int componentID, String name, float price) {

        this.componentID = componentID;
        this.name = name;
        this.price = price;
        this.Quantity = 0;

    }

    /**
     * Getter for name
     * @return
     */
    public String getName() {

        return name;

    }

    /**
     * Setter for name
     * @param name
     */
    public void setName(String name) {

        this.name = name;

    }

    /**
     * Getter for price
     * @return
     */
    public float getPrice() {

        return price;

    }

    /**
     * Setter for price
     * @param price
     */
    public void setPrice(float price) {

        this.price = price;

    }

    /**
     * Getter for component ID
     * @return
     */
    public int getID() {

        return componentID;

    }

    /**
     * Setter for component ID
     * @param componentID
     */
    public void setID(int componentID) {

        this.componentID = componentID;

    }

    /**
     * Getter for type of component
     */
    public String getType() {

        return "Component";

    }

    /**
     * Getter for quantity
     */
    public int getQuantity() {

        return Quantity;

    }

    /**
     * Setter for quantity
     */
    public void setQuantity(int Quantity) {

        this.Quantity = Quantity;

    }
}
