/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Frame;

import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebNotification;
import java.awt.Component;

/**
 *
 * @author Achi
 */
public class Message {
    public static final int ERROR_MESSAGE = 0;
    public static final int INFORMATION_MESSAGE = 1;
    public static final int WARNING_MESSAGE = 2;
    private static WebNotification notification = new WebNotification();

    public Message() {
    }

    public static void showMessage(Component c, String msg, int messageType) {
        
        switch (messageType) {
            case ERROR_MESSAGE:
                notification.setIcon(NotificationIcon.error);
            case INFORMATION_MESSAGE:
                notification.setIcon(NotificationIcon.information);
            case WARNING_MESSAGE:
                notification.setIcon(NotificationIcon.warning);
                default:
        } 
        notification.setContent(msg);
        notification.setDisplayTime(10000);
        NotificationManager.showNotification(notification);
        
    }
}
