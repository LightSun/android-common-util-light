package com.heaven7.core.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class ConfigUtil {

	public static Properties loadAssetsConfig(Context context, String filename){
		try {
			InputStream in = context.getAssets().open(filename);
			return load(in, "caused by under the /assets, filename ="+filename+" load failed or File Not Found !");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * load the xxx.properties from src
	 * <p>Note: Game Framework may cannot load.</p>
	 * @param configName exclude extension
	 * @return the  Properties map
	 */
	public static Properties loadSrcConfig(String configName){
		InputStream in = ConfigUtil.class.getResourceAsStream("/"+configName+".properties");
		return load(in, "caused by under the src ,config ="+configName+".properties load failed or File Not Found !");
	}
	
	/** load properties which is under the raw (exclude extension )
	 * @param context the context
	 * @param resId the resource id
	 * @return the   Properties
	 * */
	public static Properties loadRawConfig(Context context,int resId) {

		InputStream  in = context.getResources().openRawResource(resId);// 不能加扩展名
		if (in == null)
			throw new IllegalStateException(
					"caused by under /res/raw the config of "+resId+" is not found!");
		
		return load(in, "caused by config ="+resId+".properties load failed or File Not Found !");
	}
	
	private static Properties load(InputStream in,String exceptionMsg){
		Properties prop = new Properties();
		try {
			prop.load(in);
			Object value;
			for(Map.Entry<Object, Object> en: prop.entrySet()){
				value = en.getValue();
				if(value instanceof String){
					en.setValue(((String)value).trim());
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(exceptionMsg);
		}
		try {
			in.close();
		} catch (IOException e) {
		}
		return prop;
	}

}
