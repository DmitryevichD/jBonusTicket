package by.mercom.pstrade.printbonusticket.ui.fxContainer;

import by.mercom.pstrade.printbonusticket.ui.fxControllers.interfaces.MainScreenController;
import by.mercom.pstrade.printbonusticket.ui.fxControllers.MainScreenControllerImpl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import javax.annotation.PostConstruct;
import java.net.URL;

/**
 * Created by dm13y on 22.03.17.
 * JavaFX Container

 * You can not just inject a constructor into this class?
 * This is a good idea, but it is only possible after the class is initialized.
 * For singletons, this works until you make the injection in the @ Post Constructor method. However,
 * when you switch to @Scope ("prototype"), this will immediately cause an error
 */

public class AutowireFXMLScreen extends Stage {

    @Autowired
    private ApplicationContext context;

    private Object controller;

    public AutowireFXMLScreen(){}

    public AutowireFXMLScreen(URL fxml, Window owner) {this(fxml, owner, StageStyle.DECORATED);}

    public AutowireFXMLScreen(URL fxml, Window owner, StageStyle style) {
        super(style);
        initOwner(owner);
        initModality(Modality.WINDOW_MODAL);
        FXMLLoader loader = new FXMLLoader(fxml);
        try {
            setScene(new Scene(loader.load()));
            controller = loader.getController();
            if (controller instanceof MainScreenController) {
                ((MainScreenControllerImpl) controller).setScreen(this);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    private void postConstruct(){
        /**
         * getAutowireCapableBeanFactory - Фабрика, осуществляющая автосвязывание при условии что компоненты уже создан
         *
         * autowireBeanProperties() - Autowire the bean properties of the given bean instance by name or type.
         * Can also be invoked with AUTOWIRE_NO in order to just apply after-instantiation callbacks (e.g. for annotation-driven injection).
         * controller - the existing bean instance
         * AUTOWIRE_NO - by name or type, using the constants in this interfaces
         * false - whether to perform a dependency check for object references in the bean instance
         */
        context.getAutowireCapableBeanFactory().autowireBeanProperties(controller, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
        //init base form config
        ((MainScreenController)controller).initConfig();
    }
}
