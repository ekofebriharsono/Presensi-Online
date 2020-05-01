package com.maseko.root.absen1.SharePreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.maseko.root.absen1.SharePreference.UtilPreferences.ADMIN;
import static com.maseko.root.absen1.SharePreference.UtilPreferences.ID_USER;
import static com.maseko.root.absen1.SharePreference.UtilPreferences.ID_USER_LAIN;
import static com.maseko.root.absen1.SharePreference.UtilPreferences.INGAT;
import static com.maseko.root.absen1.SharePreference.UtilPreferences.LAT;
import static com.maseko.root.absen1.SharePreference.UtilPreferences.LOKASI;
import static com.maseko.root.absen1.SharePreference.UtilPreferences.LONG;
import static com.maseko.root.absen1.SharePreference.UtilPreferences.NAMA;
import static com.maseko.root.absen1.SharePreference.UtilPreferences.NAMA_LAIN;
import static com.maseko.root.absen1.SharePreference.UtilPreferences.PRESENSI;
import static com.maseko.root.absen1.SharePreference.UtilPreferences.PRESENSI_LAIN;
import static com.maseko.root.absen1.SharePreference.UtilPreferences.SAVE_NIP;
import static com.maseko.root.absen1.SharePreference.UtilPreferences.SAVE_NIP_LAIN;
import static com.maseko.root.absen1.SharePreference.UtilPreferences.SAVE_PASS;

public class SaveSharedPreference {


    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Set the Login Status
     * @param context
     * @param id
     */

    public static void setIdUser(Context context, String id) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(ID_USER, id);
        editor.apply();
    }

    public static void setIdUserLain(Context context, String id) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(ID_USER_LAIN, id);
        editor.apply();
    }

    public static void setAdmin(Context context, String admin) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(ADMIN, admin);
        editor.apply();
    }

    public static void setLongUser(Context context, String longitude) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(LONG, longitude);
        editor.apply();
    }

    public static void setLatUser(Context context, String latitude) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(LAT, latitude);
        editor.apply();
    }

    public static void setNipUser(Context context, String nip) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(SAVE_NIP, nip);
        editor.apply();
    }

    public static void setNipUserLain(Context context, String nip) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(SAVE_NIP_LAIN, nip);
        editor.apply();
    }

    public static void setLokasiUser(Context context, String nip) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(LOKASI, nip);
        editor.apply();
    }

    public static void setPassUser(Context context, String pass) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(SAVE_PASS, pass);
        editor.apply();
    }

    public static void setNamaUser(Context context, String nama) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(NAMA, nama);
        editor.apply();
    }

    public static void setNamaUserLain(Context context, String nama) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(NAMA_LAIN, nama);
        editor.apply();
    }

    public static void setPresensiUser(Context context, String presensi) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(PRESENSI, presensi);
        editor.apply();
    }

    public static void setPresensiUserLain(Context context, String presensi) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(PRESENSI_LAIN, presensi);
        editor.apply();
    }

    public static void setIngat(Context context, boolean ingat) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(INGAT, ingat);
        editor.apply();
    }

    /**
     * Get the Login Status
     * @param context
     * @return boolean: login status
     */

    public static String getNipUser(Context context) {
        return getPreferences(context).getString(SAVE_NIP, null);
    }

    public static String getNipUserLain(Context context) {
        return getPreferences(context).getString(SAVE_NIP_LAIN, null);
    }

    public static String getLongUser(Context context) {
        return getPreferences(context).getString(LONG, "0");
    }

    public static String getLatUser(Context context) {
        return getPreferences(context).getString(LAT, "0");
    }

    public static String getLokasiUser(Context context) {
        return getPreferences(context).getString(LOKASI, null);
    }

    public static String getIdUser(Context context) {
        return getPreferences(context).getString(ID_USER, null);
    }

    public static String getIdUserLain(Context context) {
        return getPreferences(context).getString(ID_USER_LAIN, null);
    }

    public static String getPassUser(Context context) {
        return getPreferences(context).getString(SAVE_PASS, null);
    }

    public static String getNamaUser(Context context) {
        return getPreferences(context).getString(NAMA, null);
    }

    public static String getNamaUserLain(Context context) {
        return getPreferences(context).getString(NAMA_LAIN, null);
    }

    public static String getPresensiUser(Context context) {
        return getPreferences(context).getString(PRESENSI, "0");
    }

    public static String getPresensiUserLain(Context context) {
        return getPreferences(context).getString(PRESENSI_LAIN, "0");
    }

    public static boolean getIngat(Context context) {
        return getPreferences(context).getBoolean(INGAT, false);
    }

    public static String getAdmin(Context context) {
        return getPreferences(context).getString(ADMIN, "0");
    }

}
