package com.kevin.grab.ip.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class IPInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String address;
    private String port;
    private String type;
    private String speed;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("address", address).append("port", port).append("type",type).append("speed", speed).toString();
    }
}
