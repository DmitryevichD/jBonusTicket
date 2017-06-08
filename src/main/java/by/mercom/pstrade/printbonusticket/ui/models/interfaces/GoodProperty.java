package by.mercom.pstrade.printbonusticket.ui.models.interfaces;

import by.mercom.pstrade.printbonusticket.dataset.interfaces.Good;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;

/**
 * Created by dm13y on 28.04.17.
 * add property for use and change price in view good
 */

public interface GoodProperty extends Good {
    Good getGood();

    FloatProperty originalPriceProperty();

    FloatProperty bonusSumProperty();

    FloatProperty bonusPercentProperty();

    IntegerProperty printCountProperty();

    BooleanProperty selectorProperty();

    @SuppressWarnings("unused")
    default float getBonusPrice(){
        return getGood().getPrice().getBonusPrice();
    }

}
