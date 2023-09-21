package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class EndController {

  @FXML private Label textLabel;

  public void initialize() {
    updateLabel();
  }

  @FXML
  public void updateLabel() {
    if (GameState.isTimeReached) {
      textLabel.setText("You Lost!");
    } else {
      textLabel.setText("You Escaped!");
    }
  }

  @FXML
  private void onExit() {
    Platform.exit();
    System.exit(0);
    GameState.timer.exitGame();
  }

  @FXML
  private void onRestart() throws IOException {
    // Remove scenes that need to be cleared
    SceneManager.removeUi(AppUi.ROOM);
    SceneManager.removeUi(AppUi.BLACKBOARD);
    SceneManager.removeUi(AppUi.HALLWAY);
    SceneManager.removeUi(AppUi.GYMNASIUM);
    SceneManager.removeUi(AppUi.LOCKER);
    SceneManager.removeUi(AppUi.CHAT);
    SceneManager.removeUi(AppUi.STARTSCREEN);
    SceneManager.removeUi(AppUi.END);

    System.out.println("Scenes removed.");

    GameState.isRiddleResolved = false;
    GameState.isKeyFound = false;
    GameState.isTimeReached = false;
    GameState.countryIsFound = false;
    GameState.basketballCollected = false;
    GameState.chatInRoom = false;
    GameState.chatInHall = false;
    GameState.chatInLocker = false;
    GameState.chatInGym = false;
    GameState.isChatOpen = false;
    GameState.roomController = null;
    GameState.currentState = "state1";

    // Reset the existing Timer instance (if needed)
    if (GameState.timer != null) {
      GameState.timer.reset();
      GameState.timer = new Timer();
    }

    // Use a thread pool to load resources in parallel
    ExecutorService executor = Executors.newFixedThreadPool(4);

    // Load resources in parallel
    executor.submit(() -> loadSceneAsync(AppUi.ROOM, "room"));
    executor.submit(() -> loadSceneAsync(AppUi.BLACKBOARD, "blackboard"));
    executor.submit(() -> loadSceneAsync(AppUi.HALLWAY, "hallway"));
    executor.submit(() -> loadSceneAsync(AppUi.GYMNASIUM, "gymnasium"));
    executor.submit(() -> loadSceneAsync(AppUi.LOCKER, "locker"));
    executor.submit(() -> loadSceneAsync(AppUi.CHAT, "chat"));

    // Shutdown the executor when tasks are done
    executor.shutdown();

    // Switching to start scene
    Scene thisScene = textLabel.getScene();
    SceneManager.addUi(AppUi.STARTSCREEN, App.loadFxml("startscreen"));
    thisScene.setRoot(SceneManager.getUiRoot(AppUi.STARTSCREEN));
    Stage stage = (Stage) thisScene.getWindow();
    stage.centerOnScreen();

    System.out.println("STARTSCREEN added.");
  }

  // Method to load a scene asynchronously
  private void loadSceneAsync(AppUi ui, String fxmlFileName) {
    try {
      SceneManager.addUi(ui, App.loadFxml(fxmlFileName));
      System.out.println(ui + " added.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
