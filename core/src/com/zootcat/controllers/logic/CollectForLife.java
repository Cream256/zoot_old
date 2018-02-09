package com.zootcat.controllers.logic;

import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;

//TODO add test
public class CollectForLife extends CollectOnCollide
{
	@CtrlParam(debug = true) private int life = 1;
	@CtrlParam(debug = true) private int maxLife = 0;
	
	public void onCollect(ZootActor collectible, ZootActor collector)
	{
		collector.controllerAction(LifeController.class, ctrl ->
		{
			ctrl.addMaxLife(maxLife);
			ctrl.addLife(life);
		});		
	}
}
