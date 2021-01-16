#include "Board.hpp"
#include "bits/stdc++.h"

using namespace std;

Board::Board()
{
    fill(*board, *board + MAX_ROWS * MAX_COLS, ' ');
}

void Board::setGameEnded(bool ge)
{
    gameEnded = ge;
}

bool Board::getGameEnded()
{
    return gameEnded;
}

void Board::setPlayerWon(int p)
{
    playerWon = p;
}

int Board::getPlayerWon()
{
    return playerWon;
}

bool Board::getGameTie()
{
    return gameTie;
}

void Board::setGameTie(bool gt)
{
    gameTie = gt;
}

void Board::displayBoard()
{
    printf("+-----------------------------+\n");
    for (int i = 0; i < MAX_ROWS; i++)
    {
        printf("%c%5c%5c%5c%5c%5c%5c\n", '|', board[i][0], '|', board[i][1], '|', board[i][2], '|');
        printf("+-----------------------------+\n");
    }
}

bool Board::checkPositionValidity(int x, int y)
{
    return (x < 3 && y < 3 && board[x][y] == ' ');
}

void Board::placeSymbol(int x, int y, char symbol)
{
    board[x][y] = symbol;
}

void Board::checkWinner(char symbol)
{
    bool won = false;

    // Check rows for winning
    for (int i = 0; i < 3; ++i)
    {
        if (board[i][0] == symbol && board[i][1] == symbol &&
            board[i][2] == symbol)
            won = true;
    }

    // Check columns for winning
    for (int i = 0; i < 3; ++i)
    {
        if (board[0][i] == symbol && board[1][i] == symbol &&
            board[2][i] == symbol)
            won = true;
    }

    // Check diagonals for winning
    if (board[0][0] == symbol && board[1][1] == symbol &&
        board[2][2] == symbol)
        won = true;

    if (board[0][2] == symbol && board[1][1] == symbol &&
        board[2][0] == symbol)
        won = true;

    if (won)
    {
        if (symbol == 'X')
        {
            Board::setPlayerWon(2);
        }
        else
        {
            Board::setPlayerWon(1);
        }
        setGameEnded(true);
    }
}

void Board::checkTie()
{
    int freePlaces = 0;
    for (int i = 0; i < MAX_ROWS; i++)
    {
        for (int j = 0; j < MAX_COLS; j++)
        {
            if (board[i][j] == ' ')
                freePlaces++;
        }
    }
    Board::setGameTie(freePlaces == 0);
    Board::setGameEnded(freePlaces == 0);
}
