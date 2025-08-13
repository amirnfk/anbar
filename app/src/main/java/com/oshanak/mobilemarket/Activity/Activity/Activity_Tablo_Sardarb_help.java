package com.oshanak.mobilemarket.Activity.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;


import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroCustomLayoutFragment;
import com.github.appintro.AppIntroFragment;
import com.github.appintro.AppIntroPageTransformerType;

import com.oshanak.mobilemarket.Activity.AppInteroCustomFragment;

import com.oshanak.mobilemarket.R;

public class Activity_Tablo_Sardarb_help   extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_tablo_sardarb_help2);
        addSlide(AppInteroCustomFragment.newInstance("https://storehandheld.ows.gbgnetwork.net/background/ax1.jpg",
                R.layout.custom_intro_layout,
                getResources().getColor(R.color.green_A700),
                "جهت عکاسی",
                "تمامی تصاویر یکبار به صورت افقی و یکبار به صورت عمودی عکاسی شوند",
                R.drawable.progress_image,
       true ));
        addSlide(AppInteroCustomFragment.newInstance("https://storehandheld.ows.gbgnetwork.net/background/ax2.jpg",
                R.layout.custom_intro_layout,
                getResources().getColor(R.color.red_A700),
                "عابر پیاده",
                " درصورت رد شدن عابر پیاده مجددا عکاسی شود",
                R.drawable.progress_image,false
        ));
        addSlide(AppInteroCustomFragment.newInstance("https://storehandheld.ows.gbgnetwork.net/background/ax3.jpg",
                R.layout.custom_intro_layout,
                getResources().getColor(R.color.green_A700),
                "تمیز بودن محوطه",
                "در هنگام عکاسی از تمامی نماها از تمیز بودن محوطه اطمینان حاصل فرمایید",
                R.drawable.progress_image,true
        ));
        addSlide(AppInteroCustomFragment.newInstance("https://storehandheld.ows.gbgnetwork.net/background/ax4.jpg",
                R.layout.custom_intro_layout,
                getResources().getColor(R.color.red_A700),
                "موتور و خودروهای پارک شده",
                "در تصویر عکاسی شده از نمای بیرون حضور هرگونه موتور (حتی پیک هفت) و خودروهای پارک شده در جلوی درب فروشگاه ممنوع می باشد",
                R.drawable.progress_image,false
        ));
        addSlide(AppInteroCustomFragment.newInstance("https://storehandheld.ows.gbgnetwork.net/background/ax5.jpg",
                R.layout.custom_intro_layout,
                getResources().getColor(R.color.green_A700),
                "تابلوی سردرب تمیز",
                " نمای بیرون در عکاسی باید با در نظر گرفتن پاکیزگی شیشه ها، درب ورودی، بدون اشیای اضافه و با تابلوی سردرب تمیز باشد( از چیدمان مرتب سبدها و چرخهای دستی پشت شیشه اطمینان حاصل کنید",
                R.drawable.progress_image,true
        ));
        addSlide(AppInteroCustomFragment.newInstance("https://storehandheld.ows.gbgnetwork.net/background/ax6.jpg",
                R.layout.custom_intro_layout,
                getResources().getColor(R.color.green_A700),
                "نماهای مختلف",
                "  در صورتیکه فروشگاه در نمای سه_بر میباشد باید چند تصویر از نماهای مختلف عکاسی شود بطوریکه تمام تابلوها از جهات مختلف قابل دیدن باشد",
                R.drawable.progress_image,true
        ));

        addSlide(AppInteroCustomFragment.newInstance("https://storehandheld.ows.gbgnetwork.net/background/ax7.jpg",
                R.layout.custom_intro_layout,
                getResources().getColor(R.color.red_A700),
                "فایل با کیفیت",
                "تصاویر باید به صورت فایل با کیفیت اپلود شود",
                R.drawable.progress_image,false
        ));

        addSlide(AppInteroCustomFragment.newInstance("https://storehandheld.ows.gbgnetwork.net/background/ax8.jpg",
                R.layout.custom_intro_layout,
                getResources().getColor(R.color.green_A700),
                "تنظیمات خودکار دوربین",
                "در هنگام عکاسی از نماها در شب حتما تنظیمات نور به گونه ای باشد که نوشته هفت در تابلوی سردرب نور شدید نداشته باشد که موجب سوختن و تاریک شدن کل تصویر گردد(برای حل این موضوع کافیست یکبار روی تصویر هفت در صفحه دوربین کلیک کنید تا تنظیمات خودکار دوربین فعال گردد",
                R.drawable.progress_image,true
        ));


//        addSlide(AppIntroFragment.createInstance(
//                "تنظیمات خودکار دوربین",
//                " در هنگام عکاسی از نماها در شب حتما تنظیمات نور به گونه ای باشد که نوشته هفت در تابلوی سردرب نور شدید نداشته باشد که موجب سوختن و تاریک شدن کل تصویر گردد(برای حل این موضوع کافیست یکبار روی تصویر هفت در صفحه دوربین کلیک کنید تا تنظیمات خودکار دوربین فعال گردد)",
//                R.drawable.s2,
//                R.color.deep_purple_A200,R.color.white,R.color.white,R.font.iransansbold,R.font.iransansbold
//        ));

        // Fade Transition
        setTransformer(AppIntroPageTransformerType.Fade.INSTANCE);

        // Show/hide status bar
//        showStatusBar(true);
//        //Enable the color "fade" animation between two slides (make sure the slide implements SlideBackgroundColorHolder)
//        setColorTransitionsEnabled(true);
//
//        //Prevent the back button from exiting the slides
//        setSystemBackButtonLocked(true);
//
//        //Activate wizard mode (Some aesthetic changes)
//        setWizardMode(true);
//
//        //Enable immersive mode (no status and nav bar)
//        setImmersiveMode();
//
//        //Enable/disable page indicators
//        setIndicatorEnabled(true);
//
//        //Dhow/hide ALL buttons
//        setButtonsEnabled(true);

        // Enable Vibration

    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    protected void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }
}