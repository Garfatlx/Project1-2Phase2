package solvers;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;


import java.awt.Color;

public class MapHandler {
    private int[][] redAry;
    private int[][] blueAry;
    private double[][][] gradient;

    /**
     * Read the map and store the gradient.
     *
     * @param mappath the dir path
     * @return the gradient array double [ ] [ ] [ ]
     */
    public void readmap(String mappath){
        int width = 20;
        int height = 20;
        BufferedImage image = null;

        try {
            File input_file = new File(mappath);
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            image = ImageIO.read(input_file);
            width=image.getWidth();
            height=image.getHeight();
            // System.out.println("map readed");
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        int[][] gAry=new int[width][height];
        redAry=new int[width][height];
        blueAry=new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j); // Get the RGB value at a specific pixel
                redAry[i][j] = (rgb >> 16) & 0xFF;   // Red component, Red is the friction, 0-255
                gAry[i][j] = (rgb >> 8) & 0xFF;    // Green component, Green is the height, 0-255. 
                blueAry[i][j] = rgb & 0xFF; 
            }
        }

        gradient=new double[width][height][2];
        for (int i = 0; i < width-1; i++) {
            for (int j = 0; j < height-1; j++) {
                for(int k=0;k<2;k++){
                    gradient[i][j][k]=(double) -(gAry[i+1-k][j+k]-gAry[i][j])/12.75; //scaled down, if (0-255)/12.75 then (0-20)
                }
            }
        }
    }
    public int[][] getRed(){
        return this.redAry;
    }
    public int[][] getBlue(){
        return this.blueAry;
    }
    public double[][][] getGradient(){
        return this.gradient;
    }
    /**
     * Plot trajectory.
     *
     * @param sourceMap  the input path
     * @param plotMap    the output path 
     * @param trajectory the ball trajectory
     */
    public void plotTrajectory(String sourceMap,String plotMap, ArrayList<double[]> trajectory, double[] hole){
        int width = 20;
        int height = 20;
        BufferedImage image = null;

        try {
            File input_file = new File(sourceMap);
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            image = ImageIO.read(input_file);
            width=image.getWidth();
            height=image.getHeight();
            System.out.println("map readed to plot");
            for (int i = 0; i < trajectory.size(); i++) {
                image.setRGB((int) Math.floor(trajectory.get(i)[0]*10), (int) Math.floor(trajectory.get(i)[1]*10), Color.RED.getRGB());
            }
            image.setRGB((int) Math.floor(hole[0]*10), (int) Math.floor(hole[1]*10), Color.BLACK.getRGB());
            
            File outputfile=new File(plotMap);
            ImageIO.write(image, "png", outputfile);

        } catch (IOException e) {
            System.out.println("Error: " + e);
        }


    }

    /**
     * Create map.
     *
     * @param desPath the output path
     */
    public void createMap(String desPath){
        int width= 500;
        int height=500;
        BufferedImage image = null;

        try {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < 500; i++) {
                for (int j = 0; j < 500; j++) {
                    Color color=new Color(0,heightFunction(i, j),0);
                    image.setRGB(i, j, color.getRGB());
                }
            }
            System.out.println("map created");
            File outputfile=new File(desPath);
            ImageIO.write(image, "png", outputfile);

        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    private int heightFunction(double x, double y){
        // translate x,y from (0,500) to (-10,10), 
        // int h=(int) (-((0.4*(0.9-Math.exp(-(Math.pow(x/10-25, 2)+Math.pow(y/10-25, 2))/8))))*120+125);
        int h=(int) ((Math.sin(x/10+y/10-50)+0.5)*10+125);


        // String func = "255 - ((0.4 * (0.9 - e^(-(((x / 50 - 5)^2 + (y / 50 - 5)^2) / 8)))) * 200 + 80)";
        // Map<String, Double> initVars = new HashMap<>();
        // initVars.put("x", x);
        // initVars.put("y", y);
        // ExpressionParser parser = new ExpressionParser(func, initVars);
        
        return h;
    }
}
