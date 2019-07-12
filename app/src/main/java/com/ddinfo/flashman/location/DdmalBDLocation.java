package com.ddinfo.flashman.location;

import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.baidu.mapapi.search.core.PoiInfo;

import java.util.List;

/**
 * Created by dianda on 2016/10/12.
 */
public class DdmalBDLocation {
  private String time;
  private int mLocType;//定位类型
  private double mLatitude;//维度
  private double mLongitude;//经度
  private boolean mHasAltitude;//
  private double mAltitude;//海拔高度
  private boolean mHasSpeed;
  private float mSpeed;//速度
  private boolean mHasRadius;
  private float mRadius;//半径
  private boolean mHasSateNumber;
  private int mSatelliteNumber;//卫星数
  private float mDerect;
  private String mCoorType;
  private boolean mHasAddr;
  private String mAddrStr;//地址信息
  private List<Poi> mPoiList;
  private String mDescription;
  private int mGpsAccuracyStatus;//gps 质量
  public String country;//国家名字
  public String countryCode;//国家编号
  public String province;//省
  public String city;//城市
  public String cityCode;//城市code
  public String district;//区
  public String street;//街道
  public String streetNumber;//
  public String address;//地址
  private int operationers;//运营商
  private List<PoiInfo> poiInfo;

  public List<PoiInfo> getPoiInfo() {
    return poiInfo;
  }

  public void setPoiInfo(List<PoiInfo> poiInfo) {
    this.poiInfo = poiInfo;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public int getOperationers() {
    return operationers;
  }

  public void setOperationers(int operationers) {
    this.operationers = operationers;
  }

  public String getmLocType() {
    if (mLocType == BDLocation.TypeGpsLocation) {
      return "gps";
    } else if (mLocType == BDLocation.TypeNetWorkLocation) {
      return "网络" + getOperationers();
    } else if (mLocType == BDLocation.TypeOffLineLocation) {
      return "离线定位成功，离线定位结果也是有效的";
    }
    return "";
  }

  public void setmLocType(int mLocType) {
    this.mLocType = mLocType;
  }

  public double getmLatitude() {
    return mLatitude;
  }

  public void setmLatitude(double mLatitude) {
    this.mLatitude = mLatitude;
  }

  public double getmLongitude() {
    return mLongitude;
  }

  public void setmLongitude(double mLongitude) {
    this.mLongitude = mLongitude;
  }

  public boolean ismHasAltitude() {
    return mHasAltitude;
  }

  public void setmHasAltitude(boolean mHasAltitude) {
    this.mHasAltitude = mHasAltitude;
  }

  public double getmAltitude() {
    return mAltitude;
  }

  public void setmAltitude(double mAltitude) {
    this.mAltitude = mAltitude;
  }

  public boolean ismHasSpeed() {
    return mHasSpeed;
  }

  public void setmHasSpeed(boolean mHasSpeed) {
    this.mHasSpeed = mHasSpeed;
  }

  public float getmSpeed() {
    return mSpeed;
  }

  public void setmSpeed(float mSpeed) {
    this.mSpeed = mSpeed;
  }

  public boolean ismHasRadius() {
    return mHasRadius;
  }

  public void setmHasRadius(boolean mHasRadius) {
    this.mHasRadius = mHasRadius;
  }

  public float getmRadius() {
    return mRadius;
  }

  public void setmRadius(float mRadius) {
    this.mRadius = mRadius;
  }

  public boolean ismHasSateNumber() {
    return mHasSateNumber;
  }

  public void setmHasSateNumber(boolean mHasSateNumber) {
    this.mHasSateNumber = mHasSateNumber;
  }

  public int getmSatelliteNumber() {
    return mSatelliteNumber;
  }

  public void setmSatelliteNumber(int mSatelliteNumber) {
    this.mSatelliteNumber = mSatelliteNumber;
  }

  public float getmDerect() {
    return mDerect;
  }

  public void setmDerect(float mDerect) {
    this.mDerect = mDerect;
  }

  public String getmCoorType() {
    return mCoorType;
  }

  public void setmCoorType(String mCoorType) {
    this.mCoorType = mCoorType;
  }

  public boolean ismHasAddr() {
    return mHasAddr;
  }

  public void setmHasAddr(boolean mHasAddr) {
    this.mHasAddr = mHasAddr;
  }

  public String getmAddrStr() {
    return mAddrStr;
  }

  public void setmAddrStr(String mAddrStr) {
    this.mAddrStr = mAddrStr;
  }

  public List<Poi> getmPoiList() {
    return mPoiList;
  }

  public void setmPoiList(List<Poi> mPoiList) {
    this.mPoiList = mPoiList;
  }

  public String getmDescription() {
    return mDescription;
  }

  public void setmDescription(String mDescription) {
    this.mDescription = mDescription;
  }

  public int getmGpsAccuracyStatus() {
    return mGpsAccuracyStatus;
  }

  public void setmGpsAccuracyStatus(int mGpsAccuracyStatus) {
    this.mGpsAccuracyStatus = mGpsAccuracyStatus;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCityCode() {
    return cityCode;
  }

  public void setCityCode(String cityCode) {
    this.cityCode = cityCode;
  }

  public String getDistrict() {
    return district;
  }

  public void setDistrict(String district) {
    this.district = district;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getStreetNumber() {
    return streetNumber;
  }

  public void setStreetNumber(String streetNumber) {
    this.streetNumber = streetNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
