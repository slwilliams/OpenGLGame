import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

/*
 * Model.java
 * This class represents a single complete 3D model
*/

public class Model
{
	public List<Vector3f> verticies = new ArrayList<Vector3f>();
	public List<Vector2f> textures = new ArrayList<Vector2f>();
	public List<Face> faces = new ArrayList<Face>();
	
	
}