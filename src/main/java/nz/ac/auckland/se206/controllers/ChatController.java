package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

/** Controller class for the chat view. */
public class ChatController {
  @FXML private TextField inputText;
  @FXML private AnchorPane chatPane;
  @FXML private Label hintLabel;
  @FXML private Button hintButton;
  @FXML private TextFlow chatTextFlow;
  @FXML private ScrollPane scrollPane;

  private ChatCompletionRequest chatCompletionRequest;
  private ChatCompletionRequest hintChatCompletionRequest;
  private ChatCompletionRequest factChatCompletionRequest =
      new ChatCompletionRequest().setN(1).setTemperature(1).setTopP(0.5).setMaxTokens(100);
  private ChatCompletionRequest digitChatCompletionRequest =
      new ChatCompletionRequest().setN(1).setTemperature(.5).setTopP(1).setMaxTokens(100);
  private ChatMessage chatMsg;
  private CountryChoice countryChooser = new CountryChoice();
  private String country;
  private boolean isFunFact = false;
  private boolean isDigitButton = false;

  /**
   * Initializes the chat view, loading the riddle.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {
    GameState.chatController = this;
    country = countryChooser.chooseCountry();
    GameState.countryToFind = country;
    System.out.println(country);
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(.7).setTopP(0.5).setMaxTokens(100);
    runGpt(new ChatMessage("user", GptPromptEngineering.apiNoHints("state1")), "normal", false);
    newStateHint();
    updateHintCounter();
    if (GameState.numberOfHints == 0) {
      hintButton.setDisable(true);
    }
    GameState.textFlow = chatTextFlow;
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  public void appendChatMessage(ChatMessage msg) {
    Text messageText = new Text(msg.getContent() + "\n\n");
    if (msg.getRole().equals("user")) {
      messageText.setText("Me: " + messageText.getText());
      messageText.setFill(Color.rgb(5, 236, 253));
    } else {
      messageText.setFill(Color.WHITE);
    }

    // Bind the vvalue before adding the message to the TextFlow
    Platform.runLater(
        () -> {
          scrollPane.vvalueProperty().bind(chatTextFlow.heightProperty()); // Scroll to the bottom
          chatTextFlow.getChildren().add(messageText);
        });
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @param type what the response is being used for
   * @param doubleGhost if the instance is going to cause ghost is writing to appear twice
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private ChatMessage runGpt(ChatMessage msg, String type, boolean doubleGhost)
      throws ApiProxyException {
    isFunFact = false;
    isDigitButton = false;
    ChatCompletionRequest chatToUse;
    if (type == "normal") {
      chatToUse = chatCompletionRequest;
    } else if (type == "hints") {
      chatToUse = hintChatCompletionRequest;
    } else if (type == "fact") {
      chatToUse = factChatCompletionRequest;
      isFunFact = true;
    } else if (type == "digit") {
      chatToUse = digitChatCompletionRequest;
      isDigitButton = true;
    } else {
      chatToUse = null;
    }

    if (!doubleGhost) {
      appendChatMessage(new ChatMessage("assistant", "Ghost is Writing..."));
    }

    if (GameState.isChatOpen && !GameState.isGhostTalking) {
      responseLoading();
    }

    // GPT Task
    Task<Void> askGpt =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {
            chatToUse.addMessage(msg);
            try {
              ChatCompletionResult chatCompletionResult = chatToUse.execute();
              Choice result = chatCompletionResult.getChoices().iterator().next();
              chatToUse.addMessage(result.getChatMessage());
              // The response from GPT-API
              chatMsg = result.getChatMessage();
              Platform.runLater(
                  () -> {

                    // Checking to see if the riddle has been solved and changing the game state
                    if (result.getChatMessage().getRole().equals("assistant")
                        && result.getChatMessage().getContent().startsWith("Correct")) {
                      GameState.currentState = "state2";
                      GameState.isRiddleResolved = true;
                      GameState.blackboardController.setObjectiveText(
                          "Objective: Where can I find this country?");
                      // replaceLoadingMessageWithResponse("", false);

                      changeChatAndSend(
                          new ChatCompletionRequest()
                              .setN(1)
                              .setTemperature(.7)
                              .setTopP(0.5)
                              .setMaxTokens(100),
                          "state2");
                    } else {
                      // Replacing the "Ghost is Writing..." with the response
                      replaceLoadingMessageWithResponse(chatMsg.getContent(), isFunFact);
                      // Stop the loading effects
                      responseLoaded();
                    }
                    if (GameState.isChatOpen && !GameState.isGhostTalking) {
                      responseLoaded();
                    }

                    newStateHint();
                    inputText.setDisable(false);
                    if (GameState.numberOfHints != 0) {
                      hintButton.setDisable(false);
                      GameState.lockerController.enableHelpButton();
                    }
                    // If gpt has just given a fun fact run the new prompt for new state
                    if (isFunFact) {
                      changeChatAndSend(
                          new ChatCompletionRequest()
                              .setN(1)
                              .setTemperature(.7)
                              .setTopP(0.5)
                              .setMaxTokens(100),
                          "state3");
                    }
                    if (isDigitButton) {
                      GameState.currentState = "state3";
                    }
                  });

              inputText.setDisable(false);
              // Checking to see if the riddle has been solved and changing the game state
              if (result.getChatMessage().getRole().equals("assistant")
                  && result.getChatMessage().getContent().startsWith("Correct")) {
                GameState.isRiddleResolved = true;
                System.out.println("Riddle Resolved");
              }

            } catch (Exception e) {
              e.printStackTrace();
              return null;
            }
            return null;
          }
        };
    // New thread to start GPT in background
    Thread gptThread =
        new Thread(
            () -> {
              askGpt.run();
            });
    gptThread.start();
    inputText.setDisable(true);
    hintButton.setDisable(true);
    GameState.lockerController.disableHelpButton();
    return chatMsg;
  }

  /**
   * Handles the enter pressed event.
   *
   * @param event the key event
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an error loading the fxml file
   */
  @FXML
  private void onEnterPressed(KeyEvent event) throws ApiProxyException, IOException {
    // When enter is clicked the user sends their message
    if (event.getCode().toString().equals("ENTER")) {
      String message = inputText.getText();
      if (message.trim().isEmpty()) {
        return;
      }
      // Clear the input text field and add the message to the chat text area
      inputText.clear();
      ChatMessage msg = new ChatMessage("user", message);
      appendChatMessage(msg);
      runGpt(msg, "normal", false);
    }
  }

