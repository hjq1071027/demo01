package cn.mediinfo.commonlib.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/********************************
 * @author hujq
 * @date 2019/7/17
 * @cp 联众智慧科技股份有限公司
 * @TODO
 *********************************/
public class Update implements Serializable {
    @SerializedName("VERSION_INFO")
    String version_info;
    @SerializedName("UPGRADE_FILE")
    String upgradefile;
    @SerializedName("IS_MUST")
    int is_must;
    @SerializedName("FILE_SIZE")
    long filesize;
    @SerializedName("VERSION_NAME")
    String versionname;
    @SerializedName("VERSION_CODE")
    int versioncode;

    public String getVersion_info() {
        return version_info;
    }

    public void setVersion_info(String version_info) {
        this.version_info = version_info;
    }

    public String getUpgradefile() {
        return upgradefile;
    }

    public void setUpgradefile(String upgradefile) {
        this.upgradefile = upgradefile;
    }

    public int getIs_must() {
        return is_must;
    }

    public void setIs_must(int is_must) {
        this.is_must = is_must;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public String getVersionname() {
        return versionname;
    }

    public void setVersionname(String versionname) {
        this.versionname = versionname;
    }

    public int getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(int versioncode) {
        this.versioncode = versioncode;
    }
}
