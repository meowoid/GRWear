package uk.co.alexbate.grwear;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuGridPagerAdapter extends FragmentGridPagerAdapter {
    private JSONObject json;

    public MenuGridPagerAdapter(String rd, FragmentManager fm) {
        super(fm);
        try {
            json = new JSONObject(rd);
            JSONArray jsonArray = json.getJSONArray("menus");
            json = jsonArray.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Fragment getFragment(int i, int i2) {
        if (i2==0) {
            String meal = "";
            switch(i) {
                case 0: meal="Lunch";
                        break;
                case 1: meal="Dinner";
                        break;
            }
            try {
                return CardFragment.create(meal, json.getString("date"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            JSONArray meals = null;
            String description = "";
            JSONObject thisMeal = null;
            try {
                if (i == 0) {
                    meals = json.getJSONArray("lunch");
                } else {
                    meals = json.getJSONArray("dinner");
                }
                thisMeal = meals.getJSONObject(i2 - 1);
                description = "Item " + Integer.toString(i2);
                description += " - £" + thisMeal.getJSONArray("price").getString(0);
                description += "/£" + thisMeal.getJSONArray("price").getString(1);

            } catch (JSONException e) {
                //Conference period, not dinner
                description = "Item " + Integer.toString(i2);
                if (i!=1) {
                    description += " - £5.50/£7.50";
                } else {
                    description += " - £3.75";

                }
            }

            try {
                return CardFragment.create(description, thisMeal.getString("item"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    public int getRowCount() {
        return json.length() -1;
    }

    @Override
    public int getColumnCount(int i) {
        int r;
        try {
            switch (i) {
                case (0):
                    r = json.getJSONArray("lunch").length();
                    break;
                case (1):
                    r = json.getJSONArray("dinner").length();
                    break;
                default:
                    r = 0;
            }
            r++; //Add title card
            return r;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
