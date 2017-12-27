package com.example.android.securecam;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.List;
import java.util.ListIterator;

public class ImageCaptureActivity extends Activity {
    private static final String DEBUG_TAG = "StillImageActivity";
    final public static String STILL_IMAGE_FILE = "0.jpg";
    String photoFiles[] = {"0.jpg", "1.jpg", "2.jpg"};
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.still);
        final CameraSurfaceView cameraView = new CameraSurfaceView(getApplicationContext());
        FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
        frame.addView(cameraView);

        Button capture = (Button) findViewById(R.id.capture);
        capture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                capturePhoto(cameraView);
            }
        });
    }

    private void capturePhoto(CameraSurfaceView cameraView) {
        Log.v(DEBUG_TAG, "Requesting capture");
        cameraView.capture(new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.v("Still", "Image data received from camera");
                FileOutputStream fos;
                try {
                    //String pathForAppFiles = getFilesDir().getAbsolutePath();
                    //pathForAppFiles = pathForAppFiles + "/" + photoFiles[i];
                    fos = openFileOutput(photoFiles[i], MODE_PRIVATE);
                    fos.write(data);
                    fos.close();
                    //Log.d("Still image filename:", pathForAppFiles);
                    Toast.makeText(getApplicationContext(), "Picture saved. " + photoFiles[i], Toast.LENGTH_LONG).show();
                    i++;
                    if (i > 2){i = 0;}
                } catch (Exception e) {
                    Log.e("Still", "Error writing file", e);
                }
                camera.startPreview();
            }
        });
    }

    private class CameraSurfaceView extends SurfaceView implements
            SurfaceHolder.Callback {
        private Camera camera = null;
        private SurfaceHolder mHolder = null;

        @SuppressWarnings("deprecation")
        public CameraSurfaceView(Context context) {
            super(context);
            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed
            mHolder = getHolder();
            mHolder.addCallback(this);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            try {
                Camera.Parameters params = camera.getParameters();
                // not all cameras supporting setting arbitrary sizes
                List<Size> sizes = params.getSupportedPreviewSizes();
                Size pickedSize = getBestFit(sizes, width, height);
                if (pickedSize != null) {
                    params.setPreviewSize(pickedSize.width, pickedSize.height);
                    Log.d(DEBUG_TAG, "Preview size: (" + pickedSize.width + ","
                            + pickedSize.height + ")");
                    // even after setting a supported size, the preview size may
                    // still end up just being the surface size (supported or
                    // not)
                    camera.setParameters(params);
                }
                // set the orientation to standard portrait.
                // Do this only if you know the specific orientation (0,90,180,
                // etc.)
                // Only works on API Level 8+
                camera.setDisplayOrientation(90);
                camera.startPreview();
            } catch (Exception e) {
                Log.e(DEBUG_TAG, "Failed to set preview size", e);
            }
        }

        private Size getBestFit(List<Size> sizes, int width, int height) {
            Size bestFit = null;
            ListIterator<Size> items = sizes.listIterator();
            while (items.hasNext()) {
                Size item = items.next();
                if (item.width <= width && item.height <= height) {
                    if (bestFit != null) {
                        // if our current best fit has a smaller area, then we
                        // want the new one (bigger area == better fit)
                        if (bestFit.width * bestFit.height < item.width
                                * item.height) {
                            bestFit = item;
                        }
                    } else {
                        bestFit = item;
                    }
                }
            }
            return bestFit;
        }

        public void surfaceCreated(SurfaceHolder holder) {
            camera = Camera.open();
            try {
                camera.setPreviewDisplay(mHolder);
            } catch (Exception e) {
                Log.e(DEBUG_TAG, "Failed to set camera preview display", e);
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }

        public boolean capture(Camera.PictureCallback jpegHandler) {
            if (camera != null) {
                camera.takePicture(null, null, jpegHandler);
                return true;
            } else {
                return false;
            }
        }
    }
}