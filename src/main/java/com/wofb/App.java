package com.wofb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class App {
//    private static final Logger LOGGER = LogManager.getLogger(App.class);
    public static void main(String[] args) throws IOException, SQLException {

        final Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("src/resources/config.properties");
            prop.load(input);
            System.out.println();
/*
            LOGGER.info("Starting up applicaiton with property file infos: ");
            LOGGER.info(prop.getProperty("db_url"));
            LOGGER.info(prop.getProperty("uname"));
            LOGGER.info(prop.getProperty("pwd"));
            LOGGER.info("input_file");
*/
            System.setProperties(prop);
        } catch (IOException ex) {
//            LOGGER.error(ex.getMessage());
            System.out.println(ex.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
//                    LOGGER.error(e.getMessage());
                }
            }
        }

        final String inputFilePath = System.getProperty("input_file");

        CSVProcessor processor = new CSVProcessor(inputFilePath);
        processor.startProcessing();
    }

}


