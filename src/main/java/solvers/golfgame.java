package solvers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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

    public GolfGame(MySolver solver, double[] a, double dt,double[] hole, double r, String mappath){
        this.solver=solver;
        this.a=a;
        this.dt=dt;
        this.hole=hole;
        this.r=r;
        this.mappath=mappath;
    }


    public ArrayList<double[]> shoot(double[] x,Boolean recording){
        ArrayList<double[]> xtrac=new ArrayList<double[]>();
        xtrac.clear();
        xtrac.add(x.clone());
        MapHandler map=new MapHandler();
        MyFunction golfPhysics=new golfphysics();
        
        
        //Read the map, store the gradient 
        double[][][] mapgradient=map.readmap(mappath);
        this.minDis=getDistance(x, hole);
        double dis=100;
        //loop untill ball stop or out of court
        while (!solver.nextstep(golfPhysics,x,a,mapgradient[(int)Math.floor(x[0]*10)][(int)Math.floor(x[1]*10)],dt)) {
            //check whether out of court
            if ((int)Math.floor(x[0]*10)>=mapgradient.length || (int)Math.floor(x[1]*10)>=mapgradient.length || (int)Math.floor(x[0]*10) <0 || (int)Math.floor(x[1]*10)<0) {
                break;
            }
            dis=getDistance(x, hole);
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
        }

        return xtrac;
    }

    public double getDistance (double[] src, double[] des){
        return Math.sqrt(Math.pow(des[0]-src[0], 2)+Math.pow(des[1]-src[1], 2));
    }
    public double getMinDistance(){
        return this.minDis;
    }
    public double[] getMinCoordinate(){
        return this.minCoordinate;
    }
    public boolean isGoal(){
        return this.goal;
    }
    public double getHoleBallDistance(double[] x){
        return Math.sqrt(Math.pow(x[0]-hole[0], 2)+Math.pow(x[1]-hole[1], 2));
    }
    public double[] getHole(){
        return this.hole;
    }
    public double[] getStoppoint(){
        return this.stopCoordinate;
    }

    
}
