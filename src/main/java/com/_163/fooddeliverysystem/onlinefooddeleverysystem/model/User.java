package com._163.fooddeliverysystem.onlinefooddeleverysystem.model;

/**
 * User is the base class for all user accounts in the system.
 *
 * User accounts data is stored in "users.txt" in this CSV format:
 *   userId,username,email,password,address
 * Example:
 *   U-1001,john,john@email.com,pass123,123 Main St
 *
 * NOTE: Passwords are stored as plain text here (no encryption).
 * In a real production app you would hash passwords using BCrypt.
 */
public class User {

    private String userId;    // unique ID, e.g. "U-1001" (auto-generated on register)
    private String username;  // login name, must be unique
    private String email;     // email address
    private String password;  // plain text password (for demo purposes)
    private String address;   // delivery address (also used for phone number)

    // Default no-arg constructor (required by Spring's JSON mapping)
    public User() {}

    // Full constructor used when loading from file or creating programmatically
    public User(String userId, String username, String email, String password, String address) {
        this.userId   = userId;
        this.username = username;
        this.email    = email;
        this.password = password;
        this.address  = address;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────────

    public String getUserId()              { return userId; }
    public void setUserId(String userId)   { this.userId = userId; }

    public String getUsername()            { return username; }
    public void setUsername(String u)      { this.username = u; }

    public String getEmail()               { return email; }
    public void setEmail(String email)     { this.email = email; }

    public String getPassword()            { return password; }
    public void setPassword(String p)      { this.password = p; }

    public String getAddress()             { return address; }
    public void setAddress(String address) { this.address = address; }

    /**
     * Checks whether the provided username + password match this user's credentials.
     * Called during login to verify the user is who they say they are.
     *
     * @param username  the username typed by the user on the login form
     * @param password  the password typed by the user on the login form
     * @return          true if both match, false otherwise
     */
    public boolean authenticate(String username, String password) {
        return this.username != null && this.username.equals(username)
            && this.password != null && this.password.equals(password);
    }

    /**
     * Converts this user to a CSV line for saving to "users.txt".
     * Format: "userId,username,email,password,address"
     */
    public String toDataString() {
        return String.join(",", userId, username, email, password, address);
    }

    /**
     * Parses a CSV line from "users.txt" back into a User object.
     * Returns null if the line is malformed or has fewer than 5 fields.
     *
     * @param line  a single line from users.txt
     */
    public static User fromDataString(String line) {
        String[] tokens = line.split(",");
        if (tokens.length < 5) return null;
        return new User(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4]);
    }

    @Override
    public String toString() {
        return "User{userId='" + userId + "', username='" + username + "', email='" + email + "'}";
    }
}
