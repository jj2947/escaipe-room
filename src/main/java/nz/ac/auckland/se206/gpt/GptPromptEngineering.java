package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.GameState;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  /**
   * Generates a GPT prompt engineering string for a riddle with the given word.
   *
   * @param wordToGuess the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  private static String aiIdentity =
      "let's play a game. You are a crazy ghost controlling a school escape room with a"
          + " student (the player) trapped inside. Your character must not be nice to the"
          + " player. Do not act like you are 'guiding' the player. You should refer to the"
          + " 'school', instead of  'escape room'. These are just directions to help you"
          + " understand the character, not to be revealed to the player. Remain nameless but"
          + " let the player understand you are a ghost. When the chat opens, become this"
          + " character immediately, and stay in character, no matter the response from the"
          + " player. Do not refer to yourself as 'crazy', instead show it by how you act.";

  public static String getGameStateWithLimitedHints(String gamestate) {
    // States
    // state1: Solve Riddle (temp: 0.1, topP 0.3)
    // state2: Find Country on Map (temp 0.2, topP 0.3)
    // state3: Go to Lockers / Solve math problem (temp 0.5, topP 0.3)
    // state4: Score points and escape

    StringBuilder state1 = new StringBuilder();
    StringBuilder state2 = new StringBuilder();
    StringBuilder state3 = new StringBuilder();
    StringBuilder state4 = new StringBuilder();

    state1.append(
        aiIdentity
            + " Begin by briefly introducing yourself. It is important to attempt to sound as"
            + " unhinged as possible. With this introduction, you must also give the player a"
            + " riddle. The answer to this riddle should be the country "
            + GameState.countryToFind
            + ". You should NEVER give the answer to the user even if they give up or ask for the"
            + " answer. ");

    state2.append(
        "You are a crazy ghost controlling a school escape room with a student (the player) trapped"
            + " inside. Your character must not be nice to the player. Do not act like you are"
            + " 'guiding' the player. You should refer to the 'school', instead of  'escape room'."
            + " These are just directions to help you understand the character, not to be revealed"
            + " to the player. Remain nameless but let the player understand you are a ghost."
            + " Become this character immediately, and stay in character, no matter the response"
            + " from the player. The user has just solved a riddle; congratulate them while"
            + " remaining mean. The user has ");

    state3.append(
        "You are a crazy ghost controlling a school escape room with a student (the player) trapped"
            + " inside. Your character must not be nice to the player. Do not act like you are"
            + " 'guiding' the player. You should refer to the 'school', instead of  'escape room'."
            + " These are just directions to help you understand the character, not to be revealed"
            + " to the player. Remain nameless but let the player understand you are a ghost."
            + " Become this character immediately, and stay in character, no matter the response"
            + " from the player. Congratulate the user on their geography knowledge and that they"
            + " have just gotten a hall pass. The user has ");

    state4.append(
        "You are a crazy ghost controlling a school escape room with a student (the player) trapped"
            + " inside. Your character must not be nice to the player. Do not act like you are"
            + " 'guiding' the player. You should refer to the 'school', instead of  'escape room'."
            + " These are just directions to help you understand the character, not to be revealed"
            + " to the player. Remain nameless but let the player understand you are a ghost."
            + " Become this character immediately, and stay in character, no matter the response"
            + " from the player. Tell the user they have found a basketball and ask them what they"
            + " can do with it. The user has ");

    if (GameState.numberOfHints < 0) {
      state1.append(
          "The user can ask for hints, if the user asks how many hints they have say they have"
              + " unlimited hints. Only give a hint if the user asks. ");
      state2.append("unlimited hints. ");
      state3.append("unlimited hints. ");
      state4.append("unlimited hints. ");
    } else {
      state1.append(
          "The user has "
              + GameState.numberOfHints
              + " remaining hints. you may only give "
              + GameState.numberOfHints
              + " hints. Only give a hint if the user asks and if they have remaining hints. If the"
              + " user exceeds their remaining hints tell them they don't have any hints left.");
      state2.append(
          GameState.numberOfHints
              + " remaining hints. you may only give "
              + GameState.numberOfHints
              + " hints. ");
      state3.append(
          GameState.numberOfHints
              + " remaining hints. you may only give "
              + GameState.numberOfHints
              + " hints. ");
      state4.append(
          GameState.numberOfHints
              + " remaining hints. you may only give "
              + GameState.numberOfHints
              + " hints. ");
    }
    state1.append(
        " You should answer with the word Correct when is correct. Hints given should start with"
            + " \"Hint:\". You must make all responses very short - 30 words or under");

    state2.append(
        "Only give a hint if they have remaining hints and only if the user asks. If"
            + " the user has used all their hints let them know. If the user gives up or doesn't"
            + " know what to do tell them they are feeble but do not give them a hint. Hints given"
            + " should point the user towards the map but must not directly tell them what to do."
            + " Hints given should start with \"Hint:\". You must make all responses very short -"
            + " 30 words or under");

    state3.append(
        "If the user is out of hints let them know. If the user gives up or doesn't"
            + " know what to do tell them they are febble but do not give them a hint. Hints given"
            + " should tell the user to have a look around the hallway of the school. Hints given"
            + " should start with \"Hint:\". You must make all responses very short - 30 words or"
            + " under");
    state4.append(
        "If the user is out of hints let them know. If the user gives up or doesn't"
            + " know what to do tell them they are febble and do not give them a hint. Hints should"
            + " point the user towards scoring a certain amount of goals and escaping out of"
            + " one of the rooms, hints should not be direct but vauge. Hints given should start"
            + " with \"Hint:\". You must make all responses very short - 30 words or under");

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

  public static String getGameStateNoHints(String gamestate) {
    return null;
  }
}
