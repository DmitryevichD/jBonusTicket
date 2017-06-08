package by.mercom.pstrade.printbonusticket.jreport;

/**
 * fill jasper report
 */

import by.mercom.pstrade.printbonusticket.config.Config;
import by.mercom.pstrade.printbonusticket.ui.models.interfaces.GoodProperty;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

public class ReportFactory {
    private static final Logger logger = LoggerFactory.getLogger(ReportFactory.class);

    @Autowired
    private Config config;

    private String getCurrentDateByTemplate(String tmpl){
        SimpleDateFormat sdf = new SimpleDateFormat(tmpl);
        return sdf.format(new Date());
    }

    private Map<String, Object> getParams() {
        Map<String, String> checkAndFeel = config.getJReportParams();
            String tmpl = checkAndFeel.get("dateTmpl");
        checkAndFeel.put("dateCreated", getCurrentDateByTemplate(tmpl));
        Map<String, Object> result = new HashMap<>();
        result.putAll(checkAndFeel);
        logger.info("jasper params - created");
        return result;
    }

    private JRDataSource getSource(List<GoodProperty> goods) {
        List<ReportGoodAdapter> reportGoodAdapters = new ArrayList<>();
        goods.forEach(good -> reportGoodAdapters.add(new ReportGoodAdapter(good, config.getPriceRub(), config.getPriceKop(), config.getLenghtForName())));
        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(reportGoodAdapters);
        logger.info("jasper data source - created");
        return jrBeanCollectionDataSource;
    }

    public JasperPrint makeReport(String fileNameReport, List<GoodProperty> goods) throws Exception{
        return JasperFillManager.fillReport(fileNameReport, getParams(), getSource(goods));
    }


    public void setConfig(Config config) {
        this.config = config;
    }
}
