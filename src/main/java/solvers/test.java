package solvers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import solvers.Aibot.AiBot;

public class test {
    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        MapHandler map=new MapHandler();
        
        double[] x={30.0,5.0,-5,-0.45};
        double[] a={0.06,0.10};             // a[0] is kenitic friction. a[1] is Static friction
        double dt=0.05;
        double[] hole={10,25};
        double r=0.2;
        
        
        // set map path
        String inputPath="target/classes/map.png";
        String outputPath="output/outplot.png";

        GolfGame g=new GolfGame(new RK4(), a, dt, hole,r,inputPath);

        AiBot bot=new AiBot(g);
        bot.golfbot(x);
        System.out.println(Arrays.toString(bot.getBest()));
        
        ArrayList<double[]> xpath=g.shoot(bot.getBest().clone(),true);
        map.plotTrajectory(inputPath, outputPath, xpath, hole);
        System.out.println(g.getMinDistance());

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

