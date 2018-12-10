package com.beepboopresearch.sudoku.bot;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;


import com.beepboopresearch.sudoku.*;

/**
 * Bot that plays Facebook Messenger Daily Sudoku.
 */
public class SudokuClicker implements SudokuPuzzleListener {

	private static final int MACOS_DELAY = 10;
	
	private Robot robot;
	private SudokuHostContext context;

	public SudokuClicker() {

		context = new SudokuHostContext();

		context.SCREENCAP_X0 = 543;
		context.SCREENCAP_Y0 = 279;
		context.SCREENCAP_WIDTH = context.SCREENCAP_HEIGHT = 419;

		context.CLICK_X0 = 566;
		context.CLICK_Y0 = 303;
		context.CLICK_DX = context.CLICK_DY = 46;

		context.SCREEN_DELAY = 200;
		context.GAME_DELAY = 50;
		context.BROWSER_NAME = "firefox";

		try {

			robot = new Robot();
			robot.setAutoDelay(MACOS_DELAY);

			summonBrowser(context, robot);

			SudokuPuzzle puzzle = SudokuScreenReader.getPuzzleOnScreen(context, robot);

			System.out.println("Input:");
			System.out.println(puzzle);
			robot.setAutoDelay(context.GAME_DELAY);

			SudokuReducer solver = new RuleReducer(new GuessCheckReducer(new RuleReducer(null)));
			solver.setListener(this);
			solver.solve(puzzle);
			System.out.println("Output:");
			System.out.println(puzzle);
			System.out.println(puzzle.isSolved() ? " Puzzle solved!" : " Puzzle could not be solved.");

		} catch (AWTException | SecurityException e) {
			System.out.println("Could not create Robot.");
		}
	}

	public void valueSet(int x, int y, int value) {
		robot.mouseMove(context.CLICK_X0 + context.CLICK_DX * x, context.CLICK_Y0 + context.CLICK_DY * y);
		robot.keyPress(KeyEvent.VK_1 + value - 1);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		robot.keyRelease(KeyEvent.VK_1 + value - 1);
	}

	private static void summonBrowser(SudokuHostContext context, Robot robot) {
		robot.keyPress(KeyEvent.VK_META);
		robot.keyPress(KeyEvent.VK_SPACE);
		robot.keyRelease(KeyEvent.VK_SPACE);
		robot.keyRelease(KeyEvent.VK_META);
		for (int i = 0; i < context.BROWSER_NAME.length(); i++) {
			int keycode = KeyEvent.getExtendedKeyCodeForChar(context.BROWSER_NAME.charAt(i));
			robot.keyPress(keycode);
			robot.keyRelease(keycode);
		}

		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		robot.delay(context.SCREEN_DELAY);
	}

	public static void main(String[] args) {
		new SudokuClicker();
	}
}
