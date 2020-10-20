package myGameEngine;
import ray.input.action.Action;
import ray.rage.scene.*;
import ray.rml.*;
import net.java.games.input.Event;

public class GamepadPitch implements Action {
	private SceneNode dolphinN;
	private float deadzone;
	
	public GamepadPitch(SceneNode dolphinN) {
		this.dolphinN = dolphinN;
		deadzone = .075f;
	}
	
	public void performAction(float time, Event e) {
		if(e.getValue() > deadzone || e.getValue() < deadzone * -1.0f) {
			//dolphinN.rotate(Degreef.createFrom(e.getValue() * 2.0f), dolphinN.getLocalRightAxis());
			Degreef angle = Degreef.createFrom(e.getValue() * 2.0f);
			dolphinN.pitch(angle);
		}
	}
}
