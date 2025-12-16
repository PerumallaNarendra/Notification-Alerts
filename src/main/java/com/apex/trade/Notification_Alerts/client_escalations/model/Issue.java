package com.apex.trade.Notification_Alerts.client_escalations.model;

public class Issue {
    public enum Priority { LOW, MEDIUM, HIGH, CRITICAL }
    public enum Status { OPEN, ESCALATED, RESOLVED }

    private Long id;
    private Client client;
    private String description;
    private Priority priority;
    private Status status;
    private ClientServicesRep assignedRep;

    public Issue(Long id, Client client, String description, Priority priority) {
        this.id = id;
        this.client = client;
        this.description = description;
        this.priority = priority;
        this.status = Status.OPEN;
    }

    public Long getId() { return id; }
    public Client getClient() { return client; }
    public String getDescription() { return description; }
    public Priority getPriority() { return priority; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public ClientServicesRep getAssignedRep() { return assignedRep; }
    public void setAssignedRep(ClientServicesRep assignedRep) { this.assignedRep = assignedRep; }
}