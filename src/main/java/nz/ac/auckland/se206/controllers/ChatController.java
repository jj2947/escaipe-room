package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
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
  @FXML private Button sendButton;
  @FXML private Button goBackButton;
  @FXML private Label timerLabel;
  private Timer timer;

  private ChatCompletionRequest chatCompletionRequest;
  private ChatMessage chatMsg;

  /**
   * Initializes the chat view, loading the riddle.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(100);
    runGpt(new ChatMessage("user", GptPromptEngineering.getRiddleWithGivenWord("vase")));

    timer = GameState.timer;
    updateTimer();
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    chatTextArea.appendText(msg.getRole() + ": " + msg.getContent() + "\n\n");
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {

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
                    // Enabling Buttons after API has finished loading
                    sendButton.setDisable(false);
                    goBackButton.setDisable(false);
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
    // Disabling Buttons while API is loading
    sendButton.setDisable(true);
    goBackButton.setDisable(true);
    inputText.setDisable(true);
    return chatMsg;
  }

  /**
   * Sends a message to the GPT model.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    String message = inputText.getText();
    if (message.trim().isEmpty()) {
      return;
    }
    inputText.clear();
    ChatMessage msg = new ChatMessage("user", message);
    appendChatMessage(msg);
    runGpt(msg);
  }

  /**
   * Navigates back to the previous view.
   *
   * @param event the action event triggered by the go back button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    // Switching Scenes to the room
    // Needs to be changed later on so that it goes back to the most recent room
    try {
      sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.ROOM));
    } catch (Exception e) {
      e.printStackTrace();
    }
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
        onSendMessage(null);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void updateTimer() {
    // Update the timer label every second
    Task<Void> updateLabelTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            while (!GameState.isTimeReached) {
              Thread.sleep(1000); // Wait for 1 second
              Platform.runLater(
                  () ->
                      timerLabel.setText(
                          String.format(
                              "%02d:%02d", timer.getCounter() / 60, timer.getCounter() % 60)));
              ;
            }
            return null;
          }
        };

    // Create a new thread for the update task and start it
    Thread updateThread = new Thread(updateLabelTask);
    updateThread.setDaemon(true);
    updateThread.start();
  }
}
