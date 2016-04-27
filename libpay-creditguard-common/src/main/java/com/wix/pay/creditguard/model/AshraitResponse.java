package com.wix.pay.creditguard.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ashrait")
public class AshraitResponse {
    @XmlElement
    public Response response;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AshraitResponse that = (AshraitResponse) o;

        return !(response != null ? !response.equals(that.response) : that.response != null);

    }

    @Override
    public int hashCode() {
        return response != null ? response.hashCode() : 0;
    }
}