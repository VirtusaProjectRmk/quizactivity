package com.example.harisha.virtusaquiz;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
//import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.CountDownTimer;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

//import java.util.HashSet;
//import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    //List<String> list;
    //List<String> Ans;

    private TextView mQuestion, quesNum, timer;
    public Firebase mQuestionRef, mChoice1Ref, mChoice2Ref, mChoice3Ref, mChoice4Ref, mAnswerRef;
    public Button nextButton;
    //public CheckBox mRadio1, mRadio2, mRadio3, mRadio4;
    public RadioButton mRadio1, mRadio2, mRadio3, mRadio4;
    public RadioGroup rg;

    public String mAnswer;
    public int questionCounter = 1;
    public int score = 0;
    public int mQuestionNumber = 0;
    //private CountDownTimer countDownTimer;
    public long timeLeftInMillis = 1200000;
    //public static final long COUNTDOWN_IN_MILLIS = 12000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mRadio1 = findViewById(R.id.choice1);
        mRadio2 = findViewById(R.id.choice2);
        mRadio3 = findViewById(R.id.choice3);
        mRadio4 = findViewById(R.id.choice4);
        rg = findViewById(R.id.radio);

        timer = findViewById(R.id.text_view_countdown);
        quesNum = findViewById(R.id.question_num);
        mQuestion = findViewById(R.id.question);
        nextButton = findViewById(R.id.next);

        updateQuestion();
        //startCountDown();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionCounter <= 12) {
                    if (rg.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(QuizActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                    } else {
                        checkAnswer();
                    }
                }
            }
        });
    }

    public void onBackPressed() {

        Intent intent = new Intent(QuizActivity.this,BackActivity.class);
        intent.putExtra("arg",score);
        startActivity(intent);

    }

    @Override
    protected void onUserLeaveHint() {

        Intent intent = new Intent(QuizActivity.this,BackActivity.class);
        intent.putExtra("arg",score);
        startActivity(intent);
        super.onUserLeaveHint();

    }

    CountDownTimer countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            String timeToShow = String.format(Locale.getDefault(), "%02d:%02d", (int) (timeLeftInMillis / 1000) / 60, (int) (timeLeftInMillis / 1000) % 60);
            timer.setText(timeToShow);
            if(timeLeftInMillis < 10000) {
                timer.setTextColor(Color.RED);
            }
            timeLeftInMillis = millisUntilFinished;
        }

        @Override
        public void onFinish() {
                checkAnswer();
                Intent intent = new Intent(QuizActivity.this, FinishActivity.class);
                intent.putExtra("arg", score);
                startActivity(intent);
        }
    }.start();

    public void updateQuestion() {

        rg.clearCheck();

        mQuestionRef = new Firebase("https://virtusaquiz.firebaseio.com/" + mQuestionNumber + "/question");
        mQuestionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String questions = dataSnapshot.getValue(String.class);
                mQuestion.setText(questions);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });

        mChoice1Ref = new Firebase("https://virtusaquiz.firebaseio.com/" + mQuestionNumber + "/choice1");
        mChoice1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String choice = dataSnapshot.getValue(String.class);
                mRadio1.setText(choice);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        mChoice2Ref = new Firebase("https://virtusaquiz.firebaseio.com/" + mQuestionNumber + "/choice2");
        mChoice2Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String choice = dataSnapshot.getValue(String.class);
                mRadio2.setText(choice);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mChoice3Ref = new Firebase("https://virtusaquiz.firebaseio.com/" +  mQuestionNumber + "/choice3");
        mChoice3Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String choice = dataSnapshot.getValue(String.class);
                mRadio3.setText(choice);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mChoice4Ref = new Firebase("https://virtusaquiz.firebaseio.com/" + mQuestionNumber + "/choice4");
        mChoice4Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String choice = dataSnapshot.getValue(String.class);
                mRadio4.setText(choice);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        mAnswerRef = new Firebase("https://virtusaquiz.firebaseio.com/" + mQuestionNumber + "/answer");
        mAnswerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*DataSnapshot data = dataSnapshot.child("answers");
                Iterable<DataSnapshot> children = data.getChildren();
                for(DataSnapshot child : children) {*/
                    mAnswer = dataSnapshot.getValue(String.class);
                    //Ans.add(mAnswer);
                //}
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        if(questionCounter == 12){
            String ans = "Submit";
            nextButton.setText(ans);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(rg.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(QuizActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                    } else {
                        checkAnswer();
                        Intent intent = new Intent(QuizActivity.this, FinishActivity.class);
                        intent.putExtra("arg", score);
                        startActivity(intent);
                    }
                }
            });
        }

        String ans = "Question: " + questionCounter + "/" + 12;
        quesNum.setText(ans);

        questionCounter++;
        mQuestionNumber++;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void checkAnswer() {
        /*if(mRadio1.isChecked()) {
            Ans.add(mRadio1.getText().toString());
        }
        if(mRadio2.isChecked()) {
            Ans.add(mRadio2.getText().toString());
        }
        if(mRadio3.isChecked()) {
            Ans.add(mRadio3.getText().toString());
        }
        if(mRadio4.isChecked()) {
            Ans.add(mRadio4.getText().toString());
        }
        if(new HashSet<>(Ans).equals(new HashSet<>(list))) {
            score++;
        }*/
        int selected = rg.getCheckedRadioButtonId();
        RadioButton rbSelected = findViewById(selected);
        if(rbSelected.getText().equals(mAnswer)) {
            score++;
        }
        if(questionCounter <= 12) {
            updateQuestion();
        }

    }

}