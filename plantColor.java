//Megan Dwyer
//Plant Simulation in Color

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;
import java.util.*;


public class plantColor
{
	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	private static ImageFrame frame;

	public static void main( String[] args )
	{
		frame = new ImageFrame( WIDTH, HEIGHT );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setVisible( true );
	}

}	
	//############################################
	
class ImageFrame extends JFrame
{
	private final JFileChooser chooser;
	private static int stems;
	private static int steps;
	private static double alpha;
	private static double theta;
	private static double beta;
	private static double tal;
	private static double r;
	private static double x;
	private static double y;
	private static double deltaTheta;
	private static double rDelta;
	private static int direction;
	public static BufferedImage bufferedImage;
	private static int TYPE_INT_ARGB;
	private static int w = 100;
	private static int h = 100;
	ArrayList<Double> xPositions = new ArrayList<Double>();
	ArrayList<Double> yPositions = new ArrayList<Double>();
	ArrayList<Double> thetas = new ArrayList<Double>();
	ArrayList<Double> rs = new ArrayList<Double>();
	ArrayList<Integer> directions = new ArrayList<Integer>();
	private static final int black = 0xff000000;
	private static float currentStroke = 6;
	private static int color1N;
	private static int color2N;
	private static String f1 = "0xFF660000";
	private static String f2 = "0xFF99FF00";
	
	//=====================================
	//constructor

