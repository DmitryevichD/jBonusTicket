package by.mercom.pstrade.printbonusticket.ui.tasks;

/**
 * Created by dm13y on 03.04.17.
 * Task for getting docs from database by dates and return list doc in javaFX controller
 */

import by.mercom.pstrade.printbonusticket.dao.interfaces.DocumentDAO;
import by.mercom.pstrade.printbonusticket.ui.models.DocPropAdapter;
import by.mercom.pstrade.printbonusticket.ui.models.interfaces.DocumentProperty;
import javafx.concurrent.Task;
import javafx.scene.control.DatePicker;

import java.sql.SQLData;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FillDocsViewTask extends Task<List<DocumentProperty>>{
    private final Date dateFrom;
    private final Date dateTo;
    private final DocumentDAO documentDAO;

    /**
     * Converts the date specified from DatePicker to Date,
     * which will be used to select documents in the database
     * @param datePicker date selector in GUI
     * @return java.util.Date
     */
    private Date getDate(DatePicker datePicker, int plusDays) {
        LocalDate localDate = datePicker.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()).plusDays(plusDays));
        return Date.from(instant);
    }

    /**
     * Create task for getting docs
     * @param dateFrom starting date docs from GUI DatePicker
     * @param dateTo ending date docs from GUI DatePicker
     * @param documentDAO DAO for executed query in DB
     */
    public FillDocsViewTask(DatePicker dateFrom, DatePicker dateTo, DocumentDAO documentDAO) {
        this.dateFrom = getDate(dateFrom, 0);
        this.dateTo = getDate(dateTo, 1);
        this.documentDAO = documentDAO;
    }

    @Override
    protected List<DocumentProperty> call() throws Exception {
        List<DocumentProperty> docProp = new ArrayList<>();
        documentDAO.getDocBy(dateFrom, dateTo).forEach(document -> docProp.add(new DocPropAdapter(document)));
        return docProp;
    }
}
