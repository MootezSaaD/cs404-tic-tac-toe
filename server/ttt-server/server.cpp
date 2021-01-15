// Server side C/C++ program to demonstrate Socket programming
#include <unistd.h>
#include <stdio.h>
#include <sys/socket.h>
#include <stdlib.h>
#include <netinet/in.h>
#include "bits/stdc++.h"

using namespace std;

#define PORT 8080

int main(int argc, char const *argv[])
{
    int server_fd, new_socket, valread;
    struct sockaddr_in address;
    int opt = 1;
    int addrlen = sizeof(address);
    char buffer[1024] = {0};

    // Creating socket file descriptor
    if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) == 0)
    {
        perror("socket failed");
        exit(EXIT_FAILURE);
    }

    // Forcefully attaching socket to the port 8080
    if (setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR | SO_REUSEPORT,
                   &opt, sizeof(opt)))
    {
        perror("setsockopt");
        exit(EXIT_FAILURE);
    }
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons(PORT);

    // Forcefully attaching socket to the port 8080
    if (bind(server_fd, (struct sockaddr *)&address,
             sizeof(address)) < 0)
    {
        perror("bind failed");
        exit(EXIT_FAILURE);
    }
    if (listen(server_fd, 3) < 0)
    {
        perror("listen");
        exit(EXIT_FAILURE);
    }
    if ((new_socket = accept(server_fd, (struct sockaddr *)&address,
                             (socklen_t *)&addrlen)) < 0)
    {
        perror("accept");
        exit(EXIT_FAILURE);
    }

    // Waiting for a remote player to connect
    printf("Waiting for a remote player..\n");
    char player[4096] = {0};
    int playerConnected = recv(new_socket, player, 4096, 0);
    if (playerConnected > 0)
    {
        printf("%s has connected!\n", player);
    }
    else
    {
        printf("Something went wrong!\n %d", errno);
        exit(EXIT_FAILURE);
    }

    // Acknowledge client's request to play
    string ackMsg = string("Hello ").append(player);
    int sendResult = send(new_socket, ackMsg.c_str(), strlen(ackMsg.c_str()), 0);
    if (sendResult != -1)
    {
        printf("Hello message sent\n");
    }
    else
    {
        printf("Something went wrong!\n %d", errno);
        exit(EXIT_FAILURE);
    }

    // Start playing

    return 0;
}
