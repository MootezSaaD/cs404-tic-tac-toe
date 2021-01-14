package com.ttt.client;

import java.util.Scanner;

public class Play {

	public char symbol = ' ';
	
	public static void main(String[] args) {
	
		char [][] gameBoard = {{' ', '|', ' ', '|', ' '},
				{'-', '-', '-', '-', '-'},
				{' ', '|', ' ', '|', ' '},
				{'-', '-', '-', '-', '-'},
				{' ', '|', ' ', '|', ' '}};
		
		/*
		 * Choice of the Symbol[X or O]
		 */
		Scanner scanner = new Scanner(System.in);
		System.out.println("choose your symbol X or O: ");
		char choice = scanner.nextLine().charAt(0);
		char chosenSymbol = Symbol.chooseSymbol(choice);
		
		/*
		 * Choice of the position of the Symbol[1-9]
		 */
		Scanner scanner1 = new Scanner(System.in);
		System.out.println("Place your symbol: ");
		int position = scanner1.nextInt();
		Symbol.placeSymbol(gameBoard, position, chosenSymbol);
		//Check whether the player enters a valid postion or not
		Printer.printGameBoard(gameBoard);
		
		
		scanner.close();
		scanner1.close();
	}
}
