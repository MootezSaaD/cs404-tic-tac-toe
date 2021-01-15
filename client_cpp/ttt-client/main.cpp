#include "bits/stdc++.h"

using namespace std;


int MAX_ROWS = 3;
int MAX_COLS = 3;
char board[3][3];
int turn = 0;
int winner = false;
int playerWon;
bool tieGame = false;

void checkTie()
{
    int freePlaces = 0;
    for(int i = 0; i < MAX_ROWS; i++)
    {
        for(int j = 0; j < MAX_COLS; j++)
        {
            if(board[i][j] == ' ') freePlaces++;
        }
    }
    tieGame = (freePlaces == 0);

}

void display()
{

    printf("+-----------------------------+\n");

    for(int i = 0; i < MAX_ROWS; i++)
    {
        printf("%c%5c%5c%5c%5c%5c%5c\n",'|',board[i][0], '|',board[i][1], '|',board[i][2], '|');
        printf("+-----------------------------+\n");
    }

}

bool checkValid(int x, int y)
{

    return (x < 3 && y < 3 && board[x][y] == ' ');
}

void fillBoard(int x, int y)
{

    if(turn == 1) board[x][y] = 'X';
    else board[x][y] = 'O';
}

bool checkWinner(char type)
{
    bool won = false;

    // Check rows for winning
    for(int i = 0; i < MAX_ROWS; ++i)
    {
        if(board[i][0] == type && board[i][1] == type &&
                board[i][2] == type)
            won = true;
    }

    // Check columns for winning
    for(int i = 0; i < MAX_COLS; ++i)
    {
        if(board[0][i] == type && board[1][i] == type &&
                board[2][i] == type)
            won = true;
    }

    // Check diagonals for winning
    if(board[0][0] == type && board[1][1] == type &&
            board[2][2] == type)
        won = true;

    if(board[0][2] == type && board[1][1] == type &&
            board[2][0] == type)
        won = true;

    if(won)
    {
        if(type == 'X') playerWon = 2;
        else playerWon = 1;
    }

    return won;

}


int main()
{
    fill(*board, *board + 3*3, ' ');
    display();

    // Loop while there's no winner
    while(!winner)
    {
        // If it's player 1's turn
        if(turn == 0)
        {
            // Check if the board has no longer any free places
            checkTie();
            if(tieGame) break;
            int x,y;
            printf("[Player 1]: ");
            scanf("%d%d", &x, &y);
            while(!checkValid(x,y))
            {
                printf("Wrong move, try again\n");
                printf("[Player 1]: ");
                scanf("%d%d", &x, &y);
            }
            fillBoard(x,y);
            winner = checkWinner('O');
            display();
            turn = 1;
            if(winner) break;
        }

        // If it's player 2's turn
        if(turn == 1)
        {
            // Check if the board has no longer any free places
            checkTie();
            if(tieGame) break;
            int x,y;
            printf("[Player 2]: ");
            scanf("%d%d", &x, &y);
            while(!checkValid(x,y))
            {
                printf("Wrong move, try again\n");
                printf("[Player 2]: ");
                scanf("%d%d", &x, &y);
            }
            fillBoard(x,y);
            winner = checkWinner('X');
            display();
            turn = 0;
            if(winner) break;
        }

    }

    if(tieGame)
    {
        printf("It's a Tie.");
    }
    else
    {
        printf("Player %d has won", playerWon);
    }

    return 0;

}
