import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;
import java.awt.Color;

public class PainterApplet extends Applet
{
	Graphics g;
	
	abstract class GeoShape
	{
		protected int initX, initY, finalX, finalY, type;
		protected Color color;
		protected boolean solid;
		
		public GeoShape(){};
		
		public GeoShape(int x1, int y1, int x2, int y2, Color c, boolean s, int t)
		{
			initX = x1;
			initY = y1;
			finalX = x2;
			finalY = y2;
			color = c;
			solid = s;
			type = t;
		}
		
		public void setInitX(int x){initX = x;}
		public void setInitY(int y){initY = y;}
		public void setFinalX(int x){finalX = x;}
		public void setFinalY(int y){finalY = y;}
		public void setColor(Color c){color = c;}
		public void setFinalY(boolean s){solid = s;}
		public void setType(int t){type = t;}
		
		public int getInitX(){return initX;}
		public int getInitY(){return initY;}
		public int getFinalX(){return finalX;}
		public int getFinalY(){return finalY;}
		public Color getColor(){return color;}
		public boolean getSolid(){return solid;}
		public int getType(){return type;}
		
		public abstract void paintShape(Graphics g);
	}
	
	class Rectangle extends GeoShape
	{
		protected int height, width;
		
		public Rectangle(){};
		
		public Rectangle(int x1, int y1, int x2, int y2, Color c, boolean s, int t)
		{
			super(((x1 < x2)? x1: x2), ((y1 < y2)? y1: y2), ((x2 < x1)? x1: x2), ((y2 < y1)? y1: y2), c, s, t);
			height = Math.abs(y1 - y2);
			width = Math.abs(x1 - x2);
		}
		
		public void setHeight(int h){height = h;}
		public void setWidth(int w){width = w;}
		
		public int getHeight(){return height;}
		public int getWidth(){return width;}
		
		public void paintShape(Graphics g)
		{
			g.setColor(super.getColor());
			if(super.getSolid())
				g.fillRect(super.getInitX(), super.getInitY(), width, height);
			else
				g.drawRect(super.getInitX(), super.getInitY(), width, height);
		}
	}
	
	class Oval extends GeoShape
	{
		protected int height, width;
		
		public Oval(){};
		
		public Oval(int x1, int y1, int x2, int y2, Color c, boolean s, int t)
		{
			super(((x1 < x2)? x1: x2), ((y1 < y2)? y1: y2), ((x2 < x1)? x1: x2), ((y2 < y1)? y1: y2), c, s, t);
			height = Math.abs(y1 - y2);
			width = Math.abs(x1 - x2);
		}
		
		public void setHeight(int h){height = h;}
		public void setWidth(int w){width = w;}
		
		public int getHeight(){return height;}
		public int getWidth(){return width;}
		
		public void paintShape(Graphics g)
		{
			g.setColor(super.getColor());
			if(super.getSolid())
				g.fillOval(super.getInitX(), super.getInitY(), width, height);
			else
				g.drawOval(super.getInitX(), super.getInitY(), width, height);
		}
	}
	
	class Line extends GeoShape
	{	
		public Line(){};
		
		public Line(int x1, int y1, int x2, int y2, Color c, boolean s, int t)
		{
			super(x1, y1, x2, y2, c, s, t);
		}
		
		public void paintShape(Graphics g)
		{
			g.setColor(super.getColor());
			g.drawLine(super.getInitX(), super.getInitY(), super.getFinalX(), super.getFinalY());
		}
	}
	
	class FreeHand extends GeoShape
	{
		Vector<GeoShape> vl;
		public FreeHand(){vl = new Vector<GeoShape>();}
		
		public void addLine(Line l){vl.add(l);}
		public void paintShape(Graphics g)
		{
			for (int i = 0; i < vl.size(); i++)
			{
				GeoShape v = vl.get(i);
				v.paintShape(g);
			}
		}
	}
	
	class Eraser extends GeoShape
	{
		Vector<GeoShape> ve;
		public Eraser(){ve = new Vector<GeoShape>();}
		
		public void addRect(Rectangle r){ve.add(r);}
		public void paintShape(Graphics g)
		{
			for (int i = 0; i < ve.size(); i++)
			{
				GeoShape v = ve.get(i);
				v.paintShape(g);
			}
		}
	}
	
