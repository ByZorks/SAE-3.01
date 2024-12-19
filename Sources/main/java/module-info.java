module com.example.sae3_01 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens sae3_01 to javafx.fxml;
    exports sae3_01;
}