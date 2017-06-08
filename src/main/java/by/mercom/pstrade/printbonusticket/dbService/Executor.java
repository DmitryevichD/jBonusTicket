package by.mercom.pstrade.printbonusticket.dbService;


import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * Execute sql query
 */

@SuppressWarnings("unchecked")
public class Executor {

    public <T> T execQuery(Connection connection, String query, ResultHandler<T> handler, List<Map.Entry> params) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        for (int i = 0; i < params.size(); i++) {
            Map.Entry<Class<?>, Object> param = params.get(i);
            preparedStatement.setObject(i + 1, param.getKey().cast(param.getValue()));
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        T value = handler.handle(resultSet);
        resultSet.close();
        preparedStatement.close();
        return value;
    }
}
