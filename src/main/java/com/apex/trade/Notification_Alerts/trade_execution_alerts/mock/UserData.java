package com.apex.trade.Notification_Alerts.trade_execution_alerts.mock;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter @Setter
@AllArgsConstructor
public class UserData {

    @Id
    private String userId;

    @Email @NotBlank
    private String email;
}
