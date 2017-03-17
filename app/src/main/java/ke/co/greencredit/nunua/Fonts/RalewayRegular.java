package ke.co.greencredit.nunua.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class RalewayRegular extends TextView {

    public RalewayRegular(Context context) {
        super(context);
        setFont();
    }

    public RalewayRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public RalewayRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
