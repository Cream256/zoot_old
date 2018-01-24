package com.zootcat.scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.controllers.ChangeListenerController;
import com.zootcat.controllers.Controller;
import com.zootcat.controllers.gfx.RenderController;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.fsm.StateMachine;

public class ZootActor extends Actor
{
	public static final String DEFAULT_NAME = "Unnamed Actor";
	
	private List<Controller> controllers = new ArrayList<Controller>();
	private Set<String> types = new HashSet<String>();	
	private float opacity = 1.0f;
	private int id = 0;
	private StateMachine stateMachine = new StateMachine();
			
	public ZootActor()
	{
		setName(DEFAULT_NAME);
		stateMachine.setOwner(this);
		addListener(stateMachine);
	}
	
	@Override
	public void act(float delta)
	{				
		//update
		controllers.forEach(ctrl -> ctrl.onUpdate(delta, this));
				
		//act
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) 
	{
		float delta = Gdx.graphics.getDeltaTime();
		controllers.stream().filter(ctrl -> ClassReflection.isInstance(RenderController.class, ctrl))
							.map(ctrl -> (RenderController)ctrl)
							.forEach(ctrl -> ctrl.onRender(batch, parentAlpha, this, delta));
	}
	
	@Override
	protected void positionChanged() 
	{
		controllers.stream().filter(ctrl -> ClassReflection.isInstance(ChangeListenerController.class, ctrl))
							.map(ctrl -> (ChangeListenerController)ctrl)
							.forEach(ctrl -> ctrl.onPositionChange(this));
	}

	@Override
	protected void sizeChanged() 
	{
		controllers.stream().filter(ctrl -> ClassReflection.isInstance(ChangeListenerController.class, ctrl))
							.map(ctrl -> (ChangeListenerController)ctrl)
							.forEach(ctrl -> ctrl.onSizeChange(this));
	}
	
	@Override
	protected void rotationChanged() 
	{
		controllers.stream().filter(ctrl -> ClassReflection.isInstance(ChangeListenerController.class, ctrl))
							.map(ctrl -> (ChangeListenerController)ctrl)
							.forEach(ctrl -> ctrl.onRotationChange(this));
	}

	public void addController(Controller controller)
	{
		controllers.add(controller);
		controller.onAdd(this);		
	}
	
	public void removeController(Controller controller)
	{
		controller.onRemove(this);
		controllers.removeAll(Arrays.asList(controller));
	}
	
	public List<Controller> getControllers()
	{
		return new ArrayList<Controller>(controllers);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Controller> T getController(Class<T> controllerClass)
	{
		Controller result = controllers.stream()
				  .filter(ctrl -> ClassReflection.isInstance(controllerClass, ctrl))
				  .findFirst()
				  .orElse(null);
		
		if(result == null)
		{
			throw new RuntimeZootException("Controller " + controllerClass + " not found for " + this);
		}
		return (T)result;
	}
	
	public float getOpacity() 
	{
		return opacity;
	}
	
	public void setOpacity(float value)
	{
		this.opacity = value;
	}
	    
    public void addType(String newType)
    {
        types.add(newType.toLowerCase().trim());
    }
    
    public void removeType(String typeToRemove)
    {
        types.remove(typeToRemove.toLowerCase());
    }
    
    public Set<String> getTypes()
    {
        return new HashSet<String>(types);
    }
    
    public boolean isType(String type)
    {
        return types.contains(type.toLowerCase());
    }	
    
    public int getId()
    {
    	return id;
    }
    
    public void setId(int id)
    {
    	this.id = id;
    }
    
    @Override
    public String toString()
    {
    	return getName();
    }

	public StateMachine getStateMachine()
	{
		return stateMachine;
	}
}
