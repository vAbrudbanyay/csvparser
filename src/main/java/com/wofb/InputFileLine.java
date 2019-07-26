package com.wofb;

public class InputFileLine {

    private Integer lineNumber;
    private String OrderItemId;
    private String OrderId;
    private String BuyerName;
    private String BuyerEmail;
    private String Address;
    private String Postcode;
    private String SalePrice;
    private String ShippingPrice;
    private String SKU;
    private String Status;
    private String OrderDate; // may be empty

    public InputFileLine() {
    }

    public InputFileLine(Integer lineNumber, String orderItemId, String orderId, String buyerName, String buyerEmail, String address,
                         String postcode, String salePrice, String shippingPrice, String SKU, String status, String orderDate) {
        this.lineNumber = lineNumber;
        OrderItemId = orderItemId;
        OrderId = orderId;
        BuyerName = buyerName;
        BuyerEmail = buyerEmail;
        Address = address;
        Postcode = postcode;
        SalePrice = salePrice;
        ShippingPrice = shippingPrice;
        this.SKU = SKU;
        Status = status;
        OrderDate = orderDate;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setOrderItemId(String orderItemId) {
        OrderItemId = orderItemId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public void setBuyerName(String buyerName) {
        BuyerName = buyerName;
    }

    public void setBuyerEmail(String buyerEmail) {
        BuyerEmail = buyerEmail;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setPostcode(String postcode) {
        Postcode = postcode;
    }

    public void setSalePrice(String salePrice) {
        SalePrice = salePrice;
    }

    public void setShippingPrice(String shippingPrice) {
        ShippingPrice = shippingPrice;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public String getOrderItemId() {
        return OrderItemId;
    }

    public String getOrderId() {
        return OrderId;
    }

    public String getBuyerName() {
        return BuyerName;
    }

    public String getBuyerEmail() {
        return BuyerEmail;
    }

    public String getAddress() {
        return Address;
    }

    public String getPostcode() {
        return Postcode;
    }

    public String getSalePrice() {
        return SalePrice;
    }

    public String getShippingPrice() {
        return ShippingPrice;
    }

    public String getSKU() {
        return SKU;
    }

    public String getStatus() {
        return Status;
    }

    public String getOrderDate() {
        return OrderDate;
    }
}
