package com.handstandsam.buildingdebugfeatures;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.handstandsam.buildingdebugfeatures.network.GitHubService;
import com.handstandsam.buildingdebugfeatures.network.model.GitHubUser;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private static final String EXTRA_USERNAME = "username";

    @Inject
    GitHubService gitHubService;

    @BindView(R.id.username)
    AppCompatEditText usernameEditText;

    Disposable disposable;

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        //Process Bundle
        String username = intent.getStringExtra(EXTRA_USERNAME);
        if (username != null && !username.isEmpty()) {
            username = username.trim();
            usernameEditText.setText(username);
            search(username);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((MyAbstractApplication) getApplication()).getAppComponent().inject(this);
        getSupportActionBar().setTitle(R.string.app_name);

        handleIntent(getIntent());

        usernameEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    submit();
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.submit)
    public void submit() {
        String username = usernameEditText.getText().toString();
        search(username);
    }

    private void search(final String username) {
        gitHubService.user(username).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<GitHubUser>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(GitHubUser gitHubUser) {
                Timber.v(gitHubUser.avatar_url);
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                intent.putExtras(UserActivity.BundleBuilder.createWithUser(gitHubUser));
                startActivity(intent);
                usernameEditText.setText("");
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this, "Could not find user '" + username + "'", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                Timber.v("onComplete");
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
