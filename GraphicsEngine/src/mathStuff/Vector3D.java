package mathStuff;

public class Vector3D {

	public float x, y, z, w = 1;

	public Vector3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3D() {
		x = 0; y = 0; z = 0;
	}

	public Vector3D copy() {
		return new Vector3D(x, y, z);
	}

	public void print() {
		System.out.println("x: "+x+" y: "+y+" z: "+z);
	}
	
	//NORMAL MATH
	public float norm() {
		return (float) Math.sqrt(x*x + y*y + z*z);
	}

	public Vector3D normalize() {
		return new Vector3D(x / norm(), y / norm(), z / norm());
	}

	public Vector3D scale(float s) {
		return new Vector3D(x*s, y*s, z*s);
	}
	
	//TRANSLATIONS
	public Vector3D translate(float x, float y, float z) {
		return new Vector3D(this.x + x, this.y + y, this.z + z);
	}
	
	//ROTATIONS
	public Vector3D rotateX(float O) {
		
		float cos = (float) Math.cos(O);
		float sin = (float) Math.sin(O);
		
		float[][] matrix = {{1,   0,	0, 0},
							{0, cos, -sin, 0},
							{0, sin,  cos, 0},
							{0,   0,	0, 1}};
		
		return MathClass.vecMatMult(new Vector3D(x, y, z), matrix);
	}
	
	public Vector3D rotateY(float O) {
		
		float cos = (float) Math.cos(O);
		float sin = (float) Math.sin(O);
		
		float[][] matrix = {{ cos, 0,  sin, 0},
							{	0, 1,	 0, 0},
							{-sin, 0,  cos, 0},
							{	0, 0,    0, 1}};
		
		return MathClass.vecMatMult(new Vector3D(x, y, z), matrix);
	}
	
	public Vector3D rotateZ(float O) {
		
		float cos = (float) Math.cos(O);
		float sin = (float) Math.sin(O);
		
		float[][] matrix = {{cos, -sin, 0, 0},
							{sin,  cos, 0, 0},
							{  0,	 0, 1, 0},
							{  0,	 0, 0, 1}};
		
		return MathClass.vecMatMult(new Vector3D(x, y, z), matrix);
	}

	public Vector3D rotateAxis(float O, Vector3D axis) {
		
		float ux = axis.normalize().x;
		float uy = axis.normalize().y;
		float uz = axis.normalize().z;
		
		float cos = (float) Math.cos(O);
		float sin = (float) Math.sin(O);
		
		float[][] matrix = {{cos + ux*ux*(1 - cos), ux*uy*(1 - cos) - uz*sin, ux*uz*(1 - cos) + uy*sin, 0},
							 {uy*ux*(1 - cos) + uz*sin, cos + uy*uy*(1 - cos), uy*uz*(1 - cos) - ux*sin, 0},
							 {uz*ux*(1 - cos) - uy*sin, uz*uy*(1 - cos) + ux*sin, cos + uz*uz*(1 - cos), 0},
							 {						 0,						   0,					  0, 1}};
		
		return MathClass.vecMatMult(new Vector3D(x, y, z), matrix);

	}
}