package com.wix.pay.creditguard.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class DoDealResponse {
    /**
     * Shva 3-letter status code.
     * @see com.wix.pay.direct.shva.model.StatusCodes
     */
    @XmlElement
    public String status;

    /** Extended status text. */
    @XmlElement
    public String statusText;

    /**
     * The source of the authorization number.
     * @see AuthSources
     */
    @XmlElement
    public String authSource;

    /**
     * The card acquirer.
     * @see CardAcquirers
     */
    @XmlElement
    public String cardAcquirer;

    /**
     * The card’s brand. amex and diners cards are also considered as private label.
     * @see CardBrands
     */
    @XmlElement
    public String cardBrand;

    /**
     * The card identifier.
     *
     * The cardId is provided to customers that have purchased the Card ID module.
     * cardId is returned when a card number transaction is performed and the Card ID module is present and configured
     * to produce card ids.
     */
    @XmlElement
    public String cardId;

    /**
     * The reason for communicating with ABS.
     * @see CommReasons
     */
    @XmlElement
    public String commReason;

    /**
     * The credit card company that issued the credit card.
     * The field value indicates the card issuer.
     *
     * The field attribute contains 2 digits:
     *  1. The first digit indicates the card issuer
     *  2. The second digit indicates the card sub-type
     *
     * @see CardAcquirers
     * @see CardSubTypes
     */
    @XmlElement
    public String creditCompany;

    /**
     * Informative field only. The status of the CVV.
     * Valid only if the terminal is configured to check CVV.
     * @see Statuses
     */
    @XmlElement
    public String cvvStatus;

    /**
     * Informative field only. The status of card owner’s Israeli ID number (only for Israeli card transaction code).
     * Valid only if the terminal is configured to check id.
     * @see Statuses
     */
    @XmlElement
    public String idStatus;

    /** Shva 96 protocol for settlement requests. */
    @XmlElement
    public String intIn;

    /** Shva 96 protocol for settlement responses. */
    @XmlElement
    public String intOt;

    /** The number of the credit card returned as sent in the request */
    @XmlElement
    public String cardNo;

    /** Returned as sent in the request */
    @XmlElement
    public Integer total;

    /** Returned as sent in the request. */
    @XmlElement
    public String starTotal;

    /** @see ResponseTransactionTypes */
    @XmlElement
    public String transactionType;

    /** Returned as sent in the request. */
    @XmlElement
    public String creditType;

    /** Returned as sent in the request. */
    @XmlElement
    public String currency;

    /** Returned as sent in the request. */
    @XmlElement
    public String transactionCode;

    /** Returned when a transaction is authorized. */
    @XmlElement
    public String authNumber;

    /** Returned as sent in the request. */
    @XmlElement
    public Integer firstPayment;

    /** Returned as sent in the request. */
    @XmlElement
    public Integer periodicalPayment;

    /** Returned as sent in the request. */
    @XmlElement
    public Integer numberOfPayments;

    /** Sudar in Shva. */
    @XmlElement
    public String slaveTerminalSequence;

    /** Kupa in Shva. */
    @XmlElement
    public String slaveTerminalNumber;

    /** Returned as sent in the request. */
    @XmlElement
    public String validation;

    /** Returned as sent in the request. */
    @XmlElement
    public String clubCode;

    /** Returned as sent in the request. */
    @XmlElement
    public String clubId;

    /** Returned as sent in the request. */
    @XmlElement
    public String cardExpiration;

    /** Returned as sent in the request. */
    @XmlElement
    public String user;

    /** Returned as sent in the request. */
    @XmlElement
    public String addonData;

    /** Extended Indication for the credit card identification. */
    @XmlElement
    public String cardType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoDealResponse that = (DoDealResponse) o;

        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (statusText != null ? !statusText.equals(that.statusText) : that.statusText != null) return false;
        if (authSource != null ? !authSource.equals(that.authSource) : that.authSource != null) return false;
        if (cardAcquirer != null ? !cardAcquirer.equals(that.cardAcquirer) : that.cardAcquirer != null) return false;
        if (cardBrand != null ? !cardBrand.equals(that.cardBrand) : that.cardBrand != null) return false;
        if (cardId != null ? !cardId.equals(that.cardId) : that.cardId != null) return false;
        if (commReason != null ? !commReason.equals(that.commReason) : that.commReason != null) return false;
        if (creditCompany != null ? !creditCompany.equals(that.creditCompany) : that.creditCompany != null) return false;
        if (cvvStatus != null ? !cvvStatus.equals(that.cvvStatus) : that.cvvStatus != null) return false;
        if (idStatus != null ? !idStatus.equals(that.idStatus) : that.idStatus != null) return false;
        if (intIn != null ? !intIn.equals(that.intIn) : that.intIn != null) return false;
        if (intOt != null ? !intOt.equals(that.intOt) : that.intOt != null) return false;
        if (cardNo != null ? !cardNo.equals(that.cardNo) : that.cardNo != null) return false;
        if (total != null ? !total.equals(that.total) : that.total != null) return false;
        if (starTotal != null ? !starTotal.equals(that.starTotal) : that.starTotal != null) return false;
        if (transactionType != null ? !transactionType.equals(that.transactionType) : that.transactionType != null) return false;
        if (creditType != null ? !creditType.equals(that.creditType) : that.creditType != null) return false;
        if (currency != null ? !currency.equals(that.currency) : that.currency != null) return false;
        if (transactionCode != null ? !transactionCode.equals(that.transactionCode) : that.transactionCode != null) return false;
        if (authNumber != null ? !authNumber.equals(that.authNumber) : that.authNumber != null) return false;
        if (firstPayment != null ? !firstPayment.equals(that.firstPayment) : that.firstPayment != null) return false;
        if (periodicalPayment != null ? !periodicalPayment.equals(that.periodicalPayment) : that.periodicalPayment != null) return false;
        if (numberOfPayments != null ? !numberOfPayments.equals(that.numberOfPayments) : that.numberOfPayments != null) return false;
        if (slaveTerminalSequence != null ? !slaveTerminalSequence.equals(that.slaveTerminalSequence) : that.slaveTerminalSequence != null) return false;
        if (slaveTerminalNumber != null ? !slaveTerminalNumber.equals(that.slaveTerminalNumber) : that.slaveTerminalNumber != null) return false;
        if (validation != null ? !validation.equals(that.validation) : that.validation != null) return false;
        if (clubCode != null ? !clubCode.equals(that.clubCode) : that.clubCode != null) return false;
        if (clubId != null ? !clubId.equals(that.clubId) : that.clubId != null) return false;
        if (cardExpiration != null ? !cardExpiration.equals(that.cardExpiration) : that.cardExpiration != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (addonData != null ? !addonData.equals(that.addonData) : that.addonData != null) return false;
        return !(cardType != null ? !cardType.equals(that.cardType) : that.cardType != null);

    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (statusText != null ? statusText.hashCode() : 0);
        result = 31 * result + (authSource != null ? authSource.hashCode() : 0);
        result = 31 * result + (cardAcquirer != null ? cardAcquirer.hashCode() : 0);
        result = 31 * result + (cardBrand != null ? cardBrand.hashCode() : 0);
        result = 31 * result + (cardId != null ? cardId.hashCode() : 0);
        result = 31 * result + (commReason != null ? commReason.hashCode() : 0);
        result = 31 * result + (creditCompany != null ? creditCompany.hashCode() : 0);
        result = 31 * result + (cvvStatus != null ? cvvStatus.hashCode() : 0);
        result = 31 * result + (idStatus != null ? idStatus.hashCode() : 0);
        result = 31 * result + (intIn != null ? intIn.hashCode() : 0);
        result = 31 * result + (intOt != null ? intOt.hashCode() : 0);
        result = 31 * result + (cardNo != null ? cardNo.hashCode() : 0);
        result = 31 * result + (total != null ? total.hashCode() : 0);
        result = 31 * result + (starTotal != null ? starTotal.hashCode() : 0);
        result = 31 * result + (transactionType != null ? transactionType.hashCode() : 0);
        result = 31 * result + (creditType != null ? creditType.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (transactionCode != null ? transactionCode.hashCode() : 0);
        result = 31 * result + (authNumber != null ? authNumber.hashCode() : 0);
        result = 31 * result + (firstPayment != null ? firstPayment.hashCode() : 0);
        result = 31 * result + (periodicalPayment != null ? periodicalPayment.hashCode() : 0);
        result = 31 * result + (numberOfPayments != null ? numberOfPayments.hashCode() : 0);
        result = 31 * result + (slaveTerminalSequence != null ? slaveTerminalSequence.hashCode() : 0);
        result = 31 * result + (slaveTerminalNumber != null ? slaveTerminalNumber.hashCode() : 0);
        result = 31 * result + (validation != null ? validation.hashCode() : 0);
        result = 31 * result + (clubCode != null ? clubCode.hashCode() : 0);
        result = 31 * result + (clubId != null ? clubId.hashCode() : 0);
        result = 31 * result + (cardExpiration != null ? cardExpiration.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (addonData != null ? addonData.hashCode() : 0);
        result = 31 * result + (cardType != null ? cardType.hashCode() : 0);
        return result;
    }
}