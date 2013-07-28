package com.parse.demo;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.parse.*;

import java.util.List;

/**
 * User: kingkingdubu
 * Date: 13. 7. 27
 * Time: 오전 10:33
 */
public class BattleActivity extends Activity {

    // Log용 TAG
    private static final String TAG = "HelloCamera";
    // ImageView 설정 이미지 약하게 크기. 1 / 8의 크기로 처리
    private static final int IN_SAMPLE_SIZE = 8;

    // 카메라 제어
    private Camera mCamera;
    // 촬영 사진보기
    private ImageView mImage;
    // 처리중 신고
    private boolean mInProgress;

    private ImageButton button;


    // 카메라 미리보기 SurfaceView의 리스너
    private SurfaceHolder.Callback mSurfaceListener = new SurfaceHolder.Callback()
    {
        public void surfaceCreated(SurfaceHolder holder)
        {
            // SurfaceView가 생성되면 카메라를 열

            for (int camNo = 0; camNo < Camera.getNumberOfCameras(); camNo++) {
                Camera.CameraInfo camInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(camNo, camInfo);
                if (camInfo.facing==(Camera.CameraInfo.CAMERA_FACING_FRONT)) {
                    mCamera = Camera.open(camNo);
                    Camera.Parameters parameters = mCamera.getParameters();
                    parameters.setRotation(270);
                    mCamera.setParameters(parameters);
                }
            }

            //mCamera = Camera.open();
            Log.i(TAG, "Camera opened");

            // SDK1.5에서 setPreviewDisplay이 IO Exception을 throw한다
            try
            {

                mCamera.setPreviewDisplay(holder);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder)
        {
            // SurfaceView가 삭제되는 시간에 카메라를 개방
            mCamera.release();
            mCamera = null;
            Log.i(TAG, "Camera released");
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
        {
            // 미리보기 크기를 설정
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(width, height);
            mCamera.setParameters(parameters);
            parameters.setRotation(270);
            mCamera.startPreview();
            Log.i(TAG, "Camera preview started");


            // auto save
            // button.callOnClick();
        }
    };

    // 버튼을 눌렀을 때 수신기
    private View.OnClickListener mButtonListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            if (mCamera != null && mInProgress == false)
            {
                // 이미지 검색을 시작한다. 리스너 설정
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                mCamera.takePicture(mShutterListener, // 셔터 후
                        null, // Raw 이미지 생성 후
                        mPicutureListener); // JPE 이미지 생성 후
                mInProgress = true;
            }
        }
    };

    private Camera.ShutterCallback mShutterListener = new Camera.ShutterCallback()
    {
        // 이미지를 처리하기 전에 호출된다.
        public void onShutter()
        {
            // 셔터 소리 재생 코드 생략


            Log.i(TAG, "onShutter");
        }

    };

    // JPEG 이미지를 생성 후 호출
    private Camera.PictureCallback mPicutureListener = new Camera.PictureCallback()
    {
        public void onPictureTaken(byte[] data, Camera camera)
        {
            Log.i(TAG, "Picture Taken");
            if (data != null)
            {
                Log.i(TAG, "JPEG Picture Taken");

                // 처리하는 이미지의 크기를 축소
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = IN_SAMPLE_SIZE;
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                // 이미지 뷰 이미지 설정
                mImage.setImageBitmap(bitmap);
                // 정지된 프리뷰를 재개
                camera.startPreview();
                // 처리중 플래그를 떨어뜨림
                mInProgress = false;

                savePhoto(data);
            }
        }

    };


    private  void savePhoto(final byte[] data){



        new AsyncTask<Object,Object,String>() {
            List<ParseObject> photos;


            @Override
            protected String doInBackground(Object... params) {

                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("BattlePhoto");
                query.whereContains("installationId", ParseInstallation.getCurrentInstallation().getInstallationId());
                try {
                    photos  = query.find();
                    //ParseObject obj  = todos.get(0);

                    /*
                    ParseFile applicantResume = (ParseFile)obj.get("profileFile");
                    applicantResume.getDataInBackground(new GetDataCallback() {
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                // data has the bytes for the resume
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = IN_SAMPLE_SIZE;
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                                bitmap.getHeight();
                            } else {
                                // something went wrong
                            }
                        }
                    });
                    */

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return null;

            }

            @Override
            protected void onPostExecute(String s) {

                ParseObject parseObject;
                if(photos.size() == 0 ){
                    // insert
                    parseObject = new ParseObject("BattlePhoto");
                }else{
                    // update
                    parseObject  = photos.get(0);
                }

                ParseFile file = new ParseFile("profileaa.png", data);
                parseObject.put("installationId", ParseInstallation.getCurrentInstallation().getInstallationId());
                parseObject.put("profileFile", file);
                parseObject.saveInBackground();
            }
        }.execute();


    }



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battle);

        mImage = (ImageView) findViewById(R.id.image_view);

        SurfaceView surface = (SurfaceView) findViewById(R.id.surface_view);
        SurfaceHolder holder = surface.getHolder();

        // SurfaceView 리스너를 등록
        holder.addCallback(mSurfaceListener);
        // 외부 버퍼를 사용하도록 설정
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // 셔터의 버튼에 리스너를 등록
        button = (ImageButton) findViewById(R.id.shutter);
        button.setOnClickListener(mButtonListener);

        // push notic regist
        ParseInstallation.getCurrentInstallation().saveInBackground();
        PushService.setDefaultPushCallback(this, BattleActivity.class);
        PushService.subscribe(this, "BattleActivity", BattleActivity.class);
        ParseAnalytics.trackAppOpened(getIntent());


        /*
        byte[] data = "Working at Parse is great!".getBytes();
        ParseFile file = new ParseFile("resume.txt", data);

        ParseObject jobApplication = new ParseObject("BattlePhoto");
        jobApplication.put("installationId", ParseInstallation.getCurrentInstallation().getInstallationId());
        jobApplication.put("profileFile", file);
        jobApplication.saveInBackground();
        */

    }
}
