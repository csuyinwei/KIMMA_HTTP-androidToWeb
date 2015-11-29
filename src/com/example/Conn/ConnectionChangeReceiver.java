package com.example.Conn;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/*�㲥�࣬�Ƿ������ͨ��
 *     �ڳ���������ʱ��ע�����
 */

public class ConnectionChangeReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo  mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            Toast.makeText(context,"��������ʧ��,�������",Toast.LENGTH_LONG).show();
        }		
	}

}
