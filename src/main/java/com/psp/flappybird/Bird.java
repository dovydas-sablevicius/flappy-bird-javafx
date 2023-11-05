package com.psp.flappybird;

import static com.psp.flappybird.FlappyBird.SCREEN_HEIGHT;
import static com.psp.flappybird.FlappyBird.SCREEN_WIDTH;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class Bird {

  private static final int BIRD_WIDTH = 65;
  private static final int BIRD_HEIGHT = 45;
  private static final double GRAVITY = 0.3;
  private static final double FLAP_STRENGTH = -8.0;
  private static final int BIRD_X_OFFSET_FROM_CENTER = 35;


  private final ImageView bird;
  private final Image[] birdImages = new Image[3];

  private double lastFlapTime;
  private double birdVelocity;

  public Bird() {
    birdImages[0] = new Image("images/bird1.png");
    birdImages[1] = new Image("images/bird2.png");
    birdImages[2] = new Image("images/bird3.png");
    bird = new ImageView(birdImages[0]);
    bird.setFitWidth(BIRD_WIDTH);
    bird.setFitHeight(BIRD_HEIGHT);
    setBirdVelocity(0);
    setLastFlapTime(0);
    resetPosition();
  }

  public ImageView getBird() {
    return bird;
  }

  public void flap(double frameCounter) {
    birdVelocity = FLAP_STRENGTH;
    lastFlapTime = frameCounter;
    bird.setRotate(-30);
  }

  public void move() {
    birdVelocity += GRAVITY;
    bird.setY(bird.getY() + birdVelocity);
  }

  public void resetPosition() {
    bird.setX(
        (SCREEN_WIDTH / 2.0 - bird.getFitWidth() / 2.0)
            - BIRD_X_OFFSET_FROM_CENTER);
    bird.setY(SCREEN_HEIGHT / 2.0 - bird.getFitHeight() / 2.0);
    bird.setRotate(0);
    birdVelocity = 0;
  }

  public void updateBirdFlapAnimation(int frameCounter) {
    if (frameCounter % 8 == 0) {
      bird.setImage(birdImages[(frameCounter / 8) % birdImages.length]);
    }
  }

  public void updateBirdRotationAnimation(double frameCounter) {
    double desiredRotation;
    double timeSinceLastFlap = frameCounter - lastFlapTime;
    if (timeSinceLastFlap < 45) {
      desiredRotation = -30;
    } else {
      double t = Math.min(Math.max((birdVelocity + 10) / 20, 0), 1);
      desiredRotation = lerp(-30, 90, t);
    }
    double currentRotation = bird.getRotate();
    double newRotation = lerp(currentRotation, desiredRotation, 0.1);
    bird.setRotate(newRotation);
  }

  private double lerp(double a, double b, double t) {
    return a + t * (b - a);
  }

  public void setLastFlapTime(double lastFlapTime) {
    this.lastFlapTime = lastFlapTime;
  }

  public void setBirdVelocity(double birdVelocity) {
    this.birdVelocity = birdVelocity;
  }

}
