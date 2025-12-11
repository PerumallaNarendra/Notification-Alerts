package com.apex.trade.Notification_Alerts.trade_execution_alerts.mock;

import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MockData {

    List<UserData> userDataList;

    public MockData(){
        userDataList = new ArrayList<>();
    }

    public List<UserData> fetchUserData(){
        return List.of(
                new UserData("USR567", "usr567@gmail.com"),
                new UserData("USR123", "usr123@gmail.com"),
                new UserData("USR789", "usr789@gmail.com"),
                new UserData("USR001", "usr001@gmail.com"),
                new UserData("USR999", "usr999@gmail.com")
        );

    }
}
