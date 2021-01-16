package com.ttt.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

public class Server {
	
	public static char [][] board = {{' ',' ',' '}, {' ',' ',' '}, {' ',' ',' '}};
	public static boolean tieGame = false;
	public static int turn = 0;
	public static boolean winner = false;
	static int  playerWon = 0;


	
	public static void  main(String[] args) throws IOException{	
		
		String ClientPortNumber, ClientIpAddress;
		//server socket creation
		ServerSocket serverSock=new ServerSocket(1059);
		System.out.printf("Waiting for a remote player..\n");
		//listening client connection and accept the connection
		Socket socket=serverSock.accept();
		/*
		 * Buffering data received from client
		 */
		InputStream istream=socket.getInputStream();
		BufferedReader receiveRead=new BufferedReader(new InputStreamReader(istream));
		
		/*
		 * Streaming data to client
		 */
		OutputStream ostream=socket.getOutputStream();
        PrintWriter pwrite=new PrintWriter(ostream,true);
		
		String playerName;
		if((playerName=receiveRead.readLine())!= null){
			System.out.printf("%s has connected!\n", playerName);
		}else {
			System.out.println("Something went wrong");
		}		
		
		String ackMessage = "Hello" + playerName;
		pwrite.println(ackMessage);
		System.out.flush();
		
		
		while(!winner)
	    {
	        {
	        	
	        	//Server's moves
	            int xServer,yServer;
	            Scanner scanner = new Scanner(System.in);
	            System.out.printf("Enter your move: ");
	    		String Coordinates = scanner.next();
	    		pwrite.println(Coordinates);
	    		
	    		 xServer = Character.getNumericValue(Coordinates.charAt(0));
	    		 yServer = Character.getNumericValue(Coordinates.charAt(1));
	    		 
	            while(!checkPositionValidity(xServer, yServer))
	            {
	            	System.out.printf("Wrong move, try again\n");
	            	System.out.printf("[Player 1]: ");
	            	Coordinates = scanner.next();
		    		pwrite.println(Coordinates);
		    		xServer = Character.getNumericValue(Coordinates.charAt(0));
		    		yServer = Character.getNumericValue(Coordinates.charAt(1));
	            }
	            placeSymbol(xServer, yServer);
	            winner = checkWinner('O');
	            displayBoard();
	            if(winner) break;
	        }

	        
	        //Client's moves
	            int xClient,yClient;
	            receiveRead.readLine();
	            System.out.printf("[Player 2]: ");
	    		String coordinates = receiveRead.readLine();
	    		if((coordinates=receiveRead.readLine())!= null){
	    			System.out.printf("Player's moves are: %s %s!\n", coordinates.charAt(0), coordinates.charAt(1));
	    		}else {
	    			System.out.println("Something went wrong");
	    		}		
	    		 xClient = Character.getNumericValue(coordinates.charAt(0));
	    		 yClient = Character.getNumericValue(coordinates.charAt(1));
	  
	            placeSymbol(xClient, yClient);
	            winner = checkWinner('X');
	            displayBoard();
	            
	            if(winner) break;
	    }

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
	
	
	public static void placeSymbol( int x, int y) {
		if(turn == 1) board[x][y] = 'X';
		else board[x][y] = 'O';
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
	        	playerWon = 2;
	        }
	        else {
	        	playerWon = 1;
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
