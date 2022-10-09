package nz.ac.auckland.se206;

import java.net.URISyntaxException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {

  private static MediaPlayer buttonHoverSound = null;
  private static MediaPlayer backgroundMusic = null;
  private static MediaPlayer buttonClickSound = null;
  private static MediaPlayer timerTickSound = null;
  private static MediaPlayer timerTickSoundFast = null;
  private static MediaPlayer alarmBellSound = null;
  private static MediaPlayer winSound = null;

  private static final String BACKGROUND_MUSIC = "/sounds/backgroundMusic1.mp3";
  private static final String BUTTON_HOVER_SOUND = "/sounds/buttonHoverSound.wav";
  private static final String BUTTON_CLICK_SOUND = "/sounds/buttonClickSound.wav";
  private static final String TIMER_TICK_SOUND = "/sounds/timerTickSound.wav";
  private static final String TIMER_TICK_SOUND_FAST = "/sounds/timerTickSoundFast.mp3";
  private static final String ALARM_BELL_SOUND = "/sounds/alarmBellSound.wav";
  private static final String WIN_SOUND = "/sounds/winSound.wav";

  /** This method plays the background music */
  public static void playBackgroundMusic() {
    if (backgroundMusic == null) {
      // initialise background music player if not initialised
      try {
        backgroundMusic =
            new MediaPlayer(
                new Media(SoundManager.class.getResource(BACKGROUND_MUSIC).toURI().toString()));
        backgroundMusic.setVolume(0.2);
        backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
    }
    // play from the start
    backgroundMusic.seek(backgroundMusic.getStartTime());
    backgroundMusic.play();
  }

  /** this method toggles the background music on and off */
  public static void toggleBackgroundMusic() {
    if (backgroundMusic != null) {
      // pauses if media player is playing, otherwise plays it
      if (backgroundMusic.getStatus() == MediaPlayer.Status.PLAYING) {
        backgroundMusic.pause();
      } else {
        playBackgroundMusic();
      }
    }
  }

  /** This method plays the button hover sound effect */
  public static void playButtonHover() {
    if (buttonHoverSound == null) {
      try {
        buttonHoverSound =
            new MediaPlayer(
                new Media(SoundManager.class.getResource(BUTTON_HOVER_SOUND).toURI().toString()));
        buttonHoverSound.setVolume(0.2);
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
    }
    // play from the start
    buttonHoverSound.seek(buttonHoverSound.getStartTime());
    buttonHoverSound.play();
  }

  /** This method plays the button click sound effect */
  public static void playButtonClick() {
    if (buttonClickSound == null) {
      try {
        buttonClickSound =
            new MediaPlayer(
                new Media(SoundManager.class.getResource(BUTTON_CLICK_SOUND).toURI().toString()));
        buttonClickSound.setVolume(0.2);
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
    }
    // play from the start
    buttonClickSound.seek(buttonClickSound.getStartTime());
    buttonClickSound.play();
  }

  /** This method plays the timer tick sound effect */
  public static void playTimerTick() {
    if (timerTickSound == null) {
      try {
        timerTickSound =
            new MediaPlayer(
                new Media(SoundManager.class.getResource(TIMER_TICK_SOUND).toURI().toString()));
        timerTickSound.setVolume(0.1);
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
    }
    // play from the start
    timerTickSound.seek(timerTickSound.getStartTime());
    timerTickSound.play();
  }

  /** This method plays the timer tick fast sound effect */
  public static void playTimerTickFast() {
    if (timerTickSoundFast == null) {
      try {
        timerTickSoundFast =
            new MediaPlayer(
                new Media(
                    SoundManager.class.getResource(TIMER_TICK_SOUND_FAST).toURI().toString()));
        timerTickSoundFast.setVolume(1.0);
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
    }
    // play from the start
    timerTickSoundFast.seek(timerTickSoundFast.getStartTime());
    timerTickSoundFast.play();
  }

  /** This method plays the alarm bell sound effect */
  public static void playAlarmBell() {
    if (alarmBellSound == null) {
      try {
        alarmBellSound =
            new MediaPlayer(
                new Media(SoundManager.class.getResource(ALARM_BELL_SOUND).toURI().toString()));
        alarmBellSound.setVolume(1.0);
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
    }
    // play from the start
    alarmBellSound.seek(alarmBellSound.getStartTime());
    alarmBellSound.play();
  }

  /** This method plays the alarm bell sound effect */
  public static void playWinSound() {
    if (winSound == null) {
      try {
        winSound =
            new MediaPlayer(
                new Media(SoundManager.class.getResource(WIN_SOUND).toURI().toString()));
        winSound.setVolume(0.4);
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
    }
    // play from the start
    winSound.seek(winSound.getStartTime());
    winSound.play();
  }
}
