package com.example.malihakhizer.chatapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.malihakhizer.chatapp.Fragments.ChatsFragment;
import com.example.malihakhizer.chatapp.Fragments.UsersFragment;
import com.example.malihakhizer.chatapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {
    FirebaseAuth auth;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        auth = FirebaseAuth.getInstance();

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),1));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Chats");
        tabLayout.getTabAt(1).setText("Users");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.signout:
                auth.signOut();

                FirebaseUser user = auth.getCurrentUser();
                if(user == null){
                    Intent intent = new Intent(Dashboard.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Error Logging out", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.group_chat:
                requestNewGroup();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void requestNewGroup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this,
                R.style.AlertDialog);
        builder.setTitle("Enter Group Name: ");

        final EditText groupNameField = new EditText(Dashboard.this);
        groupNameField.setHint("eg : Systems group");
        builder.setView(groupNameField);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName = groupNameField.getText().toString();
                if(TextUtils.isEmpty(groupName)){
                    Toast.makeText(Dashboard.this, "Please write a group name..", Toast.LENGTH_SHORT).show();
                } else {

                   // createNewGroup();
                    startActivity(new Intent(Dashboard.this,CreateGroup.class));
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{
        private ArrayList<Fragment> fragments;

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
            this.fragments = new ArrayList<>();
            this.fragments.add(new ChatsFragment());
            this.fragments.add(new UsersFragment());

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            String title = fragments.get(position).getClass().getName();
            return title.subSequence(title.lastIndexOf(".") + 1, title.length());
        }
    }

}
