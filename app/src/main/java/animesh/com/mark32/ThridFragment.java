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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.util.List;

import animesh.com.mark32.FirebaseInit.FaceDetection;
import animesh.com.mark32.Results.ResultsForFaces;

public class ThridFragment extends Fragment {
    private String title;
    private int page;
    Button camera;
    final static int REQUEST_FOR_LEVEL = 4321;
    FirebaseVisionImage image;
    FirebaseVisionImageLabeler labeler;

    //Constructor
    public static SceondFragment newInstance(int page,String title){

        SceondFragment sc = new SceondFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("page",page);
        bundle.putString("title",title);

        sc.setArguments(bundle);

        return sc;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        page = getArguments().getInt("page");
        title = getArguments().getString("title");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3_view, container, false);
        TextView tv = view.findViewById(R.id.tvLavel3);
        tv.setText("Page "+ page+" "+title);

        camera = view.findViewById(R.id.openCamera3);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takepic.resolveActivity(getActivity().getPackageManager())!=null)
                    startActivityForResult(takepic,REQUEST_FOR_LEVEL);
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_FOR_LEVEL){
            Bundle extra = data.getExtras();
            Bitmap bitmap = (Bitmap) extra.get("data");
            LevelMyImage(bitmap);
        }
    }

    private void LevelMyImage(Bitmap bitmap) {

        try {
            image = FirebaseVisionImage.fromBitmap(bitmap);
            labeler = FirebaseVision.getInstance().
                    getOnDeviceImageLabeler();
        } catch (Exception e) {
            e.printStackTrace();
        }


        labeler.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
            @Override
            public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels) {
                int i = 1;
                String des = "";
                for (FirebaseVisionImageLabel lab : firebaseVisionImageLabels){
                    des = des.concat("\n"+i+" ").concat(lab.getText()+"\n");
                }
                Intent intent = new Intent(getContext(), ResultsForFaces.class);
                intent.putExtra(FaceDetection.RESULT_TEXT,des);
                Log.i("Labler Des @@@",des);
                startActivity(intent);
            }
        });


    }
}
