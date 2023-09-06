package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class StartScreenController {
  @FXML private Button startButton;
  @FXML private Button exitButton;

  /**
   * Called when the start button is clicked
   *
   * @param event the event that triggered this method
   */
  @FXML
  private void onStart(ActionEvent event) {
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    // Switching Scenes to the room
    try {
      SceneManager.addUi(AppUi.ROOM, App.loadFxml("room"));
      GameState.roomIsLoaded = true;
      sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.ROOM));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Called when the exit button is clicked
   *
   * @param even the event that triggered this method
   */
  @FXML
  private void onExit() {
    // exit application
    Platform.exit();
    System.exit(0);
  }
}
