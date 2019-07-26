package com.wofb;

import com.wofb.InputFileLineValidator;
import com.wofb.OutputFileLine;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class InputFileLineValidatorTest {

    private InputFileLineValidator validator;
    private Connection connection;

    @Before
    public void init() {
        connection = mock(Connection.class);
    }

    @Test
    public void testEmailNotValidFormat() {
        final InputFileLine input = new InputFileLine();
        input.setBuyerEmail("spam.15.!.net");
        validator = new InputFileLineValidator(input, connection);
        List<String> error = validator.validateEmail();
        assertThat(error.size(), is(1));
        assertThat(error, hasItem("Email must be in a valid email address format."));
    }

    @Test
    public void testEmailValidFormat() {
        final InputFileLine input = new InputFileLine();
        input.setBuyerEmail("my-email@mycompany.com");
        validator = new InputFileLineValidator(input, connection);
        List<String> error = validator.validateEmail();
        assertThat(error.size(), is(0));
    }

    @Test
    public void testEmailEmpty() {
        final InputFileLine input = new InputFileLine();
        validator = new InputFileLineValidator(input, connection);
        List<String> error = validator.validateEmail();
        assertThat(error.size(), is(1));
        assertThat(error, hasItem("Buyer Email cannot be empty."));
    }

    @Test
    public void testStatusError() {
        final InputFileLine input = new InputFileLine();
        input.setStatus("NONE");
        validator = new InputFileLineValidator(input, connection);
        List<String> error = validator.validateStatus();
        assertThat(error.size(), is(1));
        assertThat(error, hasItem("Status can only be either IN_STOCK or OUT_OF_STOCK."));
    }

    @Test
    public void testDateErrorDashFormat() {
        final InputFileLine input = new InputFileLine();
        input.setOrderDate("2005/11/17");
        validator = new InputFileLineValidator(input, connection);
        List<String> error = validator.validateDateFormat();
        assertThat(error.size(), is(1));
        assertThat(error, hasItem("Date must be provided in the yyyy-MM-dd format."));
    }

    @Test
    public void testDateOrderError() {
        final InputFileLine input = new InputFileLine();
        input.setOrderDate("07-11-2005");
        validator = new InputFileLineValidator(input, connection);
        List<String> error = validator.validateDateFormat();
        assertThat(error.size(), is(1));
        assertThat(error, hasItem("Date must be provided in the yyyy-MM-dd format."));
    }

    @Test
    public void testEmptyOrderDate() {
        final InputFileLine input = new InputFileLine();
        validator = new InputFileLineValidator(input, connection);
        List<String> error = validator.validateDateFormat();
        assertThat(error.size(), is(0));
    }

    @Test // TODO uncomment DB related validations in InputFileValidator once DB methods are fixed.
    public void testCompleteOutputFileWithError() {
        final InputFileLine input = new InputFileLine(1, "177", "253", "My Name", "myname@myemail.com", "my Address", "H-1793", "1.00", "0.00", "SKU value", "NOT AVAILABLE", "");
        validator = new InputFileLineValidator(input, connection);
        final OutputFileLine output = validator.validate();
        assertThat(output.getLineNumber(), is(input.getLineNumber()));
        assertThat(output.getStatus(), is(OutputFileLine.Status.ERROR));
        assertThat(output.getMessage(), is(Arrays.asList("Postcode must be a number.", "Status can only be either IN_STOCK or OUT_OF_STOCK.").toString()));
    }

    @Test // TODO uncomment DB related validations in InputFileValidator once DB methods are fixed.
    public void testCompleteOutputFileWithSuccess() {
        final InputFileLine input = new InputFileLine(1, "177", "253", "My Name", "myname@myemail.com", "my Address", "1793", "1.00", "0.00", "SKU value", "IN_STOCK", "");
        validator = new InputFileLineValidator(input, connection);
        final OutputFileLine output = validator.validate();
        assertThat(output.getLineNumber(), is(input.getLineNumber()));
        assertThat(output.getStatus(), is(OutputFileLine.Status.OK));
        assertThat(output.getMessage(), is(nullValue()));
    }

    // TODO add more test cases as per InputFileLineValidator method expectations.

}