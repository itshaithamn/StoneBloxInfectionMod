package io.github.itshaithamn.infection.test;


import io.github.itshaithamn.infection.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HelloWorldTest {
    private ServerMock server;
    private Main plugin;

    @BeforeEach
    void setUp() {
        // Initialize MockBukkit and load the plugin
        server = MockBukkit.mock();
        plugin = (Main) MockBukkit.load(Main.class);
    }

    @AfterEach
    void tearDown() {
        // Clean up MockBukkit after each test
        MockBukkit.unmock();
    }

    @Test
    void testPluginLoads() {
        // Verify the plugin is loaded and enabled
        assertNotNull(plugin, "Plugin should not be null");
        assertTrue(plugin.isEnabled(), "Plugin should be enabled");
    }
}