package sae3_01;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ClasseDialog extends Dialog<Classe> {
    private Classe classe;

    private TextField nomClasseField;
    private TextField packageField;
    private TextArea attributsField;
    private TextArea methodesField;

    public ClasseDialog(String type, Classe classe) {
        super();
        this.setTitle(type + " à créer");
        buildUI();
        setResultConverter(new Callback<ButtonType, Classe>() {
            @Override
            public Classe call(ButtonType b) {
                if (b.equals(ButtonType.OK)) {
                    String nomCourt = nomClasseField.getText().split(" ")[0];
                    ArrayList<String> attributs = new ArrayList<>(Arrays.asList(attributsField.getText().split("\n")));
                    ArrayList<String> methodes = new ArrayList<>(Arrays.asList(methodesField.getText().split("\n")));
                    return new Classe(
                            "<<"+type+">>",
                            nomCourt,
                            packageField.getText(),
                            attributs,
                            methodes,
                            new double[]{0,0},
                            Analyseur.getRelations(nomClasseField.getText(), attributs)
                            );
                }
                return null;
            }
        });
        this.classe = classe;
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

    private Pane createGridPane() {
        Label nomClasse = new Label("Nom : ");
        Label packageClasse = new Label("Package : ");
        Label attributs = new Label("Attributs : ");
        Label methodes = new Label("Methodes : ");
        this.nomClasseField = new TextField();
        this.packageField = new TextField();
        this.attributsField = new TextArea();
        this.methodesField = new TextArea();
        GridPane grid = new GridPane();
        grid.add(nomClasse, 0, 0);
        grid.add(packageClasse, 0, 1);
        grid.add(attributs, 0, 2);
        grid.add(methodes, 0, 3);
        grid.add(nomClasseField, 1, 0);
        grid.add(packageField, 1, 1);
        grid.add(attributsField, 1, 2);
        grid.add(methodesField, 1, 3);
        return grid;
    }
}