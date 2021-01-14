package com.ttt.client;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Play {

	static ArrayList<Integer> playerSymbolsPositions = new ArrayList<Integer>();
	static ArrayList<Integer> computerSymbolsPositions = new ArrayList<Integer>();

	
	public char symbol = ' ';
	
	public static void main(String[] args) {
	
		char [][] gameBoard = {{' ', '|', ' ', '|', ' '},
				{'-', '-', '-', '-', '-'},
				{' ', '|', ' ', '|', ' '},
				{'-', '-', '-', '-', '-'},
				{' ', '|', ' ', '|', ' '}};
		
		
		Scanner scanner = new Scanner(System.in);
		Scanner scanner1 = new Scanner(System.in);
		
		/*
		 * Choice of the Symbol[X or O]
		 */
		System.out.println("choose your symbol X or O: ");
		char playerChoice = scanner.nextLine().charAt(0);
		char chosenSymbol = Symbol.chooseSymbol(playerChoice);
		/*
		 * Computer's choice(Remaining symbol, i gave the priority to the player)
		 */
		char computerSymbol = ' ';
		if (chosenSymbol == 'X') {
			computerSymbol = 'O';
		}else if(chosenSymbol == 'O') {
			computerSymbol = 'X';
		}
		
		String resultMessage = "";
		
		while (resultMessage.equals("")) {
			/*
			 * Choice of the position of the Symbol[1-9]
			 */
			System.out.println("Place your symbol: ");
			int playerSymbolPosition = scanner1.nextInt();
			if (playerSymbolsPositions.contains(playerSymbolPosition)|| computerSymbolsPositions.contains(playerSymbolPosition)) {
				System.out.println("Position already chosen, replace your symbol: ");
				playerSymbolPosition = scanner1.nextInt();
			}
			Symbol.placeSymbol(gameBoard, playerSymbolPosition, chosenSymbol);
			playerSymbolsPositions.add(playerSymbolPosition);
			//Check whether the player enters a valid postion or not
	
			/*
			 * 
			 */
			Random random = new Random();
			int computerSymbolPosition = random.nextInt(9) + 1;
			if (playerSymbolsPositions.contains(computerSymbolPosition)|| computerSymbolsPositions.contains(computerSymbolPosition)) {
				System.out.println("Position already chosen, replace your symbol: ");
				computerSymbolPosition = random.nextInt(9) + 1;
			}
			computerSymbolsPositions.add(computerSymbolPosition);
			Symbol.placeSymbol(gameBoard, computerSymbolPosition, computerSymbol);
			Printer.printGameBoard(gameBoard);
			
			/*
			 * Check and print the winner
			 */
			resultMessage = Winner.chooseWinner(playerSymbolsPositions, computerSymbolsPositions);
			System.out.println(resultMessage);
		}
	}
}
