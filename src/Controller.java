import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/*
 * Controller.java
 * Handles user input and collision detection with the world
*/ 

public class Controller
{
	private World world;
	
	//player position variables
	private float forwards = 25.0f;
	private float left = 0.0f;
	private float up = 160.0f;
	private float rotX = 0;
	private float rotY = 0;

  //world position variables for collision detection
	public float leftWall = -2000;
  public float rightWall = 2000;
  public float backWall = 5000;
  public float frontWall = 0;
  public float floor = 200;

	private int selectedWeapon = 1;
  public float gunXOffset = 45;
	public float gunYOffset = -50;
	public float gunZOffset = -100;

  //player attributes
	public float mouseSpeed = 0.035f;
	public double walkSpeed = 3;
	public double runSpeed = walkSpeed * 2;
	public double crouchSpeed = walkSpeed * 0.5;
	public double moveSpeed = walkSpeed;

  public int crouchingHeight = 50;
	public int normalHeight = 75;
	public int playerHeight = normalHeight;
	public int playerBox = 30;
	private float gravity = 6f;
	private float gravityMulti = 1f;
	private float jumpSpeed = 30f;
	private float jumpHeight = 175f;
	public boolean jumping = false;
	public float currentFloor = floor;

  //keyboard input
	private boolean upArrow = false;
	private boolean downArrow = false;
	private boolean leftArrow = false;
	private boolean rightArrow = false;
	private boolean lShift = false;
	private boolean space = false;
	private boolean spaceIsStillHeld = false;
	private boolean lClickCheck = false;
	private boolean rClickCheck = false;
	private boolean crouching = false;
 
	public Controller()
	{
		Mouse.setGrabbed(true);
  }

	public void assignWorld(World world)
	{
		this.world = world;
	}

	public float getAngleX()
	{
		return rotX * mouseSpeed;
	}

	public float getAngleY()
	{
		return rotY * mouseSpeed;
	}
	
	public float getX()
	{
		return left;
	}

	public float getY()
	{
		return up;
	}

	public float getZ()
	{
		return forwards;
	}

	public float getRotX()
	{
		return rotX;
	}

	public float getRotY()
	{
		return rotY;
	}
	
	public int getSelectedWeapon()
	{
		return selectedWeapon;
	}

	public void physics()
	{
		//badly designed gravity
		if(!world.collisionJump(left, (up + gravity*gravityMulti), forwards))
		{	
			up += gravity*(gravityMulti);
			gravityMulti += 0.05;		
		}
		else
		{
			currentFloor = up;
			gravityMulti = 1f;
		}
	}

