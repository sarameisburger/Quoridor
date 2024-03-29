package edu.up.cs301.quoridor;

//import cs301.sqaurespuzzle.Board;
//import cs301.sqaurespuzzle.MainActivity;
import android.R.string;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.animation.Animator;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.ProxyGame;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;

/**
 * A GUI that allows a human to play tic-tac-toe. Moves are made by clicking
 * regions on a canvas
 * 
 * @author Steven R. Vegdahl
 * @version July 2013
 */
public class QDHumanPlayer1 extends QDHumanPlayer implements Animator {

	// some constants, which are percentages with respect to the minimum
	// of the height and the width. All drawing will be done in the "middle
	// square".
	//
	// The divisions both horizontally and vertically within the
	// playing square are:
	// - first square starts at 5% and goes to 33%
	// - second square starts at 36% and goes to 64%
	// - third square starts at 67& and goes to 95%
	// There is therefore a 5% border around the edges; each square
	// is 28% high/wide, and the lines between squares are 3%
	private final static float BORDER_PERCENT = 5; // size of the border
	private final static float SQUARE_SIZE_PERCENT = 28; // size of each of our 9 squares
	private final static float LINE_WIDTH_PERCENT = 3; // width of a tic-tac-toe line
	private final static float SQUARE_DELTA_PERCENT = SQUARE_SIZE_PERCENT
			+ LINE_WIDTH_PERCENT; // distance from left (or top) edge of square to the next one

	//QUORIDOR BOARD VARIABLE
	public static int boardSize = 9; //get board size from main class input HARD CODE
	private int margin;//create a margin to be between each piece
	private int totalBoardSize = 650; //size of the board
	private int pieceLength; //total board size divided by number of pieces
	private int pieceSize; //size of each piece of the  board
	//public static Board model; //get the static board from the main class
	private Paint paint = new Paint(); //create new paint object
	private int OPAQUE; //create an invisible color
	private int pawnSize; //size of pawns
	private boolean wallMode; //whether or not we are in wall mode 
	private int wallOri = QDState.HORIZONTAL; //orientation of wall --dependent on wall mode 

	// the game's state
	protected QDState state;
	private QDPoint[] pawns;
	private int[] wallsRemain;
	private QDPoint[] legalPawnMoves;
	private int[][] wallInter = new int[8][8];
	//private int[][] wallInter;
	private int p;

	//Colors
	int wallBrown, highlight;

	//graphics variables
	int shift;  //correctional shift to center pieces
	int wallStartX;  //starting x of all wall stacks
	int wallStartY; //starting y position of first stack, other stacks y based off this value
	int wallWidth; //width of a single wall
	String text0, text1, text2, text3;
	/////////////////////////////////////////////////////////////////////////////

	/*
	 * Instance variables
	 */



	// the current activity
	private Activity myActivity;

	// the offset from the left and top to the beginning of our "middle square"; one
	// of these will always be zero
	protected float hBase;
	protected float vBase;

	// the size of one edge of our "middle square", or -1 if we have not determined
	// size
	protected float fullSquare;

	// our animation surface. (We're not actually doing moving animation, but this
	// surface is a convenient way to draw our image.)
	private AnimationSurface surface;

	/**
	 * constructor
	 * 
	 * @param name
	 * 		the player's name
	 */
	public QDHumanPlayer1(String name) {
		super(name);
	}

