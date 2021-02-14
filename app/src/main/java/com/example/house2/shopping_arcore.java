package com.example.house2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.house2.classfile.shopping_list_class;
//import com.flask.colorpicker.ColorPickerView;
//import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.Sun;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class shopping_arcore extends AppCompatActivity implements bottom_sheet_shopping.BottomSheetListener, com.example.house2.shopping_arcore_list_adapter.onModelSelectListener {
    private GLSurfaceView surfaceView;
    private static final String TAG = shopping_arcore.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;
    private ModelRenderable andyRenderable;
    private ModelRenderable andyRenderable2;
    LinearLayout placeFurniture;
    ImageView PLACE_FURNITURE; // +버튼

    // 이미지 캡쳐관련
    private ImageView captureBtn;

    // 삭제하기 버튼
    ImageView clearButton;
    AnchorNode anchorNode;
    TransformableNode andy;
    private List<AnchorNode> anchorNodeList = new ArrayList<>();
    private List<AnchorNode> anchorNodeList2 = new ArrayList<>();
    private AnchorNode currentSelectedAnchorNode = null;
    private AnchorNode currentSelectedAnchorNode2 = null;
    ImageView removeButton;
    ModelRenderable renderable;
    ModelRenderable highlightedAndyRenderable;
    ModelRenderable highlightedAndyRenderable1;

    //모델 색상 바꾸기
    ImageView color_change_btn;
    LinearLayout colorPanel; // 모델색상 바꾸는 패널

    // 모델 움직이기
    TransformableNode transformableNode;
    ImageView moveToModel_btn;

    int numberOfAnchors = 0;
    // 어떤 모델을 선택하는지 알려주는 변수
    public static String selectModel;
    Uri uri;

    // 모델선택할때 선택하는 submeshindex
    int submeshIndex;

    ImageView item_show_image;
    ImageView item_show_image2;
    ImageView item_show_image3;
    ImageView item_show_image4;
    private ImageView imageView;
    private int selectedColor = 0;

    //리사이클러뷰관련
    RecyclerView item_arcore_image_recycle;
    shopping_arcore_list_adapter shopping_arcore_list_adapter;
    shopping_list_class shopArcoreReData;
    ArrayList<shopping_list_class> shopArcoreReDatalist = new ArrayList<>();
    shop_list_arcore shop_list_arcore;

    // 삭제
    String nock;

    //선택관련
    Node hitNode;

    //부분적으로 선택하기 관련
    LinearLayout PLACE_SELECT_PART;
    SceneView sceneView;
    Scene Scene;
    ArrayList arrayList;
    ArrayAdapter arrayAdapter;
    Spinner select_spinner;
    ImageView select_change_BTN;
    Node andyNode;
    int partSelectNum = 0;
    Button part_select_OK;
    Button part_select_cancle;

    // 장바구니 관련
    ImageView select_cart_BTN;
    shopping_arcore_cart shopping_arcore_cart;
    String findSelectedValue;

    // undo 관련
    ImageView undoButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_arcore);

//        modelLoader = new ModelLoader(new WeakReference<>(this));
        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }
        colorPanel = findViewById(R.id.colorPanel);


        // 뷰페이지에서 어떤 sfb아이템을 쓸것인가 인텐트로 받아오는곳
        Intent intent = getIntent();
        selectModel = intent.getStringExtra("sfbName");
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        // uri = 어떤 모델을 렌더링 하냐를 결한하는것

        Log.i(TAG, "onCreate: 인텐트" + uri);

        // Renderable을 빌드하면 Sceneform은 반환하면서 백그라운드에서 리소스를로드합니다.
        // CompletableFuture. get ()을 호출하기 전에 thenAccept (), handle ()을 확인하거나 isDone ()을 확인하십시오.
        // setupModel

        // 모델을 셋업하는 중
        // 여기서 선택한 모델을 셋업할수있게 하면 어떨까
        // 여기 부분을 함수로 만들어서 진행해야되겠다.
        uri = Uri.parse(selectModel);
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


