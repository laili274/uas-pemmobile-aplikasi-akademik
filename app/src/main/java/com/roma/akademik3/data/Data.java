package com.roma.akademik3.data;


//TODO 3 Buat kelas model Mahasiswa

import java.sql.Struct;

public class Data {
    private String nim, nama, alamat, NIK, NIDN, Nama_Dosen, Alamat_Dosen, KMK, NMK, SKS;

    public Data() {

    }

    public Data(String nim, String nama, String alamat, String NIK, String NIDN, String Nama_Dosen, String Alamat_Dosen, String KMK, String NMK, String SKS) {
        this.nim = nim;
        this.nama = nama;
        this.alamat = alamat;
        this.NIK = NIK;
        this.NIDN = NIDN;
        this.Nama_Dosen = Nama_Dosen;
        this.Alamat_Dosen = Alamat_Dosen;
        this.KMK = KMK;
        this.NMK = NMK;
        this.SKS = SKS;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNIK() {
        return NIK;
    }

    public void setNIK(String NIK) {
        this.NIK = NIK;
    }

    public String getNIDN() {
        return NIDN;
    }

    public void setNIDN(String NIDN) {
        this.NIDN = NIDN;
    }

    public String getNama_Dosen() {
        return Nama_Dosen;
    }

    public void setNama_Dosen(String Nama_Dosen) {
        this.Nama_Dosen = Nama_Dosen;
    }

    public String getAlamat_Dosen() {
        return Alamat_Dosen;
    }

    public void setAlamat_Dosen(String Alamat_Dosen) {
        this.Alamat_Dosen = Alamat_Dosen;
    }

    public String getKMK() {
        return KMK;
    }

    public void setKMK(String KMK) {
        this.KMK = KMK;
    }

    public String getNMK() {
        return NMK;
    }

    public void setNMK(String NMK) {
        this.NMK = NMK;
    }

    public String getSKS() {
        return SKS;
    }

    public void setSKS(String SKS) {
        this.SKS = SKS;
    }
}

