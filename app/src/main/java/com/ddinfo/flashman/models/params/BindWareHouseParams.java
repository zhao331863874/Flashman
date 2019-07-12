package com.ddinfo.flashman.models.params;

/**
 * Title: 请输入标题
 * Created by fuh on 2017/2/8.
 * Email：unableApe@gmail.com
 */

public class BindWareHouseParams {
    private String warehouseId;

    public BindWareHouseParams(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }
}
