package com.ddinfo.flashman.activity.base_frame.camera;//package com.ddinfo.flashman.activity.base_frame.camera;
//
//import android.app.ProgressDialog;
//import android.content.ContentResolver;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.ddinfo.salesman.activity.base_frame.BaseActivity;
//import com.ddinfo.salesman.constant.ExampleConfig;
//import com.ddinfo.salesman.utils.FileUtils;
//import com.ddinfo.salesman.utils.ImageUtils;
//import com.ddinfo.salesman.utils.LogUtils;
//import com.ddinfo.salesman.utils.ToastUtils;
//
//import java.io.File;
//
//import top.zibin.luban.Luban;
//import top.zibin.luban.OnCompressListener;
//
//import static android.app.Activity.RESULT_OK;
//
///**
// * Created by weitf on 2016/10/13.
// */
//public abstract class TakePhotoActivity extends BaseActivity {
//	private static final int RESULT_CAPTURE_IMAGE = 1;
//	// private String strImgPath;
//	private static final int RESULT_GET_PHOTO_BY_CAMERA = 102;
//	private static final int PHOTO_REQUEST_GALLERY = 103;
//	private Uri mPhotoUri;
//	private String mCameraPhotoFilePath;
//	private String mPhotoName = "temp.jpg";
//	private String thumbImgName = "";
//	private ContentResolver cr;
//
//	public abstract void doGetThumb(ThumbBean thumBean);
//	/**
//	 * 调用相机拍照
//	 */
//	public void takePhotoFromCamera() {
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), mPhotoName)));
//		startActivityForResult(intent, RESULT_GET_PHOTO_BY_CAMERA);
//	}
//	/**
//	 * 调用相机拍照
//	 */
//	public void takePhotoFromCamera(String thumbImgName) {
//		this.thumbImgName = thumbImgName;
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), mPhotoName)));
//		startActivityForResult(intent, RESULT_GET_PHOTO_BY_CAMERA);
//	}
//	/**
//	 * 激活系统图库，选择一张图片
//	 */
//	public void takePhotoFromAlbum() {
//		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
//		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//		startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
//	}
//
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		Log.d("result", "code" + requestCode);
//		if (requestCode == RESULT_GET_PHOTO_BY_CAMERA) {
//			if (resultCode == RESULT_OK) {
//				new CameraImageTask().execute();
//			}
//		} else if (requestCode == PHOTO_REQUEST_GALLERY) {
//			if (resultCode == RESULT_OK) {
//				mPhotoUri = data.getData();
//				new ParserImageTask().execute();
//			}
//		}
//	}
//
//	private class CameraImageTask extends AsyncTask<Void, Void, Integer> {
//
//		private ProgressDialog dialog;
//		private String thumbnailPath;
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//
//			if (!TakePhotoActivity.this.isFinishing()) {
//				dialog = new ProgressDialog(TakePhotoActivity.this);
//				dialog.setMessage("正在获取图片，请稍候...");
//				dialog.show();
//				dialog.setCancelable(false);
//			}
//		}
//
//		@Override
//		protected Integer doInBackground(Void... arg0) {
//			try {
//				mCameraPhotoFilePath = Environment.getExternalStorageDirectory().getPath() + "/" + mPhotoName;
//				if (TextUtils.isEmpty(thumbnailPath))
//					thumbnailPath = mCameraPhotoFilePath;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Integer result) {
//			super.onPostExecute(result);
//			if (dialog != null && dialog.isShowing() && !TakePhotoActivity.this.isFinishing())
//				dialog.dismiss();
//			if (!TextUtils.isEmpty(mCameraPhotoFilePath)) {
//				try {
//					File file = new File(mCameraPhotoFilePath);
//					if (file.exists()) {
//						Uri fileUri = Uri.fromFile(file);
//						sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri));
//						final ThumbBean thumb = new ThumbBean(0, fileUri.toString(), thumbnailPath, mCameraPhotoFilePath);
////						new CompressBitmapTask().execute(thumb);
//						final ProgressDialog dialog = new ProgressDialog(TakePhotoActivity.this);
//						dialog.setMessage("正在压缩图片，请稍候...");
//						dialog.setCancelable(false);
//						Luban.get(context)
//								.load(file)                     //传人要压缩的图片
//								.putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
//								.setCompressListener(new OnCompressListener() { //设置回调
//
//									@Override
//									public void onStart() {
//										dialog.show();
//									}
//
//									@Override
//									public void onSuccess(File file) {
//										if (dialog != null && dialog.isShowing() && !TakePhotoActivity.this.isFinishing()) {
//											dialog.dismiss();
//										}
//										if (file == null) {
//											ToastUtils.showShortToastSafe(context, "保存失败");
//										} else {
//											thumbImgName=TextUtils.isEmpty(thumbImgName)?"thumbImg.png":thumbImgName;
//											String thumbFilePath= ExampleConfig.SDCARD_PATH+ExampleConfig.PIC_DIR_NAME + thumbImgName;
//											String size =FileUtils.getFileSize(file);
//											String srcFilepath =file.getAbsolutePath();
//											Bitmap bm =ImageUtils.getBitmap(file);
//											FileUtils.deleteFile(thumbFilePath);
//											FileUtils.moveFile(srcFilepath, thumbFilePath);
//											LogUtils.d("CompressBitmapFile", size);
//											thumb.setBitmap(bm);
//											thumb.setImage_path(thumbFilePath);
//											doGetThumb(thumb);
//											setResult(RESULT_OK);
//										}
//									}
//
//									@Override
//									public void onError(Throwable e) {
//									}
//								}).launch();    //启动压缩
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//
//
//	private class ParserImageTask extends AsyncTask<Void, Void, Integer> {
//
//		private ProgressDialog dialog;
//		private String thumbnailPath;
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//
//			if (!TakePhotoActivity.this.isFinishing()) {
//				dialog = new ProgressDialog(TakePhotoActivity.this);
//				dialog.setMessage("正在获取图片，请稍候...");
//				dialog.show();
//				dialog.setCancelable(false);
//			}
//		}
//
//		@Override
//		protected Integer doInBackground(Void... arg0) {
//			// Uri uri;
//			Cursor cursor = null;
//			try {
//				cr = getContentResolver();
//				cursor = cr.query(mPhotoUri, new String[] { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID }, null, null, null);
//				if (cursor != null && cursor.moveToFirst()) {
//					mCameraPhotoFilePath = cursor.getString(0);
//					int id = cursor.getInt(1);
//					// thumbnailPath = getThumbnailPath(id);
//					if (TextUtils.isEmpty(thumbnailPath))
//						thumbnailPath = mCameraPhotoFilePath;
//					return id;
//				}
//				// int imageId = (int) ContentUris.parseId(uri);
//				// thumbnailPath = getThumbnailPath(imageId);
//				// return imageId;
//				// } catch (FileNotFoundException e) {
//				// e.printStackTrace();
//			} finally {
//				if (cursor != null)
//					cursor.close();
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Integer result) {
//			super.onPostExecute(result);
//			if (dialog != null && dialog.isShowing() && !TakePhotoActivity.this.isFinishing())
//				dialog.dismiss();
//			if (!TextUtils.isEmpty(mCameraPhotoFilePath)) {
//				File file = new File(mCameraPhotoFilePath);
//				if (file.exists()) {
//					Uri fileUri = Uri.fromFile(file);
//					sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri));
//					int imageId = result;
//					ThumbBean thumb = new ThumbBean(imageId, fileUri.toString(), thumbnailPath, mCameraPhotoFilePath);
//					doGetThumb(thumb);
//					setResult(RESULT_OK);
//				}
//			}
//		}
//	}
//
//	/**
//	 * 压缩图片 保存sdcard
//	 */
//	private class CompressBitmapTask extends AsyncTask<ThumbBean,Void,ThumbBean>{
//		private ProgressDialog dialog;
//		private  int maxWidth=0;
//		private int maxHeight=0;
//		private long maxByteSize = 150*1024;
//		private String thumbFilePath ="";
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			if (!TakePhotoActivity.this.isFinishing()) {
//				dialog = new ProgressDialog(TakePhotoActivity.this);
//				dialog.setMessage("正在压缩图片，请稍候...");
//				dialog.setCancelable(false);
//				dialog.show();
//			}
//			maxWidth= 760;
//			maxHeight=1280;
//			thumbImgName=TextUtils.isEmpty(thumbImgName)?"thumbImg.png":thumbImgName;
//			thumbFilePath= ExampleConfig.SDCARD_PATH+ExampleConfig.PIC_DIR_NAME + thumbImgName;
////			String res = ConvertUtils.byte2FitSize(maxByteSize);
////			Log.d("ConvertUtils",res);
//			Log.d("photoScreen", "width:" + maxWidth + "  height:" + maxHeight);
//		}
//
//		@Override
//		protected ThumbBean doInBackground(ThumbBean... params) {
//			ThumbBean bean = params[0];
//			String mCameraPhotoFilePath = bean.getLarge_image_path();
//			Bitmap bitmap= ImageUtils.getBitmap(mCameraPhotoFilePath, maxWidth, maxHeight);//从sdcard获取图片
//
//			Bitmap thumbBitmap = ImageUtils.compressByQuality(bitmap, maxByteSize);//按质量压缩图片
//			boolean result= ImageUtils.save(thumbBitmap, thumbFilePath, Bitmap.CompressFormat.JPEG);//保存图片
//			int degree = ImageUtils.readPictureDegree(thumbFilePath);
////			Bitmap thumbBitmapDegree=ImageUtils.rotateBitmapByDegree(thumbBitmap, 90);+
////			boolean res= ImageUtils.save(thumbBitmapDegree, thumbFilePath, Bitmap.CompressFormat.JPEG);//保存图片
//			Log.d("SAMSUNG degree:",degree+"");
//			if(result){
//				bean.setImage_path(thumbFilePath);
//				bean.setBitmap(thumbBitmap);
//			}else{
//				return null;
//			}
//			return bean;
//		}
//
//		@Override
//		protected void onPostExecute(ThumbBean bean) {
//			super.onPostExecute(bean);
//			if (dialog != null && dialog.isShowing() && !TakePhotoActivity.this.isFinishing()) {
//				dialog.dismiss();
//			}
//			if(bean==null){
//				ToastUtils.showShortToastSafe(context,"保存失败");
//			}
//			doGetThumb(bean);
//			setResult(RESULT_OK);
//		}
//	}
//}