	public void UpdateInput()
	{
		generalInput();

		double angleX = rotX * mouseSpeed;
		double angleY = rotY * mouseSpeed;

		moveSpeed = walkSpeed;

		if(lShift)
			moveSpeed = runSpeed;

		if(crouching)
		{
			playerHeight = crouchingHeight;
			moveSpeed = crouchSpeed;
		}
		else
		{
			playerHeight = normalHeight;
		}

		if(upArrow)
		{
			if(!world.collision((float)(left), (float)(up), (float)(forwards + (float)(moveSpeed * (Math.cos(Math.toRadians(angleX)))))))
			{
				forwards += (float)(moveSpeed * (Math.cos(Math.toRadians(angleX))));
			}

			if(!(world.collision((float)(left + (float)(-moveSpeed * (Math.sin(Math.toRadians(angleX))))), (float)(up), (float)(forwards))))
			{
				left += (float)(-moveSpeed * (Math.sin(Math.toRadians(angleX))));
			}

			/*if(!(world.collision((float)(left), (float)(up + (float)(-moveSpeed * (Math.sin(Math.toRadians(angleY))))), (float)(forwards))))
			{
				up += (float)(-moveSpeed * (Math.sin(Math.toRadians(angleY))));
			}*/
		}

		if(downArrow)
		{
			if(!world.collision((float)(left), (float)(up), (float)(forwards - (float)(moveSpeed * (Math.cos(Math.toRadians(angleX)))))))
			{
				forwards -= (float)(moveSpeed * (Math.cos(Math.toRadians(angleX))));
			}

			if(!(world.collision((float)(left - (float)(-moveSpeed * (Math.sin(Math.toRadians(angleX))))), (float)(up), (float)(forwards))))
			{
				left -= (float)(-moveSpeed * (Math.sin(Math.toRadians(angleX))));
			}
		}

		if(leftArrow)
		{
			if(!(world.collision((float)(left), (float)(up), (float)(forwards + (float)(moveSpeed * (Math.cos(Math.toRadians(angleX - 90))))))))
			{
				forwards += (float)(moveSpeed * (Math.cos(Math.toRadians(angleX - 90))));
			}
			if(!(world.collision((float)(left + (float)(-moveSpeed * (Math.sin(Math.toRadians(angleX - 90))))), (float)(up), (float)(forwards))))
			{
				left += (float)(-moveSpeed * (Math.sin(Math.toRadians(angleX - 90))));
			}
		}

		if(rightArrow)
		{
			if(!(world.collision((float)(left), (float)(up), (float)(forwards + (float)(moveSpeed * (Math.cos(Math.toRadians(angleX + 90))))))))
			{
				forwards += (float)(moveSpeed * (Math.cos(Math.toRadians(angleX + 90))));
			}
			if(!(world.collision((float)(left + (float)(-moveSpeed * (Math.sin(Math.toRadians(angleX + 90))))), (float)(up), (float)(forwards))))
			{
				left += (float)(-moveSpeed * (Math.sin(Math.toRadians(angleX + 90))));
			}
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_1))
		{
			selectedWeapon = 1;						
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_2))
		{
			selectedWeapon = 2;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_3))
		{
			selectedWeapon = 3;
		}
		
		//mouse wheel scrolling for weapon selection
		double temp;
		
		if((temp = (double)Mouse.getDWheel()) != 0)
		{
			selectedWeapon += Math.signum(temp);
			if(selectedWeapon == 0)
			{
				selectedWeapon = 3;
			}
			if(selectedWeapon == 4)
			{
				selectedWeapon = 1;
			}	
		}

		if(Mouse.isButtonDown(0) && selectedWeapon != 1)
		{
			world.shoot();
		}

    //attach a cube onto selected cube
		if(Mouse.isButtonDown(0) && allowToBuild() && !lClickCheck)
		{
			lClickCheck = true;
			Cube selectedCube = world.getSelectedCube();
			int side = world.getSelectedSide();
			switch(side)
			{
				case 0: world.addCube(-selectedCube.x, -selectedCube.y, -(selectedCube.z - selectedCube.size));
						break;

				case 1: world.addCube(-selectedCube.x, -selectedCube.y, -(selectedCube.z + selectedCube.size));
						break;

				case 2: world.addCube(-(selectedCube.x - selectedCube.size), -selectedCube.y, -selectedCube.z);
						break;

				case 3: world.addCube(-(selectedCube.x + selectedCube.size), -selectedCube.y, -selectedCube.z);
						break;

				case 4: world.addCube(-selectedCube.x, -(selectedCube.y + selectedCube.size), -selectedCube.z);
						break;

				case 5: world.addCube(-selectedCube.x, -(selectedCube.y - selectedCube.size), -selectedCube.z);
						break;
			}
		}

		if(!Mouse.isButtonDown(0) && lClickCheck)
		{
			lClickCheck = false;
		}

		if(Mouse.isButtonDown(1) && !rClickCheck && allowToBuild())
		{
			world.removeSelectedCube();
			rClickCheck = true;
		}

		if(!Mouse.isButtonDown(1) && rClickCheck)
		{
			rClickCheck = false;
		}

		if(space && !jumping && !spaceIsStillHeld)
		{
			spaceIsStillHeld = true;
			jumping = true;
		}

		if(jumping)
		{
			up -= (float)jumpSpeed*(1-(gravityMulti-1));

			if(up < currentFloor - jumpHeight)
			{
				up = currentFloor - jumpHeight;
				jumping = false;
			}
		}

		worldColissionDetection();
	}

	public boolean allowToBuild()
	{
		Cube selectedCube = world.getSelectedCube();
		return !(selectedCube == null || selectedWeapon != 1);	
	}

	public void generalInput()
	{
		rotX += (float)Mouse.getDX();
		rotY += (float)Mouse.getDY();

		if(rotY*mouseSpeed >= 90)
			rotY = (float)(90/mouseSpeed);

		if(rotY*mouseSpeed <= -90)
			rotY = (float)(-90/mouseSpeed);

		if(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP))
			upArrow = true;
		else
			upArrow = false;

		if(Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN))
			downArrow = true;
		else
			downArrow = false;

		if(Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT))
			leftArrow = true;
		else
			leftArrow = false;

		if(Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
			rightArrow = true;
		else
			rightArrow = false;

		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			Mouse.setGrabbed(false);

		if(Keyboard.isKeyDown(Keyboard.KEY_Q))
			Mouse.setGrabbed(true);

		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			lShift = true;
		else
			lShift = false;

		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !jumping)
			space = true;
		else
			space = false;

		if(!Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			spaceIsStillHeld = false;

		if(!Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && crouching)
			up -= crouchingHeight;

		if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
			crouching = true;
		else
			crouching = false;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_Z))
			world.addZombie();
	}
	

	public void worldColissionDetection()
	{
		if(left-5 <= leftWall)
			left = leftWall + 5;

		if(left+5 >= -leftWall)
			left = -leftWall - 5;

		if(forwards+5 >= backWall)
			forwards = backWall - 5;

		if(forwards-5 <= frontWall)
			forwards = frontWall + 5;

		if(up + playerHeight >= floor)
		{
			gravityMulti = 1f;
			up = floor - playerHeight;
			currentFloor = up;
			jumping = false;
		}
	}

	public static void main(String[] args)
	{
		new Boot();
	}
}
