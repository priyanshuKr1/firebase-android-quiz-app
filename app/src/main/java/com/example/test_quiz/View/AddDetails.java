package com.example.test_quiz.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test_quiz.Misc_Section.ImageUtils;
import com.example.test_quiz.R;
import com.example.test_quiz.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class AddDetails extends AppCompatActivity {


    private Spinner section,sem,branch;
    private  ArrayAdapter<String> SecAdapter,BranchAdapter,SemAdapter;
    private Button update;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private EditText name;
    private Toolbar toolbar;
    private ImageView imageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_details);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth= FirebaseAuth.getInstance();
        update=findViewById(R.id.update_button);

        name=findViewById(R.id.Name_details);
        section=findViewById(R.id.Sect_spinner);
        sem=findViewById(R.id.Sem_spinner);
        branch=findViewById(R.id.Branch_spinner);
        imageView = findViewById(R.id.userImage);
        final ProgressDialog progressDialog = new ProgressDialog(this);

        //This  on click method for choosing image
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //ArrayAdapter for section, Semester and branch
        SecAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,this.getResources().getStringArray(R.array.College_Choice));
        SecAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        BranchAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,this.getResources().getStringArray(R.array.Branch_Choice));
        BranchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SemAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,this.getResources().getStringArray(R.array.Sem_Choice));
        SemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        section.setAdapter(SecAdapter);
        branch.setAdapter(BranchAdapter);
        sem.setAdapter(SemAdapter);

        //Updates user Details onCLick method
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                User user=new User(name.getText().toString(),sem.getSelectedItem().toString(),
                        branch.getSelectedItem().toString(),section.getSelectedItem().toString());
                if(name!=null) {
                    //
                    mDatabase.child("users").child(Objects.requireNonNull(auth.getUid()))
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(AddDetails.this,"Updated",Toast.LENGTH_SHORT).show();
                            refresh();//Refresh 9
                            finish();
                            progressDialog.dismiss();
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    });
                }else Toast.makeText(AddDetails.this,"Enter Valid Name",Toast.LENGTH_SHORT).show();
            }
        });
        mDatabase.child("users").child(Objects.requireNonNull(auth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(name.getText().toString().equals("")&&dataSnapshot.exists()){
                            User user=dataSnapshot.getValue(User.class);
                            assert user != null;
                            name.setText(user.name);
                            section.setSelection(Arrays.asList(AddDetails.this.getResources()
                                    .getStringArray(R.array.College_Choice)).indexOf(user.sect));
                            branch.setSelection(Arrays.asList(AddDetails.this.getResources()
                                    .getStringArray(R.array.Branch_Choice)).indexOf(user.branch));
                            sem.setSelection(Arrays.asList(AddDetails.this.getResources()
                                    .getStringArray(R.array.Sem_Choice)).indexOf(user.semester));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void refresh() {

        Intent intent = new Intent(AddDetails.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),mImageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                String userUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                //uploading the image
                UploadTask uploadTask = FirebaseStorage.getInstance()
                        .getReference().child(userUid).putFile(mImageUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageUtils.displayRoundImageFromUrl(AddDetails.this, mImageUri.toString(), imageView);
            //Picasso.get().load(mImageUri).into(imageView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
