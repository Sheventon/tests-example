package com.technokratos.tests_meet_up.unit;

import com.technokratos.tests_meet_up.service.SayStringService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class SayStringSomethingTest {

    private final SayStringService sayStringService = new SayStringService();

    @Test
    public void testSayHello() {
        Assertions.assertEquals("Hello World!", sayStringService.sayHello());
    }

    @Test
    public void testSaySomething() {
        String something = "London is the capital of Great Britain";
        Assertions.assertEquals(something, sayStringService.saySomething(something));
    }

    @ParameterizedTest
    @ValueSource(strings = {"London is the capital of Great Britain", "Cristiano better than Messi"})
    public void testSaySomethingParametrized(String something) {
        Assertions.assertEquals(something, sayStringService.saySomething(something));
    }

    @Test
    public void testSaySomethingNotEmpty() {
        String something = "London is the capital of Great Britain";
        Assertions.assertEquals(something, sayStringService.saySomethingNotEmpty(something));
    }

    @Test
    public void testSaySomethingEmpty() {
        String something = "";
        Assertions.assertThrows(IllegalArgumentException.class, () -> sayStringService.saySomethingNotEmpty(something));
    }
}
