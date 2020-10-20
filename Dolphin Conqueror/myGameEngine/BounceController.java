package myGameEngine;

import ray.rage.scene.Node;
import ray.rage.scene.controllers.AbstractController;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class BounceController extends AbstractController{

	private float cycleTime = 150.0f; //default cycle time
	private float totalTime = 0.0f;
	private float direction = 1.0f; //up direction
	
	@Override
	protected void updateImpl(float elapsedTimeMillis) {
		totalTime += elapsedTimeMillis;
		float bounceHeight  = .2f * direction;
		
		if (totalTime > cycleTime) {
			direction = -direction;
			totalTime = 0.0f;
		}
		
		for(Node n : super.controlledNodesList) {
			Vector3 curHeight = n.getLocalPosition();
			curHeight = Vector3f.createFrom(curHeight.x(), curHeight.y() + bounceHeight, curHeight.z());
			n.setLocalPosition(curHeight);
		}
		
	}

}
