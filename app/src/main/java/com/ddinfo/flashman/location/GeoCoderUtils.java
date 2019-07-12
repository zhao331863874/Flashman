package com.ddinfo.flashman.location;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.util.List;

/**
 * Created by dianda on 2016/10/21.
 */
public class GeoCoderUtils implements OnGetGeoCoderResultListener {
  GeoCoder mSearch = null; // 搜索模块
  private LatLng ptCenter;
  private GeoCoderResult listener;

  public GeoCoderUtils(LatLng ptCenter) {
    this.ptCenter = ptCenter;
    mSearch = GeoCoder.newInstance();
    mSearch.setOnGetGeoCodeResultListener(this);
  }

  public void setGeoCoder(GeoCoderResult listener) {
    this.listener = listener;
    mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
  }

  interface GeoCoderResult {
    void result(List<PoiInfo> info);
  }

  @Override
  public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

  }

  @Override
  public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
    if (listener != null) {
      if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
        listener.result(null);
      } else {
        listener.result(result.getPoiList());
      }
    }
    mSearch.destroy();
  }
}
