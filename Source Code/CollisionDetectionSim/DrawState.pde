class DrawState extends State{
  
  private float timer = 0;
  private final float MAX_TIMER = .5;
  private ArrayList<PVector> drawnNormals1 = new ArrayList<PVector>();
  private ArrayList<PVector> drawnNormals2 = new ArrayList<PVector>();
  private int currentNormalIndex = 0;
  
  private boolean drawPoly2Normals = false;

  private Polygon poly1;
  private Polygon poly2;
  
  public DrawState(Polygon poly1, Polygon poly2)
  {
    this.poly1 = poly1;
    this.poly2 = poly2;
    poly1.onDrawState();
    poly2.onDrawState();
    drawnNormals1.add(new PVector(0,0));
    drawnNormals2.add(new PVector(0,0));
    
  }
 
  public State update() {
    timer += Time.deltaTime;
    //print(timer);
    
    if(!drawPoly2Normals && poly1.normals.length > 0)
        drawnNormals1.set(currentNormalIndex, new PVector(-poly1.normals[currentNormalIndex].x * timer / MAX_TIMER * Polygon.normalLength, -poly1.normals[currentNormalIndex].y * timer / MAX_TIMER * Polygon.normalLength));
    else if(poly2.normals.length > 0)
        drawnNormals2.set(currentNormalIndex, new PVector(-poly2.normals[currentNormalIndex].x * timer / MAX_TIMER * Polygon.normalLength, -poly2.normals[currentNormalIndex].y * timer / MAX_TIMER * Polygon.normalLength));
    
    
    if(timer >= MAX_TIMER) {
      timer = MAX_TIMER;
      if(!drawPoly2Normals) {
        if(currentNormalIndex < poly1.normals.length - 1) {
            currentNormalIndex++;
            drawnNormals1.add(new PVector(0,0));
        }
        else {
          currentNormalIndex = 0;
          drawPoly2Normals = true;
        }
      } else {
        if(currentNormalIndex < poly2.normals.length - 1) {
          currentNormalIndex++;
          drawnNormals2.add(new PVector(0,0));
        }
        else 
          return new CollisionState(poly1, poly2);
      }
      
      
      
      timer = 0;
    }
    //print(poly1.normals.length);
    
    stroke(0, 255, 255);
    strokeWeight(.75);
    for(int i = 0; i < drawnNormals1.size(); i++) {
       if(poly1.normals.length > i) {
         PVector p = poly1.centerOfEdge(i);
         //print(drawnNormals1.get(i));
         line(p.x, p.y, p.x + drawnNormals1.get(i).x, p.y + drawnNormals1.get(i).y);
       }
    }
    
    for(int i = 0; i < drawnNormals2.size(); i++) {
      if(poly2.normals.length > i) {
         PVector p = poly2.centerOfEdge(i);
         line(p.x, p.y, p.x + drawnNormals2.get(i).x, p.y + drawnNormals2.get(i).y);
      }
    }
    
    return null;
    
  }
  
}
