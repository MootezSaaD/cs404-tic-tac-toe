package com.ttt.client;

public class Printer {
	
	public static void printGameBoard(char [][] gameBoard) {
		System.out.println();
		for(char[] row : gameBoard) {
			System.out.println(row);
		}
	}
}
