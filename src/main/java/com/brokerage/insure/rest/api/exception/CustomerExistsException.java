package com.brokerage.insure.rest.api.exception;

public class CustomerExistsException extends Exception{
    public CustomerExistsException(){
        super("Customer already Exists");
    }

}
