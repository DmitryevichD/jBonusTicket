package by.mercom.pstrade.printbonusticket.ui.models.interfaces;

import by.mercom.pstrade.printbonusticket.dataset.interfaces.Document;
import javafx.beans.property.BooleanProperty;


public interface DocumentProperty extends Document {
    BooleanProperty selectorProperty();
    Document getDoc();

}
