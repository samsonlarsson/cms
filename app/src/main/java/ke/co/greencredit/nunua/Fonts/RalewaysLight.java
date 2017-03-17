package ke.co.greencredit.nunua.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class RalewaysLight extends TextView {

    public RalewaysLight(Context context) {
        super(context);
        setFont();
    }

    public RalewaysLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public RalewaysLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway-Light.ttf");
        setTypeface(font, Typeface.NORMAL);
    }

}