	public  ImageFrame( int width, int height)
	{
		//-----------------------------
		//setup the frame's attributes
	
		this.setTitle("Plant Simulation in Color - Megan Dwyer");
		this.setSize( width, height);
		w = width;
		h = height;
		
		//add a menu to the frame
		addMenu();
	
		//-----------------------------
		//setup the file chooser dialogue

		chooser = new JFileChooser();
		chooser.setCurrentDirectory( new File( "." ) );
	}
	private void addMenu()
	{
		//------------------------------------
		//setup the frame's menu bar

		// === file menu
		JMenu fileMenu = new JMenu("File");

		JMenuItem makePlant = new JMenuItem("Directed random walk plant");
		makePlant.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent event )
			{
				startPlant();
			}
		});
		fileMenu.add( makePlant);
		
		// === attach Make Gradient to a menu bar

		JMenuItem exitItem = new JMenuItem("exit");
		exitItem.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent event )
			{
				System.exit( 0 );
			}
		});
		fileMenu.add( exitItem );

		// === attach menu to a menu bar
	


		JMenuBar menuBar =  new JMenuBar();
		menuBar.add( fileMenu );

		this.setJMenuBar( menuBar );
	}
	
	
	//Open a dialogue box and prompt input from user
	public void startPlant()
	{		
		//get inputs
    	Icon icon = new ImageIcon("mypic.gif");
    	String input;
		String input2;
		String input3;
		String input4;
		String input5;
		String input6;
		String input7;
		String input8;
		input = (String)JOptionPane.showInputDialog(null,"What size?","Start Game",JOptionPane.PLAIN_MESSAGE,icon,null,"Enter Size Here");
		w = Integer.parseInt (input);
		h = w;
	    input2 = (String)JOptionPane.showInputDialog(null,"How many stems?","Start Game",JOptionPane.PLAIN_MESSAGE,icon,null,"Enter Stems Here");
	    stems = Integer.parseInt (input2);
		input3 = (String)JOptionPane.showInputDialog(null,"How many steps?","Start Game",JOptionPane.PLAIN_MESSAGE,icon,null,"Enter Steps Here");
		steps = Integer.parseInt(input3);
		input4 = (String)JOptionPane.showInputDialog(null,"What is the transmission probability [0.0,1.0]?","Start Game",JOptionPane.PLAIN_MESSAGE,icon,null,"Alpha");
		alpha = Double.parseDouble(input4);
		input5 = (String)JOptionPane.showInputDialog(null,"Maximum rotation increment [0.0,1.0]?","Start Game",JOptionPane.PLAIN_MESSAGE,icon,null,"Beta");
		deltaTheta = Double.parseDouble(input5);
		input6 = (String)JOptionPane.showInputDialog(null,"Growth segment increment?","Start Game",JOptionPane.PLAIN_MESSAGE,icon,null,"Growth Segment");
		rDelta = Double.parseDouble(input6);
		
		//get hex colors
		f1 = (String)JOptionPane.showInputDialog(null,"Color 1?","Start Game",JOptionPane.PLAIN_MESSAGE,icon,null,"Ex: 0xFF660000");
		f2 = (String)JOptionPane.showInputDialog(null,"Color 2?","Start Game",JOptionPane.PLAIN_MESSAGE,icon,null,"Ex: 0xFF99FF00");
		
		//change hex string to int
		color1N = (int)Long.parseLong( f1.substring( 2, f1.length() ), 16 );
		color2N = (int)Long.parseLong( f2.substring( 2, f2.length() ), 16 );
		
		//creates bufferedImage and graphics2D
		bufferedImage = new BufferedImage(w, h, bufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) bufferedImage.createGraphics();
		
		//set antialiasing
		RenderingHints hint = new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHints( hint );
		
		//sets background color
		g2d.setColor(Color.BLACK);
		g2d.fill(new Rectangle(0,0,w,h));
		
		//sets starting position
		x = w/2;
		y = h/2;
		beta = r - alpha;

		//sets stroke
		currentStroke = newStroke(0);
		BasicStroke stroke = new BasicStroke( currentStroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
		g2d.setStroke( stroke );

		//sets color
		Color c = new Color(newColor(0));
		g2d.setColor(c);

		//draw line for first step and update position.  Adds initial conditions to array lists to store for later
		g2d.drawLine((int)x,(int)(y), (int) x, (int)(y - r));
		y = y - r;
		double xNew = x;
		double yNew = y;
		xPositions.add(xNew);
		yPositions.add(yNew);
		thetas.add(Math.PI/2);
		rs.add(1.0);
		directions.add(1);
		
		//for every step
		for(int j = 1; j < steps; j++)
		{
			//set new stroke, getting the interpolation
			currentStroke = newStroke(j - 1);
			stroke = new BasicStroke( currentStroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
			g2d.setStroke( stroke );
			
			//set new color, getting the interpolation
			c = new Color(newColor(j - 1));
			g2d.setColor(c);
			
			//for every stem
			for(int i = 0; i < stems; i++)
			{
				direction = 1;

				//if initial positions, use starting values
				if(j == 1)
				{
					x = xPositions.get(0);
					y = yPositions.get(0);
					theta = thetas.get(0);
					r = rs.get(0);
					direction = directions.get(0);
				}

				//otherwise, get the last positions for that stem (i)
				else
				{
					x = xPositions.get(i);
					y = yPositions.get(i);
					theta = thetas.get(i);
					r = rs.get(i);
					direction = directions.get(i);
				}
				
				//determines the weight of the coin toss
				tal();
				//determines next direction
				flip();
				//computes new r and theta
				compute();
				//converts polar to cartesian
				xNew = r*Math.cos(theta) + x;
				yNew = y - r*Math.sin(theta);
				
				//draws new line
				g2d.drawLine((int)x,(int)y, (int)xNew, (int) yNew);
				//if initial steps, adds to array list
				if(j == 1)
				{
					xPositions.add(xNew);
					yPositions.add(yNew);
					thetas.add(theta);
					rs.add(r);
					directions.add(direction);
				}
				//else, updates conditions
				else
				{
					xPositions.set(i, xNew);
					yPositions.set(i, yNew);
					thetas.set(i, theta);
					rs.set(i, r);
					directions.set(i, direction);
				}
			}

		}
		//displays image
	    displayBufferedImage(bufferedImage);
	}
	//-----------------------------
	
	//computes the new r and theta
	public void compute()
	{
		r = ( r + rDelta);
		theta = (theta + ( deltaTheta * Math.random() * direction));
	}
	
	//flips a virtual coin to get the direction
	public void flip()
	{
		double random = Math.random();
		if(random > tal)
		{
			direction = 1;
		}
		else
		{
			direction = -1;
		}
	}
	
	//determines the bias of the coin
	public void tal()
	{
		if(direction == -1)
		{
			tal = alpha;
		}
		else
		{
			tal = beta;
		}
	}
	//gets stroke interpolation by getting the percentage of the stroke the plant is currently at
	public float newStroke(int numSteps)
	{
		float valueS = (float) ((steps - numSteps)*(6 - .5)/steps);
		
		return valueS;
	}

//gets color interpolation by getting the percentage between the colors the plant is currently at
	public int newColor(double numOfSteps)
	{
		//get the mix of color1N and color2N for each channel
		int getAlpha = (black >> 24) & 255;
		double changeR = Math.round((long)(numOfSteps)*((getRed(color2N) - getRed(color1N))/(steps)));
		double changeG = Math.round((long)(numOfSteps)*((getGreen(color2N) - getGreen(color1N))/(steps)));
		double changeB = Math.round((long)(numOfSteps)*((getBlue(color2N) - getBlue(color1N))/(steps)));
		
		int valueR = getRed(color1N);
		int valueG = getGreen(color1N);
		int valueB = getBlue(color1N);

		//combine the bits back together
		int value = (getAlpha << 24)|((valueR + (int)changeR) << 16)|((valueG + (int)changeG) << 8)|(valueB + (int)changeB);
		return value;
	}
	//returns the new color
	
	//shift the bits to get the correct color channel 
	public int getRed(int c)
	{
		return (c >> 16) & 255;
	}
	public int getGreen(int c)
	{
		return (c >> 8) & 255;
	}
	public int getBlue(int c)
	{
		return (c) & 255;
	}
	
	//displays image
	public void displayBufferedImage(BufferedImage image)
	{
		this.setContentPane( new JScrollPane( new JLabel( new ImageIcon(image))));

		//clears arrayLists for next time
		thetas.clear();
		xPositions.clear();
		yPositions.clear();
		rs.clear();
		directions.clear();
		this.validate();
	}
}
