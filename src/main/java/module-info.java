module com.example.gradequeue {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.example.gradequeue to javafx.fxml, com.google.gson;
    exports com.example.gradequeue;
}