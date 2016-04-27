package com.wix.pay.creditguard.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ashrait")
public class AshraitRequest {
    @XmlElement
    public Request request;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AshraitRequest that = (AshraitRequest) o;

        return !(request != null ? !request.equals(that.request) : that.request != null);

    }

    @Override
    public int hashCode() {
        return request != null ? request.hashCode() : 0;
    }
}