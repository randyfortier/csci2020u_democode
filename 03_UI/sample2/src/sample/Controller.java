package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {
    @FXML private TreeView<String> projectTreeView;
    @FXML private TextArea editor;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField password2Field;
    @FXML private TextField emailField;

    // called to populate the UI elements
    public void initialize() {
        // create the tree elements
        TreeItem<String> rootItem = new TreeItem<>("Project");
        rootItem.setExpanded(true);

        TreeItem<String> src = new TreeItem<>("src");
        src.setExpanded(true);
        rootItem.getChildren().add(src);

        TreeItem<String> main = new TreeItem<>("main");
        main.setExpanded(true);
        src.getChildren().add(main);

        TreeItem<String> java = new TreeItem<>("java");
        java.setExpanded(true);
        main.getChildren().add(java);

        TreeItem<String> helloWorld = new TreeItem<>("HelloWorld.java");
        java.getChildren().add(helloWorld);

        TreeItem<String> buildFile = new TreeItem<>("build.gradle");
        rootItem.getChildren().add(buildFile);

        projectTreeView.setRoot(rootItem);
        projectTreeView.getSelectionModel()
                       .selectedItemProperty()
                       .addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable,
                                Object oldValue,
                                Object newValue) {
                TreeItem<String> selectedItem = (TreeItem<String>)newValue;
                if (selectedItem.getValue().equals("HelloWorld.java")) {
                    editor.setText(""+
                            "public class HelloWorld {\n" +
                            "  public static void main(String[] args) {\n" +
                            "    System.out.println(\"Hello, world!\");\n" +
                            "  }\n" +
                            "}\n"
                    );
                } else if (selectedItem.getValue().equals("build.gradle")) {
                    editor.setText("apply plugin: 'java'");
                }
            }
        });
    }

    // called when the Register button is clicked
    public void register(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        System.out.println("register::username = " + username);
        System.out.println("register::password = " + password);
    }
}
