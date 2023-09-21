package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Random;
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
import nz.ac.auckland.se206.gpt.ChatMessage;

public class GymnasiumController {

  @FXML private Label timerLabel;
  @FXML private Rectangle hallwayDoor;
  @FXML private Label goalLabel;
  @FXML private ImageView ghost;
  @FXML private Pane chatContainer;
  @FXML private Pane blackboardContainer;
  @FXML private ImageView chatButton;
  @FXML private ImageView ghost1;
  @FXML private ImageView ghost2;
  @FXML private ImageView redButton;
  @FXML private Rectangle exitDoor;
  @FXML private Pane room;
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
      if (goalCount == 30) {
        goalCount = 0;
      }
      goalCount += 3;
      String toAdd = String.format("%02d", goalCount);
      goalLabel.setText(toAdd);
      ChatMessage toAppend = new ChatMessage("dev", "*3 POINTER*");
      GameState.chatController.appendChatMessage(toAppend);
      if (!GameState.isChatOpen) {
        onClickChat();
      }
      if (goalCount == 24) {
        redButton.setOpacity(1);
        redButton.setEffect(new Glow(1));
        exitDoor.setEffect(glow);
      } else {
        redButton.setEffect(null);
        redButton.setOpacity(0.6);
        exitDoor.setEffect(null);
      }
    }
  }

  @FXML
  public void exitDoorClicked() {
    if (goalCount == 24) {
      GameState.timer.timeIsUp();
    }
  }

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

  @FXML
  public void hallwayDoorEntered() {
    GameState.blackboardController.setHoverText("Hallway Door");
  }

  @FXML
  public void hallwayDoorExited() {
    GameState.blackboardController.setHoverText("");
  }

  @FXML
  public void backboardEntered() {
    GameState.blackboardController.setHoverText("Backboard");
  }

  @FXML
  public void backboardExited() {
    GameState.blackboardController.setHoverText("");
  }

  @FXML
  public void exitDoorEntered() {
    GameState.blackboardController.setHoverText("Exit Door");
  }

  @FXML
  public void exitDoorExited() {
    GameState.blackboardController.setHoverText("");
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

  public void responseLoading() {
    ghost.setEffect(shadow);
    Random random = new Random();
    int randomNumber = random.nextInt(3); // Generates a random number 0, 1, or 2

    switch (randomNumber) {
      case 0:
        // Apply the glow effect to 'room'
        room.setEffect(glow);
        break;
      case 1:
        // Make the ghosts visible
        ghost1.setVisible(true);
        ghost2.setVisible(true);
        break;
      case 2:
        // Make the ghost's shadows visible
        ghost1.setVisible(true);
        ghost2.setVisible(true);
        ghost1.setEffect(shadow);
        break;
      default:
        break;
    }
  }

  public void responseLoaded() {
    ghost.setEffect(null);
    ghost1.setEffect(null);
    ghost1.setVisible(false);
    ghost2.setVisible(false);
    room.setEffect(null);
  }
}