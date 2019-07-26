package com.wofb;

import com.wofb.CSVProcessor;
import com.wofb.OutputFileLine;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CSVProcessorTest {

    final String inputFilePath = "src/resources/oneLinerTestInputFile.csv";
    CSVProcessor csvProcessor;

    @Before
    public void init() {
        csvProcessor = new CSVProcessor(inputFilePath);
    }

    @Test
    public void testCSVFileRead() throws IOException {
        final List<InputFileLine> inputList = csvProcessor.readInputFile(inputFilePath);
        final InputFileLine input = inputList.get(0);
        assertThat(input.getLineNumber(), is(1));
        assertThat(input.getOrderItemId(), is("256"));
        assertThat(input.getOrderId(), is("356"));
        assertThat(input.getBuyerName(), is("Thomas Great"));
        assertThat(input.getBuyerEmail(), is("greatT@yes.hu"));
        assertThat(input.getAddress(), is(""));
        assertThat(input.getPostcode(), is("2256"));
        assertThat(input.getSalePrice(), is("1.00"));
        assertThat(input.getShippingPrice(), is("2.00"));
        assertThat(input.getSKU(), is("12345-WHT-XL"));
        assertThat(input.getStatus(), is("IN_STOCK"));
        assertThat(input.getOrderDate(), is(""));
    }

    @Test
    public void testCSVFileWrite() throws IOException {
        final OutputFileLine output = new OutputFileLine(1);
        output.setMessage("Postcode must be a number.");
        output.setStatus(OutputFileLine.Status.ERROR);
        csvProcessor.createResponseFile(Arrays.asList(output));
        TestInputFile testInputFile = readBackOutputFile("." + File.separator + "generated_output" + File.separator + "responseFile.csv");
        assertThat(testInputFile.getLineNumber(), is("1"));
        assertThat(testInputFile.getStatus(), is("ERROR"));
        assertThat(testInputFile.getMessage(), is("Postcode must be a number."));
    }

    private TestInputFile readBackOutputFile(final String filePath) throws IOException {
        TestInputFile testInputFile = null;
        try (
                final Reader reader = Files.newBufferedReader(Paths.get(filePath));
                final CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withIgnoreEmptyLines(false)
                        .withTrim());

        ) {
            for (CSVRecord csvRecord : csvParser) {
                // Accessing values by Header names

                final String lineNumber = csvRecord.get("LineNumber");
                final String status = csvRecord.get("Status");
                final String message = csvRecord.get("Message");

                testInputFile = new TestInputFile(lineNumber, status, message);
            }
            return testInputFile;
        }
    }

    class TestInputFile {
        private final String lineNumber;
        private final String status;
        private final String message;

        public TestInputFile(String lineNumber, String status, String message) {
            this.lineNumber = lineNumber;
            this.status = status;
            this.message = message;
        }

        public String getLineNumber() {
            return lineNumber;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }

}