package com.psp.flappybird;

import java.util.Objects;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PipeTest {

  Pipe pipe;

  @BeforeEach
  void setUp() {
    Image topPipeImage = new Image(
        Objects.requireNonNull(getClass().getResourceAsStream("/images/down_pipe_cropped.png")));
    Image bottomPipeImage = new Image(
        Objects.requireNonNull(getClass().getResourceAsStream("/images/down_pipe_cropped.png")));
    pipe = new Pipe(topPipeImage, bottomPipeImage, 0);
  }


  @Test
  public void pipeShouldNotBeNullWhenCreated() {
    assertNotNull(pipe);
  }


  @Test
  public void pipeShouldBeAtTheScreenWidthWhenReset() {
    pipe.reset(0);

    assertTrue(pipe.getTopPipe().getX() >= FlappyBird.SCREEN_WIDTH);
    assertTrue(pipe.getBottomPipe().getX() >= FlappyBird.SCREEN_WIDTH);
  }
}
