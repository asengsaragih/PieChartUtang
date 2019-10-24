package org.d3ifcool.utang;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class TentangActivity extends AppCompatActivity {

    String emailRolina = "nimaderiarolina@gmail.com";
    String emailAldi = "aldiwahyu.saragih@gmail.com";
    String emailDevi = "devianarahmadhani@gmail.com";

    String instagramRolina = "nimaderiarolina";
    String instagramAldi = "aldi_saragih";
    String instagramDevi = "deviana_rahmadhani";

    String githubRolina = "nimaderiarolina";
    String githubAldi = "asengsaragih";
    String githubDevi = "devianarahmadhani";

    private ImageView mInstagramRolinaImageView;
    private ImageView mInstagramAldiImageView;
    private ImageView mInstagramDeviImageView;

    private ImageView mPicRolina;
    private ImageView mPicAldi;
    private ImageView mPicDevi;

    private ImageView mGithubRolinaImageView;
    private ImageView mGithubAldiImageView;
    private ImageView mGithubDeviImageView;

    private ImageView mEmailRolinaImageView;
    private ImageView mEmailAldiImageView;
    private ImageView mEmailDeviImageView;

    private ImageView mButtonNextImageView;
    private ImageView mButtonPrefImageView;

    private ConstraintLayout mTentangAplikasi;
    private ConstraintLayout mTentangDeveloper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);

        mInstagramRolinaImageView = findViewById(R.id.imageView_developer_instagram_rolina);
        mInstagramAldiImageView = findViewById(R.id.imageView_developer_instagram_aldi);
        mInstagramDeviImageView = findViewById(R.id.imageView_developer_instagram_devi);

        mGithubRolinaImageView = findViewById(R.id.imageView_developer_github_rolina);
        mGithubAldiImageView = findViewById(R.id.imageView_developer_github_aldi);
        mGithubDeviImageView = findViewById(R.id.imageView_developer_github_devi);

        mEmailRolinaImageView = findViewById(R.id.imageView_developer_gmail_rolina);
        mEmailAldiImageView = findViewById(R.id.imageView_developer_gmail_aldi);
        mEmailDeviImageView = findViewById(R.id.imageView_developer_gmail_devi);

        mButtonNextImageView = findViewById(R.id.imageView_button_right);
        mButtonPrefImageView = findViewById(R.id.imageView_button_left);

        mTentangAplikasi = findViewById(R.id.constraint_tentang_aplikasi);
        mTentangDeveloper = findViewById(R.id.constraint_tentang_developer);

        mPicRolina = findViewById(R.id.imageView_developer_pic_rolina);
        mPicAldi = findViewById(R.id.imageView_developer_pic_aldi);
        mPicDevi = findViewById(R.id.imageView_developer_pic_devi);

        mInstagramIconClicked();
        mGithubIconClicked();
        mEmailIconClicked();

        mPagerButton();

        mPicClick();
    }

    private void mPicClick() {
        mPicRolina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowPicture("rolina");
            }
        });

        mPicAldi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowPicture("aldi");
            }
        });

        mPicDevi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowPicture("devi");
            }
        });
    }

    private void mShowPicture(String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_detail_picture, null);

        ImageView pic = view.findViewById(R.id.imageView_detail_pic);

        int imageRes;

        if (name == "rolina") {
            imageRes = R.drawable.ic_rolina_500dp;
        } else if (name == "aldi") {
            imageRes = R.drawable.ic_aldi_500dp;
        } else {
            imageRes = R.drawable.ic_devi_500dp;
        }

        pic.setImageResource(imageRes);

        builder.setView(view);
        builder.show();
    }

    private void mPagerButton() {
        mButtonNextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTentangAplikasi.setVisibility(View.GONE);
                mTentangDeveloper.setVisibility(View.VISIBLE);
                mButtonNextImageView.setVisibility(View.GONE);
                mButtonPrefImageView.setVisibility(View.VISIBLE);

                getSupportActionBar().setTitle(getString(R.string.about_developer_title_name));
            }
        });

        mButtonPrefImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTentangAplikasi.setVisibility(View.VISIBLE);
                mTentangDeveloper.setVisibility(View.GONE);
                mButtonNextImageView.setVisibility(View.VISIBLE);
                mButtonPrefImageView.setVisibility(View.GONE);

                getSupportActionBar().setTitle(getString(R.string.about_application_title_name));
            }
        });
    }

    private void mInstagramIconClicked() {
        mInstagramRolinaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInstagramIntent(instagramRolina);
            }
        });

        mInstagramAldiImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInstagramIntent(instagramAldi);
            }
        });

        mInstagramDeviImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInstagramIntent(instagramDevi);
            }
        });
    }

    private void mEmailIconClicked() {
        mEmailRolinaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmailIntent(emailRolina);
            }
        });

        mEmailAldiImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmailIntent(emailAldi);
            }
        });

        mEmailDeviImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmailIntent(emailDevi);
            }
        });
    }

    private void mGithubIconClicked() {
        mGithubRolinaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGithubIntent(githubRolina);
            }
        });

        mGithubAldiImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGithubIntent(githubAldi);
            }
        });

        mGithubDeviImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGithubIntent(githubDevi);
            }
        });
    }


    private void mInstagramIntent(String username) {
        Uri uri = Uri.parse("http://instagram.com/_u/" + username);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.instagram.android");

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/" + username)));
        }
    }

    private void mGithubIntent(String username) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://github.com/" + username)));
    }

    private void mEmailIntent(String username) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto: " + username));

        startActivity(Intent.createChooser(intent, "Send Feedback"));
    }
}
