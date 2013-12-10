
//Megan Dwyer
//bilinear.java
//- Bilinear interpolation

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.util.*;

public class bilinear
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
	public static int heightInput;
	private static int red = 0xFFFF0000;
	private static int blue = 0xFF0000FF;
	private static int white = 0xFFFFFFFF;
	private static int green = 0xff00ff00;
	private static final int black = 0xff000000;
	private static final int greenF = 0xff00ff00;
	private static final int redF = 0xFFFF0000;
	private static final int blueF = 0xFF0000FF;
	private static final int whiteF = 0xFFFFFFFF;
	public BufferedImage bufferedImage;
	public static int TYPE_INT_ARGB;


	//=====================================
	//constructor

	public  ImageFrame( int width, int height)
	{
		//-----------------------------
		//setup the frame's attributes
	
		this.setTitle("Bilinear Interpolation - Megan Dwyer");
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

		JMenuItem makeGradient = new JMenuItem("Make Gradient");
		makeGradient.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent event )
			{
				startGrad();
			}
		});
		fileMenu.add( makeGradient );

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

	public void startGrad()
	{
      	Icon icon = new ImageIcon("mypic.gif");
    		String input;
	        input = (String)JOptionPane.showInputDialog(null,"Desired image height?","Start Game",JOptionPane.PLAIN_MESSAGE,icon,null,"Enter Height Here");
	        heightInput = Integer.parseInt (input);
		buff();

	}
	//-----------------------------
	//Open a dialogue box and prompt input from user, then call for the bufferedImage in method buff()
	public void buff()
	{
		bufferedImage = new BufferedImage(heightInput, heightInput, bufferedImage.TYPE_INT_ARGB);
		for(int y = 0; y < heightInput; y++)
		{      			
			for(int x = 0; x < heightInput; x++)
			{	
				green = delta(green, blue, greenF, blueF, y);
				white = delta(white, red, whiteF, redF, y);
				bufferedImage.setRGB(x,y,delta(green, white, green, white, x));

				
			}
				
		}
		//for every pixel, change the color using delta so that the color is a mix between the ones given
		//as the parameters. Each time, green and white are set to be a little more blue and red to imitate
		//downward interpolation
		displayBufferedImage(bufferedImage);
		red = 0xFFFF0000;
		blue = 0xFF0000;
		white = 0xFFFFFFFF;
		green = 0xff00ff00;
		//after displaying the bufferedImage, make sure that the original values for the colors are set back

	}
	public int delta(int color1, int color2, int color1F, int color2F, double numOfSteps)
	{
		//get the mix of color1 and color2
		int getAlpha = (black >> 24) & 255;
		double changeR = Math.round((long)((getRed(color2F) - getRed(color1F))*(numOfSteps/heightInput)));
		double changeG = Math.round((long)((getGreen(color2F) - getGreen(color1F))*(numOfSteps/heightInput)));
		double changeB = Math.round((long)((getBlue(color2F) - getBlue(color1F))*(numOfSteps/heightInput)));
		//a constant slope, one for each color channel
		int valueR = getRed(color1F);
		int valueG = getGreen(color1F);
		int valueB = getBlue(color1F);
		//break up the initial color into RGB
		int value = (getAlpha << 24)|((valueR + (int)changeR) << 16)|((valueG + (int)changeG) << 8)|(valueB + (int)changeB);
		//combine the bits back together

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
	public void displayBufferedImage(BufferedImage image)
	{
		this.setContentPane( new JScrollPane( new JLabel( new ImageIcon(image))));
		this.validate();
	}
}