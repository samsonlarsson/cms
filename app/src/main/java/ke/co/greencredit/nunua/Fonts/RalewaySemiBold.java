package ke.co.greencredit.nunua.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Samson on 9/29/2015.
 */
public class RalewaySemiBold extends TextView {

    public RalewaySemiBold(Context context) {
        super(context);
        setFont();
    }

    public RalewaySemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public RalewaySemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway-SemiBold.ttf");
        setTypeface(font, Typeface.NORMAL);
    }

}
