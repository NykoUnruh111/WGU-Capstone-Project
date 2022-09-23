package Handlers;

import MainPackage.Main;
import Models.Component;
import Models.PCB;
import Models.SMD;
import Models.ThroughHole;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.Instant;

/**
 * This class handles all of the database related calls
 */
public class DatabaseHandler {

    /**
     * Static strings that hold database connection information
     */
    private static final String DBNAME = "sql3400558";
    private static final String DBUSER = "sql3400558";
    private static final String DBPASS = "7EABEArkHu";
    //private static final String DBSERVERNAME = "wgudb.ucertify.com";
    private static final String DBURL = "jdbc:mysql://sql3.freesqldatabase.com/" + DBNAME;
    private static final String DBDRIVER = "com.mysql.cj.jdbc.Driver";
    private static Connection connection;
    public static String currentUser;

    public static ObservableList<Component> allComponentList = FXCollections.observableArrayList();
    public static ObservableList<PCB> allPCBList = FXCollections.observableArrayList();

    public static ObservableList<Component> visibleComponents = FXCollections.observableArrayList();
    public static ObservableList<PCB> visiblePCB = FXCollections.observableArrayList();

    /**public static ObservableList<Country> countryList = FXCollections.observableArrayList();
    public static ObservableList<Division> divisionList = FXCollections.observableArrayList();
    public static ObservableList<Contact> contactList = FXCollections.observableArrayList();
    public static ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    public static ObservableList<Appointment> sortedAppointmentList = FXCollections.observableArrayList();
    public static ObservableList<String> typeList = FXCollections.observableArrayList();**/

    /**
     * Function to log user in
     * @param username
     * @param password
     */
    public static Boolean login(String username, String password) {

        System.out.println("Attempting login");

        try {

            Statement statement = connection.createStatement();
            String dbquery = "SELECT * FROM users WHERE User_Name = '" + username + "' AND Password = '" + password + "'";
            ResultSet result = statement.executeQuery(dbquery);

            if (result.next()) {
                //System.out.println("User successfully logged in!");
                return true;
            } else {
                //System.out.println("Username or password is incorrect");
                return false;
            }


        } catch (SQLException throwables) {

            throwables.printStackTrace();
            return false;
        }



    }


    /**
     * Get components from database
     */

    public static void getAllComponents() {

        allComponentList.clear();

        try {

            Statement statement = connection.createStatement();
            String dbquery = "SELECT * FROM components";
            ResultSet results = statement.executeQuery(dbquery);
            while (results.next()) {

                if (results.getBoolean("isThroughHole")) {

                    System.out.println("Found something");

                    ThroughHole temp = new ThroughHole(results.getInt("ComponentID"),
                            results.getString("Component_Name"),
                            results.getFloat("Component_Price"));

                    allComponentList.add(temp);

                } else {

                    SMD temp = new SMD(results.getInt("ComponentID"),
                            results.getString("Component_Name"),
                            results.getFloat("Component_Price"));

                    allComponentList.add(temp);

                }

            }

            statement.close();

        } catch (SQLException throwables) {

            throwables.printStackTrace();

        }

    }



    public static void addComponent(String componentName, double price, boolean isThroughHole) {

        try {

            Statement statement = connection.createStatement();
            String dbquery = "INSERT INTO components (Component_Name, Component_Price, isThroughHole) "
                    + "VALUES ('" + componentName + "', "
                    +  price + ", "
                    + isThroughHole + ")";

            statement.executeUpdate(dbquery);


        } catch (SQLException throwables) {

            throwables.printStackTrace();

        }

    }


    /**
     * Update component in database
     * @param componentID
     * @param componentName
     * @param price
     * @param isThrougHole
     */
    public static void updateComponent(int componentID, String componentName, double price, boolean isThrougHole) {

        try {

            Statement statement = connection.createStatement();
            String dbquery = "UPDATE components SET Component_Name = '" + componentName
                    + "', Component_Price = " + price
                    + ", isThroughHole = " + isThrougHole
                    + " WHERE ComponentID = " + componentID;


            statement.executeUpdate(dbquery);


        } catch (SQLException throwables) {

            throwables.printStackTrace();

        }

    }

