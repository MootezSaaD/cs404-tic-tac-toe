package com.ttt.client;

public class Symbol {
	
	public static char symbol;
	
	public static char chooseSymbol(char choice) {
		if(choice == 'X') {
			symbol = 'X';
		}
		else if(choice == 'O') {
			symbol = 'O';
		}else {
			symbol = ' ';
		}
		return symbol;
	}
	
	public static void 	placeSymbol(char [][] gameBoard, int position, char symbol) {
	
	switch(position) {
	case 1:
		gameBoard[0][0] = symbol;
		break;
	case 2:
		gameBoard[0][2] = symbol;
		break;
	case 3:
		gameBoard[0][4] = symbol;
		break;	
	case 4:
		gameBoard[2][0] = symbol;
		break;
	case 5:
		gameBoard[2][2] = symbol;
		break;
	case 6:
		gameBoard[2][4] = symbol;
		break;
	case 7:
		gameBoard[4][0] = symbol;
		break;
	case 8:
		gameBoard[4][2] = symbol;
		break;
	case 9:
		gameBoard[4][4] = symbol;
		break;
	default:
		break;
	}
}
}
