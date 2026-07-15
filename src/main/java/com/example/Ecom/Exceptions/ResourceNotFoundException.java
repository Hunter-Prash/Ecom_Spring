package com.example.Ecom.Exceptions;

public class ResourceNotFoundException extends RuntimeException{

    //The constructor runs only once, when the object is created.
    public ResourceNotFoundException(String message){
        super(message);//call the parent
    }
}
