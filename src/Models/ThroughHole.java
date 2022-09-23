package Models;

public class ThroughHole extends Component {
    /**
     * Constructor for component
     *
     * @param componentID
     * @param name
     * @param price
     */
    public ThroughHole(int componentID, String name, float price) {
        super(componentID, name, price);
    }

    /**
     * Getter for type of component
     */
    @Override
    public String getType() {

        return "Through Hole";

    }
}
