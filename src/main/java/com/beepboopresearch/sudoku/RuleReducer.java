package com.beepboopresearch.sudoku;

/**
 * An incomplete rule-based solver/reducer.
 * This reducer the same process a human player may follow to solve a puzzle.
 * This reducer alone can solve many easy or intermediate difficulty puzzles, but does NOT work in the general case.
 */
public class RuleReducer extends SudokuReducer {
	private SudokuReducer fallback;

	public RuleReducer(SudokuReducer fallback) {
		setFallback(fallback);
	}

	@Override
	public void setListener(SudokuPuzzleListener listener) {
		super.setListener(listener);
		if (fallback != null) fallback.setListener(listener);
	}

	public void setFallback(SudokuReducer fallback) {
		this.fallback = fallback;
		if (fallback != null) fallback.setListener(this.listener);
	}

	@Override
	public void solve(SudokuPuzzle puzzle) {
		boolean changed;
		do {
			changed = false;
			// check by val
			for (int i = 0; i < SudokuPuzzle.TILES; i++) {
				// check for cell selection
				int possibilities;
				for (int xc = 0; xc < SudokuPuzzle.TILES; xc += 3) {
					for (int yc = 0; yc < SudokuPuzzle.TILES; yc += 3) {
						possibilities = 0;
						for (int xo = 0; xo < 3; xo++) {
							for (int yo = 0; yo < 3; yo++) {
								if (puzzle.possible[xc + xo][yc + yo][i]) possibilities++;
								if (puzzle.getValue(xc + xo, yc + yo) - 1 == i) possibilities = -100;
							}
						}
						if (possibilities == 1) {
							for (int xo = 0; xo < 3; xo++) {
								for (int yo = 0; yo < 3; yo++) {
									if (puzzle.possible[xc + xo][yc + yo][i]) {
										puzzle.setValue(xc + xo, yc + yo, i + 1);
										listener.valueSet(xc + xo, yc + yo, i + 1);
										changed = true;
									}
								}
							}
						}
					}
				}
				// check for row selection
				for (int x = 0; x < SudokuPuzzle.TILES; x++) {
					possibilities = 0;
					for (int y = 0; y < SudokuPuzzle.TILES; y++) {
						if (puzzle.possible[x][y][i]) possibilities++;
						if (puzzle.getValue(x, y) - 1 == i) possibilities = -100;
					}
					if (possibilities == 1) {
						for (int y = 0; y < SudokuPuzzle.TILES; y++) {
							if (puzzle.possible[x][y][i]) {
								puzzle.setValue(x, y, i + 1);
								listener.valueSet(x, y, i + 1);
								changed = true;
							}
						}
					}
				}
				// check for col selection
				for (int y = 0; y < SudokuPuzzle.TILES; y++) {
					possibilities = 0;
					for (int x = 0; x < SudokuPuzzle.TILES; x++) {
						if (puzzle.possible[x][y][i]) possibilities++;
						if (puzzle.getValue(x, y) - 1 == i) possibilities = -100;
					}
					if (possibilities == 1) {
						for (int x = 0; x < SudokuPuzzle.TILES; x++) {
							if (puzzle.possible[x][y][i]) {
								puzzle.setValue(x, y, i + 1);
								listener.valueSet(x, y, i + 1);
								changed = true;
							}
						}
					}
				}
			}
			// check for forced box values
			for (int x = 0; x < SudokuPuzzle.TILES; x++) {
				for (int y = 0; y < SudokuPuzzle.TILES; y++) {
					if (puzzle.getValue(x, y) == 0) {
						int possibilities = 0;
						for (int i = 0; i < SudokuPuzzle.TILES; i++) {
							if (puzzle.possible[x][y][i]) possibilities++;
						}
						if (possibilities == 1) {
							for (int i = 0; i < SudokuPuzzle.TILES; i++) {
								if (puzzle.possible[x][y][i]) {
									puzzle.setValue(x, y, i + 1);
									listener.valueSet(x, y, i + 1);
									changed = true;
									break;
								}
							}
						}
					}
				}
			}
			// check for extendable pseudo-cols
			for (int i = 0; i < SudokuPuzzle.TILES; i++) {
				for (int xc = 0; xc < SudokuPuzzle.TILES; xc += 3) {
					for (int yc = 0; yc < SudokuPuzzle.TILES; yc += 3) {
						int[] possibilities = new int[3];
						int colPossibilities = 0;
						for (int xo = 0; xo < 3; xo++) {
							possibilities[xo] = 0;
							for (int yo = 0; yo < 3; yo++) {
								int x = xc + xo;
								int y = yc + yo;
								if (puzzle.possible[x][y][i]) possibilities[xo]++;
							}
							if (possibilities[xo] > 0) colPossibilities++;
						}
						if (colPossibilities == 1) {
							for (int xo = 0; xo < 3; xo++) {
								int x = xc + xo;
								if (possibilities[xo] > 0) {
									for (int yb = 0; yb < SudokuPuzzle.TILES; yb++) {
										if (yb / 3 * 3 != yc) {
											if (puzzle.possible[x][yb][i]) {
												puzzle.possible[x][yb][i] = false;
												changed = true;
											}
										}
									}
								}
							}
						}
					}
				}
			}
			// check for extendable pseudo-rows
			for (int i = 0; i < SudokuPuzzle.TILES; i++) {
				for (int xc = 0; xc < SudokuPuzzle.TILES; xc += 3) {
					for (int yc = 0; yc < SudokuPuzzle.TILES; yc += 3) {
						int[] possibilities = new int[3];
						int rowPossibilities = 0;
						for (int yo = 0; yo < 3; yo++) {
							possibilities[yo] = 0;
							for (int xo = 0; xo < 3; xo++) {
								int x = xc + xo;
								int y = yc + yo;
								if (puzzle.possible[x][y][i]) possibilities[yo]++;
							}
							if (possibilities[yo] > 0) rowPossibilities++;
						}
						if (rowPossibilities == 1) {
							for (int yo = 0; yo < 3; yo++) {
								int y = yc + yo;
								if (possibilities[yo] > 0) {
									for (int xb = 0; xb < SudokuPuzzle.TILES; xb++) {
										if (xb / 3 * 3 != xc) {
											if (puzzle.possible[xb][y][i]) {
												puzzle.possible[xb][y][i] = false;
												changed = true;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} while (changed);
		if (fallback != null) {
			fallback.solve(puzzle);
		}
	}
}
