package by.mercom.pstrade.printbonusticket.dataset.interfaces;

/**
 * The interface defines the necessary methods required for working with price of goods
 */

public interface Price {
    /**
     * @return Set the price before discount
     */
    void setOriginalPrice(float originalPrice);

    /**
     * @return get bonus price
     */
    float getBonusPrice();

    /**
     * @return Set the bonus sum
     */
    void setBonusSum(float bonusSum);

    /**
     * @return Set the bonus percent
     */
    void setBonusPercent(float bonusPercent);

}
