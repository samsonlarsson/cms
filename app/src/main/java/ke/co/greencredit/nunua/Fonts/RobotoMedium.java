package ke.co.greencredit.nunua.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Fab on 10/9/2015.
 */
public class RobotoMedium extends TextView {
    public RobotoMedium(Context context) {
        super(context);
        setFont();
    }

    public RobotoMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public RobotoMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
