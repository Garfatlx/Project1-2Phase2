package solvers.Aibot;

import solvers.GolfGame;
import solvers.MySolver;
import solvers.RK4;
import solvers.GolfGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class AiBot {
    int popSize=20;
    char[] vocab={'0','1'};
    double mutationRate=0.01;
    double[] solution=new double[4];
    boolean goal=false;

    GolfGame game;

    public AiBot(GolfGame game){
        this.game=game;
    }

    public void golfbot(double[] x){
        
        Individual[] population=new Individual[popSize];
        Random rand=new Random();
        HeapSort sort=new HeapSort();
        char[][] indi=new char[2][10];
        double[] x0=x.clone();

        // initiate population

        for (int i = 0; i < popSize; i++) {
            for (int j = 0; j < 2; j++) {
                for(int k=0;k<10;k++){
                    indi[j][k]=vocab[rand.nextInt(2)];
                }
            }
            population[i]=new Individual(indi);

            population[i].setFitness(calculateFitness(population[i],  x.clone()));
        }
        sort.sort(population);


        for (int i = 0; i < 700; i++) {
            int[] slcIndex=selection(population);
            crossover(population[slcIndex[0]], population[slcIndex[1]], population);
            population[popSize-1].setFitness(calculateFitness(population[popSize-1], x.clone()));
            population[popSize-2].setFitness(calculateFitness(population[popSize-2], x.clone()));
            if (this.goal) {
                break;
            }
            sort.sort(population);
            // System.out.println(population[0].getFitness()+"  "+i);
        }

        if (!this.goal) {
            double[] best=x0;
            best[2]=population[0].genoToPhenotype()[0];
            best[3]=population[0].genoToPhenotype()[1];
            this.solution=best.clone();
        }
        
    }
    
    void initialPopulation(Individual[] pop){
       
        // Random rand=new Random();
        // HeapSort sort=new HeapSort();
        // char[][] indi=new char[2][10];

        // // initiate population
        // for (int i = 0; i < popSize; i++) {
        //     for (int j = 0; j < 2; j++) {
        //         for(int k=0;k<10;k++){
        //             indi[j][k]=vocab[rand.nextInt(2)];
        //         }
        //     }
        //     pop[i]=new Individual(indi);

        //     pop[i].setFitness(calculateFitness(pop[i], solver, x.clone(), a, dt, hole, r, mappath));
        // }
        // sort.sort(population);

    }

    double calculateFitness(Individual indi, double[] x){
        
        double ball_hole_distance=game.getHoleBallDistance(x);

        x[2]=indi.genoToPhenotype()[0];
        x[3]=indi.genoToPhenotype()[1];
        double[] x0=x.clone();
        game.shoot(x);
        if (game.isGoal()) {
            this.solution=x0.clone();
            this.goal=true;
            System.out.println("Goal!!!!!!!");;
        }
        double fit=-Math.log10((game.getMinDistance()+0.01)/(ball_hole_distance+0.01));
        
        return fit;
    }

    int[] selection(Individual[] pop){
        double sum=0;
        int[] selected={0,0};
        Random rnd= new Random();
        for (int i = 0; i < pop.length; i++) {
            sum=sum+pop[i].getFitness();
        }
        double s1=rnd.nextDouble()*sum;
        double s2=rnd.nextDouble()*sum;

        for (int i = 0; i < pop.length; i++) {
            s1=s1-pop[i].getFitness();
            if (s1<=0) {
                selected[0]=i;
                break;
            }
        }
        for (int i = 0; i < pop.length; i++) {
            s2=s2-pop[i].getFitness();
            if (s2<=0) {
                selected[1]=i;
                break;
            }
        }
        // check whether duplicated choices. 
        if (selected[0]==selected[1]) {
            selected=selection(pop);
        }
        return selected;
    }

    void crossover(Individual slc1, Individual slc2, Individual[] pop){
        Random rnd=new Random();
        HeapSort sort=new HeapSort();
        int pivot=rnd.nextInt(7)+1;
        Individual child1=slc1.clone();
        Individual child2=slc2.clone();


        for (int i = pivot; i < 10; i++) {
            char temp=child1.getChromosome()[0][i];
            child1.getChromosome()[0][i]=child2.getChromosome()[0][i];
            child2.getChromosome()[0][i]=temp;

            temp=child1.getChromosome()[1][i];
            child1.getChromosome()[1][i]=child2.getChromosome()[1][i];
            child2.getChromosome()[1][i]=temp;
        }
        mutation(child1);
        mutation(child2);
        pop[popSize-1]=child1.clone();
        pop[popSize-2]=child2.clone();
    }

    void mutation(Individual indi){
        Random rnd=new Random();
        for (int i = 0; i < 10; i++) {
            int r=rnd.nextInt((int) (1/mutationRate)) ;
            if (r==0) {
                if (indi.getChromosome()[0][i]=='0') {
                    indi.getChromosome()[0][i]='1';
                }
                if (indi.getChromosome()[0][i]=='1') {
                    indi.getChromosome()[0][i]='0';
                }
            }

            r=rnd.nextInt((int) (1/mutationRate)) ;
            if (r==0) {
                if (indi.getChromosome()[1][i]=='0') {
                    indi.getChromosome()[1][i]='1';
                }
                if (indi.getChromosome()[1][i]=='1') {
                    indi.getChromosome()[1][i]='0';
                }
            }

        }
    }
    public double[] getBest(){
        return this.solution;
    }

}
