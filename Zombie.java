import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector2f;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/*
 * Zombie.java
 * Zombie is an enemy type in game
*/

public class Zombie
{
	private Controller controller;

	private Texture zombieT;
	private Model zombie;
	
	private float x;
	private float z;
	//hard coded for now
	private float y = 200f;	
		
	private float speed = 1f;
	private float xMid, yMid, Zmid;
	private float manScale = 1.25f;

	public Zombie(Controller controller, Model m, float x, float z)
	{
		this.controller = controller;
		this.zombie = m;
		this.x = x;
		this.z = z;
		this.zombieT = loadTexture("man.png");
	  
	  calculateMid();
	}

	private float midX = 0, midY = 0, midZ = 0;
	private float maxX = 0, maxY = 0, maxZ = 0;
	private float minX = 0, minY = 0, minZ = 0;
	
	public void calculateMid()
	{
		//calculates the mid point of the model for rotating on the spot
		
		maxX = (float)zombie.verticies.get(0).x;
		minX = maxX;
		maxX = (float)zombie.verticies.get(0).y;
		minX = maxY;
		maxX = (float)zombie.verticies.get(0).z;
		minX = maxZ;
		
		for(Face face : zombie.faces)
		{
			if((float)zombie.verticies.get((int) face.vertex.x -1).x < minX)
			{
				minX = (float)zombie.verticies.get((int) face.vertex.x -1).x;
			}
			if((float)zombie.verticies.get((int) face.vertex.y -1).y < minY)
			{
				minY = (float)zombie.verticies.get((int) face.vertex.y -1).y;
			}
			if((float)zombie.verticies.get((int) face.vertex.z -1).z < minZ)
			{
				minZ = (float)zombie.verticies.get((int) face.vertex.z -1).z;
			}			
			if((float)zombie.verticies.get((int) face.vertex.x -1).x > maxX)
			{
				maxX = (float)zombie.verticies.get((int) face.vertex.x -1).x;
			}
			if((float)zombie.verticies.get((int) face.vertex.y -1).y > maxY)
			{
				maxY = (float)zombie.verticies.get((int) face.vertex.y -1).y;
			}
			if((float)zombie.verticies.get((int) face.vertex.z -1).z > maxZ)
			{
				maxZ = (float)zombie.verticies.get((int) face.vertex.z -1).z;
			}
		}
		
		midX = ((maxX+x) + (minX+x))/2;
		midY = ((maxY-200) + (minY-200))/2;
		midZ = ((maxZ+z) + (minZ+z))/2;
	}

	public void updatePosition()
	{
		//walk towards the player a bit
		float playerX = controller.getX();
		float playerZ = controller.getZ();
		
		double rads = Math.atan2((int)((-playerZ)-z), (int)((-playerX)-x));
		x += speed*Math.cos(rads);
		z += speed*Math.sin(rads);
	}
	
	public void drawZombie()
	{
		//move the world to the position of the zombie to rotate about that point
		glTranslatef(midX, midY, midZ);
		
		//angle to rotate to point towards player
		float angle = (float)Math.toDegrees(Math.atan2(((-controller.getZ()-midZ)),(-controller.getX()-midX)));
		//rotate the world to this angle then draw the model
		glRotatef(90-angle, 0.0f, 1.0f, 0.0f);		
		
		//move back ready to draw
		glTranslatef(-(midX), -(midY), -(midZ));

		zombieT.bind();

		for(Face face : zombie.faces)
		{
			Vector3f v1 = zombie.verticies.get((int) face.vertex.x -1);
			Vector3f v2 = zombie.verticies.get((int) face.vertex.y -1);
			Vector3f v3 = zombie.verticies.get((int) face.vertex.z -1);

			Vector2f t1 = zombie.textures.get((int) face.texture.x -1);
			Vector2f t2 = zombie.textures.get((int) face.texture.y -1);
			Vector2f t3 = zombie.textures.get((int) face.texture.z -1);

			glBegin(GL_TRIANGLES);
					glTexCoord2f(t1.x,t1.y);
				glVertex3f(v1.x*manScale+x, v1.y*manScale-200, v1.z*manScale+z);
					glTexCoord2f(t2.x,t2.y);
				glVertex3f(v2.x*manScale+x, v2.y*manScale-200, v2.z*manScale+z);
					glTexCoord2f(t3.x,t3.y);
				glVertex3f(v3.x*manScale+x, v3.y*manScale-200, v3.z*manScale+z);
			glEnd();
		}
		
		//undo the world rotation we did
		//This most likely isn't the correct way to roatate a model
		glTranslatef((midX), (midY), (midZ));
		glRotatef(-(90-angle), 0.0f, 1.0f, 0.0f);
		glTranslatef(-(midX), -(midY), -(midZ));
	}	
		
	public boolean collisionShoot(float x, float y, float z) 
	{
		//Just a rectangular hitbox around the zombie
	  return (x > this.x-20.0f && x < this.x+20.0f && -y > this.y-100.0f && -y < this.y+100.0f && z > this.z-20.0f && z < this.z+20.0f);
	}

	private Texture loadTexture(String key)
	{
		try
		{
			return TextureLoader.getTexture("JPG", new FileInputStream(new File(key)));
		}
		catch(IOException ioe)
		{
			System.out.println(ioe.getMessage());
			System.exit(0);
		}	
		return null;
	}

	public static void main(String[] args)
	{
		new Boot();
	}
}