//
       //  유저가 평면에 탭 할때 모델을 더한다.
        arFragment.getArSceneView().getScene().addOnPeekTouchListener(this::handleOnTouch);

        // 삭제하기 누르면 구현되는거
        // 아이콘을 휴지통 아이콘으로 바꿔서 전체삭제하는 메소드도 넣어야되겠다.(완료)
        clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Node> children = new ArrayList<>(arFragment.getArSceneView().getScene().getChildren());
                for (Node node : children) {
                    if (node instanceof AnchorNode) {
                        if (((AnchorNode) node).getAnchor() != null) {
                            ((AnchorNode) node).getAnchor().detach();
                        }
                    }
                    if (!(node instanceof Camera) && !(node instanceof Sun)) {
                        node.setParent(null);
                    }
                }
            }
        });

        // 여기서 지우는 메소드 삽입하고
        // 하단에 지우는 메소드 사용하기

        removeButton = findViewById(R.id.removeButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAnchorNode(currentSelectedAnchorNode);
                currentSelectedAnchorNode = null;
            }
        });



        // 색상바꾸기 버튼
        color_change_btn = findViewById(R.id.color_change_btn);
        color_change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pickColor();

            }
        });

        // 부분적으로 색상 바뀌는거 만들기
        // 스피너
        arrayList = new ArrayList<>();
        if (selectModel.equals("lamppost.sfb") || selectModel.equals("table_large_rectangular_01.sfb")) {
            arrayList.add("0");
        } else if (selectModel.equals("bar_chair_2.sfb") || selectModel.equals("etc_04.sfb") || selectModel.equals("lamp_ikea.sfb") || selectModel.equals("moden_cab.sfb") || selectModel.equals("tables_01.sfb") ) {
            arrayList.add("0");
            arrayList.add("1");
        } else if (selectModel.equals("chair_01.sfb") || selectModel.equals("chair_04.sfb") || selectModel.equals("etc_01.sfb") || selectModel.equals("etc_03.sfb") || selectModel.equals("test.sfb")) {
            arrayList.add("0");
            arrayList.add("1");
            arrayList.add("2");
        } else {
            arrayList.add("0");
            arrayList.add("1");
            arrayList.add("2");
            arrayList.add("3");
        }

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
        select_spinner = (Spinner)findViewById(R.id.select_spinner);
        select_spinner.setAdapter(arrayAdapter);
        select_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                submeshIndex = Integer.parseInt((String) arrayList.get(position));
                Toast.makeText(shopping_arcore.this,  arrayList.get(position) +"아이템선택함", Toast.LENGTH_SHORT).show();
                partSelectNum = position;
                modelRender(uri);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // 부분선택하기 누르면 나오는거
        sceneView = findViewById(R.id.sceneView2);
        PLACE_SELECT_PART = findViewById(R.id.PLACE_SELECT_PART);
        // 버튼 눌러서 색상 바뀌게 하는거
        select_change_BTN = findViewById(R.id.select_change_BTN);
        select_change_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여기서 색상이 바뀌어야함
                // 일단 테스트로 0 , 1, 2 까지만 되게 하자
                PLACE_SELECT_PART.setVisibility(View.VISIBLE);
                sceneView.setVisibility(View.VISIBLE);
                modelRender(uri);
            }
        });
        part_select_OK = findViewById(R.id.part_select_OK);
        part_select_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PLACE_SELECT_PART.setVisibility(View.GONE);
                sceneView.setVisibility(View.GONE);
                submeshIndex = partSelectNum;
                highlightedAndyRenderable = andyRenderable.makeCopy();
                highlightedAndyRenderable.getMaterial(submeshIndex).setFloat3("baseColorTint", new Color(android.graphics.Color.red(selectedColor), android.graphics.Color.green(selectedColor), android.graphics.Color.blue(selectedColor), android.graphics.Color.alpha(selectedColor)));
                hitNode.setRenderable(highlightedAndyRenderable);
                colorPanel.setVisibility(View.GONE);
            }
        });
        part_select_cancle = findViewById(R.id.part_select_cancle);
        part_select_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PLACE_SELECT_PART.setVisibility(View.GONE);
                sceneView.setVisibility(View.GONE);
                colorPanel.setVisibility(View.GONE);
            }
        });

        // 장바구니 추가하기 시작 ------------------------------------------------------------------------
        select_cart_BTN = findViewById(R.id.select_cart_BTN);
        select_cart_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"노드에 사용된거 알아내기2" + findSelectedValue);
                shopping_arcore_cart = new shopping_arcore_cart();
                shopping_arcore_cart.cartCkeck(findSelectedValue);
                bottom_sheet_shopping bottomSheet = new bottom_sheet_shopping();
                // 번들로 데이터 보내기
                Bundle bundle = new Bundle();
                bundle.putString("title", shopping_arcore_cart.cartCkeck(currentSelectedAnchorNode.getRenderable().getSubmeshName(0)).getFurname());
                bundle.putString("price", String.valueOf(shopping_arcore_cart.cartCkeck(currentSelectedAnchorNode.getRenderable().getSubmeshName(0)).getFurprice()));
                bundle.putString("ImageResource", shopping_arcore_cart.cartCkeck(currentSelectedAnchorNode.getRenderable().getSubmeshName(0)).getFurImage());
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");

            }
        });
        // 장바구니 추가하기 끝 ------------------------------------------------------------------------

        // 언두관련 시작 -----------------------------------------------------------------------------

        // 언두관련 로직
        //
        undoButton = findViewById(R.id.undoButton);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //언두 버튼을 누르면
                // 일단 지우고
