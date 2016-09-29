import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;

public class CellularSimulator extends JFrame implements Runnable {

  // Main simulation thread.
  Thread sim;

  // Buffered image for smooth graphics.
  BufferedImage backBuffer;

  Graphics2D g2d;
  AffineTransform identity = new AffineTransform();

  // Define the frame dimensions.
  int width  = 900;
  int height = 600;

  Cell cell = new Cell(100, 100);

  /**
   * Create the simulation.
   * 
   * @param args The arguments passed in from the command line.
   */
  public static void main(String args[]) {
    new CellularSimulator();
  }

  /**
   * Configures the frame and creates and starts the main simulation thread.
   */
  public CellularSimulator() {
    super("CellularSimulator");
    setSize(width, height);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    sim = new Thread(this);
    sim.start();
    init();
  }

  /**
   * Initialise the simulation.
   * 
   * Creates the graphics for the backbuffer.
   */
  public void init() {
    backBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    g2d = backBuffer.createGraphics();

    cell.setShape(new Polygon(new int[] {0, 10, 10, 0}, new int[] {0, 0, 10, 10}, 4));
    cell.setColor(Color.GREEN);
  }

  /**
   * Update the main simulation thread.
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
   * Draw all the things.
   *
   * @param g The graphics object.
   */
  public void paint(Graphics g) {
    // Draw the backBuffer to the window.
    g.drawImage(backBuffer, 0, 0, this);

    // Reset the transform.
    g2d.setTransform(identity);

    // Draw the background.
    g2d.setColor(Color.BLACK);
    g2d.fillRect(0, 0, width, height);

    // Draw stuff.
    cell.draw(g2d);
  }

  /**
   * Update all the things.
   */
  public void simUpdate() {
    cell.move();
  }
}
