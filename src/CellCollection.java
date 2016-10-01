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
  
  public void init() {
    for (int i = 0; i < 2; i++) {
      int randX = rand.nextInt(sim.width);
      int randY = rand.nextInt(sim.height);
      add(new Cell(randX, randY));
    }
  }

  protected List<Cell> getCells() {
    return cells;
  }

  protected void setCells(List<Cell> cells) {
    this.cells = cells;
  }

  public Cell get(int i) {
    return cells.get(i);
  }

  public void add(Cell cell) {
    cells.add(cell);  
  }

  public void drawCells(Graphics2D g2d, AffineTransform identity) {
    ListIterator<Cell> it = getCells().listIterator();

    while (it.hasNext()) {
      Cell cell = it.next();
      cell.draw(g2d, identity);
    }
  }

  public void updateCells() {
    ListIterator<Cell> it = getCells().listIterator();

    while (it.hasNext()) {
      Cell cell = it.next();
      cell.update();
    }
  }
}
