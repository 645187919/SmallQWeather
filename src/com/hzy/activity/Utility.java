package com.hzy.activity;

import com.hzy.activity.SmallQWeatherDB;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.hzy.activity.City;
import com.hzy.activity.County;
import com.hzy.activity.Province;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class Utility {
	public synchronized static boolean handleProvincesResponse(
			SmallQWeatherDB coolWeatherDB, String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");
			if (allProvinces != null && allProvinces.length > 0) {
				for (String p : allProvinces) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
										coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}

	public static boolean handleCitiesResponse(SmallQWeatherDB coolWeatherDB,
			String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if (allCities != null && allCities.length > 0) {
				for (String c : allCities) {
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}

	public static boolean handleCountiesResponse(SmallQWeatherDB coolWeatherDB,
			String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCounties = response.split(",");
			if (allCounties != null && allCounties.length > 0) {
				for (String c : allCounties) {
					String[] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}

	
	
	public static void parseJSONWithJSONObject(Context context,String jsonData) {
		
		JSONObject jsonObject = null;
		Log.d("Utility", jsonData);
		try {
			jsonObject =new JSONObject(jsonData);
			JSONObject result=jsonObject.getJSONObject("result");
			JSONObject today=result.getJSONObject("today");
			
			
			
			
			String cityName = today.getString("city");
			Log.d("cityName", cityName);
			String publishTime = today.getString("date_y");
			Log.d("publishTime", publishTime);
			String temp = (String) today.getString("temperature");
			
			String weatherDesp = today.getString("weather");
			Log.d("weatherDesp", weatherDesp);
			saveWeatherInfo(context, cityName, temp, weatherDesp, publishTime);

			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
}


public static void saveWeatherInfo(Context context, String cityName,
		  String temp, String weatherDesp,
		String publishTime) {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyƒÍM‘¬d»’", Locale.CHINA);
	SharedPreferences.Editor editor = PreferenceManager
			.getDefaultSharedPreferences(context).edit();
	editor.putBoolean("city_selected", true);
	editor.putString("city_name", cityName);
	editor.putString("temp", temp);
	editor.putString("weather_desp", weatherDesp);
	editor.putString("publish_time", publishTime);
	editor.putString("current_date", sdf.format(new Date()));
	editor.commit();
}

}