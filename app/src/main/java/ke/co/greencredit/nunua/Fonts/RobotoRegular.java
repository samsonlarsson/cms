package ke.co.greencredit.nunua.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Fab on 10/2/2015.
 */
public class RobotoRegular extends TextView {
    public RobotoRegular(Context context) {
        super(context);
        setFont();
    }

    public RobotoRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public RobotoRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
