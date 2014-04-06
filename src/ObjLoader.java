import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector2f;

/*
 * ObjLoader.java
 * This class reads in an obj file (must be triangulated) and returns it as a Model object
*/

public class ObjLoader
{
	public static Model loadModel(File obj)
	{
		Model m = new Model();
		String line = null;
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(obj));
			while((line = reader.readLine()) != null)
			{							
				if(line.startsWith("v "))
				{
					float x = Float.valueOf(line.split(" ")[1]);
					float y = Float.valueOf(line.split(" ")[2]);
					float z = Float.valueOf(line.split(" ")[3]);
					m.verticies.add(new Vector3f(x,y,z));
				}
				else if(line.startsWith("vt "))
				{
					float x = Float.valueOf(line.split(" ")[1]);
					float y = 1f-Float.valueOf(line.split(" ")[2]);
					m.textures.add(new Vector2f(x,y));				
				}
				else if(line.startsWith("f "))
				{
					Vector3f vertextIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[0]), Float.valueOf(line.split(" ")[2].split("/")[0]), Float.valueOf(line.split(" ")[3].split("/")[0]));
					Vector3f textureIndex = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[1]), Float.valueOf(line.split(" ")[2].split("/")[1]), Float.valueOf(line.split(" ")[3].split("/")[1]));
					m.faces.add(new Face(vertextIndices,textureIndex));				
				}
			}		
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.out.println(line);
			System.exit(0);
		}
		
		return m;	
	}
}