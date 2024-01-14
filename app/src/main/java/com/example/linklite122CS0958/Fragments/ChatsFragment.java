package com.example.linklite122CS0958.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.linklite122CS0958.Adapters.UsersAdapter;
import com.example.linklite122CS0958.Models.Users;
import com.example.linklite122CS0958.R;
import com.example.linklite122CS0958.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {

    public ChatsFragment() {
        // Required empty public constructor
    }

    FragmentChatsBinding binding;
    ArrayList<Users> list = new ArrayList<>();

    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        list = new ArrayList<>();

        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();
        UsersAdapter adapter = new UsersAdapter(list, getContext());
        binding.chatRecyclerVIew.setAdapter(adapter);
        DatabaseReference usersRef = database.getReference().child("Users");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerVIew.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance();


        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); // Clear the list before adding new data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey()); // Set the user ID
                    if(!users.getUserId().equals(FirebaseAuth.getInstance().getUid()))
                    list.add(users);
                }
                adapter.notifyDataSetChanged();
                Log.d("ChatsFragment", "Number of users: " + list.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }
}