package com.psp.flappybird;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FlappyBird extends Application {

  private static final String TITLE = "Flappy Bird";
  static final int SCREEN_WIDTH = 485;
  static final int SCREEN_HEIGHT = 645;
  private boolean gameStarted = false;

  private static final double KEY_FRAME_DURATION = 11;

  private static final int FIRST_PIPE_CORD_X = 0;
  private static final int SECOND_PIPE_CORD_X = 300;
  private static final int FIRST_FLOOR_CORD_X = 0;
  private static final int SECOND_FLOOR_CORD_X = 500;

  private Pipe pipe1;
  private Pipe pipe2;
  private Floor floor1;
  private Floor floor2;
  private Bird bird;
  private Score score;

  @Override
  public void start(Stage primaryStage) {

    primaryStage.setTitle(TITLE);
    Pane root = new Pane();

    createGameObjects(root);

    final int[] frameCounter = {0};

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(KEY_FRAME_DURATION), event -> {
      if (gameStarted) {
        updateGameObjects(floor1, floor2, pipe1, pipe2, bird, score, frameCounter);
        frameCounter[0]++;
      }

    }));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();

    Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
    primaryStage.setScene(scene);

    defineGameControls(scene, bird, frameCounter);

    primaryStage.show();
  }

  private void createGameObjects(Pane root) {
    createBackground(root);
    createPipes(root);
    createFloor(root);
    createBird(root);
    createScore(root);
  }

  private void updateGameObjects(Floor floor1, Floor floor2, Pipe pipe1, Pipe pipe2, Bird bird,
      Score score,
      int[] frameCounter) {
    updatePipes(pipe1, pipe2, frameCounter[0]);
    updateFloor(floor1, floor2, frameCounter[0]);
    updateBird(bird, frameCounter[0]);
    updateScore(score, pipe1, pipe2, bird);

    endGameIfBirdCrashed(bird, pipe1, pipe2);
  }


  private void resetGame() {
    gameStarted = false;
    bird.reset(0);
    pipe1.reset(FIRST_PIPE_CORD_X);
    pipe2.reset(SECOND_PIPE_CORD_X);
    floor1.reset(FIRST_FLOOR_CORD_X);
    floor2.reset(SECOND_FLOOR_CORD_X);
    score.reset(0);
  }

  private static void createBackground(Pane root) {
    Image backgroundImage = new Image("images/background_cropped.png");

    BackgroundSize backgroundSize = new BackgroundSize(SCREEN_WIDTH, SCREEN_HEIGHT, false, false,
        false, false);

    BackgroundImage backgroundConfig = new BackgroundImage(backgroundImage,
        BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
    root.setBackground(new Background(backgroundConfig));
  }

  private void createPipes(Pane root) {
    Image topPipeImage = new Image("images/down_pipe_cropped.png");
    Image bottomPipeImage = new Image("images/up_pipe_cropped.png");

    pipe1 = new Pipe(topPipeImage, bottomPipeImage, SCREEN_WIDTH);
    pipe2 = new Pipe(topPipeImage, bottomPipeImage, SCREEN_WIDTH + SECOND_PIPE_CORD_X);

    pipe1.addToPane(root);
    pipe2.addToPane(root);
  }

  private void createFloor(Pane root) {
    Image floorImage = new Image("images/floor_cropped_v2.png");

    floor1 = new Floor(floorImage, FIRST_FLOOR_CORD_X);
    floor2 = new Floor(floorImage, SECOND_FLOOR_CORD_X);

    floor1.addToPane(root);
    floor2.addToPane(root);
  }

  private void createBird(Pane root) {
    bird = new Bird();
    bird.addToPane(root);
  }

  private void createScore(Pane root) {
    score = new Score();
    score.addToPane(root);
  }

  private void defineGameControls(Scene scene, Bird bird, int[] frameCounter) {
    scene.setOnMouseClicked(event -> {
      if (!gameStarted) {
        gameStarted = true;
      }
      bird.flap(frameCounter[0]);
    });

    scene.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.SPACE && !gameStarted) {
        gameStarted = true;
      }
      bird.flap(frameCounter[0]);
    });
  }

  private static void updateBird(Bird bird, int frameCounter) {
    bird.update(frameCounter);
  }

  private static void updatePipes(Pipe pipe1, Pipe pipe2, int frameCounter) {
    pipe1.update(frameCounter);
    pipe2.update(frameCounter);

    resetPipeIfOutOfScreen(pipe1);
    resetPipeIfOutOfScreen(pipe2);
  }

  private static void updateFloor(Floor floor1, Floor floor2, int frameCounter) {
    floor1.update(frameCounter);
    floor2.update(frameCounter);

    resetFloorIfOutOfScreen(floor1, floor2);
    resetFloorIfOutOfScreen(floor2, floor1);
  }

  private void updateScore(Score score, Pipe pipe1, Pipe pipe2, Bird bird) {
    double birdX = bird.getBird().getX() + Bird.BIRD_WIDTH;
    double pipe1X = pipe1.getTopPipe().getX();
    double pipe2X = pipe2.getTopPipe().getX();
    score.checkAndIncrementScore(birdX, pipe1X, pipe2X);
  }

  private void endGameIfBirdCrashed(Bird bird, Pipe pipe1, Pipe pipe2) {
    double originalBirdRotation = bird.getBird().getRotate();
    bird.getBird().setRotate(0);

    if (birdHitPipe(bird, pipe1, pipe2) || birdHitFloor(bird)) {
      resetGame();
    }
    bird.getBird().setRotate(originalBirdRotation);
  }

  private static boolean birdHitPipe(Bird bird, Pipe pipe1, Pipe pipe2) {
    return bird.getBird().getBoundsInParent().intersects(pipe1.getTopPipe().getBoundsInParent()) ||
        bird.getBird().getBoundsInParent().intersects(pipe1.getBottomPipe().getBoundsInParent()) ||
        bird.getBird().getBoundsInParent().intersects(pipe2.getTopPipe().getBoundsInParent()) ||
        bird.getBird().getBoundsInParent().intersects(pipe2.getBottomPipe().getBoundsInParent());
  }

  private boolean birdHitFloor(Bird bird) {
    return bird.getBird().getY() + bird.getBird().getFitHeight() >= Floor.FLOOR_Y_CORD;
  }

  private static void resetFloorIfOutOfScreen(Floor floor1, Floor floor2) {
    if (floor1.isFloorOffScreen()) {
      floor1.reset(floor2.getFloor().getX() + floor1.getFloor().getImage().getWidth());
    }
  }

  private static void resetPipeIfOutOfScreen(Pipe pipe) {
    if (pipe.isPipeOffScreen()) {
      pipe.reset(0);
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