	private GeoShape gs;
	private FreeHand fh;
	private Eraser er;
	private Vector<GeoShape> v;
	private Button bOval, bRectangle, bLine, bRed, bGreen, bBlue, bClearAll, bFreeHand, bEraser, bUndo;
	private Checkbox cFill;
	private int x1, y1, x2, y2, t = -1;
	private Color c = Color.BLACK;
	private boolean s, drag, cleared;
	
	public void init()
	{
		v = new Vector<GeoShape>();
		
		bRed = new Button("Red");
		bGreen = new Button("Green");
		bBlue = new Button("Blue");
		bFreeHand = new Button("Free Hand");
		bEraser = new Button("Eraser");
		
		bRed.setBackground(Color.RED);
		bGreen.setBackground(Color.GREEN);
		bBlue.setBackground(Color.BLUE);
		
		bRed.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent ev){c = Color.RED;}});
		bGreen.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent ev){c = Color.GREEN;}});
		bBlue.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent ev){c = Color.BLUE;}});
		
		add(bRed);
		add(bGreen);
		add(bBlue);
	
		bOval = new Button("Oval");
		bRectangle = new Button("Rectangle");
		bLine = new Button("Line");
		
		bRectangle.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent ev){t = 0;}});
		bOval.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent ev){t = 1;}});
		bLine.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent ev){t = 2;}});
		bFreeHand.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent ev){t = 3;}});
		bEraser.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent ev){t = 4;}});
		
		add(bOval);
		add(bRectangle);
		add(bLine);
		add(bFreeHand);
		add(bEraser);
		
		cFill = new Checkbox("Fill");
		cFill.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent ev)
			{
				int state = ev.getStateChange();
				if(state == 2)
					s = false;
				else
					s = true;
			}
		});
		
		add(cFill);
		
		bClearAll = new Button("Clear All");
		bClearAll.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ev)
			{
				v.clear();
				cleared = true;
				repaint();
			}
		});
		add(bClearAll);
		
		bUndo = new Button("Undo");
		bUndo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ev)
			{
				if(v.size() > 0)
					v.removeElementAt(v.size() - 1);
				cleared = true;
				repaint();
			}
		});
		add(bUndo);
		
		this.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				cleared = false;
				x1 = e.getX();
				y1 = e.getY();
				switch(t)
				{
					case 3:
					fh = new FreeHand();
						break;
					case 4:
					er = new Eraser();
						break;
				}
			}
		
			public void mouseReleased(MouseEvent e)
			{
				if(drag)
				{
					switch(t)
					{
						case 0:
							gs = new Rectangle(x1, y1, x2, y2, c, s, t);
							break;
						case 1:
							gs = new Oval(x1, y1, x2, y2, c, s, t);
							break;
						case 2:
							gs = new Line(x1, y1, x2, y2, c, s, t);
							break;
						case 3:
							gs = fh;
							break;
						case 4:
							gs = er;
							break;
					}
					
					if(t != -1)
						v.add(gs);
					
					repaint();
					drag = false;
				}
			}
		});	
		
		this.addMouseMotionListener(new MouseAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				x2 = e.getX();
				y2 = e.getY();
				drag = true;
				
				switch(t)
				{
					case 3:
						fh.addLine(new Line(x1, y1, x2, y2, c, true, t));
						break;
					case 4:
						er.addRect(new Rectangle(x1, y1, x1 + 50, y1 + 50, Color.WHITE, true , t));
						break;
				}
				if (t == 3 || t == 4)
				{
					x1 = x2;
					y1 = y2;
				}
				
				repaint();
			}
		});
	}
	
	public void paint(Graphics g)
	{
		
		for(int i = 0; i < v.size(); i++)
			v.get(i).paintShape(g);
		
		g.setColor(c);
		if(!cleared)
		{
			switch(t)
			{
				case 0:
					if(s)
						g.fillRect(((x1 < x2)? x1: x2), ((y1 < y2)? y1: y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
					else
						g.drawRect(((x1 < x2)? x1: x2), ((y1 < y2)? y1: y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
						break;
				case 1:
					if(s)
						g.fillOval(((x1 < x2)? x1: x2), ((y1 < y2)? y1: y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
					else
						g.drawOval(((x1 < x2)? x1: x2), ((y1 < y2)? y1: y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
						break;
				case 2:
					g.drawLine(x1, y1, x2, y2);
					break;
				case 3:
					fh.paintShape(g);
					break;
				case 4:
					g.setColor(Color.WHITE);
					er.paintShape(g);
					break;
			}
		}
	}
}