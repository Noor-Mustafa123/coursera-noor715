package de.soco.software.simuspace.suscore.common.util;

import org.junit.Assert;
import org.junit.Test;

import de.soco.software.simuspace.suscore.common.enums.Messages;

/**
 * This test Class is responsible to test the messages processed by MessageBundleFactory class
 *
 * @author Zeeshan jamal
 */
public class MessageBundleFactoryTest {

    /**
     * Test get message.
     *
     * @description Preparing message with parameters.
     */
    @Test
    public void testGetMessage() {
        final String message = "Object can not be null or empty.";
        Assert.assertTrue( message.equals( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), "Object" ) ) );
    }

}