    /**
     * Delete component from database
     * @param componentID
     */
    public static void deleteComponent(int componentID) {

        try {

            Statement statement = connection.createStatement();
            String dbquery = "DELETE FROM components WHERE ComponentID = " + componentID;

            /*String dbquery = "INSERT INTO customers SET Customer_Name = '" + customerName
                    + "', Address = '" + customerAddress
                    + "', Postal_Code = '" + customerPostal
                    + "', Phone = " + customerPhone
                    + "', Create_Date = " + java.sql.Timestamp.from(Instant.now())
                    + ", Created_By ='" + currentUser
                    + "', Last_Update = " + java.sql.Timestamp.from(Instant.now())
                    + ", Last_Updated_By ='" + currentUser
                    + ", Division_ID = " + divisionID;*/

            statement.executeUpdate(dbquery);

            //Main.createAlert("Success!", "Successfully deleted appointment");


        } catch (SQLException throwables) {

            Main.createAlert("Error!", "Cannot delete appointment!");

        }

    }

    /**
     * Get all PCB from database
     */
    public static void getAllPCB() {

        allPCBList.clear();

        try {

            Statement statement = connection.createStatement();
            String dbquery = "SELECT * FROM pcb";
            ResultSet results = statement.executeQuery(dbquery);
            while (results.next()) {


                PCB temp = new PCB(results.getInt("PCB_ID"),
                        results.getString("PCB_Name"));

                allPCBList.add(temp);

            }

            statement.close();

        } catch (SQLException throwables) {

            throwables.printStackTrace();

        }

    }

    /**
     * Add PCB to database
     * @param pcbName
     */
    public static void addPCB(String pcbName) {

        try {

            Statement statement = connection.createStatement();
            String dbquery = "INSERT INTO pcb (PCB_Name) "
                    + "VALUES ('" + pcbName + "')";

            statement.executeUpdate(dbquery);


        } catch (SQLException throwables) {

            throwables.printStackTrace();

        }

    }

    public static void updatePCB(int pcbID, String pcbName) {

        try {

            Statement statement = connection.createStatement();
            String dbquery = "UPDATE pcb SET PCB_Name = '" + pcbName
                    + "' WHERE PCB_ID = " + pcbID;


            statement.executeUpdate(dbquery);


        } catch (SQLException throwables) {

            throwables.printStackTrace();

        }

    }


    /**
     * Delete pcb from database
     * @param pcbID
     */
    public static void deletePCB(int pcbID) {

        try {

            Statement statement = connection.createStatement();
            String dbquery = "DELETE FROM pcb WHERE PCB_ID = " + pcbID;

            /*String dbquery = "INSERT INTO customers SET Customer_Name = '" + customerName
                    + "', Address = '" + customerAddress
                    + "', Postal_Code = '" + customerPostal
                    + "', Phone = " + customerPhone
                    + "', Create_Date = " + java.sql.Timestamp.from(Instant.now())
                    + ", Created_By ='" + currentUser
                    + "', Last_Update = " + java.sql.Timestamp.from(Instant.now())
                    + ", Last_Updated_By ='" + currentUser
                    + ", Division_ID = " + divisionID;*/

            statement.executeUpdate(dbquery);

            //Main.createAlert("Success!", "Successfully deleted appointment");


        } catch (SQLException throwables) {

            Main.createAlert("Error!", "Cannot delete PCB!");

        }

    }

    /**
     * Gets all components associated to pcbID
     * @param pcbID
     */
    public static ObservableList<Component> getPCBComponents(int pcbID) {

        ObservableList<Component> addedComponents = FXCollections.observableArrayList();

        try {

            Statement statement = connection.createStatement();
            String dbquery = "SELECT * FROM pcb_components WHERE PCB_ID = " + pcbID;
            ResultSet results = statement.executeQuery(dbquery);
            while (results.next()) {


                for (int i = 0; i < allComponentList.size(); i++) {

                    if (results.getInt("ComponentID") == allComponentList.get(i).getID()) {

                        Component temp = allComponentList.get(i);
                        temp.setQuantity(results.getInt("Quantity"));

                        addedComponents.add(temp);

                    }

                }

            }

            statement.close();

        } catch (SQLException throwables) {

            throwables.printStackTrace();

        }

        return addedComponents;

    }

