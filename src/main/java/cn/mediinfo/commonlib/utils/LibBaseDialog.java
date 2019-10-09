package cn.mediinfo.commonlib.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/********************************
 * @author hujq
 * @date 2019/7/17
 * @cp 联众智慧科技股份有限公司
 * @TODO
 *********************************/

public class LibBaseDialog extends Dialog {
    Bundle bundle = null;
    private static int default_width = 160; // 默认宽度
    private static int default_height = 120;// 默认高度

    protected static Context mContext;
    public LibBaseDialog(Context context, View layout, int style) {
        this(context, default_width, default_height, layout, style);
        mContext = context;
    }

    public LibBaseDialog(Context context, int xml_id, int style) {
        super(context, style);
        LayoutInflater ly = LayoutInflater.from(context);
        View view = ly.inflate(xml_id, null);
        setContentView(view);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0; // 新位置X坐标
        params.y = 0; // 新位置Y坐标
        params.width = default_width; // 宽度
        params.height = default_height; // 高度
        window.setAttributes(params);
    }

    public LibBaseDialog(Context context, int width, int height, View layout,
                         int style) {
        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0; // 新位置X坐标
        params.y = 0; // 新位置Y坐标
        params.width = width; // 宽度
        params.height = height; // 高度
        window.setAttributes(params);
    }

    public LibBaseDialog(Context context, int width, int height, int xml_id,
                         int style) {
        super(context, style);
        LayoutInflater ly = LayoutInflater.from(context);
        View view = ly.inflate(xml_id, null);
        setContentView(view);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0; // 新位置X坐标
        params.y = 0; // 新位置Y坐标
        params.width = width; // 宽度
        params.height = height; // 高度
        window.setAttributes(params);
    }

    public LibBaseDialog(Context context, int width, int height, int xml_id,
                         int style, Bundle bundle) {
        super(context, style);
        LayoutInflater ly = LayoutInflater.from(context);
        View view = ly.inflate(xml_id, null);
        setContentView(view);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0; // 新位置X坐标
        params.y = 0; // 新位置Y坐标
        params.width = width; // 宽度
        params.height = height; // 高度
        window.setAttributes(params);
        this.bundle = bundle;
    }
}
