package fu.wanke.opengl.camera1;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Camera1Helper {


    private final MediaRecorder mMediaRecorder;

    private Camera camera;
    private Context mContext;

    private Camera.CameraInfo mFrontCameraInfo = null;
    private int mFrontCameraId = -1;

    private Camera.CameraInfo mBackCameraInfo = null;
    private int mBackCameraId = -1;

    private int mCameraId;
    private Camera.CameraInfo mCameraInfo;
    private SurfaceHolder mHolder;
    private Camera.Size mSelectSize;
    private boolean isRecorder;

    private boolean mIsFront;

    public Camera1Helper(Context mContext) {
        this.mContext = mContext;
        initCameraInfo();
        mMediaRecorder = new MediaRecorder();
    }



    public void open(SurfaceHolder holder , boolean isFront) {

        this.mHolder = holder;
        this.mIsFront = isFront;
        if (isFront) {
            mCameraId  = mFrontCameraId;
            mCameraInfo = mFrontCameraInfo;
        } else {
            mCameraId  = mBackCameraId;
            mCameraInfo = mBackCameraInfo;
        }

        camera = Camera.open(mCameraId);

        mSelectSize = selectPreviewSize(camera.getParameters());

        try {

            camera.setPreviewDisplay(holder);

            camera.setDisplayOrientation(getCameraDisplayOrientation(mCameraInfo));

            Camera.Parameters parameters = camera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FLASH_MODE_AUTO); //对焦设置为自动
            //其他参数配置略...
            camera.setParameters(parameters);//添加参数

            camera.startPreview();

            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public void setPreviewListener(Camera.PreviewCallback callback) {
        camera.setPreviewCallback(callback);
    }


    /**
     * 开启录制视频
     */
    public void startRecorder(){
        if (!isRecorder) {//如果不在录制视频
            camera.stopPreview();//暂停相机预览
            configMedioRecorder();//再次配置MedioRecorder
            try {
                mMediaRecorder.prepare();//准备录制
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaRecorder.start();//开始录制
            isRecorder = true;
        }

    }

    /**
     * 停止录制视频
     */
    public void stopRecorder(){
        if (isRecorder){ //如果在录制视频
            mMediaRecorder.stop();//暂停录制
            mMediaRecorder.reset();//重置,将MediaRecorder调整为空闲状态
            isRecorder = false;
            try {
                camera.setPreviewDisplay(mHolder);//重新设置预览SurfaceTexture
                camera.startPreview(); //重新开启相机预览
                camera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }




    private void initCameraInfo() {
        int numberOfCameras = Camera.getNumberOfCameras();// 获取摄像头个数
        for (int cameraId = 0; cameraId < numberOfCameras; cameraId++) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                // 后置摄像头信息
                mBackCameraId = cameraId;
                mBackCameraInfo = cameraInfo;
            } else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                // 前置摄像头信息
                mFrontCameraId = cameraId;
                mFrontCameraInfo = cameraInfo;
            }
        }
    }

    private int getCameraDisplayOrientation(Camera.CameraInfo cameraInfo) {
        int rotation = ((Activity)mContext).getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (cameraInfo.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (cameraInfo.orientation - degrees + 360) % 360;
        }
        return result;
    }


    private void configMedioRecorder(){
        File saveRecorderFile = new File(mContext.getExternalCacheDir(),"CameraRecorder.mp4");
        if (saveRecorderFile.exists()){
            saveRecorderFile.delete();
        }
        camera.unlock();
        mMediaRecorder.setCamera(camera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);//设置音频源
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);//设置视频源
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);//设置音频输出格式
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);//设置音频编码格式
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);//设置视频编码格式
        mMediaRecorder.setVideoSize(mSelectSize.width,mSelectSize.height);//设置视频分辨率
        mMediaRecorder.setVideoEncodingBitRate(8*1920*1080);//设置视频的比特率
        mMediaRecorder.setVideoFrameRate(60);//设置视频的帧率
        if (mIsFront) {
            mMediaRecorder.setOrientationHint(270);//设置视频的角度
        }  else {
            mMediaRecorder.setOrientationHint(90);//设置视频的角度
        }
        mMediaRecorder.setMaxDuration(60*1000);//设置最大录制时间
//        Surface surface = new Surface(mTextureview.getSurfaceTexture());
        mMediaRecorder.setPreviewDisplay(mHolder.getSurface());//设置预览
        mMediaRecorder.setOutputFile(saveRecorderFile.getAbsolutePath());//设置文件保存路径
        mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() { //录制异常监听
            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                mMediaRecorder.stop();
                mMediaRecorder.reset();
                try {
                    mMediaRecorder.setPreviewDisplay(mHolder.getSurface());//设置预览
                    camera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 计算获取预览尺寸
     * @param parameters
     * @return
     */
    private Camera.Size selectPreviewSize(Camera.Parameters parameters){
        List<Camera.Size> previewSizeList =  parameters.getSupportedPreviewSizes();
        if (previewSizeList.size() == 0){
            return null;
        }

        Camera.Size currentSelectSize = null;
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int deviceWidth =displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;
        for (int i = 1; i < 41 ; i++){
            for(Camera.Size itemSize : previewSizeList){
//                Log.e(TAG, "selectPreviewSize: itemSize 宽="+itemSize.width+"高"+itemSize.height);
                if (itemSize.height > (deviceWidth - i*5) && itemSize.height < (deviceWidth + i*5)){
                    if (currentSelectSize != null){ //如果之前已经找到一个匹配的宽度
                        if (Math.abs(deviceHeight-itemSize.width) < Math.abs(deviceHeight - currentSelectSize.width)){ //求绝对值算出最接近设备高度的尺寸
                            currentSelectSize = itemSize;
                            continue;
                        }
                    }else {
                        currentSelectSize = itemSize;
                    }

                }

            }
        }
        return currentSelectSize;
    }




}
