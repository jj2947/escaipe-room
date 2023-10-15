package nz.ac.auckland.se206.controllers;

import java.util.Random;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Shadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;

/** This is the controller for the locker scene. */
public class LockerController {
  @FXML private Label timerLabel;
  @FXML private ImageView note1;
  @FXML private ImageView note2;
  @FXML private ImageView basketball;
  @FXML private Button oneButton;
  @FXML private Button twoButton;
  @FXML private Button threeButton;
  @FXML private Button fourButton;
  @FXML private Button fiveButton;
  @FXML private Button sixButton;
  @FXML private Button sevenButton;
  @FXML private Button eightButton;
  @FXML private Button nineButton;
  @FXML private Button zeroButton;
  @FXML private Button enterButton;
  @FXML private TextField textField;
  @FXML private Button helpButton;
  @FXML private Button backButton;
  @FXML private Button clearButton;
  @FXML private Label chatLabel;
  @FXML private Pane pinpad;
  @FXML private ImageView ghost;
  @FXML private Pane chatContainer;
  @FXML private Label messageBox;
  @FXML private ImageView chatButton;
  @FXML private Pane lockerPane;
  @FXML private Label messageText;
  @FXML private Label messageText1;
  @FXML private Pane pane;
  @FXML private Label noteLabel1;
  @FXML private Label noteLabel2;
  @FXML private Path path;
  @FXML private ImageView speaker;
  @FXML private Line line;
  private int numsEntered = 0;
  private int randNum;
  private int randNum1 = 0;
  private boolean isGhostMoving;
  private Shadow shadow = new Shadow(10, Color.BLACK);
  private Glow glow = new Glow(0.8);

  /** Called when the locker scene is loaded. It initializes this scene. */
  public void initialize() {
    // Initialization code goes here
    GameState.timer.setLocker(timerLabel);
    enterButton.disableProperty().setValue(true);
    randNum = (int) (Math.random() * 100);
    while (randNum1 > 9000 || randNum1 < 1000) {
      randNum1 = (int) (Math.random() * 10000);
    }
    GameState.lockerController = this;
    GameState.pinAnswer = randNum + randNum1;
    chatLabel.setText("What is " + randNum1 + " + " + randNum + "?");
  }

  /** Called when the chat button is clicked. */
  @FXML
  private void onClickChat() {
    System.out.println("chat clicked");

    // Add the chat to the chat container
    if (!GameState.chatInLocker) {
      openChat();
    }

    if (GameState.isChatOpen) {
      Stage stage = (Stage) ghost.getScene().getWindow();
      stage.setWidth(869);
      stage.centerOnScreen();
      GameState.isChatOpen = false;
    } else {
      Stage stage = (Stage) ghost.getScene().getWindow();
      stage.setWidth(1109);
      stage.centerOnScreen();
      GameState.isChatOpen = true;
    }
  }

  @FXML
  private void onEnterGhost() {
    System.out.println("hover on ghost");
    isGhostMoving = false;
    Platform.runLater(() -> ghost.setEffect(shadow));
  }

  /**
   * Called when the ghost is clicked. If the chat is not open, open the chat and move the ghost. If
   * the chat is open, just move th ghost.
   */
  @FXML
  private void onClickGhost() {
    // If the chat is not open, open the chat and move the ghost
    if (!GameState.isChatOpen) {
      onClickChat();
      Platform.runLater(() -> moveGhost());
    } else { // If the chat is open, just move the ghost
      Platform.runLater(() -> moveGhost());
      isGhostMoving = true;
    }
  }

  @FXML
  private void onExitGhost() {
    if (isGhostMoving == false) {
      Platform.runLater(() -> ghost.setEffect(null));
    }
  }

  @FXML
  private void enterChatButton() {
    chatButton.setOpacity(0.5);
  }

  @FXML
  private void releaseChat() {
    chatButton.setOpacity(1);
  }

  /**
   * Called when the chat is opened. It adds the chat to the chat container and sets the chatInRoom
   * variable.
   */
  public void openChat() {
    GameState.chatInGym = false;
    GameState.chatInHall = false;
    GameState.chatInRoom = false;
    chatContainer.getChildren().add(GameState.chatController.getChatPane());
    GameState.chatInLocker = true;
  }

  @FXML
  private void enterBackButton() {
    System.out.println("back button entered");
    backButton.setOpacity(0.5);
  }

  @FXML
  private void exitBackButton() {
    System.out.println("back button exited");
    backButton.setOpacity(1);
  }