//                removeAnchorNode(currentSelectedAnchorNode2);

                if(anchorNodeList.size() > 0 ){
                    if(anchorNodeList.get(anchorNodeList.size()-1).equals(anchorNodeList2.get(anchorNodeList2.size()-1))) {
                        // 첫번째의 어레이 리스트 인덱스가 하나라도 남아있을때
                        // 첫번째와 두번째 어레이리스의 내용이 같을때
                        Log.d(TAG,"노드리스트 2  " + currentSelectedAnchorNode2);
                        arFragment.getArSceneView().getScene().removeChild(currentSelectedAnchorNode2);
                        anchorNodeList.remove(currentSelectedAnchorNode2);
                        anchorNodeList2.remove(currentSelectedAnchorNode2);
                        currentSelectedAnchorNode2.getAnchor().detach();
                        currentSelectedAnchorNode2.setParent(null);
                        Log.d(TAG,"노드테스트  " + "같다");
                        currentSelectedAnchorNode2 = anchorNodeList.get(anchorNodeList.size()-1);
                        Log.d(TAG,"노드리스트 후2  " + currentSelectedAnchorNode2);
                    } else {
                        if(anchorNodeList2.size() > 0){
                            // 두번째 어레이리스트의 인덱스가 1개라도 남아있을때
                            currentSelectedAnchorNode2 = anchorNodeList2.get(anchorNodeList.size()-1);
                            currentSelectedAnchorNode2.setParent(arFragment.getArSceneView().getScene());
                            currentSelectedAnchorNode2.setRenderable(andyRenderable);
                            anchorNodeList.add(currentSelectedAnchorNode2);
                            Log.d(TAG,"노드테스트  " + "다르다");
                        } else {
                            // 두번째 어레이리스트의 인덱스가 하나도 없을때
                            Log.d(TAG,"어레이리스트 없음");
                        }
                    }

                } else {

                    Log.d(TAG,"노드리스트 후  " + anchorNodeList.size());
                    Log.d(TAG,"노드리스트 후2  " + currentSelectedAnchorNode2);
                }

            }
        });

        // 언두관련 끝 ------------------------------------------------------------------------------



        //움직이기 버튼
        moveToModel_btn = findViewById(R.id.moveToModel_btn);
        moveToModel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToNode(currentSelectedAnchorNode);
            }
        });


        // -------------------------------------------------------------------------------------
        // 모델 선택 및 모델 초기화
        // -------------------------------------------------------------------------------------

        //+버튼 누르고 다른가구 배치하기 나오게 하기
        placeFurniture = findViewById(R.id.PLACE_FURNITURE_SELECT);

        PLACE_FURNITURE = findViewById(R.id.PLACE_FURNITURE);
        PLACE_FURNITURE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PLACE_FURNITURE.setVisibility(View.GONE);
                clearButton.setVisibility(View.GONE);
                captureBtn.setVisibility(View.GONE);
