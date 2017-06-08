package by.mercom.pstrade.printbonusticket;

/**
 * Created by dm13y on 21.03.17.
 * Spring config
 */

import by.mercom.pstrade.printbonusticket.config.Config;
import by.mercom.pstrade.printbonusticket.dao.DocumentDAOImpl;
import by.mercom.pstrade.printbonusticket.dao.GoodsDAOImpl;
import by.mercom.pstrade.printbonusticket.dao.interfaces.DocumentDAO;
import by.mercom.pstrade.printbonusticket.dao.interfaces.GoodDAO;
import by.mercom.pstrade.printbonusticket.jreport.ReportFactory;
import by.mercom.pstrade.printbonusticket.ui.UIConfig;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

@Configuration
@Import(UIConfig.class)
public class AppConfig {
    @Bean
    Config config(){
        Config config = new Config();
        config.setPathToConfig("AppConfig.xml");
        config.initConfig();
        //check report
        return config;
    }

    @Bean @Lazy
    Connection connection() {
        try {
            DriverManager.setLoginTimeout(2);
            return DriverManager.getConnection(config().getConnUrl());
        } catch (SQLTimeoutException ex) {
            throw new BeanCreationException("connection", "Connection timeout is over", ex);
        } catch (SQLException ex){
            throw new BeanCreationException("connection", "Failed connection", ex);
        }
    }

    @Bean @Lazy
    DocumentDAO documentDAO(){
        return new DocumentDAOImpl();
    }

    @Bean @Lazy
    GoodDAO goodDAO() {
        return new GoodsDAOImpl();
    }

    @Bean @Lazy
    ReportFactory reportFactory(){
        return new ReportFactory();
    }
}
