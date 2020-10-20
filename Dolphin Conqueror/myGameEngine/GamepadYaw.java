package myGameEngine;
import ray.input.action.Action;
import ray.rage.scene.*;
import ray.rml.*;
import net.java.games.input.Event;

public class GamepadYaw implements Action {
	private SceneNode dolphinN;
	//private SceneNode dolphinCam;
	private float deadzone;
	
	public GamepadYaw(SceneNode dolphinN) {
		this.dolphinN = dolphinN;
		//this.dolphinCam = dolphinCam;
		deadzone = .075f;
	}
	
	public void performAction(float time, Event e) {
		if(e.getValue() > deadzone || e.getValue() < deadzone * -1.0f) {
			//dolphinN.rotate(Degreef.createFrom(e.getValue() * -2.0f), Vector3f.createFrom(0.0f, 1.0f, 0.0f)); //Might be neg 2 because of dolphin orientation(?)
			Vector3 worldUp = Vector3f.createFrom(0.0f, 1.0f, 0.0f);
			Matrix3 matRot = Matrix3f.createRotationFrom(Degreef.createFrom(e.getValue() * -2.0f), worldUp);
			//SceneNode dolphinCam = (SceneNode) dolphinN.getChild("DolphinCameraNode");
			//dolphinN.setLocalRotation(matRot.mult(dolphinN.getWorldRotation()));
			//Matrix3 springSystem = matRot.mult(.00390625f); //it's multiplying the matrix by 1/64 (1/4 * 1/4 * 1/4)
			dolphinN.setLocalRotation(matRot.mult(dolphinN.getWorldRotation()));
			//dolphinCam.setLocalRotation(matRot.mult(dolphinCam.getWorldRotation()));
		}
	}
}
