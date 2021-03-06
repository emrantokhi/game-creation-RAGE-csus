package myGameEngine;
import ray.input.action.Action;
import ray.rage.scene.*;
import ray.rml.*;
import net.java.games.input.Event;

public class GlobalYawPositive implements Action {
	private SceneNode dolphinN;
	private SceneNode dolphinCam;
	
	public GlobalYawPositive(SceneNode dolphinN) {
		this.dolphinN = dolphinN;
		//this.dolphinCam = dolphinCam;
	}
	
	public void performAction(float time, Event e) {
		Vector3 worldUp = Vector3f.createFrom(0.0f, 1.0f, 0.0f);
		Matrix3 matRot = Matrix3f.createRotationFrom(Degreef.createFrom(2.0f), worldUp);
		//Matrix3 camRot = Matrix3f.createRotationFrom(Degreef.createFrom(-2.0f), dolphinN.getWorldUpAxis());
		
		dolphinN.setLocalRotation(matRot.mult(dolphinN.getWorldRotation()));
		//dolphinCam.setLocalRotation(camRot.mult(dolphinCam.getWorldRotation()));
		
	}
}
