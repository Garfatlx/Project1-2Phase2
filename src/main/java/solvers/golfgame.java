package solvers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** 
 * The main engine
*/
public class GolfGame {
    private double minDis=100;
    private boolean goal=false;
    private double[] minCoordinate=new double[4];
    private double[] stopCoordinate=new double[4];

    private MySolver solver;
    private double[] a;
    private double dt;
    private double[] hole;
    private double r;
    private String mappath;

    /**
     * Construct the game engine
     * 
     * @param solver    ODE sovler
     * @param a         friction coefficients, [knetic fraction, static friction]
     * @param dt        time step
     * @param hole      position of the hole
     * @param r         radius of the hole
     * @param mappath   the path of the map
     */
    public GolfGame(MySolver solver, double[] a, double dt,double[] hole, double r, String mappath){
        this.solver=solver;
        this.a=a;
        this.dt=dt;
        this.hole=hole;
        this.r=r;
        this.mappath=mappath;
    }

    /**
     * shoot!
     * 
     * @param x             the starting position and velocity of the ball
     * @param recording     whether save the trajectory. Disable recording in AI bot calculation to save memory. 
     * @return              The trajectory of the ball. It is null if recording is false.
     */
    public ArrayList<double[]> shoot(double[] x,Boolean recording){
        
        ArrayList<double[]> xtrac=new ArrayList<double[]>();
        xtrac.clear();
        xtrac.add(x.clone());
        MapHandler map=new MapHandler();
        MyFunction golfPhysics=new golfphysics();
        
        
        //Read the map, store the gradient 
        map.readmap(mappath);
        double[][][] mapgradient=map.getGradient();
        int[][] redElm=map.getRed();
        int[][] blueElm=map.getBlue();
        this.minDis=getDistance(x, hole);
        double dis=100;
        //loop untill ball stop or out of court
        while (!solver.nextstep(golfPhysics,x,a,mapgradient[Utility.coordinateToPixel_X(x[0])][Utility.coordinateToPixel_Y(x[1])],dt)) {
            //check whether out of court
            if (Utility.coordinateToPixel_X(x[0])>=mapgradient.length || Utility.coordinateToPixel_Y(x[1])>=mapgradient[0].length 
                    || Utility.coordinateToPixel_X(x[0])<0 || Utility.coordinateToPixel_Y(x[1])<0) {
                break;
            }
            dis=getDistance(x, this.hole);
            if(dis<r){
                System.out.println("Goal!!!");
                this.minDis=0;
                this.goal=true;
                break;
            }
            if(dis<this.minDis){
                this.minDis=dis;
                this.minCoordinate=x;
            }
            if (recording) {
                xtrac.add(x.clone());
            }
            this.stopCoordinate=x.clone();
            // bouncing with tree
            if (blueElm[Utility.coordinateToPixel_X(x[0])][Utility.coordinateToPixel_Y(x[1])]>=30 
                    && redElm[Utility.coordinateToPixel_X(x[0])][Utility.coordinateToPixel_Y(x[1])]>=30) {
                
            }

        }
        return xtrac;
    }
    /**
     * get the distance between two points
     * 
     * @param src   one of the point. Only read the src[0] and src[1] as coordination. 
     * @param des   the other point.
     * @return      the distance
     */
    public double getDistance (double[] src, double[] des){
        return Math.sqrt(Math.pow(des[0]-src[0], 2)+Math.pow(des[1]-src[1], 2));
    }

    /**
     * 
     * @return  the miminal distance throughout the whole trajectory
     */
    public double getMinDistance(){
        return this.minDis;
    }

    /**
     * 
     * @return  the nearst point
     */
    public double[] getMinCoordinate(){
        return this.minCoordinate;
    }

    /**
     * 
     * @return  whether goaled in this shot
     */
    public boolean isGoal(){
        return this.goal;
    }

    /**
     * 
     * @param x     the coordination of the ball
     * @return      the distance between the ball and the hole
     */
    public double getHoleBallDistance(double[] x){
        return Math.sqrt(Math.pow(x[0]-hole[0], 2)+Math.pow(x[1]-hole[1], 2));
    }

    /**
     * 
     * @return      the position of the ball
     */
    public double[] getHole(){
        return this.hole;
    }

    /**
     * 
     * @return      the ball stop position
     */
    public double[] getStoppoint(){
        return this.stopCoordinate;
    }

    public void findTreeCenter(int x, int y,int[][] matrix ){
        Set<Integer> xSet=new HashSet<>();
        Set<Integer> ySet=new HashSet<>();
        findTree(x, y, xSet, ySet, matrix);
        int xSum=0;
        for (Integer i : xSet) {
            xSum=xSum+i;
        }
        int ySum=0;
        for (Integer i : ySet) {
            ySum=ySum+i;
        }
        int xCenter=(int) xSum/xSet.size();
        int yCenter=(int) ySum/ySet.size();
        System.out.println(xCenter + "  "+ yCenter);
    }
    public void findTree(int x, int y, Set<Integer> xSet, Set<Integer> ySet,int[][] matrix){
        if (xSet.contains(x) && ySet.contains(y)) {
            return;
        }
        if (matrix[x][y]>20) {
            xSet.add(x);
            ySet.add(y);
            findTree(x-1, y, xSet, ySet, matrix);
            findTree(x, y-1, xSet, ySet, matrix);
            findTree(x+1, y, xSet, ySet, matrix);
            findTree(x, y+1, xSet, ySet, matrix);
        }else{
            return;
        }
    }


    
}
