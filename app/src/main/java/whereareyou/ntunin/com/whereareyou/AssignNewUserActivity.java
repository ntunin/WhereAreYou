package whereareyou.ntunin.com.whereareyou;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by nik on 17.02.2018.
 */

public class AssignNewUserActivity extends AppCompatActivity {
    static final int PICK_CONTACT_REQUEST = 1;
    static final int PICK_IMAGE_REQUEST = 2;
    static final int ENABLE_BLUETOOTH_REQUEST = 3;
    static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private static final int DISCOVER_DURATION = 3000;

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();
    private String name;
    private String number;
    private String image;
    private String email;
    private String deviceId;
    private ArrayList<BluetoothDevice> pairedDevicesList;
    private ArrayList<BluetoothDevice> nearbyDevicesList;
    private DeviceListAdapter pairedDevicesAdapter;
    private DeviceListAdapter nearbyDevicesAdapter;

    private final BroadcastReceiver bReciever = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                nearbyDevicesList.add(device);
                ListView nearbyDevicesListView = (ListView)findViewById(R.id.nearbyDevicesList);
                nearbyDevicesListView.setAdapter(nearbyDevicesAdapter);
            }
        }
    };;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assign_new_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.assign_new_user_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FloatingActionButton addUserButton = (FloatingActionButton) findViewById(R.id.findContact);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchFromContacts();
            }
        });
        FloatingActionButton syncByBluetoothButton = (FloatingActionButton) findViewById(R.id.retriveId);
        syncByBluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncByBluetooth();
            }
        });
        ImageButton thumbnailImage = (ImageButton) findViewById(R.id.thumbnailImage);
        thumbnailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchNewPhoto();
            }
        });

        ListView pairedDevicesListView = (ListView)findViewById(R.id.pairedDevicesList);
        pairedDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                deviceId = pairedDevicesList.get(position).getAddress();
                done();
            }
        });

        TextView nameView = (TextView)findViewById(R.id.nameText);
        nameView.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() > 0) {
                    findViewById(R.id.retriveId).setVisibility(View.VISIBLE);
                }
            }
        });

        final ListView nearbyDevicesListView = (ListView)findViewById(R.id.nearbyDevicesList);
        nearbyDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                deviceId = nearbyDevicesList.get(position).getAddress();
                done();
            }
        });

    }

    private void done() {
        TextView emailView = (TextView)findViewById(R.id.emailText);
        email = emailView.getText().toString();
        Friend newFriend = new Friend(deviceId, name, image, number, email);
        String key = getResources().getString(R.string.new_friend_result);
        Bundle mBundle = new Bundle();
        mBundle.putString("id", deviceId);
        mBundle.putString("name", name);
        mBundle.putString("image", image);
        mBundle.putString("number", number);
        mBundle.putString("email", email);
        Intent resultIntent = new Intent();
        resultIntent.putExtras(mBundle);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void syncByBluetooth() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();
    }

    private void checkBTState() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
                findViewById(R.id.devicesBundle).setVisibility(View.VISIBLE);
                getDevices();
            } else {
                Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
                        DISCOVER_DURATION );
                startActivityForResult(discoveryIntent, ENABLE_BLUETOOTH_REQUEST);
            }
        }
    }

    private void fetchNewPhoto() {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_REQUEST);
    }

    private void fetchFromContacts() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                Toast toast = Toast.makeText(this, getResources().getText(R.string.read_contacts_explanation), Toast.LENGTH_LONG);
                toast.show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            fetchFromContactsAnyway();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchFromContactsAnyway();

                } else {
                    Toast toast = Toast.makeText(this, getResources().getText(R.string.read_contacts_denied), Toast.LENGTH_SHORT);
                    toast.show();
                }
                return;
            }
        }
    }

    private void fetchFromContactsAnyway() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        switch (requestCode) {
            case PICK_CONTACT_REQUEST: {
                if (resultCode == RESULT_OK) {
                    Uri contactUri = data.getData();
                    String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.PHOTO_URI};

                    Cursor cursor = getContentResolver()
                            .query(contactUri, projection, null, null, null);
                    cursor.moveToFirst();
                    int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    number = cursor.getString(column);
                    column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    name = cursor.getString(column);
                    column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);
                    image = cursor.getString(column);
                    ((TextView)findViewById(R.id.nameText)).setText(name);
                    ((TextView)findViewById(R.id.phoneText)).setText(number);
                    if(image != null) {
                        ((ImageButton)findViewById(R.id.thumbnailImage)).setImageURI(Uri.parse(image));
                    }
                }
                break;
            }
            case  PICK_IMAGE_REQUEST: {
                if (resultCode == RESULT_OK) {
                    image = ImagePicker.getImagePathFromResult(this);
                    ((ImageButton) findViewById(R.id.thumbnailImage)).setImageURI(Uri.parse(image));
                }
                break;
            }
            case  ENABLE_BLUETOOTH_REQUEST: {
                if (resultCode == 300) {
                    findViewById(R.id.devicesBundle).setVisibility(View.VISIBLE);
                    getDevices();
                } else {
                    findViewById(R.id.devicesBundle).setVisibility(View.INVISIBLE);
                }
                break;
            }

        }
    }

    private void getDevices() {
        Set<BluetoothDevice> pairedDevice = btAdapter.getBondedDevices();
        pairedDevicesList = new ArrayList<BluetoothDevice>();
        if(pairedDevice.size()>0)
        {
            for(BluetoothDevice device : pairedDevice) {
                pairedDevicesList.add(device);
            }
        }
        ListView pairedDevicesListView = (ListView)findViewById(R.id.pairedDevicesList);
        pairedDevicesAdapter = new DeviceListAdapter(this, pairedDevicesList);
        pairedDevicesListView.setAdapter(pairedDevicesAdapter);
        nearbyDevicesList = new ArrayList<BluetoothDevice>();
        nearbyDevicesAdapter = new DeviceListAdapter(this, nearbyDevicesList);
        if (btAdapter.isDiscovering()) {
            // Bluetooth is already in modo discovery mode, we cancel to restart it again
            btAdapter.cancelDiscovery();
        }
        btAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bReciever, filter);
    }

    private int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }

    public static Intent getPickImageIntent(Context context) {
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra("return-data", true);
       // takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(context)));
        intentList = addIntentsToList(context, intentList, pickIntent);
        intentList = addIntentsToList(context, intentList, takePhotoIntent);

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
                    context.getString(R.string.pick_image_intent_text));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }

        return chooserIntent;
    }

    private static List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }
        return list;
    }


    public class DeviceListAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater lInflater;
        ArrayList<BluetoothDevice> objects;

        DeviceListAdapter(Context context, ArrayList<BluetoothDevice> devices) {
            ctx = context;
            objects = devices;
            lInflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        // кол-во элементов
        @Override
        public int getCount() {
            return objects.size();
        }

        // элемент по позиции
        @Override
        public Object getItem(int position) {
            return objects.get(position);
        }

        // id по позиции
        @Override
        public long getItemId(int position) {
            return position;
        }

        // пункт списка
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // используем созданные, но не используемые view
            View view = convertView;
            if (view == null) {
                view = lInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            BluetoothDevice d = getDevice(position);
            ((TextView) view.findViewById(android.R.id.text1)).setText(d.getName());
            return view;
        }

        // friend по позиции
        BluetoothDevice getDevice(int position) {
            return ((BluetoothDevice) getItem(position));
        }
    }

    @Override

    protected void onPause() {
        super.onPause();
        if(btAdapter != null) {
            btAdapter.cancelDiscovery();
            unregisterReceiver(bReciever);
        }
    }
}
