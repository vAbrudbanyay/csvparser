package com.wofb;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CSVProcessor {

    //private static final Logger LOGGER = LogManager.getLogger(CSVProcessor.class);

    private String INPUT_CSV_FILE_PATH;
    private String OUTPUT_CSV_FILE_PATH = "."+File.separator+"generated_output";
    private DB db;
    private Connection dbConnection;
    private List<InputFileLine> inputFileLineList;


    public CSVProcessor(String inputFilePath) {
        this.INPUT_CSV_FILE_PATH = inputFilePath;
    }

    public void startProcessing() throws IOException, SQLException {
        inputFileLineList = readInputFile(INPUT_CSV_FILE_PATH);
        final List<OutputFileLine> outputFileLines = createValidatedOutputForDBProcess(inputFileLineList);
        dbInsertProcess(outputFileLines);

        if (dbConnection != null) {
            dbConnection.close();
        }

        createResponseFile(outputFileLines);
    }

    List<InputFileLine> readInputFile(final String inputFilePath) throws IOException {
        final List<InputFileLine> inputList = new ArrayList<>();
    //    LOGGER.info("Loading csv file " + inputFilePath);
        try (
                final Reader reader = Files.newBufferedReader(Paths.get(inputFilePath));
                final CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withIgnoreEmptyLines(false)
                        .withTrim());

        ) {
            for (CSVRecord csvRecord : csvParser) {
                // Accessing values by Header names

                final Long lineNumber = csvRecord.getRecordNumber();
                final String orderItemId = csvRecord.get("OrderItemId");
                final String orderId = csvRecord.get("OrderId");
                final String buyerName = csvRecord.get("BuyerName");
                final String buyerEmail = csvRecord.get("BuyerEmail");
                final String address = csvRecord.get("Address");
                final String postcode = csvRecord.get("Postcode");
                final String salePrice = csvRecord.get("SalePrice");
                final String shippingPrice = csvRecord.get("ShippingPrice");
                final String SKU = csvRecord.get("SKU");
                final String status = csvRecord.get("Status");
                final String orderDate = csvRecord.get("OrderDate");

                final InputFileLine input = new InputFileLine(lineNumber.intValue(), orderItemId, orderId, buyerName, buyerEmail, address,
                        postcode, salePrice, shippingPrice, SKU, status, orderDate);
                inputList.add(input);
            }
        }
     //   LOGGER.info("csv file " + INPUT_CSV_FILE_PATH + " loaded.");
        return inputList;
    }

    List<OutputFileLine> createValidatedOutputForDBProcess(final List<InputFileLine> inputList) {
        final List<OutputFileLine> outputFileLineList = new ArrayList<>();
        db = new DB();
        dbConnection = db.getConnection();

        for (final InputFileLine input : inputList) {
            final InputFileLineValidator validator = new InputFileLineValidator(input, dbConnection);
            outputFileLineList.add(validator.validate());
        }

        return outputFileLineList;
    }

    private void dbInsertProcess(List<OutputFileLine> outputFileLines) {
        for (final OutputFileLine output : outputFileLines) {
            if (output.getStatus().equals(OutputFileLine.Status.OK)) {
                insertToDb(output);
            }
        }
    }

    private void insertToDb(OutputFileLine output) {
        final InputFileLine input = inputFileLineList.get(output.getLineNumber() - 1);

        // TODO correct the method in DB.class to insert into tables
        try {
            db.dbInsertOperation(input);
        } catch (SQLException e) {
            output.setStatus(OutputFileLine.Status.ERROR);
            output.setMessage("Error during database operation: " + e.getMessage());
        }
    }

    void createResponseFile(List<OutputFileLine> outputFileLines) {
        final File file = new File(OUTPUT_CSV_FILE_PATH);
        if (!file.exists()) {
            file.mkdir();
        }

        try (
                final BufferedWriter writer = Files.newBufferedWriter(Paths.get(OUTPUT_CSV_FILE_PATH+File.separator+"responseFile.csv"));
                final CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("LineNumber", "Status", "Message"));
        ) {
            for (OutputFileLine output : outputFileLines) {
                csvPrinter.printRecord(output.getLineNumber(), output.getStatus().name(), output.getMessage());
            }

            csvPrinter.flush();

        } catch (IOException e) {
    //        LOGGER.debug(e.getMessage());
        }
    }
}
