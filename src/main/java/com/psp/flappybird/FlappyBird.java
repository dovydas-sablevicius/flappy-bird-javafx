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


  @Override
  public void start(Stage primaryStage) {

    primaryStage.setTitle(TITLE);
    Pane root = new Pane();

    //-------------------Background-------------------\\
    createBackground(root);

    //-------------------Pipes-------------------\\
    Image topPipeImage = new Image("images/down_pipe_cropped.png");
    Image bottomPipeImage = new Image("images/up_pipe_cropped.png");

    Pipe pipe1 = new Pipe(topPipeImage, bottomPipeImage, SCREEN_WIDTH);
    Pipe pipe2 = new Pipe(topPipeImage, bottomPipeImage, SCREEN_WIDTH + X_PIPE_SPACING);

    root.getChildren().addAll(pipe1.getTopPipe(), pipe1.getBottomPipe());
    root.getChildren().addAll(pipe2.getTopPipe(), pipe2.getBottomPipe());

    //-------------------Floor-------------------\\
    Image floorImage = new Image("images/floor_cropped_v2.png");

    Floor floor1 = new Floor(floorImage, 0);
    Floor floor2 = new Floor(floorImage, floor1.getFloor().getX() + floorImage.getWidth());

    root.getChildren().addAll(floor1.getFloor(), floor2.getFloor());

    //-------------------Bird-------------------\\

    Bird bird = new Bird();

    root.getChildren().add(bird.getBird());

    final int[] frameCounter = {0};

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(11), event -> {
      if (gameStarted) {

        //-------------------Floor-------------------\\
        floor1.move();
        floor2.move();

        resetFloorIfOutOfScreen(floor1, floor2, floorImage);
        resetFloorIfOutOfScreen(floor2, floor1, floorImage);

        //-------------------Pipe-------------------\\

        pipe1.move();
        pipe2.move();

        resetPipeIfOutOfScreen(pipe1);
        resetPipeIfOutOfScreen(pipe2);

        //-------------------Bird-------------------\\
        bird.fall();
        bird.updateBirdFlapAnimation(frameCounter[0]);
        bird.updateBirdRotationAnimation(frameCounter[0]);

        endGameIfBirdCrashed(bird, pipe1, pipe2);

        frameCounter[0]++;
      }

    }));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();

    Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
    primaryStage.setScene(scene);

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

    primaryStage.show();
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

  private static void createBackground(Pane root) {
    Image backgroundImage = new Image("images/background_cropped.png");

    BackgroundSize backgroundSize = new BackgroundSize(SCREEN_WIDTH, SCREEN_HEIGHT, false, false,
        false, false);

    BackgroundImage backgroundConfig = new BackgroundImage(backgroundImage,
        BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
    root.setBackground(new Background(backgroundConfig));
  }


  public static void main(String[] args) {
    launch(args);
  }
}
