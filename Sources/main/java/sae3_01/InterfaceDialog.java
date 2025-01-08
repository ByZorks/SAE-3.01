package sae3_01;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class InterfaceDialog extends Dialog<Classe> {
    private Classe classe;

    private TextField nomClasseField;
    private TextArea attributsField;
    private TextArea methodesField;

    public InterfaceDialog(String type) {
        super();
        this.setTitle(type + " à créer");
        buildUI();
        setPropertyBindings();
        setResultConverter();
    }

    private void buildUI() {
        Pane pane = createGridPane();
        getDialogPane().setContent(pane);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button ok = (Button) getDialogPane().lookupButton(ButtonType.OK);
        ok.setText("Valider");
        Button cancel = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancel.setText("Annuler");
    }

    private void setPropertyBindings() {
        // TODO
    }

    private void setResultConverter() {
        // TODO
    }

    private Pane createGridPane() {
        Label nomClasse = new Label("Nom : ");
        Label attributs = new Label("Attributs : ");
        Label methodes = new Label("Methodes : ");
        this.nomClasseField = new TextField();
        this.attributsField = new TextArea();
        this.methodesField = new TextArea();
        GridPane grid = new GridPane();
        grid.add(nomClasse, 0, 0);
        grid.add(attributs, 0, 1);
        grid.add(methodes, 0, 2);
        grid.add(nomClasseField, 1, 0);
        grid.add(attributsField, 1, 1);
        grid.add(methodesField, 1, 2);
        return grid;
    }
}
