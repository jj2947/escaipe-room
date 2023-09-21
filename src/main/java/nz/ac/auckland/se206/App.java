package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.Timer;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {

  private static Scene scene;

  public static void main(final String[] args) {
    launch();
  }

  public static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFxml(fxml));
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  public static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {

    // Timer for the whole game
    GameState.timer = new Timer();

    stage.setTitle("EscAIpe Room");
    Image image = new Image("/images/ghost.png");
    stage.getIcons().add(image);

    int randomNum1 = (int) Math.floor(Math.random() * (7 - 1 + 1) + 1);
    int randomNum2 = (int) Math.floor(Math.random() * (15 - 10 + 1) + 10);
    System.out.println(randomNum1);
    System.out.println(randomNum2);
    GameState.numbersToFind.add(randomNum1 * 3);
    GameState.numbersToFind.add(randomNum2 * 3);
    GameState.numberSet.add(randomNum1 * 3);
    GameState.numberSet.add(randomNum2 * 3);
    GameState.numberSet.add(24);

    // Thread to load the room
    // CHANCE OF ERROR IF THE USER STARTS CLICKING STUFF FAST
    Thread loadRoom =
        new Thread(
            () -> {
              try {
                SceneManager.addUi(AppUi.ROOM, App.loadFxml("room"));
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
    loadRoom.start();

    SceneManager.addUi(AppUi.BLACKBOARD, loadFxml("blackboard"));
    SceneManager.addUi(AppUi.HALLWAY, App.loadFxml("hallway"));
    SceneManager.addUi(AppUi.GYMNASIUM, App.loadFxml("gymnasium"));
    SceneManager.addUi(AppUi.STARTSCREEN, loadFxml("startScreen"));
    SceneManager.addUi(AppUi.LOCKER, App.loadFxml("locker"));

    scene = new Scene(SceneManager.getUiRoot(AppUi.STARTSCREEN));
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();
  }
}