  /**
   * Called when the basketball is clicked. It updates the game state to reflect the basketball
   * being clicked.
   */
  @FXML
  private void onClickBasketball() {
    System.out.println("basketball clicked");
    GameState.basketballCollected = true;
    // Remove the basketball from the scene
    GameState.blackboardController.showBasketball();
    // Update the game to reflect the basketball being found
    GameState.blackboardController.setObjectiveText("Objective: How many points should I score?");
    GameState.textFlow.getChildren().clear();
    // Change the chat to the next state
    GameState.chatController.changeChatAndSend(
        new ChatCompletionRequest().setN(1).setTemperature(.2).setTopP(0.5).setMaxTokens(100),
        "state4");
    GameState.currentState = "state4";
    GameState.chatController.newStateHint();
    // Remove the basketball from the locker and make the notes visible
    basketball.setVisible(false);
    noteLabel1.setVisible(true);
    noteLabel2.setVisible(true);
  }

  @FXML
  private void enterBasketball() {
    System.out.println("basketball entered");
    basketball.setOpacity(0.4);
  }

  @FXML
  private void exitBasketball() {
    System.out.println("basketball exited");
    basketball.setOpacity(1);
  }

  /** Called when the hint button is clicked. It gives the user a hint. */
  @FXML
  private void onClickHelp() {
    System.out.println("help button clicked");
    if (!GameState.isChatOpen) { // If the chat is not open, open the chat
      onClickChat();
    }

    GameState.currentState = "state5";

    // Send a hint to the chat

    GameState.chatController.hintClicked();
    if (GameState.numberOfHints == 0) { // If there are no more hints, disable the hint button
      helpButton.setDisable(true);
    }
  }

  @FXML
  private void onClickOne() {
    updateTextField("1");
  }

  @FXML
  private void onClickTwo() {
    updateTextField("2");
  }

  @FXML
  private void onClickThree() {
    updateTextField("3");
  }

  @FXML
  private void onClickFour() {
    updateTextField("4");
  }

  @FXML
  private void onClickFive() {
    updateTextField("5");
  }

  @FXML
  private void onClickSix() {
    updateTextField("6");
  }

  @FXML
  private void onClickSeven() {
    updateTextField("7");
  }

  @FXML
  private void onClickEight() {
    updateTextField("8");
  }

  @FXML
  private void onClickNine() {
    updateTextField("9");
  }

  @FXML
  private void onClickZero() {
    updateTextField("0");
  }

  /**
   * Method to update the text field based on the number entered.
   *
   * @param number the number entered by the user
   */
  private void updateTextField(String number) {
    if (!GameState.countryIsFound) {
      return;
    }

    // Change the buttons enabled based on the number of digits entered
    if (number == null) {
      // Clear button was pressed
      updateButtons();
      return;
    }
    numsEntered++;

    // Update the text field based on the number of digits entered
    if (numsEntered == 1) {
      textField.setText(number + " _ _ _");
    } else if (numsEntered == 2) {
      textField.setText(textField.getText().charAt(0) + " " + number + " _ _");
    } else if (numsEntered == 3) {
      textField.setText(textField.getText().substring(0, 4) + number + " _");
    } else if (numsEntered == 4) {
      // Enable the enter button
      enterButton.disableProperty().setValue(false);
      enterButton.requestFocus();
      textField.setText(textField.getText().substring(0, 6) + number);
      oneButton.disableProperty().setValue(true);
      twoButton.disableProperty().setValue(true);
      threeButton.disableProperty().setValue(true);
      fourButton.disableProperty().setValue(true);
      fiveButton.disableProperty().setValue(true);
      sixButton.disableProperty().setValue(true);
      sevenButton.disableProperty().setValue(true);
      eightButton.disableProperty().setValue(true);
      nineButton.disableProperty().setValue(true);
      zeroButton.disableProperty().setValue(true);
    } else {
      numsEntered = 0;
      updateButtons();
    }
  }

  /** Method to update the buttons based on the number of digits entered. */
  private void updateButtons() {
    // Update the buttons to disable the numbers when 4 numbers have already been entered
    enterButton.disableProperty().setValue(true);
    oneButton.disableProperty().setValue(false);
    twoButton.disableProperty().setValue(false);
    threeButton.disableProperty().setValue(false);
    fourButton.disableProperty().setValue(false);
    fiveButton.disableProperty().setValue(false);
    sixButton.disableProperty().setValue(false);
    sevenButton.disableProperty().setValue(false);
    eightButton.disableProperty().setValue(false);
    nineButton.disableProperty().setValue(false);
    zeroButton.disableProperty().setValue(false);
  }

