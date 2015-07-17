package com.sageggs.actors;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class GameActor extends Actor {

    public Body body;

    public GameActor(Body body) {
        this.body = body;
    }
    
    public GameActor() {}
    
    @Override
    public void act(float delta) {
        super.act(delta);
    }

}
