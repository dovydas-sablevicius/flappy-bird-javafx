package com.psp.flappybird;

import static com.psp.flappybird.FlappyBird.SCREEN_HEIGHT;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Floor extends GameObject {

  public static final int FLOOR_Y_CORD = SCREEN_HEIGHT - 87;
  private static final double FLOOR_SPEED = 2.0;

  private final ImageView floor;

  public Floor(Image floorImage, int xCoordinate) {
    floor = new ImageView(floorImage);
    reset(xCoordinate);
  }

  @Override
  public void addToPane(Pane pane) {
    pane.getChildren().add(floor);
  }

  @Override
  public void update(int frameCounter) {
    floor.setX(floor.getX() - FLOOR_SPEED);
  }

  @Override
  public void reset(double xCoordinate) {
    floor.setX(xCoordinate);
    floor.setY(SCREEN_HEIGHT - floor.getImage().getHeight());
  }

  public boolean isFloorOffScreen() {
    return floor.getX() + floor.getImage().getWidth() <= 0;
  }

  public ImageView getFloor() {
    return floor;
  }

}
