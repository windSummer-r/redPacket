package com.example.renwuhulianwang.myapplication;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by renwuhulianwang on 2017/11/9.
 */

public class AutoInstallApkService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                handleNotification(event);
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                String className = event.getClassName().toString();
                if (className.equals("com.tencent.mm.ui.LauncherUI")) {
                    getPacket();
                } else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI")) {
                    openPacket();
                } else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI")) {
                    close();
                }

                break;
        }
    }

    /**
     * 处理通知栏信息
     *
     * 如果是微信红包的提示信息,则模拟点击
     *
     * @param event
     */
    private void handleNotification(AccessibilityEvent event) {
        List<CharSequence> texts = event.getText();
        if (!texts.isEmpty()) {
            for (CharSequence text : texts) {
                String content = text.toString();
                //如果微信红包的提示信息,则模拟点击进入相应的聊天窗口
                if (content.contains("[微信红包]")) {
                    if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
                        Notification notification = (Notification) event.getParcelableData();
                        PendingIntent pendingIntent = notification.contentIntent;
                        try {
                            pendingIntent.send();
                            Log.e("rgh","[微信红包]");
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 关闭红包详情界面,实现自动返回聊天窗口
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void close() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            //为了演示,直接查看了关闭按钮的id
            List<AccessibilityNodeInfo> infos = nodeInfo.findAccessibilityNodeInfosByViewId("@id/ez");
            nodeInfo.recycle();
            for (AccessibilityNodeInfo item : infos) {
                item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    /**
     * 模拟点击,拆开红包
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void openPacket() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            //为了演示,直接查看了红包控件的id
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("@id/brt");
            nodeInfo.recycle();
            for (AccessibilityNodeInfo item : list) {
                item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    /**
     * 模拟点击,打开抢红包界面
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void getPacket() {
//        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
//        AccessibilityNodeInfo node = recycle(rootNode);
//
//        node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//        AccessibilityNodeInfo parent = node.getParent();
//        while (parent != null) {
//            if (parent.isClickable()) {
//                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                break;
//            }
//            parent = parent.getParent();
//        }
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            //为了演示,直接查看了红包控件的id
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("@id/a9b");
            nodeInfo.recycle();
            for (AccessibilityNodeInfo item : list) {
                item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    /**
     * 递归查找当前聊天窗口中的红包信息
     *
     * 聊天窗口中的红包都存在"领取红包"一词,因此可根据该词查找红包
     *
     * @param node
     */
    public AccessibilityNodeInfo recycle(AccessibilityNodeInfo node) {
        if (node.getChildCount() == 0) {
            if (node.getText() != null) {
                if ("领取红包".equals(node.getText().toString())) {
                    return node;
                }
            }
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                if (node.getChild(i) != null) {
                    recycle(node.getChild(i));
                }
            }
        }
        return node;
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

//    private static final String DEFAULT_PACKAGE_NAME = "com.android.packageinstaller";
//
//    private static final String[] IDS = {
//            "com.android.packageinstaller:id/ok_button",        // 下一步按钮的ID，注意ID的格式，必须这样写
//            "com.android.packageinstaller:id/done_button",      // 完成按钮的ID，注意ID的格式，必须这样写
//            "com.android.packageinstaller:id/confirm_button",    // 确认按钮的ID，注意ID的格式，必须这样写
//            "com.miui.packageinstaller:id/ok_button",
//            "com.miui.packageinstaller:id/launch_button",
//            "com.miui.packageinstaller:id/confirm_button"
//    };
//
//    @Override
//    protected void onServiceConnected() {
//        Log.e("rgh","onServiceConnected执行了");
//        super.onServiceConnected();
//    }
//
//    @Override
//    public void onAccessibilityEvent(AccessibilityEvent event) {
//        Log.e("rgh","onAccessibilityEvent执行了");
//        if (null == event) {
//            Log.e("rgh","event为null");
//            return;
//        }
//        installApkIfNecessary(event);
//        recycleAccessibilityEvent(event);
//    }
//
//    private void installApkIfNecessary(AccessibilityEvent event) {
//        Log.e("rgh","installApkIfNecessary执行了");
//        AccessibilityNodeInfo rootInfo = getRootInActiveWindow();
//        if (null == rootInfo) return;
//        String packageName = rootInfo.getPackageName().toString();
//        if (DEFAULT_PACKAGE_NAME.equals(packageName)) {
//            int length = IDS.length;
//            AccessibilityNodeInfo availableNode = null;
//            for (int i = 0; i < length; i++) {
//                availableNode = findAvailableNodeInfoByViewId(rootInfo, IDS[i]);
//                if (null != availableNode) {
//                    break;
//                }
//            }
//            if (null != availableNode) {
//                performClickWithAccessibilityNode(availableNode);
//            }
//        }
//    }
//
//    private AccessibilityNodeInfo findAvailableNodeInfoByViewId(AccessibilityNodeInfo root, String id) {
//        List<AccessibilityNodeInfo> availableNodes = root.findAccessibilityNodeInfosByViewId(id);
//        if (null == availableNodes || availableNodes.isEmpty()) {
//            return null;
//        }
//        return availableNodes.get(0);
//    }
//
//    private void performClickWithAccessibilityNode(AccessibilityNodeInfo nodeInfo) {
//        if (null != nodeInfo) {
//            if (nodeInfo.isClickable()) {
//                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//            } else {
//                performClickWithAccessibilityNode(nodeInfo.getParent());
//            }
//        }
//    }
//
//    @Override
//    public void onInterrupt() {
//    }
//
//    private void recycleAccessibilityEvent(AccessibilityEvent event) {
//        if (null != event) {
//            event.recycle();
//            event = null;
//        }
//    }
}