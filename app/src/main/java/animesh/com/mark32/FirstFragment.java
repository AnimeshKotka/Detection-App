package animesh.com.mark32;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.util.List;

import animesh.com.mark32.FirebaseInit.FaceDetection;
import animesh.com.mark32.Results.ResultsForFaces;

public class FirstFragment extends Fragment {

    // Store instance variables

    Button openCamera;
    private final static int REQUEST_CAMERA_CAPTURE = 124;
    FirebaseVisionImage image;
    FirebaseVisionFaceDetector detector;

    // newInstance constructor for creating fragment with arguments
    public static FirstFragment newInstance() {
        FirstFragment fragmentFirst = new FirstFragment();

        //fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //page = getArguments().getInt("someInt", 0);
        //title = getArguments().getString("someTitle");


    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_view, container, false);

        TextView tv = view.findViewById(R.id.tvLavel);
        tv.setText("");
        openCamera = view.findViewById(R.id.openCamera);

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getActivity().getPackageManager())!= null)
                    startActivityForResult(i,REQUEST_CAMERA_CAPTURE);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA_CAPTURE){
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            DetectMyFace(bitmap);
            //method for image processing
        }
    }

    private void DetectMyFace(Bitmap bitmap) {
        FirebaseVisionFaceDetectorOptions highAccuracyOpts =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .enableTracking()
                        .setMinFaceSize(0.15f)
                        .build();

        try {
            image = FirebaseVisionImage.fromBitmap(bitmap);
            detector = FirebaseVision.getInstance()
                    .getVisionFaceDetector(highAccuracyOpts);
        } catch (Exception e) {
            e.printStackTrace();
        }

        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                String results = "";
                int i =1;
                for (FirebaseVisionFace face : firebaseVisionFaces){
                    results = results.concat("\n"+i+". ").
                    concat("smile: "+(face.getSmilingProbability()*100)+"%")
                    .concat("\n\nKeep smiling :)");
                    i++;
                }
                Log.i("Results @@@",results);

                if (results.equals(""))
                    Toast.makeText(getContext(),"No Face",Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(getContext(), ResultsForFaces.class);
                    intent.putExtra(FaceDetection.RESULT_TEXT, results);
                    startActivity(intent);
                }
            }
        });

    }
}
