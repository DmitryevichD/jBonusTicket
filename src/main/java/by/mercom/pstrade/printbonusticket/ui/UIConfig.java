package by.mercom.pstrade.printbonusticket.ui;

/**
 * Created by dm13y on 22.03.17.
 * Spring config for javafx
 */

import by.mercom.pstrade.printbonusticket.ui.fxControllers.MainScreenControllerImpl;
import by.mercom.pstrade.printbonusticket.ui.fxControllers.interfaces.MainScreenController;
import by.mercom.pstrade.printbonusticket.ui.fxContainer.AutowireFXMLScreen;
import javafx.stage.Stage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@Lazy

public class UIConfig {

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Bean
    MainScreenController mainScreenController(){
        return new MainScreenControllerImpl();
    }

    @Bean
    AutowireFXMLScreen mainScreen() {
        return new AutowireFXMLScreen(getClass().getResource("/gui/views/MainFormView.fxml"), primaryStage);
    }

}
