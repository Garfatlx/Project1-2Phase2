package solvers;

import java.util.Arrays;

public class GolfBall {
    // TODO: Implement the golfball (weight etc) for later use in the solvers
    double mass = 0.0459;
    double gravitation = 9.81;

    public static void main(String[] args) {
        int[] oi={1,2};
        InnerGolfBall ig=new InnerGolfBall(oi);

        int[] hi=ig.getx().clone();
        hi[1]=10;
        System.out.println(Arrays.toString(oi));
        System.out.println(Arrays.toString(ig.getx()));
    }
}
class InnerGolfBall {
    final int[] x;
    InnerGolfBall(int[] x){
        this.x=x.clone();
    }
    
    public int[] getx(){
        return this.x;
    }
}
