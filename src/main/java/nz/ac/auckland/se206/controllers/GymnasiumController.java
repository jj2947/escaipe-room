package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Shadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class GymnasiumController {

  @FXML private Label timerLabel;
  @FXML private Rectangle hallwayDoor;
  @FXML private Label goalLabel;
  @FXML private ImageView ghost;
  @FXML private Pane chatContainer;
  @FXML private Pane blackboardContainer;
  @FXML private ImageView chatButton;
  private Shadow shadow = new Shadow(10, Color.BLACK);
  private Glow glow = new Glow(0.8);
  private int goalCount = 0;

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    // Initialization code goes here
    GameState.gymController = this;
    // Adding timer label to synced timer
    GameState.timer.setGym(timerLabel);
  }

  @FXML
  public void clickHallDoor(MouseEvent event) throws IOException {
    System.out.println("hallway door clicked");

    if (GameState.isChatOpen) {
      GameState.hallController.openChat();
    }

    // Switching to hallway scene
    Rectangle rectangle = (Rectangle) event.getSource();
    Scene sceneRectangleIsIn = rectangle.getScene();
    sceneRectangleIsIn.setRoot(SceneManager.getUiRoot(AppUi.HALLWAY));

    if (!GameState.isChatOpen) {
      // Resizing the window so the scene fits
      sceneRectangleIsIn.getWindow().sizeToScene();
    }

    GameState.hallController.addBlackboard();
  }

  @FXML
  public void clickBackboard() {
    if (GameState.basketballCollected) {
      goalCount += 3;
      String toAdd = String.format("%02d", goalCount);
      goalLabel.setText(toAdd);
    }
  }

  @FXML
  public void clickRedButton() {}

  @FXML
  private void onClickChat() {
    System.out.println("chat clicked");
    chatButton.setOpacity(0.5);
    // Add the chat to the chat container
    if (!GameState.chatInGym) {
      openChat();
    }

    if (GameState.isChatOpen) {
      GameState.chatController.closeChat();
      GameState.isChatOpen = false;
    } else {
      GameState.chatController.openChat();
      GameState.isChatOpen = true;
    }
  }

  @FXML
  private void releaseChat() {
    chatButton.setOpacity(1);
  }

  @FXML
  private void enterChatButton() {
    chatButton.setOpacity(0.5);
  }

  @FXML
  private void onEnterGhost() {
    System.out.println("hover on ghost");
    ghost.setEffect(shadow);
  }

  @FXML
  private void onExitGhost() {
    System.out.println("hover off ghost");
    ghost.setEffect(null);
  }

  public void openChat() {
    GameState.chatInHall = false;
    GameState.chatInRoom = false;
    GameState.chatInLocker = false;
    chatContainer.getChildren().add(GameState.chatController.getChatPane());
    GameState.chatInGym = true;
  }

  public void addBlackboard() {
    // Adding the blackboard to the scene
    blackboardContainer.getChildren().add(GameState.blackboardController.getPane());
    chatButton.toFront();
  }
}
