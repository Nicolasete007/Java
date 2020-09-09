package mainStuff;

import java.io.*;
import java.io.FileReader;
import java.util.ArrayList;

import mathStuff.Mesh3D;
import mathStuff.Triangle3D;
import mathStuff.Vector3D;

public class ObjectReader {

	public static ArrayList<Vector3D> vertices(String path) {
		
		ArrayList<Vector3D> vertices = new ArrayList<Vector3D>();
		
		try {
			File objectFile = new File(path);
			FileReader fileReader = new FileReader(objectFile);
			BufferedReader br = new BufferedReader(fileReader);
			String line;
			
			while((line = br.readLine()) != null) {
				if (line.startsWith("v")) {
					Vector3D vertex = new Vector3D(
							(float) Double.parseDouble(line.split(" ")[1]),
							(float) Double.parseDouble(line.split(" ")[2]),
							(float) Double.parseDouble(line.split(" ")[3]));
					vertices.add(vertex);
				}
			}
			
			br.close();
		}
		catch (Exception e) {e.printStackTrace();}

		return vertices;
	}
	
	public static ArrayList<int[]> triangles(String path) {
		
		ArrayList<int[]> triangles = new ArrayList<int[]>();
		
		try {
			File objectFile = new File(path);
			FileReader fileReader = new FileReader(objectFile);
			BufferedReader br = new BufferedReader(fileReader);
			String line;
			
			while((line = br.readLine()) != null) {
				if (line.startsWith("f")) {
					int[] triangle = new int[3];
					triangle[0] = Integer.parseInt(line.split(" ")[1]);
					triangle[1] = Integer.parseInt(line.split(" ")[2]);
					triangle[2] = Integer.parseInt(line.split(" ")[3]);
					triangles.add(triangle);
				}
			}
			
			br.close();
		}
		catch (Exception e) {e.printStackTrace();}

		return triangles;
		
	}
	
	public static Mesh3D loadFromFile(String path) {

		ArrayList<Vector3D> vertices = vertices(path);
		ArrayList<int[]> triangles = triangles(path);

		Triangle3D[] faces = new Triangle3D[triangles.size()];
		for (int i=0; i<triangles.size(); i++) {
			Vector3D v1t = vertices.get(triangles.get(i)[0]-1);
			Vector3D v2t = vertices.get(triangles.get(i)[1]-1);
			Vector3D v3t = vertices.get(triangles.get(i)[2]-1);
			faces[i] = new Triangle3D(v1t, v2t, v3t);
		}
		
		return new Mesh3D(faces);
	}
}
