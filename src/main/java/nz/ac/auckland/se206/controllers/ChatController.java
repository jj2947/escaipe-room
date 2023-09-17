package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
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
  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;
  @FXML private AnchorPane chatPane;

  private ChatCompletionRequest chatCompletionRequest;
  private ChatMessage chatMsg;
  private CountryChoice countryChooser = new CountryChoice();
  private String country;

  /**
   * Initializes the chat view, loading the riddle.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {
    GameState.chatController = this;
    country = countryChooser.chooseCountry();
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(100);
    runGpt(new ChatMessage("user", GptPromptEngineering.initRiddleAndMaster(country)));
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    chatTextArea.appendText(msg.getContent() + "\n\n");
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    if (GameState.isChatOpen) {
      responseLoading();
    }
    // GPT Task
    Task<Void> askGPT =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {
            chatCompletionRequest.addMessage(msg);
            try {
              ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
              Choice result = chatCompletionResult.getChoices().iterator().next();
              chatCompletionRequest.addMessage(result.getChatMessage());
              // The response from GPT-API
              chatMsg = result.getChatMessage();
              Platform.runLater(
                  () -> {
                    // After Message is recieved adding it to the chat box
                    appendChatMessage(result.getChatMessage());
                    // Checking to see if the riddle has been solved and changing the game state
                    if (result.getChatMessage().getRole().equals("assistant")
                        && result.getChatMessage().getContent().startsWith("Correct")) {
                      GameState.isRiddleResolved = true;
                    }
                    if (GameState.isChatOpen) {
                      responseLoaded();
                    }
                    inputText.setDisable(false);
                  });
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
              askGPT.run();
            });
    gptThread.start();
    inputText.setDisable(true);
    return chatMsg;
  }

  @FXML
  private void onEnterPressed(KeyEvent event) throws ApiProxyException, IOException {
    if (event.getCode().toString().equals("ENTER")) {
      String message = inputText.getText();
      if (message.trim().isEmpty()) {
        return;
      }
      inputText.clear();
      ChatMessage msg = new ChatMessage("user", message);
      appendChatMessage(msg);
      runGpt(msg);
    }
  }

  public void closeChat() {
    Stage stage = (Stage) chatPane.getScene().getWindow();
    stage.setWidth(1100);
    stage.centerOnScreen();
  }

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

  private void responseLoaded() {
    if (GameState.chatInGym) {
      GameState.gymController.responseLoaded();
    } else if (GameState.chatInRoom) {
      GameState.roomController.responseLoaded();
    } else if (GameState.chatInLocker) {
      GameState.lockerController.responseLoaded();
    } else if (GameState.chatInHall) {
      GameState.hallController.responseLoaded();
    }
  }

  private void responseLoading() {
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
}
