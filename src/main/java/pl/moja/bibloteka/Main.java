package pl.moja.bibloteka;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.moja.bibloteka.database.dbuitls.DbManager;
import pl.moja.bibloteka.utils.FxmlUtils;



public class Main extends Application {

    public static final String BORDER_PANE_MAIN_FXML = "/fxml/BorderPaneMain.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        Pane borderPane = FxmlUtils.fxmlLoader(BORDER_PANE_MAIN_FXML);

        Scene scene = new Scene(borderPane);

        primaryStage.setScene(scene);
        primaryStage.setTitle(FxmlUtils.getResourceBundle().getString("title.aplication"));
        primaryStage.show();

        DbManager.initDatabase();
       // FillDatabase.fillDatabase(); // wyłaczenie obslugi ladowania pliku wsadowego z danymi
    }
}
