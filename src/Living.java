interface Living {

  /**
   * Gets the list of moves.
   *
   * @return  The list of moves.
   */
  public int[][] getMoveList();

  /**
   * Sets the list of moves.
   *
   * @param  moveList  The list of moves.
   */
  public void setMoveList(int[][] moveList);

  /**
   * Gets which move the entity is on.
   *
   * @return  The move index.
   */
  public int getMoveIndex();

  /**
   * Sets which move the entity is on.
   *
   * @param  moveIndex  The move index
   */
  public void setMoveIndex(int moveIndex);

  /**
   * Increments the move index.
   *
   * Wraps around to the start of the list.
   *
   * @param  i  How much to increment by.
   */
  public void incMoveIndex(int i);

  /**
   * Gets how far the entity has travelled for this move.
   *
   * @return  How far the entity has travelled.
   */
  public int getDistanceMoved();

  /**
   * Sets how far the entity has travelled for this move.
   *
   * @param  distanceMoved  How far the entity has travelled for this move.
   */
  public void setDistanceMoved(int distanceMoved);

  /**
   * Increments how far the entity has travelled for this move.
   *
   * @param  i  How much to increment by.
   */
  public void incDistanceMoved(int i);

  /**
   * Move the entity based on its moveList, moveIndex and distanceMoved.
   */
  public void move();

  /**
   * Get the rate at which the entity loses energy.
   *
   * @return     The metabolic rate.
   */
  public float getMetabolicRate();

  /**
   * Set the rate at which the entity loses energy.
   */
  public void setMetabolicRate(float metabolicRate);

  /**
   * Reduce the energy level of the entity.
   */
  public void metabolise();
}
