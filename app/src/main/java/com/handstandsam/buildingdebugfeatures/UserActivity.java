package com.handstandsam.buildingdebugfeatures;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.handstandsam.buildingdebugfeatures.network.GitHubService;
import com.handstandsam.buildingdebugfeatures.network.model.GitHubUser;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserActivity extends AppCompatActivity {

    public static class BundleBuilder {

        public static class Params {
            public static final String USER = "user";
            public static final String USERNAME = "username";
        }

        public static Bundle createWithUser(GitHubUser user) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Params.USER, user);
            return bundle;
        }
    }

    @Inject
    GitHubService gitHubService;


    @BindView(R.id.avatar)
    AppCompatImageView avatarImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        ((MyAbstractApplication) getApplication()).getAppComponent().inject(this);
        Bundle bundle = getIntent().getExtras();
        GitHubUser user = (GitHubUser) bundle.get(BundleBuilder.Params.USER);
        if (user != null) {
            showUser(user);
        }  else {
            Toast.makeText(this, "No User Found.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showUser(GitHubUser user) {
        getSupportActionBar().setTitle("User - " + user.name);
        Glide.with(this).load(user.avatar_url).into(avatarImage);
    }
}
