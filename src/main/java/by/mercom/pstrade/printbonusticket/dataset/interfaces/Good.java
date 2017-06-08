package by.mercom.pstrade.printbonusticket.dataset.interfaces;

/**
 * The interface defines the necessary methods required for working with goods
 */

public interface Good {
    /**
     * @return String containing the name of the goods
     */
    String getName();

    /**
     * @return String containing the country in which the goods are manufactured
     */
    String getCountry();

    /**
     * @return String containing the barcode of the goods
     */
    String getBarcode();

    /**
     * The price characteristics of the goods
     * @return Class that implements the interface price
     */
    Price getPrice();

    /**
     * The document with which the goods were credited
     * @return Class that implements the interface Document
     */
    Document getDoc();

    /**
     * @return String containing the name of the package of goods
     */
    String getPackageName();

    /**
     * @return The goods weight
     */
    float getGoodWeight();

}
