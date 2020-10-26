package com.chance.mimorobot.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chance.mimorobot.R;
import com.chance.mimorobot.constant.Globle;
import com.chance.mimorobot.manager.SlamManager;
import com.chance.mimorobot.utils.SystemUtils;
import com.chance.mimorobot.widget.SeekBarPreference;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import cn.chuangze.robot.aiuilibrary.AIUIWrapper;
import cn.chuangze.robot.aiuilibrary.params.SpeechParams;

import static com.chance.mimorobot.constant.Globle.AUTO_CHARGE_OFFSET;

public class SettingActivity extends AppCompatPreferenceActivity {
    static Toast toast;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    private void setupActionBar() {
        ViewGroup root = (ViewGroup) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.activity_setting_toolbar, root, false);
        toolbar.setTitle(R.string.title_activity_settings);
        root.addView(toolbar, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    protected static void toast(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return true;
    }

    public static class BasePreferenceFragment extends PreferenceFragment {

        SharedPreferences sharedPreferences;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        }
    }



    /**
     * 基本信息
     */
    public static class BaseInfoPreferenceFragment extends PreferenceFragment {

        EditTextPreference namePreference;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_info);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            init();
        }

        private void init() {
            namePreference = (EditTextPreference) findPreference(getString(R.string.pref_key_robot_name));
            namePreference.setSummary(Globle.ROBOT_NAME);
            namePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (!TextUtils.isEmpty(newValue.toString())) {
                        Globle.ROBOT_NAME = newValue.toString();
                        namePreference.setSummary(newValue.toString());
                        return true;
                    }
                    return false;
                }
            });
            findPreference(getString(R.string.pref_key_robot_id)).setSummary(Globle.robotId);
            findPreference(getString(R.string.pref_key_robot_qrcode)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
//                    Intent intent = new Intent(getActivity(), DownLoadAndBindActivity.class);
//                    startActivity(intent);
                    return true;
                }
            });
        }
    }


    /**
     * 语言选择
     */
    public static class LanguagePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        SharedPreferences sharedPreferences;

        String[] languages = null;
        String[] cnSpeakers, cnSpeakerParams = null;

        ListPreference languageListPreference, speakerPreference;

        EditTextPreference speedPreference, pitchPreference, asrLengthPreference;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_language);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            init();
        }

        private void init() {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

//            languages = getResources().getStringArray(R.array.support_language);
            languages = getResources().getStringArray(R.array.support_language);
            cnSpeakers = getResources().getStringArray(R.array.mandarin_speaker);
            cnSpeakerParams = getResources().getStringArray(R.array.mandarin_speaker_params);

            speakerPreference = (ListPreference) findPreference(getString(R.string.pref_key_speaker));
            speakerPreference.setOnPreferenceChangeListener(this);

            String cnspeaker = sharedPreferences.getString(getString(R.string.pref_key_speaker), getString(R.string.default_speaker_params));
            initCnSpeakerParam(cnspeaker);

            // 合成速度
            speedPreference = (EditTextPreference) findPreference(getString(R.string.pref_key_tts_speed));
            String speed = sharedPreferences.getString(getString(R.string.pref_key_tts_speed), "50");
            speedPreference.setSummary(speed);
            speedPreference.setOnPreferenceChangeListener(this);

            // 合成音调
            pitchPreference = (EditTextPreference) findPreference(getString(R.string.pref_key_tts_pitch));
            String pitch = sharedPreferences.getString(getString(R.string.pref_key_tts_pitch), "50");
            pitchPreference.setSummary(pitch);
            pitchPreference.setOnPreferenceChangeListener(this);



        }

        /**
         * 初始化中文发言人的序列号
         *
         * @param cnspeaker
         */
        private void initCnSpeakerParam(String cnspeaker) {

            for (int i = 0; i < cnSpeakerParams.length; i++) {
                if (cnspeaker.equals(cnSpeakerParams[i])) {
                    speakerPreference.setValueIndex(i);
                    speakerPreference.setSummary(cnSpeakers[i]);
                    setSpeakerParams();
                    break;
                }
            }
        }

        /**
         * 设置发言人参数
         */
        private void setSpeakerParams() {
            SpeechParams speechParams = new SpeechParams();
            String speaker = sharedPreferences.getString(getString(R.string.pref_key_speaker), getString(R.string.default_speaker_params));
            int speed = Integer.parseInt(sharedPreferences.getString(getString(R.string.pref_key_tts_speed), "50"));
            int pitch = Integer.parseInt(sharedPreferences.getString(getString(R.string.pref_key_tts_pitch), "50"));
            int volume = 100;
            speechParams.init(speaker, speed, pitch, volume);
            AIUIWrapper.getInstance(getActivity().getApplicationContext()).setSpeechParams(speechParams);
        }
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference.getKey().equals(getString(R.string.pref_key_language))) {
                int index = Integer.parseInt(newValue.toString());
                preference.setSummary(languages[index]);
            } else if (preference == speakerPreference) {
                initCnSpeakerParam(newValue.toString());
            } else if (preference == speedPreference) {
                return setValue(getActivity(), speedPreference, newValue.toString());
            } else if (preference == pitchPreference) {
                return setValue(getActivity(), pitchPreference, newValue.toString());
            } else if (preference == asrLengthPreference) {
                preference.setSummary(newValue.toString());
            }
            return true;
        }
    }

    /**
     * 配置网络
     */
    public static class NetworkPreferenceFragment extends BasePreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_network);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            findPreference(getString(R.string.pref_key_config_network)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
//                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
//                    startActivity(intent);
                    startActivity(new Intent(getActivity(), WifiActivity.class));
                    return true;
                }
            });
        }
    }


    /**
     * 设置首选项的值
     *
     * @param context
     * @param preference
     * @param newValue
     * @return
     */
    private static boolean setValue(Context context, Preference preference, String newValue) {
        if (Integer.parseInt(newValue) > 100) {
            toast(context, "对不起，您输入的数值有误");
            return false;
        } else {
            preference.setSummary(newValue);
            return true;
        }
    }


    /**
     * 唤醒方式
     */
    public static class WakePreferenceFragment extends BasePreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_wake);

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            return true;
        }
    }

    /**
     * 导航相关
     */
    public static class NavPreferenceFragment extends BasePreferenceFragment implements Preference.OnPreferenceChangeListener {


        ListPreference speedPreference, patrolTimePreference;
        EditTextPreference navWarnPreference;
        Preference mapInitPreference;
        String[] levels = null;
        String[] patrolTimes, patrolTimeValues = null;

        SwitchPreference autoChargePreference, autoWorkPreference, autoPatrolPreference;
        SeekBarPreference autoChargeValuePreference, autoWorkValuePreference;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_nav);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            speedPreference = (ListPreference) findPreference(getString(R.string.pref_key_nav_speed));
            speedPreference.setOnPreferenceChangeListener(this);



            autoChargePreference = (SwitchPreference) findPreference(getString(R.string.pref_key_auto_charge));
            autoChargePreference.setOnPreferenceChangeListener(this);
            autoChargeValuePreference = (SeekBarPreference) findPreference(getString(R.string.pref_key_auto_charge_value));
            autoChargeValuePreference.setShowOffset(AUTO_CHARGE_OFFSET);
            autoChargeValuePreference.setOnPreferenceChangeListener(this);


            levels = getResources().getStringArray(R.array.levels);
            init();
        }

        private void init() {
            speedPreference.setSummary(speedPreference.getEntry());
            boolean isAutoCharge = sharedPreferences.getBoolean(getString(R.string.pref_key_auto_charge), false);
            parseAutoCharge(isAutoCharge);

            int autoChargeValue = sharedPreferences.getInt(getString(R.string.pref_key_auto_charge_value), 10);
            autoChargeValuePreference.setSummary(autoChargeValue + AUTO_CHARGE_OFFSET + "");

        }


        /**
         * 自动充电处理
         *
         * @param isAutoCharge
         */
        private void parseAutoCharge(boolean isAutoCharge) {
            if (isAutoCharge) {
                autoChargeValuePreference.setEnabled(true);
            } else {
                autoChargeValuePreference.setEnabled(false);
            }
        }



        @Override
        public boolean onPreferenceChange(Preference preference, final Object newValue) {
            if (preference == speedPreference) {
                // 设置导航的速度
                switch (newValue.toString()){
                    case "0":
                        SlamManager.getInstance().setSpeed(0);
                        break;
                    case "1":
                        SlamManager.getInstance().setSpeed(5);
                        break;
                    case "2":
                        SlamManager.getInstance().setSpeed(10);
                        break;
                }
                int index = speedPreference.findIndexOfValue((String) newValue);
                //把listPreference中的摘要显示为当前ListPreference的实体内容中选择的那个项目
                speedPreference.setSummary(levels[index]);
            } else if (preference == navWarnPreference) {
                navWarnPreference.setSummary(newValue.toString());
            } else if (preference == autoChargePreference) {
                boolean isAutoCharge = Boolean.parseBoolean(newValue.toString());
                parseAutoCharge(isAutoCharge);
            }  else if (preference == autoChargeValuePreference) {
                int value = Integer.parseInt(newValue.toString()) + AUTO_CHARGE_OFFSET;
                autoChargeValuePreference.setSummary(value + "");
            }
            return true;
        }
    }


    /**
     * 关于
     */
    public static class AboutPreferenceFragment extends BasePreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_about);
            String version = SystemUtils.getAppVersionName(getActivity());
            findPreference(getString(R.string.pref_key_version)).setSummary(version);
        }
    }

}
