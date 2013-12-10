//Megan Dwyer
//brownian.java
//- Shows Brownian Motion

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.util.Random;

public class brownian
{
	private static final int WIDTH = 401;
	private static final int HEIGHT = 401;
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
	private BufferedImage image = null;
	public int inputAsNumber;
	public static final int w = 800;
	public static final int h = 800;
	private static int x = 100;
	private static int y = 100;
	private static int next = -1;
	private static int intARGB;
	public static BufferedImage bufferedImage;
	public static int TYPE_INT_ARGB;
	public static int black = 0xFF000000;
	public static Color myWhite = new Color(223, 208, 192); // Cream
	public static int rgb = myWhite.getRGB();
	public static Color rectColor = new Color(0,0,0); // Black
	public static int rgb2 = myWhite.getRGB();
	public static int shape = 0;


	//=====================================
	//constructor

	public  ImageFrame( int width, int height)
	{
		//-----------------------------
		//setup the frame's attributes
	
		this.setTitle("Brownian Motion- Megan Dwyer");
		this.setSize( width, height);
		this.getContentPane().setBackground( myWhite );
		
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
		JMenu fileMenu2 = new JMenu("Moore");
		JMenu fileMenu3 = new JMenu("von Neumann");
		// ----Menus
		//Moore
		JMenuItem infinitePlane2 = new JMenuItem( "Infinite Plane" );
		infinitePlane2.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent event )
			{
				shape = 1;
				startGameVM();

			}
		});
		fileMenu2.add( infinitePlane2 );

		//infinitePlane2
		JMenuItem boundedPlane2 = new JMenuItem( "Bounded Plane" );
		boundedPlane2.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent event )
			{
				shape = 2;
				startGameVM();
			}
		});
		fileMenu2.add( boundedPlane2 );
		//boundedPlane2
		JMenuItem toroidalPlane2 = new JMenuItem( "Toroidal Plane" );
		toroidalPlane2.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent event )
			{
				shape = 3;
				startGameVM();
			}
		});
		fileMenu2.add( toroidalPlane2 );
		//toroidalPlane2
		//
		//
		//von Neumann
		JMenuItem infinitePlane3 = new JMenuItem( "von Neumann's Infinite Plane" );
		infinitePlane3.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent event )
			{
				shape = 1;
				startGame();
			}
		});
		fileMenu3.add( infinitePlane3 );

		//infinitePlane3
		JMenuItem boundedPlane3 = new JMenuItem( "von Neumann's Bounded Plane" );
		boundedPlane3.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent event )
			{
				shape = 2;
				startGame();
			}
		});
		fileMenu3.add( boundedPlane3 );
		//boundedPlane3
		JMenuItem toroidalPlane3 = new JMenuItem( "von Neumann's Toroidal Plane" );
		toroidalPlane3.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent event )
			{
				shape = 3;
				startGame();
			}
		});
		fileMenu3.add( toroidalPlane3 );
		//toroidalPlane3


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
		menuBar.add( fileMenu2 );
		menuBar.add( fileMenu3 );
		this.setJMenuBar( menuBar );
	}

	public void startGame()
	{
		x = 100;
		y = 100;
        	Icon icon = new ImageIcon("mypic.gif");
      		String input;
	        input = (String)JOptionPane.showInputDialog(null,"How many steps?","Start Game",JOptionPane.PLAIN_MESSAGE,icon,null,"Enter Steps Here");
	        inputAsNumber = Integer.parseInt (input);
		buff();

	}
	public void startGameVM()
	{
        	Icon icon = new ImageIcon("mypic.gif");
      		String input;
	        input = (String)JOptionPane.showInputDialog(null,"How many steps?","Start Game",JOptionPane.PLAIN_MESSAGE,icon,null,"Enter Steps Here");
	        inputAsNumber = Integer.parseInt (input);
		buffVM();

	}
	//-----------------------------
	//Open a dialogue box and prompt input from user
	public void buff()
	{
		bufferedImage = new BufferedImage(w, h, bufferedImage.TYPE_INT_ARGB);
		Graphics g = bufferedImage.getGraphics();
        	for(int count = 0; count < inputAsNumber; count++)
		{
			if(count == inputAsNumber - 1)
			{
				g.setColor(Color.RED);
				g.drawLine(x, y, random(x), random(y));
				displayBufferedImage(bufferedImage);
				x = random(x);
				y = random(y);
			}
			else
			{
				g.setColor(Color.BLACK);
				if(count == 0)
				{
					g.drawLine(100, 100, random(100), random(100));
					displayBufferedImage(bufferedImage);
					x = random(x);
					y = random(y);
				}
				else
				{		
					g.drawLine(x, y, random(x), random(y));
					displayBufferedImage(bufferedImage);
					x = random(x);
					y = random(y);
				}
				
			}	
		}
	}
	public void buffVM()
	{
		x = 100;
		y = 100;
		bufferedImage = new BufferedImage(w, h, bufferedImage.TYPE_INT_ARGB);
		Graphics g = bufferedImage.getGraphics();
		if (randomVM() == 0)
		{
        		for(int count = 0; count < inputAsNumber; count++)
			{
				if(count == 0)
				{
					g.setColor(Color.BLACK);
					g.drawLine(100, 100, x, random(100));
				}	
				else if(count == inputAsNumber - 1)
				{
					g.setColor(Color.RED);
					g.drawLine(x, y, x, random(y));
				}
				else
				{
					g.setColor(Color.BLACK);
					g.drawLine(x, y, x, random(y));
				}

				displayBufferedImage(bufferedImage);
				x = random(x);
				y = random(y);
			}	
		}
		else
		{
        		for(int count = 0; count < inputAsNumber; count++)
			{
				if(count == 0)
				{
					g.setColor(Color.BLACK);
					g.drawLine(100, 100, random(100), y);
				}	
				else if(count == inputAsNumber - 1)
				{
					g.setColor(Color.RED);
					g.drawLine(x, y, random(x), y);
				}
				else
				{
					g.setColor(Color.BLACK);
					g.drawLine(x, y, random(x), y);	
				}			
				displayBufferedImage(bufferedImage);
				x = random(x);
				y = random(y);
			}
		}
	}
	public static int random(int oldPosition)
	{
		Random rand = new Random();
		next = rand.nextInt(3);
		int newPosition = oldPosition;
		if (next == 0)
		{
			newPosition = oldPosition + 1;
			if(shape == 3)
			{
				if(newPosition == 401)
				{
					if(oldPosition == x)
					{			
						x = 1;
					}
					else
					{
						y =1;
					}
				}
			
				else if(newPosition == 0)
				{
					if(oldPosition == x)
					{			
						x = 401;
					}
					else
					{
						y = 401;
					}
				}
			}
			else if(shape == 2)
			{
				if(newPosition == 401)
				{
					if(oldPosition == x)
					{			
						x = 400;
					}
					else
					{
						y =400;
					}
				}
			
				else if(newPosition == 0)
				{
					if(oldPosition == x)
					{			
						x = 1;
					}
					else
					{
						y = 1;
					}
				}
			}

		}
		else if (next == 1)
		{
			newPosition = oldPosition;
			if(shape == 3)
			{
				if(newPosition == 401)
				{
					if(oldPosition == x)
					{			
						x = 1;
					}
					else
					{
						y =1;
					}
				}
			
				else if(newPosition == 0)
				{
					if(oldPosition == x)
					{			
						x = 401;
					}
					else
					{
						y = 401;
					}
				}
			}
			else if(shape == 2)
			{
				if(newPosition == 401)
				{
					if(oldPosition == x)
					{			
						x = 400;
					}
					else
					{
						y =400;
					}
				}
			
				else if(newPosition == 0)
				{
					if(oldPosition == x)
					{			
						x = 1;
					}
					else
					{
						y = 1;
					}
				}
			}
		}
		else
		{
			newPosition = oldPosition - 1;
			if(shape == 3)
			{
				if(newPosition == 401)
				{
					if(oldPosition == x)
					{			
						x = 1;
					}
					else
					{
						y =1;
					}
				}
			
				else if(newPosition == 0)
				{
					if(oldPosition == x)
					{			
						x = 401;
					}
					else
					{
						y = 401;
					}
				}
			}
			else if(shape == 2)
			{
				if(newPosition == 401)
				{
					if(oldPosition == x)
					{			
						x = 400;
					}
					else
					{
						y =400;
					}
				}
			
				else if(newPosition == 0)
				{
					if(oldPosition == x)
					{			
						x = 1;
					}
					else
					{
						y = 1;
					}
				}
			}
		}

		System.out.println(x + " ,  " + y + "  ,  " + newPosition);
		return newPosition;
	}
	public static int randomVM()
	{
		Random rand2 = new Random();
		next = rand2.nextInt(2);
		int answer = 1;
		if (next == 0)
		{
			answer = 0;
		}
		else
		{
			answer = 1;
		}

		return answer;
	}
		
	public void displayBufferedImage(BufferedImage image)
	{
		System.out.println("works");
		this.setContentPane( new JScrollPane( new JLabel( new ImageIcon(image))));
		this.validate();
	}
}
	 	
