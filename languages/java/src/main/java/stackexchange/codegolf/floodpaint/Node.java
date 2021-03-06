 package stackexchange.codegolf.floodpaint;

import java.util.HashSet;
import java.util.Set;

 
public class Node {
  private Node north;
  private Node south;
  private Node east;
  private Node west;
  
  private Node accessor;
  
  private Coordinate c;
  private int value;
  private boolean isCenter;
  
  public Node(final Coordinate c, int v){
    this.c = c;
    this.value = v;
  }

  public static Node clone(Node source){
    if(null == source){
      return null;
    }
    
    final Node n = new Node(source.c, source.value);
    n.north = Node.clone(source.north);
    n.south = Node.clone(source.south);
    n.east = Node.clone(source.east);
    n.west = Node.clone(source.west);
    
    n.accessor = Node.clone(source.accessor);
    
    return n;
  }
  public boolean isCenter() {
    return isCenter;
  }

  public void setIsCenter(boolean isCenter) {
    this.isCenter = isCenter;
  }
  
  public Coordinate getCoordinate(){
    return c;
  }
  public Integer getValue(){
    return this.value;
  }
  
  public void paint(Integer v){
    this.value = v;
  }
  
  public String toString(){
    return String.format("{ %d,%d : %d }", c.row, c.column, value);
  }

  void assignNeighbor(Node x, Direction direction) {
    switch(direction){
      case N: this.north = x;break;
      case E: this.south = x;break;
      case W: this.west = x;break;
      case S: this.east = x;break;
    }
  }
  public Node getNeighbor(Direction direction) {
    switch(direction){
      case N: return this.north;
      case E: return this.south;
      case W: return this.west;
      case S: return this.east;
    }
    return null;
  }
  
  public String neighbors(){
    final StringBuilder sb = new StringBuilder();
    
    sb.append(this).append(" | ");
    
    for(Direction d : Direction.values()){
      sb.append(" { "+ d +" : " + getNeighbor(d) + " } ");
    }
    
    return sb.toString();
  }
    
  public Set<Node> getAllLikeValuedNeighbors(){
    final Set<Node> nodes = new HashSet<Node>();
    recursiveLikeValuedNeighbors(getLikeValuedNeighbors(), nodes, new HashSet<Node>());
    return  nodes;
  }
  
  private void recursiveLikeValuedNeighbors(Set<Node> nodes, Set<Node> repo, Set<Node> alreadySeen){
    for(Node n : nodes){
      if(!alreadySeen.contains(n)){
        alreadySeen.add(n);
        repo.add(n);
        recursiveLikeValuedNeighbors(n.getLikeValuedNeighbors(), repo, alreadySeen);
      }
    }
  }
  
  public Set<Node> getLikeValuedNeighbors(){
    return  neighbors(true);
  }
  
  public Set<Node> getNeighbors(){
    return neighbors(false);
  }
  
  private Set<Node> neighbors(boolean likeValued){
     final Set<Node> nodes = new HashSet<Node>();
    
    for(Direction d : Direction.values()){
      
      final Node neighbor = getNeighbor(d);
      if(null != neighbor && (!likeValued || likeValued && null!=neighbor && neighbor.value == this.value))
      { 
        nodes.add(neighbor);
        neighbor.accessor = this;
      }
    }
    return nodes;
  }

  public Node getAccessor() {
    return accessor;
  }
  public void clearAccessor(){
    this.accessor = null;
  }
}
