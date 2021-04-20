package com.example.btvn7;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btvn7.databinding.PhonebookBinding;

import java.util.ArrayList;
import java.util.List;

public class PhoneBook extends Fragment {
    PhonebookBinding binding;
    List<Contact> contactList = new ArrayList<>();
    SQLHelper sqlHelper;

    public static PhoneBook newInstance() {

        Bundle args = new Bundle();

        PhoneBook fragment = new PhoneBook();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.phonebook, container, false);
        getContactBySQL();
        if(contactList.size()==0){
            if(ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getContext(),"You need to give permission for this app ",Toast.LENGTH_SHORT).show();
            }else {
                getContactsOnPhone();
            }
        }

        setAdapter();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Bus.getInstance().register(getContext());
    }

    @Override
    public void onStop() {
        super.onStop();
        Bus.getInstance().register(getContext());
    }
    public void getContactBySQL(){
        sqlHelper = new SQLHelper(getContext());
        contactList = sqlHelper.getAllContact();
    }
    private void getContactsOnPhone(){
        ContentResolver contentResolver = getContext().getContentResolver();
        String contactId = null;
        String displayName = null;
        Cursor cursor = getContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {

                    Contact contactsInfo = new Contact();
                    contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    contactsInfo.setId(Integer.parseInt(contactId));
                    contactsInfo.setName(displayName);
                    Cursor phoneCursor = getContext().getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{contactId},
                            null);

                    if (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactsInfo.setPhoneNumber(phoneNumber);
                    }

                    phoneCursor.close();
                    contactList.add(contactsInfo);
                    sqlHelper.InsertContact(contactsInfo);
                }
            }
        }
        cursor.close();
    }
    public void setAdapter(){
        PhoneBookAdapter phoneBookAdapter = new PhoneBookAdapter(contactList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        binding.listPhonebook.setAdapter(phoneBookAdapter);
        binding.listPhonebook.setLayoutManager(layoutManager);
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = InfoContact.newInstance("","","");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
            }
        });
        phoneBookAdapter.setIonClickContact(new PhoneBookAdapter.IonClickContact() {
            @Override
            public void onClickName(Contact contact) {
                Fragment fragment = EditInfo.newInstance(String.valueOf(contact.getId()),contact.getName(),contact.getPhoneNumber());
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
            }

            @Override
            public void onClickPhone(Contact contact) {
                Fragment fragment = EditInfo.newInstance(String.valueOf(contact.getId()),contact.getName(),contact.getPhoneNumber());
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
            }

            @Override
            public void onClickLayout(Contact contact) {
                Fragment fragment = EditInfo.newInstance(String.valueOf(contact.getId()),contact.getName(),contact.getPhoneNumber());
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
            }

            @Override
            public void onClickCall(Contact contact) {
                Bus.getInstance().post(new ECall(contact.getPhoneNumber()));
            }
       });
    }

}
