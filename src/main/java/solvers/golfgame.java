package solvers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import java.awt.Color;

public class golfgame {
    private double minDis=100;
    private boolean goal=false;
    private double[] minCoordinate=new double[2];

    public ArrayList<double[]> shoot(MySolver solver, double[] x, double[] a, double dt,double[] hole, double r, String mappath,Boolean recording){
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

    
}
