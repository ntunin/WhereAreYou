package whereareyou.ntunin.com.whereareyou;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MyFriendsActivity extends AppCompatActivity {

    List<Friend> friends;
    AssignedFriendListAdapter friendsAdapter;
    private FriendReaderDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView lvMain = (ListView) findViewById(R.id.friendsList);
        dbHelper = new FriendReaderDbHelper(this);
        fillData(dbHelper);
        friendsAdapter = new AssignedFriendListAdapter(this, friends);
        lvMain.setAdapter(friendsAdapter);
        Shared.getBundle().put("selected_friends", new ArrayList<Friend>());
        Shared.getBundle().put("friends", friends);

        FloatingActionButton addUserButton = (FloatingActionButton) findViewById(R.id.assignNewMember);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewUser();
            }
        });
        final FloatingActionButton startTrackingButton = (FloatingActionButton) findViewById(R.id.startTracking);
        startTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTracking();
            }
        });


        fetchId();
    }

    private void startTracking() {
        Intent intent = new Intent(this, MapTrackingView.class);
        startActivity(intent);
    }

    public void onSelectedItemsChange() {
        List<Friend> selectedFriends = (List<Friend>)Shared.getBundle().get("selected_friends");
        findViewById(R.id.startTracking).setVisibility((selectedFriends.size() > 0)? View.VISIBLE: View.INVISIBLE);
    }

    private void fetchId() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String macAddress = android.provider.Settings.Secure.getString(getContentResolver(), "bluetooth_address");
        Shared.getBundle().put("device_id", macAddress);
    }

    private void addNewUser() {
        Intent intent = new Intent(this, AssignNewUserActivity.class);
        int requestCode = getResources().getInteger(R.integer.assign_new_friend_request_code);
        startActivityForResult(intent, requestCode);
    }


    private void fillData(FriendReaderDbHelper dbHelper) {
        friends = dbHelper.select();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String macAddress = android.provider.Settings.Secure.getString(getContentResolver(), "bluetooth_address");
        int assignNewFriend = getResources().getInteger(R.integer.assign_new_friend_request_code);
        if (requestCode == assignNewFriend) {
            if (resultCode == RESULT_OK) {
                String id = data.getStringExtra("id");
                String name = data.getStringExtra("name");
                String email = data.getStringExtra("email");
                String phone = data.getStringExtra("number");
                String image = data.getStringExtra("image");
                Friend friend = new Friend(id, name, image, phone, email);
                dbHelper.insert(friend);
                friends.add(friend);
                ((ListView)findViewById(R.id.friendsList)).setAdapter(friendsAdapter);
            }
        }
    }
}
