interface Living {

  public int[][] getMoveList();
  public void setMoveList(int[][] moveList);

  public int getMoveIndex();
  public void setMoveIndex(int moveIndex);
  public void incMoveIndex(int i);

  public int getDistanceMoved();
  public void setDistanceMoved(int distanceMoved);
  public void incDistanceMoved(int i);

  public void move();
}
