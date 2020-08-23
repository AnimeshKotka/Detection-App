package animesh.com.mark32;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import animesh.com.mark32.FirebaseInit.FaceDetection;
import animesh.com.mark32.Results.ResultsForFaces;

public class SceondFragment extends Fragment {

    FirebaseVisionImage image;
    FirebaseVisionTextRecognizer textRecognizer;
    Button openCamera;
    static final int REQUEST_CAMERA_FOR_TEXT = 2448;

    //Constructor
    public static SceondFragment newInstance(){

        SceondFragment sc = new SceondFragment();

        return sc;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_view, container, false);

        openCamera = view.findViewById(R.id.openCamera2);
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               if (takepic.resolveActivity(getActivity().getPackageManager())!=null)
                    startActivityForResult(takepic,REQUEST_CAMERA_FOR_TEXT);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA_FOR_TEXT) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            recognizeMyText(bitmap);
        }

    }

    private void recognizeMyText(Bitmap bitmap) {

        try {
            image = FirebaseVisionImage.fromBitmap(bitmap);
            textRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        textRecognizer.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                String result = firebaseVisionText.getText();
                if(result.equals("")){
                    result = result.concat("\n No Text Detected");
                }else {
                    Intent intent = new Intent(getContext(), ResultsForFaces.class);
                    intent.putExtra(FaceDetection.RESULT_TEXT,result);
                    startActivity(intent);
                }

            }
        });


    }
}
