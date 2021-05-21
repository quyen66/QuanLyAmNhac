package com.example.quanlyamnhac.casi;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.quanlyamnhac.Database;
import com.example.quanlyamnhac.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddCaSiFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    Database database;
    EditText add_mcs, add_tcs;
    Button btn_add_cs, buttonLoadPicture_cs;
    ImageView imgView_cs;
    private Uri mImageUri;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageTask storageTask;

    public AddCaSiFragment() {
    }

    String url = "NULL";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.casi_fragment_insert, container, false);

        add_mcs = root.findViewById(R.id.add_mcs);
        add_tcs = root.findViewById(R.id.add_tcs);

        btn_add_cs = root.findViewById(R.id.btn_add_cs);
        buttonLoadPicture_cs = root.findViewById(R.id.buttonLoadPicture_cs);

        imgView_cs = root.findViewById(R.id.imgView_cs);

        //Init Firebase storage
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        database = new Database(getContext(), "QuanLyAmNhac.sqlite", null, 1);

        buttonLoadPicture_cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
                imgView_cs.getLayoutParams().width = 200;
            }
        });


        btn_add_cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor mbh = database.GetData("SELECT MaCaSi FROM CaSi WHERE '" + add_mcs.getText().toString() + "' = MaCaSi");
                if (add_mcs.getText().toString() == null) {
                    Toast.makeText(getContext(), "Mã ca sĩ không được trống", Toast.LENGTH_LONG).show();
                } else if (add_tcs.getText().toString() == null) {
                    Toast.makeText(getContext(), "Tên ca sĩ không được trống", Toast.LENGTH_LONG).show();
                } else if (mbh.moveToNext()) {
                    Toast.makeText(getContext(), "Mã ca sĩ đã tồn tại", Toast.LENGTH_LONG).show();
                } else {
                    uploadFile();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            database.QueryData("INSERT INTO CaSi " +
                                    "VALUES ('" + add_mcs.getText().toString() +
                                    "', '" + add_tcs.getText().toString() +
                                    "', '" + url + "')");

                            Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_LONG).show();

                            add_mcs.setText("");
                            add_tcs.setText("");
                        }
                    }, 3000);
                }
            }
        });

        return root;

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(getContext()).load(mImageUri).into(imgView_cs);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            storageTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(getContext(), "Upload image successful", Toast.LENGTH_LONG).show();

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();
                            url = downloadUrl.toString();

                            Log.d("imageeee", downloadUrl.toString());
                            Log.d("imageeee1", url);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}