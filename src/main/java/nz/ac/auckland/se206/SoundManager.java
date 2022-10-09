package nz.ac.auckland.se206;

import java.net.URISyntaxException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {

  private static MediaPlayer buttonHoverSound = null;
  private static MediaPlayer backgroundMusic = null;
  private static MediaPlayer buttonClickSound = null;

  private static final String BACKGROUND_MUSIC = "/sounds/backgroundMusic1.mp3";
  private static final String BUTTON_HOVER_SOUND = "/sounds/buttonHoverSound.wav";
  private static final String BUTTON_CLICK_SOUND = "/sounds/buttonClickSound.wav";

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
  public static void onButtonHover() {
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
  public static void onButtonClick() {
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
}
