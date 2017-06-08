package by.mercom.pstrade.printbonusticket.dataset.interfaces;

import java.util.Date;

/**
 * The interface defines the necessary methods required for working with documents
 */
public interface Document {
    Integer getDocId();
    Date getDocDate();
    String getDocNumber();
    String getPartnerName();
    String getDocKindName();
    String toString();
}
