import java.awt.*;

public class Cell extends Sprite implements Living {

  // Class constants.
  public static final int UP    = 0;
  public static final int DOWN  = 1;
  public static final int LEFT  = 2;
  public static final int RIGHT = 3;

  // List of moves.
  protected int[][] moveList;

  // Which move the Cell is on.
  protected int moveIndex = 0;

  // How far the Cell has travelled for this move.
  protected int distanceMoved = 0;

  public Cell(int x, int y) {
    super(x, y);

    initShape();

    initMoves();
  }

  protected void initShape() {
    int[] xPoints = {0, 10, 10, 0};
    int[] yPoints = {0, 0, 10, 10};
    Polygon poly = new Polygon(xPoints, yPoints, 4);
    this.setShape(poly);
  }

  protected void initMoves() {
    int[] directions = {UP, RIGHT, DOWN, LEFT};
    int[] distances  = {20, 20, 20, 20};
    int[][] list = {directions, distances};
    this.setMoveList(list);
  }

  public int[][] getMoveList() {
    return this.moveList;
  }
  public void setMoveList(int[][] moveList) {
    this.moveList = moveList;
  }

  public int getMoveIndex() {
    return this.moveIndex;
  }
  public void setMoveIndex(int moveIndex) {
    this.moveIndex = moveIndex;
  }
  public void incMoveIndex(int i) {
    this.moveIndex = (this.moveIndex + i) % this.moveList[0].length;
  }

  public int getDistanceMoved() {
    return this.distanceMoved;
  }
  public void setDistanceMoved(int distanceMoved) {
    this.distanceMoved = distanceMoved;
  }
  public void incDistanceMoved(int i) {
    this.distanceMoved += i;
  }

  public void move() {
    int distance = getMoveList()[1][getMoveIndex()];
    if (getDistanceMoved() >= distance) {
      setDistanceMoved(0);
      incMoveIndex(1);
    }

    int currentMove = this.moveList[0][this.moveIndex];

    switch (currentMove) {
      case UP:
        this.incY(-1);
        break;
      case DOWN:
        this.incY(1);
        break;
      case LEFT:
        this.incX(-1);
        break;
      case RIGHT:
        this.incX(1);
        break;
    }

    incDistanceMoved(1);
  }
}
