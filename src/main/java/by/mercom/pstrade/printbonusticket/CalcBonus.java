package by.mercom.pstrade.printbonusticket;

/**
 * Created by dm13y on 10.04.17.
 * Class make compute the bonus price characteristic
 */

public class CalcBonus {

    /**
     * Compute the bonus price by original price and bonus percent
     * @param bonusPercent bonus percent example (25, 60)
     * @return bonus price for good
     */
    public static float originalPriceByPercent(float bonusPrice, float bonusPercent) {
        if(bonusPercent < 0) return 0;
        float carlPercent = (100 - bonusPercent) / 100;
        return (bonusPrice / carlPercent);
    }

    /**
     * Compute the bonus price by original price and bonus sum
     * @param bonusPrice original price for good
     * @param bonusSum bonus sum for good
     * @return bonus price for good
     */
    public static float originalPriceBySum(float bonusPrice, float bonusSum) {
        if(bonusSum < 0) return 0;
        return bonusPrice + bonusSum;
    }

    /**
     * Compute the bonus percent by original price and bonus price
     * @param originalPrice original price for good
     * @param bonusPrice price with bonus
     * @return bonus percent for good
     */
    public static float bonusPercentByPrice(float bonusPrice, float originalPrice) {
        if(!(originalPrice > 0)) return 0;
        return 100 - (bonusPrice / originalPrice) * 100;
    }

    /**
     * Compute the bonus sum by original price and bonus price
     * @param originalPrice original price for good
     * @param bonusPrice price with bonus
     * @return bonus sum for good
     */
    public static float bonusSumByPrice(float bonusPrice, float originalPrice) {
        if(originalPrice > 0) return 0;
        return originalPrice - bonusPrice;
    }
}