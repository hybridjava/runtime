package com.HybridServerPages;

/**
 * Derive application state class named "AS.java" from this class
 */
public class BaseApplication {

protected BaseApplication(){}

/**
 * Override this method to process Application Level data. 
 * Called by the framework right after $handlePage
 * @param ps page handled 
 * @param h1 first (name) part (before ':') of HJ_Action HTTP parameter
 * @param h2 second (value) part of HJ_Action HTTP parameter <br>
 * Using the HJ_Action is optional as HTTP request is always available 
*/
protected void $handleAS(BasePage ps,String h1,String h2){}

}
