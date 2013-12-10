//Fractal Generator - Megan Dwyer

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;

public class fractals
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
	private static File description = null;
	private static ArrayList<String> results = new ArrayList<String>();
	private static ArrayList<Double> prob = new ArrayList<Double>();
	private static ArrayList<Double> dets = new ArrayList<Double>();
	private static ArrayList<AffineTransform> matrix = new ArrayList<AffineTransform>();
	private static int countLines = 0;
	private static int w = 0;
	private static int h = 0;
	private static int color1N;
	private static int color2N;
	private static String f1 = "0xFF660000";
	private static String f2 = "0xFF99FF00";
	private static int gen = 0;
	private static Graphics2D g2d = null;
	private static double x = 0;
	private static double y = 0;
	private static Point2D.Double pt1;
	private static Point2D.Double pt2;
	private static double sumDet = 0;
	private static boolean checkIfPIsGiven = false;
	private static BufferedImage imageNew = null;
	

	
	//=============================================================================================================
	//constructor
	// ============================================================================================================
	public  ImageFrame( int width, int height)
	{
		//-----------------------------
		//setup the frame's attributes
	
		this.setTitle("Fractal Generator - MEGAN DWYER");
		this.setSize( width, height);
		
		//add a menu to the frame
		addMenu();
	
		//-----------------------------
		//setup the file chooser dialogue

		chooser = new JFileChooser();
		chooser.setCurrentDirectory( new File( "." ) );
	}
	// ============================================================================================================
	
	
	//=============================================================================================================
	// === file menu
	// ============================================================================================================
	private void addMenu()
	{
		//setup the frame's menu bar

		JMenu fileMenu = new JMenu("File");
		
		// ----Load IFS description
		JMenuItem loadDescription = new JMenuItem( "Load IFS description" );
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
		JMenuItem display = new JMenuItem( "Display IFS" );
		display.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent event )
			{
				display();
			}
		});
		fileMenu.add( display );
				
		// ----Save
		JMenuItem saveItem = new JMenuItem( "Save" );
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
	// ============================================================================================================
	
	
	//=============================================================================================================
	//Load IFS Description
	// ============================================================================================================
	//get description
	//loads in lines from the file
	public void loadDescription()
	{
		if ( chooser.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION )
		{
			description = chooser.getSelectedFile();		
		}
		String line = "";
	
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(description));
			
		//reads in lines
		    while( ( line = reader.readLine() ) != null)
		    {
		    	countLines++;
		        results.add(line);
		    }
		    getMatrix();
		    reader.close();
		}
		//throws exception if null
		catch ( IOException e )
		{
		   JOptionPane.showMessageDialog( ImageFrame.this,
			   		      "Error loading description file",
						  "File is null",
						  JOptionPane.ERROR_MESSAGE );
		}
	}
	//gets elements of matrix
	public void getMatrix()
	{
		for(int i = 0; i < countLines; i++)
		{
			Scanner sc = new Scanner(results.get(i));
			double m00 = sc.nextDouble();
			double m01 = sc.nextDouble();
			double m10 = sc.nextDouble();
			double m11 = sc.nextDouble();
			double m02 = sc.nextDouble();
			double m12 = sc.nextDouble();
			
			//creates affine transform
			AffineTransform newAff = new AffineTransform(m00,m10,m01,m11,m02,m12); 
			matrix.add(newAff);
			
			//checks if probability is given
			if(sc.hasNextDouble() == true)
			{
				prob.add(sc.nextDouble());
				checkIfPIsGiven = true;
			}

		}

	}


	// ============================================================================================================
	
	
	
	
	//=============================================================================================================
	//Configure image
	// ============================================================================================================
	public void configure()
	{
		//takes in input on dimensions and colors
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
		Color c2 = new Color(color2N);
		g2d.setColor(c2);
	}
	
	// ============================================================================================================
	

	
	
	
	//=============================================================================================================
	//Display IFS
	// ============================================================================================================
	public void display()
	{
		String input = (String)JOptionPane.showInputDialog(null,"Number of generations?","Start Game",JOptionPane.PLAIN_MESSAGE,null,null,"Enter Number Here");
		gen = Integer.parseInt (input);
		
		//gets initial point values
		getPosition();
		
		//checks if probabilities must be calculated using the determinate
		if(checkIfPIsGiven == false)
		{
			getDeterminant();
			for(int i = 0; i < countLines; i++)
			{
				prob.add(dets.get(i)/sumDet);
			}
		}
		
		//for the number of generations, transform the image
		for(int i = 0; i < gen; i++)
		{

			transforms();
							
		}

		//flip the image so that the y is in the correct direction
		
		imageNew = new BufferedImage(w, h, imageNew.TYPE_INT_ARGB);
		for(int i = 0; i < w; i++)
		{
			for(int j = 0; j < h; j++)
			{
				imageNew.setRGB(  i, j ,   image.getRGB(  i , h - j - 1));
			}
		}
		displayBufferedImage(imageNew);
		
	}
//gets initial point positions
	public void getPosition()
	{
		x = .5;
		y = .5;
		pt1 = new Point2D.Double(x,y);
		pt2 = new Point2D.Double(x,y);
	}
	//calculate probabilities based on the determinates
	public void getDeterminant()
	{
		for(int i = 0; i < countLines; i++)
		{
			if((matrix.get(i).getDeterminant()) > 0)
			{
				dets.add(matrix.get(i).getDeterminant());
				sumDet += matrix.get(i).getDeterminant();
			}
			else
			{
				//fixes values that are 0
				dets.add(0.01);
				sumDet += .01;
			}
		}
	}
	//transforms image using affine transforms
	public void transforms()
	{
		double rand = Math.random();
		double prev = 0;
		for(int i = 0; i < countLines; i++)
		{
			double store = prob.get(i);		
			//checks if random value is in range of probability
			if(rand <= store + prev)
			{
				AffineTransform tea = matrix.get(i);
				
				tea.transform(pt1, pt2);
				
				if((pt2.getX()) <= 1 && pt2.getY() <= 1 && (pt2.getY()) >= 0 && (pt2.getX()) >= 0 )
				{
				//set pixel if in bounds			image.setRGB((int)(pt2.getX()*w),(int)(pt2.getY()*h),color2N);
				}
				pt1 = pt2;
				break;
				
			}
			prev += store;

		}
		
	}
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
	// ============================================================================================================
	
	
	
	
	
	//=============================================================================================================
	//saves
	// ============================================================================================================
	public void saveFile()
	{
		String inputName = (String)JOptionPane.showInputDialog(null,"Name file","Start Game",JOptionPane.PLAIN_MESSAGE,null,null,"Enter Name Here");
		inputName += ".png";
		
		File outputFile = new File(inputName);
		try
		{
		   javax.imageio.ImageIO.write( imageNew, "png", outputFile );
		}
		catch ( IOException e )
		{
		   JOptionPane.showMessageDialog( ImageFrame.this,
			   		      "Error saving file",
						  "oops!",
						  JOptionPane.ERROR_MESSAGE );
		}
	}
	// ============================================================================================================
}


