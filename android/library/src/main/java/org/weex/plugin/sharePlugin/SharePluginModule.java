package org.weex.plugin.sharePlugin;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2017/10/24.
 * @author liumeng
 * 分享功能集成
 */

public class SharePluginModule extends WXModule {

    @JSMethod(uiThread = true)
    public void registerSDK(Map<String,Object> parms, JSCallback myCallback){

        /**
         * <Wechat
         Id="4"
         SortId="4"
         AppId="wx4868b35061f87885"
         AppSecret="64020361b8ec4c99936c0e3999a9f249"
         userName="gh_afb25ac019c9"
         path="pages/index/index.html?id=1"
         BypassApproval="true"
         Enable="true" />
         */

        Log.e("registerSDK-->",""+parms.toString());
        HashMap<String,Object> myParms1 = new HashMap<String,Object>();

        myParms1.put("Id","4");
        myParms1.put("SortId","4");
        myParms1.put("AppId",parms.get("WeiXinAppKey").toString());
        myParms1.put("AppSecret",parms.get("WeiXinAppSecret").toString());
        myParms1.put("ShareByAppClient","true");
        myParms1.put("userName","gh_afb25ac019c9");
        myParms1.put("BypassApproval","false");
        myParms1.put("Enable","true");

        ShareSDK.setPlatformDevInfo(Wechat.NAME,myParms1);

        /**
         * <WechatMoments
         Id="5"
         SortId="5"
         AppId="wx4868b35061f87885"
         AppSecret="64020361b8ec4c99936c0e3999a9f249"
         BypassApproval="true"
         Enable="true" />
         */

        HashMap<String,Object> myParms2 = new HashMap<String,Object>();
        myParms2.put("Id","5");
        myParms2.put("SortId","5");
        myParms2.put("AppId",parms.get("WeiXinAppKey").toString());
        myParms2.put("AppSecret",parms.get("WeiXinAppSecret").toString());
        myParms2.put("BypassApproval","false");
        myParms2.put("Enable","true");
        ShareSDK.setPlatformDevInfo(WechatMoments.NAME,myParms2);

        /**
         *<QQ
         Id="7"
         SortId="7"
         AppId="100371282"
         AppKey="aed9b0303e3ed1e27bae87c33761161d"
         BypassApproval="true"
         ShareByAppClient="true"
         Enable="true" />
         */
        HashMap<String,Object> myParms3 = new HashMap<String,Object>();
        myParms3.put("Id","7");
        myParms3.put("SortId","7");
        myParms3.put("AppId",parms.get("QQAppKey").toString());
        myParms3.put("AppKey",parms.get("QQAppSecret").toString());
        myParms3.put("BypassApproval","false");
        myParms3.put("ShareByAppClient","true");
        myParms3.put("Enable","true");
        ShareSDK.setPlatformDevInfo(QQ.NAME,myParms3);


        /**
         * <QZone
         Id="3"
         SortId="3"
         AppId="100371282"
         AppKey="aed9b0303e3ed1e27bae87c33761161d"
         BypassApproval="true"
         Enable="true" />
         */
        HashMap<String,Object> myParms4 = new HashMap<String,Object>();
        myParms4.put("Id","3");
        myParms4.put("SortId","3");
        myParms4.put("AppId",parms.get("QQAppKey").toString());
        myParms4.put("AppKey",parms.get("QQAppSecret").toString());
        myParms4.put("BypassApproval","false");
        myParms4.put("Enable","true");
        ShareSDK.setPlatformDevInfo(QZone.NAME,myParms4);

    }

    @JSMethod (uiThread = false)
    public void share(Map<String,Object> parms, JSCallback myCallback){
        ShareActivity.myCallback = myCallback;
        Intent intent = new Intent(mWXSDKInstance.getContext(),ShareActivity.class);
        intent.putExtra("shareTitle",parms.get("title").toString());
        intent.putExtra("shareContent",parms.get("text").toString());
        intent.putExtra("url",parms.get("url").toString());
        intent.putExtra("imagePath",parms.get("image").toString());
        mWXSDKInstance.getContext().startActivity(intent);

    }

    @JSMethod (uiThread = false)
    public void getUserInfo(String id, final JSCallback myCallback){
        Platform myPlatform = null;

        if("997".equals(id)){
            myPlatform = ShareSDK.getPlatform(Wechat.NAME);
        }else if("998".equals(id)){
            myPlatform = ShareSDK.getPlatform(QQ.NAME);
        }else{
            Toast.makeText(mWXSDKInstance.getContext(),"id传入错误",Toast.LENGTH_LONG).show();
        }

        //回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
        myPlatform.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                // TODO Auto-generated method stub
                HashMap<String,Object> params = new HashMap<String,Object>();
                params.put("state",2);
                params.put("user","");
                params.put("error","SSDKResponseStateFail");
                arg2.printStackTrace();
                myCallback.invoke(arg2);

            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                // TODO Auto-generated method stub
                //输出所有授权信息
                arg0.getDb().exportData();

                HashMap<String,Object> params = new HashMap<String,Object>();
                params.put("state",1);
                params.put("user",arg2);
                params.put("error","SSDKResponseStateSuccess");
                myCallback.invoke(arg2);
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                // TODO Auto-generated method stub
                HashMap<String,Object> params = new HashMap<String,Object>();
                params.put("state",3);
                params.put("user","");
                params.put("error","SSDKResponseStateCancel");

                myCallback.invoke(params);

            }
        });
        //authorize与showUser单独调用一个即可
        //myPlatform.authorize();//单独授权,OnComplete返回的hashmap是空的
        myPlatform.showUser(null);//授权并获取用户信息
        //移除授权
        //weibo.removeAccount(true);
    }

}
