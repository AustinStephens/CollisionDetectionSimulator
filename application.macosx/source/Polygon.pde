class Polygon { 
  
  public static final int normalLength = 20;
  
  public boolean colliding = false; // change to onCollision()
  private boolean doneChecking = false;
  private boolean dirty = true;

  private ArrayList<PVector> points = new ArrayList<PVector>();
  public PVector[] pointsTransformed;
  public PVector[] normals;
  private PVector[] edges;
  
  private PVector position;
  private boolean posIsSet = false;
  
  private color[] colors = {color(255, 255, 255), color(255, 0 ,0), color(0, 0, 255), color(71, 178, 44), color(237, 291, 26), color(255, 141, 25), color(168, 38, 255), color(255, 38, 251), color(25, 235, 255)};
  private float alpha = 75f;
  
  private int colorIndex = 0;
  
  public PVector getPosition() {
    return position;
  }
  
  public void setPosition(PVector p) {
     position = p; 
     dirty = true;
  }

  AABB aabb = new AABB();

  void update() {
    
    doneChecking = false;
    
    colliding = false; 
    aabb.resetColliding();

    if (dirty) recalc();
  }
 
  void recalc() {

    dirty = false; // reset the dirty flag

    // build a transformation matrix:
    PMatrix2D matrix = new PMatrix2D();
    if(posIsSet) matrix.translate(position.x, position.y);

    pointsTransformed = new PVector[points.size()];
    for (int i = 0; i < points.size(); i++) {
      PVector p = new PVector();
      matrix.mult(points.get(i), p); // get the transformed point, store it in (p)
      pointsTransformed[i] = p; // store (p) in the pointsTransformed array
    }
    
    //if(pointsTransformed.length != 0) print(pointsTransformed[0]);

    edges = new PVector[points.size()];
    normals = new PVector[points.size()];
    for(int i = 0; i < points.size(); i++) {
        int j = i + 1;
        if(i >= pointsTransformed.length - 1) j = 0;
        
        PVector p1 = pointsTransformed[i];
        PVector p2 = pointsTransformed[j];
        
        edges[i] = new PVector(p2.x - p1.x, p2.y - p1.y);
        normals[i] = new PVector(edges[i].y, -edges[i].x);
        normals[i].normalize();
    }

    // update this object's AABB:
    aabb.recalc(pointsTransformed);
  }

  void draw() {

    if(points.size() <= 1) return;
    aabb.draw();
    
    stroke(0);
    if(colliding) stroke(150, 150, 150);
    strokeWeight(1.5);
    
    //alpha = (colorIndex == 0 ? 1 : 0.5f);
    fill(colors[colorIndex], alpha);
    
    //fill(255, 0, 0);
   
    if(position == null) {
      beginShape();
      for (int i = 0; i < points.size(); i++) {
        vertex(points.get(i).x, points.get(i).y);
      }
      vertex(points.get(0).x, points.get(0).y);
      endShape();
    } else { 
      beginShape();
      for (int i = 0; i < points.size(); i++) {
        vertex(pointsTransformed[i].x, pointsTransformed[i].y);
      }
      endShape();
    }
    
    /*if(position == null) {
      for(int i = 0; i < points.size() - 1; i++) {
           line(points.get(i).x, points.get(i).y, points.get(i+1).x, points.get(i+1).y);
      }
      line(points.get(points.size()-1).x, points.get(points.size()-1).y, points.get(0).x, points.get(0).y);
    } else {
      for(int i = 0; i < pointsTransformed.length - 1; i++) {
         line(pointsTransformed[i].x, pointsTransformed[i].y, pointsTransformed[i+1].x, pointsTransformed[i+1].y);
      }
      line(pointsTransformed[pointsTransformed.length-1].x, pointsTransformed[pointsTransformed.length-1].y, pointsTransformed[0].x, pointsTransformed[0].y);
    }*/

    
    
  }

  void addPoint(PVector p) {
    points.add(p);
    recalc();
    aabb.recalc(points.toArray(new PVector[points.size()]));
  }
  
  void addPoint(float x, float y) {
    addPoint(new PVector(x,y));
  }

  boolean checkCollision(Polygon poly) {

    if (aabb.checkCollision(poly.aabb)) 
    {  
      for(PVector n : normals) {
        PVector mm1 = projectAlongAxis(n);
        PVector mm2 = poly.projectAlongAxis(n);
        if(mm2.x > mm1.y || mm1.x > mm2.y) return false;
      }
      
      for(PVector n : poly.normals) {
        PVector mm1 = projectAlongAxis(n);
        PVector mm2 = poly.projectAlongAxis(n);
        if(mm2.x > mm1.y || mm1.x > mm2.y) return false;
      }

      return true;
    }
    return false;
  }
  
  public PVector projectAlongAxis(PVector axis) {
    PVector mm = new PVector();
    for(int i = 0; i < pointsTransformed.length; i++) {
      float VALUE = pointsTransformed[i].dot(axis);
      if(i == 0 || VALUE < mm.x) mm.x = VALUE;
      if(i == 0 || VALUE > mm.y) mm.y = VALUE;
    }
    
    return mm;
  }
  
  public void onDrawState() {
    posIsSet = true;
    setPosition(points.get(0));
    points.set(0, new PVector(0,0));
    for(int i = 1; i < points.size(); i++) {
      points.set(i, points.get(i).sub(position));
      //points.get(i).sub(position);
    }
    points.add(new PVector(0,0));
  }
  
  public PVector centerOfEdge(int index) {
    int j = (index == pointsTransformed.length - 1 ? 0 : index + 1);
    PVector p = new PVector(pointsTransformed[index].x , pointsTransformed[index].y);
    return p.add(pointsTransformed[j]).div(2);
  }
  
  public boolean isColliding(PVector point) {
    
    if (point.x > aabb.xmax || point.x < aabb.xmin || point.y > aabb.ymax || point.y < aabb.ymin) return false;
    // in bounding box, now check in polygon:
    for (int i = 0; i < pointsTransformed.length; i++) {
      PVector t = pointsTransformed[i];
      PVector p = point.get();
      p.sub(t);
      float result = normals[i].dot(p);
      //print(result + " ");
      if (result < 0) return false;
    }
    return true;
  }
  
  public boolean isCollidingColor(PVector point) {
    
    if (point.x > aabb.xmax || point.x < aabb.xmin || point.y > aabb.ymax || point.y < aabb.ymin) return false;
    // in bounding box, now check in polygon:
    for (int i = 0; i < points.size(); i++) {
      PVector t = points.get(i);
      PVector p = point.get();
      p.sub(t);
      float result = normals[i].dot(p);
      //print(result + " ");
      if (result < 0) return false;
    }
    return true;
  }
  
  public void changeFill() {
    colorIndex = (colorIndex + 1) % colors.length;
  }
}
