package com.ttt.server;

import java.io.IOException;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;




public class Server {
	
	public static char [][] board = {{' ',' ',' '}, {' ',' ',' '}, {' ',' ',' '}};
	public static boolean tieGame = false;
	public static int turn = 0;
	public static boolean winner = false;
	static String  playerWon = "";
    public static String playerName = "";
    public static String serverCoordinates = "";
    public static String clientCoordinates = "";
    public static boolean gameEnded = false;


    
    public static void  main(String[] args) throws IOException, InterruptedException{	
		
        int port = 1059;        
        int counter = 0;
       
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Waiting for client....");
        Socket socket = serverSocket.accept();
				
		//Streaming data to client
		OutputStream ostream=socket.getOutputStream();
        PrintWriter pwrite=new PrintWriter(ostream,true);
        
        //Prepare the scanner for server's input
        Scanner scanner = new Scanner(System.in);

		
        //Welcome the player
        byte[] bytes = new byte[4096];
        int len;

        len = socket.getInputStream().read(bytes);
        playerName  = new String(bytes, 0, len);
		
		//Send acknowledgement to the player
		String ackMessage = "Dear " + playerName + ", welcome to the Tic-Tac-Toe! ";
		pwrite.println(ackMessage);
		System.out.flush();
		
		while(true){
			
			if(gameEnded) break;
			String serverCoordinates, clientCoordinates;
			int xServer, yServer, xClient, yClient;
			
			System.out.println("Enter your move: ");
			serverCoordinates = scanner.nextLine();
			xServer = Character.getNumericValue(serverCoordinates.charAt(0));
	    	yServer = Character.getNumericValue(serverCoordinates.charAt(1));
	    	
	    	while(!checkPositionValidity(xServer, yServer)) {
	    		System.out.println("Invalid Position!");
	    		System.out.println("Re-enter your move: ");
	    		serverCoordinates = scanner.nextLine();
				xServer = Character.getNumericValue(serverCoordinates.charAt(0));
		    	yServer = Character.getNumericValue(serverCoordinates.charAt(1));
	    	}
	    	
	    	//Update Board
			placeSymbol(xServer, yServer, 'X');
			displayBoard();
			checkWinner('X');
			pwrite.println(serverCoordinates);
			if (gameEnded) break;
			
	
			char xChar,yChar;
	        byte[] bytesCoordinates = new byte[4096];

			System.out.println("Waiting for the client's move");
			len = socket.getInputStream().read(bytesCoordinates);
			xChar = (char) bytesCoordinates[0];
			yChar = (char) bytesCoordinates[1];
			
			if(xChar!= '\0' && yChar != '\0') {
				System.out.println(xChar);
				System.out.println(yChar);
				
				clientCoordinates = new String(bytesCoordinates, 0, len);
				xClient = Character.getNumericValue(xChar);
		    	yClient = Character.getNumericValue(yChar);
				
				System.out.println("Client's moves are: " + xClient + " " + yClient);
				
				//Update Board
				placeSymbol(xClient, yClient, 'O');
				displayBoard();
				checkWinner('O');
				if (gameEnded) break;	
			}
		}
		scanner.close();
		serverSocket.close();
		
		checkTie();
		if(tieGame) {
			System.out.println("It's a Tie!");
		}else {
			System.out.println(playerWon + "has won!");
		}
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
	
	
	public static void placeSymbol( int x, int y, char symbol) {
		board[x][y] = symbol;
	}
	
	public static boolean checkWinner(char symbol) {
		boolean won = false;

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

	   if(won)
	    {
	        if(symbol == 'X') {
	        	playerWon = "Server";
	        }
	        else {
	        	playerWon = "Player";
	        }
	        gameEnded = true;
	    }

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
