package com.example.test_quiz.Create_Quiz;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test_quiz.R;
import com.example.test_quiz.Model.Question;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class Custom_quiz extends AppCompatActivity {

    EditText question;
    EditText aText;
    EditText bText;
    EditText cText;
    EditText dText;
    RadioButton aRadio;
    RadioButton bRadio;
    RadioButton cRadio;
    RadioButton dRadio;

    int currentQuestion = 1;
    int previousQuestion = 1;
    TextView questionNumber;

    ArrayList<Question> ques;
    JSONArray jsonArray;
    String selectedOption = "";

    Button save_button;
    AlertDialog alertDialog;
    private View dialogvView;
    String fileName = "file";
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference myRef;
    CardView fab,f2,fl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        jsonArray = new JSONArray();
        setContentView(R.layout.activity_create_quiz);
        question = findViewById(R.id.questionView);
        question =  findViewById(R.id.questionView);
        aText =  findViewById(R.id.aText);
        bText =  findViewById(R.id.bText);
        cText =  findViewById(R.id.cText);
        dText =  findViewById(R.id.dText);
        questionNumber =  findViewById(R.id.questionNumber);
        aRadio =  findViewById(R.id.aRadio);

        bRadio =  findViewById(R.id.bRadio);
        cRadio =  findViewById(R.id.cRadio);
        dRadio =  findViewById(R.id.dRadio);
        auth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        myRef=database.getReference();
        selectedOption = "";
        currentQuestion = 1;
        setListeners();

        ques = new ArrayList<>();

        alertDialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        dialogvView = inflater.inflate(R.layout.dialog_custom,null);



        fab = findViewById(R.id.nextfab);
        fl = findViewById(R.id.fab2);//save button
        f2 = findViewById(R.id.pre_card);

        f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(previousQuestion>1) {
                    previousQuestion--;
                    setAllData(previousQuestion);
                }
                if(previousQuestion==1)
                    f2.setVisibility(View.INVISIBLE);
                    //Question question1 = new Question();
                Toast.makeText(Custom_quiz.this, String.valueOf(previousQuestion), Toast.LENGTH_SHORT).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(previousQuestion!=currentQuestion) {
                    previousQuestion++;
                    if(previousQuestion!=currentQuestion)
                    setAllData(previousQuestion);
                    else {
                        clearAllData();
                        questionNumber.setText(String.valueOf(currentQuestion));
                    }
                    if(previousQuestion>1)
                        f2.setVisibility(View.VISIBLE);
                }
                boolean cont = getEnteredQuestionsValue();
                if (cont)
                {
                    previousQuestion++;
                    currentQuestion++;
                    Toast.makeText(Custom_quiz.this, "QUESTION " + currentQuestion, Toast.LENGTH_SHORT).show();
                    questionNumber.setText(String.valueOf(currentQuestion));
                    clearAllData();
                    f2.setVisibility(View.VISIBLE);
                }
            }
        });

        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(jsonArray.length()!=0)
                {
                    final JSONObject tempObject = new JSONObject();
                    // get dialog_custom.xml view
                    LayoutInflater li = LayoutInflater.from(Custom_quiz.this);
                    View promptsView = li.inflate(R.layout.dialog_custom, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            Custom_quiz.this);

                    // set dialog_custom.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);
                    final EditText userInput =  promptsView
                            .findViewById(R.id.editTextDialogUserInput);
                    final EditText userTime = promptsView.findViewById(R.id.editTextDialogUserInput1);

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {

                                            final String str = userTime.getText().toString();
                                            String temp2 = str;
                                   ////  Log.d("TIMEON",userTime.getText().toString().trim());
                                            try {
                                                tempObject.put("Questions",jsonArray);
                                                final String TIME = userTime.getText().toString().trim();
                                                tempObject.put("Time",Integer.parseInt(temp2));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            final String jsonStr = tempObject.toString();
                                            // get user input and set it to result
                                            // edit text
                                            if (str != null ) {
                                                Toast.makeText(getApplicationContext()
                                                        , str, Toast.LENGTH_LONG).show();
//                                         result.setText(userInput.getText());
                                                Map<String, Object> result = new Gson().fromJson(jsonStr, Map.class);
                                                fileName = userInput.getText().toString().trim();
                                                if (!TextUtils.isEmpty(fileName))
                                                    myRef.child("tests").child(fileName).setValue(result);
                                            }
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
                else
                {
                    Toasty.error(getApplicationContext(),
                            "Incomplete Question format", Toasty.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Custom_quiz.this);
        builder.setMessage("Exit without saving?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void updateData(int position) {
        Question question1 = new Question();
        question1 = ques.get(position-1);
    }

    public void setAllData(int position) {
        clearAllData();
        Question question1 = new Question();
        question1 = ques.get(position-1);
        questionNumber.setText(String.valueOf(question1.getId()));
        question.setText(question1.getQuestion());
        aText.setText(question1.getOpt_A());
        bText.setText(question1.getOpt_B());
        cText.setText(question1.getOpt_C());
        dText.setText(question1.getOpt_D());
        switch (question1.getAnswer()){
            case "A":
                aRadio.setChecked(true);
                break;
            case "B":
                bRadio.setChecked(true);
                break;
            case "C":
                cRadio.setChecked(true);
                break;
            case "D":
                dRadio.setChecked(true);
                break;
        }
    }

    private void clearAllData() {

        aRadio.setChecked(false);
        bRadio.setChecked(false);
        cRadio.setChecked(false);
        dRadio.setChecked(false);
        aText.setText(null);
        bText.setText(null);
        cText.setText(null);
        dText.setText(null);
        question.setText(null);
        selectedOption = "";
    }

    private boolean getEnteredQuestionsValue() {

        boolean cont = false;
        if (TextUtils.isEmpty(question.getText().toString().trim())) {
            question.setError("Please fill in a question");
        }
        else if (TextUtils.isEmpty(aText.getText().toString().trim())) {
            aText.setError("Please fill in option A");
        }
        else if (TextUtils.isEmpty(bText.getText().toString().trim())) {
            bText.setError("Please fill in option B");
        }
        else if (TextUtils.isEmpty(cText.getText().toString().trim())) {
            cText.setError("Please fill in option C");
        }
        else if (TextUtils.isEmpty(dText.getText().toString().trim())) {
            dText.setError("Please fill in option D");
        }
        else if (selectedOption.equals("")) {
            Toast.makeText(this, "Please select the correct answer", Toast.LENGTH_SHORT).show();
        }
        else {
            Question quest = new Question();
            quest.setId(currentQuestion);
            quest.setQuestion(question.getText().toString());
            quest.setOpt_A(aText.getText().toString());
            quest.setOpt_B(bText.getText().toString());
            quest.setOpt_C(cText.getText().toString());
            quest.setOpt_D(dText.getText().toString());
            quest.setAnswer(selectedOption);
            ques.add(quest);
            cont = true;

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("answer",selectedOption);
                jsonObject.put("opt_A",aText.getText().toString().trim());
                jsonObject.put("opt_B",bText.getText().toString().trim());
                jsonObject.put("opt_C",cText.getText().toString().trim());
                jsonObject.put("opt_D",dText.getText().toString().trim());
                jsonObject.put("question",question.getText().toString().trim());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            jsonArray.put(jsonObject);
        }
        return cont;
    }

    private void setListeners() {
        aRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = "A";
                bRadio.setChecked(false);
                cRadio.setChecked(false);
                dRadio.setChecked(false);
            }
        });
        bRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = "B";
                aRadio.setChecked(false);
                cRadio.setChecked(false);
                dRadio.setChecked(false);
            }
        });
        cRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = "C";
                bRadio.setChecked(false);
                aRadio.setChecked(false);
                dRadio.setChecked(false);
            }
        });
        dRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = "D";
                bRadio.setChecked(false);
                cRadio.setChecked(false);
                aRadio.setChecked(false);
            }
        });

    }
}
