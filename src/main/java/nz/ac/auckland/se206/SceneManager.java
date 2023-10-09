package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;

/** This class is used to manage the different scenes in the game. */
public class SceneManager {

  /** An enum that represents the different scenes in the game. */
  public enum AppUi {
    CHAT,
    ROOM,
    END,
    STARTSCREEN,
    HALLWAY,
    GYMNASIUM,
    LOCKER,
    BLACKBOARD
  }

  private static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();

  public static void addUi(AppUi appUi, Parent uiRoot) {
    sceneMap.put(appUi, uiRoot);
  }

  public static Parent getUiRoot(AppUi appUi) {
    return sceneMap.get(appUi);
  }

  public static void removeUi(AppUi appUi) {
    sceneMap.remove(appUi);
  }
}
