
#include <stdio.h>
#include <memory.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include "bits/stdc++.h"
#include "Board/Board.hpp"
// OpenSSL includes
#include "openssl/crypto.h"
#include "openssl/x509.h" // X.509 is a standard defining the format of public key certificates.
#include "openssl/pem.h"
#include "openssl/ssl.h"
#include "openssl/err.h"

#define CHK_NULL(x)  \
    if ((x) == NULL) \
    exit(1)
#define CHK_ERR(err, s) \
    if ((err) == -1)    \
    {                   \
        perror(s);      \
        exit(1);        \
    }
#define CHK_SSL(err)                 \
    if ((err) == -1)                 \
    {                                \
        ERR_print_errors_fp(stderr); \
        exit(2);                     \
    }

#define PORT 1059

using namespace std;

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
    char *str;

    Board *b = new Board();

    printf("Enter Player Name: ");
    cin >> playerName;

    int sock = 0, valread, serverRec, playerSend, err;

    struct sockaddr_in serv_addr;
    SSL_CTX *ctx;
    SSL *ssl;
    X509 *server_cert;
    const SSL_METHOD *meth;

    OpenSSL_add_all_algorithms();
    meth = TLS_client_method();
    SSL_load_error_strings();
    ctx = SSL_CTX_new(meth);
    CHK_NULL(ctx);

    // Creating a socket and connecting to the server
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

    // Start SSL negotiation

    ssl = SSL_new(ctx);
    CHK_NULL(ssl);
    SSL_set_fd(ssl, sock);
    err = SSL_connect(ssl);
    CHK_ERR(err, "connect");

    // Displaying the cipher
    printf("SSL Connection using %s\n", SSL_get_cipher(ssl));

    // Displaying server's certificate
    server_cert = SSL_get_peer_certificate(ssl);
    CHK_NULL(server_cert);
    printf("Server certificate:\n");

    str = X509_NAME_oneline(X509_get_subject_name(server_cert), 0, 0);
    CHK_NULL(str);
    printf("\t subject: %s\n", str);
    OPENSSL_free(str);

    str = X509_NAME_oneline(X509_get_issuer_name(server_cert), 0, 0);
    CHK_NULL(str);
    printf("\t issuer: %s\n", str);
    OPENSSL_free(str);

    X509_free(server_cert);

    // Send player name
    // int res = send(sock, playerName, strlen(playerName), 0);
    int res = SSL_write(ssl, playerName, strlen(playerName));
    if (res > 0)
    {
        printf("Request has been sent successfully!\n");
        // Receive response from server
        valread = SSL_read(ssl, buffer, sizeof(buffer) - 1);
        if (valread > 0)
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
        // serverRec = recv(sock, svMove, 4096, 0);
        serverRec = SSL_read(ssl, svMove, sizeof(svMove) - 1);
        // Checking if the server ended the connection
        if (serverRec == 0)
        {
            printf("Connection Closed from the server\n");
            exit(0);
        }
        if (serverRec > 0)
        {
            printf("[Server's Move]: %c %c\n", svMove[0], svMove[1]);
            // Update the board
            b->placeSymbol(svMove[0] - '0', svMove[1] - '0', 'X');
            b->displayBoard();
            b->checkWinner('X');
            b->checkTie();
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
            b->checkTie();
            // Check if Client has won
            // Send to server
            //playerSend = send(sock, playerMove, 4096, 0);
            playerSend = SSL_write(ssl, playerMove, strlen(playerMove));
            if (playerSend > 0)
            {
                printf("Move sent\n");
            }
            else
            {
                printf("Something went wrong! %d\n", errno);
                exit(EXIT_FAILURE);
            }
            if (b->getGameEnded())
                break;
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

    SSL_free(ssl);
    SSL_CTX_free(ctx);

    return 0;
}