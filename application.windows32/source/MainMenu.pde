class MainMenu extends Scene {
  
  Polygon poly = new Polygon();
  
  public void setup() {
    
    background(50);
    stroke(0);
    strokeWeight(2);
    textSize(40);
    textAlign(CENTER);
    fill(0, 0, 255);
    text("Collision Simulator", 400, 60);
    textSize(13);
    fill(255);
    text("Press backspace to return to main menu", 400, 90);
    
    fill(0, 255, 255, 100);
    ellipse(150, 200, 200, 200);
    textSize(15);
    fill(0);
    text("Radial Collision", 150, 165);
    
    stroke(0);
   
    ellipse(150, 200, 5, 5);
    line(150, 200, 250, 200);
    
    fill(255, 0, 0, 100);
    quad(550, 120, 550, 320, 750, 320, 750, 120);
    fill(0);
    text("AABB Collision", 650, 190);
    
    for(int i = 0; i < 3; i++)
      poly.changeFill();
    
    poly.addPoint(350, 330);
    poly.addPoint(235, 410);
    poly.addPoint(285, 540);
    poly.addPoint(415, 540);
    poly.addPoint(465, 410);
    
    
    
    poly.draw();
    stroke(0, 255, 255);
    strokeWeight(.75);
    for(int i = 0; i < poly.normals.length; i++) {
      PVector p = poly.centerOfEdge(i);
      line(p.x, p.y, p.x + (poly.normals[i].x * -25), p.y + (poly.normals[i].y * -25));
    }
    fill(0);
    text("Polygonal Collision", 350, 420);
    
  }
  
  public void update() {
    poly.update();
  }
  
  public void mouseReleased() {
    PVector pt = new PVector(mouseX, mouseY);
    if(PVector.sub(new PVector(150, 200), pt).mag() <= 100) {
      println("radial");
      scene = new Radial_Collision();
      scene.setup();
    } else if (pt.x >= 550 && pt.x <= 750 && pt.y >= 120 && pt.y <= 320) {
      scene = new AABB_Prototype();
      scene.setup();
    } else if (poly.isCollidingColor(pt)) {
      scene = new PolygonCollision();
      scene.setup();
    }
  }
  
}
