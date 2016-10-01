import java.awt.*;

/**
 * The Sprite class is used as a base for all classes which need to be drawn.
 */
public abstract class Sprite {
  // Coordinates.
  protected int x;
  protected int y;

  // Shape of sprite.
  protected Polygon shape;

  // The color of the sprite.
  protected Color color;

  /**
   * Constructs a Sprite specifying coordinates.
   *
   * @param  x  The X coordinate of the Sprite.
   * @param  y  The Y coordinate of the Sprite.
   */
  public Sprite(int x, int y) {
    setX(x);
    setY(y);

    setShape(getDefaultShape());
    setColor(getDefaultColor());
  }

  /**
   * Gets the default shape for a Sprite.
   *
   * @return  The default shape.
   */
  public static Polygon getDefaultShape() {
    int[] xPoints = {0, 10, 10, 0};
    int[] yPoints = {0, 0, 10, 10};
    Polygon poly  = new Polygon(xPoints, yPoints, 4);
    return poly;
  }

  /**
   * Gets the default color for a Sprite.
   *
   * @return  The default color.
   */
  public static Color getDefaultColor() {
    Color color = Color.WHITE;
    return color;
  }

  public int getX() {
    return this.x;
  }
  public void setX(int x) {
    this.x = x;
  }
  public void incX(int i) {
    this.x += i;
  }

  public int getY() {
    return this.y;
  }
  public void setY(int y) {
    this.y = y;
  }
  public void incY(int i) {
    this.y += i;
  }

  public Polygon getShape() {
    return this.shape;
  }
  public void setShape(Polygon shape) {
    this.shape = shape;
  }

  public Color getColor() {
    return this.color;
  }
  public void setColor(Color color) {
    this.color = color;
  }

  public void draw(Graphics2D g2d) {
    g2d.setColor(this.getColor());
    g2d.translate(x, y);
    g2d.fillPolygon(this.getShape());
  }
}
