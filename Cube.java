import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.util.ArrayList;

/*
 * Cube.java
 * A cube which players can place / remove in game
*/

public class Cube
{
	public int x, y, z;
	public int size = 50;
	public boolean[] selectedSide = new boolean[6];

	private Texture sideTexture;
	private ArrayList<Decal> decals = new ArrayList<Decal>();

	public Cube(String textureName, int x, int y, int z)
	{		
		this.x = x;
		this.y = y;
		this.z = z;		
		this.sideTexture = loadTexture(textureName);	
	}
	
	public boolean collision(float playerX, float playerY, float playerZ, int playerHeight, int playerBox)
	{
		return (playerX >= x - (size/2) - playerBox && playerX <= x + (size/2) + playerBox && playerY >= y - (size/2) && playerY <= y + (size/2) + playerHeight && playerZ >= z - (size/2) - playerBox && playerZ <= z + (size/2) + playerBox);	
	}	
		
	public boolean collisionLooking(float playerX, float playerY, float playerZ)
	{
		//used for ray tracing collision detection
		return (playerX >= x-(size/2) && playerX <= x + (size/2) && playerY >= y - (size/2) && playerY <= y + (size/2) && playerZ >= z -(size/2) && playerZ <= z + (size/2));
	}
	
	public void addDecal(int side, Texture decal, float xPos, float yPos, float zPos)
	{
		decals.add(new Decal(side, decal, xPos, yPos, zPos));
	}	
		
	public void renderDecals()
	{
		for(Decal decal : decals)
		{
			decal.render();
		}
	}
	
	public int[] getSideMid(int side)
	{
		switch(side)
		{
			case 0:		return new int[]{x,y,(int)(z-(size/2))};
			case 1:		return new int[]{x,y,(int)(z+(size/2))};
			case 2:		return new int[]{(int)(x-(size/2)),y,z};
			case 3:		return new int[]{(int)(x+(size/2)),y,z};
			case 4:		return new int[]{x,(int)(y+(size/2)),z};
			case 5:		return new int[]{x,(int)(y-(size/2)),z};			
		}		
		return null;
	}

	public void render()
	{	
		glColor3f(1.0f,1.0f,1.0f); 	
		sideTexture.bind();		
		
		//front face
		if(selectedSide[1])
			glColor3f(1.0f,0.0f,0.0f); 

		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex3f((float)(x + (size/2)), (float)(y + (size/2)), (float)(z + (size/2)));
			glTexCoord2f(1, 0);
			glVertex3f((float)(x - (size/2)), (float)(y + (size/2)), (float)(z + (size/2)));
			glTexCoord2f(1, 1);
			glVertex3f((float)(x - (size/2)), (float)(y - (size/2)), (float)(z + (size/2)));
			glTexCoord2f(0, 1);
			glVertex3f((float)(x + (size/2)), (float)(y - (size/2)), (float)(z + (size/2)));
		glEnd();
		glColor3f(1.0f,1.0f,1.0f);

		//back face
		if(selectedSide[0])
			glColor3f(1.0f,0.0f,0.0f); 
			
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex3f((float)(x - (size/2)), (float)(y - (size/2)), (float)(z - (size/2)));
			glTexCoord2f(1, 0);
			glVertex3f((float)(x - (size/2)), (float)(y + (size/2)), (float)(z - (size/2)));
			glTexCoord2f(1, 1);
			glVertex3f((float)(x + (size/2)), (float)(y + (size/2)), (float)(z - (size/2)));
			glTexCoord2f(0, 1);
			glVertex3f((float)(x + (size/2)), (float)(y - (size/2)), (float)(z - (size/2)));
		glEnd();
		glColor3f(1.0f,1.0f,1.0f);  	

		//left face
		if(selectedSide[2])
			glColor3f(1.0f,0.0f,0.0f); 
		
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex3f((float)(x - (size/2)), (float)(y + (size/2)), (float)(z + (size/2)));
			glTexCoord2f(1, 0);
			glVertex3f((float)(x - (size/2)), (float)(y + (size/2)), (float)(z - (size/2)));
			glTexCoord2f(1, 1);
			glVertex3f((float)(x - (size/2)), (float)(y - (size/2)), (float)(z - (size/2)));
			glTexCoord2f(0, 1);
			glVertex3f((float)(x - (size/2)), (float)(y - (size/2)), (float)(z + (size/2)));
		glEnd();
		glColor3f(1.0f,1.0f,1.0f);  	

		//right face
		if(selectedSide[3])
			glColor3f(1.0f,0.0f,0.0f); 
		
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex3f((float)(x + (size/2)), (float)(y - (size/2)), (float)(z - (size/2)));
			glTexCoord2f(1, 0);
			glVertex3f((float)(x + (size/2)), (float)(y + (size/2)), (float)(z - (size/2)));
			glTexCoord2f(1, 1);
			glVertex3f((float)(x + (size/2)), (float)(y + (size/2)), (float)(z + (size/2)));
			glTexCoord2f(0, 1);
			glVertex3f((float)(x + (size/2)), (float)(y - (size/2)), (float)(z + (size/2)));
		glEnd();
		glColor3f(1.0f,1.0f,1.0f);  	

		//top face
		if(selectedSide[4])
			glColor3f(1.0f,0.0f,0.0f); 
			
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex3f((float)(x - (size/2)), (float)(y + (size/2)), (float)(z - (size/2)));
			glTexCoord2f(1, 0);
			glVertex3f((float)(x - (size/2)), (float)(y + (size/2)), (float)(z + (size/2)));
			glTexCoord2f(1, 1);
			glVertex3f((float)(x + (size/2)), (float)(y + (size/2)), (float)(z + (size/2)));
			glTexCoord2f(0, 1);
			glVertex3f((float)(x + (size/2)), (float)(y + (size/2)), (float)(z - (size/2)));
		glEnd();
		glColor3f(1.0f,1.0f,1.0f);  	

		//bottom face
		if(selectedSide[5])
			glColor3f(1.0f,0.0f,0.0f); 

		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex3f((float)(x + (size/2)), (float)(y - (size/2)), (float)(z + (size/2)));
			glTexCoord2f(1, 0);
			glVertex3f((float)(x - (size/2)), (float)(y - (size/2)), (float)(z + (size/2)));
			glTexCoord2f(1, 1);
			glVertex3f((float)(x - (size/2)), (float)(y - (size/2)), (float)(z - (size/2)));
			glTexCoord2f(0, 1);
			glVertex3f((float)(x + (size/2)), (float)(y - (size/2)), (float)(z - (size/2)));
		glEnd();
		glColor3f(1.0f,1.0f,1.0f);  	
		
		clearSelectedSide();		
	}	
	
	private void clearSelectedSide()
	{
		for(int i = 0; i < selectedSide.length; i ++)
		{
			selectedSide[i] = false;
		}
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
