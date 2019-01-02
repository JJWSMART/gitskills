/* BaseballElimination.java
 CSC 226 - Summer 2018
 Assignment 4 - Baseball Elimination Program
 
 This template includes some testing code to help verify the implementation.
 To interactively provide test inputs, run the program with
 java BaseballElimination
 
 To conveniently test the algorithm with a large input, create a text file
 containing one or more test divisions (in the format described below) and run
 the program with
 java -cp .;algs4.jar BaseballElimination file.txt (Windows)
 or
 java -cp .:algs4.jar BaseballElimination file.txt (Linux or Mac)
 where file.txt is replaced by the name of the text file.
 
 The input consists of an integer representing the number of teams in the division and then
 for each team, the team name (no whitespace), number of wins, number of losses, and a list
 of integers represnting the number of games remaining against each team (in order from the first
 team to the last). That is, the text file looks like:
 
 <number of teams in division>
 <team1_name wins losses games_vs_team1 games_vs_team2 ... games_vs_teamn>
 ...
 <teamn_name wins losses games_vs_team1 games_vs_team2 ... games_vs_teamn>
 
 
 An input file can contain an unlimited number of divisions but all team names are unique, i.e.
 no team can be in more than one division.
 
 
 R. Little - 07/13/2018
 */

import edu.princeton.cs.algs4.*;
import java.util.*;
import java.io.File;

//Do not change the name of the BaseballElimination class
//sum_inflow cannot be a global variable
public class BaseballElimination{
    
    // We use an ArrayList to keep track of the eliminated teams.
    public ArrayList<String> eliminated = new ArrayList<String>();
    int numTeam;
    int sum_inflow;
    FlowNetwork G;
    double infinity;
    int row;
    int col;
    int [][] games;
    String [] team;
    int [] win;
    int [] left;
    int id;
    String scan;
    /* BaseballElimination(s)
     Given an input stream connected to a collection of baseball division
     standings we determine for each division which teams have been eliminated
     from the playoffs. For each team in each division we create a flow network
     and determine the maxflow in that network. If the maxflow exceeds the number
     of inter-divisional games between all other teams in the division, the current
     team is eliminated.
     */
    public BaseballElimination(Scanner s){
        
        /* ... Your code here ... */
        sum_inflow = 0;
        numTeam = Integer.parseInt(s.next());
        G = new FlowNetwork(numTeam); // num of tem
        infinity  = Double.POSITIVE_INFINITY;
        
        /* 2-d array store text-file */
        id = 0;
        String scan;
        win   = new int[numTeam];
        left  = new int[numTeam];
        team  = new String[numTeam];
        games = new int [numTeam][numTeam];

        while(s.hasNextLine()){
            scan = s.nextLine();
            if(scan.length()==0)continue;
            String[] split = scan.trim().split("\\s+");
            team[id] = split[0];
            win[id]  = Integer.parseInt(split[1]);
            left[id] = Integer.parseInt(split[2]);
            for(int j = 3; j< split.length; j++){
                games[id][j-3] = Integer.parseInt(split[j]);
            }
            id++;
        }
        teamEliminated();
    }
    /*call for each team*/
    public void teamEliminated(){
        for(int i = 0; i<numTeam; i++){
            if(isEliminated(i)){
                eliminated.add(team[i]);
            }
        }
    }
    
    public boolean isEliminated(int id){
        
        sum_inflow = 0;
        //simple check
        int total = win[id]+left[id];
        for(int i = 0; i < numTeam; i++){
            //total less than num of team's win: eliminated
            if(total < win[i]){
                return true;
            }
        }
        //complex check
        FordFulkerson maxflow = maxflow(id);
        
        if((int)maxflow.value() < sum_inflow)
            return true;
        else
            return false;
    }
    
   
    private FordFulkerson maxflow(int id){
        
        System.out.println("Flow Network for " + team[id]);        
        int numMatch = ((numTeam-1)*(numTeam-2))/2;
        int V = numMatch + numTeam + 1;
        int s = 0;
        int t = V-1;

        /*construct the graph*/
        FlowNetwork G = new FlowNetwork(V);
        
        /*track the order*/
        int Mrank = 1;
        int track = 1;
        int [] array = new int[numTeam];
        for(int i = 0; i < numTeam; i++){
            if(i!= id){
                array[i] = track+numMatch;
                track++;
            }
        }
        
        for(int i = 0; i < numTeam; i++){
            for(int j=i+1; j < numTeam; j++){
                //exclude test team
                if(i != id && j != id){
                    sum_inflow += games[i][j];
                    FlowEdge edge1 = new FlowEdge(s, Mrank, games[i][j]);
                    FlowEdge edge2 = new FlowEdge(Mrank,array[i],infinity);
                    FlowEdge edge3 = new FlowEdge(Mrank,array[j],infinity);
                    G.addEdge(edge1);
                    G.addEdge(edge2);
                    G.addEdge(edge3);
                    Mrank++;
                }
            }
        }

        /* add team to t */
        int tot = win[id]+left[id];
        for(int i = 0; i < numTeam; i++){
            if(i!=id){
                FlowEdge edge = new FlowEdge(array[i],t,(tot-win[i]));
                System.out.println(edge);
                G.addEdge(edge);
            }
        }
        
        // System.out.println("The original graph :");
        // System.out.println(G);
        
        FordFulkerson maxflow = new FordFulkerson(G,s,t); //cal maxflow
        // System.out.println("maxflow.value :" + maxflow.value());
        // System.out.println("The final graph :");
        System.out.println(G);
        return maxflow;
    }
    
    /* main()
     Contains code to test the BaseballElimantion function. You may modify the
     testing code if needed, but nothing in this function will be considered
     during marking, and the testing process used for marking will not
     execute any of the code below.
     */
    public static void main(String[] args){
        
        Scanner s;
       
        if (args.length > 0){
            try{
                s = new Scanner(new File(args[0]));
            } catch(java.io.FileNotFoundException e){
                System.out.printf("Unable to open %s\n",args[0]);
                return;
            }
            System.out.printf("Reading input values from %s.\n",args[0]);
        }else{
            s = new Scanner(System.in);
            System.out.printf("Reading input values from stdin.\n");
        }
        
        BaseballElimination be = new BaseballElimination(s);
        
        if (be.eliminated.size() == 0)
            System.out.println("No teams have been eliminated.");
        else
            System.out.println("Teams eliminated: " + be.eliminated);
    }
}


