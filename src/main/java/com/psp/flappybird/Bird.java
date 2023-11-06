package com.psp.flappybird;

import static com.psp.flappybird.FlappyBird.SCREEN_HEIGHT;
import static com.psp.flappybird.FlappyBird.SCREEN_WIDTH;

import java.util.Objects;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Bird extends GameObject {

  private static final double GRAVITY = 0.3;
  private static final double FLAP_STRENGTH = -8.0;
  private static final int BIRD_X_OFFSET_FROM_CENTER = 35;
  private static final int FLAP_ANIMATION_FRAME_INTERVAL = 8;
  private static final int TIME_BEFORE_ROTATE_DOWN = 45;
  private static final double MAX_BIRD_VELOCITY_FOR_ROTATION = 10;
  private static final double BIRD_VELOCITY_RANGE_FOR_ROTATION = 20;
  private static final double MIN_ROTATION_ANGLE = -30;
  private static final double MAX_ROTATION_ANGLE = 90;
  private static final double ROTATION_INTERPOLATION_FACTOR = 0.05;

  private final ImageView bird;
  private final Image[] birdImages = new Image[3];

  private double lastFlapTime;
  private double birdVelocity;

  protected static final int BIRD_WIDTH = 62;
  protected static final int BIRD_HEIGHT = 42;

  public Bird() {
    birdImages[0] = new Image(
        Objects.requireNonNull(getClass().getResourceAsStream("/images/bird1.png")));
    birdImages[1] = new Image(
        Objects.requireNonNull(getClass().getResourceAsStream("/images/bird2.png")));
    birdImages[2] = new Image(
        Objects.requireNonNull(getClass().getResourceAsStream("/images/bird3.png")));

    bird = new ImageView(birdImages[0]);
    bird.setFitWidth(BIRD_WIDTH);
    bird.setFitHeight(BIRD_HEIGHT);
    reset(0);
  }

  @Override
  public void addToPane(Pane pane) {
    pane.getChildren().add(bird);
  }

  @Override
  public void update(int frameCounter) {
    updateFallAnimation();
    updateFlapAnimation(frameCounter);
    updateRotationAnimation(frameCounter);
  }

  @Override
  public void reset(double xCoordinate) {
    bird.setX((SCREEN_WIDTH / 2.0 - bird.getFitWidth() / 2.0) - BIRD_X_OFFSET_FROM_CENTER);
    bird.setY(SCREEN_HEIGHT / 2.0 - bird.getFitHeight() / 2.0);
    bird.setRotate(0);
    birdVelocity = 0;
    lastFlapTime = 0;
  }

  public ImageView getBird() {
    return bird;
  }

  public void flap(double frameCounter) {
    birdVelocity = FLAP_STRENGTH;
    lastFlapTime = frameCounter;
    bird.setRotate(MIN_ROTATION_ANGLE);
  }

  private void updateFallAnimation() {
    birdVelocity += GRAVITY;
    bird.setY(bird.getY() + birdVelocity);
  }

  public void updateFlapAnimation(int frameCounter) {
    if (frameCounter % FLAP_ANIMATION_FRAME_INTERVAL == 0) {
      bird.setImage(birdImages[(frameCounter / FLAP_ANIMATION_FRAME_INTERVAL) % birdImages.length]);
    }
  }

  public void updateRotationAnimation(double frameCounter) {
    double desiredRotation;
    double timeSinceLastFlap = frameCounter - lastFlapTime;
    if (timeSinceLastFlap < TIME_BEFORE_ROTATE_DOWN) {
      desiredRotation = MIN_ROTATION_ANGLE;
    } else {
      double t = Math.min(Math.max(
              (birdVelocity + MAX_BIRD_VELOCITY_FOR_ROTATION) / BIRD_VELOCITY_RANGE_FOR_ROTATION, 0),
          1);
      desiredRotation = interpolateBetweenValues(MIN_ROTATION_ANGLE, MAX_ROTATION_ANGLE, t);
    }
    double currentRotation = bird.getRotate();
    double newRotation = interpolateBetweenValues(currentRotation, desiredRotation,
        ROTATION_INTERPOLATION_FACTOR);
    bird.setRotate(newRotation);
  }

  private double interpolateBetweenValues(double startValue, double endValue, double weight) {
    return startValue + weight * (endValue - startValue);
  }

  public double getBirdVelocity() {
    return birdVelocity;
  }
}
