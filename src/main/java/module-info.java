module com.vm.cvm {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires java.sql;

    opens com.vm.cvm to javafx.fxml;
    opens com.vm.cvm.controller;
    opens com.vm.cvm.modelo;
    exports com.vm.cvm;
    exports com.vm.cvm.modelo;
    exports com.vm.cvm.controller;
}