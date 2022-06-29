package com.rudy.ryanto.order.system.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {
    private final BigDecimal ammount;

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money(BigDecimal ammount) {
        this.ammount = ammount;
    }

    public BigDecimal getAmmount() {
        return ammount;
    }

    public boolean isGreaterThanZero(){
        return this.ammount!=null && this.ammount.compareTo(BigDecimal.ZERO)>0;
    }

    public boolean isGreaterThan(Money money){
        return this.ammount!=null && this.ammount.compareTo(money.getAmmount())>0;
    }

    public Money add(Money money){
        return new Money(setScale(this.ammount.add(money.getAmmount())));
    }

    public Money subStract(Money money){
        return new Money(setScale(this.ammount.subtract(money.getAmmount())));
    }

    public Money multiply(int multiplier){
        return new Money(setScale(this.ammount.multiply(new BigDecimal(multiplier))));
    }

    public BigDecimal setScale(BigDecimal input){
        return input.setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return ammount.equals(money.ammount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ammount);
    }
}
