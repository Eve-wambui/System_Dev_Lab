package com.example.meetuphubapp.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.bumptech.glide.Glide;
import com.example.meetuphubapp.R;
import com.example.meetuphubapp.databinding.FragmentExperienceBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ExperienceFragment extends Fragment {

    FragmentExperienceBinding binding;
    Uri imageUri;
    StorageReference storageReference;
    //ProgressDialog progressDialog;

    private String TAG = "ExperienceFragment";

    private ImageView firebaseimage;
    private MaterialButton selectimage, uploadimage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_experience, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentExperienceBinding.bind(view);

        binding.selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        binding.uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }

    private void uploadImage() {
        //show progress
        showProgress();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_ss", Locale.US);
        Date now = new Date();
        String filename  = formatter.format(now);

        storageReference = FirebaseStorage.getInstance().getReference("images/" + filename);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        saveToRealTimeDB(imageUri);
                    }
                }) .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //hide visisbility
                        hideProgress();

                        Toast.makeText(ExperienceFragment.super.getContext(),
                                "Image upload failed",
                                Toast.LENGTH_SHORT).show();

                        Log.e(TAG, "onFailure: Error: "+e.getMessage().toString());
                    }
                });
    }

    private void saveToRealTimeDB(Uri imageUri) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("image", imageUri);

        long timestamp = System.currentTimeMillis();

        reference
                .child("pictures")
                .child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //hide visibility
                        hideProgress();


                        Glide.with(requireContext()).load(imageUri).into(binding.firebaseimage);

                        Toast.makeText(ExperienceFragment.super.getContext(),
                                "Image successfully uploaded",
                                Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgress();
                        Toast.makeText(requireContext(), "Error: "+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onFailure: Error: "+e.getMessage().toString());

                    }
                });
    }

    public void showProgress(){
        binding.loginProgress.loadingProgress.setVisibility(View.VISIBLE);
    }

    public void hideProgress(){
        binding.loginProgress.loadingProgress.setVisibility(View.GONE);
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null){
            imageUri = data.getData();
            binding.firebaseimage.setImageURI(imageUri);
        }
    }
}
