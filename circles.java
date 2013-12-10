//Megan Dwyer
//- Circular Graphics
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
//create an image made up of circles
public class circles
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
public static int height;
public static int width;
public static BufferedImage bufferedImage;
public static BufferedImage bufferedImageNew;
public static Graphics2D g2d;
public static int TYPE_INT_ARGB;
private static int colorsRed[][];
private static int colorsGreen[][];
private static int colorsBlue[][];
private static Ellipse2D.Double circle;
private static int averageR = 0;
private static int averageG = 0;
private static int averageB = 0;
private static int averageXR = 0;
private static int averageXG = 0;
private static int averageXB = 0;
private static int widthNew = 0;
private static int heightNew = 0;
//=====================================
//constructor
public ImageFrame( int width, int height)
{
//-----------------------------
//setup the frame's attributes
this.setTitle("Circular Graphics - Megan Dwyer");
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
JMenuItem open = new JMenuItem("Open");
open.addActionListener( new ActionListener()
{
public void actionPerformed( ActionEvent event )
{
open();
}

});
fileMenu.add( open );
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
JMenuBar menuBar = new JMenuBar();
menuBar.add( fileMenu );
this.setJMenuBar( menuBar );
}
//-----------------------------
//draw circles by getting how many circles there are in a row, then cycle draw the circles for the row per column
public void circles()
{
for(int countDivision = 1; widthNew > 5; countDivision++)
{
widthNew = widthNew/countDivision;
for(int i = 0; i <= width - widthNew; i += widthNew)
{
for(int j = 0; j <= width - widthNew; j+= widthNew)
{
averageOfArea(i, (i + widthNew), j, (j + widthNew));
Color newColor = new Color(averageR, averageG, averageB);
g2d.setColor(newColor);
circle = new Ellipse2D.Double(i, j, widthNew, widthNew);
g2d.fill(circle);
averageR = 0;
averageB = 0;
averageG = 0;
averageXR = 0;
averageXG = 0;
averageXB = 0;
}
}
}
}
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
//open() - choose a file, load and display image
private void open()
{
File file = getFile();
if( file != null )
{
displayFile( file );
}
}
//-----------------------------
//Open a file selected by the user
private File getFile()
{
File file = null;
if ( chooser.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION )
{
file = chooser.getSelectedFile();
}

return file;
}
//-------------------------------------
//Display specified file in the frame
//gets the image and transformers it into circles
private void displayFile( File file )
{
try
{
//read file and get height and width
bufferedImage = ImageIO.read(file);
height = bufferedImage.getHeight();
width = bufferedImage.getWidth();
//trim if rectangular
if(width >= height)
{
width = height;
}
if(height >= width)
{
height = width;
}
//set variables
bufferedImageNew = new BufferedImage(height, height, bufferedImage.TYPE_INT_ARGB);
widthNew = width;
heightNew = height;
colorsRed = new int [width][height];
colorsGreen = new int [width][height];
colorsBlue = new int [width][height];
//set background
g2d = (Graphics2D) bufferedImageNew.createGraphics();
g2d.setColor(Color.BLACK);
g2d.fillRect(0, 0, width, height);
//put all colors into arrays of red, green, and blue
getAllColors();
//draw circles
circles();
//display
displayBufferedImage(bufferedImageNew);
}

catch(IOException exception)
{
JOptionPane.showMessageDialog( this, exception );
}
}
//put all colors into arrays of red, green, and blue
public void getAllColors()
{
for(int i = 0; i < width; i++)
{
for(int j = 0; j < height; j++)
{
colorsRed[i][j] = getRed(bufferedImage.getRGB( i, j ));
colorsGreen[i][j] = getGreen(bufferedImage.getRGB( i, j ));
colorsBlue[i][j] = getBlue(bufferedImage.getRGB( i, j ));
}
}
}
//add up all of the colors in a given area and divide by the area to get the average
public void averageOfArea(int x1, int x2, int y1, int y2)
{
for(int i = x1; i < (x2); i++)
{
for(int j = y1; j < (y2); j++)
{
averageXR += colorsRed[i][j];
averageXG += colorsGreen[i][j];
averageXB += colorsBlue[i][j];
}
}
averageR = ((averageXR/(x2 - x1))/(y2 - y1));
averageG = ((averageXG/(x2 - x1))/(y2 - y1));
averageB = ((averageXB/(x2 - x1))/(y2 - y1));
}
//display image
public void displayBufferedImage(BufferedImage image)
{
this.setContentPane( new JScrollPane( new JLabel( new ImageIcon(image))));
this.validate();
}
}
