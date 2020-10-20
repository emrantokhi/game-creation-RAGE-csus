package myGameEngine;
import ray.input.action.Action;
import ray.rage.scene.*;
import ray.rml.*;
import net.java.games.input.Event;

public class LocalPitchNegative implements Action {
	private SceneNode dolphinN;
	
	public LocalPitchNegative(SceneNode dolphinN) {
		this.dolphinN = dolphinN;
	}
	
	public void performAction(float time, Event e) {
		Degreef angle = Degreef.createFrom(2.0f);
		dolphinN.pitch(angle);
	}
}
