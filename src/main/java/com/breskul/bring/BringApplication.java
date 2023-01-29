package com.breskul.bring;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpRequest;

/**
 * Bring Application class
 *
 * @author Artem Yankovets
 */
public class BringApplication {

  private static final Logger LOGGER = LoggerFactory.getLogger(BringApplication.class);

  /**
   * Bring project entry point
   *
   * @param args array of arguments
   */
  public static void main(String[] args) {
    LOGGER.info("Hello, from Bring team!");
  }
}
