package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    final KeyCombination FullScreenKeyCombo =
            new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_ANY);

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(root, 700, 700);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setResizable(true);
        primaryStage.setTitle("Hello World");

        scene.getStylesheets().add("CSS/MyCSS.css");
        primaryStage.setScene(scene);


        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {

            if(FullScreenKeyCombo.match(event)) {

                primaryStage.setFullScreen(!primaryStage.isFullScreen());

            }
        });

        primaryStage.show();
       ResizeHelper.addResizeListener(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
