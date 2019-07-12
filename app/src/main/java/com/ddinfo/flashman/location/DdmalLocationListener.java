package com.ddinfo.flashman.location;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.ddinfo.flashman.application.MyApplication;
import com.ddinfo.flashman.constant.ExampleConfig;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by dianda on 2016/10/12.
 */
public class DdmalLocationListener implements BDLocationListener {
  private GeoCoderUtils geoCoderUtils;
  private BDLocation bLocation;

  @Override
  public void onReceiveLocation(final BDLocation bdLocation) {
    if (bdLocation != null) {
      this.bLocation = bdLocation;
      //            StringBuffer stringBuffer = new StringBuffer(256);
      //            stringBuffer.append("\n经度");
      //            stringBuffer.append(bdLocation.getLatitude());
      //            stringBuffer.append("\n维度");
      //            stringBuffer.append(bdLocation.getLongitude());
      //            stringBuffer.append("\n国家");
      //            stringBuffer.append(bdLocation.getCountry());
      //            stringBuffer.append("\n城市");
      //            stringBuffer.append(bdLocation.getCity());
      //            stringBuffer.append("\n区");
      //            stringBuffer.append(bdLocation.getDistrict());
      //            stringBuffer.append("\n街道");
      //            stringBuffer.append(bdLocation.getStreet());
      //            stringBuffer.append("\n地址");
      //            stringBuffer.append(bdLocation.getAddress());
      //            stringBuffer.append("\n定位类型");
      //            stringBuffer.append(bdLocation.getLocType());
      //            System.out.println(("--sss---" + stringBuffer.toString()));
      geoCoderUtils =
          new GeoCoderUtils(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()));
      geoCoderUtils.setGeoCoder(new GeoCoderUtils.GeoCoderResult() {
        @Override
        public void result(List<PoiInfo> info) {
          //                    DdmalBDLocation location = initDdmalDBLocationeEntity(bdLocation);
          //                    EventBus.getDefault().post(location);
          initDdmalDBLocationeEntity(info);
        }
      });
    }
  }

  private void initDdmalDBLocationeEntity(List<PoiInfo> poiInfoList) {
    DdmalBDLocation ddmalBDLocationEntity = new DdmalBDLocation();
    ddmalBDLocationEntity.setAddress(bLocation.getAddrStr());
    ddmalBDLocationEntity.setCity(bLocation.getCity());
    ddmalBDLocationEntity.setCityCode(bLocation.getCityCode());
    ddmalBDLocationEntity.setCountry(bLocation.getCountry());
    ddmalBDLocationEntity.setCountryCode(bLocation.getCountryCode());
    ddmalBDLocationEntity.setDistrict(bLocation.getDistrict());
    ddmalBDLocationEntity.setmAltitude(bLocation.getAltitude());
    ddmalBDLocationEntity.setmLatitude(bLocation.getLatitude());
    ddmalBDLocationEntity.setmLongitude(bLocation.getLongitude());
    ddmalBDLocationEntity.setmLocType(bLocation.getLocType());
    ddmalBDLocationEntity.setTime(bLocation.getTime());
    ddmalBDLocationEntity.setOperationers(bLocation.getOperators());
    if (bLocation.getPoiList() != null && !bLocation.getPoiList().isEmpty()) {
      ddmalBDLocationEntity.setmPoiList(bLocation.getPoiList());
    }
    if (poiInfoList != null && poiInfoList.size() > 0) {
      ddmalBDLocationEntity.setPoiInfo(poiInfoList);
    }
    MyApplication.getMyApplication()
        .getSPUtilsInstance()
        .putString(ExampleConfig.LAST_ADDRESS, bLocation.getAddrStr());
    EventBus.getDefault().post(ddmalBDLocationEntity);
  }
}
