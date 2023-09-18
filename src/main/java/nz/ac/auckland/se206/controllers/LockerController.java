package nz.ac.auckland.se206.controllers;

import java.util.Random;
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
import javafx.stage.Stage;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;

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
  private int numsEntered = 0;
  private int randNum;
  private int randNum1 = 0;
  private Shadow shadow = new Shadow(10, Color.BLACK);
  private Glow glow = new Glow(0.8);

  public void initialize() {
    // Initialization code goes here
    GameState.timer.setLocker(timerLabel);
    enterButton.disableProperty().setValue(true);
    randNum = (int) (Math.random() * 100);
    while (randNum1 > 9000 || randNum1 < 1000) {
      randNum1 = (int) (Math.random() * 10000);
    }
    GameState.lockerController = this;
    if (GameState.countryIsFound) {
      chatLabel.setText("What is " + randNum1 + " + " + randNum + "?");
    } else {
      chatLabel.setText("Hallpass Needed");
    }
  }

  @FXML
  private void onClickChat() {
    System.out.println("chat clicked");
    chatButton.setOpacity(0.5);
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
    ghost.setEffect(shadow);
  }

  @FXML
  private void onExitGhost() {
    System.out.println("hover off ghost");
    ghost.setEffect(null);
  }

  @FXML
  private void enterChatButton() {
    chatButton.setOpacity(0.5);
  }

  @FXML
  private void releaseChat() {
    chatButton.setOpacity(1);
  }

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

  @FXML
  private void onClickBasketball() {
    System.out.println("basketball clicked");
    GameState.basketballCollected = true;

    GameState.blackboardController.showBasketball();
    GameState.blackboardController.setObjectiveText("Objective: What new things can I reach now?");
    ChatMessage toAppend = new ChatMessage("dev", "*BASKETBALL FOUND*");
    GameState.chatController.appendChatMessage(toAppend);
    if (!GameState.isChatOpen) {
      onClickChat();
    }
    GameState.chatController.changeChatAndSend(
        new ChatCompletionRequest().setN(1).setTemperature(0.5).setTopP(0.3).setMaxTokens(100),
        "state4");
    basketball.setVisible(false);
    note1.setVisible(true);
    note2.setVisible(true);
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

  @FXML
  private void onClickHelp() {
    System.out.println("help button clicked");

    if (!GameState.countryIsFound) {
      return;
    }

    textField.setText("");
    numsEntered = 0;
    String sentence = "What is " + randNum1 + " + " + randNum + "?";
    chatLabel.setText(sentence);
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

  private void updateTextField(String number) {
    if (!GameState.countryIsFound) {
      return;
    }
    chatLabel.setText("");

    // Change the buttons enabled based on the number of digits entered
    if (number == null) {
      // Clear button was pressed
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
  }

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
      basketball.toFront();
    } else {
      // Incorrect answer
      numsEntered = 0;
      textField.setText("Incorrect");
      updateTextField(null);
    }
  }

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

  public void responseLoading() {
    ghost.setEffect(shadow);
    Random random = new Random();
    int randomNumber = random.nextInt(3); // Generates a random number 0, 1, or 2

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
      default:
        break;
    }
  }

  public void responseLoaded() {
    ghost.setEffect(null);
    pane.setEffect(null);
    messageText.setVisible(false);
    messageText.setEffect(glow);
    messageText1.setVisible(false);
    messageText1.setEffect(glow);
  }
}
