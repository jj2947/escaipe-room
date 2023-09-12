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
import javafx.stage.Stage;
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
  @FXML private Pane chatContainer;
  private Timer timer;
  private boolean chatAdded;

  /**
   * Initializes the room view, it is called when the room loads.
   *
   * @throws IOException
   */
  public void initialize() throws IOException {
    // Initialization code goes here
    // Start the timer
    timer = new Timer(timerLabel);
    GameState.timer = timer;

    SceneManager.addUi(AppUi.GYMNASIUM, App.loadFxml("gymnasium"));
    SceneManager.addUi(AppUi.LOCKER, App.loadFxml("locker"));
    SceneManager.addUi(AppUi.HALLWAY, App.loadFxml("hallway"));
    Platform.runLater(() -> startTimer());
  }

  private void startTimer() {
    timer.startTimer();
    // Update the timer label every second
    Task<Void> updateLabelTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            while (!GameState.isTimeReached) {
              Platform.runLater(() -> updateLabel());
              Thread.sleep(1000);
            }
            if (GameState.isTimeReached) {
              switchToEndScene();
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
    timerLabel.setText(String.format("%d:%02d", timer.getCounter() / 60, timer.getCounter() % 60));
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
    System.out.println("hallway door clicked");
    chatAdded = false;

    // Switching to hallway scene
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene sceneRectangleIsIn = rectangle.getScene();
    sceneRectangleIsIn.setRoot(SceneManager.getUiRoot(AppUi.HALLWAY));

    // Resizing the window so the scene fits
    sceneRectangleIsIn.getWindow().sizeToScene();
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

  @FXML
  public void onClickGhost(MouseEvent event) {
    System.out.println("ghost clicked");
    // Add the chat to the chat container
    if (!chatAdded) {
      chatContainer.getChildren().add(GameState.chatController.getChatPane());
      chatAdded = true;
    }
    // Get the stage from the chatContainer's scene
    Stage stage = (Stage) timerLabel.getScene().getWindow();
    // Resize the stage
    stage.setWidth(1407);
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
