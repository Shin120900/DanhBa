package com.example.btvn7;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.btvn7.databinding.EditInfoBinding;

public class EditInfo extends Fragment {
    EditInfoBinding binding;
    SQLHelper sqlHelper;
    String id,name,phone;
    public static EditInfo newInstance(String id, String name, String phone) {

        Bundle bundle = new Bundle();
        EditInfo fragment = new EditInfo();
        bundle.putString("id",id);
        bundle.putString("name",name);
        bundle.putString("phone",phone);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.edit_info,container,false);
        sqlHelper = new SQLHelper(getContext());
        name =getArguments().getString("name");
        binding.tvName.setText(name);
        phone = getArguments().getString("phone");
        binding.tvPhone.setText(phone);
        id = getArguments().getString("id");
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = PhoneBook.newInstance();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
            }
        });

        binding.btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = InfoContact.newInstance(id,name,phone);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
            }
        });

        return binding.getRoot();
    }
}
