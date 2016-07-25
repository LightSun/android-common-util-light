package com.heaven7.android.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * this class is a help util class.
 * Created by heaven7 on 2016/7/25.
 */
class IpcUtil {

    /**
     * convert the implicit
     * create the right implicit Intent for android 5.0,  in android 5.0 all service must be implicit Intent.
     * @param context the context
     * @param implicitIntent the implicit service intent
     * @return the new intent for service
     * @throws com.heaven7.android.ipc.RemoteMessageContext.IpcException
     */
    public static Intent getExplicitIntent(Context context, Intent implicitIntent, String fixPackname) throws RemoteMessageContext.IpcException {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() == 0) {
            return null;
        }
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = null;
        for(ResolveInfo info : resolveInfo){
             if(fixPackname.equals(info.serviceInfo.packageName)){
                 serviceInfo = info;
                 break;
             }
        }
        if(serviceInfo == null){
            throw new RemoteMessageContext.IpcException("can't find the remote service: " + fixPackname +".MessageService");
        }
       // ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        return explicitIntent;
    }
}
