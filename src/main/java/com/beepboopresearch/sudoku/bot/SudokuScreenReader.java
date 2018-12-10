package com.beepboopresearch.sudoku.bot;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.beepboopresearch.sudoku.SudokuPuzzle;

public class SudokuScreenReader {
	/**
	 * Read the sudoku puzzle on the screen.
	 * Assumes the web browser is currently focused.
	 */
	public static SudokuPuzzle getPuzzleOnScreen(SudokuHostContext context, Robot robot) {

		// take screenshot
		BufferedImage[] screenshots = new BufferedImage[SudokuPuzzle.TILES];
		for (int i = 0; i < SudokuPuzzle.TILES; i++) {
			robot.keyPress(KeyEvent.VK_1 + i);
			robot.keyRelease(KeyEvent.VK_1 + i);
			robot.delay(100);
			screenshots[i] = robot.createScreenCapture(new Rectangle(context.SCREENCAP_X0, context.SCREENCAP_Y0, context.SCREENCAP_WIDTH, context.SCREENCAP_HEIGHT));
		}

		// search for green circles
		SudokuPuzzle puzzle = new SudokuPuzzle();

		float d = (float)context.SCREENCAP_WIDTH / SudokuPuzzle.TILES;

		for (int i = 0; i < SudokuPuzzle.TILES; i++) {
			for (int x = 0; x < context.SCREENCAP_WIDTH; x++) {
				for (int y = 0; y < context.SCREENCAP_HEIGHT; y++) {
					Color c = new Color(screenshots[i].getRGB(x, y));
					if (c.getGreen() > 100 && c.getRed() == 0 && c.getBlue() == 0) {
						int xc = (int)(x / d);
						int yc = (int)(y / d);
						puzzle.setValue(xc, yc, i + 1);
					}
				}
			}
		}

		return puzzle;
	}
}
