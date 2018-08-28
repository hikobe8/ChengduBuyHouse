package com.ray.lib.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Author : hikobe8@github.com
 * Time : 2018/7/31 下午10:27
 * Description :
 */
public class QueryResultBean implements Parcelable {

    private String district;
    private String projectName;
    private String sellNo;
    private String range;
    private String houseCount;
    private String phone;
    private String startTime;
    private String endTime;
    private String selectEndTime = "暂无";
    private String status;
    private boolean canSelect;

    public QueryResultBean() {
    }

    protected QueryResultBean(Parcel in) {
        district = in.readString();
        projectName = in.readString();
        sellNo = in.readString();
        range = in.readString();
        houseCount = in.readString();
        phone = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        selectEndTime = in.readString();
        status = in.readString();
        canSelect = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(district);
        dest.writeString(projectName);
        dest.writeString(sellNo);
        dest.writeString(range);
        dest.writeString(houseCount);
        dest.writeString(phone);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(selectEndTime);
        dest.writeString(status);
        dest.writeByte((byte) (canSelect ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QueryResultBean> CREATOR = new Creator<QueryResultBean>() {
        @Override
        public QueryResultBean createFromParcel(Parcel in) {
            return new QueryResultBean(in);
        }

        @Override
        public QueryResultBean[] newArray(int size) {
            return new QueryResultBean[size];
        }
    };

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSellNo() {
        return sellNo;
    }

    public void setSellNo(String sellNo) {
        this.sellNo = sellNo;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getHouseCount() {
        return houseCount;
    }

    public void setHouseCount(String houseCount) {
        this.houseCount = houseCount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSelectEndTime() {
        return selectEndTime;
    }

    public void setSelectEndTime(String selectEndTime) {
        if (!TextUtils.isEmpty(selectEndTime))
            this.selectEndTime = selectEndTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        canSelect = TextUtils.equals("正在报名", status);
    }

    public boolean isCanSelect() {
        return canSelect;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryResultBean that = (QueryResultBean) o;

        if (canSelect != that.canSelect) return false;
        if (district != null ? !district.equals(that.district) : that.district != null)
            return false;
        if (projectName != null ? !projectName.equals(that.projectName) : that.projectName != null)
            return false;
        if (sellNo != null ? !sellNo.equals(that.sellNo) : that.sellNo != null) return false;
        if (range != null ? !range.equals(that.range) : that.range != null) return false;
        if (houseCount != null ? !houseCount.equals(that.houseCount) : that.houseCount != null)
            return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null)
            return false;
        if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null) return false;
        if (selectEndTime != null ? !selectEndTime.equals(that.selectEndTime) : that.selectEndTime != null)
            return false;
        return status != null ? status.equals(that.status) : that.status == null;
    }

    @Override
    public int hashCode() {
        int result = district != null ? district.hashCode() : 0;
        result = 31 * result + (projectName != null ? projectName.hashCode() : 0);
        result = 31 * result + (sellNo != null ? sellNo.hashCode() : 0);
        result = 31 * result + (range != null ? range.hashCode() : 0);
        result = 31 * result + (houseCount != null ? houseCount.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (selectEndTime != null ? selectEndTime.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (canSelect ? 1 : 0);
        return result;
    }
}
