package com.ttt.server;

import java.util.Scanner;

public class Play {
	
	public static char [][] board = {{' ',' ',' '}, {' ',' ',' '}, {' ',' ',' '}};
	public static boolean tieGame = false;
	public static int turn = 0;
	public static boolean winner = false;
	static int playerWon;

	
	public static void  main(String[] args){	
		
		while(!winner)
	    {
	        // If it's player 1's turn
	        if(turn == 0)
	        {
	            // Check if the board has no longer any free places
	            checkTie();
	            if(tieGame) break;
	            int x,y;
	            Scanner scanner = new Scanner(System.in);
	            System.out.printf("[Player 1]: ");
	    		String Coordinates = scanner.next();
	    		
	    		 x = Character.getNumericValue(Coordinates.charAt(0));
	    		 y = Character.getNumericValue(Coordinates.charAt(1));
	            //scanf("%d%d", &x, &y);
	            while(!checkPositionValidity(x,y))
	            {
	            	System.out.printf("Wrong move, try again\n");
	            	System.out.printf("[Player 1]: ");
	            	Coordinates = scanner.next();
		    		x = Character.getNumericValue(Coordinates.charAt(0));
		    		y = Character.getNumericValue(Coordinates.charAt(1));
	            }
	            placeSymbol(x, y);
	            winner = checkWinner('O');
	            displayBoard();
	            turn = 1;
	            if(winner) break;
	        }

	        // If it's player 2's turn
	        if(turn == 1)
	        {
	            // Check if the board has no longer any free places
	            checkTie();
	            if(tieGame) break;
	            int x,y;
	            System.out.printf("[Player 2]: ");
	            Scanner scanner = new Scanner(System.in);
	            System.out.printf("[Player 1]: ");
	    		String Coordinates = scanner.next();
	    		
	    		 x = Character.getNumericValue(Coordinates.charAt(0));
	    		 y = Character.getNumericValue(Coordinates.charAt(1));
	            while(!checkPositionValidity(x, y))
	            {
	            	System.out.printf("Wrong move, try again\n");
	            	System.out.printf("[Player 2]: ");
	            	Coordinates = scanner.next();
		    		x = Character.getNumericValue(Coordinates.charAt(0));
		    		y = Character.getNumericValue(Coordinates.charAt(1));
	            }
	            placeSymbol(x, y);;
	            winner = checkWinner('X');
	            displayBoard();
	            turn = 0;
	            if(winner) break;
	        }

	    }

	    if(tieGame)
	    {
	        System.out.printf("It's a Tie.");
	    }
	    else
	    {
	        System.out.printf("Player %d has won", playerWon);
	    }		
		/*Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the coordinates of your symbol: ");
		String Coordinates = scanner.next();
		
		int x = Character.getNumericValue(Coordinates.charAt(0));
		int y = Character.getNumericValue(Coordinates.charAt(1));
		
		while (!checkPositionValidity(x, y)) {
			System.out.println("The coordinates are either taken or incorrect, please retry: ");
			Coordinates = scanner.next();
			x = Character.getNumericValue(Coordinates.charAt(0));
			y = Character.getNumericValue(Coordinates.charAt(1));
		}
		placeSymbol(x, y, symbol);
		displayBoard();
		scanner.close();*/
	}
	
	public static void displayBoard() {
		System.out.printf("+-----------------------------+\n");

	    for (int i = 0; i < 3; i++)
	    	{
	        	System.out.printf("%c%5c%5c%5c%5c%5c%5c\n", '|', board[i][0], '|', board[i][1], '|', board[i][2], '|');
	            System.out.printf("+-----------------------------+\n");
	        }
	    
	}
	
	
	public static boolean checkPositionValidity(int x, int y) {
		return (x<3 && y<3 && 	board[x][y] == ' ');
	}
	
	
	public static void placeSymbol( int x, int y) {
		if(turn == 1) board[x][y] = 'X';
		else board[x][y] = 'O';
	}
	
	public static boolean checkWinner(char symbol) {
		boolean won = false;
    	int playerWon = 0;

	    // Check rows for winning
	    for(int i = 0; i < 3; ++i)
	    {
	        if(board[i][0] == symbol && board[i][1] == symbol &&
	                board[i][2] == symbol)
	            won = true;
	    }

	    // Check columns for winning
	    for(int i = 0; i < 3; ++i)
	    {
	        if(board[0][i] == symbol && board[1][i] == symbol &&
	                board[2][i] == symbol)
	            won = true;
	    }

	    // Check diagonals for winning
	    if(board[0][0] == symbol && board[1][1] == symbol &&
	            board[2][2] == symbol)
	        won = true;

	    if(board[0][2] == symbol && board[1][1] == symbol &&
	            board[2][0] == symbol)
	        won = true;

	    
	   /* if(won)
	    {
	        if(symbol == 'X') {
	        	playerWon = 2;
	        }
	        else {
	        	playerWon = 1;
	        }
	    }*/

	    return won;		
	}
	
	public static void checkTie()
	{
	    int freePlaces = 0;
	    for(int i = 0; i < 3; i++)
	    {
	        for(int j = 0; j < 3; j++)
	        {
	            if(board[i][j] == ' ') freePlaces++;
	        }
	    }
	    tieGame = (freePlaces == 0);

	}

}
