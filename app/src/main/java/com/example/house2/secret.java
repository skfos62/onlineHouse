package com.example.house2;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;

public class secret extends AppCompatActivity {

    private static final String TAG = "이미지 만들려고";
    private ModelRenderable andyRenderable;
    SceneView sceneView;


    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret);
        sceneView = findViewById(R.id.sceneView);
        ModelRenderable.builder().setSource(this, Uri.parse("chair_01.sfb"))
                .build().thenAccept(this::placeAndy);

    }

    private void placeAndy(ModelRenderable andy) {
        Node andyNode = new Node();
        andyNode.setRenderable(andy);
        andyNode.setWorldPosition(new Vector3(0,-0.5f,-1f));
        sceneView.getScene().addChild(andyNode);
    }


    @Override
    protected void onPause() {
        super.onPause();
        sceneView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            sceneView.resume();
        } catch (CameraNotAvailableException e) {
            e.printStackTrace();
        }
    }


    private void moderRender(Uri uri) {
        //모델 렌더

        Log.i(TAG, "onCreate: 인텐트" + uri);
        ModelRenderable.builder()
                .setSource(this,uri)
                .build()
                .thenAccept(renderable -> andyRenderable = renderable)
                .exceptionally( // 모델렌더링이 안됄때 나오는 토스트
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

    }
}
