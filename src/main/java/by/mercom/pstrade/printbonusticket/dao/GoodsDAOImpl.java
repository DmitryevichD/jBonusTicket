package by.mercom.pstrade.printbonusticket.dao;

/**
 * Created by dm13y on 06.02.17.
 * getting goods feature from the database
 */

import by.mercom.pstrade.printbonusticket.config.Config;
import by.mercom.pstrade.printbonusticket.dao.interfaces.GoodDAO;
import by.mercom.pstrade.printbonusticket.dataset.GoodImpl;
import by.mercom.pstrade.printbonusticket.dataset.interfaces.Document;
import by.mercom.pstrade.printbonusticket.dataset.interfaces.Good;
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
public class GoodsDAOImpl implements GoodDAO{
    private static final Logger logger = LoggerFactory.getLogger(GoodsDAOImpl.class);

    @Autowired @Lazy
    private Connection conn;

    @Autowired
    private Config config;

    private boolean showPricePerKg(int groupId, float weight) {
        return !config.getHideWeightGroup().contains(groupId) && (weight >= config.getMinWeightForShow()
                || config.getAlwaysShowWeightGroup().contains(groupId));
    }

    @Override
    public List<Good> getGoodBy(Document document) throws SQLException{
        String query =
                        "SELECT " +
                            "tmc.name as name, " +
                            "grp.id as groupId, " +
                            "tmc.weight as weight, " +
                            "docContent.pricePAll as originalPrice, " +
                            "country.name as country, " +
                            "(case docContent.barcode " +
                                "when 0 then tmc.weightCode " +
                                "else docContent.barcode " +
                                "end) as barcode, " +
                            "measure.name + measure.QPInit as packageName " +
                        "FROM " +
                            "WHDocumentContent as docContent " +
                        "JOIN tmc on docContent.nomenclId = tmc.id " +
                        "JOIN tmcbarcodes on tmc.id = tmcbarcodes.tmcId " +
                        "JOIN DivisionExt as div on tmc.producerId = div.id " +
                        "JOIN Country on country.id = div.CountryId " +
                        "JOIN Measure on tmc.MeasureId = Measure.id " +
                        "JOIN whNomenclature on whNomenclature.tmcId = tmc.id " +
                        "JOIN whNomenclatureGroup as grp on whNomenclature.whNomenclatureGroupId = grp.id " +
                        "WHERE WHDocumentId = ? and whNomenclature.WareHouseID = 1";

        List<Map.Entry> list = new ArrayList<>();
        @SuppressWarnings("unchecked")
        Map.Entry param1 = new AbstractMap.SimpleEntry(Integer.class, document.getDocId());
        list.add(param1);

        ResultHandler<List<Good>> handler = (resultSet) -> {
            List<Good> result = new ArrayList<>();

            while (resultSet.next()) {
                String goodName = resultSet.getString("name");
                Float originalPrice = resultSet.getFloat("originalPrice");
                String country = resultSet.getString("country");
                String packageName = resultSet.getString("packageName");
                Float weight = resultSet.getFloat("weight");
                String barcode = resultSet.getString("barcode");
                int groupId = resultSet.getInt("groupId");

                //check group weight
                if (!showPricePerKg(groupId, weight)) {
                    weight = 0f;
                }
                Good good = new GoodImpl(document, goodName, originalPrice, country, packageName, weight, barcode);
                result.add(good);
            }
            return result;
        };

        Executor exec = new Executor();
        List<Good> GoodsList;
        try {
            GoodsList = exec.execQuery(conn, query, handler, list);
        } catch (SQLException e) {
            logger.error("Error getting documents from the database", e);
            throw e;
        }
        return GoodsList;
    }
}
