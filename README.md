# Daily Sudoku Solver
Notice: this project is essentially abandoned.
It was part of an unofficial competition between me and some friends, and that competition is since over.

This project has two components: an incomplete rule-based Sudoku solver, and an extremely system-specific and imperfect bot to play the Daily Sudoku Facebook Messenger game.
I would *strongly* recommend not running the project on your computer at this stage, as it cannot be terminated, has several UX issues, and is *unbelievably* system-specific.

## Solver
### Description
The solver is composed of several reducers.
Currently, there are two reducers: a rule-based reducer that plays how a human might, and a guess-and-check reducer that is less efficient but checks every possible board configuration.
### Rule Reducer
The rule reducer cannot solve the general case of sudoku puzzles, but it can make substantial headway and is faster than the guess-and-check reducer in certain cases. It allows the user to specify a fallback solver when it can no longer make any progress.
### Guess-and-Check Reducer
The guess-and-check reducer uses recursion to check every possible board combination and sets the board to one that is valid. It allows the specification of an auxiliary reducer to (once a guess has been made) reduce the board to a simpler state.
### Current Status and TODO
- The current default and best configuration of the solver is a Rule Reducer with a Guess-and-Check Reducer fallback, which has an *additional* Rule Reducer as its auxiliary reducer.
- TODO: Modularize the different rules in the reducer  and create a multiplexing reducer
- TODO: Boost efficiency of guess-and-check reducer

## Bot
The bot runs in a fairly straightforward manner in several phases.

### Focus on Game
To focus on the game, currently the bot merely types [Command+Space] f i r e f o x [Enter].

### Scan Board
To scan the board and input the given values into our solver, we take several screenshots of the board.
(Currently, I have hard-coded the screen coordinates the board appears with my configuration of Firefox on my machine.)
Before each screenshot, we keystroke a value from 1 to 9.
Daily Sudoku highlights these numbers on a board with green circles.
We subdivide the board into its cell components and look for green pixels per screenshot.

Previously, I had tried using OCR libraries to input the board.
This was inaccurate and slow.

### Solve Puzzle
We then pass the puzzle to our solver.
The solver updates the bot as soon as it has calculated the value for a given cell.
Thus, we see the board updated in the order that the solver is solving.

The bot inputs the values passed to it by the solver by keystroking the desired value and then mouse clicking in each cell.
The mouse coordinates are system-specific, as above.

### Current Status and TODO
- Currently the bot only runs on my laptop with Firefox, however there is support for switching configurations.
- TODO: Use OpenCV or similar library to automatically configure for different browsers
- TODO: Firefox canvas rendering is the current bottleneck; testing with Chrome and Safari is in order
