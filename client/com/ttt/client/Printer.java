package com.ttt.client;

public class Printer {
	
	public static char [][] gameBoard = {{' ', '|', ' ', '|', ' '},
			{'-', '-', '-', '-', '-'},
			{' ', '|', ' ', '|', ' '},
			{'-', '-', '-', '-', '-'},
			{' ', '|', ' ', '|', ' '}};
	public static void printGameBoard() {
		System.out.println();
		for(char[] row : gameBoard) {
			System.out.println(row);
		}
	}
}
