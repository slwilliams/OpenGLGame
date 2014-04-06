import org.lwjgl.util.vector.Vector3f;

/*
 * Face.java
 * This represents a single face in an imported 3D model
*/

public class Face
{
	public Vector3f vertex = new Vector3f();
	public Vector3f texture = new Vector3f();
	
	public Face(Vector3f vertex, Vector3f texture)
	{
		this.vertex = vertex;
		this.texture = texture;		
	}	
}