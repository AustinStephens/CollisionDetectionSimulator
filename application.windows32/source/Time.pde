static class Time {
 
  public static float deltaTime = 0;
  public static float timeScale = 1;
  private static float prevTime = 0;
  
  public static void update(float milis) {
    float currentTime = milis;
    deltaTime = (currentTime - prevTime) / 1000;
    prevTime = currentTime;
  }
  
  public static float toSeconds(float milis) {
    return milis / 1000 * timeScale;
  }
  
}