  /**
   * Called when the enter button is clicked. It checks if the answer is correct and unlocks the
   * locker is correct.
   */
  @FXML
  private void onEnter() {
    if (!GameState.countryIsFound) {
      return;
    }
    // Get the answer
    int answer = randNum1 + randNum;
    String answerString = Integer.toString(answer);
    // Get player input
    String input = stripString(textField.getText());
    // Check if the input is correct
    if (input.equals(answerString)) {
      // Open Locker
      pinpad.setVisible(false);
      basketball.setVisible(true);
      note1.setVisible(true);
      note2.setVisible(true);
      basketball.toFront();
    } else {
      // Incorrect answer
      numsEntered = 0;
      // Create a PauseTransition for 5 seconds
      PauseTransition pause = new PauseTransition(Duration.seconds(2));
      pause.setOnFinished(
          event -> {
            textField.setText("_ _ _ _");
            updateTextField(null);
          });
      textField.setText("Incorrect");
      pause.play();
    }
  }

  /** Called when the back button is clicked. It returns the user to the hallway scene. */
  @FXML
  private void onBack() {
    if (GameState.isChatOpen) {
      Stage stage = (Stage) chatContainer.getScene().getWindow();
      stage.setWidth(1340);
      stage.centerOnScreen();
      GameState.hallController.openChat();
    }

    Scene currentScene = timerLabel.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.HALLWAY));

    if (!GameState.isChatOpen) {
      // Resizing the window so the scene fits
      currentScene.getWindow().sizeToScene();
      // Get the stage after switching the scene
      Stage stage = (Stage) currentScene.getWindow();
      stage.centerOnScreen();
    }
  }

  /**
   * Called when the clear button is clicked. It clears the text field and resets the numbers
   * entered.
   */
  @FXML
  private void onClear() {
    if (!GameState.countryIsFound) {
      return;
    }

    numsEntered = 0;
    textField.setText("_ _ _ _");
    updateTextField(null);
  }

  private String stripString(String str) {
    return str.replaceAll("[^a-zA-Z0-9]", "");
  }

  public void setQuestion() {
    chatLabel.setText("What is " + randNum1 + " + " + randNum + "?");
  }

  /** Called when GPT is loading. It sets the effects in the room. */
  public void responseLoading() {
    ghost.setEffect(shadow);
    Random random = new Random();
    int randomNumber = random.nextInt(4); // Generates a random number 0, 1, 2, 3

    switch (randomNumber) {
      case 0:
        // Apply the effect to 'room'
        pane.setEffect(glow);
        break;
      case 1:
        messageText.setVisible(true);
        messageText1.setVisible(true);
        messageText1.setEffect(shadow);
        break;
      case 2:
        messageText1.setVisible(true);
        messageText.setVisible(true);
        messageText.setEffect(shadow);
        break;
      case 3:
        Platform.runLater(() -> moveGhost());
        break;
    }
  }

  /** Called when GPT is done loading. It removes the effects in the room. */
  public void responseLoaded() {
    ghost.setEffect(null);
    pane.setEffect(null);
    messageText.setVisible(false);
    messageText.setEffect(glow);
    messageText1.setVisible(false);
    messageText1.setEffect(glow);
  }

  /** Called when the ghost is moving. It moves the ghost and sets the shadow effect. */
  private void moveGhost() {
    ghost.setEffect(shadow);
    chatButton.setVisible(false);
    PathTransition pathTransition = new PathTransition();
    double width = ghost.getLayoutBounds().getWidth();
    double height = ghost.getLayoutBounds().getHeight();

    path.setLayoutX(-ghost.getLayoutX() + width / 2 + 50);
    path.setLayoutY(ghost.getLayoutY() - height / 2 - 55);

    // Set the path and node for the PathTransition
    pathTransition.setPath(path);
    pathTransition.setNode(ghost);

    // Set the duration of the animation (3000 milliseconds)
    pathTransition.setDuration(Duration.millis(3000));

    // Set the cycle count to 2
    pathTransition.setCycleCount(2);
    pathTransition.setAutoReverse(true);

    // Set the action to be performed when the animation finishes
    pathTransition.setOnFinished(
        event -> {
          ghost.setEffect(null); // Set the shadow effect to null
          chatButton.setVisible(true);
        });

    // Play the animation
    pathTransition.play();
  }

  public void updateHintButton() {
    if (GameState.numberOfHints == 0) {
      helpButton.setDisable(true);
    } else {
      helpButton.setDisable(false);
    }
  }

  public void disableHelpButton() {
    helpButton.setDisable(true);
  }

  public void enableHelpButton() {
    helpButton.setDisable(false);
  }

  @FXML
  private void enterSpeaker() {
    speaker.setOpacity(0.5);
  }

  @FXML
  private void exitSpeaker() {
    speaker.setOpacity(1);
  }

  @FXML
  private void clickSpeaker() {
    GameState.clickSpeaker();
  }

  public Line getLine() {
    return line;
  }

  public Button getHintButton() {
    return helpButton;
  }
}
