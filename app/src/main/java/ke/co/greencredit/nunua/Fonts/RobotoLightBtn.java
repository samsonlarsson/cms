package ke.co.greencredit.nunua.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Fab on 5/19/2016.
 */
public class RobotoLightBtn extends Button {

    public RobotoLightBtn(Context context) {
        super(context);
        setFont();
    }

    public RobotoLightBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public RobotoLightBtn(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
        setTypeface(font, Typeface.NORMAL);
    }

}
