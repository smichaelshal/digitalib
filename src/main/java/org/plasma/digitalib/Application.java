package org.plasma.digitalib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class Application {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Application.class);

        logger.error("An exception occurred!");
        logger.error("An exception occurred!", new Exception("Custom exception"));
        logger.error("{}, {}! An exception occurred!", "Hello", "World", new Exception("Custom exception"));
    }
}
