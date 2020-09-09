package mathStuff;

public class MathClass {
	
	public static int screenHeight = 600;
	public static int screenWidth  = 800;
	
	//ProjectionMatrix
	public static float near= 0.1f;
	public static float far	= 1000;
	public static float FOV	= 90;
	public static float FOVRad	= (float) (1.0 / Math.tan(Math.toRadians(FOV)/2));
	public static float aspectRatio = (float) screenHeight / (float) screenWidth;
	
	public static final float[][] matProj = {{aspectRatio * FOVRad,			0,						0, 0},
											  {					  0,   FOVRad, 						0, 0},
											  {					  0, 		0, 		   far/(far-near), 1},
											  {					  0, 		0, (-far*near)/(far-near), 0}};

	public static float dot(Vector3D v1, Vector3D v2) {
		return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
	}
	
	
	//VECTORS
	public static Vector3D cross(Vector3D v1, Vector3D v2) {
		return new Vector3D(
				v1.y * v2.z - v1.z * v2.y,
				v1.z * v2.x - v1.x * v2.z,
				v1.x * v2.y - v1.y * v2.x);
	}

	public static Vector3D vecSum(Vector3D v1, Vector3D v2) {
		return new Vector3D(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}
	
	public static Vector3D vecDif(Vector3D v1, Vector3D v2) {
		return new Vector3D(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
	}

	public static Vector3D vecIntersectPlane(Vector3D normalPlane, Vector3D pointPlane, Vector3D lineStart, Vector3D lineEnd) {
		
		normalPlane = normalPlane.normalize();
		float plane_d = -dot(normalPlane, pointPlane);
		float ad = dot(lineStart, normalPlane);
		float bd = dot(  lineEnd, normalPlane);
		float t = (-plane_d - ad) / (bd - ad);
		Vector3D lineStartToEnd = vecDif(lineEnd, lineStart);
		Vector3D lineToIntersect = lineStartToEnd.scale(t);
		
		return vecSum(lineStart, lineToIntersect);
	}
	
	public static float distPointToPlane(Vector3D v, Vector3D normalPlane, Vector3D pointPlane) {
		normalPlane = normalPlane.normalize();
		return dot(v, normalPlane) - dot(normalPlane, pointPlane);
	}
	
	
	//MATRICES
	//CAMERA
	public static float[][] matPointAt(Vector3D pos, Vector3D target, Vector3D up) {
		
		//Calculate new forward direction
		Vector3D newForward = vecDif(target, pos).normalize();
		
		//Calculate new up direction
		Vector3D a = newForward.scale(dot(up, newForward));
		Vector3D newUp = vecDif(up, a).normalize();
		
		//Calculate new right direction
		Vector3D newRight = cross(newUp, newForward);
		
		float[][] matrix = {{0,0,0,0}, {0,0,0,0}, {0,0,0,0}, {0,0,0,1}};
		matrix[0][0] =	 newRight.x;	matrix[0][1] =	 newRight.y;	matrix[0][2] =	 newRight.z;
		matrix[1][0] =		newUp.x;	matrix[1][1] =		newUp.y;	matrix[1][2] =		newUp.z;
		matrix[2][0] = newForward.x;	matrix[2][1] = newForward.y;	matrix[2][2] = newForward.z;
		matrix[3][0] =		  pos.x;	matrix[3][1] =		  pos.y;	matrix[3][2] =		  pos.z;
		
		return matrix;		
	}
	
	public static float[][] matQuickInverse(float[][] m) {
		float[][] matrix = {{0,0,0,0}, {0,0,0,0}, {0,0,0,0}, {0,0,0,1}};
		
		matrix[0][0] = m[0][0];	matrix[0][1] = m[1][0];	matrix[0][2] = m[2][0];
		matrix[1][0] = m[0][1];	matrix[1][1] = m[1][1];	matrix[1][2] = m[2][1];
		matrix[2][0] = m[0][2];	matrix[2][1] = m[1][2];	matrix[2][2] = m[2][2];
		matrix[3][0] = - m[3][0] * m[0][0] - m[3][1] * m[0][1] - m[3][2] * m[0][2];
		matrix[3][1] = - m[3][0] * m[1][0] - m[3][1] * m[1][1] - m[3][2] * m[1][2];
		matrix[3][2] = - m[3][0] * m[2][0] - m[3][1] * m[2][1] - m[3][2] * m[2][2];;
		
		return matrix;
	}
	
	//MATRIX MULTIPLICATION
	public static float[][] matMult(float[][] m1, float[][] m2) {
		
		float[][] result = new float[m1.length][m2[0].length];
		
		for (int i=0; i<result.length; i++) {
			for (int j=0; j<result[0].length; j++) {
				
				result[i][j] = 0;
				for (int k=0; k<m2.length; k++) {
					result[i][j] += m1[i][k] * m2[k][j];
				}
			}
		}
		
		return result;
	}
	
	//VECTOR-MATRIX MULTIPLICATION
	public static Vector3D vecMatMult(Vector3D i, float[][] m) {
		
		Vector3D v = new Vector3D(0, 0, 0);
		
		v.x = i.x * m[0][0] + i.y * m[1][0] + i.z * m[2][0] + i.w * m[3][0];
		v.y = i.x * m[0][1] + i.y * m[1][1] + i.z * m[2][1] + i.w * m[3][1];
		v.z = i.x * m[0][2] + i.y * m[1][2] + i.z * m[2][2] + i.w * m[3][2];
		v.w = i.x * m[0][3] + i.y * m[1][3] + i.z * m[2][3] + i.w * m[3][3];
		
		return v;
	}

	public static Vector3D vecMatMult4(Vector3D v, float[][] m) {
		
		float x = v.x * m[0][0] + v.y * m[1][0] + v.z * m[2][0] + m[3][0];
		float y = v.x * m[0][1] + v.y * m[1][1] + v.z * m[2][1] + m[3][1];
		float z = v.x * m[0][2] + v.y * m[1][2] + v.z * m[2][2] + m[3][2];
		float w = v.x * m[0][3] + v.y * m[1][3] + v.z * m[2][3] + m[3][3];

		if (w != 0) {
			x /= w; y /= w; z /= w;
		}
		
		Vector3D r = new Vector3D(x, y, z);
		
		return r;
	}
	
	//PRINT MATRIX
	public static void printMatrix(float[][] m) {
		for (int i=0; i<m.length; i++) {
			for (int j=0; j<m[0].length; j++) {
				System.out.print(m[i][j] + " ");
			}
			System.out.println();
		}
	}

}
