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
        boolean sent = false;
        
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
		
		while(!gameEnded){
			//Server enters his moves
	        int xServer,yServer;
	        System.out.printf("Enter your move: ");
	    	String Coordinates = scanner.next();
	    	xServer = Character.getNumericValue(Coordinates.charAt(0));
	    	yServer = Character.getNumericValue(Coordinates.charAt(1));
	    		 
	    		
	    	//Check whether the coordinates are valid or not
	        while(!checkPositionValidity(xServer, yServer)){
	            System.out.printf("Invalid Move!\n");
	            System.out.printf("Enter Your Move: ");
	            serverCoordinates = scanner.next();
		    	xServer = Character.getNumericValue(serverCoordinates.charAt(0));
		    	yServer = Character.getNumericValue(serverCoordinates.charAt(1));
		    	}
	        
	        placeSymbol(xServer, yServer, 'X');
	        winner = checkWinner('X');
	        displayBoard();
	        if(winner) break;
	        
	        //Send coordinates to the client
	    	pwrite.println(Coordinates);	
        
        //Read client's moves
        int xClient,yClient;
        System.out.println("Waiting for the client's move\n");
        char c;
        len = socket.getInputStream().read(bytes);
 	    clientCoordinates  = new String(bytes, 0, len);
		if((xClient = Character.getNumericValue(clientCoordinates.charAt(0)))!=-1 && (yClient = Character.getNumericValue(clientCoordinates.charAt(1)))!=-1) {
			System.out.printf("Coordinates are: %s \n", clientCoordinates);
	 	    System.out.printf("Player's moves are: %s %s\n", clientCoordinates.charAt(0), clientCoordinates.charAt(1));
	    	yClient = Character.getNumericValue(clientCoordinates.charAt(1));
	    	placeSymbol(xClient, yClient, 'O');
		    winner = checkWinner('O');
		    displayBoard();
		}   
		
        if(winner) break;
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
