package edu.txstate.jared.menudemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.txstate.jared.service.AsyncDropletQuery;

/**
 * Activity that displays the user's profile. Displays the logged-in user's username, email, and a
 * list of Droplets they created.
 */
public class ProfileActivity extends AppCompatActivity implements AsyncDropletQuery.AsyncResponse {

    public TextView usernameText;
    public TextView emailText;
    public ListView dropList;
    public ArrayAdapter<String> adapter;
    private String username;
    private String email;

    private static final String TAG = "PROFILEACTIVITY";

    /**
     * Initiates the process of populating the profile with the user's Droplets.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get username and email
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        username = settings.getString(User.USERNAME, "no_username");
        email = settings.getString(User.EMAIL, "no_email");

        // Set username and email fields
        usernameText = (TextView) findViewById(R.id.profileUsername);
        usernameText.setText(username);
        emailText = (TextView) findViewById(R.id.profileEmail);
        emailText.setText(email);

        // Set up list of droplets
        ArrayList<String> dropletStrings = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dropletStrings);
        dropList = (ListView) findViewById(R.id.profileDropListView);
        dropList.setAdapter(adapter);

        // Populate list with droplet data
        AsyncDropletQuery queryTask = new AsyncDropletQuery(this, this);
        queryTask.execute();
    }

    /**
     * Automatically called when AsyncDropletQuery completes. Populates the profile with the
     * user's Droplets.
     * @param result Collection of Droplets returned by AsyncDropletQuery
     */
    @Override
    public void processResult(ArrayList<Droplet> result) {
        Log.d(TAG, "Finished executing droplet query.");
        if (result != null) {
            int size = result.size();
            for (int i = 0; i < size; ++i) {
                adapter.add(result.get(i).getData());
            }
        }
    }

}
