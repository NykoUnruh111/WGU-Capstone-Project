package Handlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class handles all of the information logging
 */
public class LoggingHandler {

    /**
     * Method to take a string value and write it to file.
     * @param string
     */
    public static void writeLog(String string) {

        //Check if file is created, if not create file.
        try {
            File log = new File("Reports.txt");
            if (log.createNewFile()) {
                System.out.println("File generated: " + log.getName());
            } else {
                System.out.println("File was already made!");
            }

            FileWriter fileWriter = new FileWriter("Reports.txt", true);
            fileWriter.append(string);

            fileWriter.close();
            System.out.println("Success!");

        } catch (IOException e) {
            System.out.println("The logger is a bit borked up!");
            e.printStackTrace();
        }


    }

}
