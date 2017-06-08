package by.mercom.pstrade.printbonusticket.dao;

/**
 * Implements a query to a database for retrieving documents according to specified conditions
 */

import by.mercom.pstrade.printbonusticket.config.Config;
import by.mercom.pstrade.printbonusticket.dao.interfaces.DocumentDAO;
import by.mercom.pstrade.printbonusticket.dataset.DocumentImpl;
import by.mercom.pstrade.printbonusticket.dataset.interfaces.Document;
import by.mercom.pstrade.printbonusticket.dbService.Executor;
import by.mercom.pstrade.printbonusticket.dbService.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public class DocumentDAOImpl implements DocumentDAO {
    private static final Logger logger = LoggerFactory.getLogger(DocumentDAOImpl.class);
    @Autowired @Lazy
    private Connection conn;

    @Autowired
    private Config config;

    @Override
    @SuppressWarnings("unchecked")
    public List<Document> getDocBy(Date startDate, Date endDate) throws SQLException {
        String query = "SELECT doc.id, doc.number, doc.series, doc.date, div.name as partner, doc.kind, kind.name as kind_name " +
                "FROM whDocument as doc " +
                "JOIN division as div " +
                "on div.id = doc.divisionId " +
                "JOIN whKindDocument as kind " +
                "on kind.id = doc.kind " +
                "WHERE " +
                "doc.date >= ? and doc.date < ? and doc.kind in (" + String.join(",", config.getDocKind()) +")";
        List<Map.Entry> list = new ArrayList<>();
        Map.Entry param1 = new AbstractMap.SimpleEntry(java.sql.Date.class, new java.sql.Date(startDate.getTime()));
        list.add(param1);
        Map.Entry param2 = new AbstractMap.SimpleEntry(java.sql.Date.class, new java.sql.Date(endDate.getTime()));
        list.add(param2);

        String datePattern = config.getDocDateFormat();

        ResultHandler<List<Document>> handler = (resultSet) -> {
            List<Document> result = new ArrayList<>();

            while (resultSet.next()) {
                Integer docId = resultSet.getInt("id");
                String docNumber = resultSet.getString("number");
                String docSeries = resultSet.getString("series");
                Date docDate = resultSet.getDate("date");
                String partnerName = resultSet.getString("partner");
                String docKindName = resultSet.getString("kind_name");
                Document document = new DocumentImpl(docId, docNumber, docSeries, docDate, partnerName, docKindName, datePattern);
                result.add(document);
            }
            return result;
        };

        Executor exec = new Executor();
        List<Document> DocumentList;
        try {
            DocumentList = exec.execQuery(conn, query, handler, list);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
        return DocumentList;
    }
}
