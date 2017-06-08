package by.mercom.pstrade.printbonusticket.ui.fxControllers;

/**
 * Main controller
 */

import by.mercom.pstrade.printbonusticket.CalcBonus;
import by.mercom.pstrade.printbonusticket.config.Config;
import by.mercom.pstrade.printbonusticket.dao.interfaces.DocumentDAO;
import by.mercom.pstrade.printbonusticket.dao.interfaces.GoodDAO;
import by.mercom.pstrade.printbonusticket.dataset.interfaces.Document;
import by.mercom.pstrade.printbonusticket.dataset.interfaces.Good;
import by.mercom.pstrade.printbonusticket.jreport.Printing;
import by.mercom.pstrade.printbonusticket.jreport.ReportFactory;
import by.mercom.pstrade.printbonusticket.ui.converters.PriceInputConverter;
import by.mercom.pstrade.printbonusticket.ui.fxControllers.interfaces.MainScreenController;
import by.mercom.pstrade.printbonusticket.ui.fxControllers.interfaces.PrintDialog;
import by.mercom.pstrade.printbonusticket.ui.models.interfaces.DocumentProperty;
import by.mercom.pstrade.printbonusticket.ui.models.interfaces.GoodProperty;
import by.mercom.pstrade.printbonusticket.ui.tasks.FillDocsViewTask;
import by.mercom.pstrade.printbonusticket.ui.tasks.FillGoodsViewByDocTask;
import com.google.common.collect.Table;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import net.sf.jasperreports.engine.JasperPrint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@SuppressWarnings({"ALL", "unchecked"})
public class MainScreenControllerImpl implements MainScreenController {
    private static final Logger logger = LoggerFactory.getLogger(MainScreenControllerImpl.class);
    private final ConcurrentHashMap<Document, FillGoodsViewByDocTask> goodsTask = new ConcurrentHashMap<>();
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private Stage screen;

    @Autowired
    private Config config;
    @Autowired @Lazy
    private DocumentDAO documentDAO;
    @Autowired @Lazy
    private GoodDAO goodDAO;
    @Autowired @Lazy
    private ReportFactory reportFactory;

    @Override
    public void setScreen(Stage screen) {
        this.screen = screen;
    }

    @FXML
    private DatePicker dateFrom;
    @FXML
    private DatePicker dateTo;
    @FXML
    private Button getDoc;
    @FXML
    private HBox labelFormatPane;
    @FXML
    private Button btnPrintTicket;
    @FXML
    private TableView viewDocs;
    @FXML
    private TableView viewGoods;
    @FXML
    private ProgressBar loadGoodTaskBar;

    @SuppressWarnings("unused")
    @FXML
    private void initialize() {
        initDateControls();
    }

    public void initConfig() {
        initLabelFormats();
        initDocViewColumns();
        initGoodViewColumns();
        initButtonControls();
    }

    private void initButtonControls() {
        AnchorPane.setLeftAnchor(btnPrintTicket, 430 + labelFormatPane.getPrefWidth());
        getDoc.setOnAction(event -> fillViewDoc());
        btnPrintTicket.setOnAction(event -> printTickets());
    }

    private void showAlertMsg(Alert.AlertType typeMsg, String title, String header) {
        Alert alert = new Alert(typeMsg);
        alert.setHeaderText(header);
        alert.setTitle(title);
        alert.show();
    }

    private void printTickets() {
        List<GoodProperty> goods = new ArrayList<>();

        for (int i = 0; i < viewGoods.getItems().size(); i++) {
            GoodProperty goodProperty = (GoodProperty)viewGoods.getItems().get(i);
            if (goodProperty.selectorProperty().get()) {
                if(goodProperty.bonusPercentProperty().get() == 0) continue;
                int countTicket = goodProperty.printCountProperty().get();
                for (int j = 0; j < countTicket; j++) {
                    goods.add(goodProperty);
                }
            }
        }

        if (goods.isEmpty()) {
            showAlertMsg(Alert.AlertType.WARNING, "Ошибка печати",
                    "Проверьте: \n" +
                            "отмечен ли товар для печати;\n" +
                            "процент скидки товара должны быть положительный.");
            return;
        }

        List<String> reportFiles = new ArrayList<>();
        for(Node node : labelFormatPane.getChildren()){
            if (node instanceof CheckBox){
                if (((CheckBox) node).isSelected()){
                    String name = ((CheckBox) node).getText();
                    reportFiles.add(config.getReportFilesParams().get(name, "targetFile"));
                }
            }
        }

        if (reportFiles.isEmpty()) {
            showAlertMsg(Alert.AlertType.WARNING, "Ошибка печати", "Необходимо выбрать хотя бы один формат ценника");
            return;
        }

        String printName;
        PrintDialog printDialog = new PrintDialogImpl();
        printName = printDialog.getPrintName();
        if (printName == null) {
            return;
        }

        logger.info("make reports");
        List<JasperPrint> reports = new ArrayList<>();
        reportFiles.forEach(fileName -> {
            try {
                reports.add(reportFactory.makeReport(fileName, goods));
            } catch (Exception e) {
                logger.error("Abort created report " + fileName, e);
                return;
            }
        });

        new Printing(reports, printName).start();

    }