  /** Called when the chat button is clicked. It closes the chat. */
  public void closeChat() {
    Stage stage = (Stage) chatPane.getScene().getWindow();
    stage.setWidth(1100);
    stage.centerOnScreen();
  }

  /** Called when the chat button is clicked. It opens the chat. */
  public void openChat() {
    Stage stage = (Stage) chatPane.getScene().getWindow();
    stage.setWidth(1340);
    stage.centerOnScreen();
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    // When enter is clicked the user sends their message
    if (event.getCode().toString().equals("ENTER")) {
      try {
        onEnterPressed(event);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public AnchorPane getChatPane() {
    return chatPane;
  }

  /** Adds the loading effects to the room the chat is in. */
  private void responseLoaded() {
    // Stop the loading effects when the response is loaded
    GameState.gymController.responseLoaded();
    GameState.roomController.responseLoaded();
    GameState.lockerController.responseLoaded();
    GameState.hallController.responseLoaded();
  }

  /** Removes the loading effects in the room. */
  private void responseLoading() {
    // Start the loading effects while the response is being generated
    if (GameState.chatInGym) {
      GameState.gymController.responseLoading();
    } else if (GameState.chatInRoom) {
      GameState.roomController.responseLoading();
    } else if (GameState.chatInLocker) {
      GameState.lockerController.responseLoading();
    } else if (GameState.chatInHall) {
      GameState.hallController.responseLoading();
    }
  }

  /**
   * Replaces the loading message with the response from GPT.
   *
   * @param response the response from GPT
   * @param isFunFact whether the response is a fun fact
   */
  private void replaceLoadingMessageWithResponse(String response, boolean isFunFact) {
    String loadingMessage = "Ghost is Writing...";
    // Iterate through the children of chatTextFlow to find the last occurrence of the loading
    // message
    for (int i = chatTextFlow.getChildren().size() - 1; i >= 0; i--) {
      Node child = chatTextFlow.getChildren().get(i);
      if (child instanceof Text) {
        Text text = (Text) child;
        text.setFill(Color.WHITE);
        String content = text.getText();

        int lastLoadingIndex = content.lastIndexOf(loadingMessage);

        // If "Ghost is Writing..." is found, replace it with the GPT response
        if (lastLoadingIndex != -1) {
          String newText =
              response.equals("")
                  ? ""
                  : (!isFunFact ? "Ghost: " + response : "FUN FACT: " + response);
          text.setText(content.substring(0, lastLoadingIndex) + newText + "\n\n");
          break; // Stop after replacing the last occurrence
        }
      }
    }
  }

  /**
   * Changes the chat and sends a message.
   *
   * @param chat the chat to change to
   * @param state the state to change to
   */
  public void changeChatAndSend(ChatCompletionRequest chat, String state) {
    chatCompletionRequest = chat;
    try {
      if (state.equals("state2")) {
        runGpt(new ChatMessage("user", GptPromptEngineering.apiNoHints(state)), "normal", true);
      } else {
        runGpt(new ChatMessage("user", GptPromptEngineering.apiNoHints(state)), "normal", false);
      }
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
  }

  /** Updates the hint counter. It is called when the hint button is pressed. */
  private void updateHintCounter() {
    if (GameState.numberOfHints < 0) {
      hintLabel.setText("Hints: ∞");
    } else {
      hintLabel.setText("Hints: " + GameState.numberOfHints);
    }
  }

  /** Handles the hint button being clicked. */
  @FXML
  public void hintClicked() {
    updateGameAndGuiHints();
    // GPT part of hint clicked
    if (hintChatCompletionRequest.getMessages().size() < 2) {
      // Run with prompt
      try {
        if (GameState.currentState != "state5") {
          runGpt(
              new ChatMessage("user", GptPromptEngineering.apiGetHints(GameState.currentState)),
              "hints",
              false);
        } else {
          runGpt(
              new ChatMessage("user", GptPromptEngineering.apiGetHints(GameState.currentState)),
              "digit",
              false);
        }
      } catch (ApiProxyException e) {
        e.printStackTrace();
      }
    } else {
      // Run with user
      try {
        if (GameState.currentState != "state5") {
          runGpt(new ChatMessage("user", "another hint"), "hints", false);
        } else {
          runGpt(new ChatMessage("user", "another digit"), "digit", false);
        }
      } catch (ApiProxyException e) {
        e.printStackTrace();
      }
    }
  }

  public void newStateHint() {
    hintChatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(.6).setTopP(1).setMaxTokens(100);
  }

  /** Method that returns the fact. */
  public void sayFact() {
    try {
      runGpt(new ChatMessage("user", GptPromptEngineering.getFunFact()), "fact", false);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void updateGameAndGuiHints() {
    // GUI aspects of hint being clicked Going to be extracted to new funciton
    GameState.numberOfHints--;
    GameState.lockerController.updateHintButton();
    updateHintCounter();
    if (GameState.numberOfHints == 0) {
      hintButton.setDisable(true);
    }
  }
}
