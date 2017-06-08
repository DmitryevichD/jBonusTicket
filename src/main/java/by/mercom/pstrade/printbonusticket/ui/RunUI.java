package by.mercom.pstrade.printbonusticket.ui;

import by.mercom.pstrade.printbonusticket.AppConfig;
import by.mercom.pstrade.printbonusticket.config.Config;
import by.mercom.pstrade.printbonusticket.jreport.CompileReport;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class RunUI extends Application{
    private final static Logger logger = LoggerFactory.getLogger(RunUI.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        //check and compile report forms (if necessary)
        Config config = context.getBean(Config.class);

        new CompileReport(config.getReportFilesParams()).start();

        UIConfig UIConfig = context.getBean(UIConfig.class);
        UIConfig.setPrimaryStage(primaryStage);
        UIConfig.mainScreen().show();
    }

}
