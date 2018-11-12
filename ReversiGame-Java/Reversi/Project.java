/*
 * -Analysis-
 * First the program will out the initial board on the console along with the initial score.
 * Then the program will read the String type variable and convert it into the two int types.
 * Assume the B disc and white disc will output correctly based the rule of the game
 * After that the program will output the updated board along with score and move from player and AI.
 * If the game end the game end will display the loser. 
 * -Design-
 * First step, I need to create the board NxN with initial black disc and white disc
 * In order to do so, I create a new 2 dimentionals array then i filled with -.
 * Then intialize the board like the sample run.
 * Also check the legal move
 * Also need to create the method that let me re draw the board with updated information. 
 * Need to create player turn. So i set player B go firt and using while loop to keep him ask for the right move.
 * There are also while a loop for AI. I used random for variable x and y for AI and also check if the move is correct.
 * We also need to keep track the score. I used nested for loop and coditional expression
 * to compare the color of disc with every elements in the loop and count the total.  
 * I chose the combinations of dColumn and dRow from -1 to 1 
 * with a distance of 1 to check for all the adjacent discs. 
 * If any of the adjecent discs equal to the opposite player, 
 * then I increase the distance to check for a possible flip.
 * Finally display the loser. (reference WONGOTHEAVENGER othello game)  
*/
package javaapplication98;
import java.util.Scanner;
public class Project
{
    public static void theBoard(char[][] board, int n)/*Initialize the board*/  
    {
        for (int y = 0; y < n+2; y++) /*y as column*/
        {
            for (int x = 0; x < n+2; x++) /*x as row*/
            {
                board[y][x]= '-'; /* Fill the board with -*/
            }
        }
        /*Initialize the given disc*/ 
        board[n/2][n/2] = 'W';
        board[n/2][n/2+1] = 'B';
        board[n/2+1][n/2] = 'B';
        board[n/2+1][n/2+1] = 'W';
    }
    public static void updatedBoard(char[][] board,int n) /*Draw the board*/ 
    {
        for (int y = 1; y < n+1; y++)
        {
            for(int x = 1; x < n+1; x++)
            {
                System.out.print(board[y][x]);
            }
            System.out.println();
        }
        displayScore(board,n);
    }
    public static void displayScore(char[][]board, int n) /*Calculate and print the score for two players*/ 
    {
        int scoreB = 0;/*set initial score for black to 0*/ 
        int scoreW = 0;/*set the initial score for white to 0*/
        /*Check the score by comparing every element in the 2D array board*/ 
        for (int y = 1; y < n+1; y++)
        {
            for (int x = 1; x < n+1; x++)
            {
                scoreB+= board[y][x] == 'B' ? 1 : 0;/*Calculate total black discs in the board*/ 
                scoreW+= board[y][x] == 'W' ? 1 : 0;/*Calculate total black discs in the board*/ 
            }
        }
        System.out.println("Score: Black: "+scoreB +", White: "+scoreW);/*print the score*/
    }                 
    static boolean doFlip(int row,int column, char[][] board, char player, boolean doFlip)
    {
        boolean isFlips = false;
        /*I chose the combinations of dColumn and dRow from -1 to 1 
        with a distance of 1 to check for all the adjacent discs. 
        If any of the adjecent discs equal to the opposite player, 
        I increase the distance to check for a possible flip.*/
        for(int dColumn = -1; dColumn < 2 ;dColumn++)
        {
            for(int dRow = -1; dRow <2 ;dRow++)
            {
                if (dColumn == 0 && dRow == 0)
                {
                    continue;
                }
                int distance = 0;
                do {distance++;}
                while(board[column + distance * dColumn][row + distance * dRow] == (player == 'B' ? 'W' : 'B'));
                if (board[column + distance * dColumn][row + distance * dRow] == player && distance > 1)
                {
                    isFlips = true;
                    if(doFlip)
                    {
                        for(int distance2 = 1; distance2 < distance; distance2++)
                        {
                            board[column + distance2 * dColumn][row + distance2 * dRow] = player;
                        }
                    }   
                }
            }
        }
        return isFlips;
    }
    public static void printResult(char[][] board, int num)
    {
        int scoreB = 0; 
        int scoreW = 0; 
        for (int y = 1; y < num+1; y++)
        {
            for (int x = 1; x < num+1; x++)
            {
                scoreB+= board[y][x] == 'B' ? 1 : 0; 
                scoreW+= board[y][x] == 'W' ? 1 : 0; 
            }
        }
        if (scoreB > scoreW)
        {
            System.out.println("No more move for White. Black won");    
        }
        else if (scoreB == scoreW)
        {
            System.out.println("Tie");   
        }    
        else
        {
            System.out.println("No more move for Black. White won");     
        } 
    }
    public void game(int menu, int num)
    {
        Scanner input = new Scanner(System.in);
        char[][]board = new char[num+2][num+2];/*Create the board by using two dimentional*/
        theBoard(board, num);/*Call the method the board to initileze every element in 2d array board*/
        char currentPlayer = 'B';/*set player black go first*/
        boolean isOver = true;
        while(isOver)
        {   
            updatedBoard(board, num);/*Print the board*/
            boolean legalMove = false;
            for(int y = 1; y < num+1; y++)
            {
                for(int x = 1; x < num+1; x++)
                {
                    legalMove = legalMove || (board[y][x] == '-' && doFlip(x, y, board, currentPlayer, false));        
                }
            }
            if(!legalMove){break;}            
             /* player B turn*/
            while (currentPlayer == 'B' && (menu == 2 || menu == 3))
            {
                System.out.print("Player B enter your move: ");
                String move = input.next(); /*Take the player input*/
                if(move.length()!= 2){continue;}/*Make sure player enter correctly*/
                int x = move.charAt(0) - '0';
                int y = move.charAt(1) - '0';
                if (board[y][x] != '-'){continue;}/*make sure the disc place correctly*/ 
                if(!doFlip(x,y,board, currentPlayer, true)){continue;}
                board[y][x] = currentPlayer;
                System.out.println("Success: White move at "+"("+x+","+y+")");
                break;
            }
            /* AI turn (player W)*/
            while(currentPlayer == 'W' && (menu == 1 || menu == 3))
            {
                int x = (int )(Math.random() * num + 1);/*Random posistion for AI*/
                int y = (int )(Math.random() * num + 1);
                if (board[y][x] != '-'){continue;} /*Check if position of AI is valid*/
                if(!doFlip(x,y,board,currentPlayer,true)){continue;}
                board[y][x] = currentPlayer;
                System.out.println("Success: White move at "+"("+x+","+y+")");
                break;
            }
            /* Player W if two player*/
            while (currentPlayer == 'W' && menu == 2 )
            {
                System.out.print("Player W enter your move: ");
                String move = input.next();
                if(move.length() < 2){continue;}
                int x = move.charAt(0) - '0';
                int y = move.charAt(1) - '0';
                if (board[y][x] != '-'){continue;}
                if(!doFlip(x,y,board, currentPlayer, true)){continue;}
                board[y][x] = currentPlayer;
                System.out.println("Success: White move at "+"("+x+","+y+")");
                break;
            }
            /* for AI vs AI*/
            while(currentPlayer == 'B' && menu == 1)
            {
                int x = (int )(Math.random() * num + 1);
                int y = (int )(Math.random() * num + 1);
                if (board[y][x] != '-'){continue;} 
                if(!doFlip(x,y,board, currentPlayer, true)){continue;}
                board[y][x] = currentPlayer;
                System.out.println("Success: Black move at "+"("+x+","+y+")");
                break;
            } 
            currentPlayer = currentPlayer == 'W' ? 'B' : 'W';
        }
        printResult(board, num);
    }
}       
