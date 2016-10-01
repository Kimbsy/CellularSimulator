import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;

public class CellularSimulator extends JFrame implements Runnable {

  // Class constants
  public static final int WIDTH      = 1600;
  public static final int HEIGHT     = 900;
  // public static final int WIDTH      = 600;
  // public static final int HEIGHT     = 300;
  public static final int CELL_COUNT = 50;

  // Random number generator.
  public static Random rand = new Random();

  // Main simulation thread.
  protected Thread sim;

  // Buffered image for smooth graphics.
  protected BufferedImage backBuffer;

  // Graphics object for drawing to the frame.
  protected Graphics2D g2d;

  // Base coordinate transform.
  protected AffineTransform identity = new AffineTransform();

  // The Cells in the simulation.
  protected CellCollection cells = new CellCollection();

  /**
   * Creates the simulation.
   * 
   * @param  args  The arguments passed in from the command line.
   */
  public static void main(String args[]) {
    new CellularSimulator();
  }

  /**
   * Configures the frame and creates and starts the main simulation thread.
   */
  public CellularSimulator() {
    super("CellularSimulator");
    setSize(WIDTH, HEIGHT);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    sim = new Thread(this);
    sim.start();
    init();
  }

  /**
   * Initialises the simulation.
   * 
   * Creates the graphics for the backbuffer.
   */
  public void init() {
    backBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    g2d = backBuffer.createGraphics();

    cells.init();
  }

  /**
   * Updates the main simulation thread.
   */
  public void run() {
    Thread t = Thread.currentThread();

    while (t == sim) {
      try {
        simUpdate();
        Thread.sleep(20);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      repaint();
    }
  }

  /**
   * Draws all the things.
   *
   * @param  g  The graphics object.
   */
  public void paint(Graphics g) {
    // Draw the backBuffer to the window.
    g.drawImage(backBuffer, 0, 0, this);

    // Reset the transform.
    g2d.setTransform(identity);

    // Draw the background.
    g2d.setColor(Color.BLACK);
    g2d.fillRect(0, 0, WIDTH, HEIGHT);

    // Draw stuff.
    cells.drawCells(g2d, identity);
  }

  /**
   * Updates all the things.
   */
  public void simUpdate() {
    cells.updateCells();
  }
}
