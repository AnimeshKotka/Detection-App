package animesh.com.mark32.Results;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import animesh.com.mark32.FirebaseInit.FaceDetection;
import animesh.com.mark32.R;

public class ResultsForFaces extends AppCompatActivity {

    Button buttonBack;
    TextView resultstv;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_for_faces);

        buttonBack = findViewById(R.id.back_button);
        resultstv = findViewById(R.id.result_textview);

        text = getIntent().getStringExtra(FaceDetection.RESULT_TEXT);
        resultstv.setText(text);


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
