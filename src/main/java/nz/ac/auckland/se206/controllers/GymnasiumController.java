package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class GymnasiumController {

  @FXML private Label timerLabel;
  @FXML private Rectangle hallwayDoor;

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    // Initialization code goes here
    // Adding timer label to synced timer
    GameState.timer.setGym(timerLabel);
  }

  @FXML
  public void clickHallDoor(MouseEvent event) throws IOException {
    System.out.println("hallway door clicked");

    // Switching to hallway scene
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene sceneRectangleIsIn = rectangle.getScene();
    sceneRectangleIsIn.setRoot(SceneManager.getUiRoot(AppUi.HALLWAY));

    // Resizing the window so the larger scene fits
    sceneRectangleIsIn.getWindow().sizeToScene();
  }

  private void switchToEndScene() {
    Platform.runLater(
        () -> {
          Scene currentScene = timerLabel.getScene();
          if (currentScene != null) {
            try {
              SceneManager.addUi(AppUi.END, App.loadFxml("end"));
            } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
            currentScene.setRoot(SceneManager.getUiRoot(AppUi.END));
            currentScene.getWindow().sizeToScene();
          }
        });
  }
}
