package com.fabricio.onlinelibrary.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Book {
    @JsonProperty("code")
    private String code;  

    @JsonProperty("name")
    private String name;

    @JsonProperty("booked")
    private int bookedInt;  
 
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
 
    public boolean isBooked() { return bookedInt == 1; }
    public void setBooked(boolean booked) { this.bookedInt = booked ? 1 : 0; }
}
