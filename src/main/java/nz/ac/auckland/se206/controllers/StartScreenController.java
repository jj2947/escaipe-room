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
  @FXML private Button easyButton;
  @FXML private Button mediumButton;
  @FXML private Button hardButton;
  @FXML private Button twoButton;
  @FXML private Button fourButton;
  @FXML private Button sixButton;

  private Button difficulty = null;
  private Button time = null;

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

  /** Called when the exit button is clicked */
  @FXML
  private void onExit() {
    // exit application
    Platform.exit();
    System.exit(0);
  }

  /** Setting difficulty to easy */
  @FXML
  private void onEasy() {}

  /** Setting difficulty to medium */
  @FXML
  private void onMedium() {}

  /** Setting difficulty to hard */
  @FXML
  private void onHard() {}

  /** Setting time to Two */
  @FXML
  private void onTwo() {}

  /** Setting time to Four */
  @FXML
  private void onFour() {}

  /** Setting time to Six */
  @FXML
  private void onSix() {}
}
