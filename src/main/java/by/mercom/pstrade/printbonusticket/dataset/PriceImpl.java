package by.mercom.pstrade.printbonusticket.dataset;

/**
 * Implements an interface for working with the entity price
 */

import by.mercom.pstrade.printbonusticket.dataset.interfaces.Price;

public class PriceImpl implements Price{
    private float originalPrice;
    private final float bonusPrice;
    private float bonusSum;
    private float bonusPercent;

    public PriceImpl(float bonusPrice) {this.bonusPrice = bonusPrice; this.originalPrice = bonusPrice;}

    @Override public void setOriginalPrice(float originalPrice) { this.originalPrice =  originalPrice; }

    @Override public float getBonusPrice() {return bonusPrice;}

    @Override public void setBonusSum(float bonusSum) {this.bonusSum = bonusSum;}

    @Override public void setBonusPercent(float bonusPercent) { this.bonusPercent = bonusPercent;}
}
