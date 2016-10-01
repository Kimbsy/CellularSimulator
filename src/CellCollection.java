import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class CellCollection {

  // Reference to the simulation.
  CellularSimulator sim;

  // Random number generator.
  Random rand = new Random();

  // List of Cells in the simulation.
  protected List<Cell> cells = Collections.synchronizedList(new ArrayList<Cell>());

  public CellCollection(CellularSimulator sim) {
    this.sim = sim;
  }
  
  /**
   * Initialises the Cells for the simulation.
   */
  public void init() {
    for (int i = 0; i < sim.CELL_COUNT; i++) {
      int randX = rand.nextInt(sim.WIDTH);
      int randY = rand.nextInt(sim.HEIGHT);
      add(new Cell(randX, randY));
    }
  }

  /**
   * Gets the list of Cells.
   *
   * @return  The list of Cells.
   */
  protected List<Cell> getCells() {
    return cells;
  }

  /**
   * Sets the list of Cells.
   *
   * @param  cells  The list of Cells.
   */
  protected void setCells(List<Cell> cells) {
    this.cells = cells;
  }

  /**
   * Get a specific Cell from the collection.
   *
   * @param  i  The index of the Cell in the collection.
   *
   * @return  The Cell at the specified index.
   */
  public Cell get(int i) {
    return cells.get(i);
  }

  /**
   * Add a Cell to the collection.
   *
   * @param  cell  The new Cell.
   */
  public void add(Cell cell) {
    cells.add(cell);  
  }

  /**
   * Draws all the Cells.
   *
   * @param  g2d       The Graphics object.
   * @param  identity  The base coordinate transform.
   */
  public void drawCells(Graphics2D g2d, AffineTransform identity) {
    ListIterator<Cell> it = cells.listIterator();

    while (it.hasNext()) {
      Cell cell = it.next();
      cell.draw(g2d, identity);
    }
  }

  /**
   * Updates all the Cells.
   */
  public void updateCells() {
    ListIterator<Cell> it = cells.listIterator();

    while (it.hasNext()) {
      Cell cell = it.next();
      cell.update();
    }
  }
}
