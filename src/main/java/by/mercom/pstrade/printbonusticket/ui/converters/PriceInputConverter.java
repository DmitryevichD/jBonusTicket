package by.mercom.pstrade.printbonusticket.ui.converters;


import javafx.util.converter.FloatStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PriceInputConverter extends FloatStringConverter {
    private static final Logger logger = LoggerFactory.getLogger(PriceInputConverter.class);

    @Override
    public Float fromString(String value) {
        try {
            return Float.parseFloat(value.replace(',', '.'));
        } catch (Exception e) {
            logger.error("Error parse " + value, e);
            return -1f;
        }
    }

    @Override
    public String toString(Float value) {
        return String.format("%.2f", value);
    }
}
