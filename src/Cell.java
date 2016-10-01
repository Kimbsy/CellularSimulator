import java.awt.*;
import java.util.*;

public class Cell extends Sprite implements Living {

  // Class constants.
  public static final int UP    = 0;
  public static final int DOWN  = 1;
  public static final int LEFT  = 2;
  public static final int RIGHT = 3;

  // The size of the Cell.
  protected int size = 5;

  // List of moves.
  protected int[][] moveList;

  // Which move the Cell is on.
  protected int moveIndex = 0;

  // How far the Cell has travelled for this move.
  protected int distanceMoved = 0;

  // The amount of energy the Cell has.
  protected float energy = 512;

  // How quickly the Cell uses energy.
  protected float metabolicRate = 1;

  /**
   * Constructs a Cell specifying coordinates.
   *
   * Uses the default shape, color and move list.
   *
   * @param  x  The X coordinate of the Cell.
   * @param  y  The Y coordinate of the Cell.
   */
  public Cell(int x, int y) {
    super(x, y);

    // Set defaults.
    setShape(getDefaultShape());
    setColor(getDefaultColor());
    setMoveList(getDefaultMoveList());

    energy = CellularSimulator.rand.nextInt(1024);
  }

  /**
   * Constructs a Cell specifying all required attributes.
   *
   * @param  x         The X coordinate of the Cell.
   * @param  y         The Y coordinate of the Cell.
   * @param  shape     The shape of the Cell.
   * @param  moveList  The move list fo the Cell.
   */
  public Cell(int x, int y, Shape shape, int[][] moveList) {
    super(x, y);

    setShape(shape);
    setColor(color);
    setMoveList(moveList);
    energy = CellularSimulator.rand.nextInt(1024);
  }

  /**
   * Gets the default shape for a Cell.
   *
   * @return  The default shape.
   */
  public static Polygon getDefaultShape() {
    int[] xPoints = {0, 5, 5, 0};
    int[] yPoints = {0, 0, 5, 5};
    Polygon poly  = new Polygon(xPoints, yPoints, 4);
    return poly;
  }

  /**
   * Gets the default color for a Cell.
   *
   * @return  The default color.
   */
  public static Color getDefaultColor() {
    Color color = Color.GREEN;
    return color;
  }

  /**
   * Get the color of a Cell based on its energy level.
   *
   * @return  The color of the Cell.
   */
  public Color getColor() {
    float percentage = energy / 1024;
    int normalised   = (int) (percentage * 205);
    normalised = Math.min(normalised, 205);

    int r = normalised;
    int g = normalised + 50;
    int b = normalised;

    return new Color(r, g, b);
  }

  /**
   * Gets the default move list for a Cell.
   *
   * @return  The default move list.
   */
  public static int[][] getDefaultMoveList() {
    int[] directions = {UP, RIGHT, DOWN, LEFT};
    int[] distances  = {20, 20, 20, 20};
    int[][] moveList = {directions, distances};
    return moveList;
  }

  /**
   * Gets a random move list for a Cell.
   *
   * @return  The random move list.
   */
  public static int[][] getRandomMoveList() {
    int moveCount    = CellularSimulator.rand.nextInt(9) + 1;
    int[] directions = new int[moveCount];
    int[] distances  = new int[moveCount];

    for (int i = 0; i < moveCount; i++) {
      directions[i] = CellularSimulator.rand.nextInt(4);
      distances[i]  = CellularSimulator.rand.nextInt(40);
    }

    int[][] moveList = {directions, distances};
    return moveList;
  }

  /**
   * Gets the list of moves.
   *
   * @return  The list of moves.
   */
  public int[][] getMoveList() {
    return moveList;
  }

  /**
   * Sets the list of moves.
   *
   * @param  moveList  The list of moves.
   */
  public void setMoveList(int[][] moveList) {
    this.moveList = moveList;
  }

