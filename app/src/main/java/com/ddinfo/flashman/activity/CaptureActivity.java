package com.ddinfo.flashman.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.task.WriteQRCodeActivity;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.utils.zxing.camera.CameraManager;
import com.ddinfo.flashman.utils.zxing.decoding.CaptureActivityHandler;
import com.ddinfo.flashman.utils.zxing.decoding.InactivityTimer;
import com.ddinfo.flashman.utils.zxing.decoding.RGBLuminanceSource;
import com.ddinfo.flashman.utils.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Initial the camera
 * 二维码扫码取货界面
 * @author Ryan.Tang
 */
public class CaptureActivity extends AppCompatActivity implements Callback {

  private static final int REQUEST_CODE_SCAN_GALLERY = 100;

  private CaptureActivityHandler handler;
  private ViewfinderView viewfinderView; //二维码扫描框
  private ImageButton leftBtn;   //返回按钮
  private TextView tvTitle;      //标题抬头
  private Button btnWriteQRCode; //手动扫描二维码按钮
  private boolean hasSurface;
  private Vector<BarcodeFormat> decodeFormats;
  private String characterSet;
  private InactivityTimer inactivityTimer;
  private MediaPlayer mediaPlayer;
  private boolean playBeep;      //播放扫码声音
  private static final float BEEP_VOLUME = 0.10f;
  private boolean vibrate;
  private ProgressDialog mProgress;
  private String photo_path;     //图片路径
  private Bitmap scanBitmap;
  private int cameraType;// 10: 取货 -1：仓库
  //	private Button cancelScanButton;

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scanner);
    //ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
    CameraManager.init(getApplication());
    viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_content);
    leftBtn = (ImageButton) findViewById(R.id.left_button);
    tvTitle = (TextView) findViewById(R.id.header_name);
    btnWriteQRCode = (Button) findViewById(R.id.btn_write_QRCode);
    final int type = getIntent().getExtras().getInt("type");
    if (getIntent().getExtras() == null) {
      cameraType = -1;
    } else {
      cameraType = getIntent().getExtras().getInt(ExampleConfig.TYPE_CAMERA, -1);
    }
    if (cameraType == 10) {
      btnWriteQRCode.setVisibility(View.VISIBLE); //显示手动扫描二维码
      btnWriteQRCode.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //0 取货扫描
          //1  扫一扫

          Bundle bundle = new Bundle();
          bundle.putInt("type",type);
          Intent intent  = new Intent(CaptureActivity.this,WriteQRCodeActivity.class);
          intent.putExtras(bundle);
          startActivity(intent);
          finish();
        }
      });
    } else {
      btnWriteQRCode.setVisibility(View.GONE); //隐藏手动扫描二维码
    }
    leftBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    /**
     * 1 TaskAllListActivity
     * 2 上级配送员
     * 0 TaskAllListFragment
     * -1 MainActivity
     */
    if (type == 1 || type == 2) {
      tvTitle.setText("扫一扫");
    } else {
      tvTitle.setText("到仓确认");
    }

    //		cancelScanButton = (Button) this.findViewById(R.id.btn_cancel_scan);
    hasSurface = false;
    inactivityTimer = new InactivityTimer(this);
  }

  @Override
  protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
    if (requestCode == RESULT_OK) {
      switch (requestCode) {
        case REQUEST_CODE_SCAN_GALLERY:
          //获取选中图片的路径
          Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
          if (cursor.moveToFirst()) {
            photo_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)); //图片的绝对路径
          }
          cursor.close();

          mProgress = new ProgressDialog(CaptureActivity.this);
          mProgress.setMessage("正在扫描...");
          mProgress.setCancelable(false); //设置Dialog不可取消
          mProgress.show();

          new Thread(new Runnable() {
            @Override
            public void run() {
              Result result = scanningImage(photo_path);
              if (result != null) {
                //                                Message m = handler.obtainMessage();
                //                                m.what = R.id.decode_succeeded;
                //                                m.obj = result.getText();
                //                                handler.sendMessage(m);
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("result", result.getText());
                //                                bundle.putParcelable("bitmap",result.get);
                resultIntent.putExtras(bundle);
                CaptureActivity.this.setResult(RESULT_OK, resultIntent);
              } else {
                Message m = handler.obtainMessage();
                m.what = R.id.decode_failed;
                m.obj = "Scan failed!";
                handler.sendMessage(m);
              }
            }
          }).start();
          break;
      }
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  /**
   * 扫描二维码图片的方法
   */
  public Result scanningImage(String path) {
    if (TextUtils.isEmpty(path)) {
      return null;
    }
    Hashtable<DecodeHintType, String> hints = new Hashtable<>();
    hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true; // 先获取原大小
    scanBitmap = BitmapFactory.decodeFile(path, options);
    options.inJustDecodeBounds = false; // 获取新的大小
    int sampleSize = (int) (options.outHeight / (float) 200);
    if (sampleSize <= 0) sampleSize = 1;
    options.inSampleSize = sampleSize;
    scanBitmap = BitmapFactory.decodeFile(path, options);
    RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
    BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
    QRCodeReader reader = new QRCodeReader();
    try {
      return reader.decode(bitmap1, hints);
    } catch (NotFoundException e) {
      e.printStackTrace();
    } catch (ChecksumException e) {
      e.printStackTrace();
    } catch (FormatException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  protected void onResume() {
    super.onResume();
    SurfaceView surfaceView = (SurfaceView) findViewById(R.id.scanner_view);
    SurfaceHolder surfaceHolder = surfaceView.getHolder(); //surfaceholder 是我们与surface（实际承载图像原始像素的缓冲区）对象联系的纽带
    if (hasSurface) {
      initCamera(surfaceHolder);
    } else {
      surfaceHolder.addCallback(this);
      surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    decodeFormats = null;
    characterSet = null;

    playBeep = true;
    AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE); //初始化音频管理器
    if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
      playBeep = false;
    }
    initBeepSound();
    vibrate = true;

    //quit the scan view
    //		cancelScanButton.setOnClickListener(new OnClickListener() {
    //
    //			@Override
    //			public void onClick(View v) {
    //				CaptureActivity.this.finish();
    //			}
    //		});
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (handler != null) {
      handler.quitSynchronously();
      handler = null;
    }
    CameraManager.get().closeDriver();
  }

  @Override
  protected void onDestroy() {
    inactivityTimer.shutdown();
    super.onDestroy();
  }

  /**
   * Handler scan result
   * 处理程序扫码结果
   */
  public void handleDecode(Result result, Bitmap barcode) {
    inactivityTimer.onActivity();
    playBeepSoundAndVibrate();
    String resultString = result.getText();
    if (TextUtils.isEmpty(resultString)) {
      Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
    } else {
      Intent resultIntent = new Intent();
      Bundle bundle = new Bundle();
      bundle.putString("result", resultString);
      // 不能使用Intent传递大于40kb的bitmap，可以使用一个单例对象存储这个bitmap
      //            bundle.putParcelable("bitmap", barcode);
      resultIntent.putExtras(bundle);
      this.setResult(RESULT_OK, resultIntent);
    }
    CaptureActivity.this.finish();   //ZYZ标签
  }

  private void initCamera(SurfaceHolder surfaceHolder) {
    try {
      CameraManager.get().openDriver(surfaceHolder);
    } catch (IOException ioe) {
      return;
    } catch (RuntimeException e) {
      return;
    }
    if (handler == null) {
      handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
    }
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    if (!hasSurface) {
      hasSurface = true;
      initCamera(holder);
    }
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    hasSurface = false;
  }

  public ViewfinderView getViewfinderView() {
    return viewfinderView;
  }

  public Handler getHandler() {
    return handler;
  }

  public void drawViewfinder() {
    viewfinderView.drawViewfinder();
  }

  private void initBeepSound() { //初始化扫码声音
    if (playBeep && mediaPlayer == null) {
      // The volume on STREAM_SYSTEM is not adjustable, and users found it
      // too loud,
      // so we now play on the music stream.
      setVolumeControlStream(AudioManager.STREAM_MUSIC); //媒体音量
      mediaPlayer = new MediaPlayer();
      mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); //设置播放流媒体的类型为播放音频
      mediaPlayer.setOnCompletionListener(beepListener); //社会播放完回调

      AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep); //读取扫码音频文件
      try {
        mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(),
            file.getLength()); //播放应用的资源文件
        file.close();
        mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME); //设置声音音量
        mediaPlayer.prepare();
      } catch (IOException e) {
        mediaPlayer = null;
      }
    }
  }

  private static final long VIBRATE_DURATION = 200L; //振动持续时间

  private void playBeepSoundAndVibrate() { //播放扫码声音
    if (playBeep && mediaPlayer != null) {
      mediaPlayer.start();
    }
    if (vibrate) {
      Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE); //构造手机振动器
      vibrator.vibrate(VIBRATE_DURATION); //设置手机振动
    }
  }

  /**
   * When the beep has finished playing, rewind to queue up another one.
   */
  private final OnCompletionListener beepListener = new OnCompletionListener() {
    public void onCompletion(MediaPlayer mediaPlayer) {
      mediaPlayer.seekTo(0); //恢复到0播放位置
    }
  };
}