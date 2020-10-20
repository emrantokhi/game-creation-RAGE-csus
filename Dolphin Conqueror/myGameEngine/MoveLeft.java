package myGameEngine;
import ray.input.action.Action;
import ray.rage.scene.*;
import ray.rml.*;
import net.java.games.input.Event;

public class MoveLeft implements Action {
	private SceneNode dolphinN;
	
	public MoveLeft(SceneNode dolphinN) {
		this.dolphinN = dolphinN;
	}
	
	public void performAction(float time, Event e) {
		Vector3f n = (Vector3f) dolphinN.getLocalRightAxis();
		Vector3f p = (Vector3f) dolphinN.getLocalPosition();
		Vector3f p1 = (Vector3f) Vector3f.createFrom(.1f * n.x(), .1f * n.y(), .1f * n.z()); //use positive value bc its reversed in n mode (?)
		Vector3f p2 = (Vector3f) p.add((Vector3) p1);
		
		dolphinN.setLocalPosition((Vector3f) Vector3f.createFrom(p2.x(), p2.y(), p2.z()));
	}
}
