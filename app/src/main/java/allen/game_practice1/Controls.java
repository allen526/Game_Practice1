package allen.game_practice1;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Allen on 2017-11-15.
 */

public class Controls implements GameObject {
    private Rect left;
    private Rect right;
    private Rect down;
    private Rect up;
    private int color;

    public Rect getRectangle(Rect direction) {
        if(direction == left)
            return left;
        else if(direction == right)
            return right;
        else if(direction == down)
            return down;
        else
            return up;
    }

    public Controls(Rect left, Rect right, Rect down, Rect up, int color) {
        this.left = left;
        this.down = down;
        this.right = right;
        this.up = up;
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(left, paint);
        canvas.drawRect(right, paint);
        canvas.drawRect(down, paint);
        canvas.drawRect(up, paint);
    }

    @Override
    public void update() {

    }
}
