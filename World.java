import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.BufferUtils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.FloatBuffer;

import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/*
 * World.java
 * This class represents the game world and contains all game entities
*/

public class World
{
	private Controller controller;
  private Text text;

  private Model gun;
	private Model knife;
  private Model man;
  private Model m4;
  private Model zombieM;
  
	private Texture grass;
	private Texture stone;
	private Texture gunT;
	private Texture manT;
	private Texture knifeT;
	private Texture m4T;
	private Texture bulletHole;
	  
  private ArrayList<Cube> cubes = new ArrayList<Cube>();
  private ArrayList<Zombie> zombies = new ArrayList<Zombie>();  
  	
  private Cube selectedCube = null;  
  private int selectedSide = 0;
  public final int CUBE_SIZE = 50;
  
  private float[] lightPosition = { -2.19f, 1.36f, 11.45f, 1f };
	private double lightPos = 0;
		
  public World(Controller controller)
	{
		this.controller = controller;
		
		gun = ObjLoader.loadModel(new File("gun.obj"));
		knife = ObjLoader.loadModel(new File("knife2.obj"));
		m4 = ObjLoader.loadModel(new File("m4.obj"));		
		zombieM = ObjLoader.loadModel(new File("man.obj"));		
		
		//place starting cube
		addCube(0,200-CUBE_SIZE/2,500);
	}
	
	public void assignTexturers()
	{
		grass = loadTexture("grassTop.png");
		stone = loadTexture("stoneSide.png");
		gunT = loadTexture("gun.jpg");
		manT = loadTexture("man.png");
		knifeT = loadTexture("knife.png");
		m4T = loadTexture("m4.png");
		bulletHole = loadTexture("bulletHole.png");		
	}
	
	public void assignText(Text text)
	{
		this.text = text;
	}
	
	public int getSelectedSide()
	{
		return selectedSide;
	}

	public Cube getSelectedCube()
	{
		return selectedCube;
	}

	public void addCube(int x, int y, int z)
	{
		Cube tempCube = new Cube("stoneTop.png", -x, -y, -z);
		if(!collision(-x,-y,-z) && !collision((int)controller.getX(), (int)controller.getY(), (int)controller.getZ(), tempCube))
		{
			cubes.add(tempCube);
		}
	}

	public void removeSelectedCube()
	{
		cubes.remove(getSelectedCube());
	}
	
	public void addZombie()
	{
		//adds a zombie where the player is looking
		float newX = 0, newZ = 0;
		newX = -(controller.getX() + (float)(-(100) * (Math.sin(Math.toRadians(controller.getAngleX())))*(Math.cos(Math.toRadians(controller.getAngleY())))));
		newZ = -(controller.getZ() + (float)((100) * (Math.cos(Math.toRadians(controller.getAngleX())))*(Math.cos(Math.toRadians(controller.getAngleY())))));
		zombies.add(new Zombie(controller, zombieM, newX, newZ));
	}

	public void renderCubes()
	{
		for(Cube cube : cubes)
		{
			cube.render();
			cube.renderDecals();
		}
	}
	
	public boolean collision(float x, float y, float z, Cube cube)
	{
		//collision detection for a given cube
		return cube.collision(-x, -y, -z, controller.playerHeight, controller.playerBox);
	}

	public boolean collisionJump(float x, float y, float z)
	{
		boolean collision = false;
		for(Cube cube : cubes)
		{
			if(cube.collision(-x, -y, -z, controller.playerHeight, controller.playerBox))
			{
				collision = true;
				controller.jumping = false;
				break;
			}
		}
		return collision;
	}

	public boolean collisionLooking(float x, float y, float z)
	{
		//collision for selecting a cube being looked at
		boolean collision = false;
		for(Cube cube : cubes)
		{
			if(cube.collisionLooking(-x,-y,-z))
			{
				collision = true;
				break;
			}
		}
		return collision;
	}

	public boolean collision(float x, float y, float z)
	{
		//collision detection for a player walking into a cube
		boolean collision = false;
		for(Cube cube : cubes)
		{
			//a slightly larger bounding box is given to the player
			//this makes it seem like the player is 3D
			if(cube.collision(-x, -y, -z, controller.playerHeight, controller.playerBox))
			{
				collision = true;
				break;
			}
		}
		return collision;
	}

	public Cube getCube(int x, int y, int z)
	{
		//returns a cube if x,y,z lie within it
		Cube returnCube = null;
		for(Cube cube : cubes)
		{
			if(cube.collisionLooking(-x, -y, -z))
			{
				returnCube = cube;
				break;
			}
		}
		return returnCube;
	}
	
