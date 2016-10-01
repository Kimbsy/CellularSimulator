import java.awt.*;
import java.awt.geom.*;

/**
 * The Sprite class is used as a base for all classes which need to be drawn.
 */
public abstract class Sprite {
  // Coordinates.
  protected int x;
  protected int y;

  // Shape of sprite.
  protected Shape shape;

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
  public static Shape getDefaultShape() {
    int[] xPoints = {0, 5, 5, 0};
    int[] yPoints = {0, 0, 5, 5};
    Shape poly  = new Polygon(xPoints, yPoints, 4);
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

  /**
   * Gets the X coordinate.
   *
   * @return  The X coordinate.
   */
  public int getX() {
    return x;
  }

  /**
   * Sets the X coordinate.
   *
   * @param  x  The X coordinate.
   */
  public void setX(int x) {
    this.x = limit(x, CellularSimulator.WIDTH);
  }

  /**
   * Increments the X coordinate.
   *
   * @param  i  How much to increment by.
   */
  public void incX(int i) {
    x = limit((x + i), CellularSimulator.WIDTH);
  }

  /**
   * Gets the Y coorindate.
   *
   * @return  The Y coordinate.
   */
  public int getY() {
    return y;
  }

  /**
   * Sets the Y coordinate.
   *
   * @param  x  The Y coordinate.
   */
  public void setY(int y) {
    this.y = limit(y, CellularSimulator.HEIGHT);
  }

  /**
   * Increments the Y Coordinate.
   *
   * @param  i  How much to increment by.
   */
  public void incY(int i) {
    y = limit((y + i), CellularSimulator.HEIGHT);
  }

  /**
   * Limits a number to between 0 and an upper bound.
   *
   * @param  i      The number to limit.
   * @param  limit  The upper bound.
   *
   * @return  The limited number.
   */
  public int limit(int i, int limit) {
    int limited = i;

    if (i > limit) {
      limited = i - limit;
    }
    if (i < 0) {
      limited = i + limit;
    }

    return limited;
  }

  /**
   * Gets the shape of the Sprite.
   *
   * @return  The shape of the Sprite.
   */
  public Shape getShape() {
    return shape;
  }

  /**
   * Sets the shape of the Sprite.
   *
   * @param  shape  The shape of the Sprite.
   */
  public void setShape(Shape shape) {
    this.shape = shape;
  }

  /**
   * Gets the color of the Sprite.
   *
   * @return  The color of the Sprite.
   */
  public Color getColor() {
    return color;
  }

  /**
   * Sets the color of the Sprite.
   *
   * @param  color  The color of the Sprite.
   */
  public void setColor(Color color) {
    this.color = color;
  }

  /**
   * Draws the Sprite to the screen.
   *
   * @param  g2d       The Graphics object.
   * @param  identity  The base coordinate transform.
   */
  public void draw(Graphics2D g2d, AffineTransform identity) {
    g2d.setTransform(identity);
    g2d.translate(x, y);
    g2d.setColor(getColor());
    g2d.draw(getShape());
  }
}
