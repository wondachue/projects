//Megan Dwyer
//- Diffusion limted aggregation

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.util.*;

public class diffusion
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
	private static int seeds;
	private static int seedsInitial;
	private int particles;
	private int steps;
	private static int white = 0xffffffff;
	private static int black = 0xff000000;
	private static int red = 0xFFFF0000;
	public static BufferedImage bufferedImage;
	private static int TYPE_INT_ARGB;
	ArrayList<Integer> particlePositionsX = new ArrayList<Integer>();;
	ArrayList<Integer> particlePositionsY = new ArrayList<Integer>();;
	private int w = 100;
	private int h = 100;
	private static int particleNum;
	ArrayList<Integer> allCrystalsX = new ArrayList<Integer>();
	ArrayList<Integer> allCrystalsY = new ArrayList<Integer>();
	private static boolean checkCrystal[][];

	//=====================================
	//constructor

	public  ImageFrame( int width, int height)
	{
		//-----------------------------
		//setup the frame's attributes
	
		this.setTitle("diffusion limited aggregation - Megan Dwyer");
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
		JMenu fileMenu2 = new JMenu("Crystals");

		JMenuItem makeCrystalT = new JMenuItem("Toroid");
		makeCrystalT.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent event )
			{
				startCrystal(0);
			}
		});
		fileMenu2.add( makeCrystalT );
		
		JMenuItem makeCrystalB = new JMenuItem("Bounded Plane");
		makeCrystalB.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent event )
			{
				startCrystal(1);
			}
		});
		fileMenu2.add( makeCrystalB );

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
		menuBar.add(fileMenu2);

		this.setJMenuBar( menuBar );
	}
	
	
	//Open a dialogue box and prompt input from user
	public void startCrystal(int typeOfPlane)
	{
		//get inputs
      	Icon icon = new ImageIcon("mypic.gif");
    	String input;
		String input2;
		String input3;
		String input4;
		input = (String)JOptionPane.showInputDialog(null,"What size?","Start Game",JOptionPane.PLAIN_MESSAGE,icon,null,"Enter Size Here");
		w = Integer.parseInt (input);
		h = w;
	    input2 = (String)JOptionPane.showInputDialog(null,"How many seeds?","Start Game",JOptionPane.PLAIN_MESSAGE,icon,null,"Enter Seeds Here");
	    seeds = Integer.parseInt (input2);
	    seedsInitial = seeds;
		input3 = (String)JOptionPane.showInputDialog(null,"How many particles?","Start Game",JOptionPane.PLAIN_MESSAGE,icon,null,"Enter Particles Here");
	    particles = Integer.parseInt (input3);
		input4 = (String)JOptionPane.showInputDialog(null,"How many steps?","Start Game",JOptionPane.PLAIN_MESSAGE,icon,null,"Enter Steps Here");
		steps = Integer.parseInt(input4);
	//this 2D array will be used later to check if the matching coordinates are were a crystal is
	    checkCrystal = new boolean [w][h];
		
	    bufferedImage = new BufferedImage(w, h, bufferedImage.TYPE_INT_ARGB);
	    
	    //add seed positions to crystal arraylist
	    for(int i = 0; i < seedsInitial; i++)
	    {
	    	int rX = (int)(Math.random()*(w - 1));
	    	int rY = (int)(Math.random()*(h - 1));
	       	allCrystalsX.add(rX);
	        allCrystalsY.add(rY);
	        bufferedImage.setRGB(rX, rY, red);
	        
	        checkCrystal[i][i] =  true;
	    }
	    
	    //add initial particle positions to particle position arrays
	    for(int i = 0; i < particles; i++)
	    {
	    	particlePositionsX.add((int)(Math.random()*(w - 1)));
	    	particlePositionsY.add((int)(Math.random()*(h - 1)));
	    }
	    
	    //check what type of plane and start the bufferedImage with that type
		if (typeOfPlane == 0)
		{
			buff(0);
		}
		else
		{
			buff(1);
		}
	}
	//-----------------------------
	
	
	//creates the image
	public void buff(int typeOfPlane)
	{
		for(int count = 0; count < steps; count++)
		{
			//check if a particle is touching a crystal, and if so, set the position to be black
			int ch;
			//get new coordinates for particle
				for(int countParts = 0; countParts < particles; countParts++)
				{
//sets the new direction of the particle and also checks to see if //in bounds
					int newDirection = (int)(Math.random()*(8));
					move(newDirection, countParts, typeOfPlane);
			
					//checks to see if touching a seed or attached particle
					ch = checkColor(countParts);
					if(ch == 1)
					{
						//if the particle is touching, set to black			bufferedImage.setRGB(particlePositionsX.get(countParts),particlePositionsY.get(countParts),black);
					}
				}
		}
		//creates the image by seeing if red or black, and setting the pixel to white otherwise
		for(int i = 0; i < w; i++)
		{
			for(int j = 0; j < h; j++)
			{
				if(bufferedImage.getRGB(i,j) != red)
				{
					if(bufferedImage.getRGB(i,j) != black)
					{
						bufferedImage.setRGB(i,j,white);
					}
				}
			}
		}
		//display image
		displayBufferedImage(bufferedImage);

	}
	
	//checks to see if a particle is touching a crystal
	public int checkColor(int whichParticle)
	{
		int c = 0;
		

		//compares every particle position to the position of every crystal
		//if in the particle is in the area of the crystal, changes to black and adds the point to the crystal
		for(int i = 0; i < seedsInitial; i++)
		{
			int thisCrystalX = allCrystalsX.get(i);
			int thisCrystalY = allCrystalsY.get(i);
			int thisParticleX = particlePositionsX.get(whichParticle);
			int thisParticleY = particlePositionsY.get(whichParticle);

			//sees if the particle is touching
			if(thisParticleX == thisCrystalX || thisParticleX == thisCrystalX + 1 || thisParticleX == thisCrystalX - 1)
			{
				if(thisParticleY == allCrystalsY.get(i) || thisParticleY == allCrystalsY.get(i) +  1 || thisParticleY == thisCrystalY - 1)
				{
					if(checkCrystal[thisParticleX][thisParticleY] != true)
					{
						//if touching, adds the particle to the crystal
c = 1;
						seeds++;
						allCrystalsX.add(thisParticleX);
						allCrystalsY.add(thisParticleY);
						checkCrystal[thisParticleX][thisParticleY] =  true;
					}
					
				}
			}
		}
		seedsInitial = seeds;
		return c;
	}
	
	//updates direction, going one of 8 directions at random
	public void move(int direction, int particleIndex, int type)
	{

		int particleTempx = particlePositionsX.get(particleIndex);
		int particleTempy = particlePositionsY.get(particleIndex);
		if(direction == 0)
		{
			particlePositionsX.set(particleIndex, (particleTempx + 1));
			particlePositionsY.set(particleIndex, particleTempy);
		}
		else if(direction == 1)
		{
			particlePositionsX.set(particleIndex, (particleTempx - 1));
			particlePositionsY.set(particleIndex, particleTempy);
		}
		else if(direction == 2)
		{
			particlePositionsX.set(particleIndex, (particleTempx + 1));
			particlePositionsY.set(particleIndex, (particleTempy + 1));
		}
		else if(direction == 3)
		{
			particlePositionsX.set(particleIndex, (particleTempx - 1));
			particlePositionsY.set(particleIndex, (particleTempy - 1));
		}
		else if(direction == 4)
		{
			particlePositionsX.set(particleIndex, (particleTempx));
			particlePositionsY.set(particleIndex, (particleTempy + 1));
		}
		else if(direction == 5)
		{
			particlePositionsX.set(particleIndex, (particleTempx));
			particlePositionsY.set(particleIndex, (particleTempy - 1));
		}
		else if(direction == 6)
		{
			particlePositionsX.set(particleIndex, (particleTempx + 1));
			particlePositionsY.set(particleIndex, (particleTempy - 1));
		}
		else
		{
			particlePositionsX.set(particleIndex, (particleTempx - 1));
			particlePositionsY.set(particleIndex, (particleTempy + 1));
		}	
		
		
		//if plane type toroid and out of bounds, update position
		if(type == 0)
		{
			if(particlePositionsX.get(particleIndex) >= w )
			{
				particlePositionsX.set(particleIndex, Integer.valueOf(0));
			}
			if(particlePositionsY.get(particleIndex) >= h)
			{
				particlePositionsY.set(particleIndex, Integer.valueOf(0));
			}
			if(particlePositionsX.get(particleIndex) < 0 )
			{
				particlePositionsX.set(particleIndex, Integer.valueOf(w - 1));
			}
			if(particlePositionsY.get(particleIndex) < 0)
			{
				particlePositionsY.set(particleIndex, Integer.valueOf(h - 1));
			}
		}
		//if plane type bounded and out of bounds, decrease position
		if(type == 1)
		{
			if(particlePositionsX.get(particleIndex) >= w )
			{
				particlePositionsX.set(particleIndex, Integer.valueOf(particlePositionsX.get(particleIndex) - 1));
			}
			if(particlePositionsY.get(particleIndex) >= h)
			{
				particlePositionsY.set(particleIndex, Integer.valueOf(particlePositionsY.get(particleIndex) - 1));
			}
			if(particlePositionsX.get(particleIndex) < 0 )
			{
				particlePositionsX.set(particleIndex, Integer.valueOf(particlePositionsX.get(particleIndex) + 1));
			}
			if(particlePositionsY.get(particleIndex) < 0)
			{
				particlePositionsY.set(particleIndex, Integer.valueOf(particlePositionsY.get(particleIndex) + 1));
			}
		}
	}
	
	//displays image
	public void displayBufferedImage(BufferedImage image)
	{
		this.setContentPane( new JScrollPane( new JLabel( new ImageIcon(image))));
		this.validate();
		allCrystalsX.clear();
		allCrystalsY.clear();
		particlePositionsX.clear();
		particlePositionsY.clear();
	}
}
