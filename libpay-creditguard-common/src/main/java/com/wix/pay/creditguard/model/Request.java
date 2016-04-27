package com.wix.pay.creditguard.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Request {
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

    /**
     * For transaction resent in case of transaction timeout.
     * This Option is available only when installed.
     * If <mayBeDuplicate> is true (value 1), CG Gateway checks whether the transaction has already been made and if all
     * the details of the request are identical to the existing request. An error is returned for invalid requests; for
     * identical requests, CG Gateway checks the completion status of the existing request. If the request is complete,
     * the response is sent again. If the request is incomplete, the system completes the transaction and returns the
     * response to the user.
     *
     * @see MayBeDuplicates
     */
    @XmlElement
    public String mayBeDuplicate;

    @XmlElement
    public DoDealRequest doDeal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        if (command != null ? !command.equals(request.command) : request.command != null) return false;
        if (requestId != null ? !requestId.equals(request.requestId) : request.requestId != null) return false;
        if (dateTime != null ? !dateTime.equals(request.dateTime) : request.dateTime != null) return false;
        if (version != null ? !version.equals(request.version) : request.version != null) return false;
        if (language != null ? !language.equals(request.language) : request.language != null) return false;
        if (mayBeDuplicate != null ? !mayBeDuplicate.equals(request.mayBeDuplicate) : request.mayBeDuplicate != null) return false;
        return !(doDeal != null ? !doDeal.equals(request.doDeal) : request.doDeal != null);

    }

    @Override
    public int hashCode() {
        int result = command != null ? command.hashCode() : 0;
        result = 31 * result + (requestId != null ? requestId.hashCode() : 0);
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (mayBeDuplicate != null ? mayBeDuplicate.hashCode() : 0);
        result = 31 * result + (doDeal != null ? doDeal.hashCode() : 0);
        return result;
    }
}