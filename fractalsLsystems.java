//L-Systems Fractals - Megan Dwyer

import java.util.Stack;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Scanner;

import javax.swing.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.nio.charset.Charset;

import javax.imageio.*;
import javax.swing.*;

public class fractalsLsystems
{
	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	
	//EDT calls worker thread
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			public void run()
			{
				createAndShowGUI();
			}
		});
	}
	private static void createAndShowGUI()
	{
		JFrame frame = new ImageFrame(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                 
		frame.setVisible( true );
	}
}
//############################################

class ImageFrame extends JFrame
{
	private final JFileChooser chooser;
	private BufferedImage image = null;
	File description = null;
	int w = 0;
	int h = 0;
	private static int color1N;
	private static int color2N;
	private static String f1 = "0xFF660000";
	private static String f2 = "0xFF99FF00";
	private static int gen = 0;
	private static Graphics2D g2d = null;
	private static double x = 0;
	private static double y = 0;
	private static double theta = 0;
	private static double segmentLength = 0;
	private static String intial = "";
	private static String generator = "";
	private static double base = 0;
	private static double bearing = 0;
	private static Deque<Double> stackX = new ArrayDeque<Double>();
	private static Deque<Double> stackY = new ArrayDeque<Double>();
	private static Deque<Double> stackBear = new ArrayDeque<Double>();
	
	//=====================================
	//constructor

