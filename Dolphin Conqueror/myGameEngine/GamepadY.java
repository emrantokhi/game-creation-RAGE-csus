package myGameEngine;
import ray.input.action.Action;
import ray.rage.scene.*;
import ray.rml.*;
import net.java.games.input.Event;

public class GamepadY implements Action {
	private SceneNode dolphinN;
	//private SceneNode dolphinCam;
	private float deadzone;
	
	public GamepadY(SceneNode dolphinN) {
		this.dolphinN = dolphinN;
		//this.dolphinCam = dolphinCam;
		deadzone = .075f;
	}
	
	public void performAction(float time, Event e) {
		//System.out.println(e.getValue());
		Vector3f n = (Vector3f) dolphinN.getLocalForwardAxis();
		Vector3f p = (Vector3f) dolphinN.getLocalPosition();
		//Vector3f camPos = (Vector3f) dolphinCam.getLocalPosition(); //position of camera
		//Vector3f camPlusP1 = null; //new position of camera (follows dolphin)
		Vector3f p1 = null; 
		Vector3f p2 = null;
		
		if (e.getValue() > deadzone || e.getValue() < deadzone * -1.0f) {
			p1 = (Vector3f) Vector3f.createFrom(-0.1f * e.getValue() * n.x(), -0.1f *  e.getValue() * n.y(), -0.1f *  e.getValue() * n.z());
			p2 = (Vector3f) p.add((Vector3) p1);
			//camPlusP1 = (Vector3f) p1.add(camPos); //new position of camera (follows dolphin)
			
			//dolphinCam.setLocalPosition((Vector3f) Vector3f.createFrom(camPlusP1.x(), camPlusP1.y(), camPlusP1.z()));
			dolphinN.setLocalPosition((Vector3f) Vector3f.createFrom(p2.x(), p2.y(), p2.z()));
		}
	}
}
