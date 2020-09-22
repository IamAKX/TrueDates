package com.neosao.truedates.configs;

import android.util.Log;

import com.google.gson.Gson;
import com.neosao.truedates.model.options.Caste;
import com.neosao.truedates.model.options.FieldOfStudy;
import com.neosao.truedates.model.options.Interest;
import com.neosao.truedates.model.options.MotherTongue;
import com.neosao.truedates.model.options.Religion;
import com.neosao.truedates.model.options.WorkIndustry;
import com.neosao.truedates.model.options.ZodiacSigns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResponseParser {
    public static void parseGetAllOption(JSONObject response){

        try {
            JSONObject result = response.getJSONObject("result");
            if(result.has("mothertoungeList"))
            {
                JSONArray array = result.getJSONArray("mothertoungeList");
                DynamicOptionConstants.MOTHER_TONGUE_OPTION = new String[array.length()];
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    MotherTongue model = new Gson().fromJson(item.toString(), MotherTongue.class);
                    DynamicOptionConstants.MOTHER_TONGUE_OPTION[i] = model.getMothertoungeName();
                    DynamicOptionConstants.MOTHER_TONGUE_ARRAY_LIST.add(model);
                }
            }
            else
                DynamicOptionConstants.MOTHER_TONGUE_OPTION = new String[]{"No data available"};

            if(result.has("fieldList"))
            {
                JSONArray array = result.getJSONArray("fieldList");
                DynamicOptionConstants.FIELD_OF_STUDY_OPTION = new String[array.length()];
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    FieldOfStudy model = new Gson().fromJson(item.toString(), FieldOfStudy.class);
                    DynamicOptionConstants.FIELD_OF_STUDY_OPTION[i] = model.getFieldName();
                    DynamicOptionConstants.FIELD_OF_STUDY_ARRAY_LIST.add(model);
                }
            }
            else
                DynamicOptionConstants.MOTHER_TONGUE_OPTION = new String[]{"No data available"};


            if(result.has("interestList"))
            {
                JSONArray array = result.getJSONArray("interestList");
                DynamicOptionConstants.INTEREST_OPTION = new String[array.length()];
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    Interest model = new Gson().fromJson(item.toString(), Interest.class);
                    DynamicOptionConstants.INTEREST_OPTION[i] = model.getInterestName();
                    DynamicOptionConstants.INTEREST_ARRAY_LIST.add(model);
                }
            }
            else
                DynamicOptionConstants.INTEREST_OPTION = new String[]{"No data available"};


            if(result.has("casteList"))
            {
                JSONArray array = result.getJSONArray("casteList");
                DynamicOptionConstants.CASTE_OPTION = new String[array.length()];
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    Caste model = new Gson().fromJson(item.toString(), Caste.class);
                    DynamicOptionConstants.CASTE_OPTION[i] = model.getCasteName();
                    DynamicOptionConstants.CASTE_ARRAY_LIST.add(model);
                }
            }
            else
                DynamicOptionConstants.CASTE_OPTION = new String[]{"No data available"};

            if(result.has("religionList"))
            {
                JSONArray array = result.getJSONArray("religionList");
                DynamicOptionConstants.RELIGION_OPTION = new String[array.length()];
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    Religion model = new Gson().fromJson(item.toString(), Religion.class);
                    DynamicOptionConstants.RELIGION_OPTION[i] = model.getReligionName();
                    DynamicOptionConstants.RELIGION_ARRAY_LIST.add(model);
                }
            }
            else
                DynamicOptionConstants.CASTE_OPTION = new String[]{"No data available"};

            if(result.has("workindustryList"))
            {
                JSONArray array = result.getJSONArray("workindustryList");
                DynamicOptionConstants.WORK_INDUSTRY_OPTION = new String[array.length()];
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    WorkIndustry model = new Gson().fromJson(item.toString(), WorkIndustry.class);
                    DynamicOptionConstants.WORK_INDUSTRY_OPTION[i] = model.getIndustryName();
                    DynamicOptionConstants.WORK_INDUSTRY_ARRAY_LIST.add(model);
                }
            }
            else
                DynamicOptionConstants.WORK_INDUSTRY_OPTION = new String[]{"No data available"};

            if(result.has("zodiacsignsList"))
            {
                JSONArray array = result.getJSONArray("zodiacsignsList");
                DynamicOptionConstants.ZODIAC_SIGNS_OPTION = new String[array.length()];
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    ZodiacSigns model = new Gson().fromJson(item.toString(), ZodiacSigns.class);
                    DynamicOptionConstants.ZODIAC_SIGNS_OPTION[i] = model.getSign();
                    DynamicOptionConstants.ZODIAC_SIGNS_ARRAY_LIST.add(model);
                }
            }
            else
                DynamicOptionConstants.ZODIAC_SIGNS_OPTION = new String[]{"No data available"};


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
