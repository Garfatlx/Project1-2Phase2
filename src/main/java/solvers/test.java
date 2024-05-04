package solvers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import solvers.Aibot.AiBot;

public class test {
    
    public static void main(String[] args) {
        MapHandler map=new MapHandler();
        golfgame g=new golfgame();
        // bot bot=new bot();
        double[] x={17.0,46.0,5,-6};
        double[] a={0.06,0.10};             // a[0] is kenitic friction. a[1] is Static friction
        double dt=0.05;
        double[] hole={33.5,9.97};
        double r=0.15;

        AiBot bot=new AiBot();

        
        // set map path
        String inputPath="target/classes/createdmap.png";
        String outputPath="output/outplot.png";

        bot.golfbot(new RK4(), x, a, dt, hole,r,inputPath);
        
        // create map by function
        // map.createMap(inputPath);

        // ArrayList<double[]> xpath=g.shoot(new RK4(), x, a, dt, hole,r,inputPath);
        // System.out.println(Arrays.toString(xpath.get(0)));
        // map.plotTrajectory(inputPath, outputPath, xpath);
        // System.out.println(xpath.getLast()[0]+" "+xpath.getLast()[1]);
        // System.out.println(g.getMinDistance());


        // bot.golfbot(new RK4(), x, a, dt, hole,r,"maps/map.png");
        // System.out.println(Arrays.toString(bot.getSolution()));

        // ArrayList<double[]> xpath=g.shoot(new RK4(), bot.getSolution(), a, dt, hole,r,"maps/map.png");
        // System.out.println(xpath.size());
        // g.plotTrajectory("maps/map.png", "maps/outplot.png", xpath);
        
        // System.out.println(xpath.getLast()[0]+" "+xpath.getLast()[1]);
        // System.out.println(g.getMinDistance());
        
       
    }



    
}

