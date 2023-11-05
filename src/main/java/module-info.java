module co.edu.iudigital.rrhhfuncionarios {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;
    requires jakarta.persistence;
    requires static lombok;

    opens co.edu.iudigital.rrhhfuncionarios to javafx.fxml;
    exports co.edu.iudigital.rrhhfuncionarios;

    opens co.edu.iudigital.rrhhfuncionarios.data.models to org.hibernate.orm.core;
    exports co.edu.iudigital.rrhhfuncionarios.data.models;

    exports co.edu.iudigital.rrhhfuncionarios.controllers;
    opens co.edu.iudigital.rrhhfuncionarios.controllers to javafx.fxml;
}