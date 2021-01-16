#include "bits/stdc++.h"
using namespace std;

class Board
{
private:
    const static int MAX_ROWS = 3;
    const static int MAX_COLS = 3;
    char board[MAX_ROWS][MAX_COLS];
    int playerWon;
    bool gameTie;
    bool gameEnded;

public:
    Board();
    void displayBoard();
    bool checkPositionValidity(int x, int y);
    void placeSymbol(int x, int y, char symbol);
    bool checkWinner(char symbol);
    void checkTie();
    int getPlayerWon();
    void setPlayerWon(int p);
    bool getGameTie();
    void setGameTie(bool gt);
    void setGameEnded(bool ge);
    bool getGameEnded();

};