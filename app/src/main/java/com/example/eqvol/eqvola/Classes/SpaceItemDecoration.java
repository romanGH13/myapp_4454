package com.example.eqvol.eqvola.Classes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.example.eqvol.eqvola.R;

/**
 * Created by eqvol on 28.12.2017.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private Context context;

    public SpaceItemDecoration(int space, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        this.space =  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, space, metrics);
        this.context = context;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = space;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        int color = ContextCompat.getColor(context, R.color.colorWhite);

        for(int i=0; i<parent.getChildCount(); i++){
            final View child = parent.getChildAt(i);
            c.drawRect(layoutManager.getDecoratedLeft(child) + space,
                    layoutManager.getDecoratedTop(child) + space,
                    layoutManager.getDecoratedRight(child) - space,
                    layoutManager.getDecoratedBottom(child) - space, getPaint(color));
        }
    }



    public Paint getPaint(int color){
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0);

        return paint;
    }
}