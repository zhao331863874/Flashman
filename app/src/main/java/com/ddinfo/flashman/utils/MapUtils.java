package com.ddinfo.flashman.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.DistanceUtil;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.models.SeckillOrderList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dianda on 2016/10/18.
 */
public class MapUtils {
  private BaiduMap mBaiduMap;
  BitmapDescriptor mCurrentMarker;
  private float mRadius;//半径
  double maxLng, minLng, maxLat, minLat;
  private Context context;
  private MapView mapView;
  private List<SeckillOrderList> list;
  private Marker lastMarker;
  private int lastPosition;
  private ClickInterface listener;

  public MapUtils(MapView bmapView, Context context) {
    this.mapView = bmapView;
    mBaiduMap = mapView.getMap();
    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
    //        mBaiduMap.setMyLocationData(new MyLocationData());
    // 开启定位图层
    mBaiduMap.setMyLocationEnabled(true);
    mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
      @Override
      public boolean onMarkerClick(Marker marker) {
        marker.getExtraInfo().getInt("position", 0);
        if (listener != null) {
          listener.clickWhich(marker.getExtraInfo().getInt("position", 0));
        }
        return false;
      }
    });
    this.context = context;
  }

  public void setListener(ClickInterface listener) {
    this.listener = listener;
  }

  /**
   * 添加marker
   */
  public void addMarker(List<SeckillOrderList> list) {
    this.list = list;
    View view = LayoutInflater.from(context).inflate(R.layout.marker_view, null);
    maxLat = list.get(0).getLat();
    minLat = list.get(0).getLat();
    minLng = list.get(0).getLon();
    maxLng = list.get(0).getLon();
    for (int i = 0; i < list.size(); i++) {
      mBaiduMap.addOverlay(getOp(view, list.get(i), i));
      if (list.get(i).getLon() > maxLng) maxLng = list.get(i).getLon();
      if (list.get(i).getLon() < minLng) minLng = list.get(i).getLon();
      if (list.get(i).getLat() > maxLat) maxLat = list.get(i).getLat();
      if (list.get(i).getLat() < minLat) minLat = list.get(i).getLat();
    }
  }

  private OverlayOptions getOp(View view, SeckillOrderList entity, int position) {
    BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(getView(view, entity, false));
    LatLng point = new LatLng(entity.getLat(), entity.getLon());
    //构建MarkerOption，用于在地图上添加Marker
    Bundle b = new Bundle();
    b.putInt("id", entity.getId());
    b.putInt("position", position);
    MarkerOptions option = new MarkerOptions().position(point).icon(bitmap).extraInfo(b);
    return option;
  }

  private View getView(View view, SeckillOrderList entity, boolean isCurrent) {
    if (isCurrent) {
      view.findViewById(R.id.rel_marker).setBackgroundResource(R.mipmap.icon_map_select);
    } else {
      view.findViewById(R.id.rel_marker).setBackgroundResource(R.mipmap.icon_map_unselect);
    }
    ((TextView) view.findViewById(R.id.txt_shop_name)).setText(entity.getStoreName());
    return view;
  }

  public void moveLocation(double lat, double lon) {
    mBaiduMap.setMyLocationConfigeration(
        new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true,
            mCurrentMarker));
    MyLocationData locData = new MyLocationData.Builder().accuracy(0)
        // 此处设置开发者获取到的方向信息，顺时针0-360
        .direction(0).latitude(lat).longitude(lon).build();
    mBaiduMap.setMyLocationData(locData);
    goCenter(lat, lon, true);
  }

  /**
   * 移动
   */
  private void goCenter(double lat, double lon, boolean isGoCenter) {
    LatLng ll = new LatLng(lat, lon);
    MapStatus.Builder builder = new MapStatus.Builder();
    builder.target(ll);
    if (isGoCenter) builder.zoom(getZoom(maxLng, minLng, maxLat, minLat));

    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
  }

  /**
   * 修改当前店铺的背景
   */
  public void moveWithList(double lat, double lon, int position) throws IndexOutOfBoundsException {

    View view;
    LatLng llC = new LatLng(lat, lon);
    LatLngBounds bounds = new LatLngBounds.Builder().include(llC).build();
    List<Marker> markers = mBaiduMap.getMarkersInBounds(bounds);
    for (Marker marker : markers) {
      if (marker.getExtraInfo().getInt("id", 0) == list.get(position).getId()) {
        if (lastMarker != null) {
          view = LayoutInflater.from(context).inflate(R.layout.marker_view, null);
          BitmapDescriptor bitmap =
              BitmapDescriptorFactory.fromView(getView(view, list.get(lastPosition), false));
          lastMarker.setIcon(bitmap);
          marker.setZIndex(5);
        }
          view = LayoutInflater.from(context).inflate(R.layout.marker_view, null);
          BitmapDescriptor bitmap =
              BitmapDescriptorFactory.fromView(getView(view, list.get(position), true));
        marker.setIcon(bitmap);
        marker.setZIndex(10);
        marker.setToTop();
        lastMarker = marker;
        lastPosition = position;
        }
      }
    goCenter(lat, lon, position == 0 ? true : false);
  }

  /**
   * 计算当前的缩放级别
   */
  private float getZoom(double maxLng, double minLng, double maxLat, double minLat) {
    double[] zoom = {
        50, 100, 200, 500, 1000, 2000, 5000, 10000, 20000, 25000, 50000, 100000, 200000, 500000,
        1000000, 2000000
    };//级别18到3。
    LatLng pointA = new LatLng(maxLat, maxLng);  // 创建点坐标A
    LatLng pointB = new LatLng(minLat, minLng);  // 创建点坐标B
    double distance = DistanceUtil.getDistance(pointA, pointB);  //获取两点距离,保留小数点后两位
    for (int i = 0, zoomLen = zoom.length; i < zoomLen; i++) {
      if (zoom[i] - distance > 0) {
        return 18 - i + 3;//之所以会多3，是因为地图范围常常是比例尺距离的10倍以上。所以级别会增加3。
      }
    }
    return 18f;
  }

  public void clearAllMarker() {
    try {
      mapView.getOverlay().clear();
      lastMarker = null;
    } catch (Exception e) {
      e.printStackTrace();
    }
    mBaiduMap.clear();
  }

  public interface ClickInterface {
    void clickWhich(int position);
  }
}
