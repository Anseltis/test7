package com.example.test7;

import java.io.Serializable;

import org.apache.http.cookie.Cookie;

public class SerializedCookie implements Serializable {
	
	private static final long serialVersionUID = 5801674064619014020L;
	
	private String name;
    private String value;
    private String domain;
     
    public SerializedCookie(Cookie cookie){
        this.name = cookie.getName();
        this.value = cookie.getValue();
        this.domain = cookie.getDomain();
    }
     
    public String getName(){
        return name;
    }
     
    public String getValue(){
        return value;
    }
    public String getDomain(){
        return domain;
    }

}