  /**
   * Gets which move the Cell is on.
   *
   * @return  The move index.
   */
  public int getMoveIndex() {
    return moveIndex;
  }

  /**
   * Sets which move the Cell is on.
   *
   * @param  moveIndex  The move index
   */
  public void setMoveIndex(int moveIndex) {
    this.moveIndex = moveIndex;
  }

  /**
   * Increments the move index.
   *
   * Wraps around to the start of the list.
   *
   * @param  i  How much to increment by.
   */
  public void incMoveIndex(int i) {
    moveIndex = (moveIndex + i) % moveList[0].length;
  }

  /**
   * Gets how far the Cell has travelled for this move.
   *
   * @return  How far the Cell has travelled.
   */
  public int getDistanceMoved() {
    return distanceMoved;
  }

  /**
   * Sets how far the Cell has travelled for this move.
   *
   * @param  distanceMoved  How far the Cell has travelled for this move.
   */
  public void setDistanceMoved(int distanceMoved) {
    this.distanceMoved = distanceMoved;
  }

  /**
   * Increments how far the Cell has travelled for this move.
   *
   * @param  i  How much to increment by.
   */
  public void incDistanceMoved(int i) {
    distanceMoved += i;
  }

  /**
   * Gets the amount of energy the Cell has.
   *
   * @return  The amount of energy.
   */
  public float getEnergy() {
    return energy;
  }

  /**
   * Sets the amount of energy the Cell has.
   *
   * @param  energy  The amount of energy.
   */
  public void setEnergy(float energy) {
    this.energy = energy;
  }

  /**
   * Gets the rate at which the Cell loses energy.
   *
   * @return  The metabolic rate.
   */
  public float getMetabolicRate() {
    return metabolicRate;
  }

  /**
   * Sets the rate at which the Cell loses energy.
   */
  public void setMetabolicRate(float metabolicRate) {
    this.metabolicRate = metabolicRate;
  }

  /**
   * Updates this Cell.
   *
   * @param  iter  An iterator for the simulation CellCollection.
   * @param  sim   The simulation.
   */
  public void update(ListIterator<Cell> iter, CellularSimulator sim) {
    if (energy <= 0) {
      return;
    }
    move();
    absorb(sim.foodMap);
    divide(iter);
    metabolise();
  }

  /**
   * Moves the Cell based on its moveList, moveIndex and distanceMoved.
   */
  public void move() {
    int distance = moveList[1][moveIndex];

    if (distanceMoved >= distance) {
      setDistanceMoved(0);
      incMoveIndex(1);
    }

    int currentMove = moveList[0][moveIndex];

    switch (currentMove) {
      case UP:
        incY(-1);
        break;
      case DOWN:
        incY(1);
        break;
      case LEFT:
        incX(-1);
        break;
      case RIGHT:
        incX(1);
        break;
    }

    incDistanceMoved(1);
  }

  /**
   * Absorbs energy from the surrounding environment.
   */
  public void absorb(FoodMap foodMap) {
    int minX = x;
    int maxX = x + size;
    int minY = y;
    int maxY = y + size;

    float absorbedEnergy = foodMap.absorbFromArea(minX, maxX, minY, maxY);
    energy = Math.min((energy + absorbedEnergy), 1024);
  }

  /**
   * Creates a new Cell based on this one.
   *
   * @param  iter  An iterator for the simulation CellCollection.
   */
  public void divide(ListIterator<Cell> iter) {
    if (energy >= 1024) {
      float newEnergy = energy / 2;
      Cell child = new Cell((x - size), (y - size), getShape(), getMoveList());
      child.setEnergy(newEnergy);
      setEnergy(newEnergy);
      iter.add(child);
    }
  }

  /**
   * Reduces the energy level of the Cell.
   */
  public void metabolise() {
    energy = Math.max((energy - metabolicRate), 0);
    if (energy - metabolicRate > 0) {
      energy -= metabolicRate;
    }
  }
}
