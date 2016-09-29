import java.awt.*;

public abstract class Sprite {
  // Coordinates.
  protected int x;
  protected int y;

  // Shape of sprite.
  protected Polygon shape;

  // The color of the sprite.
  protected Color color;

  /**
   * Constructs the sprite.
   */
  public Sprite(int x, int y) {
    this.setX(x);
    this.setY(y);
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
