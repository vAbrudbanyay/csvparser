package com.wofb;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class DB {
    //  private static final Logger LOGGER = LogManager.getLogger(DB.class);

    private final String URL;
    private final String USER_NAME;
    private final String USER_PWD;
    private Connection connection;

    final String INSERT_TO_ORDER_TABLE = "INSERT INTO ORDER (OrderId, BuyerName, BuyerEmail, OrderDate, OrderTotalValue, Address, Postcode)" +
            "VALUES(<%s>, '<%s>', '<%s>', TO_DATE('<%s>', 'yyyy-mm-dd'), <%s>, '<%s>', <%s>)";
    /*
    OrderId (Primary Key)
    BuyerName (string)
    BuyerEmail (string)
    OrderDate (Date)
    OrderTotalValue (numeric)
    Address (String)
    Postcode (int)
*/

    final String INSERT_TO_ORDER_ITEM_TABLE = "INSERT INTO ORDER_ITEM (OrderItemId, OrderId, SalePrice, ShippingPrice, TotalItemPrice, SKU, Status)" +
            "VALUES(<%s>, <%s>, <%s>, <%s>, <%s>, '<%s>', '<%s>')";

/*
    OrderItemId (Primary Key)
    OrderId (foreign key to order)
    SalePrice (numeric)
    ShippingPrice (numeric)
    TotalItemPrice (numeric)
    SKU (string)
    Status (enum)
*/

    private final String FIND_ORDER_ID = "SELECT OrderId FROM ORDER WHERE OrderId=<%s>";
    private final String FIND_ORDER_ITEM_ID = "SELECT OrderItemId FROM ORDER_ITEM WHERE OrderItemId=<%s>";

    public DB() {
        URL = System.getProperty("db_url");
        USER_NAME = System.getProperty("uname");
        USER_PWD = System.getProperty("pwd");
    }

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(URL, USER_NAME, USER_PWD);

        } catch (SQLException ex) {
    /*        LOGGER.info("SQLException: " + ex.getMessage());
            LOGGER.info("SQLState: " + ex.getSQLState());
            LOGGER.info("VendorError: " + ex.getErrorCode());*/
        } catch (IllegalAccessException e) {
            // LOGGER.info(e.getMessage());
        } catch (InstantiationException e) {
            // LOGGER.info(e.getMessage());
        } catch (ClassNotFoundException e) {
            // LOGGER.info(e.getMessage());
        }
        return connection;
    }

    String getOrderId(final String orderId) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(FIND_ORDER_ID, orderId));
        String retrievedOrderId = null;
        while (rs.next()) {
            retrievedOrderId = rs.getString("OrderId");
        }
        return retrievedOrderId;
    }

    String getOrderItemId(final String orderItemId) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(FIND_ORDER_ITEM_ID, orderItemId));
        String retrievedOrderItemId = null;
        while (rs.next()) {
            retrievedOrderItemId = rs.getString("OrderId");
        }
        return retrievedOrderItemId;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void dbInsertOperation(final InputFileLine input) throws SQLException {

        // if orderDate is empty, use today's date
        if (isEmpty(input.getOrderDate())) {
            input.setOrderDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }

       /* StringBuilder queryString = new StringBuilder();

        // input doesn't have an OrderTotalValue as per specification, where does it come from??
        queryString.append(String.format(INSERT_TO_ORDER_TABLE, input.getOrderId(), input.getBuyerName(), input.getBuyerEmail(), input.getOrderDate(),
                input.getOrderTotalValue(), input.getAddress(), input.getPostcode()));

        // input doesn't have an getTotalItemPrice as per specification, where does it come from?

        queryString.append(String.format(INSERT_TO_ORDER_ITEM_TABLE, input.getOrderItemId(), input.getOrderId(), input.getSalePrice(), input.getShippingPrice(),
                input.getTotalItemPrice(), input.getSKU(), input.getStatus()));

        Statement statement = connection.createStatement();
        statement.executeUpdate(queryString.toString());*/
    }
}
