package com.example.btvn7;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.btvn7.databinding.InfoContactBinding;

public class InfoContact extends Fragment implements IPhoneBook{
    InfoContactBinding binding;
    SQLHelper sqlHelper;
    String id,name,phone;
    PresenterInfo presenterInfo;

    public static InfoContact newInstance(String id, String name, String phone) {

        Bundle bundle = new Bundle();
        InfoContact fragment = new InfoContact();
        bundle.putString("id",id);
        bundle.putString("name",name);
        bundle.putString("phone",phone);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.info_contact,container,false);
        sqlHelper = new SQLHelper(getContext());
        name =getArguments().getString("name");
        binding.edtName.setText(name);
        phone = getArguments().getString("phone");
        binding.edtPhone.setText(phone);
        id = getArguments().getString("id");
        presenterInfo = new PresenterInfo(this);
        if(name==""){
            binding.tvInfoTitle.setText("Thêm Liên Lạc");
            binding.btnSave.setText("Lưu liên lạc");
            binding.btnRemove.setVisibility(View.GONE);

        }
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name == ""){
                    Fragment fragment = PhoneBook.newInstance();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();
                }else {
                    Fragment fragment = EditInfo.newInstance(id, name, phone);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();
                }
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = binding.edtName.getText().toString();
                phone = binding.edtPhone.getText().toString();
                if(presenterInfo.onInfo(name, phone) == false){

                }else {
                    Contact contact = new Contact(name, phone);
                    if (binding.btnSave.getText().toString().equals("Lưu liên lạc")) {

                        sqlHelper.InsertContact(contact);
                        onMessenger("Thêm Thành Công");
                        binding.edtName.setText("");
                        binding.edtPhone.setText("");
                        Fragment fragment = EditInfo.newInstance(id, name, phone);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container, fragment);
                        fragmentTransaction.commit();
                    } else {
                        sqlHelper.updateContact(id, contact);
                        onMessenger("Lưu Thành Công");
                        Fragment fragment = EditInfo.newInstance(id, name, phone);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container, fragment);
                        fragmentTransaction.commit();
                    }
                }
            }
        });
        binding.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlHelper.deleteContact(id);
                onMessenger("Xóa Thành Công");
                binding.edtName.setText("");
                binding.edtPhone.setText("");
                Fragment fragment = PhoneBook.newInstance();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
            }
        });
        return binding.getRoot();
    }


    @Override
    public void onMessenger(String mes) {
        Toast.makeText(getContext(), mes, Toast.LENGTH_LONG).show();
    }
}
