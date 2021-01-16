#include "bits/stdc++.h"
using namespace std;

class Board
{
private:
    const static int MAX_ROWS = 3;
    const static int MAX_COLS = 3;
    int playerWon;
    bool gameTie;
    bool gameEnded;

public:
    char board[MAX_ROWS][MAX_COLS];
    Board();
    void displayBoard();
    bool checkPositionValidity(int x, int y);
    void placeSymbol(int x, int y, char symbol);
    void checkWinner(char symbol);
    void checkTie();
    int getPlayerWon();
    void setPlayerWon(int p);
    bool getGameTie();
    void setGameTie(bool gt);
    void setGameEnded(bool ge);
    bool getGameEnded();
};