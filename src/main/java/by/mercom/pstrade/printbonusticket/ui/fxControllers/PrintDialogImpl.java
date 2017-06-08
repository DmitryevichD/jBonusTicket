package by.mercom.pstrade.printbonusticket.ui.fxControllers;

/**
 * Created by dm13y on 12.05.17.
 * Select printer
 */

import by.mercom.pstrade.printbonusticket.ui.fxControllers.interfaces.PrintDialog;
import javafx.print.Printer;
import javafx.scene.control.ChoiceDialog;

import java.util.Optional;

public class PrintDialogImpl implements PrintDialog {

    @Override
    public String getPrintName() {
        @SuppressWarnings("unchecked")
        ChoiceDialog printDialog = new ChoiceDialog(Printer.getDefaultPrinter(), Printer.getAllPrinters());
        printDialog.setHeaderText("Выбор принтера для печати ценников");
        printDialog.setTitle("Выбор принтера");
        printDialog.getDialogPane().setMaxWidth(300);
        @SuppressWarnings("unchecked")
        Optional<Printer> opt = printDialog.showAndWait();
        if (opt.isPresent()) {
            return opt.get().getName();
        }
        return null;
    }
}
