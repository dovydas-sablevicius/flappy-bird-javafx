package com.psp.flappybird;

import javafx.scene.layout.Pane;

public abstract class GameObject {
    public abstract void addToPane(Pane pane);
    public abstract void update(int frameCounter);
    public abstract void reset(double xCoordinate);
}
