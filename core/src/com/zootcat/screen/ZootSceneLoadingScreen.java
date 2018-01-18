package com.zootcat.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.zootcat.controllers.Controller;
import com.zootcat.controllers.factory.ControllerFactory;
import com.zootcat.controllers.physics.StaticBodyController;
import com.zootcat.game.ZootGame;
import com.zootcat.map.tiled.ZootTiledMap;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.tiled.ZootTiledScene;
import com.zootcat.scene.tiled.ZootTiledActorFactory;

public class ZootSceneLoadingScreen extends ZootLoadingScreen
{	
	private List<ZootActor> tileActors = new ArrayList<ZootActor>();
	private List<ZootActor> objectActors = new ArrayList<ZootActor>();
	private Map<Integer, List<Controller>> controllersByObjectActorId = new HashMap<Integer, List<Controller>>();
	
	private ZootTiledMap map;
	private ZootTiledScene scene;
	private ZootGame game;
	
	public ZootSceneLoadingScreen(ZootGame game, String levelPath, float viewportWidth, float viewportHeight, float unitPerTile)
	{
		super(game.getAssetManager());		
		this.game = game;
		
		addTask((Void) -> loadLevel(levelPath));
		addTask((Void) -> createLevel(levelPath, viewportWidth, viewportHeight, unitPerTile));
		addTask((Void) -> loadActors());
		addTask((Void) -> assignControllers());
		addTask((Void) -> setupLevel());
	}
				
	@Override
	public void onFinishLoading()
	{
		cleanupAfterLoading();
		game.setScreen(new ZootLevelScreen(scene));
	}
	
	public void loadLevel(String levelPath)
	{
		game.getAssetManager().load(levelPath, TiledMap.class);
	}

	public void createLevel(String levelPath, float viewportWidth, float viewportHeight, float unitPerTile)
	{
		map = new ZootTiledMap(game.getAssetManager().get(levelPath, TiledMap.class));
		scene = new ZootTiledScene(map, viewportWidth, viewportHeight, unitPerTile);
		game.getControllerFactory().addGlobalParameter(ControllerFactory.SCENE_GLOBAL_PARAM, scene);
	}
	
	public void loadActors()
	{
		//create actors
		ZootTiledActorFactory actorFactory = new ZootTiledActorFactory(scene.getUnitScale());
		objectActors = actorFactory.createFromMapObjects(map.getAllObjects());
		
		//create controllers for object actors
		map.getAllObjects().forEach((obj) ->
    	{
    		int objId = obj.getProperties().get("id", Integer.class);    		
    		ZootActor actor = objectActors.stream().filter(act -> objId == act.getId()).findFirst().get();
    		
    		List<Controller> ctrls = game.getControllerFactory().createFromProperties(obj.getProperties());
    		ctrls.forEach(ctrl -> ctrl.init(actor, getAssetManager()));
    		
    		controllersByObjectActorId.put(objId, ctrls);
    	});
		
		//create tile actors
		tileActors = actorFactory.createFromMapCells(map.getLayerCells(ZootTiledMap.COLLISION_LAYER_NAME));
	}
	
	public void assignControllers()
	{
		//object actors
		controllersByObjectActorId.forEach((id, ctrls) ->
		{
			ZootActor actor = objectActors.stream().filter(act -> id == act.getId()).findFirst().get();
			ctrls.forEach(ctrl -> actor.addController(ctrl));
		});		
		
		//tile actors
		tileActors.forEach(actor -> 
		{
			StaticBodyController staticBodyCtrl = new StaticBodyController(scene);
			staticBodyCtrl.init(actor, getAssetManager());
			actor.addController(staticBodyCtrl);
		});	
	}
	
	public void setupLevel()
	{
		objectActors.forEach(actor -> scene.addActor(actor));
		tileActors.forEach(actor -> scene.addActor(actor));
	}
	
	public void cleanupAfterLoading()
	{
		tileActors.clear();
		objectActors.clear();
		controllersByObjectActorId.clear();
		game.getControllerFactory().removeGlobalParameter(ControllerFactory.SCENE_GLOBAL_PARAM);
	}
}
