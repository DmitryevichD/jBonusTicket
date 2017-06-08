package by.mercom.pstrade.printbonusticket.ui.tasks;

/**
 * Created by dm13y on 05.04.17.
 * Task for getting goods from database by doc and return list doc in javaFX controller
 */

import by.mercom.pstrade.printbonusticket.dao.interfaces.GoodDAO;
import by.mercom.pstrade.printbonusticket.dataset.interfaces.Document;
import by.mercom.pstrade.printbonusticket.ui.models.GoodPropAdapter;
import by.mercom.pstrade.printbonusticket.ui.models.interfaces.GoodProperty;
import javafx.concurrent.Task;
import java.util.ArrayList;
import java.util.List;

public class FillGoodsViewByDocTask extends Task<List<GoodProperty>> {
    private final Document document;
    private final GoodDAO goodDAO;

    public FillGoodsViewByDocTask(Document document, GoodDAO goodDAO) {
        this.document = document;
        this.goodDAO = goodDAO;
    }

    @Override
    protected List<GoodProperty> call() throws Exception {
        List<GoodProperty> result = new ArrayList<>();
        goodDAO.getGoodBy(document).forEach(good -> result.add(new GoodPropAdapter(good)));
        return result;
    }
}
