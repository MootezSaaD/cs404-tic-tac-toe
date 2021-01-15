package com.ttt.server;


public class Play {
	
	public static void main(String[] args){	
		
		char [][] board = {{' ',' ',' '}, {' ',' ',' '}, {' ',' ',' '}};
		System.out.printf("+-----------------------------+\n");

	    for (int i = 0; i < 3; i++)
	    	{
	        	System.out.printf("%c%5c%5c%5c%5c%5c%5c\n", '|', board[i][0], '|', board[i][1], '|', board[i][2], '|');
	            System.out.printf("+-----------------------------+\n");
	        }
	}
}