	/**
	 * Callback method, called when player gets a message
	 * 
	 * @param info
	 * 		the message
	 */
	@Override
	public void receiveInfo(GameInfo info) {
		Log.w("human player", "info is received at all");
		if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo) {
			// if the move was out of turn or otherwise illegal, flash the screen
			surface.flash(Color.RED, 50);
		}
		else if (!(info instanceof QDState))
			// if we do not have a TTTState, ignore
			return;
		else {
			// update our 'state' variable with the new state
			this.state = (QDState) info;
			pawns = state.getPawns();
			wallsRemain = state.getWallsRem();
			p = state.getWhoseMove();
			legalPawnMoves = state.legalPawnMoves(p);
			wallInter = state.getIntersections();
			//int[][] wallInter = state.getWallsLoc();
			Log.i("human player", "receiving");
		}
	}

	/**
	 * sets the current player as the activity's GUI
	 */
	public void setAsGui(GameMainActivity activity) {

		// remember our activitiy
		myActivity = activity;

		// Load the layout resource for the new configuration
		activity.setContentView(R.layout.ttt_human_player1and2);

		// set the animator (us) for the animation surface
		surface = (AnimationSurface) myActivity
				.findViewById(R.id.padding8Bot);
		surface.setAnimator(this);

		// "erase" the previous GUI's notion of what the screen size is
		fullSquare = -1;

		// if we have state, "simulate" that it just came from the game, and
		// hence draw it on the GUI
		if (state != null) {
			receiveInfo(state);
		}
	}

	/**
	 * returns the GUI's top view
	 * 
	 * @return
	 * 		the GUI's top view
	 */
	@Override
	public View getTopView() {
		return myActivity.findViewById(R.id.top_gui_layout);
	}

	/** 
	 * @return
	 * 		the time interval for the animation
	 */
	public int interval() {
		// 50 milliseconds, or 20 times per second
		return 50;
	}

	/**
	 * @return
	 * 		the animation's background color
	 */
	public int backgroundColor() {
		return Color.WHITE;
	}

	/**
	 * @return
	 * 		whether the animation should be pause
	 */
	public boolean doPause() {
		// never tell the animation to pause
		return false;
	}

	/**
	 * @return
	 * 		whether the animation should quit
	 */
	public boolean doQuit() {
		// never tell the animation to quit
		return false;
	}

	/**
	 * perform any initialization that needs to be done after the player
	 * knows what their game-position and opponents' names are.
	 */
	protected void initAfterReady() {
		myActivity.setTitle("Quoridor: "+allPlayerNames[0]+" vs. "+allPlayerNames[1]);
	}

	/**
	 * callback method, called whenever it's time to draw the next animation
	 * frame
	 * 
	 * @param g
	 * 		the canvas to draw on
	 */
	public void tick(Canvas g) {

		//QUORIDOR BOARD
		//calculating variables
		margin = 40/boardSize; //creating the margin based on board size
		pieceLength = (int)(totalBoardSize/boardSize); //creating piece length based off board size
		pieceSize = pieceLength+ margin; //create the total piece size
		pawnSize = (int)(pieceLength/3); //diameter of pawn
		shift = (int)(pieceSize/2); //correctional shift to center pieces
		wallStartX = 700; //starting x of all wall stacks
		wallStartY = 100; //starting y position of first stack, other stacks y based off this value
		wallWidth = margin*3;

		int i, j; //iterators

		for (i=0;i<boardSize;i++){
			for (j=0; j<boardSize; j++){
				//initialize board 
				paint.setColor(Color.GRAY);
				g.drawRect(margin+(j*pieceSize), margin+(i*pieceSize), pieceSize+(j*pieceSize), pieceSize+(i*pieceSize), paint);
			}

		}
		//draw pawns on board
//		if (pawns == null) {
//			Log.w("state null", "yes, yes it is");
//		}
		if (pawns != null){

			//if there are two players, only draw two pawns
			if (pawns.length == 2){
				paint.setColor(Color.RED);

				g.drawCircle(pawns[0].x*(pieceSize)+shift, pawns[0].y*(pieceSize)+shift, pawnSize, paint);//bottom middle

				paint.setColor(Color.YELLOW);
				g.drawCircle(pawns[1].x*(pieceSize)+shift, pawns[1].y*(pieceSize)+shift, pawnSize, paint); //top piece

			}

			//if there are four players, draw four pawns
			else if(pawns.length == 4){
				paint.setColor(Color.RED);
				g.drawCircle(pawns[0].x*(pieceSize)+shift, pawns[0].y*(pieceSize)+shift, pawnSize, paint);//bottom middle

				paint.setColor(Color.YELLOW);
				g.drawCircle(pawns[1].x*(pieceSize)+shift, pawns[1].y+shift, pawnSize, paint); //up

				paint.setColor(Color.GREEN);

				g.drawCircle(pawns[2].x+shift, pawns[2].y*(pieceSize)+shift, pawnSize, paint);//right side player

				paint.setColor(Color.BLUE);
				g.drawCircle(pawns[3].x*(pieceSize)+shift, pawns[3].y*(pieceSize)+shift, pawnSize, paint);//left

			}
		}

		//draw stacks of walls with number of walls left on top
		if(wallsRemain != null){

			if(pawns.length == 2){
				
				if(wallOri == QDState.HORIZONTAL){
					if (wallMode){
				paint.setColor(highlight);
				g.drawRect(wallStartX-margin, wallStartY-margin, (pieceSize*2)+wallStartX+margin, wallStartY+wallWidth+margin, paint);
				g.drawRect(wallStartX-margin, 2*wallStartY-margin, (pieceSize*2)+wallStartX+margin, 2*wallStartY+wallWidth+margin, paint);
					}
				paint.setTextSize(50); //set font size

				wallBrown = Color.rgb(153, 76, 0);//make walls brown
				paint.setColor(wallBrown);//set color to brown

				//draw 2 stacks of walls
				
				g.drawRect(wallStartX, wallStartY, (pieceSize*2)+wallStartX, wallStartY+wallWidth, paint);
				g.drawRect(wallStartX, 2*wallStartY, (pieceSize*2)+wallStartX, 2*wallStartY+wallWidth, paint);

				//get each players number of remaining walls	
				text0 = String.valueOf(wallsRemain[0]);
				text1 = String.valueOf(wallsRemain[1]);

				//draw value of walls remaining for each player
				paint.setColor(Color.BLACK);
				g.drawText(text0, wallStartX, wallStartY, paint);
				g.drawText(text1, wallStartX, 2*wallStartY, paint);

				//write out player name in their pawn color
				paint.setColor(Color.RED);
				g.drawText(allPlayerNames[0], wallStartX+pieceSize, wallStartY, paint);

				paint.setColor(Color.YELLOW);
				g.drawText(allPlayerNames[1], wallStartX+pieceSize, 2*wallStartY, paint);
				
				}
				else{
					if(wallMode){
						paint.setColor(highlight);
						g.drawRect(wallStartX-margin, wallStartY-margin, wallStartX+wallWidth+margin,(pieceSize*2)+wallStartY+margin, paint);
						g.drawRect(wallStartX-margin, 3*wallStartY-margin, wallStartX+wallWidth+margin, (pieceSize*2)+3*wallStartY+margin, paint);
					}
					paint.setTextSize(50); //set font size

					wallBrown = Color.rgb(153, 76, 0);//make walls brown
					paint.setColor(wallBrown);//set color to brown

					//draw 2 stacks of walls
					g.drawRect(wallStartX, wallStartY, wallStartX+wallWidth,(pieceSize*2)+wallStartY, paint);
					g.drawRect(wallStartX, 3*wallStartY, wallStartX+wallWidth, (pieceSize*2)+3*wallStartY, paint);

					//get each players number of remaining walls	
					text0 = String.valueOf(wallsRemain[0]);
					text1 = String.valueOf(wallsRemain[1]);

					//draw value of walls remaining for each player
					paint.setColor(Color.BLACK);
					g.drawText(text0, wallStartX+margin, wallStartY+pieceSize, paint);
					g.drawText(text1, wallStartX+margin, 3*wallStartY+pieceSize, paint);

					//write out player name in their pawn color
					paint.setColor(Color.RED);
					g.drawText(allPlayerNames[0], wallStartX+pieceSize+margin, wallStartY+pieceSize, paint);

					paint.setColor(Color.YELLOW);
					g.drawText(allPlayerNames[1], wallStartX+pieceSize+margin, 3*wallStartY+pieceSize, paint);
					}
			
				}
				
			
			else if(pawns.length == 4){

				paint.setTextSize(50); 

				wallBrown = Color.rgb(153, 76, 0);//make walls brown
				paint.setColor(wallBrown);//set color to brown

				//draw 4 stacks of walls
				g.drawRect(wallStartX, wallStartY, (pieceSize*2)+wallStartX, wallStartY+wallWidth, paint);
				g.drawRect(wallStartX, 2*wallStartY, (pieceSize*2)+wallStartX, 2*wallStartY+wallWidth, paint);
				g.drawRect(wallStartX, 3*wallStartY, (pieceSize*2)+wallStartX, 3*wallStartY+wallWidth, paint);
				g.drawRect(wallStartX, 4*wallStartY, (pieceSize*2)+wallStartX, 4*wallStartY+wallWidth, paint);

				//get each players number of remaining walls	
				text0 = String.valueOf(wallsRemain[0]);
				text1 = String.valueOf(wallsRemain[1]);
				text2 = String.valueOf(wallsRemain[2]);
				text3 = String.valueOf(wallsRemain[3]);

				//draw value of walls remaining for each player
				paint.setColor(Color.BLACK);
				g.drawText(text0, wallStartX, wallStartY, paint);
				g.drawText(text1, wallStartX, 2*wallStartY, paint);
				g.drawText(text2, wallStartX, 3*wallStartY, paint);
				g.drawText(text3, wallStartX, 4*wallStartY, paint);

				//write out player name in their pawn color
				paint.setColor(Color.RED);
				g.drawText(allPlayerNames[0], wallStartX+pieceSize, wallStartY, paint);

				paint.setColor(Color.YELLOW);
				g.drawText(allPlayerNames[1], wallStartX+pieceSize, 2*wallStartY, paint);

				paint.setColor(Color.GREEN);
				g.drawText(allPlayerNames[2], wallStartX+pieceSize, 3*wallStartY, paint);

				paint.setColor(Color.BLUE);
				g.drawText(allPlayerNames[3], wallStartX+pieceSize, 4*wallStartY, paint);
			}
		}

			//highlight valid moves
			highlight = Color.argb(100,243,249,69);
			//highlight = Color.argb(100, 2, 170, 200);

			if (legalPawnMoves != null){
				for (int k = 0; k < legalPawnMoves.length; k++){

					paint.setColor(highlight);//set to highlight color

					g.drawRect(margin+(legalPawnMoves[k].x*pieceSize), margin+(legalPawnMoves[k].y*pieceSize), pieceSize+(legalPawnMoves[k].x*pieceSize), pieceSize+(legalPawnMoves[k].y*pieceSize), paint);
				}
				///////////WALL TEST/////////////////
				paint.setColor(wallBrown);
				int d = 5;
				int e = 4;
				int y = 6;
				int h = 6;
				//g.drawRect((d+1)*pieceSize-margin, ((e)*pieceSize)+(margin), (d+1)*pieceSize+2*margin, ((e+2)*pieceSize), paint);//v
				//g.drawRect(margin+((y)*pieceSize), ((h+1)*pieceSize)-(margin), ((y+2)*pieceSize), ((h+1)*pieceSize)+2*margin, paint);//h
				//draw walls on board
				//get the intersections from the state
				//wallInter[1][1]=QDState.HORIZONTAL;
				
				//place wall box
				//paint.setColor(Color.RED);
				//g.drawRect(800, 100, 900, 200, paint);
				//g.drawText("Place Wall", 800, 200, paint);

				if (wallInter != null){	

					for(int r = 0; r < wallInter.length; r++)
					{
						for(int c = 0; c < wallInter[r].length; c++)
						{

							if (wallInter[r][c] == QDState.HORIZONTAL )
							{
								//there's a horizontal wall here!
								//draw it
								paint.setColor(wallBrown);//set color to brown

								//draw horizontal wall
								g.drawRect(margin+((c)*pieceSize), ((r+1)*pieceSize)-(margin), ((c+2)*pieceSize), ((r+1)*pieceSize)+2*margin, paint);

							} else if (wallInter[r][c] == QDState.VERTICAL)
							{
								//there's a vertical wall here!
								//draw it
								paint.setColor(wallBrown);//set color to brown

								//draw vertical wall 
								g.drawRect((c+1)*pieceSize-margin, ((r)*pieceSize)+(margin), (c+1)*pieceSize+2*margin, ((r+2)*pieceSize), paint);
							}

						}
					}
				}


			}




			// if the full square size is outdated our variables that relate
			// to the dimensions of the animation surface
			if (fullSquare < 0) {
				updateDimensions(g);
			}

			// if we don't have any state, there's nothing more to draw, so return
			if (state == null) {
				return;
			}

			// for each square that has an X or O, draw it on the appropriate
			// place on the canvas
			for (int row = 0; row < 3; row++) {
				for (int col = 0; col < 3; col++) {
					//char result = state.getPiece(row, col); // get piece
					//drawSymbol(g, result, col, row);
				}
			}
		}
	

	/**
	 * helper-method to convert from a percentage to a horizontal pixel location
	 * 
	 * @param percent
	 * 		the percentage across the drawing square
	 * @return
	 * 		the pixel location that corresponds to that percentage
	 */
	protected float h(float percent) {
		return hBase + percent * fullSquare / 100;
	}

	/**
	 * helper-method to convert from a percentage to a vertical pixel location
	 * 
	 * @param percent
	 * 		the percentage down the drawing square
	 * @return
	 * 		the pixel location that corresponds to that percentage
	 */
	protected float v(float percent) {
		return vBase + percent * fullSquare / 100;
	}

	/**
	 * update the instance variables that relate to the drawing surface
	 * 
	 * @param g
	 * 		an object that references the drawing surface
	 */
	private void updateDimensions(Canvas g) {

		// initially, set the height and width to be that of the
		// drawing surface
		int width = g.getWidth();
		int height = g.getHeight();

		// Set the "full square" size to be the minimum of the height and
		// the width. Depending on which is greater, set either the
		// horizontal or vertical base to be partway across the screen,
		// so that the "playing square" is in the middle of the screen on
		// its long dimension
		if (width > height) {
			fullSquare = height;
			vBase = 0;
			hBase = (width - height) / (float) 2.0;
		} else {
			fullSquare = width;
			hBase = 0;
			vBase = (height - width) / (float) 2.0;
		}

	}

	/**
	 * @return
	 * 		the color to paint the tic-tac-toe lines, and the X's and O's
	 */
	public int foregroundColor() {
		return Color.YELLOW;
	}

	/**
	 * callback method when the screen it touched. We're
	 * looking for a screen touch (which we'll detect on
	 * the "up" movement" onto a tic-tac-tie square
	 * 
	 * @param event
	 * 		the motion event that was detected
	 */
	public void onTouch(MotionEvent event) {

		// ignore if not an "up" event
		if (event.getAction() != MotionEvent.ACTION_UP) return;

		// get the x and y coordinates of the touch-location;
		// convert them to square coordinates (where both
		// values are in the range 0..2)
		//float x = (float) event.getX();
		//float y = (float) event.getY();
		float x = (float)event.getX();
		float y = (float) event.getY();
		mapPixelToWallStack(x, y);
		QDPoint p = mapPixelToSquare(x, y);
		QDPoint q = mapPixeltoIntersections(x,y); 

		// if the location did not map to a legal square, flash
		// the screen; otherwise, create and send an action to
		// the game
		if (p != null && !wallMode) {
			//attempt pawn move
			QDMovePawnAction action = new QDMovePawnAction(this, p.x, p.y);
			//QDMovePawnAction action = new QDMovePawnAction(this, 4, 7);
			Log.i("onTouch", "Human player sending QDMA ...");
			game.sendAction(action);
		} else if (wallMode && q != null)
		{
			//make wall move action
			//check if it's legal
			if(state.canPlaceWall(0, q.x, q.y, wallOri))
			{
				//move is legal, so we'll send the action, which we know will go through
				//reset wallMode to false for the next turn, otherwise we'll be stuck
				wallMode = false;
				game.sendAction(new QDMoveWallAction(this, q.x, q.y, wallOri));
			}
		}
		//}

	}

	// x- and y-percentage-coordinates for a polygon that displays the X's
	// first slash
	private static float[] xQDPoints1 = { 6.25f, 12.5f, 87.5f, 93.75f };
	private static float[] yQDPoints1 = { 12.5f, 6.25f, 93.75f, 87.5f };

	// x- and y-percentage-coordinates for a polygon that displays the X's
	// second slash
	private static float[] xQDPoints2 = { 87.5f, 6.25f, 93.75f, 12.5f };
	private static float[] yQDPoints2 = { 6.25f, 87.5f, 12.5f, 93.75f };

	/**
	 * Draw a symbol (X or O) on the canvas in a particular location
	 * 
	 * @param g
	 *            the graphics object on which to draw
	 * @param sym
	 *            the symbol to draw (X or O)
	 * @param col
	 *            the column number of the square on which to draw (0, 1 or 2)
	 * @param col
	 *            the row number of the square on which to draw (0, 1 or 2)
	 */
	protected void drawSymbol(Canvas g, char sym, int col, int row) {

		// compute the pixel-location
		float xLoc = col * pieceSize; // compute ...
		float yLoc = row * pieceSize; // ... location

		// set the paint color to be the foreground color
		Paint p = new Paint();
		p.setColor(foregroundColor());

		// draw either an X or O, depending on the symbol
		switch (sym) {
		case 'O':
			// 'O' found: draw it by drawing two circles: an outer one with the
			// foreground color, and an inner one with the background color
			RectF rect = new RectF(h(xLoc + 5), v(yLoc + 1), h(xLoc
					+ SQUARE_SIZE_PERCENT - 5), v(yLoc + SQUARE_SIZE_PERCENT
							- 1));
			g.drawOval(rect, p); // outside of the 'O'
			p.setColor(backgroundColor());
			rect = new RectF(h(xLoc + 6), v(yLoc + 2), h(xLoc
					+ SQUARE_SIZE_PERCENT - 8), v(yLoc + SQUARE_SIZE_PERCENT
							- 3));
			g.drawOval(rect, p); // carve out "hole"
			break;
		case 'X': // 'X' found: draw it

			// create a translation matrix to move Path to the given square on the
			// surface
			Matrix translateMatrix = new Matrix();
			translateMatrix.setTranslate(h(xLoc), v(yLoc));

			// create the Path object for the X's first slash; move and draw it
			Path pth = createPoly(xQDPoints1, yQDPoints1, fullSquare
					* SQUARE_SIZE_PERCENT / 100);
			pth.transform(translateMatrix);
			g.drawPath(pth, p);

			// create the Path object for the X's second slash; move and draw it
			pth = createPoly(xQDPoints2, yQDPoints2, fullSquare
					* SQUARE_SIZE_PERCENT / 100);
			pth.transform(translateMatrix);
			g.drawPath(pth, p);
			break;
		default:
			// if not X or O, draw nothing
			break;
		}
	}

	/**
	 * 
	 * helper-method to create a scaled polygon (Path) object from a list of points
	 * 
	 * @param xQDPoints
	 * 		list of x-coordinates, taken as percentages
	 * @param yQDPoints
	 * 		corresponding list of y-coordinates--should have the same length as xQDPoints
	 * @param scale
	 * 		factor by which to scale them
	 * @return
	 */
	private Path createPoly(float[] xQDPoints, float[] yQDPoints, float scale) {

		// in case array-lengths are different, take the minimim length, to avoid
		// array-out-of-bounds errors
		int count = Math.min(xQDPoints.length, yQDPoints.length);

		// run through the points, adding each to the Path object, scaling as we go
		Path rtnVal = new Path();
		rtnVal.moveTo(xQDPoints[0] * scale / 100, yQDPoints[0] * scale / 100);
		for (int i = 1; i < count; i++) {
			float xQDPoint = xQDPoints[i] * scale / 100;
			float yQDPoint = yQDPoints[i] * scale / 100;
			rtnVal.lineTo(xQDPoint, yQDPoint);
		}

		// close the Path into a polygon; return the object
		rtnVal.close();
		return rtnVal;
	}

	/**
	 * maps a point from the canvas' pixel coordinates to "square" coordinates
	 * 
	 * @param x
	 * 		the x pixel-coordinate
	 * @param y
	 * 		the y pixel-coordinate
	 * @return
	 *		a QDPoint whose components are in the range 0-2, indicating the
	 *		column and row of the corresponding square on the tic-tac-toe
	 * 		board, or null if the point does not correspond to a square
	 */
	//		public QDPoint mapPixelToSquare(int x, int y) {
	//
	//			// loop through each square and see if we get a "hit"; if so, return
	//			// the corresponding QDPoint in "square" coordinates
	//			for (int i = 0; i < 9; i++) {
	//				for (int j = 0; j < 9; j++) {
	//					float left = h((i * pieceLength));
	//					float right = h(pieceLength
	//							+ (i * pieceSize));
	//					float top = v((j * pieceLength));
	//					float bottom = v(pieceLength
	//							+ (j * pieceSize));
	//					if ((x > left) != (x > right) && (y > top) != (y > bottom)) {
	//						return new QDPoint(i, j);
	//					}
	//				}
	//			}
	//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	// no match: return null
	//return null;
	/**
	 * tells what square the point is in
	 * @param x
	 *         the x-coordinate of the point
	 * @param y
	 *         the y-coordinate of the point
	 * @return
	 *         number of square at that point
	 */

	public boolean mapPixelToWallStack(float x, float y){
		int width = pieceSize * boardSize - margin;

		//check if user has touched on wall stack
		if (x >= wallStartX && x <= wallStartX + (pieceSize * 5) && y >= wallStartY && y <= wallStartY + (pieceSize* 5)){
		
			if (!wallMode){
				wallMode = true;
			}
			else if (wallMode && wallOri == QDState.HORIZONTAL){
				//wallMode is already true, so now, we just switch between orientations
				//if(wallOri == QDState.HORIZONTAL) {wallOri = QDState.VERTICAL;}
				//else if (wallOri == QDState.VERTICAL) {wallOri = QDState.HORIZONTAL;}
				wallOri = QDState.VERTICAL;
				//wallMode = false;
			}
			else if (wallMode && wallOri == QDState.VERTICAL){
				wallOri = QDState.HORIZONTAL;
				wallMode = false;
			}
		}
		return wallMode;
	}

	public QDPoint mapPixeltoIntersections(float x, float y){

		int width = pieceSize * boardSize - margin; 

		//iterate through intersections
		for (int i = 0; i < boardSize ; i++ ){
			//check within x boundaries 
			if (x >= (pieceSize * (i + 1) - (3 *margin)) && x <= pieceSize * (i+1) + margin + (3 *margin)){
				for (int j = 0; j < boardSize; j++){
					//check within y boundaries 
					if (y >= (pieceSize * (j +1) - (3 *margin)) && y <= pieceSize * (j+1) + margin + (3 *margin)){
						QDPoint p = new QDPoint(i, j); 
						return p; 
					}
				}
			}
		}
		return null; 
	}

	@SuppressWarnings("null")
	public QDPoint mapPixelToSquare(float x, float y) 
	{
		//number of tiles drawn based from square width
		//int tileSides = (sides - (MainActivity.width - 1) * margin)/MainActivity.width;

		int width = pieceSize * boardSize - margin;

		//iterate through all tile spaces, see where it is located
		for(int i = 0; i < boardSize ; i++)
		{
			//Is the x within the boundaries?
			if(x > margin + i * (pieceSize) && x < (i + 1) * pieceSize)
			{
				for(int j = 0; j < boardSize ; j++)
				{
					//tile number for current space
					//int l = model.getTiles()[i][j];

					//Since is it within the x boundary, is it within the y boundary?
					if(y > margin + j * (pieceSize) && y < (j + 1) * pieceSize)
					{
						//within boundary
						//Now: Is it a valid move?
						//if(l > 0)
						//{
						//  validMove(l);
						//}
						//test by changing square to 0, which should turn it black when surface.invalidate is called
						//model.setTiles(i, j, 0);
						QDPoint p = new QDPoint(i, j);
						//p.x = i;
						//p.y = j;
						return p;
					}
				}
			}
		}
		return null;
	}
}







