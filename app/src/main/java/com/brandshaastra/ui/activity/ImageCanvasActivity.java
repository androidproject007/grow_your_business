package com.brandshaastra.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

//import com.brandshaastra.ffmpeg.ExecuteBinaryResponseHandler;
//import com.brandshaastra.ffmpeg.FFmpeg;
import com.brandshaastra.databinding.ActivityImageCanvasBinding;
import com.brandshaastra.databinding.ActivityPreviewBinding;
import com.brandshaastra.utils.FileUtility;
import com.bumptech.glide.Glide;
import com.easystudio.rotateimageview.OnDragTouchListener;
import com.easystudio.rotateimageview.RotateZoomImageView;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.brandshaastra.DTO.BusinessDataDto;
import com.brandshaastra.DTO.UserDTO;
import com.brandshaastra.R;
import com.brandshaastra.https.HttpsRequest;
import com.brandshaastra.interfaces.Consts;
import com.brandshaastra.interfaces.CustomGridLayoutManager;
import com.brandshaastra.interfaces.Helper;
import com.brandshaastra.interfaces.OnFontStyleChange;
import com.brandshaastra.interfaces.ThemeItemClick;
import com.brandshaastra.network.NetworkManager;
import com.brandshaastra.photoeditor.OnPhotoEditorListener;
import com.brandshaastra.photoeditor.PhotoEditor;
import com.brandshaastra.photoeditor.PhotoEditorView;
import com.brandshaastra.photoeditor.SaveSettings;
import com.brandshaastra.photoeditor.TextStyleBuilder;
import com.brandshaastra.photoeditor.ViewType;
import com.brandshaastra.preferences.SharedPrefrence;
import com.brandshaastra.ui.StickerBSFragment;
import com.brandshaastra.ui.adapter.FontStyleAdapter;
import com.brandshaastra.ui.adapter.ImageCanvasAdapter;
import com.brandshaastra.ui.adapter.ThemeAdapter;
import com.brandshaastra.utils.CameraUtils;
import com.brandshaastra.utils.CustomTextViewBold;
import com.brandshaastra.utils.DimensionData;
import com.brandshaastra.utils.FontCache;
import com.brandshaastra.utils.TextEditorDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


import static android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION;
import static com.brandshaastra.utils.Utils.getScaledDimension;


public class ImageCanvasActivity extends AppCompatActivity implements ThemeItemClick, View.OnClickListener, OnFontStyleChange,

        CompoundButton.OnCheckedChangeListener, OnPhotoEditorListener, StickerBSFragment.StickerListener {
    //  List<Drawable> themeList;
    List<String> themeList;
    ActivityImageCanvasBinding binding;
    boolean share_flag = false;
    ThemeAdapter themeAdapter;
    int selectedTheme = 0;
    List<String> imageList;
    ImageCanvasAdapter adapter;
    float xDown = 0, yDown = 0;
    BottomSheetDialog bottomSheetDialog;
    SeekBar bottom_seekbar;
    CustomTextViewBold bottomsheet_title, bottomsheet_text_size;
    LinearLayout bottomsheet_close;
    RelativeLayout bottomsheet_relative, bottomsheet_relative2, bottomsheet_relative3;
    Switch setting_logo_switch, setting_socialmedia_switch, setting_website_switch,
            setting_email_switch, setting_phone_switch, setting_address_switch,setting_whatsapp_switch;
    RecyclerView font_rv;
    private String imagePath = "";
    private String[] newCommand;
    int selected_theme_no = 0;
    FontStyleAdapter fontStyleAdapter;
    ArrayList<String> stringImageList = new ArrayList<>();
    HashMap<String, File> imageMap;
    boolean text_flag = false;
    boolean image_flag = false;
    boolean video_flag = false;
    private SharedPreferences firebase;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    String position = "";
    HashMap<String, String> params = new HashMap<>();
    HashMap<String, String> downloadVideoparams = new HashMap<>();
    ImageView setting_theme_color, setting_font_color;
    int frame_border_color = 0;
    boolean frame_flag = false;
    boolean image_background_flag = false;
    int image_background_color = -1;
    PhotoEditor mPhotoEditor;
    PhotoEditorView mPhotoEditorView;
    private CameraUtils cameraUtils;
    String category_name = "";
    String cat_tracker = "";
    BusinessDataDto businessDataDto;
    public RotateZoomImageView rotateZoomImageView, rotateZoomImageView_video;
    private int originalDisplayWidth;
    private int originalDisplayHeight;
    private int newCanvasWidth, newCanvasHeight;
    private int DRAW_CANVASW = 0;
    private int DRAW_CANVASH = 0;
    private StickerBSFragment mStickerBSFragment;
    //FFmpeg fFmpeg;
    private MediaPlayer mediaPlayer;
    private String videoPath = "";
    private ArrayList<String> exeCmd;
    String fileN = "";
    String video_img_path = "";
    String video_url2 = "";
    boolean image_loaded_flag = false;
    boolean VIDEO_FFMPEG_flag = false;
    private ProgressDialog progressDialog;
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageCanvasBinding.inflate(getLayoutInflater());
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 101);
        }
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                //todo when permission is granted
            } else {
                //request for the permission
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }*/

        setContentView(R.layout.activity_image_canvas);
        setContentView(binding.getRoot());
//        fFmpeg = FFmpeg.getInstance(this);
        getSupportActionBar().hide();
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        prefrence = SharedPrefrence.getInstance(ImageCanvasActivity.this);
        firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        businessDataDto = prefrence.getBusinessData(Consts.BUSINESSDATA_DTO);
        // cameraUtils = new CameraUtils(this, this);
        text_flag = getIntent().getBooleanExtra("text", false);
        image_flag = getIntent().getBooleanExtra("image", false);
        video_flag = getIntent().getBooleanExtra("video", false);
        rotateZoomImageView = findViewById(R.id.text_image_img);
        rotateZoomImageView_video = findViewById(R.id.imageView);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(250, 250);
        //lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        rotateZoomImageView.setLayoutParams(lp);
        rotateZoomImageView_video.setLayoutParams(lp);
        setUidata();

        if(text_flag){
            binding.imageToolsSubLinear.setWeightSum(5);
            //binding.imageToolsSubLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,6f));
            binding.imageRecyclerview.setVisibility(View.GONE);
            rotateZoomImageView.setVisibility(View.GONE);
            binding.rotateImgClose.setVisibility(View.GONE);
            binding.resizeImg.setVisibility(View.GONE);
            binding.pickimage.setVisibility(View.GONE);
            binding.addText.setVisibility(View.GONE);
            binding.videoShare.setVisibility(View.GONE);
            binding.movableEditText.setVisibility(View.VISIBLE);
        }
        if(image_flag){
            binding.imageToolsSubLinear.setWeightSum(6);
            //binding.imageToolsSubLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,7f));
            binding.imageRecyclerview.setVisibility(View.GONE);
            //  binding.resizeImg.setVisibility(View.VISIBLE);
            // binding.rotateImgClose.setVisibility(View.VISIBLE);
            binding.editBg.setVisibility(View.GONE);
            binding.videoShare.setVisibility(View.GONE);
            binding.movableEditText.setVisibility(View.GONE);
        }

        if(video_flag){

            binding.imageToolsSubLinear.setWeightSum(6);
            //binding.imageToolsSubLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,7f));
            binding.imageShare.setVisibility(View.GONE);
            binding.videoShare.setVisibility(View.VISIBLE);
            binding.imgDone.setVisibility(View.VISIBLE);
            binding.imageRecyclerview.setVisibility(View.GONE);
            //  binding.resizeImg.setVisibility(View.VISIBLE);
            // binding.rotateImgClose.setVisibility(View.VISIBLE);
            binding.videoImage.setVisibility(View.VISIBLE);
            //binding.imgSticker.setVisibility(View.VISIBLE);
            binding.videoSurface.setVisibility(View.VISIBLE);
            // binding.imgClose.setVisibility(View.VISIBLE);
            // binding.imgDone.setVisibility(View.VISIBLE);
            binding.editBg.setVisibility(View.GONE);
            binding.movableEditText.setVisibility(View.GONE);
            video_url2 = getIntent().getStringExtra("video_url2");
            videoPath = getIntent().getStringExtra("DATA");
            Log.d("mytag","video_url2 "+video_url2);
            Log.d("mytag"," videopath "+videoPath);
            //Log.d("mytag","userid "+ userDTO.getUser_id());
            Log.e("VIDEOPATH", "" + videoPath);

            /*if (NetworkManager.isConnectToInternet(this)){
              DownloadTask downloadTask = new DownloadTask(ImageCanvasActivity.this);
                downloadTask.execute(videoPath);
            } else {
                Toast.makeText(this, getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();
            }*/
        }

        if (getIntent().hasExtra(Consts.POSITION)) {
            binding.editBg.setVisibility(View.GONE);
            binding.frame.setVisibility(View.GONE);
            category_name = getIntent().getStringExtra(Consts.CATEGORY_NAME);
            cat_tracker = getIntent().getStringExtra(Consts.TRACKER);
            Log.e("CATEGORY_NAME_track", " 0 " + category_name + " tracker:-- " + cat_tracker);
            position = getIntent().getStringExtra(Consts.POSITION);
            stringImageList = (ArrayList<String>) getIntent().getSerializableExtra(Consts.IMAGE_LIST);
            getImageList();
        }

    }

    private void setUidata() {
        if (!businessDataDto.getImage().equalsIgnoreCase("") || businessDataDto.getImage().isEmpty()) {
            //new frame code
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.twentynineNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.thirtyNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.thirtyoneNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.thirtytwoNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.thirtythreeNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.thirtyfourNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.thirtyfiveNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.thirtysixthNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.thirtysevenNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.thirtyeightNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.thirtynineNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.fortyNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.fortyoneNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.fortytwoNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.fortythreeNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.fortyfourNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.fortyfiveNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.fortysixNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.fortysevenNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.fortyeightNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.fortynineNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.fiftyNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.fiftyoneNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.fiftytwoNew.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.fiftythreeNew.businessIcon);


            //old frame code
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.first.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.second.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.third.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.forth.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.fifth.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.sixth.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.seventh.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.nineth.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.tenth.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.eleven.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.twelve.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.thirteen.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.fourteen.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.fifteen.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.sixteenth.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.seventeenth.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.eighteen.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.nineteen.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.twenty.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.twentyone.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.twentytwo.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.twentythree.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.twentyfour.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.twentyfive.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.twentysix.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.twentyseven.businessIcon);
            Glide.with(this).load(businessDataDto.getImage()).placeholder(R.drawable.brand_shaastra_logo).into(binding.twentyeight.businessIcon);


        }
        //new frame code
        //frame29
        binding.twentynineNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.twentynineNew.businessMailDetails.setText(businessDataDto.getEmail());
        binding.twentynineNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.twentynineNew.businessWhatsappDetails.setText(businessDataDto.getMobile_no());
        binding.twentynineNew.businessCompanyDetails.setText(businessDataDto.getName());
        //frame30
        binding.thirtyNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.thirtyNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.thirtyNew.businessMailDetails.setText(businessDataDto.getEmail());
        binding.thirtyNew.businessLocationDetails.setText(businessDataDto.getAddress());

        //frame31
        binding.thirtyoneNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.thirtyoneNew.businessMailDetails.setText(businessDataDto.getEmail());
        binding.thirtyoneNew.businessLocationDetails.setText(businessDataDto.getAddress());
        binding.thirtyoneNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.thirtyoneNew.businessCompanyDetails.setText(businessDataDto.getName());
        //frame32
        binding.thirtytwoNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.thirtytwoNew.businessMailDetails.setText(businessDataDto.getEmail());
        binding.thirtytwoNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.thirtytwoNew.businessLocationDetails.setText(businessDataDto.getAddress());
        binding.thirtytwoNew.businessCompanyDetails.setText(businessDataDto.getName());

        //frame33
        binding.thirtythreeNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.thirtythreeNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.thirtythreeNew.businessLocationDetails.setText(businessDataDto.getAddress());
        binding.thirtythreeNew.businessMailDetails.setText(businessDataDto.getEmail());
        binding.thirtythreeNew.businessWhatsappDetails.setText(businessDataDto.getMobile_no());
        binding.thirtythreeNew.businessCompanyDetails.setText(businessDataDto.getName());


        //frame34
        binding.thirtyfourNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.thirtyfourNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.thirtyfourNew.businessLocationDetails.setText(businessDataDto.getAddress());
        binding.thirtyfourNew.businessMailDetails.setText(businessDataDto.getEmail());
        binding.thirtyfourNew.businessCompanyDetails.setText(businessDataDto.getName());

        //frame35
        binding.thirtyfiveNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.thirtyfiveNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.thirtyfiveNew.businessMailDetails.setText(businessDataDto.getEmail());
        binding.thirtyfiveNew.businessCompanyDetails.setText(businessDataDto.getName());


        //frame36
        binding.thirtysixthNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.thirtysixthNew.businessCompanyDetails.setText(businessDataDto.getName());

        binding.thirtysixthNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.thirtysixthNew.businessMailDetails.setText(businessDataDto.getEmail());
        binding.thirtysixthNew.businessCallDetailsSecond.setText(businessDataDto.getMobile_no());

        //frame37
        binding.thirtysevenNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.thirtysevenNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.thirtysevenNew.businessMailDetails.setText(businessDataDto.getEmail());
        binding.thirtysevenNew.businessLocationDetails.setText(businessDataDto.getAddress());
        binding.thirtysevenNew.businessCompanyDetails.setText(businessDataDto.getName());

        //frame38
        binding.thirtyeightNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.thirtyeightNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.thirtyeightNew.businessMailDetails.setText(businessDataDto.getEmail());
        binding.thirtyeightNew.businessCompanyDetails.setText(businessDataDto.getName());

        //frame39
        binding.thirtynineNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.thirtynineNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.thirtynineNew.businessMailDetails.setText(businessDataDto.getEmail());
        binding.thirtynineNew.businessWhatsappDetails.setText(businessDataDto.getMobile_no());
        binding.thirtynineNew.businessCompanyDetails.setText(businessDataDto.getName());
        binding.thirtynineNew.businessLocationDetails.setText(businessDataDto.getAddress());
        //frame40
        binding.fortyNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.fortyNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.fortyNew.businessMailDetails.setText(businessDataDto.getEmail());
        binding.fortyNew.businessLocationDetails.setText(businessDataDto.getAddress());
        binding.fortyNew.businessCompanyDetails.setText(businessDataDto.getName());
        //frame41
        binding.fortyoneNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.fortyoneNew.businessMailDetails.setText(businessDataDto.getEmail());
        binding.fortyoneNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.fortyoneNew.businessCompanyDetails.setText(businessDataDto.getName());
        binding.fortyoneNew.businessLocationDetails.setText(businessDataDto.getAddress());
        //frame42
        binding.fortytwoNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.fortytwoNew.businessLocationDetails.setText(businessDataDto.getAddress());
        binding.fortytwoNew.businessCompanyDetails.setText(businessDataDto.getName());
        binding.fortytwoNew.businessMailDetails.setText(businessDataDto.getEmail());
        binding.fortytwoNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        //frame43
        binding.fortythreeNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.fortythreeNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.fortythreeNew.businessMailDetails.setText(businessDataDto.getEmail());
        binding.fortythreeNew.businessCompanyDetails.setText(businessDataDto.getName());
        //frame44
        binding.fortyfourNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.fortyfourNew.businessLocationDetails.setText(businessDataDto.getAddress());
        binding.fortyfourNew.businessCompanyDetails.setText(businessDataDto.getName());


        //frame45
        binding.fortyfiveNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.fortyfiveNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.fortyfiveNew.businessMailDetails.setText(businessDataDto.getEmail());
        binding.fortyfiveNew.businessCompanyDetails.setText(businessDataDto.getName());

        //frame46
        binding.fortysixNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.fortysixNew.businessWhatsappDetails.setText(businessDataDto.getMobile_no());
        binding.fortysixNew.businessMailDetails.setText(businessDataDto.getWebsite());
        binding.fortysixNew.businessCompanyDetails.setText(businessDataDto.getName());
        binding.fortysixNew.businessLocationDetails.setText(businessDataDto.getAddress());

        //frame47
        binding.fortysevenNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.fortysevenNew.businessLocationDetails.setText(businessDataDto.getAddress());
        binding.fortysevenNew.businessCompanyDetails.setText(businessDataDto.getName());
        binding.fortysevenNew.businessCallDetailsSecond.setText(businessDataDto.getMobile_no());
        binding.fortysevenNew.businessMailDetails.setText(businessDataDto.getEmail());
        binding.fortysevenNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        //frame48


        //frame49
        binding.fortynineNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.fortynineNew.businessCallDetailsSecond.setText(businessDataDto.getMobile_no());
        binding.fortynineNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.fortynineNew.businessCompanyDetails.setText(businessDataDto.getName());
        binding.fortynineNew.businessMailDetails.setText(businessDataDto.getEmail());
        binding.fortynineNew.businessLocationDetails.setText(businessDataDto.getAddress());

        //frame50
        binding.fiftyNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.fiftyNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.fiftyNew.businessMailDetails.setText(businessDataDto.getEmail());
        binding.fiftyNew.businessCompanyDetails.setText(businessDataDto.getName());
        binding.fiftyNew.businessLocationDetails.setText(businessDataDto.getAddress());


        //frame51
        binding.fiftyoneNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.fiftyoneNew.businessCallDetailsSecond.setText(businessDataDto.getMobile_no());
        binding.fiftyoneNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.fiftyoneNew.businessMailDetails.setText(businessDataDto.getEmail());
        binding.fiftyoneNew.businessLocationDetails.setText(businessDataDto.getAddress());

        //frame52
        binding.fiftytwoNew.businessCompanyDetails.setText(businessDataDto.getName());
        binding.fiftytwoNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.fiftytwoNew.businessLocationDetails.setText(businessDataDto.getAddress());
        binding.fiftytwoNew.businessMailDetails.setText(businessDataDto.getEmail());

        //frame53
        binding.fiftythreeNew.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.fiftythreeNew.businessCompanyDetails.setText(businessDataDto.getName());
        binding.fiftythreeNew.businessLocationDetails.setText(businessDataDto.getAddress());
        binding.fiftythreeNew.businessMailDetails.setText(businessDataDto.getEmail());
        binding.fiftythreeNew.businessWebsiteDetails.setText(businessDataDto.getWebsite());




        //old frame code
        binding.first.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.first.businessMailDetails.setText(businessDataDto.getEmail());
        binding.first.businessWebsiteDetails.setText(businessDataDto.getWebsite());
       /* binding.first.companyName.setText(businessDataDto.getName());*/

       /* binding.first.businessLocationDetails.setText(businessDataDto.getAddress());*/

        binding.second.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.second.businessMailDetails.setText(businessDataDto.getEmail());
        binding.second.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.second.businessLocationDetails.setText(businessDataDto.getAddress());

        binding.third.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.third.businessMailDetails.setText(businessDataDto.getEmail());
        binding.third.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.third.businessLocationDetails.setText(businessDataDto.getAddress());

        binding.forth.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.forth.businessMailDetails.setText(businessDataDto.getEmail());
        binding.forth.businessLocationDetails.setText(businessDataDto.getAddress());

        binding.fifth.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.fifth.businessMailDetails.setText(businessDataDto.getEmail());
        binding.fifth.businessLocationDetails.setText(businessDataDto.getAddress());

        binding.sixth.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.sixth.businessMailDetails.setText(businessDataDto.getEmail());
        binding.sixth.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.sixth.businessLocationDetails.setText(businessDataDto.getAddress());

        binding.seventh.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.seventh.businessMailDetails.setText(businessDataDto.getEmail());
        binding.seventh.businessLocationDetails.setText(businessDataDto.getAddress());

        binding.eight.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.eight.businessMailDetails.setText(businessDataDto.getEmail());
        binding.eight.businessLocationDetails.setText(businessDataDto.getAddress());

        binding.nineth.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.nineth.businessWebsiteDetails.setText(businessDataDto.getWebsite());

        binding.tenth.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.tenth.businessMailDetails.setText(businessDataDto.getEmail());
        binding.tenth.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.tenth.businessLocationDetails.setText(businessDataDto.getAddress());

        binding.eleven.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.eleven.businessMailDetails.setText(businessDataDto.getEmail());
        binding.eleven.businessWebsiteDetails.setText(businessDataDto.getWebsite());

        binding.twelve.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.twelve.businessMailDetails.setText(businessDataDto.getEmail());
        binding.twelve.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.twelve.businessLocationDetails.setText(businessDataDto.getAddress());

        binding.thirteen.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.thirteen.businessMailDetails.setText(businessDataDto.getEmail());
        binding.thirteen.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.thirteen.businessLocationDetails.setText(businessDataDto.getAddress());

        binding.fourteen.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.fourteen.businessMailDetails.setText(businessDataDto.getEmail());
        binding.fourteen.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.fourteen.businessLocationDetails.setText(businessDataDto.getAddress());

        binding.fifteen.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.fifteen.businessMailDetails.setText(businessDataDto.getEmail());
        binding.fifteen.businessWebsiteDetails.setText(businessDataDto.getWebsite());

        binding.sixteenth.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.sixteenth.businessWebsiteDetails.setText(businessDataDto.getWebsite());

        binding.seventeenth.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.seventeenth.businessWebsiteDetails.setText(businessDataDto.getWebsite());

        binding.eighteen.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.eighteen.businessWebsiteDetails.setText(businessDataDto.getWebsite());
        binding.eighteen.businessMailDetails.setText(businessDataDto.getEmail());

        binding.nineteen.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.nineteen.businessMailDetails.setText(businessDataDto.getEmail());
        binding.nineteen.businessLocationDetails.setText(businessDataDto.getAddress());

        binding.twenty.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.twenty.businessMailDetails.setText(businessDataDto.getEmail());
        binding.twenty.businessLocationDetails.setText(businessDataDto.getAddress());

        binding.twentyone.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.twentyone.businessMailDetails.setText(businessDataDto.getEmail());

        binding.twentytwo.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.twentytwo.businessMailDetails.setText(businessDataDto.getEmail());
        binding.twentytwo.businessLocationDetails.setText(businessDataDto.getAddress());

        binding.twentythree.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.twentythree.businessMailDetails.setText(businessDataDto.getEmail());
        binding.twentythree.businessName.setText(businessDataDto.getName());

        binding.twentyfour.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.twentyfour.businessMailDetails.setText(businessDataDto.getEmail());
        binding.twentyfour.businessName.setText(businessDataDto.getName());

        binding.twentyfive.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.twentyfive.businessMailDetails.setText(businessDataDto.getEmail());
        binding.twentyfive.businessName.setText(businessDataDto.getName());

        binding.twentysix.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.twentysix.businessMailDetails.setText(businessDataDto.getEmail());
        binding.twentysix.businessName.setText(businessDataDto.getName());

        binding.twentyseven.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.twentyseven.businessMailDetails.setText(businessDataDto.getEmail());
        binding.twentyseven.businessName.setText(businessDataDto.getName());

        binding.twentyeight.businessCallDetails.setText(businessDataDto.getMobile_no());
        binding.twentyeight.businessMailDetails.setText(businessDataDto.getEmail());
        binding.twentyeight.businessName.setText(businessDataDto.getName());

        changeDrawablecolor(binding.third.footerCallImage.getBackground(), getResources().getColor(R.color.frame3_bg));
        changeDrawablecolor(binding.third.footerWebsiteImage.getBackground(), getResources().getColor(R.color.frame3_bg));
        changeDrawablecolor(binding.third.footerLocationImage.getBackground(), getResources().getColor(R.color.frame3_bg));
        changeDrawablecolor(binding.third.footerMailImage.getBackground(), getResources().getColor(R.color.frame3_bg));

        changeDrawablecolor(binding.tenth.locationMainRelative.getBackground(), getResources().getColor(R.color.frame10));
        changeDrawablecolor(binding.tenth.mailMainRelative.getBackground(), getResources().getColor(R.color.frame10));
        changeDrawablecolor(binding.tenth.callMainRelative.getBackground(), getResources().getColor(R.color.frame10));
        changeDrawablecolor(binding.tenth.websiteMainRelative.getBackground(), getResources().getColor(R.color.frame10));

        /*changeDrawablecolor(binding.fourteen.locationMainRelative.getBackground(), getResources().getColor(R.color.fram));
        changeDrawablecolor(binding.fourteen.mailMainRelative.getBackground(), getResources().getColor(R.color.frame10));
        changeDrawablecolor(binding.fourteen.callMainRelative.getBackground(), getResources().getColor(R.color.frame10));
        changeDrawablecolor(binding.fourteen.websiteMainRelative.getBackground(), getResources().getColor(R.color.frame10));*/

        changeDrawablecolor(binding.forth.footerCallImage.getBackground(), getResources().getColor(R.color.frame4_bg));
        changeDrawablecolor(binding.forth.footerLocationImage.getBackground(), getResources().getColor(R.color.frame4_bg));
        changeDrawablecolor(binding.forth.footerMailImage.getBackground(), getResources().getColor(R.color.frame4_bg));

        binding.frameRelative.setBackground(ContextCompat.getDrawable(this, R.drawable.top_curved_card_bg));

        binding.frameRelative.setPadding(0, 0, 0, 0);

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_dialog_layout);

        setting_theme_color = bottomSheetDialog.findViewById(R.id.setting_theme_color);
        setting_font_color = bottomSheetDialog.findViewById(R.id.setting_font_color);
        bottom_seekbar = bottomSheetDialog.findViewById(R.id.bottomsheet_seekbar);
        bottomsheet_title = bottomSheetDialog.findViewById(R.id.bottomsheet_title);
        bottomsheet_text_size = bottomSheetDialog.findViewById(R.id.bottomsheet_text_size);
        bottomsheet_close = bottomSheetDialog.findViewById(R.id.bottomsheet_close);
        bottomsheet_relative = bottomSheetDialog.findViewById(R.id.bottomsheet_relative);
        bottomsheet_relative2 = bottomSheetDialog.findViewById(R.id.bottomsheet_relative2);
        bottomsheet_relative3 = bottomSheetDialog.findViewById(R.id.bottomsheet_relative3);
        setting_logo_switch = bottomSheetDialog.findViewById(R.id.setting_logo_switch);
        setting_socialmedia_switch = bottomSheetDialog.findViewById(R.id.setting_socialmedia_switch);
        setting_website_switch = bottomSheetDialog.findViewById(R.id.setting_website_switch);
        setting_email_switch = bottomSheetDialog.findViewById(R.id.setting_email_switch);
        setting_phone_switch = bottomSheetDialog.findViewById(R.id.setting_phone_switch);
        setting_address_switch = bottomSheetDialog.findViewById(R.id.setting_address_switch);
        setting_whatsapp_switch = bottomSheetDialog.findViewById(R.id.setting_whatsapp_switch);
        font_rv = bottomSheetDialog.findViewById(R.id.font_rv);
        binding.imgDone.setOnClickListener(this);

        getThemeData();
        getOnClickListeners();

        if(video_flag){
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setInverseBackgroundForced(true);
            mStickerBSFragment = new StickerBSFragment();
            mStickerBSFragment.setStickerListener(this);

            mPhotoEditor = new PhotoEditor.Builder(this, binding.videoImage)
                    .setPinchTextScalable(true) // set flag to make text scalable when pinch
                    .setDeleteView(binding.imgDelete)
                    //.setDefaultTextTypeface(mTextRobotoTf)
                    //.setDefaultEmojiTypeface(mEmojiTypeFace)
                    .build(); // build photo editor sdk

            mPhotoEditor.setOnPhotoEditorListener(this);
            binding.imgDelete.setOnClickListener(this);
            binding.imgClose.setOnClickListener(this);
            binding.imgDone.setOnClickListener(this);
            binding.imgSticker.setOnClickListener(this);

            //   lp.addRule(RelativeLayout.CENTER_IN_PARENT);

            binding.videoSurface.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                    Surface surface = new Surface(surfaceTexture);

                    try {
                        mediaPlayer = new MediaPlayer();

                        Log.d("VideoPath>>", videoPath);
                        mediaPlayer.setDataSource(videoPath);
                        mediaPlayer.setSurface(surface);
                        mediaPlayer.prepare();
                        mediaPlayer.setOnCompletionListener(onCompletionListener);
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.start();
                    } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

                }
            });

            exeCmd = new ArrayList<>();

