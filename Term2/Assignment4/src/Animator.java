package com.duckhunt;

import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Animator {
    private Timeline timeline;
    private ArrayList<IAnimation> animationContainers;

    public Animator(Duration frameDuration) {
        timeline = createAnimationTimeline(frameDuration);
        animationContainers  = new ArrayList<IAnimation>();
    }

    public void Start() {
        timeline.play();
    }

    public void Stop() {
        timeline.stop();
    }

    public void AddAnimation(IAnimation animation) {
        animationContainers.add(animation);
    }

    public void removeAnimation(IAnimation animation) {
        animationContainers.remove(animation);
    }

    private Timeline createAnimationTimeline(Duration frameDuration) {
        Timeline timeline = new Timeline(new KeyFrame(frameDuration, e -> {
            for (IAnimation animation : animationContainers) {
                animation.Tick();
            }
        }));

        timeline.setCycleCount(Animation.INDEFINITE); // Loop indefinitely
        return timeline;
    }
}
