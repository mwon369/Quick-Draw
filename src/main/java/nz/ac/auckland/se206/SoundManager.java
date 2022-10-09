package nz.ac.auckland.se206;

import java.net.URISyntaxException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {

  private static MediaPlayer buttonHoverSound = null;
  private static MediaPlayer backgroundMusic = null;

  private static final String BACKGROUND_MUSIC = "/sounds/backgroundMusic1.mp3";
  private static final String BUTTON_HOVER_SOUND = "/sounds/buttonHoverSound.wav";

  public static void playBackgroundMusic() {
    if (backgroundMusic == null) {
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
    backgroundMusic.seek(backgroundMusic.getStartTime());
    backgroundMusic.play();
  }

  public static void onToggleMusic() {
    if (backgroundMusic != null) {
      if (backgroundMusic.getStatus() == MediaPlayer.Status.PLAYING) {
        backgroundMusic.pause();
      } else {
        playBackgroundMusic();
      }
    }
  }

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
    buttonHoverSound.seek(buttonHoverSound.getStartTime());
    buttonHoverSound.play();
  }
}
