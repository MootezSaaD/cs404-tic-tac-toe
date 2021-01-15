#include "bits/stdc++.h"

class Board
{
private:
    static const int MAX_ROWS = 3;
    static const int MAX_COLS = 3;

public:
    char board[MAX_ROWS][MAX_COLS];
    Board();
    ~Board();
    bool checkValid(int x, int y)
    {
        return (x < 3 && y < 3 && board[x][y] == ' ');
    }
    void fillBoard(int x, int y, char type)
    {
        board[x][y] = type;
    }
    bool checkWinner(char type)
    {
        bool won = false;

        // Check rows for winning
        for (int i = 0; i < MAX_ROWS; ++i)
        {
            if (board[i][0] == type && board[i][1] == type &&
                board[i][2] == type)
                won = true;
        }

        // Check columns for winning
        for (int i = 0; i < MAX_COLS; ++i)
        {
            if (board[0][i] == type && board[1][i] == type &&
                board[2][i] == type)
                won = true;
        }

        // Check diagonals for winning
        if (board[0][0] == type && board[1][1] == type &&
            board[2][2] == type)
            won = true;

        if (board[0][2] == type && board[1][1] == type &&
            board[2][0] == type)
            won = true;

        return won;
    }
    void display()
    {

        printf("+-----------------------------+\n");

        for (int i = 0; i < MAX_ROWS; i++)
        {
            printf("%c%5c%5c%5c%5c%5c%5c\n", '|', board[i][0], '|', board[i][1], '|', board[i][2], '|');
            printf("+-----------------------------+\n");
        }
    }
};

Board::Board()
{
}

Board::~Board()
{
}
