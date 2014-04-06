import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/*
 * Text.java
 * This class allows drawing of arbitrary strings onto the HUD
*/

public class Text
{
	private Texture a;
	private Texture b;
	private Texture c;
	private Texture d;
	private Texture e;
	private Texture f;
	private Texture g;
	private Texture h;
	private Texture i;
	private Texture j;
	private Texture k;
	private Texture l;
	private Texture m;
	private Texture n;
	private Texture o;
	private Texture p;
	private Texture q;
	private Texture r;
	private Texture s;
	private Texture t;
	private Texture u;
	private Texture v;
	private Texture w;
	private Texture x;
	private Texture y;
	private Texture z;
	private Texture space;
	private Texture one;
	private Texture two;
	private Texture three;
	private Texture four;
	private Texture colon;	
	private int width = 16;
	private int height = 16;

	public void assignTextures()
	{
		a = loadTexture("Text/a.png");
		b = loadTexture("Text/b.png");
		c = loadTexture("Text/c.png");
		d = loadTexture("Text/d.png");
		e = loadTexture("Text/e.png");
		f = loadTexture("Text/f.png");
		g = loadTexture("Text/g.png");
		h = loadTexture("Text/h.png");
		i = loadTexture("Text/i.png");
		j = loadTexture("Text/j.png");
		k = loadTexture("Text/k.png");
		l = loadTexture("Text/l.png");
		m = loadTexture("Text/m.png");
		n = loadTexture("Text/n.png");
		o = loadTexture("Text/o.png");
		p = loadTexture("Text/p.png");
		q = loadTexture("Text/q.png");
		r = loadTexture("Text/r.png");
		s = loadTexture("Text/s.png");
		t = loadTexture("Text/t.png");
		u = loadTexture("Text/u.png");
		v = loadTexture("Text/v.png");
		w = loadTexture("Text/w.png");
		x = loadTexture("Text/x.png");
		y = loadTexture("Text/y.png");
		z = loadTexture("Text/z.png");
		space = loadTexture("Text/space.png");
		colon = loadTexture("Text/colon.png");
		one = loadTexture("Text/1.png");
		two = loadTexture("Text/2.png");
		three = loadTexture("Text/3.png");
		four = loadTexture("Text/4.png");		
	}

	public void drawString(String text, int xPos, int yPos)
	{
		glEnable(GL_BLEND);
 		glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
		char[] chars = text.toCharArray();
		for(int it = 0; it < chars.length; it ++)
		{
			switch(chars[it])
			{
				case 'a':	a.bind();						
							break;
				case 'b':	b.bind();						
							break;
				case 'c':	c.bind();
							break;
				case 'd':	d.bind();						
							break;
				case 'e':	e.bind();
							break;
				case 'f':	f.bind();
							break;
				case 'g':	g.bind();
							break;
				case 'h':	h.bind();
							break;
				case 'i':	i.bind();
							break;
				case 'j':	j.bind();
							break;
				case 'k':	k.bind();
							break;
				case 'l':	l.bind();
							break;
				case 'm':	m.bind();
							break;
				case 'n':	n.bind();
							break;				
				case 'o':	o.bind();
							break;
				case 'p':	p.bind();
							break;
				case 'q':	q.bind();
							break;
				case 'r':	r.bind();
							break;
				case 's':	s.bind();
							break;
				case 't':	t.bind();
							break;	
				case 'u':	u.bind();
							break;	
				case 'v':	v.bind();
							break;
				case 'w':	w.bind();
							break;
				case 'x':	x.bind();
							break;
				case 'y':	y.bind();
							break;	
				case 'z':	z.bind();
							break;	
				case ' ':   space.bind();
							break;
				case ':':   colon.bind();
							break;
				case '1':   one.bind();
							break;
				case '2':   two.bind();
							break;
				case '3':   three.bind();
							break;
				case '4':   four.bind();
							break;					
			}
			glBegin(GL_QUADS);
					glTexCoord2f(0,1);
				glVertex3f(xPos+(width*(it+1)), yPos, -300);
					glTexCoord2f(1,1);
				glVertex3f(xPos+(width*(it+1))+width, yPos, -300);
					glTexCoord2f(1,0);
				glVertex3f(xPos+width+(width*(it+1)), yPos+width, -300);
					glTexCoord2f(0,0);
				glVertex3f(xPos+(width*(it+1)), yPos+width, -300);
			glEnd();
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