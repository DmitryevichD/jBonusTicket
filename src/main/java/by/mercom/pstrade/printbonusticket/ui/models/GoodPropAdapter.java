package by.mercom.pstrade.printbonusticket.ui.models;

import by.mercom.pstrade.printbonusticket.dataset.interfaces.Document;
import by.mercom.pstrade.printbonusticket.dataset.interfaces.Good;
import by.mercom.pstrade.printbonusticket.dataset.interfaces.Price;
import by.mercom.pstrade.printbonusticket.ui.models.interfaces.GoodProperty;
import javafx.beans.property.*;

/**
 * Created by dm13y on 28.04.17.
 * Adapter for wrap good and add property for change price features
 */

public class GoodPropAdapter implements GoodProperty, Good{
    private final Good good;
    private final FloatProperty originalPrice;
    private final FloatProperty bonusSum = new SimpleFloatProperty();
    private final FloatProperty bonusPercent = new SimpleFloatProperty();
    private final IntegerProperty printCount = new SimpleIntegerProperty(1);
    private final BooleanProperty selector = new SimpleBooleanProperty();

    public GoodPropAdapter(Good good) {
        this.good = good;
        originalPrice = new SimpleFloatProperty(good.getPrice().getBonusPrice());

    }

    @Override public FloatProperty originalPriceProperty() {return originalPrice;}

    @Override public FloatProperty bonusSumProperty() {return bonusSum;}

    @Override public FloatProperty bonusPercentProperty() {return bonusPercent;}

    @Override public String getName() {return good.getName();}

    @Override public Good getGood() {return good;}

    @Override public String getCountry() {return good.getCountry();}

    @Override public String getBarcode() {return good.getBarcode();}

    @Override public Price getPrice() {return good.getPrice();}

    @Override public Document getDoc() {return good.getDoc();}

    @Override public String getPackageName() {return good.getPackageName();}

    public String getPartnerName(){return good.getDoc().getPartnerName();};

    public String getNumDoc(){return good.getDoc().getDocNumber();}

    @Override public float getGoodWeight() {return good.getGoodWeight();}

    @Override public IntegerProperty printCountProperty(){return printCount;}

    @Override public BooleanProperty selectorProperty() {return selector;}
}
