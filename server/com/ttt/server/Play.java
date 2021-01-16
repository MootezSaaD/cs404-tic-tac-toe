package com.ttt.server;

import java.util.Scanner;

public class Play {
	
	public static char [][] board = {{' ',' ',' '}, {' ',' ',' '}, {' ',' ',' '}};
	public static char symbol = 'X';
	
	public static void main(String[] args){	
		
		Scanner scanner = new Scanner(System.in);
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
		scanner.close();
	}
	
	
	
	public static boolean checkPositionValidity(int x, int y) {
		return (x<3 && y<3 && 	board[x][y] == ' ');
	}
	
	
	public static void placeSymbol( int x, int y, char symbol) {
		System.out.println(x);
		board[x][y] = symbol;
	}
	
	public static void displayBoard() {
		System.out.printf("+-----------------------------+\n");

	    for (int i = 0; i < 3; i++)
	    	{
	        	System.out.printf("%c%5c%5c%5c%5c%5c%5c\n", '|', board[i][0], '|', board[i][1], '|', board[i][2], '|');
	            System.out.printf("+-----------------------------+\n");
	        }
	    
	}
}
