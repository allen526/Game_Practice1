package allen.game_practice1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.EventLog;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static android.graphics.Color.rgb;

/**
 * Created by Allen on 2017-11-09.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
    private MainThread thread;
    private Rect r = new Rect();

    private RectPlayer player;

    /*-------- testing new controls ----------*/
    private Controls left;
    private Controls right;
    private Controls up;
    private Controls down;

    private Point leftPoint;
    private Point rightPoint;
    private Point upPoint;
    private Point downPoint;
    /*-------- testing new controls ----------*/

    private Point playerPoint;
    private ObstacleManager obstacleManager;

    private boolean movingPlayer = false;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean movingUp = false;
    private boolean movingDown = false;

    private boolean gameOver = false;

    private long gameOverTime;

    public GamePanel(Context context) {
        super (context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(),this);

        player = new RectPlayer(new Rect(100,100,200,200), rgb(255,0,0));
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        /*----------- testing new controls --------------*/
        left = new Controls(new Rect(100, 100, 200, 200), rgb(0,255,0));
        right = new Controls(new Rect(100,100,200,200), Color.BLUE);
        down = new Controls(new Rect(100,100,200,200), Color.RED);
        up = new Controls(new Rect(100,100,200,200), Color.RED);

        leftPoint = new Point(Constants.SCREEN_WIDTH - 200, 4*Constants.SCREEN_HEIGHT/5);
        rightPoint = new Point (Constants.SCREEN_WIDTH - 75, 4*Constants.SCREEN_HEIGHT/5);
        downPoint = new Point (Constants.SCREEN_WIDTH - 100, 4*Constants.SCREEN_HEIGHT/5 + 175);
        upPoint = new Point (Constants.SCREEN_WIDTH - 100, 4*Constants.SCREEN_HEIGHT/5 - 75);

        left.update(leftPoint);
        right.update(rightPoint);
        down.update(downPoint);
        up.update(upPoint);
        /*----------- testing new controls --------------*/


        obstacleManager = new ObstacleManager(200, 350, 75, Color.BLACK);

        setFocusable(true);
    }

    public void reset() {
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(200, 350, 75, Color.BLACK);
        movingPlayer = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(true) {
            try{
                thread.setRunning(false);
                thread.join();
            } catch(Exception e) {e.printStackTrace();}
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /*if (!gameOver && player.getRectangle().contains((int) event.getX(), (int) event.getY()))
                    movingPlayer = true;*/

                /*----------------- testing new controls; left first ---------------------------------*/
                if(!gameOver && left.getRectangle().contains((int)event.getX(), (int)event.getY())) {
                    movingLeft = true;
                    movingPlayer = true;
                    playerPoint.set(playerPoint.x + 1, playerPoint.y);
                }
                /*----------------- testing new controls; left first ---------------------------------*/

                if (gameOver && (System.currentTimeMillis() - gameOverTime >= 2000)) {//restarting the game in 2 seconds
                    reset();
                    gameOver = false;
                }
                break;

            /*case MotionEvent.ACTION_MOVE:
                if(!gameOver && movingPlayer)
                    playerPoint.set((int)event.getX(), (int)event.getY());
                break;*/

            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                movingLeft = false;
                movingRight = false;
                movingUp = false;
                movingDown = false;
                break;
        }

        //return super.onTouchEvent(event);
        return true;
    }

    public void update() {
        if(!gameOver) {
            player.update(playerPoint);
            obstacleManager.update();

            if(obstacleManager.playerCollide(player)) {
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawColor(Color.WHITE); //fill the canvas with designated color

        player.draw(canvas);
        left.draw(canvas);
        right.draw(canvas);
        up.draw(canvas);
        down.draw(canvas);
        obstacleManager.draw(canvas);

        if(gameOver) {
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.MAGENTA);
            drawCenterText(canvas, paint, "GAME OVER");
        }
    }

    // andreas1724 (white color):
    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }
}
