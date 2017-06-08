package by.mercom.pstrade.printbonusticket.dataset;

/**
 * Implements an interface for working with the entity goods
 */

import by.mercom.pstrade.printbonusticket.dataset.interfaces.Document;
import by.mercom.pstrade.printbonusticket.dataset.interfaces.Good;
import by.mercom.pstrade.printbonusticket.dataset.interfaces.Price;


public class GoodImpl implements Good{
    private final Document doc;
    private final String goodName;
    private final Price price;
    private final String country;
    private final String packName;
    private final String barcode;
    private final float goodWeight;

    /**
     * Constructor
     * @param doc delivery document
     * @param goodName product name
     * @param originalPrice original price
     * @param country country fo origin
     * @param packageName packaging
     * @param goodWeight product weight in kg
     * @param barcode bar code of the goods
     */
    public GoodImpl(Document doc, String goodName, float originalPrice, String country,
                    String packageName, float goodWeight, String barcode) {
        this.doc = doc;
        this.goodName = goodName.trim();
        this.price = new PriceImpl(originalPrice);
        this.country = country.trim();
        this.packName = packageName.trim();
        this.goodWeight = goodWeight;
        this.barcode = barcode;
    }

    @Override public String getName() {
        return  goodName;}

    @Override public String getCountry() {return country;}

    @Override public String getBarcode() {return barcode;}

    @Override public Price getPrice() {return price;}

    @Override public Document getDoc(){return doc;}

    @Override public String getPackageName() { return packName;}

    @Override public float getGoodWeight(){ return goodWeight;}
}
