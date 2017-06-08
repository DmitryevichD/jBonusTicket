package by.mercom.pstrade.printbonusticket.jreport;

/**
 * print report on A4 page format
 */

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;
import java.util.ArrayList;
import java.util.List;

public class Printing extends Thread{
    private static final Logger logger = LoggerFactory.getLogger(Printing.class);
    private final List<JasperPrint> reports;
    private final String printerName;
    public Printing(List<JasperPrint> reports, String printerName) {
        this.reports = reports;
        this.printerName = printerName;
    }
    @Override
    public void run(){
        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        printRequestAttributeSet.add(MediaSizeName.ISO_A4);

        PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
        printServiceAttributeSet.add(new PrinterName(printerName, null));

        SimplePrintServiceExporterConfiguration printConfig = new SimplePrintServiceExporterConfiguration();
        printConfig.setPrintRequestAttributeSet(printRequestAttributeSet);
        printConfig.setPrintServiceAttributeSet(printServiceAttributeSet);
        printConfig.setDisplayPageDialog(false);
        printConfig.setDisplayPrintDialog(false);

        JRPrintServiceExporter exporter = new JRPrintServiceExporter();
        exporter.setConfiguration(printConfig);

        for (JasperPrint report : reports) {
            exporter.setExporterInput(new SimpleExporterInput(report));
            try {
                exporter.exportReport();
            } catch (JRException ex) {
                logger.error("print error", ex);
            }
        }

    }
}
