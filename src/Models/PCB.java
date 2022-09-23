package Models;

public class PCB {

    String name;
    int pcbID;

    /**
     * Constructor for component
     * @param pcbID
     * @param name
     */
    public PCB(int pcbID, String name) {

        this.pcbID = pcbID;
        this.name = name;

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
     * Getter for component ID
     * @return
     */
    public int getID() {

        return pcbID;

    }

    /**
     * Setter for component ID
     * @param pcbID
     */
    public void setID(int pcbID) {

        this.pcbID = pcbID;

    }

}
