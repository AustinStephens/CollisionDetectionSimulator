class CollisionState extends State {
  
  private float timer;
  private final float MAX_TIMER = 0.25;
  
  private PVector[] drawnNormals1;
  private PVector[] drawnNormals2;
  private boolean movingPoly1 = false;
  private boolean movingPoly2 = false;
  
  private float timeSinceLastClick = 0;
  private boolean advancedMode = false;
  private int normalIndex = 0;
  private int pointIndex = 0;
  
  private boolean poly2Points = false;
  private boolean poly2Normals = false;
  
  private PVector previousMouse;
  
  private Polygon poly1;
  private Polygon poly2;

  
   public State update() {
     fill(0);
     textAlign(CENTER);
     textSize(14);
     text("Double click to " + (advancedMode ? "return to regular mode" : "go to advanced mode"), 400, 20);
     drawAABB = true;
     if(poly1.checkCollision(poly2))
     {
       poly1.colliding = true;
       poly2.colliding = true;
     }
    
    if(movingPoly1) {
      PVector dif = new PVector(mouseX - previousMouse.x, mouseY - previousMouse.y);
      PVector p = poly1.getPosition().get();
      poly1.setPosition(p.add(dif));
    } else if(movingPoly2) {
      PVector dif = new PVector(mouseX - previousMouse.x, mouseY - previousMouse.y);
      PVector p = poly2.getPosition().get();
      poly2.setPosition(p.add(dif));
    }
   
     stroke(0, 255, 255);
     strokeWeight(.75);
     for(int i = 0; i < drawnNormals1.length; i++) {
       PVector p = poly1.centerOfEdge(i);
       line(p.x, p.y, p.x + drawnNormals1[i].x, p.y + drawnNormals1[i].y);
     }
    
     for(int i = 0; i < drawnNormals2.length; i++) {
       PVector p = poly2.centerOfEdge(i);
       line(p.x, p.y, p.x + drawnNormals2[i].x, p.y + drawnNormals2[i].y);
     }
     
     if(advancedMode) {
       timer += Time.deltaTime;
       
       Polygon polyPoints = (poly2Points ? poly2 : poly1);
       Polygon polyNormals = (poly2Normals ? poly2 : poly1);
       
       PVector coe = polyNormals.centerOfEdge(normalIndex);
       PVector axis = PVector.mult(polyNormals.normals[normalIndex], 200);
       PVector pt = polyPoints.pointsTransformed[pointIndex];
       float value = PVector.dot(polyNormals.normals[normalIndex], pt) / pt.mag();
       
       PVector proj = PVector.add(coe, PVector.mult(polyNormals.normals[normalIndex], value * 50));
       
       //line(coe.x - axis.x, coe.y - axis.y, coe.x + axis.x, coe.y + axis.y);

       line(coe.x, coe.y, proj.x, proj.y);
       stroke(20);
       line(pt.x, pt.y, proj.x, proj.y);
       
       
       if(timer >= MAX_TIMER) {
         
         if(pointIndex < polyPoints.pointsTransformed.length - 1) pointIndex++;
         else {
           if(polyPoints == poly2) normalIndex++;
           pointIndex = 0;
           poly2Points = !poly2Points;
         }
         
         if(!(normalIndex < polyNormals.normals.length - 1)) {
           normalIndex = 0;
           poly2Normals = !poly2Normals;
         }
         
         timer = 0;
       } 
     }
      
     timeSinceLastClick += Time.deltaTime;
     previousMouse = new PVector(mouseX, mouseY);
     return null;
   }
   
   public CollisionState(Polygon poly1, Polygon poly2) {
      print("hello");
      this.poly1 = poly1;
      this.poly2 = poly2;
      drawnNormals1 = new PVector[poly1.normals.length];
      drawnNormals2 = new PVector[poly2.normals.length];
      
      for(int i = 0; i < drawnNormals1.length; i++) {
         drawnNormals1[i] = new PVector(-poly1.normals[i].x * Polygon.normalLength, -poly1.normals[i].y * Polygon.normalLength);
      }
      
      for(int i = 0; i < drawnNormals2.length; i++) {
        drawnNormals2[i] = new PVector(-poly2.normals[i].x * Polygon.normalLength, -poly2.normals[i].y * Polygon.normalLength);
      }
      
      previousMouse = new PVector(mouseX, mouseY);
   }
   
   public State onMousePressed() {
     //print(poly1.isColliding(new PVector(mouseX, mouseY)));
     if(poly1.isColliding(new PVector(mouseX, mouseY))) 
     {
       movingPoly1 = true;
     } else if (poly2.isColliding(new PVector(mouseX, mouseY))) {
       movingPoly2 = true;
     } 
     
     if(timeSinceLastClick < .2f) {advancedMode = !advancedMode; println("hey");}
     //print("not hit");
     return null;
   }
   
   public State onMouseReleased(Polygon poly1, Polygon poly2) {
     movingPoly1 = false;
     movingPoly2 = false;
     timeSinceLastClick = 0;
     return null;
   }
}
