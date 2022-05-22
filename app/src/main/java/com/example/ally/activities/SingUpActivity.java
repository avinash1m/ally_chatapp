package com.example.ally.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ally.R;
import com.example.ally.databinding.ActivitySingUpBinding;
import com.example.ally.utilities.Constants;
import com.example.ally.utilities.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class SingUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ActivitySingUpBinding binding;
    private PreferenceManager preferenceManager;
    private String encodedImage;

    String[] select_type = {"Student","Faculty"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySingUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager= new PreferenceManager(getApplicationContext());
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,select_type);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        setListeners();
    }
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(),select_type[position] , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }


    private void setListeners() {

        binding.textSingIn.setOnClickListener(v -> onBackPressed());
        binding.buttonSignUp.setOnClickListener(v ->{
            if(isValidSignUpDetails()){


                                    signUp();

            }
        });
        binding.layoutImage.setOnClickListener(v->{
            Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }
    private void  showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    private void signUp(){



        loading(true);
       FirebaseAuth database = FirebaseAuth.getInstance();
        HashMap<String, Object> user =new HashMap<>();
        user.put(Constants.KEY_NAME,binding.inputName.getText().toString());
        user.put(Constants.KEY_SELECT_TYPE,binding.spinner.getSelectedItem().toString());
        user.put(Constants.KEY_COLLAGE_ID,binding.inputCollageId.getText().toString());
        user.put(Constants.KEY_EMAIL, binding.inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
        user.put(Constants.KEY_IMAGE, encodedImage);
        String email = binding.inputEmail.getText().toString();
        String password = binding.inputPassword.getText().toString();
        Log.i("tag: before create", "starting create user");
        database.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = database.getCurrentUser();
                            user.sendEmailVerification();
                            updateUI(user);
                            Log.i("tag: after create", "user created successfully");
                            uploadData();



                        } else {
                            updateUI(null);
                            Log.e("after create", "createUserWithEmail:failure", task.getException());
                        }
                    }
                });

    }

    private void updateUI(FirebaseUser user) {
    }

    private String encodeImage(Bitmap bitmap){
        int previewWidth =150;
        int previewHeight =bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth,previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,  50, byteArrayOutputStream);
        byte[] bytes=byteArrayOutputStream.toByteArray();
        return Base64. encodeToString(bytes, Base64. DEFAULT);

    }
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
         new ActivityResultContracts.StartActivityForResult(),
         result -> {
             if (result.getResultCode()== RESULT_OK){
                 if (result.getData() != null){
                     Uri imageUri = result.getData().getData();
                     try{
                         InputStream inputStream = getContentResolver().openInputStream(imageUri);
                         Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                         binding.imageProfile.setImageBitmap(bitmap);
                         binding.textAddImage.setVisibility(View.GONE);
                         encodedImage = encodeImage(bitmap);
                     }catch ( FileNotFoundException e){
                         e.printStackTrace();
                     }
                 }
             }
         }
    );


    private Boolean isValidSignUpDetails() {
        Log.i("tag: validating", "starting validation");
        if (encodedImage == null){
            showToast("Select profile image");
            return false;
        }
        else if (binding.inputName.getText().toString().trim().isEmpty()) {
            showToast("enter name");
            return false;
        } else if (binding.inputCollageId.getText().toString().trim().isEmpty()) {
            showToast("Enter Collage Id");
            return false;
        } else if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
       } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches() || !binding.inputEmail.getText().toString().endsWith("@shiats.edu.in"))  {
            showToast("enter valid email ");
            return false;


        }
            else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter Password");
            return false;
        } else if (binding.inputConfirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Confirm password");
            return false;
        } else if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())) {
            showToast("password & confirm password must be same");
            return false;
        }else {
              Log.i("tag: validating", "validated successfully");
        return true;
        }

    }
    private void loading(Boolean isLoading){
        if (isLoading){
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignUp.setVisibility(View.VISIBLE);
        }
    }
    private void uploadData(){

        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user =new HashMap<>();
        user.put(Constants.KEY_NAME,binding.inputName.getText().toString());
        user.put(Constants.KEY_SELECT_TYPE,binding.spinner.getSelectedItem().toString());
        user.put(Constants.KEY_COLLAGE_ID,binding.inputCollageId.getText().toString());
        user.put(Constants.KEY_EMAIL, binding.inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
        user.put(Constants.KEY_IMAGE, encodedImage);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    FirebaseAuth database1 = FirebaseAuth.getInstance();
                    FirebaseUser user1 = database1.getCurrentUser();
                    if(user1.isEmailVerified()){
                        loading(  false);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                        preferenceManager.putString(Constants.KEY_NAME, binding.inputName.getText().toString());
                        preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
                        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent. FLAG_ACTIVITY_NEW_TASK|Intent. FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }



                })
                .addOnFailureListener(exception ->{
                    loading(  false);
                    showToast(exception.getMessage());

                });

    }
}