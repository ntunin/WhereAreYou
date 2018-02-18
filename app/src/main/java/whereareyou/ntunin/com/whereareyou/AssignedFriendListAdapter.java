package whereareyou.ntunin.com.whereareyou;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;


/**
 * Created by nik on 17.02.2018.
 */

public class AssignedFriendListAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    List<Friend> objects;

    AssignedFriendListAdapter(Context context, List<Friend> friends) {
        ctx = context;
        objects = friends;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.assigned_friend_list_item, parent, false);
        }

        Friend f = getFriend(position);
        ((TextView) view.findViewById(R.id.friendItemName)).setText(f.getName());
        if(f.getImage() != null) {
            ((ImageView) view.findViewById(R.id.friendItemImage)).setImageURI(Uri.parse(f.getImage()));
        }
        if(f.getPhone() !=null) {
            ((TextView) view.findViewById(R.id.friendItemPhone)).setText(f.getPhone());
        }
        if(f.getEmail() !=null) {
            ((TextView) view.findViewById(R.id.friendItemEmail)).setText(f.getEmail());
        }
        ((CheckBox)view.findViewById(R.id.friendSelected)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                List<Friend> selectedFriends = (List<Friend>)Shared.getBundle().get("selected_friends");
                List<Friend> friends = (List<Friend>)Shared.getBundle().get("friends");
                Friend friend = friends.get(position);
                if(b) {
                    selectedFriends.add(friend);
                } else {
                    selectedFriends.remove(friend);
                }
                ((MyFriendsActivity)parent.getContext()).onSelectedItemsChange();
            }
        });

        return view;
    }

    // friend по позиции
    Friend getFriend(int position) {
        return ((Friend) getItem(position));
    }
}
