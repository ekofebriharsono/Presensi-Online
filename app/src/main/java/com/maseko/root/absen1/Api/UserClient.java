package com.maseko.root.absen1.Api;

import com.maseko.root.absen1.Notification.MyResponse;
import com.maseko.root.absen1.Notification.Sender;
import com.maseko.root.absen1.Respone.CekPresensiHariIni;
import com.maseko.root.absen1.Respone.DataPresensi;
import com.maseko.root.absen1.Respone.DataPrizinanLuarKota;
import com.maseko.root.absen1.Respone.ListAdmin;
import com.maseko.root.absen1.Respone.ListKaryawan;
import com.maseko.root.absen1.Respone.Login;
import com.maseko.root.absen1.Respone.NotifAdmin;
import com.maseko.root.absen1.Respone.NotifPegawai;
import com.maseko.root.absen1.Respone.Presensi;
import com.maseko.root.absen1.Respone.ServerResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface UserClient {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAWVVa2Ls:APA91bF_qj-DKb3eYbA2JC_kpOrWV_ytxyWI6Qid2SCi7r2voKcjKTRbxkoC5hOvKd6o4mNP0Z9L0uG-dftDCbejU2ECyWOjLtyo8rcHi9C-XPMZu5JHjLWgZFfwKwqULMn30NpEmH9j"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

    @FormUrlEncoded
    @POST("login.php")
    Call<Login> login(@Field("nip") String nip,
                      @Field("password") String password);

    @FormUrlEncoded
    @POST("cek_presensi_hari_ini.php")
    Call<CekPresensiHariIni> cekPresensiHariIni(@Field("id_user") String id_user,
                                                @Field("tanggal") String tanggal);

    @FormUrlEncoded
    @POST("request_device.php")
    Call<Presensi> requestDevice(@Field("id_user") String id_user,
                                 @Field("id_device") String id_device);

    @FormUrlEncoded
    @POST("cek_versi.php")
    Call<Presensi> cekVersi(@Field("versi") String versi);

    @FormUrlEncoded
    @POST("login_pegawai_lain.php")
    Call<Login> presensiPegawai(@Field("nip") String nip);

    @FormUrlEncoded
    @POST("list_notif_for_pegawai.php")
    Call<NotifPegawai> notifPegawai(@Field("nip") String nip);

    @FormUrlEncoded
    @POST("acc_admin.php")
    Call<Presensi> accAdmin(@Field("id_presensi") String id_presensi,
                            @Field("acc") String acc,
                            @Field("hadir_pulang") String hadir_pulang,
                            @Field("nip") String nip,
                            @Field("waktu") String waktu,
                            @Field("tanggal") String tanggal,
                            @Field("presensi_tanggal") String presensi_tanggal,
                            @Field("pesan") String pesan);

    @FormUrlEncoded
    @POST("request_new_password.php")
    Call<Presensi> requestPassword(@Field("id_user") String id_user,
                                   @Field("password") String password,
                                   @Field("id_device") String id_device,
                                   @Field("token") String token);

    @FormUrlEncoded
    @POST("hitung_long_lat.php")
    Call<Presensi> cekLokasiBarcode(@Field("id_barcode") String id_barcode,
                                    @Field("lat") String lat,
                                    @Field("longitude") String longitude);

    @GET("list_notif_for_admin.php")
    Call<NotifAdmin> getListNotifAdmin();

    @GET("list_admin.php")
    Call<ListAdmin> getListTokenAdmin();

    @FormUrlEncoded
    @POST("input_presensi.php")
    Call<Presensi> inputPresensi(@Field("id_user") String id_user,
                                 @Field("tanggal") String tanggal,
                                 @Field("waktu") String waktu,
                                 @Field("lokasi") String lokasi,
                                 @Field("barcode") String barcode,
                                 @Field("link_foto") String link_foto,
                                 @Field("shift") String shift,
                                 @Field("hadir") String hadir,
                                 @Field("pulang") String pulang,
                                 @Field("imei") String imei,
                                 @Field("hadir_luar_kota") String hadir_luar_kota,
                                 @Field("pulang_luar_kota") String pulang_luar_kota);

    @Multipart
    @POST("upload_image.php")
    Call<ServerResponse> upload(@PartMap Map<String, RequestBody> map);

    @FormUrlEncoded
    @POST("input_presensi.php")
    Call<Presensi> updatePresensi(@Field("id_user") String id_user,
                                  @Field("tanggal") String tanggal,
                                  @Field("waktu") String waktu,
                                  @Field("lokasi") String lokasi,
                                  @Field("barcode") String barcode,
                                  @Field("link_foto") String link_foto,
                                  @Field("shift") String shift,
                                  @Field("hadir") String hadir,
                                  @Field("pulang") String pulang,
                                  @Field("imei") String imei);

    @FormUrlEncoded
    @POST("data_presensi.php")
    Call<DataPresensi> filterDataPresensi(@Field("id_user") String id_user,
                                          @Field("tanggal_awal") String tanggal_awal,
                                          @Field("tanggal_akhir") String tanggal_akhir);

    @GET("list_karyawan.php")
    Call<ListKaryawan> getListKaryawan();

    @FormUrlEncoded
    @POST("data_perizinan_luar_kota.php")
    Call<DataPrizinanLuarKota> filterDataPresensiLuarKota(@Field("tanggal_awal") String tanggal_awal,
                                                          @Field("tanggal_akhir") String tanggal_akhir);
}
