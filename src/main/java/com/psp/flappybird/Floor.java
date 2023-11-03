package com.psp.flappybird;

import static com.psp.flappybird.FlappyBird.SCREEN_HEIGHT;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Floor {

  public static final int FLOOR_Y = SCREEN_HEIGHT - 87;
  private static final double FLOOR_SPEED = 2.0;

  private final ImageView floor;

  public Floor(Image floorImage, double floorOffsetX) {
    floor = new ImageView(floorImage);
    resetFloorPosition(floorOffsetX);
  }

  public void resetFloorPosition(double floorOffSet) {
    floor.setX(floorOffSet);
    floor.setY(SCREEN_HEIGHT - floor.getImage().getHeight());
  }

  public void move() {
    floor.setX(floor.getX() - FLOOR_SPEED);
  }

  public boolean isFloorOffScreen() {
    return floor.getX() + floor.getImage().getWidth() <= 0;
  }

  public ImageView getFloor() {
    return floor;
  }
}
