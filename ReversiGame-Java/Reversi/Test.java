package javaapplication98;
import java.util.Scanner;

public class Test {
    public static void main (String[] args)
    {
        Project newgame = new Project();
        Scanner input = new Scanner(System.in);
        System.out.printf("Computer vs Computer press 1 | Two players press 2| Player versus Computer press 3: ");
        int menu = input.nextInt();
        System.out.printf("Enter number a row or column for NxN board: ");
        int num = input.nextInt();        
        newgame.game(menu,num);
    }
}
