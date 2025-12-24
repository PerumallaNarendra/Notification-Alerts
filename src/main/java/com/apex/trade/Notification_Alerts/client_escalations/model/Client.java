package com.apex.trade.Notification_Alerts.client_escalations.model;

public class Client {
    private Long id;
    private String name;
    private String contact;
    private String email;

    public Client(Long id, String name, String contact, String email) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.email=email;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getEmail() { return email; }
}
