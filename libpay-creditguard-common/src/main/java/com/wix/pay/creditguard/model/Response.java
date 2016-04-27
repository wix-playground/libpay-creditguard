package com.wix.pay.creditguard.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Response {
    /**
     * Request name for CG Gateway.
     * @see Commands
     */
    @XmlElement
    public String command;

    /**
     * ID of request, which is returned in the response.
     * requestId is limited to 20 characters.
     */
    @XmlElement
    public String requestId;

    /**
     * Requested date and time, e.g. "YYYY-MM-DD HH:MM
     */
    @XmlElement
    public String dateTime;

    /** ID of transaction. */
    @XmlElement
    public String tranId;

    /** Response result code. */
    @XmlElement
    public String result;

    /** Response text message. */
    @XmlElement
    public String message;

    /** Response text message for non technical personnel. */
    @XmlElement
    public String userMessage;

    /** Additional information if available, which can assist you with the returned response. */
    @XmlElement
    public String additionalInfo;

    /**
     * XML version.
     * @see Versions
     */
    @XmlElement
    public String version;

    /**
     * Language of "message" and "user message" fields - Hebrew/English.
     * @see Languages
     */
    @XmlElement
    public String language;

    @XmlElement
    public DoDealResponse doDeal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Response response = (Response) o;

        if (command != null ? !command.equals(response.command) : response.command != null) return false;
        if (requestId != null ? !requestId.equals(response.requestId) : response.requestId != null) return false;
        if (dateTime != null ? !dateTime.equals(response.dateTime) : response.dateTime != null) return false;
        if (tranId != null ? !tranId.equals(response.tranId) : response.tranId != null) return false;
        if (result != null ? !result.equals(response.result) : response.result != null) return false;
        if (message != null ? !message.equals(response.message) : response.message != null) return false;
        if (userMessage != null ? !userMessage.equals(response.userMessage) : response.userMessage != null) return false;
        if (additionalInfo != null ? !additionalInfo.equals(response.additionalInfo) : response.additionalInfo != null) return false;
        if (version != null ? !version.equals(response.version) : response.version != null) return false;
        if (language != null ? !language.equals(response.language) : response.language != null) return false;
        return !(doDeal != null ? !doDeal.equals(response.doDeal) : response.doDeal != null);

    }

    @Override
    public int hashCode() {
        int result1 = command != null ? command.hashCode() : 0;
        result1 = 31 * result1 + (requestId != null ? requestId.hashCode() : 0);
        result1 = 31 * result1 + (dateTime != null ? dateTime.hashCode() : 0);
        result1 = 31 * result1 + (tranId != null ? tranId.hashCode() : 0);
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        result1 = 31 * result1 + (message != null ? message.hashCode() : 0);
        result1 = 31 * result1 + (userMessage != null ? userMessage.hashCode() : 0);
        result1 = 31 * result1 + (additionalInfo != null ? additionalInfo.hashCode() : 0);
        result1 = 31 * result1 + (version != null ? version.hashCode() : 0);
        result1 = 31 * result1 + (language != null ? language.hashCode() : 0);
        result1 = 31 * result1 + (doDeal != null ? doDeal.hashCode() : 0);
        return result1;
    }
}