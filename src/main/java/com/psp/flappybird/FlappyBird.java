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

  static final int SCREEN_WIDTH = 485;
  static final int SCREEN_HEIGHT = 645;
  private static final String TITLE = "Flappy Bird";
  private static final int X_PIPE_SPACING = 300;
  private boolean gameStarted = false;

  private Pipe pipe1;
  private Pipe pipe2;
  private Floor floor1;
  private Floor floor2;
  private Image floorImage;
  private Bird bird;


  @Override
  public void start(Stage primaryStage) {

    primaryStage.setTitle(TITLE);
    Pane root = new Pane();

    createGameObjects(root);

    final int[] frameCounter = {0};

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(11), event -> {
      if (gameStarted) {
        updateGameObjects(floor1, floor2, floorImage, pipe1, pipe2, bird, frameCounter);
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
  }

  private void updateGameObjects(Floor floor1, Floor floor2, Image floorImage, Pipe pipe1,
      Pipe pipe2,
      Bird bird, int[] frameCounter) {
    updatePipes(pipe1, pipe2);
    updateFloor(floor1, floor2, floorImage);
    updateBird(bird, frameCounter);
    endGameIfBirdCrashed(bird, pipe1, pipe2);
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
    pipe2 = new Pipe(topPipeImage, bottomPipeImage, SCREEN_WIDTH + X_PIPE_SPACING);

    root.getChildren().addAll(pipe1.getTopPipe(), pipe1.getBottomPipe(), pipe2.getTopPipe(),
        pipe2.getBottomPipe());
  }

  private void createFloor(Pane root) {
    floorImage = new Image("images/floor_cropped_v2.png");

    floor1 = new Floor(floorImage, 0);
    floor2 = new Floor(floorImage, floor1.getFloor().getX() + floorImage.getWidth());

    root.getChildren().addAll(floor1.getFloor(), floor2.getFloor());
  }

  private void createBird(Pane root) {
    bird = new Bird();
    root.getChildren().add(bird.getBird());
  }

  private static void updateBird(Bird bird, int[] frameCounter) {
    bird.move();
    bird.updateBirdFlapAnimation(frameCounter[0]);
    bird.updateBirdRotationAnimation(frameCounter[0]);
  }

  private static void updatePipes(Pipe pipe1, Pipe pipe2) {
    pipe1.move();
    pipe2.move();

    resetPipeIfOutOfScreen(pipe1);
    resetPipeIfOutOfScreen(pipe2);
  }

  private static void updateFloor(Floor floor1, Floor floor2, Image floorImage) {
    floor1.move();
    floor2.move();

    resetFloorIfOutOfScreen(floor1, floor2, floorImage);
    resetFloorIfOutOfScreen(floor2, floor1, floorImage);
  }

  private void endGameIfBirdCrashed(Bird bird, Pipe pipe1, Pipe pipe2) {
    double originalBirdRotation = bird.getBird().getRotate();
    bird.getBird().setRotate(0);

    if (birdHitPipe(bird, pipe1, pipe2) || birdHitFloor(bird)) {
      onBirdCrash(bird, pipe1, pipe2);
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
    return bird.getBird().getY() + bird.getBird().getFitHeight() >= Floor.FLOOR_Y;
  }

  private void onBirdCrash(Bird bird, Pipe pipe1, Pipe pipe2) {
    gameStarted = false;
    bird.resetPosition();
    pipe1.resetPipePosition(0);
    pipe2.resetPipePosition(X_PIPE_SPACING);
  }

  private static void resetFloorIfOutOfScreen(Floor floor1, Floor floor2, Image floorImage) {
    if (floor1.isFloorOffScreen()) {
      floor1.resetFloorPosition(floor2.getFloor().getX() + floorImage.getWidth());
    }
  }

  private static void resetPipeIfOutOfScreen(Pipe pipe) {
    if (pipe.isPipeOffScreen()) {
      pipe.resetPipePosition(0);
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
