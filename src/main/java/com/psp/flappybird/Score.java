package com.psp.flappybird;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Score extends GameObject {

  private static final int SCORE_FONT_SIZE = 30;
  private static final int SCORE_TEXT_Y_POSITION = 50;
  private static final double SCORE_INCREMENT = 0.5;
  private static final int SCORE_CHECK_RANGE = 5;

  private double score;
  private final Text scoreText;

  public Score() {
    this.score = 0;
    this.scoreText = new Text(String.valueOf(0));
    scoreText.setFont(Font.font("Arial", FontWeight.BOLD, SCORE_FONT_SIZE));
    scoreText.setFill(Color.WHITE);
  }

  @Override
  public void addToPane(Pane pane) {
    pane.getChildren().add(scoreText);
    scoreText.setX(FlappyBird.SCREEN_WIDTH / 2.0 - scoreText.getLayoutBounds().getWidth() / 2.0);
    scoreText.setY(SCORE_TEXT_Y_POSITION);
  }

  @Override
  public void update(int frameCounter) {
    scoreText.setText(String.valueOf((int) score));
  }

  @Override
  public void reset(double xCoordinate) {
    this.score = 0;
    update(0);
  }

  public void incrementScore() {
    this.score = score + SCORE_INCREMENT;
    update(0);
  }

  public void checkAndIncrementScore(double birdCordX, double pipe1CordX, double pipe2CordX) {
    if (birdCordX > pipe1CordX + Pipe.PIPE_WIDTH
        && birdCordX < pipe1CordX + Pipe.PIPE_WIDTH + SCORE_CHECK_RANGE ||
        birdCordX > pipe2CordX + Pipe.PIPE_WIDTH
            && birdCordX < pipe2CordX + Pipe.PIPE_WIDTH + SCORE_CHECK_RANGE) {
      incrementScore();
    }
  }
}