	public void checkSelected()
	{
		//this method highlights a cube if it is being looked at
		//I use a method of ray tracing to check along the players line of sight
		if(controller.getSelectedWeapon() != 1)
			return;

		boolean anythingSelected = false;
		int maxIterations = 500;
		
		float newX = 0, newY = 0, newZ = 0;
		
		for(int i = 0; i < maxIterations; i += 1)
		{
			newX = -(controller.getX() + (float)(-(i) * (Math.sin(Math.toRadians(controller.getAngleX())))*(Math.cos(Math.toRadians(controller.getAngleY())))));
			newY = -(controller.getY() + (float)(-(i) * (Math.sin(Math.toRadians(controller.getAngleY())))));
			newZ = -(controller.getZ() + (float)((i) * (Math.cos(Math.toRadians(controller.getAngleX())))*(Math.cos(Math.toRadians(controller.getAngleY())))));

			if(collisionLooking(-newX, -newY, -newZ))
			{
				Cube selected = getCube(-(int)newX, -(int)newY, -(int)newZ);
				int[][] sides = new int[6][3];
				float[] differences = new float[6];
				for(int j = 0; j < sides.length; j ++)
				{
					sides[j] = selected.getSideMid(j);
				}

				for(int j = 0; j < sides.length; j ++)
				{
					differences[j] = (float)Math.sqrt(Math.pow((sides[j][0] - newX), 2) + Math.pow((sides[j][1] - newY), 2) + Math.pow((sides[j][2] - newZ), 2) );
				}

				float smallest = differences[0];
				for(int j = 0; j < differences.length; j ++)
				{
					if(differences[j] < smallest)
						smallest = differences[j];
				}

				int side = -1;

				for(int j = 0; j < differences.length; j ++)
				{
					if(differences[j] == smallest)
						side = j;
				}

				selected.selectedSide[side] = true;

				selectedCube = selected;
				selectedSide = side;
				anythingSelected = true;

				break;
			}
		}

		if(!anythingSelected)
		{
			selectedCube = null;
			selectedSide = -1;
		}
	}
	
	public void shoot()
	{
		//this is very similar to the above method
		int maxIterations = 500;
		float newX = 0, newY = 0, newZ = 0;
		
		for(int i = 0; i < maxIterations; i += 1)
		{
			newX = -(controller.getX() + (float)(-(i) * (Math.sin(Math.toRadians(controller.getAngleX())))*(Math.cos(Math.toRadians(controller.getAngleY())))));
			newY = -(controller.getY() + (float)(-(i) * (Math.sin(Math.toRadians(controller.getAngleY())))));
			newZ = -(controller.getZ() + (float)((i) * (Math.cos(Math.toRadians(controller.getAngleX())))*(Math.cos(Math.toRadians(controller.getAngleY())))));
				
			if(checkShootZombie(newX,newY,newZ))
				break;
		}				
	}
	
	private boolean checkShootZombie(float x, float y, float z)
	{
		Zombie shot = null;
		for(Zombie zombie : zombies) 
		{
			if(zombie.collisionShoot(x,y,z))
			{
				shot = zombie;
				break;
			}
		}
		if(shot != null) 
		{
			zombies.remove(shot);
			return true;
		}
		return false;
	}

