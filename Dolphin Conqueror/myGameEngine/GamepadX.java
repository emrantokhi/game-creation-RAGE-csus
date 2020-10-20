package myGameEngine;
import ray.input.action.Action;
import ray.rage.scene.*;
import ray.rml.*;
import net.java.games.input.Event;

public class GamepadX implements Action {
	private SceneNode dolphinN;
	private float deadzone;
	
	public GamepadX(SceneNode dolphinN) {
		this.dolphinN = dolphinN;
		deadzone = .075f;
	}
	
	public void performAction(float time, Event e) { 
		Vector3f n = (Vector3f) dolphinN.getLocalRightAxis();
		Vector3f p = (Vector3f) dolphinN.getLocalPosition();
		Vector3f p1 = null;
		Vector3f p2 = null;
		
		if (e.getValue() > deadzone || e.getValue() < deadzone * -1.0f) {
			p1 = (Vector3f) Vector3f.createFrom(-0.1f * e.getValue() * n.x(), -0.1f *  e.getValue() * n.y(), -0.1f *  e.getValue() * n.z());
			p2 = (Vector3f) p.add((Vector3) p1);
			dolphinN.setLocalPosition((Vector3f) Vector3f.createFrom(p2.x(), p2.y(), p2.z()));
		}
	}
}
