package by.mercom.pstrade.printbonusticket.dataset;

/**
 * Implements an interface for working with the entity document
 */

import by.mercom.pstrade.printbonusticket.dataset.interfaces.Document;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DocumentImpl implements Document {
    private final Integer docId;
    private final String number;
    private final Date docDate;
    private final String partnerName;
    private final String docKindName;
    private final String dateFormat;

    public DocumentImpl(Integer docId, String docNumber, String docSeries, Date docDate, String partnerName, String docKindName, String dateFormat) {
        this.docId = docId;
        this.number = docSeries + "_" + docNumber;
        this.docDate = docDate;
        this.partnerName = partnerName;
        this.docKindName = docKindName;
        this.dateFormat = dateFormat;
    }

    @Override
    public Integer getDocId() {
        return docId;
    }

    @Override
    public Date getDocDate() {
        return docDate;
    }

    @Override
    public String getDocNumber() {
        return number;
    }

    @Override
    public String getPartnerName() {
        return partnerName;
    }

    @Override
    public String getDocKindName() {
        return docKindName;
    }

    private String formatDate(){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(getDocDate());
    }

    @Override
    public String toString(){
        return getPartnerName() + ". Док: " + getDocNumber() + " от " + formatDate();
    }
}
