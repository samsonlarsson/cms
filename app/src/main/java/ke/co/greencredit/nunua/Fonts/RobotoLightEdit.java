package ke.co.greencredit.nunua.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;


public class RobotoLightEdit extends EditText {
    public RobotoLightEdit(Context context) {
        super(context);
        setFont();
    }

    public RobotoLightEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public RobotoLightEdit(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
