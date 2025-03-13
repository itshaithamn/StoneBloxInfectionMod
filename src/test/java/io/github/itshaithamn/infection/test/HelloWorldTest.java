package io.github.itshaithamn.infection.test;


import io.github.itshaithamn.infection.HelloWorld;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

public class HelloWorldTest {
    private ServerMock server;
    private HelloWorld helloWorld;

    @BeforeEach
    public void setUp() {
        server = MockBukkit.mock();
        helloWorld = new HelloWorld();
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void test() {
        assert(true);
    }
}