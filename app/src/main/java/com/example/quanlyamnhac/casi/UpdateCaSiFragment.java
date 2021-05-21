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
import androidx.fragment.app.DialogFragment;

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

public class UpdateCaSiFragment extends DialogFragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    Database database;
    EditText up_mcs, up_tcs;
    ImageView imgView_up_cs;
    Button btn_up_cs, buttonLoadPicture_up_cs;

    private Uri mImageUri;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageTask storageTask;

    String texta, textb, textc, url = "NULL";

    public UpdateCaSiFragment(String a, String b, String c) {
        texta = a;
        textb = b;
        textc = c;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.casi_fragment_update, container, false);

        //Init Firebase storage
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        up_mcs = root.findViewById(R.id.up_mcs);
        up_tcs = root.findViewById(R.id.up_tcs);

        btn_up_cs = root.findViewById(R.id.btn_up_cs);
        buttonLoadPicture_up_cs = root.findViewById(R.id.buttonLoadPicture_up_cs);

        imgView_up_cs = root.findViewById(R.id.imgView_up_cs);

        up_mcs.setText(texta);
        up_tcs.setText(textb);
        if (textc != null) {
            url = textc;
            Picasso.with(getContext()).load(textc).into(imgView_up_cs);
            imgView_up_cs.getLayoutParams().width = 200;
        }

        database = new Database(getContext(), "QuanLyAmNhac.sqlite", null, 1);

        buttonLoadPicture_up_cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
                imgView_up_cs.getLayoutParams().width = 200;
            }
        });

        btn_up_cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (url.equalsIgnoreCase("NULL")) {
                            database.QueryData("UPDATE CaSi SET " +
                                    " TenCaSi = '" + up_tcs.getText().toString() + "'" +
                                    ", URL = " + url +
                                    " WHERE MaCaSi = '" + up_mcs.getText().toString() + "'");
                        } else {
                            database.QueryData("UPDATE CaSi SET " +
                                    " TenCaSi = '" + up_tcs.getText().toString() + "'" +
                                    ", URL = '" + url + "'" +
                                    " WHERE MaCaSi = '" + up_mcs.getText().toString() + "'");
                        }

                        Toast.makeText(getContext(), "Sửa thành công", Toast.LENGTH_LONG).show();

                        Intent casi = new Intent(getActivity().getBaseContext(), CaSiActivity.class);
                        startActivity(casi);
                    }
                }, 5000);

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

            Picasso.with(getContext()).load(mImageUri).into(imgView_up_cs);
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