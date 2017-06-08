package by.mercom.pstrade.printbonusticket.dao.interfaces;

import by.mercom.pstrade.printbonusticket.dataset.interfaces.Document;
import by.mercom.pstrade.printbonusticket.dataset.interfaces.Good;

import java.sql.SQLException;
import java.util.List;

/**
 * The implementation of this interface allows you to use objects to obtain a list of goods according to specified conditions
 */

public interface GoodDAO {
    List<Good> getGoodBy(Document document) throws SQLException;
}
