class PolygonCollision extends Scene {
  public State state = new NoAnimation();
  Polygon polygon1 = new Polygon();
  Polygon polygon2 = new Polygon();
  
  boolean isDoneWithPoly1 = false;
  boolean isDoneWithPoly2 = false;
  boolean isHeld = false;
  
  float timeStartHeld = 0;
  
  public void setup() {
  }
  
  public void draw() {
    background(60);
    noFill();
    
    stroke(0);
    
    line(0, 300, 800, 300); // x axis
    line(400, 0, 400, 800); // y axis
    
    update();
    
    polygon1.draw();
    polygon2.draw();
  }
  
  void update() {
    Time.update(millis());
    
    polygon1.update();
    polygon2.update();
    
    State newState = state.update();
    if(newState != null)
      state = newState;
    
  }
  
  public void keyPressed() {
    if(keyCode == BACKSPACE) {
      drawAABB = false;
      scene = new MainMenu();
      scene.setup();
    }
  }
  
  public void mousePressed() {
    state.onMousePressed();
    timeStartHeld = millis();
  }
  public void mouseReleased() {
    State newState = state.onMouseReleased(polygon1, polygon2);
    if(newState != null) state = newState;
  }
}
