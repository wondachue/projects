// Megan Dwyer
// plant simulation

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;

import javax.swing.*;


public class plant
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
	
	//=====================================
	//constructor

	public  ImageFrame( int width, int height)
	{
		//-----------------------------
		//setup the frame's attributes
	
		this.setTitle("Plant Simulation - Megan Dwyer");
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
		
		//initialize variables
		
		theta = (int)((Math.PI)/2);
		r = 1;
		
		//get inputs
    	Icon icon = new ImageIcon("mypic.gif");
    	String input;
		String input2;
		String input3;
		String input4;
		String input5;
		String input6;
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
		
		//creates bufferedImage and graphics2D
		bufferedImage = new BufferedImage(w, h, bufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) bufferedImage.createGraphics();
		
		//sets background color
		g2d.setColor(Color.WHITE);
		g2d.fill(new Rectangle(0,0,w,h));
		
		//sets the "brush" color
		g2d.setColor(Color.BLACK);
		
		//sets starting position
		x = w/2;
		y = h/2;
		
		//sets beta and draws line, updating y to the new position
		beta = r - alpha;
		g2d.drawLine((int)x,(int)(y), (int) x, (int)(y - r));
		y = y - r;
		
		//for every stem
		for(int i = 0; i < stems; i++)
		{
			direction = 1;
			
			//for every step
			for(int j = 1; j < steps; j++)
			{
				//determines the weight of the coin toss
				tal();
				//determines next direction
				flip();
				//computes new r and theta
				compute();
				//converts polar to cartesian
				double xNew = r*Math.cos(theta) + x;
				double yNew = y - r*Math.sin(theta);
				
				//draws new line
				g2d.drawLine((int)x,(int)y, (int)xNew, (int) yNew);
				x = xNew;
				y = yNew;
	    	}
			
			//reset starting position, theta and r
			x = w/2;
			y = h/2;
			theta = Math.PI/2;
			r = 1;
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

	//displays image
	public void displayBufferedImage(BufferedImage image)
	{
		this.setContentPane( new JScrollPane( new JLabel( new ImageIcon(image))));
		this.validate();
	}
}