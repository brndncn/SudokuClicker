package com.beepboopresearch.sudoku;

public abstract class SudokuReducer {
	protected SudokuPuzzleListener listener;

	public abstract void solve(SudokuPuzzle puzzle);

	public void setListener(SudokuPuzzleListener listener) {
		this.listener = listener;
	}
}
