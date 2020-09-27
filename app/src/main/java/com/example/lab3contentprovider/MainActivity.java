package com.example.lab3contentprovider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * link tham khao https://gist.github.com/srayhunter/47ab2816b01f0b00b79150150feb2eb2
 */

public class MainActivity extends AppCompatActivity {
    Button btnContacts, btnCallLog, btnMediaStore;
    ListView lvItem;
    List<Item> itemList;
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItem = findViewById(R.id.lvItem);
        btnContacts = findViewById(R.id.btnContacts);
        btnCallLog = findViewById(R.id.btnCallLog);
        btnMediaStore = findViewById(R.id.btnMedia);

        btnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllContacts();
                adapter = new ItemAdapter(itemList);
                lvItem.setAdapter(adapter);
            }
        });

        btnCallLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCallLog();
                adapter = new ItemAdapter(itemList);
                lvItem.setAdapter(adapter);
            }
        });
        btnMediaStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMedia();
                adapter = new ItemAdapter(itemList);
                lvItem.setAdapter(adapter);
            }
        });
    }

    public void showAllContacts() {
        itemList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            Log.e("contacts", "contacts");
            // 1- Khai báo địa chỉ
            Uri uri = ContactsContract.Contacts.CONTENT_URI;
            // 2- Viết câu lệnh truy vấn
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            // 3- Đọc kết quả từ câu lệnh truy vấn
            if (cursor != null & cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Item item = new Item();

                    // get the contact's information
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    Integer hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    // get the user's phone number
                    String phone = null;
                    if (hasPhone > 0) {
                        Cursor cp = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        if (cp != null && cp.moveToFirst()) {
                            phone = cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            cp.close();
                        }
                        item.setName(name);
                        item.setDescription(phone);
                        item.setIcon(R.drawable.ic_user);
                        itemList.add(item);
                        cursor.moveToNext();
                    }
                }
                // clean up cursor
                cursor.close();
            } else {
                Toast.makeText(this, "Không có quyền truy cập!", Toast.LENGTH_SHORT).show();
                // Câu lệnh xin cấp quyền
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
            }
        }
    }

    public void showCallLog() {
        itemList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            // 1- Khai báo địa chỉ
            Log.e("callLog", "call log");
            Uri uri = CallLog.Calls.CONTENT_URI;
            // 2- Viết câu lệnh truy vấn
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            // 3- Đọc kết quả từ câu lệnh truy vấn
            if (cursor != null & cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Item item = new Item();
                    // Lấy dữ liệu
                    String numberPhone =
                            cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    String date = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));

                    Date callDayTime = new Date(Long.valueOf(date));
                    DateFormat dateFormat = new SimpleDateFormat("hh:mm E, dd/MM/yyyy");
                    String strDate = dateFormat.format(callDayTime);

                    item.setName(numberPhone);
                    item.setDescription(strDate);
                    item.setIcon(R.drawable.ic_user);
                    itemList.add(item);
                    // Đảo ngược mảng
                    Collections.reverse(itemList);
                    cursor.moveToNext();
                }
                // Clear cursor
                cursor.close();
            }
        } else {
            Toast.makeText(this, "Chưa cấp quyền", Toast.LENGTH_SHORT).show();
            // Xin quyền truy cập
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CALL_LOG}, 2);
        }
    }

    // Media Store
    public void showMedia() {
        Bitmap bitmap = null;
        itemList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Viet cau lenh truy van
            Log.e("media", "media");
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            // 2- Viet cau lenh truy van
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            // 3- doc ket qua tu cau lenh truy van
            if (cursor != null & cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < 10; i++) {
                    String id =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                    String data =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                    Item item = new Item();
                    File imgFile = new File(data);

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    item.setName(String.valueOf(id));
                    item.setDescription(data);
                    item.setIcon2(myBitmap);
                    itemList.add(item);

                    cursor.moveToNext();
                }
                }
                cursor.close();

        } else {
            Toast.makeText(this, "Chưa cấp quyền", Toast.LENGTH_SHORT).show();
            // Cau lenh xin quyen
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
        }
    }


}