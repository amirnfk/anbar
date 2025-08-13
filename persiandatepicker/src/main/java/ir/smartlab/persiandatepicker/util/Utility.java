package ir.smartlab.persiandatepicker.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import ir.smartlab.persiandatepicker.Font;

public class Utility
{
    public static void setFont(Context context, ViewGroup parent)
    {
        for (int i = parent.getChildCount() - 1; i >= 0; i--)
        {
            final View child = parent.getChildAt(i);
            if (child instanceof ViewGroup)
            {
                setFont(context , (ViewGroup) child);
            }
            else
            {
                setFont( context, child);
            }
        }
    }

    public static void setFont(Context context, View view)
    {
        setFont(context, view, Font.SansIran);
    }
    public static void setFont(Context context, View view, Font font)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float size = 0;

//        if(density < 1)//ldpi
//            size = 22;
//        else if(density >= 1 && density < 1.5)//mdpi
//            size = 20;
//        else if(density >= 1.5 && density < 2)//hdpi
//            size = 18;
//        else if(density >= 2 && density < 3)//xhdpi
//            size = 16;
//        else if(density >= 3 && density < 4)//xxhdpi
//            size = 16;
//        else if(density >= 4)//xxxhdpi
//            size = 16;

        Typeface typeFace = null;
        if(font == Font.SansIran) {
            typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSansMobile(FaNum).ttf");
            size = (displayMetrics.widthPixels / displayMetrics.density) / 30;
        }
        else if(font == Font.Nazanin) {
            typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/BNazanin.ttf");
            size = (displayMetrics.widthPixels / displayMetrics.density) / 23;
        }

        if (view instanceof EditText)
        {
            EditText et = ((EditText) view);
            et.setTypeface(typeFace);
            et.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
        else if (view instanceof CheckBox)
        {
            CheckBox cb = ((CheckBox) view);
            cb.setTypeface(typeFace);
            cb.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
        else if (view instanceof Button)
        {
//            size -= size * 50 /100;
            Button b = ((Button) view);
            b.setTypeface(typeFace);
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
        else if (view instanceof TextView)
        {
            TextView tv = ((TextView) view);
            tv.setTypeface(typeFace);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
//        else if (view instanceof NumberPicker)
//        {
//            NumberPicker np = ((NumberPicker) view);
//            np.setTypeface(typeFace);
//            np.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
//        }
    }
}
