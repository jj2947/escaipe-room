package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.animation.PathTransition;
import javafx.scene.effect.Shadow;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Path;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import nz.ac.auckland.se206.controllers.BlackboardController;
import nz.ac.auckland.se206.controllers.ChatController;
import nz.ac.auckland.se206.controllers.GymnasiumController;
import nz.ac.auckland.se206.controllers.HallwayController;
import nz.ac.auckland.se206.controllers.LockerController;
import nz.ac.auckland.se206.controllers.RoomController;
import nz.ac.auckland.se206.controllers.Timer;

/** Represents the state of the game. */
public class GameState {

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether time is up. */
  public static boolean isTimeReached = false;

  /** Represents number of hints left, dependant on used and difficulty. */
  public static int numberOfHints;

  public static int totalTime;

  public static String countryToFind;

  public static String difficulty;

  public static boolean countryIsFound = false;

  public static boolean basketballCollected = false;

  public static Timer timer;

  public static ChatController chatController;

  public static HallwayController hallController;

  public static RoomController roomController;

  public static GymnasiumController gymController;

  public static LockerController lockerController;

  public static BlackboardController blackboardController;

  public static String currentState = "state1";

  public static boolean chatInRoom = false;

  public static boolean chatInHall = false;

  public static boolean chatInLocker = false;

  public static boolean chatInGym = false;

  public static boolean isChatOpen = false;

  public static List<Integer> numbersToFind = new ArrayList<>();

  public static Set<Integer> numberSet = new HashSet<>();

  public static Integer pinAnswer;

  public static boolean userWins = false;

  public static boolean isGhostTalking = false;

  public static TextFlow textFlow;

  public static boolean isMuted = false;

  /** Sets the new numbers to find. */
  public static void setNewNumbers() {
    // Generating random numbers to find
    int randomNum1 = (int) Math.floor(Math.random() * (7 - 1 + 1) + 1);
    int randomNum2 = (int) Math.floor(Math.random() * (15 - 10 + 1) + 10);
    System.out.println(randomNum1);
    System.out.println(randomNum2);
    // Adding the numbers to find to the list and the set array
    GameState.numbersToFind.add(randomNum1 * 3);
    GameState.numbersToFind.add(randomNum2 * 3);
    GameState.numberSet.add(randomNum1 * 3);
    GameState.numberSet.add(randomNum2 * 3);
    GameState.numberSet.add(24);
  }

  /**
   * Moves the ghost along the path.
   *
   * @param ghost The ghost to move
   * @param path The path to move the ghost along
   * @param playForward Whether to play the animation forward or not
   * @param shadow The shadow effect to apply to the ghost
   * @return Whether the animation is playing forward or not
   */
  public static boolean moveGhost(ImageView ghost, Path path, boolean playForward, Shadow shadow) {
    ghost.setEffect(shadow);
    PathTransition pathTransition = new PathTransition();
    double width = ghost.getLayoutBounds().getWidth();
    double height = ghost.getLayoutBounds().getHeight();

    path.setLayoutX(-ghost.getLayoutX() + width / 2);
    path.setLayoutY(-ghost.getLayoutY() + height / 2);

    // Set the path and node for the PathTransition
    pathTransition.setPath(path);
    pathTransition.setNode(ghost);

    // Set the duration of the animation (4000 milliseconds)
    pathTransition.setDuration(Duration.millis(4000));

    // Set the cycle count to 1 (plays the animation once)
    pathTransition.setCycleCount(1);

    // Toggle the animation direction if playForward is false
    if (!playForward) {
      pathTransition.setRate(-1); // Reverse direction
    }

    // Set the action to be performed when the animation finishes
    pathTransition.setOnFinished(
        event -> {
          ghost.setEffect(null); // Set the shadow effect to null
        });

    // Play the animation
    pathTransition.play();

    // Toggle the playForward flag for the next call
    playForward = !playForward;
    return playForward;
  }

  /** Mutes or unmutes the game. */
  public static void clickSpeaker() {
    if (GameState.isMuted) { // Unmutes the game
      System.out.println("Unmuted");
      GameState.isMuted = false;
      hallController.getLine().setVisible(false);
      lockerController.getLine().setVisible(false);
      gymController.getLine().setVisible(false);
      roomController.getLine().setVisible(false);
    } else { // Mutes the game
      System.out.println("Muted");
      GameState.isMuted = true;
      hallController.getLine().setVisible(true);
      lockerController.getLine().setVisible(true);
      gymController.getLine().setVisible(true);
      roomController.getLine().setVisible(true);
    }
  }
}
