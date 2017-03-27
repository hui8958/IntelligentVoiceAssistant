package edu.hui.vassistant.activities;

import java.io.BufferedOutputStream;
import android.hardware.Camera.PictureCallback; 
import android.hardware.Camera.ShutterCallback;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import edu.hui.vassistant.R;

import edu.hui.vassistant.supports.DefaultResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

public class CameraActivity extends Activity implements OnInitListener,
		OnClickListener, MediaScannerConnectionClient {

	// MySurfaceView mySurface;
	boolean isClicked = false;
	String path = "";
	private MediaScannerConnection mMs = null;
	String commandResult = "";
	String keywords[] = { "cheese", "chase" };
	boolean speech = true;
	private SpeechRecognizer sr = null;
	private ImageButton ib = null;
	private TextToSpeech mTts = null;
	private ImageButton sb = null;
	Camera mCamera;
	int numberOfCameras;
	int cameraCurrentlyLocked;
	int defaultCameraId;
	int frontCameraID;
	private Preview mPreview;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cameraview);
		// mySurface = new MySurfaceView(this);
		FrameLayout f1 = (FrameLayout) findViewById(R.id.frameLayoutCamera);
		ib = (ImageButton) findViewById(R.id.imageButtonCamera);
		sb = (ImageButton) findViewById(R.id.imageButtonSwitch);
		ib.setOnClickListener(this);
		sb.setOnClickListener(this);
		mPreview = new Preview(this);
		
		f1.addView(mPreview);
		System.out.println(f1.getChildCount());

		f1.bringChildToFront(ib);
		f1.bringChildToFront(sb);
		// setContentView(mySurface);
		numberOfCameras = Camera.getNumberOfCameras();
		CameraInfo cameraInfo = new CameraInfo();
		for (int i = 0; i < numberOfCameras; i++) {
			Camera.getCameraInfo(i, cameraInfo);
			if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
				defaultCameraId = i;
			}else if(cameraInfo.facing==CameraInfo.CAMERA_FACING_FRONT){
				frontCameraID = i;
			}
		
		}

		mMs = new MediaScannerConnection(this, this);
		mPreview.setOnClickListener(this);
		if (speech) {

			initspeech();
		}
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, 0);
		mCamera = Camera.open();
		setDisplayOrientation(mCamera,90);
		cameraCurrentlyLocked = defaultCameraId;
		mPreview.setCamera(mCamera,defaultCameraId);

	}



	public void initspeech() {
		sr = SpeechRecognizer.createSpeechRecognizer(this);
		sr.setRecognitionListener(new speechListener());
	}

	

	public Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mMs.connect();
				unbindService(mMs);
				finishCamera();
				break;
			case 2:

				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra("calling_package", "VoiceIME");
				intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
				// long wait = 5000;
				// intent.putExtra(
				// RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,
				// wait);
				Toast.makeText(CameraActivity.this, "Start listening:",
						Toast.LENGTH_SHORT).show();
				Log.v("cameraSpeech", "Start listening:");
				sr.startListening(intent);

				break;

			}

		}
	};
	public void onDestory(){
		if(mTts!=null){
			mTts.shutdown();
		}
	}

	private void finishCamera() {
		Intent i = new Intent();
		Bundle b = new Bundle();
		b.putString("path", path);
		i.putExtras(b);
		sr.destroy();
		mTts.shutdown();
		this.setResult(1, i);
		finish();
		Log.v("cameraGivePath", path);
		
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0 instanceof ImageButton) {
			ImageButton ib = (ImageButton) arg0;
			switch (ib.getId()) {
			case R.id.imageButtonCamera:
				mTts.speak("Say cheese!", TextToSpeech.QUEUE_ADD, null);
				while (mTts.isSpeaking()) {

				}
				Message msg = new Message();
				msg.what = 2;
				myHandler.sendMessage(msg);

				break;
			case R.id.imageButtonSwitch:
				if (numberOfCameras == 1) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage("Only one camera aviliable")
							.setNeutralButton("Close", null);
					AlertDialog alert = builder.create();
					alert.show();
					return;
				}

				// OK, we have multiple cameras.
				// Release this camera -> cameraCurrentlyLocked
				if (mCamera != null) {
					mCamera.stopPreview();
					mPreview.setCamera(null,0);
					mCamera.release();
					mCamera = null;
				}

				// Acquire the next camera and request Preview to reconfigure
				// parameters.
				mCamera = Camera.open((cameraCurrentlyLocked + 1)
						% numberOfCameras);
				cameraCurrentlyLocked = (cameraCurrentlyLocked + 1)
						% numberOfCameras;
				mPreview.switchCamera(mCamera,cameraCurrentlyLocked);
				setDisplayOrientation(mCamera,90);
				// Start the preview
				mCamera.startPreview();
				return;

			}
			
		} else {
			if (!isClicked) {

				takePicture() ;
				isClicked = true;
			} else {
				mPreview.voerTack();
				isClicked = false;
			}
		}
	}

	public void onInit(int status) {
		// TODO Auto-generated method stub
		if (status == TextToSpeech.SUCCESS) {
			int result = mTts.setLanguage(Locale.US);
		
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED)
			
			{
				Log.v("", "Language is not available");
				//
			} else {
			
			}
		} else {
			System.out.println("failed to init");
		}
	}

	public void takePicture() {
		mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
	}
	 ShutterCallback shutterCallback = new ShutterCallback() {  
	        public void onShutter() { 
	        } 
	    }; 
	 PictureCallback rawCallback = new PictureCallback() {  
	        public void onPictureTaken(byte[] data, Camera camera) { 
	        } 
	    }; 
	 PictureCallback jpegCallback = new PictureCallback() {  
	        public void onPictureTaken(byte[] data, Camera camera) { 
	        	try {

					File sd = Environment.getExternalStorageDirectory();

					String folderpath = sd.getPath() + "/IMG";
					File folder = new File(folderpath);

					if (!folder.exists())
						folder.mkdir();

					int files = folder.list().length;
					
					int fileNumber = ++files;

					File file = new File("/sdcard/IMG/IMG_" + fileNumber
							+ ".jpg");

					path = "/sdcard/IMG/IMG_" + fileNumber + ".jpg";

					Bitmap bm = BitmapFactory.decodeByteArray(data, 0,
							data.length);
					System.out.println("name ==" + file.getName());

					BufferedOutputStream bos = new BufferedOutputStream(
							new FileOutputStream(file));

					bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);

					bos.flush();

					bos.close();
					Message msg = new Message();
					msg.what = 1;

					myHandler.sendMessage(msg);

				} catch (Exception e) {

					e.printStackTrace();

				}

	           
	        } 
	    }; 
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			switch (resultCode) {
			case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:
			{
				mTts = new TextToSpeech(this, this);
				Log.v("", "TTS Engine is installed!");

			}

				break;
			case TextToSpeech.Engine.CHECK_VOICE_DATA_BAD_DATA:

			case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA:

			case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_VOLUME:

			{

				Log.v("", "Need language stuff:" + resultCode);
				Intent dataIntent = new Intent();
				dataIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(dataIntent);

			}
				break;
			case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:

			default:
				Log.v("", "Got a failure. TTS apparently not available");
				break;
			}
		}
	}

	class speechListener implements RecognitionListener {
		public void onReadyForSpeech(Bundle params) {
//			Toast.makeText(CameraActivity.this, "onReadyForSpeech",
//					Toast.LENGTH_SHORT).show();
//			Log.v("cameraSpeech", "Listening");

		}

		public void onBeginningOfSpeech() {
			// Log.v("cameraSpeech", "onBeginningOfSpeech");

		}

		public void onRmsChanged(float rmsdB) {
			// Toast.makeText(MyStt3Activity.this, "onRmsChanged",
			// Toast.LENGTH_SHORT).show();
			// Log.v("cameraSpeech", String.valueOf(rmsdB));

		}

		public void onBufferReceived(byte[] buffer) {

			// Log.v("cameraSpeech", String.v);
		}

		public void onEndOfSpeech() {

			Log.v("cameraSpeech", "onEndOfSpeech");

		}

		public void onError(int error) {
			String s = "";
			Log.v("Error", s);
			// mText.setText(s);

		}

		public void onResults(Bundle results) {

			String recognizer_result = results.getStringArrayList(
					SpeechRecognizer.RESULTS_RECOGNITION).get(0);
			// mText.setText( recognizer_result);
			String result = recognizer_result;
			
			Log.v("cameraSpeech", "onResults");
			// Log.v("GetMessage", userInputMessage);
			if (KeywordSearch(result)) {
				Toast.makeText(CameraActivity.this, "Done!",
						Toast.LENGTH_SHORT).show();
				mTts.speak(DefaultResponse.getCameraSuccessSpeak(),
						TextToSpeech.QUEUE_ADD, null);
				takePicture() ;
				isClicked = true;
			}else{
				Toast.makeText(CameraActivity.this, "Please try again! I hear "+result,
						Toast.LENGTH_SHORT).show();
			}

		}

		public void onPartialResults(Bundle partialResults) {
			Log.v("cameraSpeech", "onPartialResults");
		}

		public void onEvent(int eventType, Bundle params) {
			Log.v("cameraSpeech", "onEvent");
		}

	}


	public boolean KeywordSearch(String word) {
		for (String a : keywords) {
			if (word.toLowerCase().contains(a.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public void onMediaScannerConnected() {
		// TODO Auto-generated method stub
		mMs.scanFile(path, null);

	}

	public void onScanCompleted(String path, Uri uri) {
		// TODO Auto-generated method stub
		mMs.disconnect();

	}
	private void setDisplayOrientation(Camera camera, int angle) {
		Method downPolymorphic;

		try {

			downPolymorphic = camera.getClass().getMethod(
					"setDisplayOrientation", new Class[] { int.class });
			if (downPolymorphic != null)
				downPolymorphic.invoke(camera, new Object[] { angle });

		} catch (Exception e) {
		}
	}

}


class Preview extends ViewGroup implements SurfaceHolder.Callback,AutoFocusCallback {
    private final String TAG = "Preview";

    Timer timer=null; 
              

    SurfaceView mSurfaceView;
    SurfaceHolder mHolder;
    Size mPreviewSize;
    List<Size> mSupportedPreviewSizes;
    Camera mCamera;
    boolean autoFocus = false;
    Preview(Context context) {
        super(context);

        mSurfaceView = new SurfaceView(context);
        addView(mSurfaceView);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

	public void voerTack() {
		// TODO Auto-generated method stub
		mCamera.startPreview();
//		if (autoFocus) {
//			mCamera.autoFocus(this);
//		}
	}

	public void setCamera(Camera camera,int id) {
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		
		android.hardware.Camera.getCameraInfo(id, cameraInfo);
		if (cameraInfo.facing != CameraInfo.CAMERA_FACING_FRONT) {
			autoFocus = true;
			System.out.println("Back camera");
			mCamera = camera;
			timer = new Timer(); 
			timer.schedule(new TimerTask() {

				public void run() {
					// ���պ���
					autoFocus();
				}

			}, 3000);

		}else{
			System.out.println("Front camera");
			timer.cancel();
			mCamera = camera;
		}
		if (mCamera != null) {
			System.out.println("Camera inited!");
			mSupportedPreviewSizes = mCamera.getParameters()
					.getSupportedPreviewSizes();
			requestLayout();
        }
    }
	public void autoFocus(){
		System.out.println("Focusing");
		mCamera.autoFocus(this);
	}

    public void switchCamera(Camera camera,int id) {
       setCamera(camera,id);
       try {
           camera.setPreviewDisplay(mHolder);
       } catch (IOException exception) {
           Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
       }
       Camera.Parameters parameters = camera.getParameters();
       parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
       requestLayout();

       camera.setParameters(parameters);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // We purposely disregard child measurements because act as a
        // wrapper to a SurfaceView that centers the camera preview instead
        // of stretching it.
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		final int height = resolveSize(getSuggestedMinimumHeight(),
				heightMeasureSpec);
		setMeasuredDimension(width, height);

		if (mSupportedPreviewSizes != null) {
			mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width,
					height);
		}
	}

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed && getChildCount() > 0) {
            final View child = getChildAt(0);

            final int width = r - l;
            final int height = b - t;

            int previewWidth = width;
            int previewHeight = height;
//            if (mPreviewSize != null) {
//                previewWidth = mPreviewSize.width;
//                previewHeight = mPreviewSize.height;
//            }

            // Center the child SurfaceView within the parent.
            if (width * previewHeight > height * previewWidth) {
                final int scaledChildWidth = previewWidth * height / previewHeight;
                child.layout((width - scaledChildWidth) / 2, 0,
                        (width + scaledChildWidth) / 2, height);
            } else {
                final int scaledChildHeight = previewHeight * width / previewWidth;
                child.layout(0, (height - scaledChildHeight) / 2,
                        width, (height + scaledChildHeight) / 2);
            }
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw.
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
            }
        } catch (IOException exception) {
            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
        }
    }


    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        	timer.cancel();
        	
        }
    }


    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        requestLayout();

        mCamera.setParameters(parameters);
        mCamera.startPreview();
        

		
    }

	public void onAutoFocus(boolean success, Camera camera) {
		// TODO Auto-generated method stub
		
	}

}


