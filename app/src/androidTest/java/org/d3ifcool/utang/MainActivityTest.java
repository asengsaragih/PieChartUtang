package org.d3ifcool.utang;

import android.widget.DatePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void LayoutTestMainActivity() {
        //cek tombol tab layout
        onView(withText(R.string.dipinjam)).perform(click());
        onView(withText(R.string.meminjam)).perform(click());

        //cek swipe viewpager
        onView(withId(R.id.viewpager_main_utang)).perform(swipeRight());
        onView(withId(R.id.viewpager_main_utang)).perform(swipeLeft());

        //masuk ke form activity dan keluar lagi
        onView(withId(R.id.floatingbutton_main_utang)).perform(click());
        onView(isRoot()).perform(pressBack());

        //masuk ke history dan keluar lagi
        onView(withId(R.id.action_history)).perform(click());
        onView(isRoot()).perform(pressBack());

        //click button seetting yang ada di action bottom bar
        onView(withId(R.id.action_setting)).perform(click());
    }

    @Test
    public void LayoutTestFormActivity() {
//        //masuk ke form activity
        onView(withId(R.id.floatingbutton_main_utang)).perform(click());

        //tes tanggal peminjaman
        onView(withId(R.id.edittext_form_tanggal_peminjaman)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2018, 9, 12));
        onView(withId(android.R.id.button1)).perform(click());

        //tes nama
        onView(withId(R.id.edittext_form_nama)).perform(typeText("Depi"));
        onView(withId(R.id.edittext_form_nama)).perform(clearText());
        onView(withId(R.id.edittext_form_nama)).perform(typeText("Aseng"));
        onView(withId(R.id.edittext_form_nama)).perform(clearText());
        onView(withId(R.id.edittext_form_nama)).perform(typeText("Roline"));

        //tes jumlah
        onView(withId(R.id.edittext_form_jumlah)).perform(typeText("500000"));
        onView(withId(R.id.edittext_form_jumlah)).perform(clearText());
        onView(withId(R.id.edittext_form_jumlah)).perform(typeText("500000"));

        //tes tanggal peminjaman
        onView(withId(R.id.edittext_form_jatuh_tempo)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2018, 9, 12));
        onView(withId(android.R.id.button1)).perform(click());

        //cek clear button jatuh tempo
        onView(withId(R.id.textview_form_hapus_jatuh_tempo)).perform(click());

        //tes tanggal peminjaman
        onView(withId(R.id.edittext_form_jatuh_tempo)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2018, 9, 12));
        onView(withId(android.R.id.button1)).perform(click());

        //cek keterangan
        onView(withId(R.id.edittext_form_keterangan)).perform(typeText("Hutang Sepatu"));
        onView(withId(R.id.edittext_form_keterangan)).perform(clearText());
        onView(withId(R.id.edittext_form_keterangan)).perform(typeText("Hutang Beli Baju"), closeSoftKeyboard());

        //cek radio button
        onView(withId(R.id.radiobutton_form_dipinjam)).perform(click());
        onView(withId(R.id.radioButton_form_meminjam)).perform(click());

        //cek tombol back
        onView(isRoot()).perform(pressBack());
        onView(withText(R.string.yes)).perform(click());
    }

    @Test
    public void SaveButtonTestFormActivity() {
        onView(withId(R.id.floatingbutton_main_utang)).perform(click());

        onView(withId(R.id.edittext_form_tanggal_peminjaman)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.edittext_form_nama)).perform(typeText("Aldi"));

        onView(withId(R.id.edittext_form_jumlah)).perform(typeText("500000"));

        onView(withId(R.id.edittext_form_jatuh_tempo)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2019, 9, 28));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.edittext_form_keterangan)).perform(typeText("Hutang Beli Baju"), closeSoftKeyboard());

        onView(withId(R.id.radiobutton_form_dipinjam)).perform(click());

        onView(withId(R.id.button_form_tambah)).perform(click());
    }

    @Test
    public void UpdateButtonTestFormActivity() {
        onView(withText(R.string.dipinjam)).perform(click());

        onData(anything()).inAdapterView(withId(R.id.listview_dipinjam))
                .atPosition(0).perform(click());

        onView(withText(R.string.edit)).perform(click());

        onView(withId(R.id.edittext_form_nama)).perform(clearText());

        onView(withId(R.id.edittext_form_nama)).perform(typeText("Rolina"));

        onView(withId(R.id.textview_form_hapus_jatuh_tempo));

        onView(withId(R.id.edittext_form_keterangan)).perform(clearText(), closeSoftKeyboard());

        onView(withId(R.id.radioButton_form_meminjam)).perform(click());

        onView(withId(R.id.button_form_perbaruhi)).perform(click());

        onView(withText(R.string.meminjam)).perform(click());

        onView(withText(R.string.dipinjam)).perform(click());
    }

    @Test
    public void MarkAsDoneTestActivity() {
        onView(withText(R.string.meminjam)).perform(click());

        onData(anything()).inAdapterView(withId(R.id.listview_meminjam))
                .atPosition(0).perform(click());

        onView(withText(R.string.mark_as_done)).perform(click());

        onView(withId(R.id.action_history)).perform(click());

        onView(withText(R.string.meminjam)).perform(click());

        onView(isRoot()).perform(pressBack());
    }

    @Test
    public void RemoveButtonTestFormActivity() {
        onView(withText(R.string.meminjam)).perform(click());

        onData(anything()).inAdapterView(withId(R.id.listview_meminjam))
                .atPosition(0).perform(click());

        onView(withText(R.string.edit)).perform(click());

        onView(withId(R.id.button_form_hapus)).perform(click());

        onView(withText(R.string.yes)).perform(click());
    }
}
