package objects;

import mathStuff.Vector3D;

public class DirectionalLight extends Light{

	public DirectionalLight(Vector3D pos) {
		this.pos = pos;
	}
	
	public DirectionalLight(Vector3D pos, float b) {
		this.pos = pos;
		brightness = b;
	}
	
}
