import static org.lwjgl.util.glu.GLU.gluPerspective;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import org.lwjgl.LWJGLException;

/*
 * OpenGL 3D game
 * Scott Williams 15/04/2012
 * Unfortunately using now depreciated OpenGL methods (GL_Begin etc)
 * Also some weird issues with co-ordinates, I think I've swaped
 * negative and positive (up and down) half way through somewhere
*/

/*
 * Boot.java
 * Boot sets up the display instance and initialises the world
 * Boot also contains the render loop
*/

public class Boot
{
	private Controller controller;
	private World world;
	private Text text;

	private final float FOV = 75;

	public Boot()
	{
		try
		{
			Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
		  //Display.setDisplayMode(new DisplayMode(800,600));
			Display.setTitle("FPS Defence v0.9");
			Display.create();
		}
		catch(LWJGLException e)
		{
			System.out.println(e.getMessage());
			return;
		}
		
		glMatrixMode(GL_PROJECTION);
		gluPerspective(FOV, (float)Display.getWidth() / (float)Display.getHeight(), 1F, 10000);
		glMatrixMode(GL_MODELVIEW);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		
		controller = new Controller();
		world = new World(controller);
		text = new Text();
		
		world.setUpFog();
		
		controller.assignWorld(world);
		world.assignTexturers();
		world.assignText(text);
		text.assignTextures();			
	
	  //lighting is turning out to be quite complicated
		//world.setUpLighting();
				
		while(!Display.isCloseRequested())
		{
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			controller.UpdateInput();
			controller.physics();

			glPushMatrix();
				glRotatef((float)(controller.getRotY() * -controller.mouseSpeed), 1.0f, 0.0f, 0.0f);
				glRotatef((float)(controller.getRotX() * controller.mouseSpeed), 0.0f, 1.0f, 0.0f);
			
				//world.updateLightArrays();
				
				glTranslatef(controller.getX(), controller.getY(), controller.getZ());

				world.checkSelected();
	
				world.renderCubes();				
				world.renderZombies();
				world.renderWorld();
				world.renderHud();
			glPopMatrix();

			Display.update();
			Display.sync(60);
		}
		
		Display.destroy();
	}

	public static void main(String[] args)
	{
		new Boot();
	}
}
