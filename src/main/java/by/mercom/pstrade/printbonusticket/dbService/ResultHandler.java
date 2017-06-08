package by.mercom.pstrade.printbonusticket.dbService;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * FunctionalInterface for handle resultSet in executed method
 */

@FunctionalInterface
public interface ResultHandler<T> {
    @SuppressWarnings("RedundantThrows")
    T handle(ResultSet resultSet) throws SQLException;
}
