
#include <stdio.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include "bits/stdc++.h"
#include "Board/Board.hpp"
#define PORT 1059

using namespace std;

// Global Variables and Constants.
int turn = 0;
bool gameEnded = false;

int main(int argc, char const *argv[])
{
    // Check if the client has entered the server's IP.
    if (argc < 2)
    {
        printf("Usage: ./client <SERVER-IP>\n");
        exit(0);
    }
    string ip = argv[1];
    char playerName[4096] = {0};
    char svMove[4096] = {0};
    char playerMove[4096] = {0};
    char buffer[4096] = {0};
    Board *b = new Board();

    printf("Enter Player Name: ");
    cin >> playerName;

    int sock = 0, valread, serverRec, playerSend;

    struct sockaddr_in serv_addr;
    if ((sock = socket(AF_INET, SOCK_STREAM, 0)) < 0)
    {
        printf("\n Socket creation error \n");
        return -1;
    }

    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons(PORT);

    // Convert IPv4 and IPv6 addresses from text to binary form
    if (inet_pton(AF_INET, ip.c_str(), &serv_addr.sin_addr) <= 0)
    {
        printf("\nInvalid address/ Address not supported \n");
        return -1;
    }

    if (connect(sock, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0)
    {
        printf("\nConnection Failed \n");
        return -1;
    }
    // Send player name
    int res = send(sock, playerName, strlen(playerName), 0);
    if (res != -1)
    {
        printf("Request has been sent successfully!\n");
        // Receive response from server
        valread = recv(sock, buffer, 4096, 0);
        if (valread != -1)
        {
            printf("Server Said: %s\n", buffer);
        }
        else
        {
            printf("Something went wrong! %d\n", errno);
        }
    }
    else
    {
        printf("Something went wrong");
        exit(EXIT_FAILURE);
    }

    // Start playing
    // Server starts first

    while (!b->getGameEnded())
    {
        // Game ended
        if (b->getGameEnded())
            exit(0);
        printf("Waiting for the server's move\n");
        memset(&svMove, 0, sizeof(svMove));
        serverRec = recv(sock, svMove, 4096, 0);
        // Checking if the server ended the connection
        if (serverRec == 0)
        {
            printf("Connection Closed from the server\n");
            exit(0);
        }
        if (serverRec != -1)
        {
            printf("[Server's Move]: %c %c\n", svMove[0], svMove[1]);
            // Update the board
            b->placeSymbol(svMove[0] - '0', svMove[1] - '0', 'X');
            b->displayBoard();
            b->checkWinner('X');
            // Check if Server has won
            if (b->getGameEnded())
                break;
        }
        else
        {
            printf("Something went wrong! %d\n", errno);
            exit(EXIT_FAILURE);
        }
        char x, y;
        cout << "Enter Your Move: ";
        cin >> x >> y;
        playerMove[0] = x;
        playerMove[1] = y;
        // Update board
        {
            while (!b->checkPositionValidity(playerMove[0] - '0', playerMove[1] - '0'))
            {
                cout << "Invalid Move!\n";
                cout << "Enter Your Move: ";
                cin >> x >> y;
                playerMove[0] = x;
                playerMove[1] = y;
            }

            b->placeSymbol(playerMove[0] - '0', playerMove[1] - '0', 'O');
            b->displayBoard();
            b->checkWinner('O');
            // Check if Client has won
            if (b->getGameEnded())
                break;
        }
        // Send to server
        playerSend = send(sock, playerMove, 4096, 0);
        if (playerSend != -1)
        {
            printf("Move sent\n");
        }
        else
        {
            printf("Something went wrong! %d\n", errno);
            exit(EXIT_FAILURE);
        }
    }

    // Check if it's a win or a tie
    b->checkTie();
    if (b->getGameTie())
    {
        printf("It's a Tie.\n");
    }
    else
    {
        int whoWon = b->getPlayerWon();
        string whoWonStr = (whoWon == 2) ? "Server" : "Client";
        printf("%s has won!\n", whoWonStr.c_str());
    }

    return 0;
}
