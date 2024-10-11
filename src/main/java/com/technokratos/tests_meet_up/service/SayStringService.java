package com.technokratos.tests_meet_up.service;

public class SayStringService {

    // 1 сценарий: передали строку
    public String sayHello() {
        return "Hello World!";
    }

    // 1 сценарий: передали строку
    public String saySomething(String something) {
        return something;
    }

    // 2 сценария: передали непустую строку и пустую
    public String saySomethingNotEmpty(String something) {
        if (!something.isEmpty()) {
            return something;
        } else {
            throw new IllegalArgumentException("Something is empty");
        }
    }


}
