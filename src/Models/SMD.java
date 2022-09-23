package Models;

public class SMD extends Component {
    /**
     * Constructor for component
     *
     * @param componentID
     * @param name
     * @param price
     */
    public SMD(int componentID, String name, float price) {
        super(componentID, name, price);
    }

    /**
     * Getter for type of component
     */
    @Override
    public String getType() {

        return "SMD";

    }
}
