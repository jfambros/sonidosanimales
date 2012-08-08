package com.amber;

import android.content.Context;
import android.telephony.TelephonyManager;

public class EsTelefono {
		public boolean verifica(Context context){
			TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
	        if(manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE){
	            return false;
	        }else{
	            return true;
	        }
		}
}
