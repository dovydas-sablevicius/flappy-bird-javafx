package com.psp.flappybird;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BirdTest {

  private Bird bird;

  @BeforeEach
  void setUp() {
    bird = new Bird();
  }

  @Test
  void birdShouldNotBeNull() {
    assertNotNull(bird);
  }

  @Test
  void birdShouldIncreaseVelocityAndRotateWhenFlapped() {
    double initialVelocity = bird.getBirdVelocity();
    double initialRotation = bird.getBird().getRotate();

    bird.flap(0);

    assertTrue(bird.getBirdVelocity() < initialVelocity);
    assertTrue(bird.getBird().getRotate() < initialRotation);
  }

  @Test
  void birdRotationAndVelocityShouldBeZeroWhenReset() {
    bird.flap(0);
    bird.reset(0);

    assertEquals(0, bird.getBirdVelocity());
    assertEquals(0, bird.getBird().getRotate());
  }


}
