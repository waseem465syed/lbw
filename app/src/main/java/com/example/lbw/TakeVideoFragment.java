package com.example.lbw;


import android.Manifest;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lbw.MyLibraries.CommonRoutines;
import com.example.lbw.MyLibraries.MyConstants;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class TakeVideoFragment extends Fragment implements View.OnClickListener, SurfaceHolder.Callback {


    private ImageButton btnRecord;
    private SurfaceView svVideo;

    private Camera camera;
    private SurfaceHolder holder;


    public TakeVideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_take_video, container, false);

        btnRecord = v.findViewById(R.id.btnRecord);
        btnRecord.setOnClickListener(this);




        svVideo = v.findViewById(R.id.svVideo);

        //pop message to ask user to allow permission
        CommonRoutines.checkPermission(getActivity(), Manifest.permission.RECORD_AUDIO, "RECORD_AUDIO");
        CommonRoutines.checkPermission(getActivity(), Manifest.permission.CAMERA, "Camera");
        CommonRoutines.checkPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, "WRITE_EXTERNAL_STORAGE");

        holder = svVideo.getHolder();
//        holder.addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//
//            }
//        });

        holder.addCallback(this);
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.btnRecord:
                recordVideo();
                break;
        }

    }

    private void disableButtons() {
        btnRecord.setEnabled(false);

    }

    private void enableButtons() {
        btnRecord.setEnabled(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        releaseCameraAndPreview();

        try {
            //open camera
            camera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (camera != null) {

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                camera.setDisplayOrientation(90);
            }

            try {
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            camera.startPreview();
        } else {
            //display message in toast
            CommonRoutines.displayMessage(getContext(), null, "No camera hardware found", 0, Toast.LENGTH_SHORT);
        }

    }


    private void releaseCameraAndPreview() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        refreshCamera();
    }

    private void refreshCamera() {
        if (holder.getSurface() != null) {
            camera.stopPreview();

            try {
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            camera.startPreview();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        releaseCameraAndPreview();
    }


    private void recordVideo() {


        if (camera != null) {

            //disable the buttons
            disableButtons();

            //unlock the camera
            camera.unlock();

            //create a new instance of MediaRecorder
            final MediaRecorder recorder = new MediaRecorder();

            //set the recorder's camera
            recorder.setCamera(camera);

            //set the video and audio source
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

            //set the save format
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            //set the video and audio encoder
            recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

            //set the output file location
            recorder.setOutputFile(MyConstants.VIDEOPATH);

            //prepare the recording
            try {
                recorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("OluError" , "Error: " + e);
            }

            //display message toast message
            CommonRoutines.displayMessage(getContext(), null, "Recording, Please wait...", 0, Toast.LENGTH_LONG);

            //start the recording
            recorder.start();

            //create an handler
            Handler handle = new Handler();

            handle.postDelayed(new Runnable() {
                @Override
                public void run() {

                    //lock the camera
                    camera.lock();

                    //stop the recording
                    recorder.stop();

                    //release
                    recorder.release();

                    //display message toast message
                    CommonRoutines.displayMessage(getContext(), null, "Recording Finished", 0, Toast.LENGTH_LONG);

                    //enable buttons
                    enableButtons();
                }
            }, 5 * 1000);


        }
    }
}
