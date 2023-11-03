package com.psp.flappybird;


import static com.psp.flappybird.FlappyBird.SCREEN_WIDTH;

import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

class Pipe {

  private static final int Y_GAP_BETWEEN_PIPES = 180;
  private static final int PIPE_LOWE_Y_BOUNDARY = 300;
  private static final int PIPE_WIDTH = 100;
  private static final double PIPE_SPEED = 2.0;
  private static final int PIPE_UPPER_Y_BOUNDARY = -250;

  private final ImageView topPipe;
  private final ImageView bottomPipe;

  public Pipe(Image topPipeImage, Image bottomPipeImage, int pipeOffsetX) {
    topPipe = new ImageView(topPipeImage);
    bottomPipe = new ImageView(bottomPipeImage);
    resetPipePosition(pipeOffsetX);
  }

  public void move() {
    topPipe.setX(topPipe.getX() - PIPE_SPEED);
    bottomPipe.setX(bottomPipe.getX() - PIPE_SPEED);
  }

  public void resetPipePosition(int initialX) {
    Random random = new Random();
    double randomY = PIPE_LOWE_Y_BOUNDARY + random.nextDouble() * PIPE_UPPER_Y_BOUNDARY;

    topPipe.setX(SCREEN_WIDTH + initialX);
    bottomPipe.setX(SCREEN_WIDTH + initialX);

    topPipe.setY(randomY - topPipe.getImage().getHeight());
    bottomPipe.setY(randomY + Y_GAP_BETWEEN_PIPES);
  }

  public boolean isPipeOffScreen() {
    return topPipe.getX() + PIPE_WIDTH < 0;
  }

  public ImageView getTopPipe() {
    return topPipe;
  }

  public ImageView getBottomPipe() {
    return bottomPipe;
  }

}