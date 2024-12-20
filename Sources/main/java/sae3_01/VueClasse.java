package sae3_01;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class VueClasse extends VBox implements Observateur {

    private Classe classe;
    private Label nom;
    private VBox header;
    private VBox attributs;
    private VBox methodes;
    private Line separation1;
    private Line separation2;
    private double x;
    private double y;

    public VueClasse() {
        this.setPrefSize(200, 200);
        this.setStyle("-fx-background-color: yellow; -fx-border-color: black;");
        this.nom = new Label();
        this.header = new VBox();
        this.header.setAlignment(Pos.CENTER);
        this.attributs = new VBox();
        this.methodes = new VBox();
        this.separation1 = new Line(0, 0, 400, 0);
        this.separation1.setStyle("-fx-stroke: black;");
        this.separation2 = new Line(0, 0, 400, 0);
        this.separation2.setStyle("-fx-stroke: black;");
    }

    @Override
    public void actualiser(Sujet s) {
        Model model = (Model) s;
        this.classe = model.getClasse(this.nom.getText().substring(this.nom.getText().lastIndexOf(".") + 1));
        String nomClasse = this.nom.getText().substring(this.nom.getText().lastIndexOf(".") + 1);
        this.getChildren().clear();
        updateHeader(model);
        this.getChildren().addAll(header, separation1);
        updateAttributs(model.getClasse(nomClasse).getAttributs());
        this.getChildren().addAll(attributs, separation2);
        updateMethodes(model.getClasse(nomClasse).getMethodes());
        this.getChildren().add(methodes);
    }

    public Classe getClasse() {
        return classe;
    }

    public void setNom(String nom) {
        this.nom.setText(nom);
    }

    private void updateHeader(Model model) {
        this.header.getChildren().clear();
        String nomClasse = this.nom.getText().substring(this.nom.getText().lastIndexOf(".") + 1);
        this.header.getChildren().add(new Label(model.getClasse(nomClasse).getPackage()));
        this.header.getChildren().add(this.nom);
    }

    private void updateAttributs(ArrayList<String> attributs) {
        this.attributs.getChildren().clear();
        if (attributs.isEmpty()) {
            this.attributs.getChildren().add(new Label("")); // Pour éviter que la VBox ne se réduise à 0
        }
        for (String attribut : attributs) {
            this.attributs.getChildren().add(new Label(attribut));
        }
    }

    private void updateMethodes(ArrayList<String> methodes) {
        this.methodes.getChildren().clear();
        if (methodes.isEmpty()) {
            this.methodes.getChildren().add(new Label("")); // Pour éviter que la VBox ne se réduise à 0
        }
        for (String methode : methodes) {
            this.methodes.getChildren().add(new Label(methode));
        }
    }

    public void drag(Node node) {
        node.setOnMousePressed(e -> {
            x = e.getSceneX() - this.getLayoutX();
            y = e.getSceneY() - this.getLayoutY();
        });

        node.setOnMouseDragged(e -> {
            this.setLayoutX(e.getSceneX() - x);
            this.setLayoutY(e.getSceneY() - y);
        });
    }
}