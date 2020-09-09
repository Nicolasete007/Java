package mathStuff;

import java.util.ArrayList;

public class Mesh3D {
	
	public ArrayList<Triangle3D> mesh = new ArrayList<Triangle3D>();
	
	public Mesh3D(Triangle3D[] triangle) {
		for (Triangle3D t: triangle) mesh.add(t);
	}
	
	public void rotateX(float O) {
		
		for (Triangle3D t: mesh) {
			t.rotateX(O);
		}

	}
	
	public void rotateY(float O) {
		
		for (Triangle3D t: mesh) {
			t.rotateY(O);
		}

	}
	
	public void rotateZ(float O) {
		
		for (Triangle3D t: mesh) {
			t.rotateZ(O);
		}

	}

	public void rotateAxis(float O, Vector3D axis) {
		
		for (Triangle3D t: mesh) {
			t.rotateAxis(O, axis);
		}

	}
}
