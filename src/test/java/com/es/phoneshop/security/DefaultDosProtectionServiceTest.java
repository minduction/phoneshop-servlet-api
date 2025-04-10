package com.es.phoneshop.security;

import org.junit.Assert;
import org.junit.Test;

import java.util.stream.IntStream;

public class DefaultDosProtectionServiceTest {
    private static final int THRESHOLD = 20;
    private static final String TEST_VALID_IP = "127.0.0.1";
    private static final String TEST_INVALID_IP = "127.0.0.2";
    private final DosProtectionService dosProtectionService = DefaultDosProtectionService.getInstance();

    @Test
    public void testIsAllowedAccepts() {
        Assert.assertTrue(dosProtectionService.isAllowed(TEST_VALID_IP));
    }

    @Test
    public void testIsAllowedRejects() {
        IntStream.range(0, THRESHOLD).forEach(i -> dosProtectionService.isAllowed(TEST_INVALID_IP));
        Assert.assertFalse(dosProtectionService.isAllowed(TEST_INVALID_IP));
    }
}