	private FloatBuffer asFloatBuffer(float... values)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		return buffer;
	}

	public void renderWorld()
	{
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glColor3f(1f,1f,1f);

		//floor
		grass.bind();
		glBegin(GL_QUADS);
				glTexCoord2f(0,0);
			glVertex3f(2000,-200,-5000);
				glTexCoord2f(50f,0);
			glVertex3f(-2000,-200,-5000);
				glTexCoord2f(50f,50f);
			glVertex3f(-2000,-200,0);
				glTexCoord2f(0,50f);
			glVertex3f(2000,-200,0);
		glEnd();
		
		//roof
		stone.bind();
		glBegin(GL_QUADS);
				glTexCoord2f(0,0);
			glVertex3f(2000,1000,-5000);
				glTexCoord2f(20f,0);
			glVertex3f(-2000,1000,-5000);
				glTexCoord2f(20f,20f);
			glVertex3f(-2000,1000,0);
				glTexCoord2f(0,20f);
			glVertex3f(2000,1000,0);
		glEnd();

		//left wall
		stone.bind();
		glBegin(GL_QUADS);
				glTexCoord2f(10f,30f);
			glVertex3f(-2000,1000,-5000);
				glTexCoord2f(10f,0);
			glVertex3f(-2000,1000,0);
				glTexCoord2f(0,0);
			glVertex3f(-2000,-200,0);
				glTexCoord2f(0,30f);
			glVertex3f(-2000,-200,-5000);
		glEnd();

		//right wall
		glBegin(GL_QUADS);
				glTexCoord2f(0,0);
			glVertex3f(2000,-200,0);
				glTexCoord2f(10f,0);
			glVertex3f(2000,1000,0);
				glTexCoord2f(10f,30f);
			glVertex3f(2000,1000,-5000);
				glTexCoord2f(0,30f);
			glVertex3f(2000,-200, -5000);
		glEnd();

		//back wall
		glBegin(GL_QUADS);
				glTexCoord2f(10f,30f);
			glVertex3f(2000,1000,-5000);
				glTexCoord2f(10f,0);
			glVertex3f(-2000,1000,-5000);
				glTexCoord2f(0,0);
			glVertex3f(-2000,-200,-5000);
				glTexCoord2f(0,30f);
			glVertex3f(2000,-200,-5000);
		glEnd();

		//front wall
		glBegin(GL_QUADS);
				glTexCoord2f(0,0);
			glVertex3f(-2000,-200,0);
				glTexCoord2f(10f,0);
			glVertex3f(-2000,1000,0);
				glTexCoord2f(10f,30f);
			glVertex3f(2000,1000,0);
				glTexCoord2f(0,30f);
			glVertex3f(2000,-200,0);
		glEnd();
	}
	
	public void renderZombies()
	{
		for(Zombie zombie : zombies)
		{
			zombie.calculateMid();
			zombie.updatePosition();
			zombie.drawZombie();
		}
	}
	
	public void renderHud()
	{
		glClear(GL_DEPTH_BUFFER_BIT);
		glTranslatef(-controller.getX(), -controller.getY(), -controller.getZ());
		glRotatef((float)(-controller.getRotX() * controller.mouseSpeed), 0.0f, 1.0f, 0.0f);
		glRotatef((float)(-controller.getRotY() * -controller.mouseSpeed), 1.0f, 0.0f, 0.0f);

		glColor3f(0f,1.0f,0f);
		glLineWidth(2f);

		glBegin(GL_LINES);
			glVertex3f(-1.5f,0f,-50f);
			glVertex3f(-0.5f,0f,-50f);

			glVertex3f(0.5f,0f,-50f);
			glVertex3f(1.5f,0f,-50f);

			glVertex3f(0f,-1.5f,-50f);
			glVertex3f(0f,-0.5f,-50f);

			glVertex3f(0f,0.5f,-50f);
			glVertex3f(0f,1.5f,-50f);
		glEnd();
		
		glColor3f(1f,1f,1f);
		
		Model currentWeapon = null;
		Texture weaponTexture = null;
		
		switch(controller.getSelectedWeapon())
		{
			case 1:  currentWeapon = knife;
					 weaponTexture = knifeT;
					 break;
			case 2:  currentWeapon = gun;
					 weaponTexture = gunT;
					 break;
			case 3:  currentWeapon = m4;
					 weaponTexture = m4T;
					 break;
		}

		weaponTexture.bind();

		for(Face face : currentWeapon.faces)
		{
			Vector3f v1 = currentWeapon.verticies.get((int) face.vertex.x -1);
			Vector3f v2 = currentWeapon.verticies.get((int) face.vertex.y -1);
			Vector3f v3 = currentWeapon.verticies.get((int) face.vertex.z -1);

			Vector2f t1 = currentWeapon.textures.get((int) face.texture.x -1);
			Vector2f t2 = currentWeapon.textures.get((int) face.texture.y -1);
			Vector2f t3 = currentWeapon.textures.get((int) face.texture.z -1);

			glBegin(GL_TRIANGLES);
					glTexCoord2f(t1.x,t1.y);
				glVertex3f(v1.x+controller.gunXOffset, v1.y+controller.gunYOffset, v1.z+controller.gunZOffset);
					glTexCoord2f(t2.x,t2.y);
				glVertex3f(v2.x+controller.gunXOffset, v2.y+controller.gunYOffset, v2.z+controller.gunZOffset);
					glTexCoord2f(t3.x,t3.y);
				glVertex3f(v3.x+controller.gunXOffset, v3.y+controller.gunYOffset, v3.z+controller.gunZOffset);
			glEnd();
		}

		text.drawString("health:123   armor:123", -300, -200);
	}
	
	public void setUpFog()
	{
		glEnable(GL_FOG);
		
		FloatBuffer fogColor = BufferUtils.createFloatBuffer(4);
    fogColor.put(0.1f).put(0.1f).put(0.1f).put(1.0f).flip();
      
		glFogi(GL_FOG_MODE, GL_LINEAR);
    glFog(GL_FOG_COLOR, fogColor);
    glFogf(GL_FOG_DENSITY, 0.005f);
    glHint(GL_FOG_HINT, GL_NICEST);
    glFogf(GL_FOG_START, 300.0f);
    glFogf(GL_FOG_END, 2000.0f);
      
    glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
	}

	public void setUpLighting()
	{
		glShadeModel(GL_SMOOTH);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glLightModel(GL_LIGHT_MODEL_AMBIENT, asFloatBuffer(new float[] { 1.00f, 1.00f, 1.00f, 1f }));
		glLight(GL_LIGHT0, GL_POSITION, asFloatBuffer(new float[] { 10000, 0, 0, 1 }));
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_DIFFUSE);
	}

	public void updateLightArrays()
	{
		lightPos += 0.5;
		glLight(GL_LIGHT0, GL_POSITION, asFloatBuffer(lightPosition));
		lightPosition = new float[] { (float)(Math.sin(lightPos/100)*5000), -150, 700, 1f };
		addCube((int)(Math.sin(lightPos/100)*5000), -150, 700);
	}

	private Texture loadTexture(String key)
	{
		try
		{
			return TextureLoader.getTexture("JPG", new FileInputStream(new File(key)));
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return null;
	}

	public static void main(String[] args)
	{
		new Boot();
	}
}
