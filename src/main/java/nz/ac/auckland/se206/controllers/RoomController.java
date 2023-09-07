package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** Controller class for the room view. */
public class RoomController {

  @FXML private Pane room;
  @FXML private Rectangle door;
  @FXML private Rectangle window;
  @FXML private Rectangle vase;
  @FXML private Label timerLabel;
  private Timer timer;

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    // Initialization code goes here
    // Start the timer
    timer = new Timer(timerLabel);
    GameState.timer = timer;

    Platform.runLater(() -> startUpdateLabelTask());
  }

  private void startUpdateLabelTask() {
    timer.startTimer();
    // Update the timer label every second
    Task<Void> updateLabelTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            while (!GameState.isTimeReached) {
              Thread.sleep(1000); // Wait for 1 second
              Platform.runLater(() -> updateLabel());
            }
            return null;
          }
        };

    // Create a new thread for the update task and start it
    Thread updateThread = new Thread(updateLabelTask);
    updateThread.setDaemon(true);
    updateThread.start();
  }

  private void updateLabel() {
    timerLabel.setText(
        String.format("%02d:%02d", timer.getCounter() / 60, timer.getCounter() % 60));
  }

  /**
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {
    System.out.println("key " + event.getCode() + " pressed");
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    System.out.println("key " + event.getCode() + " released");
  }

  /**
   * Displays a dialog box with the given title, header text, and message.
   *
   * @param title the title of the dialog box
   * @param headerText the header text of the dialog box
   * @param message the message content of the dialog box
   */
  private void showDialog(String title, String headerText, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Handles the click event on the door.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickDoor(MouseEvent event) throws IOException {
    System.out.println("door clicked");

    if (!GameState.isRiddleResolved) {
      showDialog("Info", "Riddle", "You need to resolve the riddle!");
      Scene sceneButtonIsIn = room.getScene();
      // Switching Scenes to the room
      try {
        // Chat isn't already loaded load first
        if (!GameState.chatIsLoaded) {
          SceneManager.addUi(AppUi.CHAT, App.loadFxml("chat"));
          GameState.chatIsLoaded = true;
        }
        sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.CHAT));
      } catch (Exception e) {
        e.printStackTrace();
      }
      return;
    }

    if (!GameState.isKeyFound) {
      showDialog(
          "Info", "Find the key!", "You resolved the riddle, now you know where the key is.");
    } else {
      showDialog("Info", "You Won!", "Good Job!");
    }
  }

  /**
   * Handles the click event on the vase.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickVase(MouseEvent event) {
    System.out.println("vase clicked");
    if (GameState.isRiddleResolved && !GameState.isKeyFound) {
      showDialog("Info", "Key Found", "You found a key under the vase!");
      GameState.isKeyFound = true;
    }
  }

  /**
   * Handles the click event on the window.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickWindow(MouseEvent event) {
    System.out.println("window clicked");
  }
}
