package by.mercom.pstrade.printbonusticket.jreport;

/**
 * Class for transfer params and fields to report
 */

import by.mercom.pstrade.printbonusticket.dataset.interfaces.Document;
import by.mercom.pstrade.printbonusticket.dataset.interfaces.Good;
import by.mercom.pstrade.printbonusticket.dataset.interfaces.Price;
import by.mercom.pstrade.printbonusticket.ui.models.interfaces.GoodProperty;

import java.text.DecimalFormatSymbols;

@SuppressWarnings("unused")
public class ReportGoodAdapter implements Good {

    private final GoodProperty good;
    private final String rub;
    private final String kop;
    private final int nameLength;
    String separator = String.valueOf(new DecimalFormatSymbols().getDecimalSeparator());
    public ReportGoodAdapter(GoodProperty good, String rub, String kop, int nameLength){
        this.good = good;
        this.rub = rub;
        this.kop = kop;
        this.nameLength = nameLength;
    }

    @Override
    public String getName() {
        String name;
        if (good.getName().length() > nameLength){
            name = good.getName() + ", " + getPackageName();
        }else{
            name = good.getName() + "\n" + getPackageName();
        }
        return name;
    }

    @Override
    public String getCountry() {
        return good.getCountry();
    }

    @Override
    public String getBarcode() {
        int length = good.getBarcode().length();
        if (length == 13) {
            return good.getBarcode();
        }else {
            return String.format("%1$13s", good.getBarcode()).replace(" ", "0");
        }

    }

    @Override
    public Price getPrice() {
        return good.getPrice();
    }

    @Override
    public Document getDoc() {
        return good.getDoc();
    }

    @Override
    public String getPackageName() {
        return good.getPackageName();
    }

    @Override
    public float getGoodWeight() {
        return good.getGoodWeight();
    }

    private String formatPrice(float price) {
        return String.format("%.2f", price).replace(separator, rub) + kop;
    }

    public String getOriginalPrice(){
        return formatPrice(good.originalPriceProperty().get());
    }

    public String getBonusPercent(){
        return formatPrice(good.bonusPercentProperty().get());
    }

    public String getBonusPrice(){
        return formatPrice(good.getPrice().getBonusPrice());
    }

    public String getBonusSum(){
        return formatPrice(good.bonusSumProperty().get());
    }

    public float getPricePerOneKg(){
        if (good.getGoodWeight() > 0) {
            return good.getPrice().getBonusPrice() / good.getGoodWeight();
        } else {
            return 0;
        }
    }

    public String getPartnerName(){
        return getDoc().getPartnerName();
    }

    public String getStrPricePerOneKg() {
        if (getPricePerOneKg() != 0) {
            return formatPrice(getPricePerOneKg());
        }else {
            return "";
        }

    }

    public String getDocument(){

        return good.getDoc().toString();
    }
}