    /**
     * Saves the component and pcb association in the database
     * @param pcbID
     * @param componentID
     */
    public static void addPCBComponents(int pcbID, int componentID, int Quantity) {

        try {

            //Get all components to use to check and prevent double entry
            ObservableList<Component> checkComponents = FXCollections.observableArrayList();
            checkComponents = getPCBComponents(pcbID);
            boolean isDupe = false;

            for (int i = 0; i < checkComponents.size(); i++) {

                if (checkComponents.get(i).getID() == componentID) {

                    System.out.println("dupe entry");
                    isDupe = true;

                }

            }

            if (!isDupe) {
                Statement statement = connection.createStatement();
                String dbquery = "INSERT INTO pcb_components (PCB_ID, ComponentID, Quantity) "
                        + "VALUES ('" + pcbID + "', '" + componentID + "', " + Quantity + ")";

                statement.executeUpdate(dbquery);

                statement.close();

            } else {

                Statement statement = connection.createStatement();
                String dbquery = "UPDATE pcb_components SET Quantity = " + Quantity
                        + " WHERE PCB_ID = " + pcbID
                        + " AND ComponentID = " + componentID;

                statement.executeUpdate(dbquery);

                statement.close();

            }




        } catch (SQLException throwables) {

            throwables.printStackTrace();

        }

    }

    /**
     * Delete pcb from database
     * @param pcbID
     */
    public static void deletePCBComponents(int pcbID, int componentID) {

        try {

            Statement statement = connection.createStatement();
            String dbquery = "DELETE FROM pcb_components WHERE PCB_ID = " + pcbID + " AND ComponentID = " + componentID;

            /*String dbquery = "INSERT INTO customers SET Customer_Name = '" + customerName
                    + "', Address = '" + customerAddress
                    + "', Postal_Code = '" + customerPostal
                    + "', Phone = " + customerPhone
                    + "', Create_Date = " + java.sql.Timestamp.from(Instant.now())
                    + ", Created_By ='" + currentUser
                    + "', Last_Update = " + java.sql.Timestamp.from(Instant.now())
                    + ", Last_Updated_By ='" + currentUser
                    + ", Division_ID = " + divisionID;*/

            statement.executeUpdate(dbquery);

            //Main.createAlert("Success!", "Successfully deleted appointment");


        } catch (SQLException throwables) {

            Main.createAlert("Error!", "Cannot delete PCB!");

        }

    }

    public static ObservableList<Component> getPCBComponentsReport() {

        ObservableList<Component> addedComponents = FXCollections.observableArrayList();

        try {

            Statement statement = connection.createStatement();
            String dbquery = "SELECT * FROM pcb_components";
            ResultSet results = statement.executeQuery(dbquery);
            while (results.next()) {


                for (int i = 0; i < allComponentList.size(); i++) {

                    if (results.getInt("ComponentID") == allComponentList.get(i).getID()) {

                        boolean bHasMatch = false;

                        for (int j = 0; j < addedComponents.size(); j++) {

                            if (results.getInt("ComponentID") == addedComponents.get(j).getID()) {

                                addedComponents.get(j).setQuantity(addedComponents.get(j).getQuantity() + results.getInt("Quantity"));
                                bHasMatch = true;

                            }

                        }

                        if (!bHasMatch) {

                            Component temp = allComponentList.get(i);
                            temp.setQuantity(results.getInt("Quantity"));

                            addedComponents.add(temp);

                        }


                    }

                }

            }

            statement.close();

        } catch (SQLException throwables) {

            throwables.printStackTrace();

        }

        return addedComponents;

    }


    /**
     * Function for connecting to database
     */

    public static void dbConnect() {

        try {
            Class.forName(DBDRIVER);

            connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
            System.out.println("Connected");
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            System.out.println("Not connected");
        }

    }

    /**
     * Function for disconnecting database.
     */

    public static void dbDisconnect() {

        try {
            connection.close();
            System.out.println("DB Disconnect success.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("DB Disconnect failed.");
        }

    }

}
