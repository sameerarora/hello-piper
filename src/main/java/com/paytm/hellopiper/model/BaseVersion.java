package com.paytm.hellopiper.model;

public class BaseVersion {

    private String version;

    private String applicationName;

    public BaseVersion() {
    }

    public BaseVersion(String version, String applicationName) {
        this.version = version;
        this.applicationName = applicationName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
