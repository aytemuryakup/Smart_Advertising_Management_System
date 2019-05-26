package com.example.yazlab23dnm;

public class Firma_Bilgileri {

    public int FirmaID;
    public String FirmaAdi;
    public String KampanyaIcerik;
    public String KampanyaZaman;
    public String Latitude_en;
    public String Longitude_boy;
    public String Turu;

    public Firma_Bilgileri() {

    }

    public Firma_Bilgileri(String firmaAdi,int firmaId,  String kampanyaIcerik, String kampanyaSure, String firmaLatitude, String firmaLongitude, String firmaTip) {

        this.FirmaID = firmaId;
        this.FirmaAdi = firmaAdi;
        this.KampanyaIcerik = kampanyaIcerik;
        this.KampanyaZaman = kampanyaSure;
        this.Latitude_en = firmaLatitude;
        this.Longitude_boy = firmaLongitude;
        this.Turu = firmaTip;
    }
}
