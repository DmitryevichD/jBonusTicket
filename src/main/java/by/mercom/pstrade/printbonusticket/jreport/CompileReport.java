package by.mercom.pstrade.printbonusticket.jreport;

/**
 * Check reports file and compile it, if not found
 */

import com.google.common.collect.Table;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;

public class CompileReport extends Thread{
    private static final Logger logger = LoggerFactory.getLogger(CompileReport.class);
    private final Table<String, String, String> params;
    public CompileReport(Table<String, String, String> params) {
        super();
        super.setDaemon(true);
        this.params = params;
    }

    @Override
    public void run(){
        params.rowMap().forEach((s, stringStringMap) -> {
            String sourceFile = stringStringMap.get("sourceFile");
            String targetFile = stringStringMap.get("targetFile");
            logger.info("check report file: " + sourceFile);
            if (Files.exists(Paths.get(sourceFile))) {
                logger.info(sourceFile + ": ok");
                if(!Files.exists(Paths.get(targetFile))){
                    logger.info(targetFile + ": not found - compile");
                    try {
                        JasperCompileManager.compileReportToFile(sourceFile);
                    } catch (JRException e) {
                        logger.error(targetFile + " is not compile!", e);
                    }
                }
            }
        });
    }
}
