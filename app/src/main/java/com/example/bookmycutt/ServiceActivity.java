package com.example.bookmycutt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static android.app.PendingIntent.getActivity;
import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class  ServiceActivity extends AppCompatActivity {
    private ImageView image;
    private ImageView imageView;

    private EditText name,time,price;
    private Button next;
    private FirebaseAuth mAuth;
    StorageReference storageReference;
    String storagePath = "Users_Profile_Cover_Imags/";
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    ProgressDialog pd;
    private static final int CAMERA_REQUEST_CODE=100;
    private static final int STORAGE_REQUEST_CODE=00;
    private static final int IMAGE_PICK_CAMERA_CODE=300;
    private static final int IMAGE_PICK_GALLERY_CODE=400;
    String profileOrCoverPhoto;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    String cameraPermissions[];
    String storagePermissions[];
    Uri image_uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        Employees emp=(Employees) getApplicationContext();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
//        image=findViewById(R.id.serviceimage);
        imageView=findViewById(R.id.serviceimage);

        name=findViewById(R.id.servicename);
        time=findViewById(R.id.timings);
        price=findViewById(R.id.no);
        next=findViewById(R.id.submit);

        Picasso.get().load(R.drawable.flogo).into(imageView);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Owners");
        storageReference = getInstance().getReference(); //firebase storage reference
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.length()==0){
                    name.setError("Enter Service Name");
                }
                else if(price.length()==0){
                    price.setError("Enter Price");
                }
                else if(time.length()==0){
                    time.setError("Enter time");
                }
                else {
                    Services();
                }


            }
        });
        pd = new ProgressDialog(ServiceActivity.this);

        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    }
    public void Services(){
        Employees emp=(Employees) getApplicationContext();
        int count=emp.getService_count();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid=user.getUid();
        count=count-1;
        emp.setService_count(count);

        if (emp.getService_count()==0){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Shop_details");
            final HashMap<Object,String> hashMap = new HashMap<>();
            hashMap.put("Name",name.getText().toString().trim());
            hashMap.put("TimeRequird",time.getText().toString().trim());
            hashMap.put("Price",price.getText().toString().trim());
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference dateRef = storageRef.child(uid+"/Services/"+name.getText().toString());
            dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
            {
                @Override
                public void onSuccess(Uri downloadUrl)
                {
                    hashMap.put("Url", String.valueOf(downloadUrl));                }
            });

            reference.child(uid+"/Services/"+name.getText().toString().trim()).setValue(hashMap);
            startActivity(new Intent(ServiceActivity.this,Odashboard.class));
            Toast.makeText(ServiceActivity.this,"Shop Details has been added",Toast.LENGTH_SHORT).show();
        }
        else {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Shop_details");
            final HashMap<Object,String> hashMap = new HashMap<>();
            hashMap.put("Name",name.getText().toString().trim());
            hashMap.put("TimeRequird",time.getText().toString().trim());
            hashMap.put("Price",price.getText().toString().trim());

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference dateRef = storageRef.child(uid+"/Services/"+name.getText().toString());
            dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
            {
                @Override
                public void onSuccess(Uri downloadUrl)
                {
                    hashMap.put("Url", String.valueOf(downloadUrl));                }
            });
            reference.child(uid+"/Services/"+name.getText().toString().trim()).setValue(hashMap);

            startActivity(new Intent(ServiceActivity.this,ServiceActivity.class));
        }
    }

    private boolean checkStoragePermission(){
        //check if storage permission is enabled or not
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(ServiceActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        //request runtime storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(storagePermissions,STORAGE_REQUEST_CODE);
        }
    }

    private boolean checkCameraPermission(){
        //check if storage permission is enabled or not
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(ServiceActivity.this,Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(ServiceActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission(){
        //request runtime storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(cameraPermissions,CAMERA_REQUEST_CODE);
        }
    }

    private void showEditProfileDialog() {

        //options to show in dialog box
        String options[] = {"Edit Profile Photo","Edit Cover Photo","Edit Name","Edit Phone Number"};
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceActivity.this);
        //set Title
        builder.setTitle("Choose Action");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog item clicks
                if(which == 0){
                    //Edit Profile pic
                    pd.setMessage("Updating Profile Picture");
                    showImagePicDialog();
                    profileOrCoverPhoto="image";
                }
                else if(which == 1){
                    //Edit Cover pic
                    pd.setMessage("Updating Cover Picture");
                    showImagePicDialog();
                    profileOrCoverPhoto="cover";
                }
                else if(which == 2){
                    //Edit Name
                    pd.setMessage("Updating Name");
                }
                else if(which == 3){
                    //Edit Phone Number
                    pd.setMessage("Updating Phone Number");
                }
            }
        });
        //create and show dialog box
        builder.create().show();

    }

    private void pickFromCamera() {
        //Intent of Picking image from device camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp description");
        //put image uri
        image_uri=ServiceActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //intent to start camera
        Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGallery() {
        //pick from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);
    }
    private void showImagePicDialog() {

        //options to show in dialog box
        String options[] = {"Camera","Gallery"};
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceActivity.this);
        //set Title
        builder.setTitle("Select Image From");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog item clicks
                if(which == 0){
                    //Camera pic
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else {
                        pickFromCamera();
                    }
                }
                else if(which == 1){
                    //Gallery pic
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else{
                        pickFromGallery();
                    }
                }
            }
        });
        //create and show dialog box
        builder.create().show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //This method will be called when user press Allow or Deny from permission request dialog

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writeStorageAccepted){
                        //permissions enabled
                        pickFromCamera();
                    }
                    else{
                        //permission denied
                        Toast.makeText(ServiceActivity.this, "Please enable camera & storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted){
                        //permissions enabled
                        pickFromGallery();
                    }
                    else{
                        //permission denied
                        Toast.makeText(this, "Please enable storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //this image will be called after selecting image from camera or gallery
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                //image is picked from gallery,get uri of image
                image_uri=data.getData();

                uploadProfileCoverPhoto(image_uri);
            }
            if(requestCode == IMAGE_PICK_CAMERA_CODE){
                //image is picked from camera,get uri of image
                uploadProfileCoverPhoto(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileCoverPhoto(Uri uri) {
        //creating same function for cover and profile
        //path and name of image to be stored in firebase storage
        String filePathAndName =user.getUid()+"/Services/"+name.getText().toString().trim();
        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();

                //check if image is uploaded or not and uri is recieved
                if(uriTask.isSuccessful()){
                    //image uploaded
                    //add/update url in user's database
                    HashMap<String,Object> results = new HashMap<>();
                    results.put(profileOrCoverPhoto,downloadUri.toString());

                    databaseReference.child(user.getUid()).updateChildren(results)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //uri added in database successfully
                                    pd.dismiss();
                                    Toast.makeText(ServiceActivity.this, "Image Updated", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //error addding uri in database
                            pd.dismiss();
                            Toast.makeText(ServiceActivity.this, "Image Not Updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    //error
                    pd.dismiss();
                    Toast.makeText(ServiceActivity.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(ServiceActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
