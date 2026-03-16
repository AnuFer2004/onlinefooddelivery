package com._163.fooddeliverysystem.onlinefooddeleverysystem.model;

/**
 * Order represents a customer's food order.
 *
 * When a user clicks "Checkout" in the browser, the frontend sends a JSON
 * payload that matches the fields in this class. Spring automatically converts
 * that JSON into an Order object (this is called "deserialization").
 *
 * Orders are stored in "orders.txt" in this CSV format:
 *   orderId,customerName,orderDetails,totalPrice,status
 * Example:
 *   ORD-12345,john,Veg Burgerx2|Friesx1,0.0,Pending
 *
 * NOTE: Commas inside orderDetails are replaced with "|" to avoid breaking
 * the CSV format, and restored when reading back from the file.
 */
public class Order {

    private String orderId;       // e.g. "ORD-12345" — generated randomly in the browser
    private String customerName;  // username of who placed the order
    private String orderDetails;  // what was ordered, e.g. "Veg Burgerx2, Friesx1"
    private double totalPrice;    // total cost (currently always 0.0 — can be calculated server-side)
    private String status;        // "Pending", "Paid", "Canceled", etc.

    // Default no-arg constructor (required by Spring's JSON binding)
    public Order() {}

    // Full constructor
    public Order(String orderId, String customerName, String orderDetails,
                 double totalPrice, String status) {
        this.orderId      = orderId;
        this.customerName = customerName;
        this.orderDetails = orderDetails;
        this.totalPrice   = totalPrice;
        this.status       = status;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────────

    public String getOrderId()              { return orderId; }
    public void setOrderId(String v)        { this.orderId = v; }

    public String getCustomerName()         { return customerName; }
    public void setCustomerName(String v)   { this.customerName = v; }

    public String getOrderDetails()         { return orderDetails; }
    public void setOrderDetails(String v)   { this.orderDetails = v; }

    public double getTotalPrice()           { return totalPrice; }
    public void setTotalPrice(double v)     { this.totalPrice = v; }

    public String getStatus()               { return status; }
    public void setStatus(String v)         { this.status = v; }

    /**
     * Converts this order to a CSV line for saving to "orders.txt".
     *
     * Problem: orderDetails contains commas ("Burger x2, Fries x1"),
     *          which would break CSV parsing.
     * Solution: Replace commas inside orderDetails with "|" before saving,
     *           and restore them when reading back.
     */
    public String toDataString() {
        // Replace commas inside the details field with "|" so CSV stays intact
        String safeDetails = orderDetails == null ? "" : orderDetails.replace(",", "|");
        return String.join(",", orderId, customerName, safeDetails,
                String.valueOf(totalPrice), status);
    }

    /**
     * Parses a line from "orders.txt" back into an Order object.
     *
     * We use split(",", 5) with limit=5 to split into exactly 5 parts,
     * so the orderDetails field (which may be empty or contain "|") stays together.
     *
     * @param line  one line from orders.txt
     * @return      an Order object, or null if the line is invalid
     */
    public static Order fromDataString(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] tokens = line.split(",", 5);  // limit=5 keeps orderDetails as one piece
        if (tokens.length < 5) return null;
        try {
            // Restore "|" back to "," in the details field
            String details = tokens[2].replace("|", ",");
            return new Order(tokens[0], tokens[1], details,
                    Double.parseDouble(tokens[3]), tokens[4]);
        } catch (NumberFormatException e) {
            return null;  // skip lines with invalid price
        }
    }

    @Override
    public String toString() {
        return "Order{id='" + orderId + "', customer='" + customerName
                + "', status='" + status + "'}";
    }
}
