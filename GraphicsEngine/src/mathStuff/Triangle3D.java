package mathStuff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class Triangle3D implements Comparable<Triangle3D>{

	public Vector3D v1, v2, v3, mid;
	
	public Color color = Color.WHITE;

	public Triangle3D(Vector3D v1, Vector3D v2, Vector3D v3) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		
		mid = MathClass.vecSum(MathClass.vecSum(v1, v2), v3);
		mid.x /= 3; mid.y /= 3; mid.z /= 3;	
	}
	
	public Triangle3D(Vector3D v1, Vector3D v2, Vector3D v3, Color color) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.color = color;
		
		mid = MathClass.vecSum(MathClass.vecSum(v1, v2), v3);
		mid.x /= 3; mid.y /= 3; mid.z /= 3;	
	}
	
	public Triangle3D copy() {
		return new Triangle3D(
				new Vector3D(v1.x, v1.y, v1.z),
				new Vector3D(v2.x, v2.y, v2.z),
				new Vector3D(v3.x, v3.y, v3.z),
				color);
	}

	public int compareTo(Triangle3D o) {
		if (mid.z < o.mid.z) return 1;
		else if (mid.z > o.mid.z) return -1;
		else return 0;
	}
	
	//NORMAL MATH
	public Vector3D normal() {
		Vector3D line1 	= MathClass.vecDif(v2, v1);
		Vector3D line2 	= MathClass.vecDif(v3, v1);
		return MathClass.cross(line1, line2);
	}
	
	//PROJECTION
	public Triangle3D projectToScreen() {
		Vector3D v1P = MathClass.vecMatMult4(v1, MathClass.matProj);
		Vector3D v2P = MathClass.vecMatMult4(v2, MathClass.matProj);
		Vector3D v3P = MathClass.vecMatMult4(v3, MathClass.matProj);
		
		return new Triangle3D(v1P, v2P, v3P);
	}
	
	//TRANSLATIONS
	public Triangle3D translate(float x, float y, float z) {
		return new Triangle3D(
				v1.translate(x, y, z),
				v2.translate(x, y, z),
				v3.translate(x, y, z));
	}
	
	//ROTATIONS
	public Triangle3D rotateX(float O) {
		return new Triangle3D(v1.rotateX(O), v2.rotateX(O), v3.rotateX(O));
	}
	
	public Triangle3D rotateY(float O) {
		return new Triangle3D(v1.rotateY(O), v2.rotateY(O), v3.rotateY(O));
	}
	
	public Triangle3D rotateZ(float O) {
		return new Triangle3D(v1.rotateZ(O), v2.rotateZ(O), v3.rotateZ(O));
	}

	public Triangle3D rotateAxis(float O, Vector3D axis) {
		return new Triangle3D(v1.rotateAxis(O, axis), v2.rotateAxis(O, axis), v3.rotateAxis(O, axis));
	}

	//DRAW TO SCREEN
	public void drawFrame(Graphics g2) {
		
		g2.drawLine((int) v1.x, (int) v1.y, (int) v2.x, (int) v2.y);
		g2.drawLine((int) v2.x, (int) v2.y, (int) v3.x, (int) v3.y);
		g2.drawLine((int) v3.x, (int) v3.y, (int) v1.x, (int) v1.y);

	}

	public void fill(Graphics g2) {
		int[] X = {(int) v1.x, (int) v2.x, (int) v3.x};
		int[] Y = {(int) v1.y, (int) v2.y, (int) v3.y};
		Polygon t = new Polygon(X, Y, 3);
		g2.fillPolygon(t);
	}


}
