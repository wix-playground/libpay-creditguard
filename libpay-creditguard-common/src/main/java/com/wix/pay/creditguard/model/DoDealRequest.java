package com.wix.pay.creditguard.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class DoDealRequest {
    /**
     * Terminal number.
     * This is the entity that holds the financial agreement between the merchant and the credit company.
     * The merchant might choose to work with more than one terminal number.
     */
    @XmlElement
    public String terminalNumber;

    /**
     * A card identification number provided by Credit Guard.
     * If cardId is provided, cardNo is not necessary.
     * Length is subject to terminal card id settings (16 or 36)
     *
     * The cardId is provided to customers that have purchased the Card ID module.
     * It is designed for customers that don't want to save credit card numbers in their systems.
     */
    @XmlElement
    public String cardId;

    /**
     * Track2 data – The magnetic field of the card (when the credit card is swiped).
     *
     * Mandatory when swiping card (transactionCode value should be Regular).
     * If sent, there is no need to send cardNo + cardExpiration
     */
    @XmlElement
    public String track2;

    /**
     * The card number (when the transaction is over the phone/Internet or the card could not be swiped).
     * The cardNo can be replaced by the cardId if working with Card Id module.
     * Mandatory when transaction is over phone/Internet.
     */
    @XmlElement
    public String cardNo;

    /**
     * Card expiration date (Month and year).
     * Mandatory if using card number or cardId
     */
    @XmlElement
    public String cardExpiration;

    /**
     * Three/four last digits on back of credit card.
     * @see Cvvs
     */
    @XmlElement
    public String cvv;

    /** Israeli ID number of card owner. */
    @XmlElement
    public String id;

    /** @see CreditTypes */
    @XmlElement
    public String creditType;

    /**
     * ISO currency code (according to supported currencies by the credit company).
     * @see <a href="https://en.wikipedia.org/wiki/ISO_4217">ISO 4217</a>
     * @see Currencies
     */
    @XmlElement
    public String currency;

    /** @see TransactionCodes */
    @XmlElement
    public String transactionCode;

    /** @see RequestTransactionTypes */
    @XmlElement
    public String transactionType;

    /** The total amount of the transaction in cents, Agorot, etc. */
    @XmlElement
    public Integer total;

    /** Israeli star amount in cents, Agorot, etc. */
    @XmlElement
    public Integer starTotal;

    /** @see Validations */
    @XmlElement
    public String validation;

    /** Authorization number that is returned from the credit card company when a transaction is authorized. */
    @XmlElement
    public String authNumber;

    /**
     * First payment amount in cents, Agorot, etc.
     * This field is mandatory when using creditType Payments.
     */
    @XmlElement
    public Integer firstPayment;

    /**
     * Periodical payment in cents, Agorot, etc.
     * This field is mandatory when using creditType Payments.
     */
    @XmlElement
    public Integer periodicalPayment;

    /**
     * Number of payments.
     * This field is mandatory when using creditType:
     *   Payments – The value will be the number of payments minus 1
     *   SpecialCredit – the value will be the total number of payments
     *   SpecialAlpha - the value will be the total number of payments
     */
    @XmlElement
    public Integer numberOfPayments;

    /** Kupa in Shva (3 digits with leading zeros)*/
    @XmlElement
    public String slaveTerminalNumber;

    /**
     * Field for any text (optional). This is returned in response as is.
     * Typically used for merchant unique identifier.
     * It is recommended toenter your unique identifier for the transaction in the merchant’s system.
     */
    @XmlElement
    public String user;

    /** Supplier number (MID) */
    @XmlElement
    public String supplierNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoDealRequest that = (DoDealRequest) o;

        if (terminalNumber != null ? !terminalNumber.equals(that.terminalNumber) : that.terminalNumber != null) return false;
        if (cardId != null ? !cardId.equals(that.cardId) : that.cardId != null) return false;
        if (track2 != null ? !track2.equals(that.track2) : that.track2 != null) return false;
        if (cardNo != null ? !cardNo.equals(that.cardNo) : that.cardNo != null) return false;
        if (cardExpiration != null ? !cardExpiration.equals(that.cardExpiration) : that.cardExpiration != null) return false;
        if (cvv != null ? !cvv.equals(that.cvv) : that.cvv != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (creditType != null ? !creditType.equals(that.creditType) : that.creditType != null) return false;
        if (currency != null ? !currency.equals(that.currency) : that.currency != null) return false;
        if (transactionCode != null ? !transactionCode.equals(that.transactionCode) : that.transactionCode != null) return false;
        if (transactionType != null ? !transactionType.equals(that.transactionType) : that.transactionType != null) return false;
        if (total != null ? !total.equals(that.total) : that.total != null) return false;
        if (starTotal != null ? !starTotal.equals(that.starTotal) : that.starTotal != null) return false;
        if (validation != null ? !validation.equals(that.validation) : that.validation != null) return false;
        if (authNumber != null ? !authNumber.equals(that.authNumber) : that.authNumber != null) return false;
        if (firstPayment != null ? !firstPayment.equals(that.firstPayment) : that.firstPayment != null) return false;
        if (periodicalPayment != null ? !periodicalPayment.equals(that.periodicalPayment) : that.periodicalPayment != null) return false;
        if (numberOfPayments != null ? !numberOfPayments.equals(that.numberOfPayments) : that.numberOfPayments != null) return false;
        if (slaveTerminalNumber != null ? !slaveTerminalNumber.equals(that.slaveTerminalNumber) : that.slaveTerminalNumber != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        return !(supplierNumber != null ? !supplierNumber.equals(that.supplierNumber) : that.supplierNumber != null);

    }

    @Override
    public int hashCode() {
        int result = terminalNumber != null ? terminalNumber.hashCode() : 0;
        result = 31 * result + (cardId != null ? cardId.hashCode() : 0);
        result = 31 * result + (track2 != null ? track2.hashCode() : 0);
        result = 31 * result + (cardNo != null ? cardNo.hashCode() : 0);
        result = 31 * result + (cardExpiration != null ? cardExpiration.hashCode() : 0);
        result = 31 * result + (cvv != null ? cvv.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (creditType != null ? creditType.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (transactionCode != null ? transactionCode.hashCode() : 0);
        result = 31 * result + (transactionType != null ? transactionType.hashCode() : 0);
        result = 31 * result + (total != null ? total.hashCode() : 0);
        result = 31 * result + (starTotal != null ? starTotal.hashCode() : 0);
        result = 31 * result + (validation != null ? validation.hashCode() : 0);
        result = 31 * result + (authNumber != null ? authNumber.hashCode() : 0);
        result = 31 * result + (firstPayment != null ? firstPayment.hashCode() : 0);
        result = 31 * result + (periodicalPayment != null ? periodicalPayment.hashCode() : 0);
        result = 31 * result + (numberOfPayments != null ? numberOfPayments.hashCode() : 0);
        result = 31 * result + (slaveTerminalNumber != null ? slaveTerminalNumber.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (supplierNumber != null ? supplierNumber.hashCode() : 0);
        return result;
    }
}