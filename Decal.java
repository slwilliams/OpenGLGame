import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/*
 * Decal.java
 * A class for representing a decal attached to an object
 * For now this is just a bullet hole attached to a cube
*/

public class Decal
{
	private Texture decal;
	
	private int side;
	private float xPos,yPos,zPos;
	private final int SIZE = 6;
	
	public Decal(int side, Texture decal, float x, float y, float z)
	{
		this.side = side;	
		this.decal = decal;
		this.xPos = x;
		this.yPos = y;
		this.zPos = z;
	}
	
	public void render()
	{
		glEnable(GL_BLEND);
 		glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
 		decal.bind();
 		
 		switch(side)
 		{
			case 0:	glBegin(GL_QUADS);
								glTexCoord2f(0,1);
									glVertex3f(xPos-size/2, yPos+size/2, zPos-1);
								glTexCoord2f(1,1);
									glVertex3f(xPos+size/2, yPos+size/2, zPos-1);
								glTexCoord2f(1,0);
									glVertex3f(xPos+size/2, yPos-size/2, zPos-1);
								glTexCoord2f(0,0);
									glVertex3f(xPos-size/2, yPos-size/2, zPos-1);
							glEnd();	
							break;
			case 1:	glBegin(GL_QUADS);
								glTexCoord2f(0,1);
									glVertex3f(xPos-size/2, yPos+size/2, zPos+1);
								glTexCoord2f(1,1);
									glVertex3f(xPos+size/2, yPos+size/2, zPos+1);
								glTexCoord2f(1,0);
									glVertex3f(xPos+size/2, yPos-size/2, zPos+1);
								glTexCoord2f(0,0);
									glVertex3f(xPos-size/2, yPos-size/2, zPos+1);
							glEnd();	
							break;
			case 2:	glBegin(GL_QUADS);
								glTexCoord2f(0,1);
									glVertex3f(xPos-1, yPos-size/2, zPos+size/2);
								glTexCoord2f(1,1);
									glVertex3f(xPos-1, yPos+size/2, zPos+size/2);
								glTexCoord2f(1,0);
									glVertex3f(xPos-1, yPos+size/2, zPos-size/2);
								glTexCoord2f(0,0);
									glVertex3f(xPos-1, yPos-size/2, zPos-size/2);
							glEnd();	
							break;
			case 3:	glBegin(GL_QUADS);
								glTexCoord2f(0,1);
									glVertex3f(xPos+1, yPos-size/2, zPos+size/2);
								glTexCoord2f(1,1);
									glVertex3f(xPos+1, yPos+size/2, zPos+size/2);
								glTexCoord2f(1,0);
									glVertex3f(xPos+1, yPos+size/2, zPos-size/2);
								glTexCoord2f(0,0);
									glVertex3f(xPos+1, yPos-size/2, zPos-size/2);
							glEnd();	
							break;
			case 4:	glBegin(GL_QUADS);
								glTexCoord2f(0,1);
									glVertex3f(xPos-size/2, yPos+1, zPos+size/2);
								glTexCoord2f(1,1);
									glVertex3f(xPos+size/2, yPos+1, zPos+size/2);
								glTexCoord2f(1,0);
									glVertex3f(xPos+size/2, yPos+1, zPos-size/2);
								glTexCoord2f(0,0);
									glVertex3f(xPos-size/2, yPos+1, zPos-size/2);
							glEnd();	
							break;
			case 5:	glBegin(GL_QUADS);
								glTexCoord2f(0,1);
									glVertex3f(xPos-size/2, yPos-1, zPos+size/2);
								glTexCoord2f(1,1);
									glVertex3f(xPos+size/2, yPos-1, zPos+size/2);
								glTexCoord2f(1,0);
									glVertex3f(xPos+size/2, yPos-1, zPos-size/2);
								glTexCoord2f(0,0);
									glVertex3f(xPos-size/2, yPos-1, zPos-size/2);
							glEnd();	
							break;
 		}
	}
	
	public static void main(String[] args)
	{
		new Boot();
	}
}