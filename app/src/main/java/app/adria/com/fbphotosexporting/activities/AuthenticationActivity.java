package app.adria.com.fbphotosexporting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import app.adria.com.fbphotosexporting.R;

public class AuthenticationActivity extends AppCompatActivity {

    public static final String TAG = AuthenticationActivity.class.getName();
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        LoginButton fbLoginButton = findViewById(R.id.login_button);

        callbackManager = CallbackManager.Factory.create();

        fbLoginButton.clearPermissions();
//        fbLoginButton.setLoginBehavior(LoginBehavior.WEB_ONLY);
        fbLoginButton.setReadPermissions("user_photos");
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                        Profile.setCurrentProfile(currentProfile);
                        startApp();
                    }
                };
            }

            @Override
            public void onCancel() {
                Toast.makeText(AuthenticationActivity.this, getString(R.string.str_1), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(AuthenticationActivity.this, getString(R.string.str_2), Toast.LENGTH_SHORT).show();
                //Print the error
                Log.e(TAG, error.getLocalizedMessage());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isLoggedIn()) {
            startApp();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Check the facebook connection status
     *
     * @return true in case a accessToken found
     */
    private boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && !accessToken.isExpired();
    }

    /**
     * Start the app
     * Open the photos activity {@link PhotosActivity}
     */
    private void startApp() {
        Intent intent = new Intent(this, AlbumsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
