package mainStuff;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import mathStuff.*;
import objects.DirectionalLight;
import objects.Light;

@SuppressWarnings("serial")
public class ScreenPanel extends JPanel{	
	
	private int screenWidth  = getWidth();
	private int screenHeight = getHeight();

	private Timer timer;
	
	private Mesh3D axis = ObjectReader.loadFromFile("monkey.obj");
	public float yaw = 0;
	public float pitch = 0;

	Vector3D vCamera = new Vector3D();
	Vector3D vLookDirection = new Vector3D();
	
	//float[] depthBuffer;
	
	public ScreenPanel(float FPS) {
		
		IsKeyPressed.initialize();

		timer = new Timer();
		TimerTask task = new TimerTask() {public void run() {update(); MainClass.time++;} };
		timer.schedule(task, 0, (int) (1000/FPS));

	}
	
	private Color getBrightness(Triangle3D triangle, Light... lights) {
		
		float finalBrightness = 0;
		for (Light light: lights) {
			float b = MathClass.dot(light.pos.normalize(), triangle.normal().normalize());
			finalBrightness += b*light.brightness;
		}
		finalBrightness = (float) Math.max(0.03, Math.min(1, finalBrightness)); //Light between 0 and 1
		
		return new Color((int) (255*finalBrightness), (int) (255*finalBrightness), (int) (255*finalBrightness));
	}
	
	private ArrayList<Triangle3D> TriangleClipAgainstPlane(Vector3D pointPlane, Vector3D normalPlane, Triangle3D inTri) {
		
		ArrayList<Triangle3D> clipped = new ArrayList<Triangle3D>();
		Triangle3D outTri1, outTri2;
		normalPlane = normalPlane.normalize();
		
		Vector3D[] insidePoints  = new Vector3D[3];	int nInsidePoints  = 0;
		Vector3D[] outsidePoints = new Vector3D[3];	int nOutsidePoints = 0;
		
		//Calculate distance from every point in the inside triangle to the plane
		float d1 = MathClass.distPointToPlane(inTri.v1.copy(), normalPlane, pointPlane);
		float d2 = MathClass.distPointToPlane(inTri.v2.copy(), normalPlane, pointPlane);
		float d3 = MathClass.distPointToPlane(inTri.v3.copy(), normalPlane, pointPlane);
		
		//Add the points to the in and out arrays
		if (d1 >= 0) insidePoints[nInsidePoints++] = inTri.v1.copy();
		else outsidePoints[nOutsidePoints++] = inTri.v1.copy();
		if (d2 >= 0) insidePoints[nInsidePoints++] = inTri.v2.copy();
		else outsidePoints[nOutsidePoints++] = inTri.v2.copy();
		if (d3 >= 0) insidePoints[nInsidePoints++] = inTri.v3.copy();
		else outsidePoints[nOutsidePoints++] = inTri.v3.copy();
		
		if (nInsidePoints == 3) { //One triangle is valid
			clipped.add(inTri.copy());
		}
		if (nInsidePoints == 1 && nOutsidePoints == 2) {
			
			outTri1 = inTri.copy();
			
			outTri1.v1 = insidePoints[0]; //Keep the inside point of the triangle. Here the order makes a difference
			outTri1.v2 = MathClass.vecIntersectPlane(normalPlane, pointPlane, insidePoints[0], outsidePoints[0]);	//New points intersect
			outTri1.v3 = MathClass.vecIntersectPlane(normalPlane, pointPlane, insidePoints[0], outsidePoints[1]);	//with the screen plane
			
			clipped.add(outTri1);
		}
		if (nInsidePoints == 2 && nOutsidePoints == 1) {
			
			outTri1 = inTri.copy();
			outTri2 = inTri.copy();
			
			outTri1.v1 = insidePoints[0]; //Keep the inside points of the triangle. Here the order makes a difference
			outTri1.v2 = insidePoints[1];
			outTri1.v3 = MathClass.vecIntersectPlane(normalPlane, pointPlane, insidePoints[0], outsidePoints[0]);	//with the screen plane
			
			outTri2.v1 = insidePoints[1]; //Keep the inside points of the triangle. Here the order makes a difference
			outTri2.v2 = outTri1.v3.copy();
			outTri2.v3 = MathClass.vecIntersectPlane(normalPlane, pointPlane, insidePoints[1], outsidePoints[0]);	//with the screen plane
			
			clipped.add(outTri1);
			clipped.add(outTri2);
		}

		return clipped;
		
	}
	
