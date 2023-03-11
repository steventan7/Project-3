module studentinfo.project3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires junit;


    opens studentinfo.project3 to javafx.fxml;
    exports studentinfo.project3;
}