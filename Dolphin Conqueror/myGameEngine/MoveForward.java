package myGameEngine;
import ray.input.action.Action;
import ray.rage.scene.*;
import ray.rml.*;
import net.java.games.input.Event;

public class MoveForward implements Action {
	private SceneNode dolphinN;
	//private SceneNode dolphinCam;
	
	public MoveForward(SceneNode dolphinN) {
		this.dolphinN = dolphinN;
		//this.dolphinCam = dolphinCam;
	}
	
	public void performAction(float time, Event e) {
		Vector3f n = (Vector3f) dolphinN.getLocalForwardAxis();
		Vector3f p = (Vector3f) dolphinN.getLocalPosition();
		//Vector3f camPos = (Vector3f) dolphinCam.getLocalPosition(); //position of camera
		Vector3f p1 = (Vector3f) Vector3f.createFrom(.1f * n.x(), .1f * n.y(), .1f * n.z());
		Vector3f p2 = (Vector3f) p.add((Vector3) p1);
		//Vector3f camPlusP1 = (Vector3f) p1.add(camPos); //new position of camera (follows dolphin)
			
		//dolphinCam.setLocalPosition((Vector3f) Vector3f.createFrom(camPlusP1.x(), camPlusP1.y(), camPlusP1.z()));
		dolphinN.setLocalPosition((Vector3f) Vector3f.createFrom(p2.x(), p2.y(), p2.z()));
	}
}