	protected void paintComponent(Graphics g) {
		
		screenWidth = getWidth();
		screenHeight = getHeight();
		
		ArrayList<Triangle3D> trianglesToRaster = new ArrayList<Triangle3D>();
		for (Triangle3D t: axis.mesh) {

			Triangle3D tProjected, tTransformed, tViewed;
			
			//Rotate triangles
			tTransformed = t.rotateX((float) Math.toRadians(180));//t.rotateY(0.02*MainClass.time).rotateZ(0.01*MainClass.time).rotateX(0.005*MainClass.time);
			
			//Translate triangle into the world
			tTransformed = tTransformed.translate(0, 0, 5);
			
			Vector3D vUp = new Vector3D(0, 1, 0);
			Vector3D vTarget = new Vector3D(0, 0, 1);
			vLookDirection = vTarget.rotateY(yaw);
			vLookDirection = vLookDirection.rotateAxis(pitch, MathClass.cross(vLookDirection, vUp));
			vTarget = MathClass.vecSum(vCamera, vLookDirection);
			
			float[][] matCamera = MathClass.matPointAt(vCamera, vTarget, vUp);
			
			//Make view matrix from camera
			float[][] matView = MathClass.matQuickInverse(matCamera);
			
			//See only those which are in front
			Vector3D vNormal = tTransformed.normal().normalize();
			Vector3D vCameraRay = MathClass.vecDif(tTransformed.v1, vCamera); //Here a ray is casted from de vCamera to the triangle, and then compared to the normal
			
			if (MathClass.dot(vNormal, vCameraRay) < 0) {

				Color color = getBrightness(tTransformed,
						new DirectionalLight(new Vector3D(0, -5, -1), 1f));
				
				tViewed = tTransformed.copy();
				tViewed.v1 = MathClass.vecMatMult(tTransformed.v1, matView);
				tViewed.v2 = MathClass.vecMatMult(tTransformed.v2, matView);
				tViewed.v3 = MathClass.vecMatMult(tTransformed.v3, matView);
				
				//Clip tViewed against near plane								//point in near plane	//normal of near plane
				ArrayList<Triangle3D> clippedTriangles = TriangleClipAgainstPlane(new Vector3D(0, 0, 0.05f), new Vector3D(0, 0, 1), tViewed);
				
				for (Triangle3D tClipped: clippedTriangles) {
					//Calculate projected triangle				
					tProjected = tClipped.projectToScreen();

					tProjected.color = color;
					
					//Scale into view
					tProjected.v1.x += 1; tProjected.v1.y += 1;
					tProjected.v2.x += 1; tProjected.v2.y += 1;
					tProjected.v3.x += 1; tProjected.v3.y += 1;
					
					tProjected.v1.x *= 0.5 * screenWidth; tProjected.v1.y *= 0.5 * screenHeight;
					tProjected.v2.x *= 0.5 * screenWidth; tProjected.v2.y *= 0.5 * screenHeight;
					tProjected.v3.x *= 0.5 * screenWidth; tProjected.v3.y *= 0.5 * screenHeight;
		
					trianglesToRaster.add(tProjected);
				}
			}
		}
		
		Collections.sort(trianglesToRaster);
		
		super.paintComponent(g);
		setBackground(Color.BLACK);
		
		for (Triangle3D tToRaster: trianglesToRaster) {
			
			//Clip triangles against screen edges
			ArrayList<Triangle3D> listTriangles = new ArrayList<Triangle3D>();
			
			//Add initial triangle
			listTriangles.add(tToRaster);
			int newTriangles = 1;

			for (int plane=0; plane<4; plane++) {
				
				ArrayList<Triangle3D> trisToAdd = new ArrayList<Triangle3D>();
				while (newTriangles > 0) {
					
					//Take triangle from queue
					Triangle3D test = listTriangles.get(0);
					listTriangles.remove(0);
					newTriangles--;
					
					//Clip it against every plane
					switch (plane) {
					case 0: trisToAdd = TriangleClipAgainstPlane(new Vector3D(0, 0, 0), new Vector3D(0, 1, 0), test); break;
					case 1: trisToAdd = TriangleClipAgainstPlane(new Vector3D(0, screenHeight - 1, 0), new Vector3D(0, -1, 0), test); break;
					case 2: trisToAdd = TriangleClipAgainstPlane(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0), test); break;
					case 3: trisToAdd = TriangleClipAgainstPlane(new Vector3D(screenWidth - 1, 0, 0), new Vector3D(-1, 0, 0), test); break;
					}
					//This may create new triangles to clip against other planes
					listTriangles.addAll(trisToAdd);
				}
				newTriangles = listTriangles.size();
			}
			
			for (Triangle3D t: listTriangles) {

				g.setColor(t.color);
				t.fill(g);
				//g.setColor(Color.RED);
				//t.drawFrame(g2);
			}
		}
    }
	
	public void update() {
		
		//Camera control
		float speed = 0.1f;
		
		Vector3D vForward = vLookDirection.scale(speed);
		Vector3D vRight = MathClass.cross(new Vector3D(0, 1, 0), vForward).normalize().scale(speed);
		
		if (IsKeyPressed.isSpacePressed()) vCamera.y -= speed;
		if (IsKeyPressed.isShiftPressed()) vCamera.y += speed;

		if (IsKeyPressed.isWPressed()) vCamera = MathClass.vecSum(vCamera, new Vector3D(vForward.x, 0, vForward.z).normalize().scale(speed));
		if (IsKeyPressed.isSPressed()) vCamera = MathClass.vecDif(vCamera, new Vector3D(vForward.x, 0, vForward.z).normalize().scale(speed));
		if (IsKeyPressed.isAPressed()) vCamera = MathClass.vecDif(vCamera, new Vector3D(vRight.x, 0, vRight.z).normalize().scale(speed));
		if (IsKeyPressed.isDPressed()) vCamera = MathClass.vecSum(vCamera, new Vector3D(vRight.x, 0, vRight.z).normalize().scale(speed));
		
		//Paint on screen
		//depthBuffer = new float[screenWidth*screenHeight];
		//for (int i=0; i<depthBuffer.length; i++) depthBuffer[i] = 0;
		
		repaint();
	}
}
