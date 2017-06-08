package by.mercom.pstrade.printbonusticket.ui.models;

/**
 * Created by dm13y on 28.04.17.
 * Adapter for wrap document and add selector for document
 */

import by.mercom.pstrade.printbonusticket.dataset.interfaces.Document;
import by.mercom.pstrade.printbonusticket.ui.models.interfaces.DocumentProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.Date;

public class DocPropAdapter implements DocumentProperty {
    private final Document doc;
    private final BooleanProperty selector = new SimpleBooleanProperty();

    public DocPropAdapter(Document doc) {
        this.doc = doc;
    }

    @Override
    public BooleanProperty selectorProperty() {
        return selector;
    }

    @Override
    public Integer getDocId() {
        return doc.getDocId();
    }

    @Override
    public Date getDocDate() {
        return doc.getDocDate();
    }

    @Override
    public String getDocNumber() {
        return doc.getDocNumber();
    }

    @Override
    public String getPartnerName() {
        return doc.getPartnerName();
    }

    @Override
    public String getDocKindName() {
        return doc.getDocKindName();
    }

    @Override
    public String toString(){
        return getDocNumber() + " от " + getDocDate();
    }

    @Override
    public Document getDoc() {
        return doc;
    }

}
