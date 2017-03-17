package ke.co.greencredit.nunua.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Fab on 5/12/2016.
 */
public class RobotoRegularEdit extends EditText {

    public RobotoRegularEdit(Context context) {
        super(context);
        setFont();
    }

    public RobotoRegularEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public RobotoRegularEdit(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}


