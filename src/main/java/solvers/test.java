import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class test {
    
    public static void main(String[] args) {

        golfgame g=new golfgame();
        double[] x={17.0,46.0,1.85,-2.8};
        double[] a={0.1,0.15};
        double dt=0.1;

        ArrayList<Double>[] xpath=g.trajectory(new RK4(), x, a, dt,"maps/map.png");
        g.plotTrajectory("maps/map.png", "maps/outplot.png", xpath);
        System.out.println(xpath[0].getLast()+" "+xpath[1].getLast());
        
       
    }



    
}

