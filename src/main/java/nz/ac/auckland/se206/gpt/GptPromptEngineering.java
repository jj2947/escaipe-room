package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.GameState;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {
  private static String aiIdentity =
      "You are a crazy ghost controlling a school escape room with a student (the player) trapped"
          + " inside. Your character must not be nice to the player. Do not act like you are"
          + " 'guiding' the player. You should refer to the 'school' instead of  'escape room'."
          + " (The school has three rooms, classroom, hallway and gymnasium). These are just"
          + " directions to help you understand the character, not to be revealed to the player."
          + " Let the player understand you are a ghost. Become this character immediately, and"
          + " stay in character, no matter the response from the player. ";

  /**
   * Generates a prompt for the GPT-3 API to use when the user has used all their hints.
   *
   * @param gamestate the current gamestate
   * @return a prompt for the GPT-3 API to use
   */
  public static String apiNoHints(String gamestate) {
    // States
    // state1: Solve Riddle
    // state2: Find Country on Map
    // state3: Go to Lockers / Solve math problem
    // state4: Score points and escape

    StringBuilder state1 = new StringBuilder();
    StringBuilder state2 = new StringBuilder();
    StringBuilder state3 = new StringBuilder();
    StringBuilder state4 = new StringBuilder();

    state1.append(
        aiIdentity
            + "Begin by briefly introducing yourself. With this introduction, you must also give"
            + " the player a riddle. The answer to this riddle should be the country "
            + GameState.countryToFind
            + ". You should NEVER give the answer to the user, even if they give up or ask for the"
            + " answer. ");

    state2.append(
        aiIdentity
            + "The user has just solved your riddle; respond with bitterness. If the user gives up"
            + " or doesn't know what to do, belittle them. ");

    state3.append(
        aiIdentity
            + "The user has just solved the first puzzle and gotten a hall pass. Respond in"
            + " bitterness and ask them what they can do with the hall pass now. If the user gives"
            + " up don't let them.");

    state4.append(
        aiIdentity
            + "The user has just found a basketball; inform the user of this and taunt them. If the"
            + " user asks for a hint, don't give the user any. If the user gives up don't let"
            + " them.");

    if (GameState.difficulty.equals("EASY")) {
      state1.append("Only if the user asks for a hint, point them to the hint button. ");
      state2.append("Only if the user asks for a hint, point them to the hint button. ");
      state3.append("Only if the user asks for a hint, point them to the hint button. ");
      state4.append("Only if the user asks for a hint, point them to the hint button. ");
      System.out.println("EASY");
    } else if (GameState.difficulty.equals("MEDIUM")) {
      state1.append(
          "Only if the user asks for a hint, point them towards the hint button and remind them"
              + " that hints are limited");
      state2.append(
          "Only if the user asks for a hint, point them towards the hint button and remind them"
              + " that hints are limited");
      state3.append(
          "Only if the user asks for a hint, point them towards the hint button and remind them"
              + " that hints are limited");
      state4.append(
          "Only if the user asks for a hint, point them towards the hint button and remind them"
              + " that hints are limited");
      System.out.println("MEDIUM");
    } else {
      state1.append("If the user asks for a hint, tell them that you will not give any hints.");
      state2.append("If the user asks for a hint, tell them that you will not give any hints.");
      state3.append("If the user asks for a hint, tell them that you will not give any hints.");
      state4.append("If the user asks for a hint, tell them that you will not give any hints.");
      System.out.println("HARD");
    }

    state1.append(
        "You should answer with the word Correct when is correct. You must make"
            + " all responses very short - 30 words or under");
    state2.append(" You must make all responses very short - 30 words or under");
    state3.append(" You must make all responses very short - 30 words or under");
    state4.append(" You must make all responses very short - 30 words or under");

    if (gamestate.equals("state1")) {
      return state1.toString();
    } else if (gamestate.equals("state2")) {
      return state2.toString();
    } else if (gamestate.equals("state3")) {
      return state3.toString();
    } else if (gamestate.equals("state4")) {
      return state4.toString();
    }
    return null;
  }

  /**
   * Generates a prompt for the GPT-3 API to use when the user has used all their hints.
   *
   * @param gamestate the current gamestate
   * @return a prompt for the GPT-3 API to use
   */
  public static String apiGetHints(String gamestate) {
    // Create states
    StringBuilder state1 = new StringBuilder();
    StringBuilder state2 = new StringBuilder();
    StringBuilder state3 = new StringBuilder();
    StringBuilder state4 = new StringBuilder();
    StringBuilder state5 = new StringBuilder();
    StringBuilder state6 = new StringBuilder();

    // Add ai prompts for each state
    state1.append(
        "Give a hint to a riddle, where the answer is "
            + GameState.countryToFind
            + " in 15 words or less");
    state2.append(
        "Give a hint that points to searching around a classroom and finding a country on a map in"
            + " 15 words or less");
    state3.append(
        "Give a hint that points to searching around the school and using their hallpass to unlock"
            + " something previously locked in 15 words or less.");
    state4.append(
        "Give a hint that points to getting a basketball to go to the gym and shoot hoops in 15"
            + " words or less");
    state5.append(
        "Give a hint to the value "
            + GameState.pinAnswer
            + ", by providing a digit from the value");
    state6.append(
        "Give a hint that points to leaving the gym through a door under red lights in 15 words or"
            + " less");
    // Return the correct state
    if (gamestate.equals("state1")) {
      return state1.toString();
    } else if (gamestate.equals("state2")) {
      return state2.toString();
    } else if (gamestate.equals("state3")) {
      return state3.toString();
    } else if (gamestate.equals("state4")) {
      return state4.toString();
    } else if (gamestate.equals("state5")) {
      return state5.toString();
    } else if (gamestate.equals("state6")) {
      return state6.toString();
    }
    return null;
  }

  public static String getFunFact() {
    return "Give a fun fact about " + GameState.countryToFind + " in less than 20 words";
  }
}
