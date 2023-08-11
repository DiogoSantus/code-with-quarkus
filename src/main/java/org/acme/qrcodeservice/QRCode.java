package org.acme.qrcodeservice;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QRCode {
    @JsonProperty("data")
    private String data;

    public QRCode() {
    }

    public QRCode(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