//                imageView.setVisibility(View.GONE);
                placeFurniture.setVisibility(View.VISIBLE);
                colorPanel.setVisibility(View.GONE);
            }
        });

        // 리사이클러뷰관련
        item_arcore_image_recycle = findViewById(R.id.item_arcore_image_recycle);
        shop_list_arcore = new shop_list_arcore();
        try {
            shop_list_arcore.execute("http://13.125.62.22/h_getShopList.php").get();
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplication());
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
            item_arcore_image_recycle.setLayoutManager(mLayoutManager);
            shopping_arcore_list_adapter = new shopping_arcore_list_adapter(shopArcoreReDatalist);
            item_arcore_image_recycle.setAdapter(shopping_arcore_list_adapter);
            shopping_arcore_list_adapter.setModelSelectListener(this);
        } catch (Exception e){

        }



        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


        //캡쳐하기 버튼
        captureBtn = findViewById(R.id.CAPTURE_BUTTON);
        captureBtn.setOnClickListener(view -> takePhoto());
    }

    // 부분적으로 색상 바꾸는데에 쓰이는거 ---------------------------------------------------------------
    private void modelRender (Uri uri){
        ModelRenderable.builder().setSource(this,uri)
                .build().thenAccept(this::placeAndy);
    }
    private void placeAndy(ModelRenderable andy) {
        andyNode = new Node();
        andyNode.setRenderable(andy);
        andy.getMaterial(partSelectNum).setFloat3("baseColorTint", new Color(android.graphics.Color.red(selectedColor), android.graphics.Color.green(selectedColor), android.graphics.Color.blue(selectedColor), android.graphics.Color.alpha(selectedColor)));
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

    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     *
     * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     *
     * <p>Finishes the activity if Sceneform can not run
     */
    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Log.i(TAG, "onCreate: ");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Log.i(TAG, "onCreate: ");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }
    @Override
    public void onButtonClicked(String text) {
        // 바텀네비게이션에서 클릭하면 값이 여기로 들어오는거같다.
    // 아마 모델클릭햇을때 가져오는 값을 여기다가 넣어주면 될듯..
        if (selectModel.equals("barChair")) {
            uri = Uri.parse("raw/bar_chair_2.sfb");
        } else if (selectModel.equals("andy")){
            uri = Uri.parse("andy.sfb");
        }

}

    // 사진찍을때 사용하는 파일 이름
    private String generateFilename() {
        String date =
                new SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.getDefault()).format(new Date());
        return Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + File.separator + "Sceneform/" + date + "_screenshot.jpg";
    }
    // 비트맵을 파일에 쓸때쓴다.
    private void saveBitmapToDisk(Bitmap bitmap, String filename) throws IOException {

        File out = new File(filename);
        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(filename);
             ByteArrayOutputStream outputData = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputData);
            outputData.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            throw new IOException("Failed to save bitmap to disk", ex);
        }
    }
    // 사진찍는 메소드
    private void takePhoto() {
        final String filename = generateFilename();
        ArSceneView view = arFragment.getArSceneView();

        // Create a bitmap the size of the scene view.
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Create a handler thread to offload the processing of the image.
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();
        // Make the request to copy.
        PixelCopy.request(view, bitmap, (copyResult) -> {
            if (copyResult == PixelCopy.SUCCESS) {
                try {
                    saveBitmapToDisk(bitmap, filename);
                } catch (IOException e) {
                    Toast toast = Toast.makeText(this, e.toString(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                File photoFile = new File(filename);

                Uri photoURI = FileProvider.getUriForFile(this,
                        this.getPackageName() + ".ar.codelab.name.provider",
                        photoFile);
                Log.i(TAG, "takePhoto: "+ photoURI);
                Intent intent = new Intent(Intent.ACTION_VIEW, photoURI);
                intent.setDataAndType(photoURI, "image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);

                // 갤러리에 사진 저장하는 코드
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(filename);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                        "사진이 저장되었습니다!", Snackbar.LENGTH_LONG);

                snackbar.show();
            } else {
                Toast toast = Toast.makeText(this,
                        "Failed to copyPixels: " + copyResult, Toast.LENGTH_LONG);
                toast.show();
            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));
    }
    // 앵커를 지우는 메소드 ----------------------------------------------------------------------------
    private void removeAnchorNode(AnchorNode nodeToremove) {
        //Remove an anchor node
        if (nodeToremove != null) {
            arFragment.getArSceneView().getScene().removeChild(nodeToremove);
            anchorNodeList.remove(nodeToremove);
            nodeToremove.getAnchor().detach();
            nodeToremove.setParent(null);
            nodeToremove = null;
//            node.setParent(null);

            Toast.makeText(this, "오브젝트를 지웠습니다.", Toast.LENGTH_SHORT).show();
            colorPanel.setVisibility(View.GONE);
            PLACE_FURNITURE.setVisibility(View.VISIBLE); // 가구 선택하는 패널 업애기
            clearButton.setVisibility(View.VISIBLE);
            captureBtn.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "오브젝트가 널포인트 입니다.", Toast.LENGTH_SHORT).show();
        }
    }
    //앵커를 지우는 메소드 ----------------------------------------------------------------------------


    // 노드의 색상을 바꿔주는 메소드 --------------------------------------------------------------------
    private void colorChange(AnchorNode colorchange) {
        System.out.println("모델값"+selectModel);
        System.out.println("서브매쉬인덱스값"+submeshIndex);
        highlightedAndyRenderable = andyRenderable.makeCopy();
        highlightedAndyRenderable.getMaterial(submeshIndex).setFloat3("baseColorTint", new Color(android.graphics.Color.red(selectedColor), android.graphics.Color.green(selectedColor), android.graphics.Color.blue(selectedColor), android.graphics.Color.alpha(selectedColor)));
        hitNode.setRenderable(highlightedAndyRenderable);

    }
    // 노드의 색상을 바꿔주는 메소드 --------------------------------------------------------------------


    // 모델을 클릭했을때 나오는곳 ----------------------------------------------------------------------
    private void handleOnTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
        Log.d(TAG,"handleOnTouch");

        arFragment.onPeekTouch(hitTestResult, motionEvent);


        if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
            return;
        }

        // 노드를 터치 했는지 체크함
        if (hitTestResult.getNode() != null) {
            Log.i(TAG, "노드 동일 확인 힛테스트리절트" + hitTestResult.getNode().toString());
            Log.d(TAG,"handleOnTouch hitTestResult.getNode() != null");
            hitNode = new Node();
            hitNode = hitTestResult.getNode();

            if (hitNode.getRenderable() == andyRenderable) {
                Toast.makeText(this, "가구를 선택했습니다."  , Toast.LENGTH_SHORT).show();

                currentSelectedAnchorNode = (AnchorNode) hitNode;
                Log.d(TAG,"노드에 사용된거 알아내기" + hitNode.getRenderable().getSubmeshName(0));
                findSelectedValue = (String)hitNode.getRenderable().getSubmeshName(0);
                Log.d(TAG,"노드에 사용된거 알아내기" + findSelectedValue);
                Log.i(TAG, "노드 동일 확인 힛노드" + currentSelectedAnchorNode.toString());

                // 클릭했을때 패널없애기 
                colorPanel.setVisibility(View.VISIBLE);
                PLACE_FURNITURE.setVisibility(View.GONE); // 가구 선택하는 패널 업애기
                clearButton.setVisibility(View.GONE);
                captureBtn.setVisibility(View.GONE);

            } else {
                Toast.makeText(this, "색깔있는거 클릭함 "  , Toast.LENGTH_SHORT).show();

                currentSelectedAnchorNode = (AnchorNode) hitNode;


                Log.d("널포인트","handleOnTouch hitTestResult.getNode() != null" +currentSelectedAnchorNode);

                colorPanel.setVisibility(View.VISIBLE);
                PLACE_FURNITURE.setVisibility(View.GONE); // 가구 선택하는 패널 업애기
                clearButton.setVisibility(View.GONE);
                captureBtn.setVisibility(View.GONE);
            }

        return;
    } else{
        // 제일 처음에 노드를 추가하는 곳
        Log.d(TAG,"adding Andy in fornt of camera");

        Frame frame = arFragment.getArSceneView().getArFrame();
        Session session = arFragment.getArSceneView().getSession();
        Anchor newMarkAnchor = session.createAnchor(
                    frame.getCamera().getPose()
                            .compose(Pose.makeTranslation(0, 0, -1f))
                            .extractTranslation());
        AnchorNode addedAnchorNode = new AnchorNode(newMarkAnchor);
        addedAnchorNode.setRenderable(andyRenderable);
        addAnchorNode(addedAnchorNode);
        currentSelectedAnchorNode2 = addedAnchorNode;
            Log.i(TAG, "노드 동일 확인 커런트노드" + currentSelectedAnchorNode2.toString());


        }
    }
    // 모델을 클릭했을때 나오는곳 ----------------------------------------------------------------------

    // 앵커 노드를 더하는 메소드 ----------------------------------------------------------------------
    private void addAnchorNode(AnchorNode nodeToAdd) {
        //Add an anchor node
        nodeToAdd.setParent(arFragment.getArSceneView().getScene());
        anchorNodeList.add(nodeToAdd);
        anchorNodeList2.add(nodeToAdd);
        numberOfAnchors++;
        Log.d(TAG,"노드리스트 갯수 확인 " + anchorNodeList.size());
//        Log.d(TAG,"노드리스트 갯수 확인 " + numberOfAnchors);
    }
    // 앵커 노드를 더하는 메소드 ----------------------------------------------------------------------

    // 모델을 움직이는 메소드 ------------------------------------------------------------------------
    private void moveToNode(AnchorNode nodeToMove) {
        ; // As you said i have added one transformablenode which will allow transformation.
        transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        transformableNode.setRenderable(andyRenderable);
        transformableNode.select();
//
//        addAnchorNode(addedAnchorNode);
//        currentSelectedAnchorNode = addedAnchorNode;
    }

    // 데이터 받아오는곳
    class shop_list_arcore extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            Log.e("리사이클러", "4");
            StringBuilder jsonHtml = new StringBuilder();
            try {
                // 연결 url 설정
                URL url = new URL(params[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 연결되었으면.
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    // 연결되었음 코드가 리턴되면.
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for (; ; ) {
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
                            if (line == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println(jsonHtml.toString());
            String str2 = jsonHtml.toString();
            return jsonHtml.toString();
        }

        protected void onPostExecute(String str) {
            try {
                // PHP에서 받아온 JSON 데이터를 JSON오브젝트로 변환
                JSONObject jObject = new JSONObject(str);
                // results라는 key는 JSON배열로 되어있다.

                JSONArray results = jObject.getJSONArray("status");
                try {
                    JSONArray arr = new JSONArray(results);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // 지슨을 이용해서 리사이클러뷰에 보여줄 리스트 만들기
                int index = 0;
                while (index < results.length()) {
                    String id = results.getJSONObject(index).getString("id");
                    String Furname = results.getJSONObject(index).getString("Furname");
                    int Furprice = Integer.parseInt(results.getJSONObject(index).getString("Furprice"));
                    String Furcopy = results.getJSONObject(index).getString("Furcopy");
                    String Furcategory = results.getJSONObject(index).getString("Furcategory");
                    String FurArck = results.getJSONObject(index).getString("FurArck");
                    String FurImage = results.getJSONObject(index).getString("FurImage");
                    String sfa = results.getJSONObject(index).getString("sfa");
                    String sellerName = results.getJSONObject(index).getString("sellerName");

                    shopArcoreReData= new shopping_list_class(Furname,Furprice,Furcopy,Furcategory,FurArck,FurImage,sfa,sellerName);
                    shopArcoreReDatalist.add(shopArcoreReData);
                    Log.e("제이슨확인2", String.valueOf(shopArcoreReDatalist.get(0).getFurprice()));
                    index++;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    // 리사이클러뷰에서 오버라이드해서 받아온 콜백값 ---------------------------------------------------------
    @Override
    public void onModelSelected(String Furname, String Furtsfa) {
        selectModel = Furtsfa;
                uri = Uri.parse(selectModel);
                Log.i(TAG, "onCreate: 인텐트  " + uri);
                ModelRenderable.builder()
                        .setSource(getApplicationContext(),uri)
                        .build()
                        .thenAccept(renderable -> andyRenderable = renderable)
                        .exceptionally( // 모델렌더링이 안됄때 나오는 토스트
                                throwable -> {
                                    Toast toast =
                                            Toast.makeText(getApplicationContext(), "Unable to load andy renderable", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    return null;
                                });

                placeFurniture.setVisibility(View.GONE); // 가구 선택하는 패널 업애기
                PLACE_FURNITURE.setVisibility(View.VISIBLE);
                clearButton.setVisibility(View.VISIBLE);
                captureBtn.setVisibility(View.VISIBLE);
//                imageView.setVisibility(View.VISIBLE);
                colorPanel.setVisibility(View.GONE);

    }

}



