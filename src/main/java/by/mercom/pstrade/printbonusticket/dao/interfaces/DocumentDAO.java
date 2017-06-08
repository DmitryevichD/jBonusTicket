package by.mercom.pstrade.printbonusticket.dao.interfaces;

import by.mercom.pstrade.printbonusticket.dataset.interfaces.Document;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * The implementation of this interface allows you to use objects to obtain
 * a list of documents according to specified conditions
 */
public interface DocumentDAO {
    List<Document> getDocBy(Date startDate, Date endDate) throws SQLException;
}
