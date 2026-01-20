package com.comulynx.wallet.rest.api.exception;

public class CustomerExistsException extends Exception{
    public CustomerExistsException(){
        super("Customer already Exists");
    }

}