	public  ImageFrame( int width, int height)
	{
		//-----------------------------
		//setup the frame's attributes
	
		this.setTitle("LSystem Fractals - MEGAN DWYER");
		this.setSize( width, height);
		
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
		
		// ----Load IFS description
		JMenuItem loadDescription = new JMenuItem( "Load L-System" );
		loadDescription.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent event )
			{
				loadDescription();
			}
		});
		fileMenu.add( loadDescription );
				
		// ----Configure image
		JMenuItem configure = new JMenuItem( "Configure image" );
		configure.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent event )
			{
				configure();
			}
		});
		fileMenu.add( configure );
				
		// ----Display IFS
		JMenuItem display = new JMenuItem( "Display L-System" );
		display.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent event )
			{
				display();
			}
		});
		fileMenu.add( display );
				
		// ----Save
		JMenuItem saveItem = new JMenuItem( "Save image" );
		saveItem.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent event )
			{
				saveFile();
			}
		});
		fileMenu.add( saveItem );
		
		// --- Exit
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
	// ===========================================
	//displays image
	public void displayBufferedImage(BufferedImage image)
	{
		ImageIcon icon = new ImageIcon();
		JLabel label = new JLabel(icon);
		icon.setImage( image );
		label.repaint();
		this.setContentPane(label);
		validate();
	}
	
	public void getDescription()
	{
		
		if ( chooser.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION )
		{
			description = chooser.getSelectedFile();		
		}
		
	}
	public void display()
	{
		String input = (String)JOptionPane.showInputDialog(null,"Number of generations?","Start Game",JOptionPane.PLAIN_MESSAGE,null,null,"Enter Number Here");
		gen = Integer.parseInt (input);
		String input2 = (String)JOptionPane.showInputDialog(null,"What x [-1.0,1.0]?","Start Game",JOptionPane.PLAIN_MESSAGE,null,null,"Enter x Here");
		x = Double.parseDouble (input2);
		String input3 = (String)JOptionPane.showInputDialog(null,"What y [-1.0,1.0]?","Start Game",JOptionPane.PLAIN_MESSAGE,null,null,"Enter y Here");
		y = Double.parseDouble (input3);
		String input4 = (String)JOptionPane.showInputDialog(null,"What base length?","Start Game",JOptionPane.PLAIN_MESSAGE,null,null,"Enter base Here");
		base = Double.parseDouble (input4);
		String input5 = (String)JOptionPane.showInputDialog(null,"What bearing?","Start Game",JOptionPane.PLAIN_MESSAGE,null,null,"Enter bearing Here");
		bearing = Double.parseDouble (input5);	
		
		turtlePower();
		
	}
	public void turtlePower()
	{
		System.out.println("x Before: " + x);
		System.out.println("y Before: " + y);

		
		for(int i = 0; i < gen; i++)
		{

			path();
						
		}
		System.out.println(intial);
		
		//x and y initial positions (when drawing a line later)
		final double xP =  (x +1 )* w*.5;
		final double yP =  (y +1 )* h*.5;
		
		//the x and y to be updated for the endpoints of the line (to be drawn later)
		final double testx = (x +1 )* w*.5;
		final double testy = (y +1 )* h*.5;
		
		final double testbearing = bearing;
		
		double seeAnswer = (Math.pow((segmentLength),gen));
		final double testbase = (base/(Math.pow((segmentLength),gen)))* h *.5;
		System.out.println(testbase);
		double seeTestBaseAfter = testbase;
		
		new Thread(new Runnable()
		{
			public void run()
			{
					final BufferedImage newFrame = createImage(xP, yP, testx, testy, testbase, testbearing);
					SwingUtilities.invokeLater(new Runnable()
					{
						public void run()
						{
							displayBufferedImage(newFrame);
						}
					});
			}
		}).start();
		
	}
	public BufferedImage createImage(double xP, double yP, double testx, double testy, double testbase, double testbearing)
	{
		
		for(int i = 0; i < intial.length(); i++)
		{
			
			if(bearing > (2 * Math.PI))
			{
				testbearing = bearing - 2*Math.PI*Math.floor(bearing / (2 * Math.PI));
				System.out.println(3.5%3.14);
			}
			if(intial.charAt(i) == 'F' || intial.charAt(i) == 'R' || intial.charAt(i) == 'L')
			{
				testx = (testx + (testbase*Math.cos(testbearing)));
				testy = (testy + (testbase*Math.sin(testbearing)));

				g2d.drawLine((int)(xP), (int)(yP), (int)(testx), (int)(testy));
			}
			else if(intial.charAt(i) == 'f' || intial.charAt(i) == 'r' || intial.charAt(i) == 'l')
			{
				testx = (testx + (testbase*Math.cos(testbearing)));
				testy = (testy + (testbase*Math.sin(testbearing)));
			}
			else if(intial.charAt(i) == '+')
			{
				testbearing += theta;
			}
			else if(intial.charAt(i) == '-')
			{
				testbearing -= theta;
			}
			else if(intial.charAt(i) == '[')
			{
				stackX.push(xP);
				stackY.push(yP);
				stackBear.push(testbearing);
			}
			if(intial.charAt(i) == ']')
			{
				testx = stackX.pop();
				testy = stackY.pop();
				testbearing = stackBear.pop();
				
			}

				xP =  testx;
				yP =  testy;
		
			displayBufferedImage(image);
		}
		g2d.dispose();
		return image;
	}
	public void path()
	{
		String newString = "";
		for(int i = 0; i < intial.length(); i++)
		{
			if(intial.charAt(i) == 'F' || intial.charAt(i) == 'R' || intial.charAt(i) == 'L')
			{
				newString += generator;
			}
			else
			{
				newString += intial.charAt(i);
			}

		}
		intial = newString;

	}
	public void configure()
	{
		String input = (String)JOptionPane.showInputDialog(null,"What height?","Start Game",JOptionPane.PLAIN_MESSAGE,null,null,"Enter Size Here");
		h = Integer.parseInt (input);
		String input2 = (String)JOptionPane.showInputDialog(null,"What width?","Start Game",JOptionPane.PLAIN_MESSAGE,null,null,"Enter Size Here");
		w = Integer.parseInt (input2);
		
		//get hex colors
		f1 = (String)JOptionPane.showInputDialog(null,"Color 1?","Start Game",JOptionPane.PLAIN_MESSAGE,null,null,"Ex: 660000");
		f2 = (String)JOptionPane.showInputDialog(null,"Color 2?","Start Game",JOptionPane.PLAIN_MESSAGE,null,null,"Ex: 99FF00");
		
		
		//change hex string to int
		color1N = (int)Long.parseLong( f1.substring( 0, f1.length() ), 16 );
		color2N = (int)Long.parseLong( f2.substring( 0, f2.length() ), 16 );
		color1N = color1N | 0xFF000000;
		color2N = color2N | 0xFF000000;
		
		image = new BufferedImage(w, h, image.TYPE_INT_ARGB);
		g2d = (Graphics2D) image.createGraphics();
		
		//sets background color
		Color c = new Color(color1N);
		g2d.setColor(c);
		g2d.fill(new Rectangle(0,0,w,h));
		
		//sets pen color
		Color c2 = new Color(color2N);
		g2d.setColor(c2);
	}
	public void loadDescription()
	{
		getDescription();
	
		try
		{
		   Scanner s = new Scanner(description);
		   theta = s.nextDouble();
		   segmentLength = s.nextDouble();
		   intial = s.next();
		   generator = s.next();
		
					
		
		}
		catch ( IOException e )
		{
		   JOptionPane.showMessageDialog( ImageFrame.this,
			   		      "Error loading description file",
						  "File is null",
						  JOptionPane.ERROR_MESSAGE );
		}
	}
	public void saveFile()
	{
		String inputName = (String)JOptionPane.showInputDialog(null,"Name file","Start Game",JOptionPane.PLAIN_MESSAGE,null,null,"Enter Name Here");
		inputName += ".png";
		
		File outputFile = new File(inputName);
		try
		{
		   javax.imageio.ImageIO.write( image, "png", outputFile );
		}
		catch ( IOException e )
		{
		   JOptionPane.showMessageDialog( ImageFrame.this,
			   		      "Error saving file",
						  "oops!",
						  JOptionPane.ERROR_MESSAGE );
		}
	}
}
	 	