//            if (FFmpeg.getInstance(this).isSupported()) {
//                // ffmpeg is supported
//                Log.e("FFMPEG", "  ffmpeg is supported ");
//            } else {
//                // ffmpeg is not supported
//                Log.e("FFMPEG", "  ffmpeg is not supported ");
//            }
            /*try {
                fFmpeg.loadBinary(new FFmpegLoadBinaryResponseHandler() {
                    @Override
                    public void onFailure() {
                        Log.d("binaryLoad", "onFailure");

                    }

                    @Override
                    public void onSuccess() {
                        Log.d("binaryLoad", "onSuccess");
                    }

                    @Override
                    public void onStart() {
                        Log.d("binaryLoad", "onStart");

                    }

                    @Override
                    public void onFinish() {
                        Log.d("binaryLoad", "onFinish");

                    }
                });
            } catch (FFmpegNotSupportedException e) {
                e.printStackTrace();
            }*/


        }
    }

    private void setCanvasAspectRatio() {
        originalDisplayHeight = getDisplayHeight();
        originalDisplayWidth = getDisplayWidth();

        DimensionData displayDiamenion =
                getScaledDimension(new DimensionData((int) DRAW_CANVASW, (int) DRAW_CANVASH),
                        new DimensionData(originalDisplayWidth, originalDisplayHeight));

        newCanvasWidth = displayDiamenion.width;
        newCanvasHeight = displayDiamenion.height;
    }

    private int getDisplayWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private int getDisplayHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private void getImageList() {
        adapter = new ImageCanvasAdapter(ImageCanvasActivity.this, stringImageList);
        Log.e("CATEGORY_NAME_tracker", " 1 " + category_name + " tracker:-- " + cat_tracker);
        // if (category_name.equalsIgnoreCase("Greetings")) {
        if (cat_tracker.equalsIgnoreCase("2")) {

            CustomGridLayoutManager customGridLayoutManager = new CustomGridLayoutManager(this);
            customGridLayoutManager.setScrollEnabled(false);
            binding.imageRecyclerview.setLayoutManager(customGridLayoutManager);
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            binding.imageRecyclerview.setLayoutManager(linearLayoutManager);
        }
        binding.imageRecyclerview.scrollToPosition(Integer.parseInt(position));
        if (binding.imageRecyclerview.getOnFlingListener() == null) {

            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(binding.imageRecyclerview);
        }
        binding.imageRecyclerview.setAdapter(adapter);

    }

    private void getOnClickListeners() {

        binding.addText.setOnClickListener(this);
        binding.imageviewBackBtn.setOnClickListener(this);
        //  binding.businessIconClose.setOnClickListener(this);
        binding.text.setOnClickListener(this);
        binding.textlay.setOnClickListener(this);
        binding.textcolor.setOnClickListener(this);
        binding.textsize.setOnClickListener(this);
        binding.fontstyle.setOnClickListener(this);
        binding.pickimage.setOnClickListener(this);
        binding.settings.setOnClickListener(this);
        binding.imageView.setOnClickListener(this);
        binding.movableEditText.setOnClickListener(this);
        binding.imageShare.setOnClickListener(this);
        binding.videoShare.setOnClickListener(this);
        binding.editBg.setOnClickListener(this);
        binding.frame.setOnClickListener(this);
        setting_logo_switch.setOnCheckedChangeListener(this);
        setting_socialmedia_switch.setOnCheckedChangeListener(this);
        setting_website_switch.setOnCheckedChangeListener(this);
        setting_email_switch.setOnCheckedChangeListener(this);
        setting_phone_switch.setOnCheckedChangeListener(this);
        setting_address_switch.setOnCheckedChangeListener(this);
        setting_whatsapp_switch.setOnCheckedChangeListener(this);
       //new frame condition
        //radio29
        if (binding.twentynineNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.twentynineNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.twentynineNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.twentynineNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.twentynineNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.twentynineNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.twentynineNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.twentynineNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.twentynineNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.twentynineNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
        if (binding.twentynineNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.twentynineNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);

        }

        //radio30
        if (binding.thirtyNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.thirtyNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.thirtyNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.thirtyNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.thirtyNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.thirtyNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.thirtyNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.thirtyNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.thirtyNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.thirtyNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
     /*   if (binding.thirtyNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtyNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);

        }*/
        //radio31
        if (binding.thirtyoneNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.thirtyoneNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.thirtyoneNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.thirtyoneNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.thirtyoneNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.thirtyoneNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.thirtyoneNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.thirtyoneNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.thirtyoneNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.thirtyoneNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
       /* if (binding.thirtyoneNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtyoneNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
         }*/
        //radio32
        if (binding.thirtytwoNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.thirtytwoNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.thirtytwoNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.thirtytwoNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.thirtytwoNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.thirtytwoNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.thirtytwoNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.thirtytwoNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.thirtytwoNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.thirtytwoNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
       /* if (binding.thirtytwoNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtytwoNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }
*/
        //radio33
        if (binding.thirtythreeNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.thirtythreeNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.thirtythreeNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.thirtythreeNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.thirtythreeNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.thirtythreeNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.thirtythreeNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.thirtythreeNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.thirtythreeNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.thirtythreeNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
        /*if (binding.thirtythreeNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtythreeNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }*/

        //radio34
        if (binding.thirtyfourNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.thirtyfourNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.thirtyfourNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.thirtyfourNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.thirtyfourNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.thirtyfourNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.thirtyfourNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.thirtyfourNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.thirtyfourNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.thirtyfourNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
       /* if (binding.thirtyfourNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtyfourNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }
*/
        //radio35
        if (binding.thirtyfiveNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.thirtyfiveNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.thirtyfiveNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.thirtyfiveNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.thirtyfiveNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.thirtyfiveNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.thirtyfiveNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.thirtyfiveNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.thirtyfiveNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.thirtyfiveNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
       /* if (binding.thirtyfiveNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtyfiveNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }
*/
        //radio36

        if (binding.thirtysixthNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.thirtysixthNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.thirtysixthNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.thirtysixthNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
       /*     if (binding.thirtysixthNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.thirtysixthNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.thirtysixthNew.businessWebsiteDetails.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.thirtysixthNew.businessWebsiteDetails.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.thirtysixthNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.thirtysixthNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }*/
      /*  if (binding.thirtysixthNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtysixthNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }*/

        //radio37
        if (binding.thirtysevenNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.thirtysevenNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.thirtysevenNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.thirtysevenNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.thirtysevenNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.thirtysevenNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.thirtysevenNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.thirtysevenNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.thirtysevenNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.thirtysevenNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
/*        if (binding.thirtysevenNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtysevenNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }*/
        //radio38
        if (binding.thirtyeightNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.thirtyeightNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.thirtyeightNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.thirtyeightNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.thirtyeightNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.thirtyeightNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.thirtyeightNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.thirtyeightNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.thirtyeightNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.thirtyeightNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
       /* if (binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }*/
        //radio39
        if (binding.thirtynineNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.thirtynineNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.thirtynineNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.thirtynineNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.thirtynineNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.thirtynineNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.thirtynineNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.thirtynineNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.thirtynineNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.thirtynineNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
       /* if (binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }*/


        //radio40
        if (binding.fortyNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.fortyNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.fortyNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.fortyNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.fortyNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.fortyNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.fortyNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.fortyNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.fortyNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.fortyNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
       /* if (binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }*/


        //radio41
        if (binding.fortyoneNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.fortyoneNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.fortyoneNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.fortyoneNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.fortyoneNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.fortyoneNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        /*if (binding.fortyoneNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.fortyoneNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.fortyoneNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.fortyoneNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
*/
       /* if (binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }*/


        //radio42
        if (binding.fortytwoNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.fortytwoNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.fortytwoNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.fortytwoNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.fortytwoNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.fortytwoNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.fortytwoNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.fortytwoNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.fortytwoNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.fortytwoNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
       /* if (binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }*/
        //radio43
        if (binding.fortythreeNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.fortythreeNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.fortythreeNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.fortythreeNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.fortythreeNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.fortythreeNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.fortythreeNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.fortythreeNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.fortythreeNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.fortythreeNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
       /* if (binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }*/
        //radio44
        if (binding.fortyfourNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {

            setting_socialmedia_switch.setChecked(true);
        } else if (binding.fortyfourNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.fortyfourNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.fortyfourNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.fortyfourNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.fortyfourNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
       /* if (binding.fortyfourNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.fortyfourNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }*/
       /* if (binding.fortyfourNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.fortyfourNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }*/
       /* if (binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }*/
        //radio45
        if (binding.fortyfiveNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.fortyfiveNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.fortyfiveNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.fortyfiveNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.fortyfiveNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.fortyfiveNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.fortyfiveNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.fortyfiveNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.fortyfiveNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.fortyfiveNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
       /* if (binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }*/
        //radio46
        if (binding.fortysixNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.fortysixNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.fortysixNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.fortysixNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.fortysixNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.fortysixNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
      /*  if (binding.fortysixNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.fortysixNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }*/
        if (binding.fortysixNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.fortysixNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
       /* if (binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }*/
        //radio47
        if (binding.fortysevenNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.fortysevenNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.fortysevenNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.fortysevenNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.fortysevenNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.fortysevenNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.fortysevenNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.fortysevenNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.fortysevenNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.fortysevenNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
       /* if (binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }*/

        //radio48
        /*if (binding.fortyeightNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.fortyeightNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }*/
        if(binding.fortyeightNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.fortyeightNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
       /* if (binding.fortyeightNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.fortyeightNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.fortyeightNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.fortyeightNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.fortyeightNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.fortyeightNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }*/
       /* if (binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }*/

        //radio49
        if (binding.fortynineNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.fortynineNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.fortynineNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.fortynineNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.fortynineNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.fortynineNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.fortynineNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.fortynineNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.fortynineNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.fortynineNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
       /* if (binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }*/

        //radio50
        if (binding.fiftyNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.fiftyNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.fiftyNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.fiftyNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.fiftyNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.fiftyNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.fiftyNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.fiftyNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.fiftyNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.fiftyNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
       /* if (binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }*/
        //radio51
        if (binding.fiftyoneNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.fiftyoneNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.fiftyoneNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.fiftyoneNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.fiftyoneNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.fiftyoneNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.fiftyoneNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.fiftyoneNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.fiftyoneNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.fiftyoneNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
       /* if (binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }*/
        //radio52
        if (binding.fiftytwoNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.fiftytwoNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.fiftytwoNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.fiftytwoNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.fiftytwoNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.fiftytwoNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.fiftytwoNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.fiftytwoNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.fiftytwoNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.fiftytwoNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
       /* if (binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }*/
        //radio53
        if (binding.fiftythreeNew.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.fiftythreeNew.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }
        if(binding.fiftythreeNew.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.fiftythreeNew.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }
        if (binding.fiftythreeNew.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.fiftythreeNew.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.fiftythreeNew.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.fiftythreeNew.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.fiftythreeNew.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.fiftythreeNew.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }
       /* if (binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.VISIBLE){
            setting_whatsapp_switch.setChecked(true);

        }else if(binding.thirtyeightNew.whatsappMainRelative.getVisibility() == View.GONE){
            setting_whatsapp_switch.setChecked(false);
        }*/


        //old frame condition


        if(binding.first.businessLogoRelative.getVisibility() == View.VISIBLE) {
            setting_logo_switch.setChecked(true);
        } else if (binding.first.businessLogoRelative.getVisibility() == View.GONE) {
            setting_logo_switch.setChecked(false);
        }

        if (binding.first.businessSocialmediaImageRelative.getVisibility() == View.VISIBLE) {
            setting_socialmedia_switch.setChecked(true);
        } else if (binding.first.businessSocialmediaImageRelative.getVisibility() == View.GONE) {
            setting_socialmedia_switch.setChecked(false);
        }

        if (binding.first.callMainRelative.getVisibility() == View.VISIBLE) {
            setting_phone_switch.setChecked(true);
        } else if (binding.first.callMainRelative.getVisibility() == View.GONE) {
            setting_phone_switch.setChecked(false);
        }
        if (binding.first.locationMainRelative.getVisibility() == View.VISIBLE) {
            setting_address_switch.setChecked(true);
        } else if (binding.first.locationMainRelative.getVisibility() == View.GONE) {
            setting_address_switch.setChecked(false);
        }
        if (binding.first.websiteMainRelative.getVisibility() == View.VISIBLE) {
            setting_website_switch.setChecked(true);
        } else if (binding.first.websiteMainRelative.getVisibility() == View.GONE) {
            setting_website_switch.setChecked(false);
        }
        if (binding.first.mailMainRelative.getVisibility() == View.VISIBLE) {
            setting_email_switch.setChecked(true);
        } else if (binding.first.mailMainRelative.getVisibility() == View.GONE) {
            setting_email_switch.setChecked(false);
        }

       /* binding.businessIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // binding.businessIconClose.setVisibility(View.VISIBLE);
                return false;
            }
        });
        */

        binding.movableEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (v.getId() == R.id.movable_edit_text) {

                    binding.movableEditText.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            switch (event.getActionMasked()) {
                                case MotionEvent.ACTION_DOWN:
                                    xDown = event.getX();
                                    yDown = event.getY();
                                    return true;
                                //break;
                                case MotionEvent.ACTION_MOVE:
                                    float movedX, movedY;
                                    movedX = event.getX();
                                    movedY = event.getY();

                                    float distenceX = movedX - xDown;
                                    float distenceY = movedY - yDown;

                                    binding.movableEditText.setX(binding.movableEditText.getX() + distenceX);
                                    binding.movableEditText.setY(binding.movableEditText.getY() + distenceY);

                            /*    xDown = movedX;
                            yDown = movedY;*/
                                    return true;
                                //break;
                                case MotionEvent.ACTION_UP:
                                    return false;
                                //break;
                            }
                            binding.rel.invalidate();
                            return true;
                        }
                    });
                }

                return false;
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getThemeData() {

        themeList = new ArrayList<>();
        for (int i = 1; i < 54; i++) {

//           themeList.add("https://webknight.co.in/grow_your_bussiness/assets/webp/frame_img_" + i + ".webp");
            //themeList.add("frame_img_" + i + ".webp");
            themeList.add("http://thebrandshaastra.com/admin/assets/frame1/frame_img_" + i + ".webp");
        }
        themeAdapter = new ThemeAdapter(ImageCanvasActivity.this, themeList, this);
        binding.themes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.themes.setAdapter(themeAdapter);
    }

    @Override
    public void getThemeItem(int value) {
        selectedTheme = value;
        selected_theme_no = value;
        Log.e("selected_theme_no", "" + selected_theme_no);
        switch (selectedTheme) {

            case 0:

                binding.firstFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);


                //old layout
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;
            case 1:

                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;
            case 2:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;

            case 3:

                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;

            case 4:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;

            case 5:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;

            case 6:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;

            case 7:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;

            case 8:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;

            case 9:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;

            case 10:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;

            case 11:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;

            case 12:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;
            case 13:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;

            case 14:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;
            case 15:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;
            case 16:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;
            case 17:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;
            case 18:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;
            case 19:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;
            case 20:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;

            case 21:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;
            case 22:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;
            case 23:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 24:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 25:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.VISIBLE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 26:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.VISIBLE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 27:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.VISIBLE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 28:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.VISIBLE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 29:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.VISIBLE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 30:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.VISIBLE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 31:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.VISIBLE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 32:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.VISIBLE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 33:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.VISIBLE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 34:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.VISIBLE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 35:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.VISIBLE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 36:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.VISIBLE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 37:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.VISIBLE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 38:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.VISIBLE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 39:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.VISIBLE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 40:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.VISIBLE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 41:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.VISIBLE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 42:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.VISIBLE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 43:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 44:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.VISIBLE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 45:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.VISIBLE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 46:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.VISIBLE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 47:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.VISIBLE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 48:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.VISIBLE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 49:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.VISIBLE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 50:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.VISIBLE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 51:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.VISIBLE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            case 52:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.VISIBLE);

                break;
            case 53:
                binding.firstFrameIncludeNew.setVisibility(View.GONE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.firstFrameInclude.setVisibility(View.GONE);
                binding.secondFrameInclude.setVisibility(View.GONE);
                binding.thirdFrameInclude.setVisibility(View.GONE);
                binding.forthFrameInclude.setVisibility(View.GONE);
                binding.fifthFrameInclude.setVisibility(View.GONE);
                binding.sixthFrameInclude.setVisibility(View.GONE);
                binding.seventhFrameInclude.setVisibility(View.GONE);
                binding.eightFrameInclude.setVisibility(View.GONE);
                binding.ninethFrameInclude.setVisibility(View.GONE);
                binding.tenthFrameInclude.setVisibility(View.GONE);
                binding.eleventhFrameInclude.setVisibility(View.GONE);
                binding.twelvethFrameInclude.setVisibility(View.GONE);
                binding.thirteenFrameInclude.setVisibility(View.GONE);
                binding.fourteenFrameInclude.setVisibility(View.GONE);
                binding.fifteenFrameInclude.setVisibility(View.GONE);
                binding.sixteenthFrameInclude.setVisibility(View.GONE);
                binding.seventeenthFrameInclude.setVisibility(View.GONE);
                binding.eighteenFrameInclude.setVisibility(View.GONE);
                binding.nineteenFrameInclude.setVisibility(View.GONE);
                binding.twentyFrameInclude.setVisibility(View.GONE);
                binding.twentyoneFrameInclude.setVisibility(View.GONE);
                binding.twentytwoFrameInclude.setVisibility(View.GONE);
                binding.twentythreeFrameInclude.setVisibility(View.GONE);
                binding.twentyfourFrameInclude.setVisibility(View.GONE);
                binding.twentyfiveFrameInclude.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);

                break;
            default:
                binding.firstFrameIncludeNew.setVisibility(View.VISIBLE);
                binding.thirdFrameIncludeNew.setVisibility(View.GONE);
                binding.secondFrameIncludeNew.setVisibility(View.GONE);
                binding.forthFrameIncludeNew.setVisibility(View.GONE);
                binding.fifthFrameIncludeNew.setVisibility(View.GONE);
                binding.sixthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventhFrameIncludeNew.setVisibility(View.GONE);
                binding.eightFrameIncludeNew.setVisibility(View.GONE);
                binding.ninethFrameIncludeNew.setVisibility(View.GONE);
                binding.tenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eleventhFrameIncludeNew.setVisibility(View.GONE);
                binding.twelvethFrameIncludeNew.setVisibility(View.GONE);
                binding.thirteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fourteenFrameIncludeNew.setVisibility(View.GONE);
                binding.fifteenFrameIncludeNew.setVisibility(View.GONE);
                binding.sixteenthFrameIncludeNew.setVisibility(View.GONE);
                binding.seventeenthFrameIncludeNew.setVisibility(View.GONE);
                binding.eighteenFrameIncludeNew.setVisibility(View.GONE);
                binding.nineteenFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyoneFrameIncludeNew.setVisibility(View.GONE);
                binding.twentytwoFrameIncludeNew.setVisibility(View.GONE);
                binding.twentythreeFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfourFrameIncludeNew.setVisibility(View.GONE);
                binding.twentyfiveFrameIncludeNew.setVisibility(View.GONE);
                binding.twentysixFrameInclude.setVisibility(View.GONE);
                binding.twentysevenFrameInclude.setVisibility(View.GONE);
                binding.twentyeightFrameInclude.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.addText:
                if (binding.movableEditText.getVisibility() == View.GONE) {
                    binding.movableEditText.setVisibility(View.VISIBLE);

                 /*   Typeface face = Typeface.createFromAsset(getAssets(),
                            "fonts/roboto.ttf");
                    binding.movableEditText.setTypeface(face);*/
                    //  rotateZoomImageView.addText(binding.movableEditText.getText().toString(),Color.BLACK);

                } else {
                    binding.movableEditText.setVisibility(View.GONE);
                }
                break;

            case R.id.imgDone:
                File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Brand Shaastra");
                if (!pictureFileDir.exists()) {
                    boolean isDirectoryCreated = pictureFileDir.mkdirs();

                }
                if (video_flag) {
                    shareImage("2");
                } else {
                    shareImage("3");
                }
                break;
            case R.id.video_share:
                shareImage("2");
                break;
            case R.id.imgClose:
                onBackPressed();
                break;
            case R.id.imgSticker:
                mStickerBSFragment.show(getSupportFragmentManager(), mStickerBSFragment.getTag());
                break;
           /* case R.id.business_icon_close:

                binding.businessImageRelative.setVisibility(View.GONE);
                break;*/

            case R.id.text:
                break;
            case R.id.frame:

                //  getFrameClick();
                break;

            case R.id.textlay:
                break;

            case R.id.textcolor:

                changeFontColor();
                break;

            case R.id.textsize:

                getBottomDialog("0");
                break;

            case R.id.fontstyle:
                getBottomDialog("1");
                break;

            case R.id.pickimage:

               /* if (video_flag) {
                    binding.imgSticker.performClick();
                } else {
*/
                ImagePicker.Companion.with(this)
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .start(23);
                // }

                break;

            case R.id.edit_bg:
                setBackgroundColor();
                break;

            case R.id.image_share:
                shareImage("1");
                break;

            case R.id.movable_edit_text:
                break;

            case R.id.imageview_back_btn:
                onBackPressed();
                finish();
                break;

            case R.id.settings:
                getBottomDialog("2");
                break;
            case R.id.imageView:

                String a = binding.edittext.getText().toString();
                binding.text.setText(a);
                binding.text.setVisibility(View.VISIBLE);
                binding.textlay.setVisibility(View.GONE);

                break;

        }
    }

    private void saveimgFromBitmap() {
        Log.e("VODEO", "vide create 2");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1011);
            } else {

                if (ContextCompat.checkSelfPermission(ImageCanvasActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ImageCanvasActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                } else {

                    File file = saveBitMapFromImage(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
                    if (file != null) {
                        Log.i("SHIVAKASHI", "Drawing saved to the gallery!");
                    } else {
                        Log.i("SHIVAKASHI", "Oops! Image could not be saved.");
                    }
                }
            }
        }
    }

    private File saveBitMapFromImage(Context context, View drawView) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Brand Shaastra");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i("ATG", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpeg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, oStream);
            oStream.flush();
            oStream.close();
            Toast.makeText(ImageCanvasActivity.this, "Image saved at Pictures/Brand Shaastra in your file manager", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        return pictureFile;
    }

    private static Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    public Bitmap fastblur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    @SuppressLint("MissingPermission")
    private void saveImage() {

        Log.e("VODEO", "vide create");
        Bitmap map = takeScreenShot(ImageCanvasActivity.this);

        Bitmap fast = fastblur(map, 10);
        final Drawable draw = new BitmapDrawable(getResources(), fast);

        WindowManager.LayoutParams lp = progressDialog.getWindow().getAttributes();
        //lp.dimAmount=0.0f;
        progressDialog.getWindow().setAttributes(lp);
        progressDialog.setMessage("Creating video for you");
        progressDialog.show();
        //   progressDialog.getWindow().setBackgroundDrawable(draw);

        if (selected_theme_no == 0) {
            //binding.first.frame1.setPadding(5, 5, 5, 5);
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 1) {
//            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 2) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 3) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 4) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 5) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 6) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 7) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 8) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 9) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 10) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 11) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 12) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 13) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 14) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 15) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 16) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 17) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 18) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 19) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 20) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 21) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 22) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 23) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 24) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 25) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 26) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        } else if (selected_theme_no == 27) {
            saveBitMapForVideo(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //       checkFolder();
        /*File file, outputnew;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "shivam.png");
            Log.e("root", String.valueOf(file));

        } else {
            // root = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + context.getString(R.string.app_name) + "/temp");
//            file = new File(Environment.getExternalStorageDirectory() +
//                    fileN);
            file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "shivam.png");
            outputnew = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "output.mp4");
        }*/

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator + +System.currentTimeMillis()
                + ".png");


        //video_img_path = file.getPath();
        //String path = FileUtility.getPath(ImageCanvasActivity.this, Uri.parse(video_img_path));

        //File image_file = new File(path);

        //  Log.e("video_img_path", "" + video_img_path);
        //   Log.e("video_img_path", " img_path " + file.toString());
      /*  try {
//            file.createNewFile();

            SaveSettings saveSettings = new SaveSettings.Builder()
                    *//*.setClearViewsEnabled(true)
                    .setTransparencyEnabled(true)*//*
                    .build();


            mPhotoEditor.saveAsFile(file.getAbsolutePath(), saveSettings, new PhotoEditor.OnSaveListener() {
                @SuppressLint("MissingPermission")
                @Override
                public void onSuccess(@NonNull String imagePath) {
                    ImageCanvasActivity.this.imagePath = imagePath;
                    Log.d("imagePath>>", imagePath);
                    Log.d("imagePath2>>", Uri.fromFile(new File(imagePath)).toString());
                    binding.videoImage.getSource().setImageURI(Uri.fromFile(new File(imagePath)));
                    //    Toast.makeText(ImageCanvasActivity.this, "Saved successfully...", Toast.LENGTH_SHORT).show();
                    applayWaterMark();
                }

                @Override
                public void onFailure(@NonNull Exception exception) {
                    //    Toast.makeText(ImageCanvasActivity.this, "Saving Failed...", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }*/

        downloadVideo();

    }

    private void downloadVideo() {

        downloadVideoparams.put("video_path", videoPath);
        downloadVideoparams.put("video_height", String.valueOf(DRAW_CANVASH));
        downloadVideoparams.put("video_width", String.valueOf(DRAW_CANVASW));
        downloadVideoparams.put("video_url2", video_url2);
        downloadVideoparams.put(Consts.USER_ID, userDTO.getUser_id());
        Log.e("downloadVideo_res", " video_url2:--  " + video_url2);
        Log.e("downloadVideo_res", " params " + downloadVideoparams.toString());
        // Log.d("mytag", " params " + downloadVideoparams.toString());

        new HttpsRequest(Consts.VISEO_FFMPEG_API, downloadVideoparams, this).stringPos2("downloadVideo", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Log.e("downloadVideo_res", "" + response.toString());

                    String uploaded_video_url = null;
                    try {
                        uploaded_video_url = response.getString("data");
                        Log.e("uploaded_video_url", "" + uploaded_video_url);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    VIDEO_FFMPEG_flag = true;
                    DownloadTask downloadTask = new DownloadTask(ImageCanvasActivity.this);
                    downloadTask.execute(uploaded_video_url);
                } else {

                }
            }
        });
    }

    public void saveBitMapImage(Context context, Bitmap b, String imageName) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);

            Log.e("IMAGE_COM", "" + b.compress(Bitmap.CompressFormat.PNG, 100, fos));
        } catch (FileNotFoundException e) {
            Log.d("TAG", "file not found");
            e.printStackTrace();
        } finally {
            fos.close();
        }
    }

    public class DownloadTask extends AsyncTask<String, Integer, String> {
        private Context context;
        private PowerManager.WakeLock mWakeLock;
        String vid_url = "";

        public DownloadTask(Context context) {
            this.context = context;
        }

        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(ImageCanvasActivity.this);

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                java.net.URL url = new URL(sUrl[0]);
                vid_url = sUrl[0];
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();

                input = connection.getInputStream();
                fileN = "/TBS_" + UUID.randomUUID().toString().substring(0, 10) + ".mp4";
//                fileN = "/.shivam.mp4";
                File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Brand Shaastra");

                File filename = new File(pictureFileDir, fileN);
                output = new FileOutputStream(filename);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    if (fileLength > 0)
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            LayoutInflater dialogLayout = LayoutInflater.from(ImageCanvasActivity.this);
            // downloadDialog.setContentView(DialogView);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            // lp.copyFrom(downloadDialog.getWindow().getAttributes());
            lp.width = (getResources().getDisplayMetrics().widthPixels);
            lp.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.65);
            //downloadDialog.setAttributes(lp);

            // final Button cancel = (Button) DialogView.findViewById(R.id.cancel_btn);
            downloadDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cancel(true);
                    dialog.dismiss();
                }
            });

            downloadDialog.setCancelable(false);
          /*  bnp = (NumberProgressBar)DialogView.findViewById(R.id.number_progress_bar);
            bnp.setProgress(0);
            bnp.setMax(100);*/
            // downloadDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            //bnp.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
//            Toast.makeText(ImageCanvasActivity.this, "Completed", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            Log.e("SHIVAKASHI", String.valueOf(fileN));
            /*String newpath = Environment.getExternalStorageDirectory().getAbsolutePath() + fileN;
            if (VIDEO_FFMPEG_flag) {

                Intent i = new Intent(ImageCanvasActivity.this, VideoPreviewActivity.class);
                i.putExtra("DATA", newpath);
                startActivity(i);
                finish();
            }

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(String.valueOf(newpath));
            String metaRotation = retriever.extractMetadata(METADATA_KEY_VIDEO_ROTATION);

            int rotation = metaRotation == null ? 0 : Integer.parseInt(metaRotation);
            if (rotation == 90 || rotation == 270) {
                DRAW_CANVASH = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                DRAW_CANVASW = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            } else {
                DRAW_CANVASW = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                DRAW_CANVASH = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            }*/
            /*if (result != null) {

                //  Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            } else*/
            //  Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
            File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Brand Shaastra");
            MediaScannerConnection.scanFile(ImageCanvasActivity.this,
                    new String[]{pictureFileDir + fileN}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String newpath, Uri newuri) {
                            Log.i("ExternalStorage", "Scanned " + newpath + ":");
                            Log.i("ExternalStorage", "-> uri=" + newuri);

                            if (VIDEO_FFMPEG_flag) {

                                Intent i = new Intent(ImageCanvasActivity.this, VideoPreviewActivity.class);
                                i.putExtra("DATA", newpath);
                                startActivity(i);
                                finish();
                            }

                            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                            retriever.setDataSource(String.valueOf(newpath));
                            String metaRotation = retriever.extractMetadata(METADATA_KEY_VIDEO_ROTATION);

                            int rotation = metaRotation == null ? 0 : Integer.parseInt(metaRotation);
                            if (rotation == 90 || rotation == 270) {
                                DRAW_CANVASH = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                                DRAW_CANVASW = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                            } else {
                                DRAW_CANVASW = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                                DRAW_CANVASH = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                            }
                            setCanvasAspectRatio();
                        }
                    });
        }
    }

    /*private void applayWaterMark() {

        File output = new File(Environment.getExternalStorageDirectory()
                + File.separator + ""
                + System.currentTimeMillis() + ".mp4");
        try {
            output.createNewFile();

            exeCmd.add("-y");
            exeCmd.add("-i");
            exeCmd.add("/storage/emulated/0/.shivam.mp4");
            exeCmd.add("-i");
            exeCmd.add("/storage/emulated/0/.shivam.png");
            exeCmd.add("-filter_complex");
            exeCmd.add("[1:v]scale=" + DRAW_CANVASW + ":" + DRAW_CANVASH + "[ovrl];[0:v][ovrl]overlay=x=0:y=0");
            exeCmd.add("-c:v");
            exeCmd.add("libx264");
            exeCmd.add("-preset");
            exeCmd.add("ultrafast");
            exeCmd.add(output.getAbsolutePath());

            newCommand = new String[exeCmd.size()];
            for (int j = 0; j < exeCmd.size(); j++) {
                newCommand[j] = exeCmd.get(j);
            }


            for (int k = 0; k < newCommand.length; k++) {
                Log.d("CMD==>>", newCommand[k] + "");
            }
            Log.e("TAG", "applayWaterMark:" + Arrays.toString(newCommand));
            executeCommand(newCommand, output.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void executeCommand(String[] command, final String absolutePath) {
        try {
            fFmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onSuccess(String s) {
                    Log.d("CommandExecute", "onSuccess" + "  " + s);
                    //   Toast.makeText(getApplicationContext(), "Sucess", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ImageCanvasActivity.this, VideoPreviewActivity.class);
                    i.putExtra("DATA", absolutePath);
                    startActivity(i);
                    finish();
                }

                @Override
                public void onProgress(String s) {
                    //progressDialog.setMessage(s);
                    Log.d("CommandExecute", "onProgress" + "  " + s);

                }

                @Override
                public void onFailure(String s) {
                    //Log.d("CommandExecute", "onFailure" + "  " + s);
                    progressDialog.hide();

                }

                @Override
                public void onStart() {
                    progressDialog.setIcon(R.drawable.brand_shaastra_logo);
                    progressDialog.setTitle("Preccesing");
                    progressDialog.setMessage("Creating video for you...");
                    progressDialog.show();
                }

                @Override
                public void onFinish() {

                    File dir = getFilesDir();
                    File file = new File(dir, ".shivam.mp4");
                    boolean deleted = file.delete();
                    progressDialog.hide();
                }
            });
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
    }*/

/*
    private void getFrameClick() {
        if (frame_flag) {
            Log.e("frame1234", "1");
            frame_flag = false;

            binding.bgView.setVisibility(View.GONE);
            if (image_background_flag && image_background_color != -1) {
                binding.first.frame1.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad = (GradientDrawable) binding.first.frame1.getBackground();
                myGrad.setColor(image_background_color);

                binding.second.frame2.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad2 = (GradientDrawable) binding.second.frame2.getBackground();
                myGrad2.setColor(image_background_color);

                binding.third.frame3.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad3 = (GradientDrawable) binding.third.frame3.getBackground();
                myGrad3.setColor(image_background_color);

                binding.forth.frame4.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad4 = (GradientDrawable) binding.forth.frame4.getBackground();
                myGrad4.setColor(image_background_color);

                binding.fifth.frame5.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad5 = (GradientDrawable) binding.fifth.frame5.getBackground();
                myGrad5.setColor(image_background_color);

                binding.sixth.frame6.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad6 = (GradientDrawable) binding.sixth.frame6.getBackground();
                myGrad6.setColor(image_background_color);

                binding.seventh.frame7.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad7 = (GradientDrawable) binding.seventh.frame7.getBackground();
                myGrad7.setColor(image_background_color);

                binding.eight.frame8.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad8 = (GradientDrawable) binding.eight.frame8.getBackground();
                myGrad8.setColor(image_background_color);

                binding.nineth.frame9.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad9 = (GradientDrawable) binding.nineth.frame9.getBackground();
                myGrad9.setColor(image_background_color);

                binding.tenth.frame10.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad10 = (GradientDrawable) binding.tenth.frame10.getBackground();
                myGrad10.setColor(image_background_color);

                binding.eleven.frame11.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad11 = (GradientDrawable) binding.eleven.frame11.getBackground();
                myGrad11.setColor(image_background_color);

                binding.twelve.frame12.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad12 = (GradientDrawable) binding.twelve.frame12.getBackground();
                myGrad12.setColor(image_background_color);

                binding.thirteen.frame13.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad13 = (GradientDrawable) binding.thirteen.frame13.getBackground();
                myGrad13.setColor(image_background_color);

                binding.fourteen.frame14.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad14 = (GradientDrawable) binding.fourteen.frame14.getBackground();
                myGrad14.setColor(image_background_color);

                binding.fifteen.frame15.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad15 = (GradientDrawable) binding.fifteen.frame15.getBackground();
                myGrad15.setColor(image_background_color);

                binding.sixteenth.frame16.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad16 = (GradientDrawable) binding.sixteenth.frame16.getBackground();
                myGrad16.setColor(image_background_color);

                binding.seventeenth.frame17.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad17 = (GradientDrawable) binding.seventeenth.frame17.getBackground();
                myGrad17.setColor(image_background_color);

                binding.eighteen.frame18.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad18 = (GradientDrawable) binding.eighteen.frame18.getBackground();
                myGrad18.setColor(image_background_color);

                binding.nineteen.frame19.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad19 = (GradientDrawable) binding.nineteen.frame19.getBackground();
                myGrad19.setColor(image_background_color);

                binding.twenty.frame20.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad20 = (GradientDrawable) binding.twenty.frame20.getBackground();
                myGrad20.setColor(image_background_color);

                binding.twentyone.frame21.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad21 = (GradientDrawable) binding.twentyone.frame21.getBackground();
                myGrad21.setColor(image_background_color);

                binding.twentytwo.frame22.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad22 = (GradientDrawable) binding.twentytwo.frame22.getBackground();
                myGrad22.setColor(image_background_color);

                binding.twentythree.frame23.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad23 = (GradientDrawable) binding.twentythree.frame23.getBackground();
                myGrad23.setColor(image_background_color);

                binding.twentyfour.frame24.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad24 = (GradientDrawable) binding.twentyfour.frame24.getBackground();
                myGrad24.setColor(image_background_color);

                binding.twentyfive.frame25.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad25 = (GradientDrawable) binding.twentyfive.frame25.getBackground();
                myGrad25.setColor(image_background_color);

                binding.twentysix.frame26.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad26 = (GradientDrawable) binding.twentysix.frame26.getBackground();
                myGrad26.setColor(image_background_color);

                binding.twentyseven.frame27.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad27 = (GradientDrawable) binding.twentyseven.frame27.getBackground();
                myGrad27.setColor(image_background_color);

                binding.twentyeight.frame28.setBackground(getResources().getDrawable(R.drawable.border_frame));
                GradientDrawable myGrad28 = (GradientDrawable) binding.twentyeight.frame28.getBackground();
                myGrad28.setColor(image_background_color);

            }
            // binding.rel.setBackground(ContextCompat.getDrawable(this, R.drawable.border_frame));
            //   binding.rel2.setBackgroundColor(frame_border_color);
               */
/*     GradientDrawable myGrad = (GradientDrawable) binding.rel.getBackground();
                    myGrad.setStroke(3, frame_border_color);
*//*

     */
/*                    ViewGroup.MarginLayoutParams layoutParams =
                            (ViewGroup.MarginLayoutParams) binding.rel.getLayoutParams();
                    layoutParams.(2, 2, 2, 2);*//*

            if (frame_border_color != 0) {
                Log.e("FRAME", " if ma gayu");
                */
/*GradientDrawable myGrad = (GradientDrawable) binding.frameRelative.getBackground();
                myGrad.setColor(frame_border_color);*//*

                binding.frameRelative.setBackground(ContextCompat.getDrawable(this, R.drawable.top_curved_color_card_bg));
                GradientDrawable myGrad = (GradientDrawable) binding.frameRelative.getBackground();
                myGrad.setStroke(5, frame_border_color);

            }
            //binding.frameRelative.setBackgroundColor(frame_border_color);
            binding.frameRelative.setPadding(5, 5, 5, 5);
        } else {
            Log.e("frame1234", "2");
            frame_flag = true;
            binding.frameRelative.setBackground(ContextCompat.getDrawable(this, R.drawable.top_curved_card_bg));

            if (image_background_flag && image_background_color != -1) {

                binding.bgView.setVisibility(View.GONE);
                binding.first.frame1.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad = (GradientDrawable) binding.first.frame1.getBackground();
                myGrad.setColor(image_background_color);

                binding.second.frame2.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad2 = (GradientDrawable) binding.second.frame2.getBackground();
                myGrad2.setColor(image_background_color);

                binding.third.frame3.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad3 = (GradientDrawable) binding.third.frame3.getBackground();
                myGrad3.setColor(image_background_color);

                binding.forth.frame4.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad4 = (GradientDrawable) binding.forth.frame4.getBackground();
                myGrad4.setColor(image_background_color);

                binding.fifth.frame5.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad5 = (GradientDrawable) binding.fifth.frame5.getBackground();
                myGrad5.setColor(image_background_color);

                binding.sixth.frame6.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad6 = (GradientDrawable) binding.sixth.frame6.getBackground();
                myGrad6.setColor(image_background_color);

                binding.seventh.frame7.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad7 = (GradientDrawable) binding.seventh.frame7.getBackground();
                myGrad7.setColor(image_background_color);

                binding.eight.frame8.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad8 = (GradientDrawable) binding.eight.frame8.getBackground();
                myGrad8.setColor(image_background_color);

                binding.nineth.frame9.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad9 = (GradientDrawable) binding.nineth.frame9.getBackground();
                myGrad9.setColor(image_background_color);

                binding.tenth.frame10.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad10 = (GradientDrawable) binding.tenth.frame10.getBackground();
                myGrad10.setColor(image_background_color);

                binding.eleven.frame11.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad11 = (GradientDrawable) binding.eleven.frame11.getBackground();
                myGrad11.setColor(image_background_color);

                binding.twelve.frame12.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad12 = (GradientDrawable) binding.twelve.frame12.getBackground();
                myGrad12.setColor(image_background_color);

                binding.thirteen.frame13.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad13 = (GradientDrawable) binding.thirteen.frame13.getBackground();
                myGrad10.setColor(image_background_color);

                binding.fourteen.frame14.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad14 = (GradientDrawable) binding.fourteen.frame14.getBackground();
                myGrad14.setColor(image_background_color);

                binding.fifteen.frame15.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad15 = (GradientDrawable) binding.fifteen.frame15.getBackground();
                myGrad15.setColor(image_background_color);

                binding.sixteenth.frame16.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad16 = (GradientDrawable) binding.sixteenth.frame16.getBackground();
                myGrad16.setColor(image_background_color);

                binding.seventeenth.frame17.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad17 = (GradientDrawable) binding.seventeenth.frame17.getBackground();
                myGrad17.setColor(image_background_color);

                binding.eighteen.frame18.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad18 = (GradientDrawable) binding.eighteen.frame18.getBackground();
                myGrad18.setColor(image_background_color);

                binding.nineteen.frame19.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad19 = (GradientDrawable) binding.nineteen.frame19.getBackground();
                myGrad19.setColor(image_background_color);

                binding.twenty.frame20.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad20 = (GradientDrawable) binding.twenty.frame20.getBackground();
                myGrad20.setColor(image_background_color);

                binding.twentyone.frame21.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad21 = (GradientDrawable) binding.twentyone.frame21.getBackground();
                myGrad21.setColor(image_background_color);

                binding.twentytwo.frame22.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad22 = (GradientDrawable) binding.twentytwo.frame22.getBackground();
                myGrad22.setColor(image_background_color);

                binding.twentythree.frame23.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad23 = (GradientDrawable) binding.twentythree.frame23.getBackground();
                myGrad23.setColor(image_background_color);

                binding.twentyfour.frame24.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad24 = (GradientDrawable) binding.twentyfour.frame24.getBackground();
                myGrad24.setColor(image_background_color);

                binding.twentyfive.frame25.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad25 = (GradientDrawable) binding.twentyfive.frame25.getBackground();
                myGrad25.setColor(image_background_color);

                binding.twentysix.frame26.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad26 = (GradientDrawable) binding.twentysix.frame26.getBackground();
                myGrad26.setColor(image_background_color);

                binding.twentyseven.frame27.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad27 = (GradientDrawable) binding.twentyseven.frame27.getBackground();
                myGrad27.setColor(image_background_color);

                binding.twentyeight.frame28.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                GradientDrawable myGrad28 = (GradientDrawable) binding.twentyeight.frame28.getBackground();
                myGrad28.setColor(image_background_color);

            } else {
                binding.bgView.setVisibility(View.VISIBLE);

            }
            binding.frameRelative.setPadding(0, 0, 0, 0);

            binding.imageToolsLinear.setBackground(ContextCompat.getDrawable(this, R.drawable.bottom_curved_card_bg));
        }
    }
*/

    private void changeFontColor() {

        ColorPickerDialogBuilder
                .with(ImageCanvasActivity.this)
                .setTitle("Choose color")
                .initialColor(getResources().getColor(R.color.blue_color))
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {

                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        //binding.rel.setBackgroundColor(selectedColor);
                        binding.movableEditText.setHintTextColor(selectedColor);
                        binding.movableEditText.setTextColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    private void setBackgroundColor() {


        ColorPickerDialogBuilder
                .with(ImageCanvasActivity.this)
                .setTitle("Choose color")
                .initialColor(getResources().getColor(R.color.blue_color))
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {

                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        //binding.rel.setBackgroundColor(selectedColor);
                        image_background_color = selectedColor;

                        Log.e("image_background_color", "" + image_background_color);
                        image_background_flag = true;
                        //   binding.bgView.setVisibility(View.GONE);
                        if (frame_flag) {

                            binding.first.frame1.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad = (GradientDrawable) binding.first.frame1.getBackground();
                            myGrad.setColor(image_background_color);

                            binding.second.frame2.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad2 = (GradientDrawable) binding.second.frame2.getBackground();
                            myGrad2.setColor(image_background_color);

                            binding.third.frame3.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad3 = (GradientDrawable) binding.third.frame3.getBackground();
                            myGrad3.setColor(image_background_color);

                            binding.forth.frame4.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad4 = (GradientDrawable) binding.forth.frame4.getBackground();
                            myGrad4.setColor(image_background_color);

                            binding.fifth.frame5.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad5 = (GradientDrawable) binding.fifth.frame5.getBackground();
                            myGrad5.setColor(image_background_color);

                            binding.sixth.frame6.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad6 = (GradientDrawable) binding.sixth.frame6.getBackground();
                            myGrad6.setColor(image_background_color);

                            binding.seventh.frame7.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad7 = (GradientDrawable) binding.seventh.frame7.getBackground();
                            myGrad7.setColor(image_background_color);

                            binding.eight.frame8.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad8 = (GradientDrawable) binding.eight.frame8.getBackground();
                            myGrad8.setColor(image_background_color);

                            binding.nineth.frame9.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad9 = (GradientDrawable) binding.nineth.frame9.getBackground();
                            myGrad9.setColor(image_background_color);

                            binding.tenth.frame10.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad10 = (GradientDrawable) binding.tenth.frame10.getBackground();
                            myGrad10.setColor(image_background_color);

                            binding.eleven.frame11.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad11 = (GradientDrawable) binding.eleven.frame11.getBackground();
                            myGrad11.setColor(image_background_color);

                            binding.twelve.frame12.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad12 = (GradientDrawable) binding.twelve.frame12.getBackground();
                            myGrad12.setColor(image_background_color);

                            binding.thirteen.frame13.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad13 = (GradientDrawable) binding.thirteen.frame13.getBackground();
                            myGrad13.setColor(image_background_color);

                            binding.fourteen.frame14.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad14 = (GradientDrawable) binding.fourteen.frame14.getBackground();
                            myGrad14.setColor(image_background_color);

                            binding.fifteen.frame15.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad15 = (GradientDrawable) binding.fifteen.frame15.getBackground();
                            myGrad15.setColor(image_background_color);

                            binding.sixteenth.frame16.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad16 = (GradientDrawable) binding.sixteenth.frame16.getBackground();
                            myGrad16.setColor(image_background_color);

                            binding.seventeenth.frame17.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad17 = (GradientDrawable) binding.seventeenth.frame17.getBackground();
                            myGrad17.setColor(image_background_color);

                            binding.eighteen.frame18.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad18 = (GradientDrawable) binding.eighteen.frame18.getBackground();
                            myGrad18.setColor(image_background_color);

                            binding.nineteen.frame19.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad19 = (GradientDrawable) binding.nineteen.frame19.getBackground();
                            myGrad19.setColor(image_background_color);

                            binding.twenty.frame20.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad20 = (GradientDrawable) binding.twenty.frame20.getBackground();
                            myGrad20.setColor(image_background_color);

                            binding.twentyone.frame21.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad21 = (GradientDrawable) binding.twentyone.frame21.getBackground();
                            myGrad21.setColor(image_background_color);

                            binding.twentytwo.frame22.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad22 = (GradientDrawable) binding.twentytwo.frame22.getBackground();
                            myGrad22.setColor(image_background_color);

                            binding.twentythree.frame23.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad23 = (GradientDrawable) binding.twentythree.frame23.getBackground();
                            myGrad23.setColor(image_background_color);

                            binding.twentyfour.frame24.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad24 = (GradientDrawable) binding.twentyfour.frame24.getBackground();
                            myGrad24.setColor(image_background_color);

                            binding.twentyfive.frame25.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad25 = (GradientDrawable) binding.twentyfive.frame25.getBackground();
                            myGrad25.setColor(image_background_color);

                            binding.twentysix.frame26.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad26 = (GradientDrawable) binding.twentysix.frame26.getBackground();
                            myGrad26.setColor(image_background_color);

                            binding.twentyseven.frame27.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad27 = (GradientDrawable) binding.twentyseven.frame27.getBackground();
                            myGrad27.setColor(image_background_color);

                            binding.twentyeight.frame28.setBackground(getResources().getDrawable(R.drawable.border_frame));
                            GradientDrawable myGrad28 = (GradientDrawable) binding.twentyeight.frame28.getBackground();
                            myGrad28.setColor(image_background_color);


                        } else {

                            binding.first.frame1.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad = (GradientDrawable) binding.first.frame1.getBackground();
                            myGrad.setColor(image_background_color);

                            binding.second.frame2.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad2 = (GradientDrawable) binding.second.frame2.getBackground();
                            myGrad2.setColor(image_background_color);

                            binding.third.frame3.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad3 = (GradientDrawable) binding.third.frame3.getBackground();
                            myGrad3.setColor(image_background_color);

                            binding.forth.frame4.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad4 = (GradientDrawable) binding.forth.frame4.getBackground();
                            myGrad4.setColor(image_background_color);

                            binding.fifth.frame5.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad5 = (GradientDrawable) binding.fifth.frame5.getBackground();
                            myGrad5.setColor(image_background_color);

                            binding.sixth.frame6.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad6 = (GradientDrawable) binding.sixth.frame6.getBackground();
                            myGrad6.setColor(image_background_color);

                            binding.seventh.frame7.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad7 = (GradientDrawable) binding.seventh.frame7.getBackground();
                            myGrad7.setColor(image_background_color);

                            binding.eight.frame8.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad8 = (GradientDrawable) binding.eight.frame8.getBackground();
                            myGrad8.setColor(image_background_color);

                            binding.nineth.frame9.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad9 = (GradientDrawable) binding.nineth.frame9.getBackground();
                            myGrad9.setColor(image_background_color);

                            binding.tenth.frame10.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad10 = (GradientDrawable) binding.tenth.frame10.getBackground();
                            myGrad10.setColor(image_background_color);

                            binding.eleven.frame11.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad11 = (GradientDrawable) binding.eleven.frame11.getBackground();
                            myGrad11.setColor(image_background_color);

                            binding.twelve.frame12.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad12 = (GradientDrawable) binding.twelve.frame12.getBackground();
                            myGrad12.setColor(image_background_color);

                            binding.thirteen.frame13.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad13 = (GradientDrawable) binding.thirteen.frame13.getBackground();
                            myGrad13.setColor(image_background_color);

                            binding.fourteen.frame14.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad14 = (GradientDrawable) binding.fourteen.frame14.getBackground();
                            myGrad14.setColor(image_background_color);

                            binding.fifteen.frame15.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad15 = (GradientDrawable) binding.fifteen.frame15.getBackground();
                            myGrad15.setColor(image_background_color);

                            binding.sixteenth.frame16.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad16 = (GradientDrawable) binding.sixteenth.frame16.getBackground();
                            myGrad16.setColor(image_background_color);

                            binding.seventeenth.frame17.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad17 = (GradientDrawable) binding.seventeenth.frame17.getBackground();
                            myGrad17.setColor(image_background_color);

                            binding.eighteen.frame18.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad18 = (GradientDrawable) binding.eighteen.frame18.getBackground();
                            myGrad18.setColor(image_background_color);

                            binding.nineteen.frame19.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad19 = (GradientDrawable) binding.nineteen.frame19.getBackground();
                            myGrad19.setColor(image_background_color);

                            binding.twenty.frame20.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad20 = (GradientDrawable) binding.twenty.frame20.getBackground();
                            myGrad20.setColor(image_background_color);

                            binding.twentyone.frame21.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad21 = (GradientDrawable) binding.twentyone.frame21.getBackground();
                            myGrad21.setColor(image_background_color);

                            binding.twentytwo.frame22.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad22 = (GradientDrawable) binding.twentytwo.frame22.getBackground();
                            myGrad22.setColor(image_background_color);

                            binding.twentythree.frame23.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad23 = (GradientDrawable) binding.twentythree.frame23.getBackground();
                            myGrad23.setColor(image_background_color);

                            binding.twentyfour.frame24.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad24 = (GradientDrawable) binding.twentyfour.frame24.getBackground();
                            myGrad24.setColor(image_background_color);

                            binding.twentyfive.frame25.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad25 = (GradientDrawable) binding.twentyfive.frame25.getBackground();
                            myGrad25.setColor(image_background_color);

                            binding.twentysix.frame26.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad26 = (GradientDrawable) binding.twentysix.frame26.getBackground();
                            myGrad26.setColor(image_background_color);

                            binding.twentyseven.frame27.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad27 = (GradientDrawable) binding.twentyseven.frame27.getBackground();
                            myGrad27.setColor(image_background_color);

                            binding.twentyeight.frame28.setBackground(getResources().getDrawable(R.drawable.frame_rounded_border));
                            GradientDrawable myGrad28 = (GradientDrawable) binding.twentyeight.frame28.getBackground();
                            myGrad28.setColor(image_background_color);

                        }


                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        cameraUtils.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 23) {

                Uri imageUri = data.getData();

                if (video_flag) {
                    Log.e("CATEGORY_NAME", " second if ");
                    Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath());
                    //  mPhotoEditor.addImage(bitmap);
                    rotateZoomImageView_video.setVisibility(View.VISIBLE);
                    image_loaded_flag = true;
                    rotateZoomImageView_video.setImageURI(imageUri);
                    if (image_loaded_flag) {


                        rotateZoomImageView_video.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return rotateZoomImageView_video.onTouch(v, event);
                            }
                        });
                        rotateZoomImageView_video.setOnTouchListener(new OnDragTouchListener(rotateZoomImageView_video) {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return rotateZoomImageView_video.onTouch(v, event);
                            }
                        });
                    }
                } else if (getIntent().hasExtra(Consts.POSITION)) {
                    Log.e("CATEGORY_NAME", " first if ");

                    ImageCanvasAdapter adapter1 = new ImageCanvasAdapter(ImageCanvasActivity.this, stringImageList, true, String.valueOf(imageUri), cat_tracker, category_name);

                    Log.e("CATEGORY_NAME", " 2 " + category_name);
                    //  if (category_name.equalsIgnoreCase("Greetings")) {

                    CustomGridLayoutManager customGridLayoutManager = new CustomGridLayoutManager(this);
                    customGridLayoutManager.setScrollEnabled(false);
                    binding.imageRecyclerview.setLayoutManager(customGridLayoutManager);
                   /* } else {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                        binding.imageRecyclerview.setLayoutManager(linearLayoutManager);
                        binding.imageRecyclerview.setNestedScrollingEnabled(false);
                    }*/
                    binding.imageRecyclerview.scrollToPosition(Integer.parseInt(position));

                    if (binding.imageRecyclerview.getOnFlingListener() == null) {

                        SnapHelper snapHelper = new LinearSnapHelper();
                        snapHelper.attachToRecyclerView(binding.imageRecyclerview);
                    }
                    binding.imageRecyclerview.setAdapter(adapter1);
                } else if (image_flag) {
                    Log.e("CATEGORY_NAME", " third if ");
                    rotateZoomImageView.setVisibility(View.VISIBLE);
                    image_loaded_flag = true;
                    rotateZoomImageView.setImageURI(imageUri);
                    if (image_loaded_flag) {


                        rotateZoomImageView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return rotateZoomImageView.onTouch(v, event);
                            }
                        });
                        rotateZoomImageView.setOnTouchListener(new OnDragTouchListener(rotateZoomImageView) {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return rotateZoomImageView.onTouch(v, event);
                            }
                        });
                    }
                }
                //img_path = imageUri.getPath();
            }
        }
    }

    private boolean shareImage(String image_flag) {

        params.put(Consts.USER_ID, userDTO.getUser_id());

        new HttpsRequest(Consts.CHECK_APPROVE, params, ImageCanvasActivity.this).stringPost("TAG", Consts.USER_CONTROLLER, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                try {
                    String status = response.getString("status");
                    Log.e("responsestr", "" + status);

                    if (status.equalsIgnoreCase("4")) {
                        startActivity(new Intent(ImageCanvasActivity.this, SubscriptionActivity.class).putExtra("from_image", true));
                    } else {

                        if (image_flag.equalsIgnoreCase("1")) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1011);
                                } else {

                                    File file = saveBitMap(ImageCanvasActivity.this, binding.frameRelative);    //which view you want to pass that view as parameter
                                    if (file != null) {
                                        Log.i("SHIVAKASHI", "Drawing saved to the gallery!");
                                    } else {
                                        Log.i("SHIVAKASHI", "Oops! Image could not be saved.");
                                    }
                                }
                            }
                        } else if (image_flag.equalsIgnoreCase("2")) {
                            saveImage();
                        } else if (image_flag.equalsIgnoreCase("3")) {
                            saveimgFromBitmap();

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return share_flag;

    }

    private void saveBitMapForVideo(Context context, View drawView){
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Handcare");
        if (!pictureFileDir.exists()) {
            Log.e("TRACKER12345", "1");
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.e("TRACKER12345", "2");
            Log.i("Tag", "Can't create directory to save the image");
            // return null;
        }
        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);

        if (video_flag) {
            Log.e("video_img_path", " base64 ");
            //  bitmap.setPixel(1080,1080,0);
            //  Bitmap bm = BitmapFactory.decodeFile(pictureFile);
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bOut);
            String base64Image = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);
            Log.e("video_img_path", " base64 " + base64Image);
            Log.d("mytag", " base64 " + base64Image);
            downloadVideoparams.put("img_path", base64Image);
            //imageMap.put("img_path",file);
        }

        mPhotoEditor.addImage(bitmap);

        //return pictureFile;
    }//create bitmap from view and returns it

    private File saveBitMap(Context context, View drawView) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Brand Shaastra");
        if (!pictureFileDir.exists()) {
            Log.e("TRACKER12345", "1");
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.e("TRACKER12345", "2");
            Log.i("Tag", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery(context, pictureFile.getAbsolutePath());
        return pictureFile;
    }
    //create bitmap from view and returns it

    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.e("TRACKER12345", "5");
                    Log.e("TRACKER12345", "" + uri.toString());

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    sharingIntent.setType("image/jpeg");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Business-Card");
                    // sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Log.e("TRACKER12345", "3");

        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    private void getBottomDialog(String fontstyle_flag) {

        List<String> fontList = new ArrayList<>();
        fontList.add("0");
        fontList.add("1");
        fontList.add("2");
        fontList.add("3");
        fontList.add("4");
        fontList.add("5");
        fontList.add("6");
        fontList.add("7");
        fontList.add("8");
        fontList.add("9");
        fontList.add("10");
        fontList.add("11");
        fontList.add("12");
        fontList.add("13");
        fontList.add("14");
        fontList.add("15");
        fontStyleAdapter = new FontStyleAdapter(this, fontList, this);
        font_rv.setLayoutManager(new GridLayoutManager(this, 3));
        font_rv.setAdapter(fontStyleAdapter);


        if (fontstyle_flag.equalsIgnoreCase("1")) {

            bottomsheet_title.setText("Select font style");
            bottomsheet_relative.setVisibility(View.GONE);
            bottomsheet_relative3.setVisibility(View.GONE);
            bottomsheet_relative2.setVisibility(View.VISIBLE);
        } else if (fontstyle_flag.equalsIgnoreCase("0")) {
            bottomsheet_title.setText("Select text size");
            bottomsheet_relative.setVisibility(View.VISIBLE);
            bottomsheet_relative2.setVisibility(View.GONE);
            bottomsheet_relative3.setVisibility(View.GONE);
        } else if (fontstyle_flag.equalsIgnoreCase("2")) {
            bottomsheet_title.setText("Change Theme");
            bottomsheet_relative.setVisibility(View.GONE);
            bottomsheet_relative2.setVisibility(View.GONE);
            bottomsheet_relative3.setVisibility(View.VISIBLE);
        }

        bottomsheet_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        bottom_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bottomsheet_text_size.setText(String.valueOf(progress));
                binding.movableEditText.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        setting_theme_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(ImageCanvasActivity.this)
                        .setTitle("Choose color")
                        .initialColor(getResources().getColor(R.color.blue_color))
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {

                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                //binding.rel.setBackgroundColor(selectedColor);

                                setting_theme_color.setBackgroundColor(selectedColor);
                                frame_border_color = selectedColor;

                                //new frame code
                                //theme29
                                binding.twentynineNew.whatsappMainRelative.setBackgroundColor(selectedColor);
                                binding.twentynineNew.callMainRelative.setBackgroundColor(selectedColor);
                                binding.twentynineNew.mailMainRelative.setBackgroundColor(selectedColor);
                                binding.twentynineNew.websiteMainRelative.setBackgroundColor(selectedColor);
                                binding.twentynineNew.headerBg.setBackgroundColor(selectedColor);
                                //theme30
                                binding.thirtyNew.bottomLayout.setBackgroundColor(selectedColor);
                                //theme31
//                                binding.thirtyoneNew.
                                //theme32
                                binding.thirtytwoNew.locationMainRelative.setBackgroundColor(selectedColor);
                                binding.thirtytwoNew.footerStik1.setColorFilter(selectedColor);
                                binding.thirtytwoNew.footerStik2.setColorFilter(selectedColor);
                                binding.thirtytwoNew.footerStik3.setColorFilter(selectedColor);
                                binding.thirtytwoNew.footerStik4.setColorFilter(selectedColor);

                                //theme33
                                binding.thirtythreeNew.footerBg.setBackgroundColor(selectedColor);

                                //theme34
                                binding.thirtyfourNew.footerBg.setBackgroundColor(selectedColor);

                                //theme35
                                binding.thirtyfiveNew.locationMainRelative.setBackgroundColor(selectedColor);


                                //theme36
                                binding.thirtysixthNew.headerBg.setBackgroundColor(selectedColor);

                                //theme37
                                changeDrawablecolor(binding.thirtysevenNew.businessLogoBg.getBackground(), selectedColor);
                                changeDrawablecolor(binding.thirtysevenNew.businessServiceDetails.getBackground(),selectedColor);
                                binding.thirtysevenNew.footerMailImage12.setColorFilter(selectedColor);

                                //theme38
                                binding.thirtyeightNew.businessLogoRelative1.setBackgroundColor(selectedColor);


                                //theme39
//                                binding.thirtynineNew.footerBg.setBackgroundColor(selectedColor);
                                binding.thirtynineNew.businessLogoRelative1.setBackgroundColor(selectedColor);



                                //theme40
                                binding.fortyNew.footerRelative.setBackgroundColor(selectedColor);

                                //theme41

                                //theme42
                              /*  binding.fortytwoNew.callMainRelative.setBackgroundColor(selectedColor);
                                binding.fortytwoNew.mailMainRelative12.setBackgroundColor(selectedColor);
                                binding.fortytwoNew.mailMainRelative112.setBackgroundColor(selectedColor);
                                binding.fortytwoNew.mailMainRelative1234.setBackgroundColor(selectedColor);
*/
                                //theme43
//                                binding.fortythreeNew.footerRelative.setBackgroundColor(selectedColor);

                                //theme44

                                //theme45
                           /*     binding.fortyfiveNew.footerBg.setBackgrondColor(selectedColor);
*/

                                //theme46
                                binding.fortysixNew.footerBg.setBackgroundColor(selectedColor);
                                binding.fortysixNew.businessLogoRelative1.setBackgroundColor(selectedColor);

                                //theme47
                                binding.fortysevenNew.businessLogoRelative1.setBackgroundColor(selectedColor);
                                binding.fortysevenNew.locationMainRelative.setBackgroundColor(selectedColor);
                              /*  binding.fortysevenNew.footerBg.getBackgroundColor(selectedColor);*/
                                //theme48


                                //theme49
                                binding.fortynineNew.bottomFirstFrameSecondLinear.setBackgroundColor(selectedColor);
                                binding.fortynineNew.callMainRelative.setBackgroundColor(selectedColor);
                                binding.fortynineNew.locationMainRelative.setBackgroundColor(selectedColor);
                                //theme50
                                binding.fiftyNew.bottomFirstFrameFirstLinear.setBackgroundColor(selectedColor);


                                //theme51
                                binding.fiftyoneNew.footerRelative.setBackgroundColor(selectedColor);

                                //theme52
                                binding.fiftytwoNew.footerRelative.setBackgroundColor(selectedColor);


                                //theme53
                                binding.fiftythreeNew.locationMainRelative.setBackgroundColor(selectedColor);

                                //theme39
                              /*  changeDrawablecolor(binding.thirtynineNew.businessLogoRelative1.getBackground(),selectedColor);
                                changeDrawablecolor(binding.thirtynineNew.footerBg.getBackground(),selectedColor);*/

                                //theme40
                              /*  changeDrawablecolor(binding.fortyNew.footerRelative.getBackground(),selectedColor);*/

                                //old frame code
                                binding.first.leftFirstFDot.setColorFilter(selectedColor);
                                binding.first.leftFirstFDotFooter.setColorFilter(selectedColor);
                                binding.first.headerFooterLine.setBackgroundColor(selectedColor);
                                binding.first.headerLine.setBackgroundColor(selectedColor);
                                binding.first.rightFirstFDot.setColorFilter(selectedColor);
                                binding.first.rightFirstFDotFooter.setColorFilter(selectedColor);

                              /*  binding.first.websiteMainRelative.setBackgroundColor(selectedColor);
                                binding.first.mailMainRelative.setBackgroundColor(selectedColor);
                                binding.first.whatsappBg.setBackgroundColor(selectedColor);
                                binding.first.callMainRelative.setBackgroundColor(selectedColor);
                                binding.first.headerBg.setBackgroundColor(selectedColor);*/

                                GradientDrawable myGrad = (GradientDrawable) binding.second.footerRelative.getBackground();
                                myGrad.setStroke(1, selectedColor);


                                Drawable unwrappedDrawable = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_third_frame_bg);
                                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                                DrawableCompat.setTint(wrappedDrawable, selectedColor);


                                /*VectorDrawable frame3_1 = (VectorDrawable) binding.third.footerCallImage.getBackground();
                                frame3_1.setTint(selectedColor);

                                VectorDrawable frame3_2 = (VectorDrawable) binding.third.footerWebsiteImage.getBackground();
                                frame3_2.setTint(selectedColor);

                                VectorDrawable frame3_3 = (VectorDrawable) binding.third.footerLocationImage.getBackground();
                                frame3_3.setTint(selectedColor);

                                VectorDrawable frame3_4 = (VectorDrawable) binding.third.footerMailImage.getBackground();
                                frame3_4.setTint(selectedColor);
*/


                                changeDrawablecolor(binding.third.footerCallImage.getBackground(), selectedColor);
                                changeDrawablecolor(binding.third.footerWebsiteImage.getBackground(), selectedColor);
                                changeDrawablecolor(binding.third.footerLocationImage.getBackground(), selectedColor);
                                changeDrawablecolor(binding.third.footerMailImage.getBackground(), selectedColor);

                                changeDrawablecolor(binding.forth.footerCallImage.getBackground(), selectedColor);
                                changeDrawablecolor(binding.forth.footerLocationImage.getBackground(), selectedColor);
                                changeDrawablecolor(binding.forth.footerMailImage.getBackground(), selectedColor);

                                /*VectorDrawable frame4 = (VectorDrawable) binding.forth.footerCallImage.getBackground();
                                frame4.setTint(selectedColor);
*/
                              /*  GradientDrawable myGrad123 = (GradientDrawable) binding.frameRelative.getBackground();
                                myGrad123.setColor(frame_border_color);
*/

                                // setDrawableBackground(binding.second.footerRelative.getBackground(),selectedColor);
                   /*             Drawable unwrappedDrawable2 = AppCompatResources.getDrawable(ImageCanvasActivity.this, R.drawable.border_frame);
                                Drawable wrappedDrawable2 = DrawableCompat.wrap(unwrappedDrawable2);
                                DrawableCompat.setTint(wrappedDrawable2, selectedColor);*/

                                binding.third.twitterIcon.setColorFilter(selectedColor);
                                binding.third.facebookIcon.setColorFilter(selectedColor);
                                binding.third.whatsappIcon.setColorFilter(selectedColor);
                                binding.third.instagramIcon.setColorFilter(selectedColor);

                                binding.fifth.twitterIcon.setColorFilter(selectedColor);
                                binding.fifth.facebookIcon.setColorFilter(selectedColor);
                                binding.fifth.whatsappIcon.setColorFilter(selectedColor);
                                binding.fifth.instagramIcon.setColorFilter(selectedColor);

                                binding.sixth.rightCornerBg.setColorFilter(selectedColor);
                                binding.sixth.leftCornerBg.setColorFilter(selectedColor);
                                binding.sixth.footerRelative.setBackgroundColor(selectedColor);

                                binding.seventh.twitterIcon.setColorFilter(selectedColor);
                                binding.seventh.facebookIcon.setColorFilter(selectedColor);
                                binding.seventh.whatsappIcon.setColorFilter(selectedColor);
                                binding.seventh.instagramIcon.setColorFilter(selectedColor);


/*
                                VectorDrawable frame8 = (VectorDrawable) binding.eight.footerCallImage.getBackground();
                                frame8.setTint(selectedColor);



                                binding.eight.footerCallImage.setColorFilter(selectedColor);
                                binding.eight.footerWebsiteImage.setColorFilter(selectedColor);
                                binding.eight.footerMailImage.setColorFilter(selectedColor);
                                binding.eight.footerLocationImage.setColorFilter(selectedColor);
*/
                                binding.nineth.ninenthBottomRelative.setBackgroundColor(selectedColor);

                                binding.tenth.twitterIcon.setColorFilter(selectedColor);
                                binding.tenth.facebookIcon.setColorFilter(selectedColor);
                                binding.tenth.whatsappIcon.setColorFilter(selectedColor);
                                binding.tenth.instagramIcon.setColorFilter(selectedColor);

                                changeDrawablecolor(binding.tenth.locationMainRelative.getBackground(), selectedColor);
                                changeDrawablecolor(binding.tenth.mailMainRelative.getBackground(), selectedColor);
                                changeDrawablecolor(binding.tenth.callMainRelative.getBackground(), selectedColor);
                                changeDrawablecolor(binding.tenth.websiteMainRelative.getBackground(), selectedColor);

                                binding.eleven.twitterIcon.setColorFilter(selectedColor);
                                binding.eleven.facebookIcon.setColorFilter(selectedColor);
                                binding.eleven.whatsappIcon.setColorFilter(selectedColor);
                                binding.eleven.instagramIcon.setColorFilter(selectedColor);

                                binding.twelve.twitterIcon.setColorFilter(selectedColor);
                                binding.twelve.facebookIcon.setColorFilter(selectedColor);
                                binding.twelve.whatsappIcon.setColorFilter(selectedColor);
                                binding.twelve.instagramIcon.setColorFilter(selectedColor);

                                binding.twelve.footerCallImage.setBackgroundColor(selectedColor);
                                binding.twelve.footerCallImage.setAlpha(0.9f);
                                binding.twelve.footerLocationImage.setAlpha(0.9f);
                                binding.twelve.footerWebsiteImage.setBackgroundColor(selectedColor);
                                binding.twelve.footerLocationImage.setBackgroundColor(selectedColor);

                                binding.thirteen.twitterIcon.setColorFilter(selectedColor);
                                binding.thirteen.facebookIcon.setColorFilter(selectedColor);
                                binding.thirteen.whatsappIcon.setColorFilter(selectedColor);
                                binding.thirteen.instagramIcon.setColorFilter(selectedColor);


                                binding.thirteen.footerCallImage.setBackgroundColor(selectedColor);
                                binding.thirteen.footerWebsiteImage.setBackgroundColor(selectedColor);
                                binding.thirteen.footerLocationImage.setBackgroundColor(selectedColor);
                                binding.thirteen.footerMailImage.setBackgroundColor(selectedColor);


                                changeSvgColor(binding.fourteen.callMainRelative, binding.fourteen.callMainRelative.getBackground(), selectedColor);
                                changeSvgColor(binding.fourteen.locationMainRelative, binding.fourteen.locationMainRelative.getBackground(), selectedColor);


                                binding.fifteen.view1.setBackgroundColor(selectedColor);
                                binding.fifteen.view2.setBackgroundColor(selectedColor);

                                Drawable buttonDrawable = binding.fourteen.mailMainRelative.getBackground();
                                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                                //the color is a direct color int and not a color resource
                                DrawableCompat.setTint(buttonDrawable, selectedColor);
                                binding.fourteen.mailMainRelative.setBackground(buttonDrawable);

                                Drawable buttonDrawable2 = binding.fourteen.websiteMainRelative.getBackground();
                                buttonDrawable2 = DrawableCompat.wrap(buttonDrawable2);
                                //the color is a direct color int and not a color resource
                                DrawableCompat.setTint(buttonDrawable2, selectedColor);
                                binding.fourteen.websiteMainRelative.setBackground(buttonDrawable2);


                              /*  changeSvgColor(binding.fourteen.mailMainRelative,binding.fourteen.mailMainRelative.getBackground(),selectedColor);
                                changeSvgColor(binding.fourteen.websiteMainRelative,binding.fourteen.websiteMainRelative.getBackground(),selectedColor);
*/
                                changeDrawablecolor(binding.fifteen.footerCallImage.getBackground(), selectedColor);
                                changeDrawablecolor(binding.fifteen.footerWebsiteImage.getBackground(), selectedColor);
                                changeDrawablecolor(binding.fifteen.footerMailImage.getBackground(), selectedColor);


                                changeDrawablecolor(binding.twentytwo.footerCallImage.getBackground(), selectedColor);
                                changeDrawablecolor(binding.twentytwo.footerLocationImage.getBackground(), selectedColor);
                                changeDrawablecolor(binding.twentytwo.footerMailImage.getBackground(), selectedColor);

                                binding.seventeenth.footerCallImage.setBackgroundColor(selectedColor);
                                binding.seventeenth.footerWebsiteImage.setBackgroundColor(selectedColor);
                                binding.seventeenth.businessWebsiteDetails.setBackgroundColor(selectedColor);
                                binding.seventeenth.businessCallDetails.setBackgroundColor(selectedColor);

                                binding.eighteen.footerCallImage.setColorFilter(selectedColor);
                                binding.eighteen.footerWebsiteImage.setColorFilter(selectedColor);
                                binding.eighteen.footerMailImage.setColorFilter(selectedColor);

                                binding.eighteen.rightCornerBg.setColorFilter(selectedColor);
                                binding.eighteen.leftCornerBg.setColorFilter(selectedColor);


                                binding.nineteen.footerRelative.setBackgroundColor(selectedColor);

                              /*  Drawable buttonDrawable = binding.twentyone.businessMailDetails.getBackground();
                                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                                //the color is a direct color int and not a color resource
                                DrawableCompat.setTint(buttonDrawable, Color.RED);
                                binding.twentyone.businessMailDetails.setBackground(buttonDrawable);
*/


                                changeSvgColor(binding.twenty.bottomTwentyFrameLinear, binding.twenty.bottomTwentyFrameLinear.getBackground(), selectedColor);
                                changeSvgColor(binding.twentyone.businessMailDetails, binding.twentyone.businessMailDetails.getBackground(), selectedColor);
                                changeSvgColor(binding.twentyone.businessCallDetails, binding.twentyone.businessCallDetails.getBackground(), selectedColor);

                                changeSvgColor(binding.twentythree.frame23TopLeftImg, binding.twentythree.frame23TopLeftImg.getBackground(), selectedColor);
                                changeSvgColor(binding.twentythree.callMainRelative, binding.twentythree.callMainRelative.getBackground(), selectedColor);
                                changeSvgColor(binding.twentyfour.frame24TopBg, binding.twentyfour.frame24TopBg.getBackground(), selectedColor);
                                changeSvgColor(binding.twentyfour.frame24BottomBg, binding.twentyfour.frame24BottomBg.getBackground(), selectedColor);
                                changeSvgColor(binding.twentyfive.frame25TopBg, binding.twentyfive.frame25TopBg.getBackground(), selectedColor);
                                changeSvgColor(binding.twentyfive.businessCallDetails, binding.twentyfive.businessCallDetails.getBackground(), selectedColor);
                                changeSvgColor(binding.twentyfive.businessMailDetails, binding.twentyfive.businessMailDetails.getBackground(), selectedColor);

                                changeSvgColor(binding.twentysix.bg, binding.twentysix.bg.getBackground(), selectedColor);
                                changeSvgColor(binding.twentysix.bottomTwentysixFrameLinear, binding.twentysix.bottomTwentysixFrameLinear.getBackground(), selectedColor);
                                changeSvgColor(binding.twentyseven.bottomTwentysevenFrameLinear, binding.twentyseven.bottomTwentysevenFrameLinear.getBackground(), selectedColor);
                                // changeSvgColor(binding.twentyseven.frame27TopRight, binding.twentyseven.frame27TopRight.getBackground(), selectedColor);
                                changeSvgColor(binding.twentyeight.frame28TopLeftBg, binding.twentyeight.frame28TopLeftBg.getBackground(), selectedColor);
                                changeSvgColor(binding.twentyeight.bottomTwentyeightFrameLinear, binding.twentyeight.bottomTwentyeightFrameLinear.getBackground(), selectedColor);
                                binding.twentyseven.frame27TopRight.setBackgroundColor(selectedColor);
                                /*   GradientDrawable frame10_1 = (GradientDrawable) binding.tenth.locationMainRelative.getBackground();
                                frame10_1.setColor(selectedColor);

                                GradientDrawable frame10_2 = (GradientDrawable) binding.tenth.mailMainRelative.getBackground();
                                frame10_2.setColor(selectedColor);

                                GradientDrawable frame10_3 = (GradientDrawable) binding.tenth.callMainRelative.getBackground();
                                frame10_3.setColor(selectedColor);

                                GradientDrawable frame10_4 = (GradientDrawable) binding.tenth.websiteMainRelative.getBackground();
                                frame10_4.setColor(selectedColor);

*/
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });

        setting_font_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ColorPickerDialogBuilder
                        .with(ImageCanvasActivity.this)
                        .setTitle("Choose color")
                        .initialColor(getResources().getColor(R.color.blue_color))
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {

                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                //binding.rel.setBackgroundColor(selectedColor);
                                setting_font_color.setBackgroundColor(selectedColor);
                                // binding.movableEditText.setTextColor(selectedColor);
                                //new frame code
                                //frame29
                                binding.twentynineNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.twentynineNew.businessCallDetails.setTextColor(selectedColor);
                                binding.twentynineNew.businessWhatsappDetails.setTextColor(selectedColor);
                                binding.twentynineNew.businessMailDetails.setTextColor(selectedColor);
                                binding.twentynineNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.twentynineNew.whatsappIcon.setColorFilter(selectedColor);
                                binding.twentynineNew.instaIcon.setColorFilter(selectedColor);
                                binding.twentynineNew.facebookIcon.setColorFilter(selectedColor);
                                binding.twentynineNew.twitterIcon.setColorFilter(selectedColor);

                                //frame30
                                binding.thirtyNew.businessCallDetails.setTextColor(selectedColor);
                                binding.thirtyNew.businessCall1Details.setTextColor(selectedColor);
                                binding.thirtyNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.thirtyNew.businessMailDetails.setTextColor(selectedColor);
                                binding.thirtyNew.businessLocationDetails.setTextColor(selectedColor);
                                binding.thirtyNew.businessAddressDetails.setTextColor(selectedColor);
                                binding.thirtyNew.footerLocationImage.setColorFilter(selectedColor);
                                binding.thirtyNew.footerWebsiteIcon.setColorFilter(selectedColor);
                                binding.thirtyNew.footerLocationIcon.setColorFilter(selectedColor);
                                binding.thirtyNew.footerMailIcon.setColorFilter(selectedColor);
                                binding.thirtyNew.footerCallIcon.setColorFilter(selectedColor);
                                binding.thirtyNew.footerCallIcon1.setColorFilter(selectedColor);


                                //frame31
                                binding.thirtyoneNew.twitterIcon.setColorFilter(selectedColor);
                                binding.thirtyoneNew.instagramIcon.setColorFilter(selectedColor);
                                binding.thirtyoneNew.facebookIcon.setColorFilter(selectedColor);
                                binding.thirtyoneNew.whatsappIcon.setColorFilter(selectedColor);
                                binding.thirtyoneNew.headerSlashIcon.setColorFilter(selectedColor);
                                binding.thirtyoneNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.thirtyoneNew.footerMailImage.setColorFilter(selectedColor);
                                binding.thirtyoneNew.footerWebsiteIcon.setColorFilter(selectedColor);
                                binding.thirtyoneNew.footerCallIcon.setColorFilter(selectedColor);
                                binding.thirtyoneNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.thirtyoneNew.businessMailDetails.setTextColor(selectedColor);
                                binding.thirtyoneNew.businessLocationDetails.setTextColor(selectedColor);
                                binding.thirtyoneNew.businessCallDetails.setTextColor(selectedColor);
                                binding.thirtyoneNew.businessCompanyDetails.setTextColor(selectedColor);


                                //frame32
                                binding.thirtytwoNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.thirtytwoNew.businessLocationDetails.setTextColor(selectedColor);
                                binding.thirtytwoNew.businessMailDetails.setTextColor(selectedColor);
                                binding.thirtytwoNew.businessWhatsappDetails.setTextColor(selectedColor);
                                binding.thirtytwoNew.instagramIcon.setColorFilter(selectedColor);
                                binding.thirtytwoNew.facebookIcon.setColorFilter(selectedColor);
                                binding.thirtytwoNew.twitterIcon.setColorFilter(selectedColor);
                              /*  binding.thirtytwoNew.headerSlashIcon.setColorFilter(selectedColor);*/
                                binding.thirtytwoNew.whatsappIcon.setColorFilter(selectedColor);
                                binding.thirtytwoNew.businessCompanyDetails.setTextColor(selectedColor);


                                //frame33
                                binding.thirtythreeNew.businessCallDetails.setTextColor(selectedColor);
                                binding.thirtythreeNew.businessWhatsappDetails.setTextColor(selectedColor);
                                binding.thirtythreeNew.businessLocationDetails.setTextColor(selectedColor);
                                binding.thirtythreeNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.thirtythreeNew.businessMailDetails.setTextColor(selectedColor);
                                binding.thirtythreeNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.thirtythreeNew.businessMailDetails.setTextColor(selectedColor);
                                binding.thirtythreeNew.headerSlashIcon.setColorFilter(selectedColor);
                                binding.thirtythreeNew.footerWhatsappIcon.setColorFilter(selectedColor);
                                binding.thirtythreeNew.footerCallIcon.setColorFilter(selectedColor);
                                binding.thirtythreeNew.footerMailImage.setColorFilter(selectedColor);
                                binding.thirtythreeNew.footerWebsiteIcon.setColorFilter(selectedColor);
                                binding.thirtythreeNew.footerLocationImage.setColorFilter(selectedColor);
                                binding.thirtythreeNew.twitterIcon.setColorFilter(selectedColor);
                                binding.thirtythreeNew.facebookIcon.setColorFilter(selectedColor);
                                binding.thirtythreeNew.instagramIcon.setColorFilter(selectedColor);
                                binding.thirtythreeNew.whatsappIcon.setColorFilter(selectedColor);


                                //frame34

                                binding.thirtyfourNew.businessLocationDetails.setTextColor(selectedColor);
                                binding.thirtyfourNew.businessMailDetails.setTextColor(selectedColor);
                                binding.thirtyfourNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.thirtyfourNew.businessWhatsappDetails.setTextColor(selectedColor);
                                binding.thirtyfourNew.businessCallDetails.setTextColor(selectedColor);
                                binding.thirtyfourNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.thirtyfourNew.twitterIcon.setColorFilter(selectedColor);
                                binding.thirtyfourNew.facebookIcon.setColorFilter(selectedColor);
                                binding.thirtyfourNew.whatsappIcon.setColorFilter(selectedColor);
                                binding.thirtyfourNew.footerCallIcon.setColorFilter(selectedColor);
                                binding.thirtyfourNew.instagramIcon.setColorFilter(selectedColor);
                                binding.thirtyfourNew.footerLocationIcon.setColorFilter(selectedColor);
                                binding.thirtyfourNew.footerEmailIcon.setColorFilter(selectedColor);
                                binding.thirtyfourNew.headerSlashIcon.setColorFilter(selectedColor);
                                //frame35
                                binding.thirtyfiveNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.thirtyfiveNew.businessMailDetails.setTextColor(selectedColor);
                                binding.thirtyfiveNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.thirtyfiveNew.businessCallDetails.setTextColor(selectedColor);
                                binding.thirtyfiveNew.headerSlashIcon.setColorFilter(selectedColor);
                                binding.thirtyfiveNew.businessMailDetailsText.setTextColor(selectedColor);
                                binding.thirtyfiveNew.businessCallDetailsText.setTextColor(selectedColor);
                                binding.thirtyfiveNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.thirtyfiveNew.footerMailIcon.setColorFilter(selectedColor);
                                binding.thirtyfiveNew.footerWebsiteIcon.setColorFilter(selectedColor);
                                binding.thirtyfiveNew.footerCallIcon.setColorFilter(selectedColor);
                                //frame36
                                binding.thirtysixthNew.businessMailDetails.setTextColor(selectedColor);
                                binding.thirtysixthNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.thirtysixthNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.thirtysixthNew.businessCallDetails.setTextColor(selectedColor);
                                binding.thirtysixthNew.businessCallDetailsSecond.setTextColor(selectedColor);
                                binding.thirtysixthNew.headerSlashIcon.setColorFilter(selectedColor);
                                binding.thirtysixthNew.twitterIcon.setColorFilter(selectedColor);
                                binding.thirtysixthNew.facebookIcon.setColorFilter(selectedColor);
                                binding.thirtysixthNew.whatsappIcon.setColorFilter(selectedColor);
                                binding.thirtysixthNew.instagramIcon.setColorFilter(selectedColor);

                                //frame37
                                binding.thirtysevenNew.businessMailDetails.setTextColor(selectedColor);
                                binding.thirtysevenNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.thirtysevenNew.businessLocationDetails.setTextColor(selectedColor);
                                binding.thirtysevenNew.businessCallDetails.setTextColor(selectedColor);
                                binding.thirtysevenNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.thirtysevenNew.twitterIcon.setColorFilter(selectedColor);
                                binding.thirtysevenNew.facebookIcon.setColorFilter(selectedColor);
                                binding.thirtysevenNew.whatsappIcon.setColorFilter(selectedColor);
                                binding.thirtysevenNew.instagramIcon.setColorFilter(selectedColor);

                                //frame38
                                binding.thirtyeightNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.thirtyeightNew.businessMailDetails.setTextColor(selectedColor);
                                binding.thirtyeightNew.businessCallDetails.setTextColor(selectedColor);
                                binding.thirtyeightNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.thirtyeightNew.footerCallIcon.setColorFilter(selectedColor);
                                binding.thirtyeightNew.footerMailIcon.setColorFilter(selectedColor);
                                binding.thirtyeightNew.footerWebsiteIcon.setColorFilter(selectedColor);
                                binding.thirtyeightNew.twitterIcon.setColorFilter(selectedColor);
                                binding.thirtyeightNew.instagramIcon.setColorFilter(selectedColor);
                                binding.thirtyeightNew.facebookIcon.setColorFilter(selectedColor);
                                binding.thirtyeightNew.whatsappIcon.setColorFilter(selectedColor);
                                //frame39
                                binding.thirtynineNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.thirtynineNew.businessMailDetails.setTextColor(selectedColor);
                                binding.thirtynineNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.thirtynineNew.businessLocationDetails.setTextColor(selectedColor);
                                binding.thirtynineNew.businessLocationDetails.setTextColor(selectedColor);
                                binding.thirtynineNew.businessWhatsappDetails.setTextColor(selectedColor);
                                binding.thirtynineNew.twitterIcon.setColorFilter(selectedColor);
                                binding.thirtynineNew.facebookIcon.setColorFilter(selectedColor);
                                binding.thirtynineNew.whatsappIcon.setColorFilter(selectedColor);
                                binding.thirtynineNew.footerCallIcon.setColorFilter(selectedColor);
                                binding.thirtynineNew.footerCallIcon1.setColorFilter(selectedColor);
                                binding.thirtynineNew.footerLocationIcon.setColorFilter(selectedColor);
                                binding.thirtynineNew.footerMailIcon.setColorFilter(selectedColor);
                                binding.thirtynineNew.instagramIcon.setColorFilter(selectedColor);

                                //frame40
                                binding.fortyNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.fortyNew.businessLocationDetails.setTextColor(selectedColor);
                                binding.fortyNew.businessMailDetails.setTextColor(selectedColor);
                                binding.fortyNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.fortyNew.businessCallDetails.setTextColor(selectedColor);
                                binding.fortyNew.headerSlashIcon.setColorFilter(selectedColor);
                                binding.fortyNew.instagramIcon.setColorFilter(selectedColor);
                                binding.fortyNew.facebookIcon.setColorFilter(selectedColor);
                                binding.fortyNew.whatsappIcon.setColorFilter(selectedColor);
                                binding.fortyNew.footerWebsiteIcon.setColorFilter(selectedColor);
                                binding.fortyNew.footerCallIcon.setColorFilter(selectedColor);
                                binding.fortyNew.EmailIcon.setTextColor(selectedColor);
                                binding.fortyNew.whatsappIcon.setColorFilter(selectedColor);

                                //frame41
                                binding.fortyoneNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.fortyoneNew.businessLocationDetails.setTextColor(selectedColor);
                                binding.fortyoneNew.businessMailDetails.setTextColor(selectedColor);
                                binding.fortyoneNew.businessCallDetails.setTextColor(selectedColor);
                                binding.fortyoneNew.headerSlashIcon.setColorFilter(selectedColor);
                                binding.fortyoneNew.instagramIcon.setColorFilter(selectedColor);
                                binding.fortyoneNew.facebookIcon.setColorFilter(selectedColor);
                                binding.fortyoneNew.whatsappIcon.setColorFilter(selectedColor);
                                binding.fortyoneNew.businessCallDetailsText.setTextColor(selectedColor);
                                binding.fortyoneNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.fortyoneNew.twitterIcon.setColorFilter(selectedColor);
                                
                                //frame42
                                binding.fortytwoNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.fortytwoNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.fortytwoNew.businessMailDetails.setTextColor(selectedColor);
                                binding.fortytwoNew.businessLocationDetails.setTextColor(selectedColor);
                                binding.fortytwoNew.headerSlashIcon.setColorFilter(selectedColor);
                                binding.fortytwoNew.twitterIcon.setColorFilter(selectedColor);
                                binding.fortytwoNew.instagramIcon.setColorFilter(selectedColor);
                                binding.fortytwoNew.whatsappIcon.setColorFilter(selectedColor);
                                binding.fortytwoNew.facebookIcon.setColorFilter(selectedColor);
                                binding.fortytwoNew.footerCallIcon.setColorFilter(selectedColor);
                                binding.fortytwoNew.footerLocationIcon.setColorFilter(selectedColor);
                                binding.fortytwoNew.footerMailIcon.setColorFilter(selectedColor);
                                binding.fortytwoNew.footerWebsiteIcon.setColorFilter(selectedColor);
                                binding.fortyoneNew.businessCallDetails.setTextColor(selectedColor);

                                //frame43
                                binding.fortythreeNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.fortythreeNew.businessMailDetails.setTextColor(selectedColor);
                                binding.fortythreeNew.businessCallDetails.setTextColor(selectedColor);
                                binding.fortythreeNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.fortythreeNew.headerSlashIcon.setColorFilter(selectedColor);
                                binding.fortythreeNew.twitterIcon.setColorFilter(selectedColor);
                                binding.fortythreeNew.facebookIcon.setColorFilter(selectedColor);

                                binding.fortythreeNew.instagramIcon.setColorFilter(selectedColor);
                                binding.fortythreeNew.whatsappIcon.setColorFilter(selectedColor);
                                binding.fortythreeNew.footerCallIcon.setColorFilter(selectedColor);
                                binding.fortythreeNew.footerMailIcon.setColorFilter(selectedColor);
                                binding.fortythreeNew.footerWebsiteIcon.setColorFilter(selectedColor);

                                //frame44
                                binding.fortyfourNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.fortyfourNew.businessCallDetails.setTextColor(selectedColor);
                                binding.fortyfourNew.businessLocationDetails.setTextColor(selectedColor);
                                binding.fortyfourNew.headerSlashIcon.setColorFilter(selectedColor);
                                binding.fortyfourNew.twitterIcon.setColorFilter(selectedColor);
                                binding.fortyfourNew.facebookIcon.setColorFilter(selectedColor);
                                binding.fortyfourNew.instagramIcon.setColorFilter(selectedColor);
                                binding.fortyfourNew.whatsappIcon.setColorFilter(selectedColor);
                                binding.fortyfourNew.footerCallIcon.setColorFilter(selectedColor);
                                binding.fortyfourNew.businessCallDetailsSecond.setTextColor(selectedColor);

                                //frame45
                                binding.fortyfiveNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.fortyfiveNew.businessMailDetails.setTextColor(selectedColor);
                                binding.fortyfiveNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.fortyfiveNew.headerSlashIcon.setColorFilter(selectedColor);
                                binding.fortyfiveNew.twitterIcon.setColorFilter(selectedColor);
                                binding.fortyfiveNew.facebookIcon.setColorFilter(selectedColor);
                                binding.fortyfiveNew.instagramIcon.setColorFilter(selectedColor);
                                binding.fortyfiveNew.whatsappIcon.setColorFilter(selectedColor);
                                binding.fortyfiveNew.businessCallDetails.setTextColor(selectedColor);
                                binding.fortyfiveNew.businessCallDetailsSecond.setTextColor(selectedColor);


                                //frame46
                                binding.fortysixNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.fortysixNew.businessMailDetails.setTextColor(selectedColor);
                                binding.fortysixNew.businessCallDetails.setTextColor(selectedColor);
                                binding.fortysixNew.businessLocationDetails.setTextColor(selectedColor);
                                binding.fortysixNew.businessWhatsappDetails.setTextColor(selectedColor);
                                binding.fortysixNew.twitterIcon.setColorFilter(selectedColor);
                                binding.fortysixNew.facebookIcon.setColorFilter(selectedColor);
                                binding.fortysixNew.instagramIcon.setColorFilter(selectedColor);
                                binding.fortysixNew.whatsappIcon.setColorFilter(selectedColor);
                                binding.fortysixNew.footerLocationIcon.setColorFilter(selectedColor);
                                binding.fortysixNew.footerMailIcon.setColorFilter(selectedColor);
                                binding.fortysixNew.footerWhatsappIcon.setColorFilter(selectedColor);

                                //frame47
                                binding.fortysevenNew.businessCallDetails.setTextColor(selectedColor);
                                binding.fortysevenNew.businessMailDetails.setTextColor(selectedColor);
                                binding.fortysevenNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.fortysevenNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.fortysevenNew.footerLocationIcon.setColorFilter(selectedColor);
                                binding.fortysevenNew.footerMailIcon.setColorFilter(selectedColor);
                                binding.fortysevenNew.footerWebsiteIcon.setColorFilter(selectedColor);
                                binding.fortysevenNew.headerCallIcon.setColorFilter(selectedColor);
                                binding.fortysevenNew.headerCallIcon1.setColorFilter(selectedColor);
                                binding.fortysevenNew.businessCallDetailsSecond.setTextColor(selectedColor);
                                binding.fortysevenNew.businessLocationDetails.setTextColor(selectedColor);
                                binding.fortysevenNew.twitterIcon.setColorFilter(selectedColor);
                                binding.fortysevenNew.facebookIcon.setColorFilter(selectedColor);
                                binding.fortysevenNew.instagramIcon.setColorFilter(selectedColor);
                                binding.fortysevenNew.whatsappIcon.setColorFilter(selectedColor);
                                //frame48


                                //frame49
                                binding.fortynineNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.fortynineNew.businessMailDetails.setTextColor(selectedColor);
                                binding.fortynineNew.businessCallDetails.setTextColor(selectedColor);
                                binding.fortynineNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.fortynineNew.businessLocationDetails.setTextColor(selectedColor);

                                binding.fortynineNew.businessCallDetailsSecond.setTextColor(selectedColor);
                                binding.fortynineNew.footerLocationIcon.setColorFilter(selectedColor);
                                binding.fortynineNew.footerMailIcon.setColorFilter(selectedColor);
                                binding.fortynineNew.footerWebsiteIcon.setColorFilter(selectedColor);


                                //frame50
                                binding.fiftyNew.businessLocationDetails.setTextColor(selectedColor);
                                binding.fiftyNew.businessMailDetails.setTextColor(selectedColor);
                                binding.fiftyNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.fiftyNew.businessCallDetails.setTextColor(selectedColor);
                                binding.fiftyNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.fiftyNew.headerSlashIcon.setColorFilter(selectedColor);
                                binding.fiftyNew.footerCallIcon.setColorFilter(selectedColor);
                                binding.fiftyNew.footerWebsiteIcon.setColorFilter(selectedColor);
                                binding.fiftyNew.EmailIcon.setTextColor(selectedColor);

                                //frame51
                                binding.fiftyoneNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.fiftyoneNew.businessCallDetails.setTextColor(selectedColor);
                                binding.fiftyoneNew.businessLocationDetails.setTextColor(selectedColor);
                                binding.fiftyoneNew.footerLocationImage.setColorFilter(selectedColor);
                                binding.fiftyoneNew.footerMailIcon.setColorFilter(selectedColor);
                                binding.fiftyoneNew.footerWebsiteIcon.setColorFilter(selectedColor);
                                binding.fiftyoneNew.businessCallDetails.setTextColor(selectedColor);
                                binding.fiftyoneNew.businessCallDetailsSecond.setTextColor(selectedColor);
                                binding.fiftyoneNew.headerCallIcon.setColorFilter(selectedColor);
                                binding.fiftyoneNew.headerCallIcon1.setColorFilter(selectedColor);
                                binding.fiftyoneNew.footerLocationIcon.setColorFilter(selectedColor);
                                binding.fiftyoneNew.businessMailDetails.setTextColor(selectedColor);

                                //frame52
                                binding.fiftytwoNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.fiftytwoNew.businessLocationDetails.setTextColor(selectedColor);
                                binding.fiftytwoNew.businessMailDetails.setTextColor(selectedColor);
                                binding.fiftytwoNew.businessLocationDetails.setTextColor(selectedColor);
                                binding.fiftytwoNew.footerLocationIcon.setColorFilter(selectedColor);
                                binding.fiftytwoNew.footerLocationImage.setColorFilter(selectedColor);
                                binding.fiftytwoNew.footerWebsiteIcon.setColorFilter(selectedColor);
                                binding.fiftytwoNew.footerMailIcon.setColorFilter(selectedColor);
                                binding.fiftytwoNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.fiftytwoNew.headerSlashIcon.setColorFilter(selectedColor);
                                binding.fiftytwoNew.twitterIcon.setColorFilter(selectedColor);
                                binding.fiftytwoNew.facebookIcon.setColorFilter(selectedColor);
                                binding.fiftytwoNew.whatsappIcon.setColorFilter(selectedColor);
                                binding.fiftytwoNew.instagramIcon.setColorFilter(selectedColor);
                                binding.fiftytwoNew.businessLocationDetails.setTextColor(selectedColor);

                                //frame53
                                binding.fiftythreeNew.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.fiftythreeNew.businessLocationDetails.setTextColor(selectedColor);
                                binding.fiftythreeNew.businessCompanyDetails.setTextColor(selectedColor);
                                binding.fiftythreeNew.businessMailDetails.setTextColor(selectedColor);
                                binding.fiftythreeNew.footerCallIcon.setColorFilter(selectedColor);
                                binding.fiftythreeNew.footerLocationImage.setColorFilter(selectedColor);
                                binding.fiftythreeNew.footerWebsiteIcon.setColorFilter(selectedColor);
                                binding.fiftythreeNew.footerMailIcon.setColorFilter(selectedColor);
                                binding.fiftythreeNew.headerSlashIcon.setColorFilter(selectedColor);
                                binding.fiftythreeNew.twitterIcon.setColorFilter(selectedColor);
                                binding.fiftythreeNew.facebookIcon.setColorFilter(selectedColor);
                                binding.fiftythreeNew.instagramIcon.setColorFilter(selectedColor);
                                binding.fiftythreeNew.whatsappIcon.setColorFilter(selectedColor);




                                
                                   //old frame code
                                binding.first.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.second.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.third.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.sixth.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.nineth.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.tenth.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.eleven.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.twelve.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.thirteen.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.fourteen.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.fifteen.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.sixteenth.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.seventeenth.businessWebsiteDetails.setTextColor(selectedColor);
                                binding.eighteen.businessWebsiteDetails.setTextColor(selectedColor);

                                //new frame code
                                binding.twentynineNew.businessMailDetails.setTextColor(selectedColor);


                                //old frame code
                                binding.first.businessMailDetails.setTextColor(selectedColor);
                                binding.second.businessMailDetails.setTextColor(selectedColor);
                                binding.third.businessMailDetails.setTextColor(selectedColor);
                                binding.forth.businessMailDetails.setTextColor(selectedColor);
                                binding.fifth.businessMailDetails.setTextColor(selectedColor);
                                binding.sixth.businessMailDetails.setTextColor(selectedColor);
                                binding.seventh.businessMailDetails.setTextColor(selectedColor);
                                binding.eight.businessMailDetails.setTextColor(selectedColor);
                                binding.tenth.businessMailDetails.setTextColor(selectedColor);
                                binding.eleven.businessMailDetails.setTextColor(selectedColor);
                                binding.twelve.businessMailDetails.setTextColor(selectedColor);
                                binding.thirteen.businessMailDetails.setTextColor(selectedColor);
                                binding.fourteen.businessMailDetails.setTextColor(selectedColor);
                                binding.fifteen.businessMailDetails.setTextColor(selectedColor);
                                binding.eighteen.businessMailDetails.setTextColor(selectedColor);
                                binding.nineteen.businessMailDetails.setTextColor(selectedColor);
                                binding.twenty.businessMailDetails.setTextColor(selectedColor);
                                binding.twentyone.businessMailDetails.setTextColor(selectedColor);


//                                binding.twentyone.businessMailDetails.setBackgroundTintList(getResources().getColorStateList(selectedColor));


                                binding.twentytwo.businessMailDetails.setTextColor(selectedColor);
                                binding.twentythree.businessMailDetails.setTextColor(selectedColor);
                                binding.twentyfour.businessMailDetails.setTextColor(selectedColor);
                                binding.twentyfive.businessMailDetails.setTextColor(selectedColor);
                                binding.twentysix.businessMailDetails.setTextColor(selectedColor);
                                binding.twentyseven.businessMailDetails.setTextColor(selectedColor);
                                binding.twentyeight.businessMailDetails.setTextColor(selectedColor);

                                //new frame code
                                binding.twentynineNew.businessCallDetails.setTextColor(selectedColor);
                                //old frame code
                                binding.first.businessCallDetails.setTextColor(selectedColor);
                                binding.second.businessCallDetails.setTextColor(selectedColor);
                                binding.third.businessCallDetails.setTextColor(selectedColor);
                                binding.forth.businessCallDetails.setTextColor(selectedColor);
                                binding.fifth.businessCallDetails.setTextColor(selectedColor);
                                binding.sixth.businessCallDetails.setTextColor(selectedColor);
                                binding.seventh.businessCallDetails.setTextColor(selectedColor);
                                binding.eight.businessCallDetails.setTextColor(selectedColor);
                                binding.nineth.businessCallDetails.setTextColor(selectedColor);
                                binding.tenth.businessCallDetails.setTextColor(selectedColor);
                                binding.eleven.businessCallDetails.setTextColor(selectedColor);
                                binding.twelve.businessCallDetails.setTextColor(selectedColor);
                                binding.thirteen.businessCallDetails.setTextColor(selectedColor);
                                binding.fourteen.businessCallDetails.setTextColor(selectedColor);
                                binding.fifteen.businessCallDetails.setTextColor(selectedColor);
                                binding.sixteenth.businessCallDetails.setTextColor(selectedColor);
                                binding.seventeenth.businessCallDetails.setTextColor(selectedColor);
                                binding.eighteen.businessCallDetails.setTextColor(selectedColor);
                                binding.nineteen.businessCallDetails.setTextColor(selectedColor);
                                binding.twenty.businessCallDetails.setTextColor(selectedColor);
                                binding.twentyone.businessCallDetails.setTextColor(selectedColor);
                                binding.twentytwo.businessCallDetails.setTextColor(selectedColor);
                                binding.twentythree.businessCallDetails.setTextColor(selectedColor);
                                binding.twentyfour.businessCallDetails.setTextColor(selectedColor);
                                binding.twentyfive.businessCallDetails.setTextColor(selectedColor);
                                binding.twentysix.businessCallDetails.setTextColor(selectedColor);
                                binding.twentyseven.businessCallDetails.setTextColor(selectedColor);
                                binding.twentyeight.businessCallDetails.setTextColor(selectedColor);

/*
                                binding.first.businessLocationDetails.setTextColor(selectedColor);
*/
                                binding.second.businessLocationDetails.setTextColor(selectedColor);
                                binding.third.businessLocationDetails.setTextColor(selectedColor);
                                binding.forth.businessLocationDetails.setTextColor(selectedColor);
                                binding.fifth.businessLocationDetails.setTextColor(selectedColor);
                                binding.sixth.businessLocationDetails.setTextColor(selectedColor);
                                binding.seventh.businessLocationDetails.setTextColor(selectedColor);
                                binding.eight.businessLocationDetails.setTextColor(selectedColor);
                                binding.tenth.businessLocationDetails.setTextColor(selectedColor);
                                binding.twelve.businessLocationDetails.setTextColor(selectedColor);
                                binding.thirteen.businessLocationDetails.setTextColor(selectedColor);
                                binding.fourteen.businessLocationDetails.setTextColor(selectedColor);
                                binding.nineteen.businessLocationDetails.setTextColor(selectedColor);
                                binding.twenty.businessLocationDetails.setTextColor(selectedColor);
                                binding.twentytwo.businessLocationDetails.setTextColor(selectedColor);

                                binding.twentythree.businessName.setTextColor(selectedColor);
                                binding.twentyfour.businessName.setTextColor(selectedColor);
                                binding.twentyfive.businessName.setTextColor(selectedColor);
                                binding.twentysix.businessName.setTextColor(selectedColor);
                                binding.twentyseven.businessName.setTextColor(selectedColor);
                                binding.twentyeight.businessName.setTextColor(selectedColor);

                                //new frame code
                                binding.twentynineNew.footerCallImage.setColorFilter(selectedColor);
                                //old frame code
                                binding.first.footerCallImage.setColorFilter(selectedColor);
                                binding.second.footerCallImage.setColorFilter(selectedColor);
                                binding.third.footerCallImage.setColorFilter(selectedColor);
                                binding.forth.footerCallImage.setColorFilter(selectedColor);
                                binding.fifth.footerCallImage.setColorFilter(selectedColor);
                                binding.sixth.footerCallImage.setColorFilter(selectedColor);
                                binding.seventh.footerCallImage.setColorFilter(selectedColor);
                                binding.nineth.footerCallImage.setColorFilter(selectedColor);
                                binding.tenth.footerCallImage.setColorFilter(selectedColor);
                                binding.eleven.footerCallImage.setColorFilter(selectedColor);
                                binding.twelve.footerCallImage.setColorFilter(selectedColor);
                                binding.thirteen.footerCallImage.setColorFilter(selectedColor);
                                binding.fourteen.footerCallImage.setColorFilter(selectedColor);
                                binding.fifteen.footerCallImage.setColorFilter(selectedColor);
                                binding.sixteenth.footerCallImage.setColorFilter(selectedColor);
                                binding.seventeenth.footerCallImage.setColorFilter(selectedColor);
                                binding.eighteen.footerCallImage.setColorFilter(selectedColor);
                                binding.nineteen.footerCallImage.setColorFilter(selectedColor);
                                binding.twenty.footerCallImage.setColorFilter(selectedColor);
                                binding.twentytwo.footerCallImage.setColorFilter(selectedColor);
                                binding.twentythree.footerCallImage.setColorFilter(selectedColor);
                                binding.twentyfour.footerCallImage.setColorFilter(selectedColor);
                                binding.twentyfive.footerCallImage.setColorFilter(selectedColor);
                                binding.twentysix.footerCallImage.setColorFilter(selectedColor);
                                binding.twentyseven.footerCallImage.setColorFilter(selectedColor);
                                binding.twentyeight.footerCallImage.setColorFilter(selectedColor);

                                binding.first.footerLocationImage.setColorFilter(selectedColor);
                                binding.second.footerLocationImage.setColorFilter(selectedColor);
                                binding.third.footerLocationImage.setColorFilter(selectedColor);
                                binding.forth.footerLocationImage.setColorFilter(selectedColor);
                                binding.fifth.footerLocationImage.setColorFilter(selectedColor);
                                binding.sixth.footerLocationImage.setColorFilter(selectedColor);
                                binding.seventh.footerLocationImage.setColorFilter(selectedColor);
                                binding.tenth.footerLocationImage.setColorFilter(selectedColor);
                                binding.twelve.footerLocationImage.setColorFilter(selectedColor);
                                binding.thirteen.footerLocationImage.setColorFilter(selectedColor);
                                binding.fourteen.footerLocationImage.setColorFilter(selectedColor);
                                binding.nineteen.footerLocationImage.setColorFilter(selectedColor);
                                binding.twenty.footerLocationImage.setColorFilter(selectedColor);
                                binding.twentytwo.footerLocationImage.setColorFilter(selectedColor);




                                //new frame code
                                binding.twentynineNew.footerMailImage.setColorFilter(selectedColor);

                                //old frame code
                                binding.first.footerMailImage.setColorFilter(selectedColor);
                                binding.second.footerMailImage.setColorFilter(selectedColor);
                                binding.third.footerMailImage.setColorFilter(selectedColor);
                                binding.forth.footerMailImage.setColorFilter(selectedColor);
                                binding.fifth.footerMailImage.setColorFilter(selectedColor);
                                binding.sixth.footerMailImage.setColorFilter(selectedColor);
                                binding.seventh.footerMailImage.setColorFilter(selectedColor);
                                binding.tenth.footerMailImage.setColorFilter(selectedColor);
                                binding.eleven.footerMailImage.setColorFilter(selectedColor);
                                binding.thirteen.footerMailImage.setColorFilter(selectedColor);
                                binding.fourteen.footerMailImage.setColorFilter(selectedColor);
                                binding.fifteen.footerMailImage.setColorFilter(selectedColor);
                                binding.eighteen.footerMailImage.setColorFilter(selectedColor);
                                binding.nineteen.footerMailImage.setColorFilter(selectedColor);
                                binding.twenty.footerMailImage.setColorFilter(selectedColor);
                                binding.twentytwo.footerMailImage.setColorFilter(selectedColor);
                                binding.twentythree.footerMailImage.setColorFilter(selectedColor);
                                binding.twentyfour.footerMailImage.setColorFilter(selectedColor);
                                binding.twentyfive.footerMailImage.setColorFilter(selectedColor);
                                binding.twentysix.footerMailImage.setColorFilter(selectedColor);
                                binding.twentyseven.footerMailImage.setColorFilter(selectedColor);
                                binding.twentyeight.footerMailImage.setColorFilter(selectedColor);


                                //new frame code
                                binding.twentynineNew.footerWebsiteImage.setColorFilter(selectedColor);

                                //old frame code
                                binding.first.footerWebsiteImage.setColorFilter(selectedColor);
                                binding.second.footerWebsiteImage.setColorFilter(selectedColor);
                                binding.third.footerWebsiteImage.setColorFilter(selectedColor);
                                binding.sixth.footerWebsiteImage.setColorFilter(selectedColor);
                                binding.nineth.footerWebsiteImage.setColorFilter(selectedColor);
                                binding.tenth.footerWebsiteImage.setColorFilter(selectedColor);
                                binding.eleven.footerWebsiteImage.setColorFilter(selectedColor);
                                binding.twelve.footerWebsiteImage.setColorFilter(selectedColor);
                                binding.thirteen.footerWebsiteImage.setColorFilter(selectedColor);
                                binding.fourteen.footerWebsiteImage.setColorFilter(selectedColor);
                                binding.fifteen.footerWebsiteImage.setColorFilter(selectedColor);
                                binding.sixteenth.footerWebsiteImage.setColorFilter(selectedColor);
                                binding.seventeenth.footerWebsiteImage.setColorFilter(selectedColor);
                                binding.eighteen.footerWebsiteImage.setColorFilter(selectedColor);

                                //whatsapp color
                                //new frame code
                                //whatsapp frame icon
                                binding.twentynineNew.footerWhatsappImage.setColorFilter(selectedColor);


                                //whatsapp frame text
                                binding.twentynineNew.businessWhatsappDetails.setTextColor(selectedColor);


                                //company_name color
                                binding.twentynineNew.businessCompanyDetails.setTextColor(selectedColor);

                                //  changeSvgColor(binding.twentyseven.frameTwentysevenTopBg, binding.twentyseven.frameTwentysevenTopBg.getBackground(), selectedColor);


                                binding.sixteenth.frameSizteenthFollowTxt.setTextColor(selectedColor);
                                binding.seventeenth.frameSeventeenthFollowTxt.setTextColor(selectedColor);
                                binding.nineteen.frameSeventeenthFollowTxt.setTextColor(selectedColor);
                                binding.twenty.frameSeventeenthFollowTxt.setTextColor(selectedColor);
                                binding.twentyone.frameSeventeenthFollowTxt.setTextColor(selectedColor);
                                binding.twentytwo.frameSeventeenthFollowTxt.setTextColor(selectedColor);

                                //new frame fontcolor changer
                              /*  binding.first.companyName.setTextColor(selectedColor);
                               binding.first.facebookIcon.setColorFilter(selectedColor);
                                binding.first.whatsappIcon.setColorFilter(selectedColor);
                                binding.first.twitterIcon.setColorFilter(selectedColor);
                                binding.first.instaIcon.setColorFilter(selectedColor);
*/
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();

            }
        });
        bottomSheetDialog.show();
    }

    public void changeSvgColor(View view, Drawable drawable, int color) {

        Drawable buttonDrawable = drawable;
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        //the color is a direct color int and not a color resource
        DrawableCompat.setTint(buttonDrawable, color);
        view.setBackground(buttonDrawable);
    }

    public void changeDrawablecolor(Drawable drawable, int color) {

        GradientDrawable frame3 = (GradientDrawable) drawable;
        frame3.setTint(color);
    }

    @Override
    public void setStyle(int position) {

        if (position == 0) {
            binding.movableEditText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        if (position == 1) {
            binding.movableEditText.setTypeface(Typeface.SANS_SERIF);
        }
        if (position == 2) {
            binding.movableEditText.setTypeface(Typeface.SERIF);
        }
        if (position == 3) {
            binding.movableEditText.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        }
        if (position == 4) {
            binding.movableEditText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
        if (position == 5) {
            binding.movableEditText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        }
        if (position == 6) {
            binding.movableEditText.setTypeface(Typeface.DEFAULT);
        }
        if (position == 7) {
            binding.movableEditText.setTypeface(Typeface.MONOSPACE);
        }
        if (position == 8) {
            binding.movableEditText.setTypeface(FontCache.getTypeface("Lato_Regular.ttf", getApplicationContext()));
        }
        if (position == 9) {
            binding.movableEditText.setTypeface(FontCache.getTypeface("Lato_Black.ttf", getApplicationContext()));
        }
        if (position == 10) {
            binding.movableEditText.setTypeface(FontCache.getTypeface("Lato-Semibold.ttf", getApplicationContext()));
        }
        if (position == 11) {
            binding.movableEditText.setTypeface(FontCache.getTypeface("Lato-Heavy.ttf", getApplicationContext()));
        }
        if (position == 12) {
            binding.movableEditText.setTypeface(FontCache.getTypeface("Lato_BlackItalic.ttf", getApplicationContext()));
        }
        if (position == 13) {
            binding.movableEditText.setTypeface(FontCache.getTypeface("Lato_BoldItalic.ttf", getApplicationContext()));
        }
        if (position == 14) {
            binding.movableEditText.setTypeface(FontCache.getTypeface("Lato_Thin.ttf", getApplicationContext()));
        }
        if (position == 15) {
            binding.movableEditText.setTypeface(FontCache.getTypeface("Lato_ThinItalic.ttf", getApplicationContext()));
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {

            case R.id.setting_logo_switch:
                if (isChecked) {
                    show_logo();
                    Log.e("switch", "called");

                } else {
                    hide_logo();
                }
                break;
            case R.id.setting_socialmedia_switch:
                if (isChecked) {
                    show_socialmedia();
                } else {
                    hide_socialmedia();
                }
                break;
            case R.id.setting_website_switch:
                if (isChecked) {
                    show_website();
                } else {
                    hide_website();
                }
                break;
            case R.id.setting_email_switch:
                if (isChecked) {
                    show_mail();
                } else {
                    hide_mail();
                }
                break;
            case R.id.setting_phone_switch:
                if (isChecked) {
                    show_call();
                } else {
                    hide_call();
                }
                break;
            case R.id.setting_address_switch:
                if (isChecked) {
                    show_location();
                } else {
                    hide_location();
                }
                break;
            case R.id.setting_whatsapp_switch:
                if (isChecked) {
                   show_whatsapp();
                } else {
                   hide_whatsapp();
                }
                break;
        }
    }
    private void show_whatsapp(){

        //show_whatsapp29
        binding.twentynineNew.whatsappMainRelative.setVisibility(View.VISIBLE);
        binding.twentynineNew.footerWhatsappImage.setVisibility(View.VISIBLE);
        binding.twentynineNew.whatsappIcon.setVisibility(View.VISIBLE);
        //show_whatsapp30

        //show_whatsapp31
        binding.thirtyoneNew.whatsappIcon.setVisibility(View.VISIBLE);

        //show_whatsapp32
        binding.thirtytwoNew.whatsappMailRelative.setVisibility(View.VISIBLE);
        binding.thirtytwoNew.footerStik2.setVisibility(View.VISIBLE);
        binding.thirtytwoNew.whatsappIcon.setVisibility(View.VISIBLE);

        //show_whatsapp33
        binding.thirtythreeNew.businessWhatsappDetails.setVisibility(View.VISIBLE);
        binding.thirtythreeNew.footerWhatsappIcon.setVisibility(View.VISIBLE);
        binding.thirtythreeNew.whatsappIcon.setVisibility(View.VISIBLE);

        //show_whatsapp34
        binding.thirtyfourNew.businessWhatsappDetails.setVisibility(View.VISIBLE);
        binding.thirtyfourNew.whatsappIcon.setVisibility(View.VISIBLE);

        //show_whatsapp35

        //show_whatsapp36
        binding.thirtysixthNew.whatsappIcon.setVisibility(View.VISIBLE);

        //show_whatsapp37
        binding.thirtysevenNew.whatsappIcon.setVisibility(View.VISIBLE);
        //show_whatsapp38
        binding.thirtyeightNew.whatsappIcon.setVisibility(View.VISIBLE);
        //show_whatsapp39
        binding.thirtynineNew.whatsappIcon.setVisibility(View.VISIBLE);
        binding.thirtynineNew.businessWhatsappDetails.setVisibility(View.VISIBLE);
        //show_whatsapp40
        binding.fortyNew.whatsappIcon.setVisibility(View.VISIBLE);
        //show_whatsapp41
        binding.fortyoneNew.whatsappIcon.setVisibility(View.VISIBLE);
        //show_whatsapp42
        binding.fortytwoNew.whatsappIcon.setVisibility(View.VISIBLE);
        //show_whatsapp43
        binding.fortythreeNew.whatsappIcon.setVisibility(View.VISIBLE);
        //show_whatsapp44
        binding.fortyfourNew.whatsappIcon.setVisibility(View.VISIBLE);
        //show_whatsapp45
        binding.fortyfiveNew.whatsappIcon.setVisibility(View.VISIBLE);
        //show_whatsapp46
        binding.fortysixNew.businessWhatsappDetails.setVisibility(View.VISIBLE);
        binding.fortysixNew.whatsappIcon.setVisibility(View.VISIBLE);
        //show_whatsapp47
        binding.fortysevenNew.whatsappIcon.setVisibility(View.VISIBLE);
        //show_whatsapp48
        //show_whatsapp49
        binding.fortynineNew.footerWhatsappIcon.setVisibility(View.VISIBLE);
        //show_whatsapp50
        binding.fiftyNew.whatsappIcon.setVisibility(View.VISIBLE);
        //show_whatsapp51

        //show_whatsapp52
        binding.fiftytwoNew.whatsappIcon.setVisibility(View.VISIBLE);
        //show_whatsapp53
        binding.fiftythreeNew.whatsappIcon.setVisibility(View.VISIBLE);
        }
    private void hide_whatsapp(){
        //hide_whatsapp29

        binding.twentynineNew.whatsappMainRelative.setVisibility(View.INVISIBLE);
        binding.twentynineNew.footerWhatsappImage.setVisibility(View.INVISIBLE);
        binding.twentynineNew.whatsappIcon.setVisibility(View.INVISIBLE);
        //hide_whatsapp30

        //hide_whatsapp31
        binding.thirtyoneNew.whatsappIcon.setVisibility(View.INVISIBLE);

        //hide_whatsapp32
        binding.thirtytwoNew.whatsappMailRelative.setVisibility(View.INVISIBLE);
        binding.thirtytwoNew.footerStik2.setVisibility(View.INVISIBLE);
        binding.thirtytwoNew.whatsappIcon.setVisibility(View.INVISIBLE);

        //hide_whatsapp33
        binding.thirtythreeNew.businessWhatsappDetails.setVisibility(View.INVISIBLE);
        binding.thirtythreeNew.footerWhatsappIcon.setVisibility(View.INVISIBLE);
        binding.thirtythreeNew.whatsappIcon.setVisibility(View.INVISIBLE);

        //hide_whatsapp34
        binding.thirtyfourNew.businessWhatsappDetails.setVisibility(View.INVISIBLE);
        binding.thirtyfourNew.whatsappIcon.setVisibility(View.INVISIBLE);

        //hide_whatsapp35

        //hide_whatsapp36
        binding.thirtysixthNew.whatsappIcon.setVisibility(View.INVISIBLE);

        //hide_whatsapp37
        binding.thirtysevenNew.whatsappIcon.setVisibility(View.INVISIBLE);
        //hide_whatsapp38
        binding.thirtyeightNew.whatsappIcon.setVisibility(View.INVISIBLE);
        //hide_whatsapp39
        binding.thirtynineNew.whatsappIcon.setVisibility(View.INVISIBLE);
        binding.thirtynineNew.businessWhatsappDetails.setVisibility(View.INVISIBLE);
        //hide_whatsapp40
        binding.fortyNew.whatsappIcon.setVisibility(View.INVISIBLE);
        //hide_whatsapp41
        binding.fortyoneNew.whatsappIcon.setVisibility(View.INVISIBLE);
        //hide_whatsapp42
        binding.fortytwoNew.whatsappIcon.setVisibility(View.INVISIBLE);
        //hide_whatsapp43
        binding.fortythreeNew.whatsappIcon.setVisibility(View.INVISIBLE);
        //hide_whatsapp44
        binding.fortyfourNew.whatsappIcon.setVisibility(View.INVISIBLE);
        //hide_whatsapp45
        binding.fortyfiveNew.whatsappIcon.setVisibility(View.INVISIBLE);
        //hide_whatsapp46
        binding.fortysixNew.businessWhatsappDetails.setVisibility(View.INVISIBLE);
        binding.fortysixNew.whatsappIcon.setVisibility(View.INVISIBLE);
        //hide_whatsapp47
        binding.fortysevenNew.whatsappIcon.setVisibility(View.INVISIBLE);
        //hide_whatsapp48
        //hide_whatsapp49
        binding.fortynineNew.footerWhatsappIcon.setVisibility(View.INVISIBLE);
        //hide_whatsapp50
        binding.fiftyNew.whatsappIcon.setVisibility(View.INVISIBLE);
        //hide_whatsapp51

        //hide_whatsapp52
        binding.fiftytwoNew.whatsappIcon.setVisibility(View.INVISIBLE);
        //hide_whatsapp53
        binding.fiftythreeNew.whatsappIcon.setVisibility(View.INVISIBLE);
    }



    private void show_website() {
            //new frame code

            //show_website29
            binding.twentynineNew.websiteMainRelative.setVisibility(View.VISIBLE);
            binding.twentynineNew.footerWebsiteImage.setVisibility(View.VISIBLE);
            //show_website30
            binding.thirtyNew.businessWebsiteDetails.setVisibility(View.VISIBLE);
            binding.thirtyNew.footerWebsiteIcon.setVisibility(View.VISIBLE);
            //show_website31
            binding.thirtyoneNew.businessWebsiteDetails.setVisibility(View.VISIBLE);
            binding.thirtyoneNew.footerWebsiteIcon.setVisibility(View.VISIBLE);

            //show_website32
            binding.thirtytwoNew.businessWebsiteDetails.setVisibility(View.VISIBLE);
            binding.thirtytwoNew.footerWebsiteIcon.setVisibility(View.VISIBLE);
            //show_website33
            binding.thirtythreeNew.businessWebsiteDetails.setVisibility(View.VISIBLE);
            binding.thirtythreeNew.footerWebsiteIcon.setVisibility(View.VISIBLE);
            //show_website34
            binding.thirtyfourNew.businessWebsiteDetails.setVisibility(View.VISIBLE);

            //show_website35
            binding.thirtyfiveNew.businessWebsiteDetails.setVisibility(View.VISIBLE);
            binding.thirtyfiveNew.footerWebsiteIcon.setVisibility(View.VISIBLE);

            //show_website36
            binding.thirtysixthNew.businessWebsiteDetails.setVisibility(View.VISIBLE);

            //show_website37
            binding.thirtysevenNew.businessWebsiteDetails.setVisibility(View.VISIBLE);

            //show_website38
            binding.thirtyeightNew.businessWebsiteDetails.setVisibility(View.VISIBLE);
            binding.thirtyeightNew.footerWebsiteIcon.setVisibility(View.VISIBLE);


            //show_website39
            binding.thirtynineNew.businessWebsiteDetails.setVisibility(View.VISIBLE);

            //show_website40
            binding.fortyNew.businessWebsiteDetails.setVisibility(View.VISIBLE);
            binding.fortyNew.footerWebsiteIcon.setVisibility(View.VISIBLE);
            binding.fortyNew.websiteMainRelative.setVisibility(View.VISIBLE);


            //show_website41
            binding.fortyoneNew.businessWebsiteDetails.setVisibility(View.VISIBLE);

            //show_website42
            binding.fortytwoNew.businessWebsiteDetails.setVisibility(View.VISIBLE);
            binding.fortytwoNew.footerWebsiteIcon.setVisibility(View.VISIBLE);
            binding.fortytwoNew.mailMainRelative.setVisibility(View.VISIBLE);
            //show_website43
            binding.fortythreeNew.businessWebsiteDetails.setVisibility(View.VISIBLE);
            binding.fortythreeNew.footerWebsiteIcon.setVisibility(View.VISIBLE);

            //show_website44

            //show_website45
            binding.fortyfiveNew.businessWebsiteDetails.setVisibility(View.VISIBLE);

            //show_website46

            //show_website47
            binding.fortysevenNew.businessWebsiteDetails.setVisibility(View.VISIBLE);
            binding.fortysevenNew.footerWebsiteIcon.setVisibility(View.VISIBLE);

            //show_website48


            //show_website49
            binding.fortynineNew.businessWebsiteDetails.setVisibility(View.VISIBLE);
            binding.fortynineNew.footerWebsiteIcon.setVisibility(View.VISIBLE);

            //show_website50
            binding.fiftyNew.businessWebsiteDetails.setVisibility(View.VISIBLE);
            binding.fiftyNew.footerWebsiteIcon.setVisibility(View.VISIBLE);
            //show_website51
            binding.fiftyoneNew.businessWebsiteDetails.setVisibility(View.VISIBLE);
            binding.fiftyoneNew.footerWebsiteIcon.setVisibility(View.VISIBLE);
            //show_website52
            binding.fiftytwoNew.businessWebsiteDetails.setVisibility(View.VISIBLE);
            binding.fiftytwoNew.footerWebsiteIcon.setVisibility(View.VISIBLE);
            //show_website53
            binding.fiftythreeNew.businessWebsiteDetails.setVisibility(View.VISIBLE);
            binding.fiftythreeNew.footerWebsiteIcon.setVisibility(View.VISIBLE);


            //old frame code
            binding.first.websiteMainRelative.setVisibility(View.VISIBLE);
            binding.second.websiteMainRelative.setVisibility(View.VISIBLE);
            binding.third.websiteMainRelative.setVisibility(View.VISIBLE);
            binding.sixth.websiteMainRelative.setVisibility(View.VISIBLE);
            binding.nineth.websiteMainRelative.setVisibility(View.VISIBLE);
            binding.tenth.websiteMainRelative.setVisibility(View.VISIBLE);
            binding.eleven.websiteMainRelative.setVisibility(View.VISIBLE);
            binding.thirteen.websiteMainRelative.setVisibility(View.VISIBLE);
            binding.fourteen.websiteMainRelative.setVisibility(View.VISIBLE);
            binding.fifteen.websiteMainRelative.setVisibility(View.VISIBLE);
            binding.sixteenth.websiteMainRelative.setVisibility(View.VISIBLE);
            binding.seventeenth.websiteMainRelative.setVisibility(View.VISIBLE);
            binding.eighteen.websiteMainRelative.setVisibility(View.VISIBLE);

    }

    private void hide_website() {
        //new frame code
        //hide_website29
        binding.twentynineNew.websiteMainRelative.setVisibility(View.GONE);
        binding.twentynineNew.footerWebsiteImage.setVisibility(View.GONE);

        //hide_website30
        binding.thirtyNew.businessWebsiteDetails.setVisibility(View.GONE);
        binding.thirtyNew.footerWebsiteIcon.setVisibility(View.GONE);
        //hide_website31
        binding.thirtyoneNew.businessWebsiteDetails.setVisibility(View.GONE);
        binding.thirtyoneNew.footerWebsiteIcon.setVisibility(View.GONE);

        //hide_website32
        binding.thirtytwoNew.businessWebsiteDetails.setVisibility(View.GONE);
        binding.thirtytwoNew.footerWebsiteIcon.setVisibility(View.GONE);
        //hide_website33
        binding.thirtythreeNew.businessWebsiteDetails.setVisibility(View.GONE);
        binding.thirtythreeNew.footerWebsiteIcon.setVisibility(View.GONE);
        //hide_website34
        binding.thirtyfourNew.businessWebsiteDetails.setVisibility(View.GONE);

        //hide_website35
        binding.thirtyfiveNew.businessWebsiteDetails.setVisibility(View.GONE);
        binding.thirtyfiveNew.footerWebsiteIcon.setVisibility(View.GONE);

        //hide_website36
        binding.thirtysixthNew.businessWebsiteDetails.setVisibility(View.GONE);

        //hide_website37
        binding.thirtysevenNew.businessWebsiteDetails.setVisibility(View.GONE);

        //hide_website38
        binding.thirtyeightNew.businessWebsiteDetails.setVisibility(View.GONE);
        binding.thirtyeightNew.footerWebsiteIcon.setVisibility(View.GONE);


        //hide_website39
        binding.thirtynineNew.businessWebsiteDetails.setVisibility(View.GONE);

        //hide_website40
        binding.fortyNew.businessWebsiteDetails.setVisibility(View.GONE);
        binding.fortyNew.footerWebsiteIcon.setVisibility(View.GONE);
        binding.fortyNew.websiteMainRelative.setVisibility(View.GONE);


        //hide_website41
        binding.fortyoneNew.businessWebsiteDetails.setVisibility(View.GONE);

        //hide_website42
        binding.fortytwoNew.businessWebsiteDetails.setVisibility(View.GONE);
        binding.fortytwoNew.footerWebsiteIcon.setVisibility(View.GONE);
        binding.fortytwoNew.websiteMainRelative.setVisibility(View.GONE);


        //hide_website43
        binding.fortythreeNew.businessWebsiteDetails.setVisibility(View.GONE);
        binding.fortythreeNew.footerWebsiteIcon.setVisibility(View.GONE);

        //hide_website44

        //hide_website45
        binding.fortyfiveNew.businessWebsiteDetails.setVisibility(View.GONE);

        //hide_website46

        //hide_website47
        binding.fortysevenNew.businessWebsiteDetails.setVisibility(View.GONE);
        binding.fortysevenNew.footerWebsiteIcon.setVisibility(View.GONE);

        //hide_website48


        //hide_website49
        binding.fortynineNew.businessWebsiteDetails.setVisibility(View.GONE);
        binding.fortynineNew.footerWebsiteIcon.setVisibility(View.GONE);

        //hide_website50
        binding.fiftyNew.businessWebsiteDetails.setVisibility(View.GONE);
        binding.fiftyNew.footerWebsiteIcon.setVisibility(View.GONE);
        //hide_website51
        binding.fiftyoneNew.businessWebsiteDetails.setVisibility(View.GONE);
        binding.fiftyoneNew.footerWebsiteIcon.setVisibility(View.GONE);
        //hide_website52
        binding.fiftytwoNew.businessWebsiteDetails.setVisibility(View.GONE);
        binding.fiftytwoNew.footerWebsiteIcon.setVisibility(View.GONE);
        //hide_website53
        binding.fiftythreeNew.businessWebsiteDetails.setVisibility(View.GONE);
        binding.fiftythreeNew.footerWebsiteIcon.setVisibility(View.GONE);

        //old frame code
        binding.first.websiteMainRelative.setVisibility(View.GONE);
        binding.second.websiteMainRelative.setVisibility(View.GONE);
        binding.third.websiteMainRelative.setVisibility(View.INVISIBLE);
        binding.sixth.websiteMainRelative.setVisibility(View.GONE);
        binding.nineth.websiteMainRelative.setVisibility(View.INVISIBLE);
        binding.tenth.websiteMainRelative.setVisibility(View.GONE);
        binding.eleven.websiteMainRelative.setVisibility(View.INVISIBLE);
        binding.thirteen.websiteMainRelative.setVisibility(View.GONE);
        binding.fourteen.websiteMainRelative.setVisibility(View.INVISIBLE);
        binding.fifteen.websiteMainRelative.setVisibility(View.INVISIBLE);
        binding.sixteenth.websiteMainRelative.setVisibility(View.INVISIBLE);
        binding.seventeenth.websiteMainRelative.setVisibility(View.INVISIBLE);
        binding.eighteen.websiteMainRelative.setVisibility(View.INVISIBLE);

    }


    private void show_call() {
        //new frame code
        //show_call29
        binding.twentynineNew.callMainRelative.setVisibility(View.VISIBLE);
        binding.twentynineNew.footerCallImage.setVisibility(View.VISIBLE);

        //show_call30
        binding.thirtyNew.footerCallIcon.setVisibility(View.VISIBLE);
        binding.thirtyNew.footerCallIcon1.setVisibility(View.VISIBLE);
        binding.thirtyNew.businessCallDetails.setVisibility(View.VISIBLE);
        binding.thirtyNew.businessCall1Details.setVisibility(View.VISIBLE);
        //show_call31
        binding.thirtyoneNew.businessCallDetails.setVisibility(View.VISIBLE);
        binding.thirtyoneNew.footerCallIcon.setVisibility(View.VISIBLE);
        //show_call32
        binding.thirtytwoNew.businessCallDetails.setVisibility(View.VISIBLE);
        binding.thirtytwoNew.footerCallIcon.setVisibility(View.VISIBLE);

        //show_call33
        binding.thirtythreeNew.businessCallDetails.setVisibility(View.VISIBLE);
        binding.thirtythreeNew.footerCallIcon.setVisibility(View.VISIBLE);

        //show_call34
        binding.thirtyfourNew.businessCallDetails.setVisibility(View.VISIBLE);
        binding.thirtyfourNew.footerCallIcon.setVisibility(View.VISIBLE);

        //show_call35
        binding.thirtyfiveNew.businessCallDetails.setVisibility(View.VISIBLE);
        binding.thirtyfiveNew.footerCallIcon.setVisibility(View.VISIBLE);
        binding.thirtyfiveNew.callMainRelative.setVisibility(View.VISIBLE);


        //show_call36
        binding.thirtysixthNew.businessCallDetails.setVisibility(View.VISIBLE);
        binding.thirtysixthNew.callMainRelative.setVisibility(View.VISIBLE);

        //show_call37
        binding.thirtysevenNew.businessCallDetails.setVisibility(View.VISIBLE);
        binding.thirtysevenNew.footerMailImage22.setVisibility(View.VISIBLE);
        //show_call38
        binding.thirtyeightNew.businessCallDetails.setVisibility(View.VISIBLE);
        binding.thirtyeightNew.footerCallIcon.setVisibility(View.VISIBLE);

        //show_call39
        binding.thirtynineNew.businessCallDetails.setVisibility(View.VISIBLE);
        binding.thirtynineNew.footerCallIcon.setVisibility(View.VISIBLE);
        binding.thirtynineNew.callMainRelative.setVisibility(View.VISIBLE);
        binding.thirtynineNew.callMainRelativeSecond.setVisibility(View.VISIBLE);

        //show_call40
        binding.fortyNew.businessCallDetails.setVisibility(View.VISIBLE);
        binding.fortyNew.footerCallIcon.setVisibility(View.VISIBLE);

        //show_call41
        binding.fortyoneNew.businessCallDetails.setVisibility(View.VISIBLE);
        binding.fortyoneNew.businessCallDetailsText.setVisibility(View.VISIBLE);

        //show_call42
        binding.fortytwoNew.businessCallDetails.setVisibility(View.VISIBLE);
        binding.fortytwoNew.callMainRelative.setVisibility(View.VISIBLE);

        //show_call43
        binding.fortythreeNew.businessCallDetails.setVisibility(View.VISIBLE);
        binding.fortythreeNew.footerCallIcon.setVisibility(View.VISIBLE);

        //show_call44
        binding.fortyfourNew.businessCallDetails.setVisibility(View.VISIBLE);
        binding.fortyfourNew.footerCallIcon.setVisibility(View.VISIBLE);

        //show_call45
        binding.fortyfiveNew.businessCallDetails.setVisibility(View.VISIBLE);

        //show_call46
        binding.fortysixNew.businessCallDetails.setVisibility(View.VISIBLE);
        //show_call47
        binding.fortysevenNew.businessCallDetails.setVisibility(View.VISIBLE);
        binding.fortysevenNew.headerCallIcon.setVisibility(View.VISIBLE);
        binding.fortysevenNew.headerCallIcon1.setVisibility(View.VISIBLE);
        binding.fortysevenNew.callMainRelative.setVisibility(View.VISIBLE);
        binding.fortysevenNew.callMainRelativeSecond.setVisibility(View.VISIBLE);


        //show_call48

        //show_call49
        binding.fortynineNew.businessCallDetails.setVisibility(View.VISIBLE);
        binding.fortynineNew.footerCallIcon.setVisibility(View.VISIBLE);

        //show_call50
        binding.fiftyNew.businessCallDetails.setVisibility(View.VISIBLE);
        binding.fiftyNew.footerCallIcon.setVisibility(View.VISIBLE);

        //show_call51
        binding.fiftyoneNew.businessCallDetails.setVisibility(View.VISIBLE);
        binding.fiftyoneNew.headerCallIcon1.setVisibility(View.VISIBLE);
        binding.fiftyoneNew.headerCallIcon.setVisibility(View.VISIBLE);
        binding.fiftyoneNew.businessCallDetailsSecond.setVisibility(View.VISIBLE);


        //show_call52

        //show_call53
        binding.fiftythreeNew.businessCallDetails.setVisibility(View.VISIBLE);
        binding.fiftythreeNew.footerCallIcon.setVisibility(View.VISIBLE);


        //old frame code
        binding.first.callMainRelative.setVisibility(View.VISIBLE);
        binding.second.callMainRelative.setVisibility(View.VISIBLE);
        binding.third.callMainRelative.setVisibility(View.VISIBLE);
        binding.forth.callMainRelative.setVisibility(View.VISIBLE);
        binding.fifth.callMainRelative.setVisibility(View.VISIBLE);
        binding.sixth.callMainRelative.setVisibility(View.VISIBLE);
        binding.seventh.callMainRelative.setVisibility(View.VISIBLE);
        binding.nineth.callMainRelative.setVisibility(View.VISIBLE);
        binding.tenth.callMainRelative.setVisibility(View.VISIBLE);
        binding.eleven.callMainRelative.setVisibility(View.VISIBLE);
        binding.twelve.callMainRelative.setVisibility(View.VISIBLE);
        binding.thirteen.callMainRelative.setVisibility(View.VISIBLE);
        binding.fourteen.callMainRelative.setVisibility(View.VISIBLE);
        binding.fifteen.callMainRelative.setVisibility(View.VISIBLE);
        binding.sixteenth.callMainRelative.setVisibility(View.VISIBLE);
        binding.seventeenth.callMainRelative.setVisibility(View.VISIBLE);
        binding.eighteen.callMainRelative.setVisibility(View.VISIBLE);
        binding.nineteen.callMainRelative.setVisibility(View.VISIBLE);
        binding.twenty.callMainRelative.setVisibility(View.VISIBLE);
        binding.twentyone.callMainRelative.setVisibility(View.VISIBLE);
        binding.twentytwo.callMainRelative.setVisibility(View.VISIBLE);
        binding.twentythree.callMainRelative.setVisibility(View.VISIBLE);
        binding.twentyfour.callMainRelative.setVisibility(View.VISIBLE);
        binding.twentyfive.callMainRelative.setVisibility(View.VISIBLE);
        binding.twentysix.callMainRelative.setVisibility(View.VISIBLE);
        binding.twentyseven.callMainRelative.setVisibility(View.VISIBLE);
        binding.twentyeight.callMainRelative.setVisibility(View.VISIBLE);
    }

    private void hide_call() {
        //new frame code
        //hide_call29
        binding.twentynineNew.callMainRelative.setVisibility(View.GONE);
        binding.twentynineNew.footerCallImage.setVisibility(View.GONE);
        //hide_call30
        binding.thirtyNew.footerCallIcon.setVisibility(View.GONE);
        binding.thirtyNew.footerCallIcon1.setVisibility(View.GONE);
        binding.thirtyNew.businessCallDetails.setVisibility(View.GONE);
        binding.thirtyNew.businessCall1Details.setVisibility(View.GONE);

        //hide_call31
        binding.thirtyoneNew.businessCallDetails.setVisibility(View.GONE);
        binding.thirtyoneNew.footerCallIcon.setVisibility(View.GONE);

        //hide_call32
        binding.thirtytwoNew.businessCallDetails.setVisibility(View.GONE);
        binding.thirtytwoNew.footerCallIcon.setVisibility(View.GONE);

        //hide_call33
        binding.thirtythreeNew.businessCallDetails.setVisibility(View.GONE);
        binding.thirtythreeNew.footerCallIcon.setVisibility(View.GONE);

        //hide_call34
        binding.thirtyfourNew.businessCallDetails.setVisibility(View.GONE);
        binding.thirtyfourNew.footerCallIcon.setVisibility(View.GONE);

        //hide_call35
        binding.thirtyfiveNew.businessCallDetails.setVisibility(View.GONE);
        binding.thirtyfiveNew.footerCallIcon.setVisibility(View.GONE);
        binding.thirtyfiveNew.callMainRelative.setVisibility(View.GONE);


        //hide_call36
        binding.thirtysixthNew.businessCallDetails.setVisibility(View.GONE);
        binding.thirtysixthNew.callMainRelative.setVisibility(View.GONE);

        //hide_call37
        binding.thirtysevenNew.businessCallDetails.setVisibility(View.GONE);
        binding.thirtysevenNew.footerMailImage22.setVisibility(View.GONE);

        //hide_call38
        binding.thirtyeightNew.businessCallDetails.setVisibility(View.GONE);
        binding.thirtyeightNew.footerCallIcon.setVisibility(View.GONE);

        //hide_call39
        binding.thirtynineNew.businessCallDetails.setVisibility(View.GONE);
        binding.thirtynineNew.footerCallIcon.setVisibility(View.GONE);
        binding.thirtynineNew.callMainRelativeSecond.setVisibility(View.GONE);
        binding.thirtynineNew.callMainRelative.setVisibility(View.GONE);


        //hide_call40
        binding.fortyNew.businessCallDetails.setVisibility(View.GONE);
        binding.fortyNew.footerCallIcon.setVisibility(View.GONE);

        //hide_call41
        binding.fortyoneNew.businessCallDetails.setVisibility(View.GONE);
        binding.fortyoneNew.businessCallDetailsText.setVisibility(View.GONE);

        //hide_call42
        binding.fortytwoNew.businessCallDetails.setVisibility(View.GONE);
        binding.fortytwoNew.callMainRelative.setVisibility(View.GONE);

        //hide_call43
        binding.fortythreeNew.businessCallDetails.setVisibility(View.GONE);
        binding.fortythreeNew.footerCallIcon.setVisibility(View.GONE);

        //hide_call44
        binding.fortyfourNew.businessCallDetails.setVisibility(View.GONE);
        binding.fortyfourNew.footerCallIcon.setVisibility(View.GONE);

        //hide_call45
        binding.fortyfiveNew.businessCallDetails.setVisibility(View.GONE);

        //hide_call46
        binding.fortysixNew.businessCallDetails.setVisibility(View.GONE);
        //hide_call47
        binding.fortysevenNew.businessCallDetails.setVisibility(View.GONE);
        binding.fortysevenNew.headerCallIcon.setVisibility(View.GONE);
        binding.fortysevenNew.headerCallIcon1.setVisibility(View.GONE);
        binding.fortysevenNew.callMainRelative.setVisibility(View.GONE);
        binding.fortysevenNew.callMainRelativeSecond.setVisibility(View.GONE);

        //hide_call48

        //hide_call49
        binding.fortynineNew.businessCallDetails.setVisibility(View.GONE);
        binding.fortynineNew.footerCallIcon.setVisibility(View.GONE);

        //hide_call50
        binding.fiftyNew.businessCallDetails.setVisibility(View.GONE);
        binding.fiftyNew.footerCallIcon.setVisibility(View.GONE);

        //hide_Call51
        binding.fiftyoneNew.businessCallDetails.setVisibility(View.GONE);
        binding.fiftyoneNew.headerCallIcon1.setVisibility(View.GONE);
        binding.fiftyoneNew.headerCallIcon.setVisibility(View.GONE);
        binding.fiftyoneNew.businessCallDetailsSecond.setVisibility(View.GONE);


        //hide_call52

        //hide_Call53
        binding.fiftythreeNew.businessCallDetails.setVisibility(View.GONE);
        binding.fiftythreeNew.footerCallIcon.setVisibility(View.GONE);

        //old frame code
        binding.first.callMainRelative.setVisibility(View.GONE);
        binding.second.callMainRelative.setVisibility(View.GONE);
        binding.third.callMainRelative.setVisibility(View.INVISIBLE);
        binding.forth.callMainRelative.setVisibility(View.INVISIBLE);
        binding.fifth.callMainRelative.setVisibility(View.INVISIBLE);
        binding.sixth.callMainRelative.setVisibility(View.GONE);
        binding.seventh.callMainRelative.setVisibility(View.INVISIBLE);
        binding.nineth.callMainRelative.setVisibility(View.INVISIBLE);
        binding.tenth.callMainRelative.setVisibility(View.GONE);
        binding.eleven.callMainRelative.setVisibility(View.INVISIBLE);
        binding.twelve.callMainRelative.setVisibility(View.INVISIBLE);
        binding.thirteen.callMainRelative.setVisibility(View.GONE);
        binding.fourteen.callMainRelative.setVisibility(View.INVISIBLE);
        binding.fifteen.callMainRelative.setVisibility(View.INVISIBLE);
        binding.sixteenth.callMainRelative.setVisibility(View.INVISIBLE);
        binding.seventeenth.callMainRelative.setVisibility(View.INVISIBLE);
        binding.eighteen.callMainRelative.setVisibility(View.INVISIBLE);
        binding.nineteen.callMainRelative.setVisibility(View.INVISIBLE);
        binding.twenty.callMainRelative.setVisibility(View.INVISIBLE);
        binding.twentyone.callMainRelative.setVisibility(View.INVISIBLE);
        binding.twentytwo.callMainRelative.setVisibility(View.INVISIBLE);
        binding.twentythree.callMainRelative.setVisibility(View.INVISIBLE);
        binding.twentyfour.callMainRelative.setVisibility(View.INVISIBLE);
        binding.twentyfive.callMainRelative.setVisibility(View.INVISIBLE);
        binding.twentysix.callMainRelative.setVisibility(View.INVISIBLE);
        binding.twentyseven.callMainRelative.setVisibility(View.INVISIBLE);
        binding.twentyeight.callMainRelative.setVisibility(View.INVISIBLE);
    }


    private void show_location() {
        //show_location29

        //show_location30
        binding.thirtyNew.businessLocationDetails.setVisibility(View.VISIBLE);
        binding.thirtyNew.footerLocationIcon.setVisibility(View.VISIBLE);
        //show_location31
        binding.thirtyoneNew.businessLocationDetails.setVisibility(View.VISIBLE);
        binding.thirtyoneNew.footerLocationImage.setVisibility(View.VISIBLE);
        //show_location32
        binding.thirtytwoNew.businessLocationDetails.setVisibility(View.VISIBLE);
        //show_location33
        binding.thirtythreeNew.businessLocationDetails.setVisibility(View.VISIBLE);
        binding.thirtythreeNew.footerLocationImage.setVisibility(View.VISIBLE);
        //show_location34
        binding.thirtyfourNew.businessLocationDetails.setVisibility(View.VISIBLE);
        binding.thirtyfourNew.footerLocationIcon.setVisibility(View.VISIBLE);
        //show_location35

        //show_location36

        //show_location37
        binding.thirtysevenNew.businessLocationDetails.setVisibility(View.VISIBLE);

        //show_location38

        //show_location39
        binding.thirtynineNew.businessLocationDetails.setVisibility(View.VISIBLE);
        binding.thirtynineNew.footerLocationIcon.setVisibility(View.VISIBLE);
        //show_location40
        binding.fortyNew.businessLocationDetails.setVisibility(View.VISIBLE);
        /*binding.fortyNew.footerLocationImage.setVisibility(View.VISIBLE);*/

        //show_location41
        binding.fortyoneNew.businessLocationDetails.setVisibility(View.VISIBLE);

        //show_location42
        binding.fortytwoNew.businessLocationDetails.setVisibility(View.VISIBLE);
        binding.fortytwoNew.footerLocationIcon.setVisibility(View.VISIBLE);
        //show_location43

        //show_location44
        binding.fortyfourNew.businessLocationDetails.setVisibility(View.VISIBLE);

        //show_location45

        //show_location46

        //show_location47
        binding.fortysevenNew.businessLocationDetails.setVisibility(View.VISIBLE);
        binding.fortysevenNew.footerLocationIcon.setVisibility(View.VISIBLE);
        //show_location48

        //show_location49
        binding.fortynineNew.businessLocationDetails.setVisibility(View.VISIBLE);
        binding.fortynineNew.footerLocationIcon.setVisibility(View.VISIBLE);
        //show_location50
        binding.fiftyNew.businessLocationDetails.setVisibility(View.VISIBLE);
        /*binding.fiftyNew.footerLocationImage.setVisibility(View.VISIBLE);*/
        //show_location51
        binding.fiftyoneNew.businessLocationDetails.setVisibility(View.VISIBLE);
        binding.fiftyoneNew.footerLocationIcon.setVisibility(View.VISIBLE);
        //show_location52
       binding.fiftytwoNew.businessLocationDetails.setVisibility(View.VISIBLE);
       binding.fiftytwoNew.footerLocationImage.setVisibility(View.VISIBLE);

        //show_location53
        binding.fiftythreeNew.businessLocationDetails.setVisibility(View.VISIBLE);
        binding.fiftythreeNew.footerLocationImage.setVisibility(View.VISIBLE);


        binding.first.locationMainRelative.setVisibility(View.VISIBLE);
        binding.second.locationMainRelative.setVisibility(View.VISIBLE);
        binding.third.locationMainRelative.setVisibility(View.VISIBLE);
        binding.forth.locationMainRelative.setVisibility(View.VISIBLE);
        binding.fifth.locationMainRelative.setVisibility(View.VISIBLE);
        binding.sixth.locationMainRelative.setVisibility(View.VISIBLE);
        binding.seventh.locationMainRelative.setVisibility(View.VISIBLE);
        binding.tenth.locationMainRelative.setVisibility(View.VISIBLE);
        binding.twelve.locationMainRelative.setVisibility(View.VISIBLE);
        binding.thirteen.locationMainRelative.setVisibility(View.VISIBLE);
        binding.fourteen.locationMainRelative.setVisibility(View.VISIBLE);
        binding.nineteen.locationMainRelative.setVisibility(View.VISIBLE);
        binding.twenty.locationMainRelative.setVisibility(View.VISIBLE);
        binding.twentytwo.locationMainRelative.setVisibility(View.VISIBLE);
    }

    private void hide_location() {
        //show_location29

        //show_location30
        binding.thirtyNew.businessLocationDetails.setVisibility(View.INVISIBLE);
        binding.thirtyNew.footerLocationIcon.setVisibility(View.INVISIBLE);
        //show_location31
        binding.thirtyoneNew.businessLocationDetails.setVisibility(View.INVISIBLE);
        binding.thirtyoneNew.footerLocationImage.setVisibility(View.INVISIBLE);
        //show_location32
        binding.thirtytwoNew.businessLocationDetails.setVisibility(View.INVISIBLE);

        //show_location33
        binding.thirtythreeNew.businessLocationDetails.setVisibility(View.INVISIBLE);
        binding.thirtythreeNew.footerLocationImage.setVisibility(View.INVISIBLE);
        //show_location34
        binding.thirtyfourNew.businessLocationDetails.setVisibility(View.INVISIBLE);
        binding.thirtyfourNew.footerLocationIcon.setVisibility(View.INVISIBLE);
        //show_location35

        //show_location36

        //show_location37
        binding.thirtysevenNew.businessLocationDetails.setVisibility(View.INVISIBLE);

        //show_location38

        //show_location39
        binding.thirtynineNew.businessLocationDetails.setVisibility(View.INVISIBLE);
        binding.thirtynineNew.footerLocationIcon.setVisibility(View.INVISIBLE);
        //show_location40
        binding.fortyNew.businessLocationDetails.setVisibility(View.INVISIBLE);
        /*binding.fortyNew.footerLocationImage.setVisibility(View.INVISIBLE);*/
        //show_location41
        binding.fortyoneNew.businessLocationDetails.setVisibility(View.INVISIBLE);

        //show_location42
        binding.fortytwoNew.businessLocationDetails.setVisibility(View.INVISIBLE);
        binding.fortytwoNew.footerLocationIcon.setVisibility(View.INVISIBLE);
        //show_location43

        //show_location44
        binding.fortyfourNew.businessLocationDetails.setVisibility(View.INVISIBLE);

        //show_location45

        //show_location46

        //show_location47
        binding.fortysevenNew.businessLocationDetails.setVisibility(View.INVISIBLE);
        binding.fortysevenNew.footerLocationIcon.setVisibility(View.INVISIBLE);
        //show_location48

        //show_location49
        binding.fortynineNew.businessLocationDetails.setVisibility(View.INVISIBLE);
        binding.fortynineNew.footerLocationIcon.setVisibility(View.INVISIBLE);
        //show_location50
        binding.fiftyNew.businessLocationDetails.setVisibility(View.INVISIBLE);
        /*binding.fiftyNew.footerLocationImage.setVisibility(View.INVISIBLE);*/
        //show_location51
        binding.fiftyoneNew.businessLocationDetails.setVisibility(View.INVISIBLE);
        binding.fiftyoneNew.footerLocationIcon.setVisibility(View.INVISIBLE);
        //show_location52
        binding.fiftytwoNew.businessLocationDetails.setVisibility(View.INVISIBLE);
        binding.fiftytwoNew.footerLocationImage.setVisibility(View.INVISIBLE);

        //show_location53
        binding.fiftythreeNew.businessLocationDetails.setVisibility(View.INVISIBLE);
        binding.fiftythreeNew.footerLocationImage.setVisibility(View.INVISIBLE);

        binding.first.locationMainRelative.setVisibility(View.GONE);
        binding.second.locationMainRelative.setVisibility(View.GONE);
        binding.third.locationMainRelative.setVisibility(View.INVISIBLE);
        binding.forth.locationMainRelative.setVisibility(View.INVISIBLE);
        binding.fifth.locationMainRelative.setVisibility(View.INVISIBLE);
        binding.sixth.locationMainRelative.setVisibility(View.GONE);
        binding.seventh.locationMainRelative.setVisibility(View.INVISIBLE);
        binding.tenth.locationMainRelative.setVisibility(View.GONE);
        binding.twelve.locationMainRelative.setVisibility(View.INVISIBLE);
        binding.thirteen.locationMainRelative.setVisibility(View.GONE);
        binding.fourteen.locationMainRelative.setVisibility(View.INVISIBLE);
        binding.nineteen.locationMainRelative.setVisibility(View.INVISIBLE);
        binding.twenty.locationMainRelative.setVisibility(View.INVISIBLE);
        binding.twentytwo.locationMainRelative.setVisibility(View.INVISIBLE);
    }


    private void show_mail() {
        //new frame code
        //show_mail29
        binding.twentynineNew.mailMainRelative.setVisibility(View.VISIBLE);
        binding.twentynineNew.footerMailImage.setVisibility(View.VISIBLE);
        //show_mail30
        binding.thirtyNew.mailMainRelative.setVisibility(View.VISIBLE);
        //show_mail31
        binding.thirtyoneNew.mailMainRelative.setVisibility(View.VISIBLE);
        //show_mail32
        binding.thirtytwoNew.mailMainRelative.setVisibility(View.VISIBLE);
        //show_mail33
        binding.thirtythreeNew.footerMailImage.setVisibility(View.VISIBLE);
        binding.thirtythreeNew.businessMailDetails.setVisibility(View.VISIBLE);
        //show_mail34
        binding.thirtyfourNew.businessMailDetails.setVisibility(View.VISIBLE);
        binding.thirtyfourNew.footerEmailIcon.setVisibility(View.VISIBLE);
        //show_mail35
        binding.thirtyfiveNew.businessMailDetails.setVisibility(View.VISIBLE);
        binding.thirtyfiveNew.footerMailIcon.setVisibility(View.VISIBLE);
        //show_mail36
        binding.thirtysixthNew.businessMailDetails.setVisibility(View.VISIBLE);
      //show_mail37
        binding.thirtysevenNew.businessMailDetails.setVisibility(View.VISIBLE);
        //show_mail38
        binding.thirtyeightNew.businessMailDetails.setVisibility(View.VISIBLE);
        //show_mail39
        binding.thirtynineNew.businessMailDetails.setVisibility(View.VISIBLE);
        binding.thirtynineNew.footerMailIcon.setVisibility(View.VISIBLE);
        //show_mail40
        binding.fortyNew.businessMailDetails.setVisibility(View.VISIBLE);
        binding.fortyNew.EmailIcon.setVisibility(View.VISIBLE);
        //show_mail41
        binding.fortyoneNew.businessMailDetails.setVisibility(View.VISIBLE);

        //show_mail42
//        binding.fortytwoNew.mailMainRelative112.setVisibility(View.VISIBLE);

        //show_mail43
        binding.fortythreeNew.businessMailDetails.setVisibility(View.VISIBLE);
        binding.fortythreeNew.footerMailIcon.setVisibility(View.VISIBLE);

        //show_mail44
        //show_mail45
        binding.fortyfiveNew.businessMailDetails.setVisibility(View.VISIBLE);
        //show_mail46
        binding.fortysixNew.businessMailDetails.setVisibility(View.VISIBLE);
        binding.fortysixNew.footerMailIcon.setVisibility(View.VISIBLE);
        //show_mail47
        binding.fortysevenNew.businessMailDetails.setVisibility(View.VISIBLE);
        binding.fortysevenNew.footerMailIcon.setVisibility(View.VISIBLE);
        //show_mail48

        //show_mail49
        binding.fortynineNew.businessMailDetails.setVisibility(View.VISIBLE);
        binding.fortynineNew.footerMailIcon.setVisibility(View.VISIBLE);
        //show_mail50
        binding.fiftyNew.businessMailDetails.setVisibility(View.VISIBLE);
        binding.fiftyNew.EmailIcon.setVisibility(View.VISIBLE);
        //show_mail51
        binding.fiftyoneNew.businessMailDetails.setVisibility(View.VISIBLE);
        binding.fiftyoneNew.footerMailIcon.setVisibility(View.VISIBLE);
        //show_mail52
        binding.fiftytwoNew.businessMailDetails.setVisibility(View.VISIBLE);
        binding.fiftytwoNew.footerMailIcon.setVisibility(View.VISIBLE);
        //show_mail53
        binding.fiftythreeNew.businessMailDetails.setVisibility(View.VISIBLE);
        binding.fiftythreeNew.footerMailIcon.setVisibility(View.VISIBLE);




        //old frame code
        binding.first.mailMainRelative.setVisibility(View.VISIBLE);
        binding.second.mailMainRelative.setVisibility(View.VISIBLE);
        binding.third.mailMainRelative.setVisibility(View.VISIBLE);
        binding.forth.mailMainRelative.setVisibility(View.VISIBLE);
        binding.fifth.mailMainRelative.setVisibility(View.VISIBLE);
        binding.sixth.mailMainRelative.setVisibility(View.VISIBLE);
        binding.seventh.mailMainRelative.setVisibility(View.VISIBLE);
        binding.tenth.mailMainRelative.setVisibility(View.VISIBLE);
        binding.eleven.mailMainRelative.setVisibility(View.VISIBLE);
        binding.twelve.mailMainRelative.setVisibility(View.VISIBLE);
        binding.thirteen.mailMainRelative.setVisibility(View.VISIBLE);
        binding.fourteen.mailMainRelative.setVisibility(View.VISIBLE);
        binding.fifteen.mailMainRelative.setVisibility(View.VISIBLE);
        binding.eighteen.mailMainRelative.setVisibility(View.VISIBLE);
        binding.nineteen.mailMainRelative.setVisibility(View.VISIBLE);
        binding.twenty.mailMainRelative.setVisibility(View.VISIBLE);
        binding.twentyone.mailMainRelative.setVisibility(View.VISIBLE);
        binding.twentytwo.mailMainRelative.setVisibility(View.VISIBLE);
        binding.twentythree.mailMainRelative.setVisibility(View.VISIBLE);
        binding.twentyfour.mailMainRelative.setVisibility(View.VISIBLE);
        binding.twentyfive.mailMainRelative.setVisibility(View.VISIBLE);
        binding.twentysix.mailMainRelative.setVisibility(View.VISIBLE);
        binding.twentyseven.mailMainRelative.setVisibility(View.VISIBLE);
        binding.twentyeight.mailMainRelative.setVisibility(View.VISIBLE);
    }

    private void hide_mail() {
        //new frame code

        //hide_mail29
        binding.twentynineNew.mailMainRelative.setVisibility(View.GONE);
        binding.twentynineNew.footerMailImage.setVisibility(View.GONE);
        //hide_mail30
        binding.thirtyNew.mailMainRelative.setVisibility(View.GONE);
        //hide_mail31
        binding.thirtyoneNew.mailMainRelative.setVisibility(View.GONE);
        //hide_mail32
        binding.thirtytwoNew.mailMainRelative.setVisibility(View.GONE);
        //hide_mail33
        binding.thirtythreeNew.footerMailImage.setVisibility(View.GONE);
        binding.thirtythreeNew.businessMailDetails.setVisibility(View.GONE);
        //hide_mail34
        binding.thirtyfourNew.businessMailDetails.setVisibility(View.GONE);
        binding.thirtyfourNew.footerEmailIcon.setVisibility(View.GONE);
        //hide_mail35
        binding.thirtyfiveNew.businessMailDetails.setVisibility(View.GONE);
        binding.thirtyfiveNew.footerMailIcon.setVisibility(View.GONE);
        //hide_mail36
        binding.thirtysixthNew.businessMailDetails.setVisibility(View.GONE);
        //hide_mail37
        binding.thirtysevenNew.businessMailDetails.setVisibility(View.GONE);
        //hide_mail38
        binding.thirtyeightNew.businessMailDetails.setVisibility(View.GONE);
        //hide_mail39
        binding.thirtynineNew.businessMailDetails.setVisibility(View.GONE);
        binding.thirtynineNew.footerMailIcon.setVisibility(View.GONE);
        //hide_mail40
        binding.fortyNew.businessMailDetails.setVisibility(View.GONE);
        binding.fortyNew.EmailIcon.setVisibility(View.GONE);
        //hide_mail41
        binding.fortyoneNew.businessMailDetails.setVisibility(View.GONE);

        //hide_mail42
//        binding.fortytwoNew.mailMainRelative112.setVisibility(View.GONE);
        //hide_mail43
        binding.fortythreeNew.businessMailDetails.setVisibility(View.GONE);
        binding.fortythreeNew.footerMailIcon.setVisibility(View.GONE);

        //hide_mail44
        //hide_mail45
        binding.fortyfiveNew.businessMailDetails.setVisibility(View.GONE);
        //hide_mail46
        binding.fortysixNew.businessMailDetails.setVisibility(View.GONE);
        binding.fortysixNew.footerMailIcon.setVisibility(View.GONE);
        //hide_mail47
        binding.fortysevenNew.businessMailDetails.setVisibility(View.GONE);
        binding.fortysevenNew.footerMailIcon.setVisibility(View.GONE);
        //hide_mail48

        //hide_mail49
        binding.fortynineNew.businessMailDetails.setVisibility(View.GONE);
        binding.fortynineNew.footerMailIcon.setVisibility(View.GONE);
        //hide_mail50
        binding.fiftyNew.businessMailDetails.setVisibility(View.GONE);
        binding.fiftyNew.EmailIcon.setVisibility(View.GONE);
        //hide_mail51
        binding.fiftyoneNew.businessMailDetails.setVisibility(View.GONE);
        binding.fiftyoneNew.footerMailIcon.setVisibility(View.GONE);
        //hide_mail52
        binding.fiftytwoNew.businessMailDetails.setVisibility(View.GONE);
        binding.fiftytwoNew.footerMailIcon.setVisibility(View.GONE);
        //hide_mail53
        binding.fiftythreeNew.businessMailDetails.setVisibility(View.GONE);
        binding.fiftythreeNew.footerMailIcon.setVisibility(View.GONE);

        //old frame code
        binding.first.mailMainRelative.setVisibility(View.GONE);
        binding.second.mailMainRelative.setVisibility(View.GONE);
        binding.third.mailMainRelative.setVisibility(View.INVISIBLE);
        binding.forth.mailMainRelative.setVisibility(View.INVISIBLE);
        binding.fifth.mailMainRelative.setVisibility(View.INVISIBLE);
        binding.sixth.mailMainRelative.setVisibility(View.GONE);
        binding.seventh.mailMainRelative.setVisibility(View.INVISIBLE);
        binding.tenth.mailMainRelative.setVisibility(View.GONE);
        binding.eleven.mailMainRelative.setVisibility(View.INVISIBLE);
        binding.twelve.mailMainRelative.setVisibility(View.INVISIBLE);
        binding.thirteen.mailMainRelative.setVisibility(View.GONE);
        binding.fourteen.mailMainRelative.setVisibility(View.INVISIBLE);
        binding.fifteen.mailMainRelative.setVisibility(View.INVISIBLE);
        binding.eighteen.mailMainRelative.setVisibility(View.INVISIBLE);
        binding.nineteen.mailMainRelative.setVisibility(View.INVISIBLE);
        binding.twenty.mailMainRelative.setVisibility(View.INVISIBLE);
        binding.twentyone.mailMainRelative.setVisibility(View.INVISIBLE);
        binding.twentytwo.mailMainRelative.setVisibility(View.INVISIBLE);
        binding.twentythree.mailMainRelative.setVisibility(View.INVISIBLE);
        binding.twentyfour.mailMainRelative.setVisibility(View.INVISIBLE);
        binding.twentyfive.mailMainRelative.setVisibility(View.INVISIBLE);
        binding.twentysix.mailMainRelative.setVisibility(View.INVISIBLE);
        binding.twentyseven.mailMainRelative.setVisibility(View.INVISIBLE);
        binding.twentyeight.mailMainRelative.setVisibility(View.INVISIBLE);
    }


    private void show_socialmedia() {
        //new frame code
        //show_socialmedia29

        binding.twentynineNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        //show_soicalmedia30
        binding.thirtyNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);

        //show_soicalmedia31
        binding.thirtyoneNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);

        //show_soicalmedia32
        binding.thirtytwoNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);


        //show_soicalmedia33
        binding.thirtythreeNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);

        //show_soicalmedia34
        binding.thirtyfourNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);

        //show_soicalmedia35
        binding.thirtyfiveNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);

        //show_soicalmedia36
        binding.thirtysixthNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);

            //show_soicalmedia37
       binding.thirtysevenNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);

        //show_soicalmedia38
        binding.thirtyeightNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);


        //show_soicalmedia39
        binding.thirtynineNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);

        //show_soicalmedia40
        binding.fortyNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        //show_soicalmedia41
        binding.fortyoneNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);

        //show_soicalmedia42
        binding.fortytwoNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);

        //show_soicalmedia43
        binding.fortythreeNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);

        //show_soicalmedia44
        binding.fortyfourNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);

        //show_soicalmedia45
        binding.fortyfiveNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);

        //show_soicalmedia46
        binding.fortysixNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);

        //show_soicalmedia47
        binding.fortysevenNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);


        //show_soicalmedia48

        //show_soicalmedia49
        binding.fortynineNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);

        //show_soicalmedia50
        binding.fiftyNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);

        //show_soicalmedia51
        binding.fiftyoneNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);

        //show_soicalmedia52
        binding.fiftytwoNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);

        //show_soicalmedia53
        binding.fiftythreeNew.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);

        //old frame code
        binding.first.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.second.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.third.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.fifth.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.sixth.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.seventh.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.tenth.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.eleven.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.twelve.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.thirteen.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.fourteen.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.fifteen.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.sixteenth.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.seventeenth.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.eighteen.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.nineteen.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.twenty.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.twentyone.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.twentytwo.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.twentythree.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.twentyfour.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.twentyfive.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.twentysix.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.twentyseven.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
        binding.twentyeight.businessSocialmediaImageRelative.setVisibility(View.VISIBLE);
    }

    private void hide_socialmedia() {
        //new frame code
          //hide_socialmedia29
        binding.twentynineNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        //hide_socialmedia30
        binding.thirtyNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);

        //hide_socialmedia31
        binding.thirtyoneNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);

        //hide_socialmedia32
        binding.thirtytwoNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);


        //hide_socialmedia33
        binding.thirtythreeNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);

        //hide_socialmedia34
        binding.thirtyfourNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);

        //hide_socialmedia35
        binding.thirtyfiveNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);

        //hide_socialmedia36
        binding.thirtysixthNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);

        //hide_socialmedia37

        binding.thirtysevenNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);

        //hide_socialmedia38
        binding.thirtyeightNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);


        //hide_socialmedia39
        binding.thirtynineNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);

        //hide_socialmedia40
        binding.fortyNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        //hide_socialmedia41
        binding.fortyoneNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);

        //hide_socialmedia42
        binding.fortytwoNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);

        //hide_socialmedia43
        binding.fortythreeNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);

        //hide_socialmedia44
        binding.fortyfourNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);

        //hide_socialmedia45
        binding.fortyfiveNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);

        //hide_socialmedia46
        binding.fortysixNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);

        //hide_socialmedia47
        binding.fortysevenNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);


        //hide_socialmedia48

        //hide_socialmedia49
        binding.fortynineNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);

        //hide_socialmedia50
        binding.fiftyNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);

        //hide_socialmedia51
        binding.fiftyoneNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);

        //hide_socialmedia52
        binding.fiftytwoNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);

        //hide_socialmedia53
        binding.fiftythreeNew.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);



        //old frame code

        binding.first.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.second.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.third.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.fifth.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.sixth.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.seventh.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.tenth.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.eleven.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.twelve.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.thirteen.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.fourteen.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.fifteen.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.sixteenth.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.seventeenth.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.eighteen.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.nineteen.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.twenty.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.twentyone.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.twentytwo.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.twentythree.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.twentyfour.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.twentyfive.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.twentysix.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.twentyseven.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
        binding.twentyeight.businessSocialmediaImageRelative.setVisibility(View.INVISIBLE);
    }

    private void show_logo() {
        //new frame code
        //show_logo29
        binding.twentynineNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo30
        binding.thirtyNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo31
        binding.thirtyoneNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo32
        binding.thirtytwoNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo33
        binding.thirtythreeNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo34
        binding.thirtyfourNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo35
        binding.thirtyfiveNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo36
        binding.thirtysixthNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo37
        binding.thirtysevenNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo38
        binding.thirtyeightNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo39
        binding.thirtynineNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo40
        binding.fortyNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo41
        binding.fortyoneNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo42
        binding.fortytwoNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo43
        binding.fortythreeNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo44
        binding.fortyfourNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo45
        binding.fortyfiveNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo46
        binding.fortysixNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo47
        binding.fortysevenNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo48
        binding.fortyeightNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo49
        binding.fortynineNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo50
        binding.fiftyNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo51
        binding.fiftyoneNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo52
        binding.fiftytwoNew.businessLogoRelative.setVisibility(View.VISIBLE);
        //show_logo53
        binding.fiftythreeNew.businessLogoRelative.setVisibility(View.VISIBLE);

        //old frame code
        binding.first.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.second.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.third.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.forth.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.fifth.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.sixth.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.seventh.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.nineth.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.tenth.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.eleven.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.twelve.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.thirteen.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.fourteen.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.fifteen.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.sixteenth.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.seventeenth.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.eighteen.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.nineteen.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.twenty.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.twentyone.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.twentytwo.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.twentythree.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.twentyfour.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.twentyfive.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.twentysix.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.twentyseven.businessLogoRelative.setVisibility(View.VISIBLE);
        binding.twentyeight.businessLogoRelative.setVisibility(View.VISIBLE);
    }

    private void hide_logo() {
        //new frame code
        //hide_logo29
        binding.twentynineNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo30
        binding.thirtyNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo31
        binding.thirtyoneNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo32
        binding.thirtytwoNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo33
        binding.thirtythreeNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo34
        binding.thirtyfourNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo35
        binding.thirtyfiveNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo36
        binding.thirtysixthNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo37
        binding.thirtysevenNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo38
        binding.thirtyeightNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo39
        binding.thirtynineNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo40
        binding.fortyNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo41
        binding.fortyoneNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo42
        binding.fortytwoNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo43
        binding.fortythreeNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo44
        binding.fortyfourNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo45
        binding.fortyfiveNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo46
        binding.fortysixNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo47
        binding.fortysevenNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo48
        binding.fortyeightNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo49
        binding.fortynineNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo50
        binding.fiftyNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo51
        binding.fiftyoneNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo52
        binding.fiftytwoNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        //hide_logo53
        binding.fiftythreeNew.businessLogoRelative.setVisibility(View.INVISIBLE);
        
        //old frame code
        binding.first.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.second.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.third.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.forth.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.fifth.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.sixth.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.seventh.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.nineth.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.tenth.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.eleven.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.twelve.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.thirteen.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.fourteen.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.fifteen.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.sixteenth.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.seventeenth.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.eighteen.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.nineteen.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.twenty.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.twentyone.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.twentytwo.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.twentythree.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.twentyfour.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.twentyfive.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.twentysix.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.twentyseven.businessLogoRelative.setVisibility(View.INVISIBLE);
        binding.twentyeight.businessLogoRelative.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onEditTextChangeListener(View rootView, String text, int colorCode, int pos) {
        TextEditorDialogFragment textEditorDialogFragment =
                TextEditorDialogFragment.show(this, text, colorCode, pos);
        textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
            @Override
            public void onDone(String inputText, int colorCode, int position) {
                final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                styleBuilder.withTextColor(colorCode);
                Typeface typeface = ResourcesCompat.getFont(ImageCanvasActivity.this, TextEditorDialogFragment.getDefaultFontIds(ImageCanvasActivity.this).get(position));
                styleBuilder.withTextFont(typeface);
                mPhotoEditor.editText(rootView, inputText, styleBuilder, position);
            }
        });
    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d("TAG", "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");

    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d("TAG", "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");

    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
        Log.d("TAG", "onStartViewChangeListener() called with: viewType = [" + viewType + "]");

    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
        Log.d("TAG", "onStopViewChangeListener() called with: viewType = [" + viewType + "]");

    }

    @Override
    public void onStickerClick(Bitmap bitmap) {
        //binding.imgDraw.setBackgroundColor(ContextCompat.getColor(this, R.color.black_trasp));
        mPhotoEditor.addImage(bitmap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}