    private void fillViewDoc() {
        //clear the table view goods
        viewGoods.getItems().clear();
        //clear the table view docs
        viewDocs.getItems().clear();

        //cancel and delete current tasks
        goodsTask.values().forEach(FillGoodsViewByDocTask::cancel);
        goodsTask.clear();


        //creating a task for selecting documents and filling them with a table
        FillDocsViewTask fillDocsViewTask = new FillDocsViewTask(dateFrom, dateTo, documentDAO);
        fillDocsViewTask.setOnSucceeded(taskComplete -> {
            List<DocumentProperty> documents = fillDocsViewTask.getValue();

            //add changed listeners for each doc
            documents.forEach(doc -> doc.selectorProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    addGoodsToViewByDoc(doc.getDoc());
                } else {
                    remGoodsFromViewByDoc(doc.getDoc());
                }
            }));

            //noinspection unchecked
            viewDocs.getItems().setAll(documents);
        });

        //noinspection ThrowableResultOfMethodCallIgnored
        fillDocsViewTask.setOnFailed(taskException -> fillDocsViewTask.getException().printStackTrace());
        //start task
        Thread daemon = new Thread(fillDocsViewTask);
        daemon.setDaemon(true);
        daemon.start();
    }

    private void remGoodsFromViewByDoc(Document doc) {
        FillGoodsViewByDocTask currentTask = goodsTask.get(doc);
        if (currentTask != null) {
            currentTask.cancel();
            goodsTask.remove(doc);
        }
        List list = new ArrayList();
        viewGoods.getItems().forEach(item -> {
            if (((Good) item).getDoc() == doc) {
                list.add(item);
            }
        });
        viewGoods.getItems().removeAll(list);
        if(goodsTask.isEmpty()) {resetGoodTaskBar();}
    }

    private void addGoodsToViewByDoc(Document doc) {

        //Executed if there are not tasks in the selection of goods for the document
        if (goodsTask.get(doc) == null) {
            FillGoodsViewByDocTask fillGoodsViewByDocTask = new FillGoodsViewByDocTask(doc, goodDAO);
            goodsTask.put(doc, fillGoodsViewByDocTask);

            //binding progressbar
            loadGoodTaskBar.progressProperty().bind(fillGoodsViewByDocTask.progressProperty());

            //task was completed successfully
            fillGoodsViewByDocTask.setOnSucceeded(taskComplete -> {
                List<GoodProperty> goods = fillGoodsViewByDocTask.getValue();
                goods.forEach(good -> {
                    good.originalPriceProperty().addListener((observable, oldValue, newValue) -> {
                        if(newValue.doubleValue() < good.getPrice().getBonusPrice()) {
                            good.originalPriceProperty().set(good.getPrice().getBonusPrice());
                        }else{
                            //set sum across bonus price
                            good.bonusSumProperty().set(CalcBonus.bonusSumByPrice(good.getPrice().getBonusPrice(), newValue.floatValue()));
                            //set percent across bonus price
                            good.bonusPercentProperty().set(CalcBonus.bonusPercentByPrice(good.getPrice().getBonusPrice(), newValue.floatValue()));
                        }
                    });

                    good.bonusSumProperty().addListener((observable, oldValue, newValue) -> {
                        //set percent and price across price property
                        good.originalPriceProperty().set(CalcBonus.originalPriceBySum(good.getPrice().getBonusPrice(), newValue.floatValue()));
                    });

                    good.bonusPercentProperty().addListener((observable, oldValue, newValue) -> {
                        //set sum and price across price property
                        good.originalPriceProperty().set(CalcBonus.originalPriceByPercent(good.getPrice().getBonusPrice(), newValue.floatValue()));
                    });

                    viewGoods.getItems().add(good);
                });
                goodsTask.remove(doc);
                if(goodsTask.isEmpty()) {resetGoodTaskBar();}
            });

            fillGoodsViewByDocTask.setOnFailed(taskException -> {
                logger.error("Getting goods by doc error", fillGoodsViewByDocTask.getException());
                goodsTask.remove(doc);
                if(goodsTask.isEmpty()) {resetGoodTaskBar();}
            });

            //start task
            Thread thread = new Thread(fillGoodsViewByDocTask);
            thread.setDaemon(true);
            thread.start();
        }
    }

    private void resetGoodTaskBar() {
        loadGoodTaskBar.progressProperty().unbind();
        loadGoodTaskBar.setProgress(0);
    }

    private void initDocViewColumns() {
        Table<String, String, String> colParam = config.getDocColumnParams();
        colParam.rowMap().forEach((tag, mapParam) -> {
            String title = mapParam.get("title");
            String prefWidth = mapParam.get("prefWidth");
            TableColumn column = new TableColumn(title);
            column.setPrefWidth(Double.parseDouble(prefWidth));

            if (tag.equals("selector")) {
                column.setCellValueFactory(new PropertyValueFactory<DocumentProperty, Boolean>(tag));
                column.setCellFactory(cell -> new CheckBoxTableCell<>());
                column.setEditable(true);
            } else {
                column.setCellValueFactory(new PropertyValueFactory<DocumentProperty, String>(tag));
            }

            viewDocs.getColumns().add(column);
        });
    }

    private void initGoodViewColumns() {
        Table<String, String, String> colParam = config.getGoodsColumnParams();
        colParam.rowMap().forEach((tag, mapParam) -> {
            String title = mapParam.get("title");
            String prefWidth = mapParam.get("prefWidth");
            String typeName = mapParam.get("type");
            Boolean isEditable = Boolean.valueOf(mapParam.get("editable"));

            TableColumn column = new TableColumn(title);
            column.setPrefWidth(Double.parseDouble(prefWidth));

            if (typeName.equals("boolean")) {
                column.setCellValueFactory(new PropertyValueFactory<GoodProperty, Boolean>(tag));
                column.setCellFactory(columnType -> new CheckBoxTableCell<>());
            }

            if (typeName.equals("float")) {
                column.setCellFactory(columnType -> new TextFieldTableCell(new PriceInputConverter()));
                column.setCellValueFactory(new PropertyValueFactory<GoodProperty, Float>(tag));
            }

            if (typeName.equals("int")) {
                column.setCellFactory(columnType -> new TextFieldTableCell(new IntegerStringConverter(){
                    @Override
                    public Integer fromString(String value) {
                        try {
                            return Integer.parseInt(value);
                        } catch (Exception e) {
                            return 1;
                        }
                    }
                }));
                column.setCellValueFactory(new PropertyValueFactory<GoodProperty, Integer>(tag));
            }

            if (typeName.equals("String")) {
                column.setCellValueFactory(new PropertyValueFactory<GoodProperty, String>(tag));
            }

            column.setEditable(isEditable);
            viewGoods.getColumns().add(column);
        });
    }


    private void initLabelFormats() {
        //margin children node
        Insets margin = new Insets(8);

        labelFormatPane.getChildren().add(new Label("Формат:"));
        for (String label : config.getReportFilesParams().rowKeySet()) {
            labelFormatPane.getChildren().add(new CheckBox(label));
        }

        for (Node node : labelFormatPane.getChildren()) {
            //noinspection AccessStaticViaInstance
            labelFormatPane.setMargin(node, margin);
        }

        double formatPaneWidth = labelFormatPane.getChildren().size() * 82;
        labelFormatPane.setPrefWidth(formatPaneWidth);
    }

    private void initDateControls() {
        dateFrom.setValue(LocalDate.now());
        dateTo.setValue(LocalDate.now());
        dateFrom.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isAfter(dateTo.getValue())) dateFrom.setValue(oldValue);
        });

        dateFrom.setOnKeyPressed(keyPressed -> {
            if (keyPressed.getCode() == KeyCode.ENTER) fillViewDoc();
        });

        dateTo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isBefore(dateFrom.getValue())) dateTo.setValue(oldValue);
        });

        dateTo.setOnKeyPressed(keyPressed -> {
            if (keyPressed.getCode() == KeyCode.ENTER) fillViewDoc();
        });
    }

}

