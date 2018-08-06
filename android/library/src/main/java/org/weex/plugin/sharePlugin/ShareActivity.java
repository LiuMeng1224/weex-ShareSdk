package org.weex.plugin.sharePlugin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.taobao.weex.bridge.JSCallback;

import java.io.File;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * @author liumeng
 * 描述：分享页面
 */
public class ShareActivity extends Activity implements
        PlatformActionListener {

	private ImageView iv_friends;
	private ImageView iv_weixin;
	private ImageView iv_qq;
	private ImageView iv_space;
	private View empty_view;
	private TextView tv_confirm;
	
	private String url = "";
	private String shareContent = "";
	private String shareTitle = "";

	private String imagePath = "";

	public static JSCallback myCallback = null;
	
	//招聘只分享到微信，朋友圈
	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		init();
	}

	public void init() {

		iv_friends = (ImageView)findViewById(R.id.iv_friends);
		iv_weixin = (ImageView)findViewById(R.id.iv_weixin);
		iv_qq = (ImageView)findViewById(R.id.iv_qq);
		iv_space = (ImageView)findViewById(R.id.iv_space);

		empty_view = (View)findViewById(R.id.empty_view);
		tv_confirm = (TextView) findViewById(R.id.tv_confirm);

		iv_friends.setOnClickListener(new MyClickListener());
		iv_weixin.setOnClickListener(new MyClickListener());
		iv_qq.setOnClickListener(new MyClickListener());
		iv_space.setOnClickListener(new MyClickListener());

		empty_view.setOnClickListener(new MyClickListener());
		tv_confirm.setOnClickListener(new MyClickListener());


		shareTitle = getIntent().getStringExtra("shareTitle");
		shareContent = getIntent().getStringExtra("shareContent");
		url = getIntent().getStringExtra("url");
		imagePath = getIntent().getStringExtra("imagePath");

		if(TextUtils.isEmpty(shareTitle)){
			shareTitle = "分享标题";
		}

		if(TextUtils.isEmpty(shareContent)){
			shareContent = "分享内容...";
		}

		if(TextUtils.isEmpty(url)){
			url = "https://weex.apache.org/cn/guide/";
		}

		if(!TextUtils.isEmpty(imagePath)){
			imagePath = "/sdcard/DCIM/"+imagePath;
		}

	}

	class MyClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			if(v.getId()== R.id.iv_friends){
				// 微信朋友圈
				showOnekeyshare(ShareActivity.this, WechatMoments.NAME,
						false);
				finish();
				overridePendingTransition(R.anim.slide_in_from_top,
						R.anim.slide_out_to_bottom);
			}else if(v.getId()==R.id.iv_weixin){
				// 微信
				showOnekeyshare(ShareActivity.this, Wechat.NAME, false);
				finish();
				overridePendingTransition(R.anim.slide_in_from_top,
						R.anim.slide_out_to_bottom);
			}else if(v.getId()==R.id.iv_qq){
				// QQ
				showOnekeyshare(ShareActivity.this, QQ.NAME, false);
				finish();
				overridePendingTransition(R.anim.slide_in_from_top,
						R.anim.slide_out_to_bottom);
			}else if(v.getId()==R.id.iv_space){
				// QQ空间
				showOnekeyshare(ShareActivity.this, QZone.NAME, false);
				finish();
				overridePendingTransition(R.anim.slide_in_from_top,
						R.anim.slide_out_to_bottom);
			}else if(v.getId()==R.id.empty_view||v.getId()==R.id.tv_confirm){
				finish();
				overridePendingTransition(R.anim.slide_in_from_top,
						R.anim.slide_out_to_bottom);
			}
			/*switch (v.getId()) {
				case R.id.iv_friends:
					// 微信朋友圈
					showOnekeyshare(url, ShareActivity.this, WechatMoments.NAME,
							false);
					finish();
					overridePendingTransition(R.anim.slide_in_from_top,
							R.anim.slide_out_to_bottom);
					break;
				case R.id.iv_weixin:
					// 微信
					showOnekeyshare(url,ShareActivity.this, Wechat.NAME, false);
					finish();
					overridePendingTransition(R.anim.slide_in_from_top,
							R.anim.slide_out_to_bottom);
					break;
				case R.id.iv_qq:
					// QQ
					showOnekeyshare(url,ShareActivity.this, QQ.NAME, false);
					finish();
					overridePendingTransition(R.anim.slide_in_from_top,
							R.anim.slide_out_to_bottom);
					break;
				case R.id.iv_space:
					// QQ空间
					showOnekeyshare(url, ShareActivity.this, QZone.NAME, false);
					finish();
					overridePendingTransition(R.anim.slide_in_from_top,
							R.anim.slide_out_to_bottom);
					break;
				case R.id.empty_view:
				case R.id.tv_confirm:
					finish();
					overridePendingTransition(R.anim.slide_in_from_top,
							R.anim.slide_out_to_bottom);
					break;
				default:
					break;
			}*/
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			overridePendingTransition(R.anim.slide_in_from_top,
					R.anim.slide_out_to_bottom);
			break;

		default:
			break;
		}
		return true;

	}

	@SuppressWarnings("deprecation")
	@SuppressLint("SdCardPath")
	public void showOnekeyshare(Context context, String platform, boolean silent) {
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
//		oks.setDialogMode();
		// 分享时Notification的图标和文字
		// oks.setNotification(R.drawable.ybb_logo,context.getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(shareTitle);
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(url);
		// text是分享文本，所有平台都需要这个字段
		oks.setText(shareContent);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");
		File file = new File(imagePath);
		if (!file.exists()) {
			oks.setImageUrl("http://img1.vued.vanthink.cn/vued08aa73a9ab65dcbd360ec54659ada97c.png");// 确保SDcard下面存在此张图片
		} else {
			oks.setImagePath(imagePath);
		}
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(url);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment(shareContent);
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");
		// 是否直接分享（true则直接分享）
		oks.setSilent(silent);
		// 指定分享平台，和slient一起使用可以直接分享到指定的平台
		if (platform != null) {
			oks.setPlatform(platform);
		}
		// 启动分享GUI
		oks.show(this);
		oks.setCallback(this);
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {

		Log.e("onCancel","arg1---------》"+arg1);

	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {

		Log.e("onComplete","arg1---------》"+arg1);

		if(myCallback!=null){
			myCallback.invoke(arg2);
		}

	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		Log.e("onError","arg1---------》"+arg1);
	}
}
