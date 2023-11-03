module com.psp.flappybird {
    requires javafx.controls;
    requires javafx.media;
    requires javafx.fxml;


    opens com.psp.flappybird to javafx.fxml;
    exports com.psp.flappybird;
}