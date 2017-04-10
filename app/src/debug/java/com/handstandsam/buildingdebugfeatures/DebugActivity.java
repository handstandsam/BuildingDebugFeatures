package com.handstandsam.buildingdebugfeatures;

import static com.handstandsam.buildingdebugfeatures.utils.IntentUtils.getIntentForUsername;

import javax.inject.Inject;

import com.handstandsam.buildingdebugfeatures.debug.DebugPreferences;
import com.handstandsam.buildingdebugfeatures.debug.HomeScreenShortcutBuilder;
import com.handstandsam.buildingdebugfeatures.network.GitHubService;
import com.handstandsam.buildingdebugfeatures.utils.IntentUtils;
import com.jakewharton.processphoenix.ProcessPhoenix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DebugActivity extends AppCompatActivity {

    @Inject
    GitHubService gitHubService;

    @BindView(R.id.mocks_on)
    CheckBox mocksCheckbox;

    @BindView(R.id.chuck)
    CheckBox chuckEnabledCheckbox;

    @BindView(R.id.apply_changes)
    AppCompatButton applyChangesButton;

    @BindView(R.id.trigger_notification)
    AppCompatButton triggerNotificationButton;

    @BindView(R.id.username)
    AppCompatEditText usernameEditText;

    @BindView(R.id.baseurl)
    AppCompatEditText baseUrlEditText;

    @BindView(R.id.add_shortcut)
    AppCompatButton addShortcutButton;

    private DebugPreferences debugPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        ButterKnife.bind(this);
        debugPreferences = new DebugPreferences(this);

        mocksCheckbox.setChecked(debugPreferences.isMockMode());

        mocksCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                debugPreferences.setMockMode(b);
            }
        });

        chuckEnabledCheckbox.setChecked(debugPreferences.isChuckEnabled());

        chuckEnabledCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                debugPreferences.setChuckEnabled(b);
            }
        });

        applyChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String altEndpoint = baseUrlEditText.getText().toString();
                if (altEndpoint != null && altEndpoint.length() > 0) {
                    debugPreferences.setBaseUrl(altEndpoint);
                }
                ProcessPhoenix.triggerRebirth(DebugActivity.this);
            }
        });

        addShortcutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                Intent intent = getIntentForUsername(DebugActivity.this, username);
                new HomeScreenShortcutBuilder(DebugActivity.this, username, intent).create();
            }
        });

        triggerNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                IntentUtils.triggerPRNotification(DebugActivity.this, username);
            }
        });
    }
}
