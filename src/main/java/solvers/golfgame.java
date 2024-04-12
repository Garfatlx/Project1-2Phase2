package solvers;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;

public class golfgame {
    public ArrayList<Double>[] trajectory(MySolver solver, double[] x, double[] a, double dt,String mappath){
        ArrayList<Double>[] xtrajectory=new ArrayList[2]; // use two arraylist to store the x and y coordinate for each time step.
        xtrajectory[0]=new ArrayList<Double>();
        xtrajectory[1]=new ArrayList<Double>();
        xtrajectory[0].add(x[0]);
        xtrajectory[1].add(x[1]);
        
        //Read the map, store the gradient 
        double[][][] mapgradient=readmap(mappath);

        //loop untill ball stop or out of court
        while (!solver.nextstep(new golfphysics(),x,a,mapgradient[(int)Math.floor(x[0]*10)][(int)Math.floor(x[1]*10)],dt)) {
            //check whether out of court
            if ((int)Math.floor(x[0]*10)>=mapgradient.length || (int)Math.floor(x[1]*10)>=mapgradient.length || (int)Math.floor(x[0]*10) <0 || (int)Math.floor(x[1]*10)<0) {
                break;
            }
            xtrajectory[0].add(x[0]);
            xtrajectory[1].add(x[1]);
        }

        return xtrajectory;
    }

    public double[][][] readmap(String mappath){
        int width = 20;
        int height = 20;
        BufferedImage image = null;

        try {
            File input_file = new File(mappath);
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            image = ImageIO.read(input_file);
            width=image.getWidth();
            height=image.getHeight();
            System.out.println("map readed");
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }

        int[][] gAry=new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j); // Get the RGB value at a specific pixel
                int r = (rgb >> 16) & 0xFF;   // Red component, Red is the friction, 0-255
                gAry[i][j] = (rgb >> 8) & 0xFF;    // Green component, Green is the height, 0-255. 
                int b = rgb & 0xFF; 
            }
        }

        double[][][] gradient=new double[width][height][2];
        for (int i = 0; i < width-1; i++) {
            for (int j = 0; j < height-1; j++) {
                for(int k=0;k<2;k++){
                    gradient[i][j][k]=(double) (gAry[i+1-k][j+k]-gAry[i][j])/40; //scaled down, if (0-255)/12.75 then (0-20)
                }
            }
        }
        return gradient;
        
    }

    public void plotTrajectory(String sourceMap,String plotMap, ArrayList<Double>[] trajectory){
        int width = 20;
        int height = 20;
        BufferedImage image = null;

        try {
            File input_file = new File(sourceMap);
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            image = ImageIO.read(input_file);
            width=image.getWidth();
            height=image.getHeight();
            System.out.println("map readed");
            for (int i = 0; i < trajectory[0].size(); i++) {
                image.setRGB((int) Math.floor(trajectory[0].get(i)*10), (int) Math.floor(trajectory[1].get(i)*10), Color.RED.getRGB());
            }
            File outputfile=new File(plotMap);
            ImageIO.write(image, "png", outputfile);

        } catch (IOException e) {
            System.out.println("Error: " + e);
        }


    }
}
