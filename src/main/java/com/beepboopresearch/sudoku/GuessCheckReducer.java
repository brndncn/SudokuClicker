package com.beepboopresearch.sudoku;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A brute-force trial-and-error Sudoku reducer.
 * Given the small size of a sudoku game, it runs fast enough to be feasible, but it is less poetic than the Rule-based Reducer.
 */
public class GuessCheckReducer extends SudokuReducer {
	private class IgnoreChangeListener implements SudokuPuzzleListener {
		@Override
		public void valueSet(int x, int y, int value) {
			// literally do nothing
		}
	}
	private class StackEntry {
		public int x, y, value;
		public SudokuPuzzle puzzle;

		public StackEntry(int x, int y, int value, SudokuPuzzle puzzle) {
			this.x = x;
			this.y = y;
			this.value = value;
			this.puzzle = puzzle;
		}
	}

	private SudokuReducer auxiliary;

	public GuessCheckReducer() {
	}

	public GuessCheckReducer(SudokuReducer auxiliary) {
		this.auxiliary = auxiliary;
		if (auxiliary != null) auxiliary.setListener(new IgnoreChangeListener());
	}

	public void setAuxiliary(SudokuReducer auxiliary) {
		auxiliary.setListener(new IgnoreChangeListener());
	}

	@Override
	public void solve(SudokuPuzzle puzzle) {
		SudokuPuzzle newPuzzle = puzzle.clone();
		recursiveSolve(newPuzzle);
		for (int xb = 0; xb < SudokuPuzzle.TILES; xb++) {
			for (int yb = 0; yb < SudokuPuzzle.TILES; yb++) {
				if (puzzle.getValue(xb, yb) == 0) {
					puzzle.setValue(xb, yb, newPuzzle.getValue(xb, yb));
					listener.valueSet(xb, yb, puzzle.getValue(xb, yb));
				}
			}
		}
	}

	private void recursiveSolve(SudokuPuzzle puzzle) {
		int x = 0, y = 0;

outer:
		for (; x < SudokuPuzzle.TILES; x++) {
			for (y = 0; y < SudokuPuzzle.TILES; y++) {
				if (puzzle.getValue(x, y) == 0) {
					break outer;
				}
			}
		}

		if (x >= 9 || y >= 9) return;

		for (int i = 0; i < SudokuPuzzle.TILES; i++) {
			if (puzzle.possible[x][y][i]) {
				SudokuPuzzle guessPuzzle = puzzle.clone();
				guessPuzzle.setValue(x, y, i + 1);

				if (auxiliary != null) {
					auxiliary.solve(guessPuzzle);
				}
				if (guessPuzzle.isContradictory()) continue;

				if (!guessPuzzle.isSolved()) recursiveSolve(guessPuzzle);
				if (guessPuzzle.isContradictory()) continue;

				if (guessPuzzle.isSolved()) {
					// copy into new puzzle while updating listener
					for (int xb = 0; xb < SudokuPuzzle.TILES; xb++) {
						for (int yb = 0; yb < SudokuPuzzle.TILES; yb++) {
							if (puzzle.getValue(xb, yb) == 0) {
								puzzle.setValue(xb, yb, guessPuzzle.getValue(xb, yb));
							}
						}
					}
				}
			}
		}
	}
}
