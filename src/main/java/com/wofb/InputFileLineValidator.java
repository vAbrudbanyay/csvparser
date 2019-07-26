package com.wofb;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class InputFileLineValidator {
   // private static final Logger LOGGER = LogManager.getLogger(InputFileLineValidator.class);

    public enum Status {
        IN_STOCK,
        OUT_OF_STOCK
    }

    private InputFileLine input;
    private List<String> errorMsg;
    private Connection connection;
    private DB db;

    public InputFileLineValidator(InputFileLine input, final Connection connection) {
        this.input = input;
        this.connection = connection;
        db = new DB();
        db.setConnection(connection);
    }

    public OutputFileLine validate() {
        errorMsg = new ArrayList<>();
        errorMsg.addAll(validateName());
        errorMsg.addAll(validateEmail());
        errorMsg.addAll(validatePostCode());
        errorMsg.addAll(validateDateFormat());
        errorMsg.addAll(validateStatus());
        errorMsg.addAll(validateShippingPrice());
        errorMsg.addAll(validateSalePrice());
     //   errorMsg.addAll(validateOrderId());
     //   errorMsg.addAll(OrderItemId());

        final OutputFileLine output = new OutputFileLine(input.getLineNumber());
        if (isEmpty(errorMsg)) {
            output.setStatus(OutputFileLine.Status.OK);
        } else {
            output.setStatus(OutputFileLine.Status.ERROR);
            output.setMessage(errorMsg.toString());
        }
        return output;
    }

    List<String> validateSKU() {
        final List<String> errors = new ArrayList<>();
        if (isEmpty(input.getSKU())) {
            errors.add("SKU cannot be empty.");
            return errors;
        }
        return errors;
    }

    List<String> validateName() {
        final List<String> errors = new ArrayList<>();
        if (isEmpty(input.getBuyerName())) {
            errors.add("Buyer Name cannot be empty.");
            return errors;
        }
        return errors;
    }

    List<String> validateEmail() {
        final List<String> errors = new ArrayList<>();
        if (isEmpty(input.getBuyerEmail())) {
            errors.add("Buyer Email cannot be empty.");
            return errors;
        }

        if (!EmailValidator.getInstance().isValid(input.getBuyerEmail())) {
            errors.add("Email must be in a valid email address format.");
        }
        return errors;
    }

    List<String> validatePostCode() {
        final List<String> errors = new ArrayList<>();
        if (isEmpty(input.getPostcode())) {
            errors.add("Postcode cannot be empty.");
            return errors;
        }

        try {
            Integer.parseInt(input.getPostcode());
        } catch (Exception e) {
            errors.add("Postcode must be a number.");
        }
        return errors;
    }

    List<String> validateDateFormat() {
        // can be empty
        if (isEmpty(input.getOrderDate())) {
            return emptyList();
        }

        // must be valid format yyyy-MM-dd
        // must use today's date

        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        final List<String> errors = new ArrayList<>();

        try {
            dateFormat.parse(input.getOrderDate().trim());
        } catch (ParseException pe) {
            errors.add("Date must be provided in the yyyy-MM-dd format.");
        }
        return errors;
    }

    List<String> validateStatus() {
        final List<String> errors = new ArrayList<>();
        if (isEmpty(input.getStatus())) {
            errors.add("Status cannot be empty.");
            return errors;
        }

        if (!(input.getStatus().equalsIgnoreCase(Status.IN_STOCK.name()) ||
                input.getStatus().equalsIgnoreCase(Status.OUT_OF_STOCK.name()))) {
            errors.add("Status can only be either IN_STOCK or OUT_OF_STOCK.");

        }

        return errors;
    }

    List<String> validateShippingPrice() {
        final List<String> errors = new ArrayList<>();
        if (isEmpty(input.getShippingPrice())) {
            errors.add("ShippingPrice cannot be empty.");
            return errors;
        }

        Double shippingPrice = null;
        try {
            shippingPrice = Double.parseDouble(input.getShippingPrice());
        } catch (Exception e) {
            errors.add("Shipping price must be a number.");
        }

        if (shippingPrice < 0.00) {
            errors.add("Shipping price must be a positive number.");
        }
        return errors;
    }

    List<String> validateSalePrice() {
        final List<String> errors = new ArrayList<>();
        if (isEmpty(input.getSalePrice())) {
            errors.add("SalePrice cannot be empty.");
            return errors;
        }

        Double salesPrice = null;
        try {
            salesPrice = Double.parseDouble(input.getSalePrice());
        } catch (Exception e) {
            errors.add("SalePrice must be a number.");
        }

        if (salesPrice < 1.00) {
            errors.add("SalePrice must be at least 1.00.");
        }
        return errors;
    }

    List<String> validateOrderId() {
        DB db = new DB();
        db.setConnection(connection);

        final List<String> errors = new ArrayList<>();
        if (isEmpty(input.getOrderId())) {
            errors.add("OrderId cannot be empty.");
            return errors;
        }

        try {
            if (db.getOrderId(input.getOrderId()) != null) {
                errors.add("OrderId is already present in the database.");
            }
        } catch (SQLException e) {
     //       LOGGER.debug(e.getMessage());
        }
        return errors;
    }

    List<String> OrderItemId() {
        DB db = new DB();
        db.setConnection(connection);

        final List<String> errors = new ArrayList<>();
        if (isEmpty(input.getOrderItemId())) {
            errors.add("OrderItemId cannot be empty.");
            return errors;
        }

        try {
            if (db.getOrderItemId(input.getOrderItemId()) != null) {
                errors.add("OrderItemId is already present in the database.");
            }
        } catch (SQLException e) {
   //         LOGGER.debug(e.getMessage());
        }
        return errors;
    }
}
