import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

/**
 * The FoodMap class represents the distribution of food available in the simulation.
 * 
 * A two dimensional array determines the energy levels of each coordinate point.
 */
public class FoodMap {

  public static final int INITIAL_FOOD = 10;

  // The width and height of the Simulation.
  protected int width  = CellularSimulator.WIDTH;
  protected int height = CellularSimulator.HEIGHT;

  // The energy levels of erach pixel in the simulation.
  protected int[][] map = new int[width][height];

  /**
   * Initialises the FoodMap.
   */
  public void init() {
    for (int i = 0; i < INITIAL_FOOD; i++) {
      update();
    }
  }

  /**
   * Adds food to random coordinate locations.
   */
  public void update() {
    for (int i = 0; i < CellularSimulator.FOOD_RATE; i++) {
      int randX = CellularSimulator.rand.nextInt(width);
      int randY = CellularSimulator.rand.nextInt(height);

      map[randX][randY] = CellularSimulator.rand.nextInt(256);
    }
  }

  /**
   * Drains some energy from an area defined by a rectangle.
   *
   * @param  minX  The left edge.
   * @param  maxX  The right edge.
   * @param  minY  The top edge.
   * @param  maxY  The bottom edge.
   *
   * @return  How much energy was drained.
   */
  public float absorbFromArea(int minX, int maxX, int minY, int maxY) {
    float absorbedEnergy = 0;

    for (int i = minX; i < maxX; i++) {
      for (int j = minY; j < maxY; j++) {
        if (map[i % width][j % height] > 0) {
          map[i % width][j % height]-=10;
          absorbedEnergy+=0.5;
        }
      }
    }

    return absorbedEnergy;
  }

  // /**
  //  * Draws the FoodMap to the screen.
  //  *
  //  * @param  g2d       The Graphics object.
  //  * @param  identity  The base coordinate transform.
  //  */
  // public void draw(CellularSimulator sim, Graphics2D g2d, AffineTransform identity) {
  //   BufferedImage mapImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

  //   int[] pixels = new int[width * height * 3];

  //   int ind = 0;
  //   for (int i = 0; i < width; i++) {
  //     for (int j = 0; j < height; j++) {
  //       pixels[ind + (j * 3) + 0] = map[i][j];
  //       pixels[ind + (j * 3) + 1] = map[i][j];
  //       pixels[ind + (j * 3) + 2] = map[i][j];
  //     }
  //     ind += 3;
  //   }

  //   g2d.setTransform(identity);
  //   g2d.drawImage(mapImage, 0, 0, sim);
  // }
}
