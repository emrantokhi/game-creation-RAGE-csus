package myGameEngine;
import ray.input.action.Action;
import ray.rage.scene.*;
import ray.rml.*;
import net.java.games.input.Event;

public class LocalPitchPositive implements Action {
	private SceneNode dolphinN;
	
	public LocalPitchPositive(SceneNode dolphinN) {
		this.dolphinN = dolphinN;
	}
	
	public void performAction(float time, Event e) {
		Degreef angle = Degreef.createFrom(-2.0f); //angle in degrees (Radianf for radians)
		dolphinN.pitch(angle);
	}
}
