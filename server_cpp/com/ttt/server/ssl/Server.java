package com.ttt.server.ssl;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class Server {
	
	public static char [][] board = {{' ',' ',' '}, {' ',' ',' '}, {' ',' ',' '}};
	public static boolean tieGame = false;
	public static int turn = 0;
	public static boolean winner = false;
	static String  playerWon = "";
    public static String playerName = "";
    public static String clientCoordinates = "";
    static boolean gameEnded = false;


    
    public static void  main(String[] args) throws IOException{	
		
    	 //The Port number through which this server will accept client connections
        int port = 1059;
        
        System.setProperty("javax.net.ssl.keyStore","myKeyStore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword","98305955Karim");
        System.setProperty("javax.net.debug","ssl");
        
        
        SSLServerSocketFactory sslServerSocketfactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
        SSLServerSocket sslServerSocket = (SSLServerSocket)sslServerSocketfactory.createServerSocket(port);
        System.out.println("Echo Server Started & Ready to accept Client Connection");
        SSLSocket sslSocket = (SSLSocket)sslServerSocket.accept();
		
		//Buffering data received from client
		InputStream istream=sslSocket.getInputStream();
		BufferedReader receiveRead=new BufferedReader(new InputStreamReader(istream));
		
		//Streaming data to client
		OutputStream ostream=sslSocket.getOutputStream();
        PrintWriter pwrite=new PrintWriter(ostream,true);
        
        //Prepare the scanner for server's input
        Scanner scanner = new Scanner(System.in);

		
        //Welcome the player
        byte[] bytes = new byte[4096];
        int len;

        len = sslSocket.getInputStream().read(bytes);
        playerName  = new String(bytes, 0, len);
		
		//Send acknowledgement to the player
		String ackMessage = "Hello " + playerName;
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
	            Coordinates = scanner.next();
		    	xServer = Character.getNumericValue(Coordinates.charAt(0));
		    	yServer = Character.getNumericValue(Coordinates.charAt(1));
		    	}
	        
	        placeSymbol(xServer, yServer, 'X');
	        displayBoard();	 
	        checkWinner('X');
	        checkTie();
	        //Send coordinates to the client
	    	pwrite.println(Coordinates);
	        if(gameEnded) break;
	        
        
        //Read client's moves
        int xClient,yClient;
        System.out.println("Waiting for the client's move\n");
        char c;
        len = sslSocket.getInputStream().read(bytes);
 	    clientCoordinates  = new String(bytes, 0, len);
		if((xClient = Character.getNumericValue(clientCoordinates.charAt(0)))!=-1 && (yClient = Character.getNumericValue(clientCoordinates.charAt(1)))!=-1) {
			System.out.printf("Coordinates are: %s \n", clientCoordinates);
	 	    System.out.printf("Player's moves are: %s %s\n", clientCoordinates.charAt(0), clientCoordinates.charAt(1));
	    	yClient = Character.getNumericValue(clientCoordinates.charAt(1));
	    	placeSymbol(xClient, yClient, 'O');
		    displayBoard();
		    checkWinner('O');
		    checkTie();
		    if (gameEnded) break;
		}   
		
        if(gameEnded) break;
        }
	scanner.close();
    if(tieGame)
    {
        System.out.printf("It's a Tie.");
    }
    else
    {
        System.out.printf("%s has won", playerWon);
    }	
    pwrite.close();
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
	
	public static void checkWinner(char symbol) {

	    // Check rows for winning
	    for(int i = 0; i < 3; ++i)
	    {
	        if(board[i][0] == symbol && board[i][1] == symbol &&
	                board[i][2] == symbol)
	            gameEnded = true;
	    }

	    // Check columns for winning
	    for(int i = 0; i < 3; ++i)
	    {
	        if(board[0][i] == symbol && board[1][i] == symbol &&
	                board[2][i] == symbol)
	        	gameEnded = true;
	    }

	    // Check diagonals for winning
	    if(board[0][0] == symbol && board[1][1] == symbol &&
	            board[2][2] == symbol)
	    	gameEnded = true;

	    if(board[0][2] == symbol && board[1][1] == symbol &&
	            board[2][0] == symbol)
	    	gameEnded = true;

	   if(gameEnded)
	    {
	        if(symbol == 'X') {
	        	playerWon = "Server";
	        }
	        else {
	        	playerWon = "Player";
	        }
	    }

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
	    if (gameEnded || tieGame) {
		    gameEnded = true;
	    }
	}

}
