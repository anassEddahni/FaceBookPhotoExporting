package app.adria.com.fbphotosexporting.managers;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.adria.com.fbphotosexporting.activities.AuthenticationActivity;
import app.adria.com.fbphotosexporting.entities.Photo;

public class FaceBookManager {

    public static void getAlbums(@NonNull final ManagerCallBacks callBacks) {
        Profile profile = Profile.getCurrentProfile();
        if (profile == null) {
            if (callBacks != null) {
                callBacks.error(null);
            }
            return;
        }

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + profile.getId() + "/albums?fields=name",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            ArrayList<Photo> albums = new ArrayList<>();
                            JSONArray data = (JSONArray) response.getJSONObject().get("data");
                            for (int i = 0; i < data.length(); i++) {
                                if (data.get(i) instanceof JSONObject) {
                                    JSONObject photo = (JSONObject) data.get(i);
                                    albums.add(new Photo(photo.getString("id"), photo.getString("name")));
                                }
                            }

                            if (callBacks != null) {
                                callBacks.results(albums);
                            }

                        } catch (JSONException e) {
                            if (callBacks != null) {
                                callBacks.error(e);
                            }
                        }
                    }
                }).executeAsync();
    }

    public static void getPhotos(@NonNull String albumId, @NonNull final ManagerCallBacks callBacks) {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumId + "/photos/uploaded?fields=picture&size=large",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            ArrayList<Photo> photos = new ArrayList<>();
                            JSONArray data = (JSONArray) response.getJSONObject().get("data");
                            for (int i = 0; i < data.length(); i++) {
                                if (data.get(i) instanceof JSONObject) {
                                    JSONObject photo = (JSONObject) data.get(i);
                                    photos.add(new Photo(photo.getString("picture")));
                                }
                            }

                            if (callBacks != null) {
                                callBacks.results(photos);
                            }

                        } catch (JSONException e) {
                            if (callBacks != null) {
                                callBacks.error(e);
                            }
                        }
                    }
                }).executeAsync();
    }

    public static void logOut(final Activity activity) {
        Profile profile = Profile.getCurrentProfile();
        if (profile == null) return;

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + Profile.getCurrentProfile().getId() + "/permissions",
                null,
                HttpMethod.DELETE,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        LoginManager.getInstance().logOut();
                        Intent intent = new Intent(activity, AuthenticationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(intent);
                    }
                }
        ).executeAsync();
    }

    public interface ManagerCallBacks {
        void error(Exception e);

        void results(ArrayList<Photo> photos);
    }
}
