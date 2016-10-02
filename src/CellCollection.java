import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class CellCollection {

  // List of Cells in the simulation.
  protected List<Cell> cells = new CopyOnWriteArrayList<Cell>();
  
  /**
   * Initialises the Cells for the simulation.
   */
  public void init() {
    for (int i = 0; i < CellularSimulator.CELL_COUNT; i++) {
      int randX = CellularSimulator.rand.nextInt(CellularSimulator.WIDTH);
      int randY = CellularSimulator.rand.nextInt(CellularSimulator.HEIGHT);

      Cell cell = new Cell(randX, randY);

      int[][] moveList = Cell.getRandomMoveList();
      cell.setMoveList(moveList);
      cell.updateStationaryFactor();

      // double multiplier = 4 * Math.pow(cell.stationaryFactor, 4);
      // System.out.println(cell.stationaryFactor + " >> " + Math.max(multiplier, 1));

      add(cell);
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
   * Removes a specific Cell from the collection.
   *
   * @param  cell  The Cell to remove.
   */
  public void remove(Cell cell) {
    cells.remove(cell);
  }

  /**
   * Draws all the Cells.
   *
   * @param  g2d       The Graphics object.
   * @param  identity  The base coordinate transform.
   */
  public void draw(Graphics2D g2d, AffineTransform identity) {
    ListIterator<Cell> iter = cells.listIterator();

    while (iter.hasNext()) {
      Cell cell = iter.next();
      cell.draw(g2d, identity);
    }
  }

  /**
   * Updates all the Cells.
   *
   * @param  sim  The simulation.
   */
  public void update(CellularSimulator sim) {
    ListIterator<Cell> iter = cells.listIterator();

    while (iter.hasNext()) {
      Cell cell = iter.next();
      cell.update(sim);
    }
  }
}
