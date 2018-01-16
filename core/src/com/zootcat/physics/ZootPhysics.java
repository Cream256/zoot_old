package com.zootcat.physics;

import java.util.List;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Disposable;

public interface ZootPhysics extends Disposable
{
	void step(float delta);
	void setGravity(float x, float y, float z);
		
	Body createBody(BodyDef bodyDef);
	void removeBody(Body body);
	
	List<Fixture> createFixtures(Body body, List<FixtureDef> fixtureDefs);
}
