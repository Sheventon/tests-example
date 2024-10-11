package com.technokratos.tests_meet_up.service;

public class SayStringService {

    public String sayHello() {
        return "Hello World!";
    }

    public String saySomething(String something) {
        return something;
    }

    public String saySomethingNotEmpty(String something) {
        if (!something.isEmpty()) {
            return something;
        } else {
            throw new IllegalArgumentException("Something is empty");
        }
    }


}
