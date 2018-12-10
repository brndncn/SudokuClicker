package com.beepboopresearch.sudoku.bot;

/**
 * Various browser- and system-dependent parameters needed to run the bot.
 */
public class SudokuHostContext {

	public int SCREENCAP_X0, SCREENCAP_Y0;
	public int SCREENCAP_WIDTH, SCREENCAP_HEIGHT;

	public int CLICK_X0, CLICK_Y0;
	public int CLICK_DX, CLICK_DY;

	/**
	 * The delay in milliseconds we wait to allow the OS to switch apps.
	 */
	public int SCREEN_DELAY;
	
	/**
	 * The delay in milliseconds we wait to allow the browser to accept our inputs.
	 */
	public int GAME_DELAY;

	public String BROWSER_NAME;

}
