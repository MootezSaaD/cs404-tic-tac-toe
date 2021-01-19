package com.ttt.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    public static String coordinates = "";
	
	public static void  main(String[] args) throws IOException{	
				
		//server socket creation
		ServerSocket serverSock=new ServerSocket(1059);
		System.out.printf("Waiting for a remote player..\n");
		
		//listening client connection and accept the connection
		Socket socket=serverSock.accept();
		
		//Buffering data received from client
		InputStream istream=socket.getInputStream();
		BufferedReader receiveRead=new BufferedReader(new InputStreamReader(istream));
		
		//Streaming data to client
		OutputStream ostream=socket.getOutputStream();
        PrintWriter pwrite=new PrintWriter(ostream,true);
        
        //Prepare the scanner for server's input
        Scanner scanner = new Scanner(System.in);

		
        //Welcome the player
        byte[] bytes = new byte[20000];
        byte[] bytes1 = new byte[20000];
        int len;

        len = socket.getInputStream().read(bytes);
        playerName  = new String(bytes, 0, len);
		
		//Send acknowledgement to the player
		String ackMessage = "Hello " + playerName;
		pwrite.println(ackMessage);
		System.out.flush();
		
		while(!winner){
			
	        	
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
	            Coordinates = scanner.next();
		    	xServer = Character.getNumericValue(Coordinates.charAt(0));
		    	yServer = Character.getNumericValue(Coordinates.charAt(1));
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
	        int len1 = socket.getInputStream().read(bytes1);
	 	    coordinates  = new String(bytes1, 0, len1);
			if((xClient = Character.getNumericValue(coordinates.charAt(0)))!=-1 && (yClient = Character.getNumericValue(coordinates.charAt(1)))!=-1) {
				//System.out.printf("Coordinates are: %s \n", coordinates);
		 	    System.out.printf("Player's moves are: %s %s\n", coordinates.charAt(0), coordinates.charAt(1));
		    	yClient = Character.getNumericValue(coordinates.charAt(1));
		    	placeSymbol(xClient, yClient, 'O');
			    winner = checkWinner('O');
			    displayBoard();		
			}   
			
	        if(winner) break;
	        }
		scanner.close();
	    if(tieGame)
	    {
	        System.out.printf("It's a Tie.");
	    }
	    else
	    {
	        System.out.printf("Player %d has won", playerWon);
	    }	
	    pwrite.close();
		serverSock.close();
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
