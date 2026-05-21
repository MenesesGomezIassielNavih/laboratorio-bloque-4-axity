package com.axity.dinosaurpark.model;

import java.math.BigDecimal;

public class Ticket {

    private final int touristId;
    private final Tourist.Type type;
    private final BigDecimal pricePaid;

    public Ticket(int touristId, Tourist.Type type, BigDecimal pricePaid) {
        if (pricePaid == null || pricePaid.signum() < 0) {
            throw new IllegalArgumentException("pricePaid debe ser >= 0");
        }
        this.touristId = touristId;
        this.type = type;
        this.pricePaid = pricePaid;
    }

    public int getTouristId()       { return touristId; }
    public Tourist.Type getType()   { return type; }
    public BigDecimal getPricePaid(){ return pricePaid; }
}