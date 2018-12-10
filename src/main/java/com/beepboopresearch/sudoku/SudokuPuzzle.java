package com.beepboopresearch.sudoku;

/** A Sudoku board state.
 * Contains the values on a board and a superset of the possible values per cell.
 */
public class SudokuPuzzle implements Cloneable {
	public static final int TILES = 9;

	private int[][] values;
	private boolean solved = false;
	private boolean contradictory = false;
	public boolean[][][] possible;

	public SudokuPuzzle() {
		values = new int[TILES][TILES];
		possible = new boolean[TILES][TILES][TILES];
		for (int x = 0; x < TILES; x++) {
			for (int y = 0; y < TILES; y++) {
				for (int i = 0; i < TILES; i++) {
					possible[x][y][i] = true;
				}
			}
		}
	}

	private SudokuPuzzle(SudokuPuzzle a) {
		values = new int[TILES][TILES];
		possible = new boolean[TILES][TILES][TILES];
		for (int x = 0; x < TILES; x++) {
			for (int y = 0; y < TILES; y++) {
				values[x][y] = a.values[x][y];
			}
		}
		for (int x = 0; x < TILES; x++) {
			for (int y = 0; y < TILES; y++) {
				for (int i = 0; i < TILES; i++) {
					possible[x][y][i] = a.possible[x][y][i];
				}
			}
		}
		solved = a.solved;
		contradictory = a.contradictory;
	}

	public void setValue(int x, int y, int value) {
		if (!possible[x][y][value - 1]) contradictory = true;

		values[x][y] = value;
		
		// clear row
		for (int xb = 0; xb < TILES; xb++) {
			possible[xb][y][value - 1] = false;
		}

		// clear col
		for (int yb = 0; yb < TILES; yb++) {
			possible[x][yb][value - 1] = false;
		}

		// clear block
		int xc = (x / 3) * 3;
		int yc = (y / 3) * 3;
		for (int xo = 0; xo < 3; xo++) {
			for (int yo = 0; yo < 3; yo++) {
				possible[xc + xo][yc + yo][value - 1] = false;
			}
		}
		for (int i = 0; i < TILES; i++) {
			possible[x][y][i] = value - 1 == i;
		}

		solved = true;
checkingSolved:
		for (int xb = 0; xb < TILES; xb++) {
			for (int yb = 0; yb < TILES; yb++) {
				if (values[xb][yb] == 0) {
					solved = false;
					break checkingSolved;
				}
			}
		}
	}

	public int getValue(int x, int y) {
		return values[x][y];
	}

	public boolean isSolved() {
		return solved;
	}

	/**
	 * Returns true if there is a value in a cell that should not be possible.
	 * This method returning false does not guarantee the board is solvable or non-contradicting.
	 */
	public boolean isContradictory() {
		return contradictory;
	}

	@Override
	public SudokuPuzzle clone() {
		return new SudokuPuzzle(this);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < TILES; y++) {
			for (int x = 0; x < TILES; x++) {
				sb.append("  ");
				sb.append(values[x][y] == 0 ? " " : values[x][y]);
			}
			sb.append("\n\n");
		}
		return sb.toString();
	}
}
