class NoAnimation extends State {
  
  boolean isDoneWithPoly1 = false;
  boolean isDoneWithPoly2 = false;
  int timeStartHeld = 0;
  
  public State update() { 
    fill(0);
    textAlign(CENTER);
    textSize(14);
    text("Create your polygons, press inside the polygon to change the color", 400, 20);
    text("Press and hold to move on to " + (!isDoneWithPoly1 ? "the next Polygon" : "the collision mode"), 400, 40);
    return null; 
  }
  
  public State onMousePressed() {
    timeStartHeld = millis();
    return null;
  }
  
  public State onMouseReleased(Polygon poly1, Polygon poly2) {
    if(Time.toSeconds(millis() - timeStartHeld) < .3)
    {
     if(!isDoneWithPoly1) {
       //println(poly1.isColliding(new PVector(mouseX, mouseY)));
       if(poly1.isCollidingColor(new PVector(mouseX, mouseY)))
         poly1.changeFill();
       else {
         poly1.addPoint(new PVector(mouseX, mouseY)); print("x"); }
     } else if(!isDoneWithPoly2) {
       if(poly2.isCollidingColor(new PVector(mouseX, mouseY)))
         poly2.changeFill();
       else
         poly2.addPoint(new PVector(mouseX, mouseY));
     }
    }
    else
    {
     if(!isDoneWithPoly1)
       isDoneWithPoly1 = true;
     else {
       isDoneWithPoly2 = true;
       return new DrawState(poly1, poly2);
     }
    }
    return null;
  }
}
