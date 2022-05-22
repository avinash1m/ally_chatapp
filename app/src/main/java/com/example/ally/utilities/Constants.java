package com.example.ally.utilities;

import java.util.HashMap;

public class Constants {
    public static  final  String KEY_COLLECTION_USERS = "users";
    public  static final String KEY_NAME ="name";
    public  static final String KEY_SELECT_TYPE ="select_type";
    public  static final String KEY_COLLAGE_ID ="collage_id";
    public  static final String KEY_EMAIL ="email";
    public  static final String KEY_PASSWORD ="password";
    public  static final String KEY_PREFERENCE_NAME ="allyPreference";
    public  static final String KEY_IS_SIGNED_IN ="isSignedIn";
    public  static final String KEY_USER_ID ="userId";
    public  static final String KEY_IMAGE ="image";
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_USER = "user";
    public static final String KEY_COLLECTION_CHAT="chat";
    public static final String KEY_SENDER_ID="senderId";
    public static final String KEY_RECEIVER_ID="receiverId";
    public static final String KEY_MESSAGE="message";
    public static final String KEY_TIMESTAMP="timestamp";
    public static final String KEY_COLLECTION_CONVERSATIONS="conversations";
    public static final String KEY_SENDER_NAME="senderName";
    public static final String KEY_RECEIVER_NAME="receiverName";
    public static final String KEY_SENDER_IMAGE="senderImage";
    public static final String KEY_RECEIVER_IMAGE="receiver Image";
    public static final String KEY_LAST_MESSAGE="lastMessage";
    public static final String KEY_AVAILABILITY="availability";
    public static final String REMOTE_MSG_AUTHORIZATION="Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE="Content-Type";
    public static final String REMOTE_MSG_DATA="data";
    public static final String REMOTE_MSG_REGISTRATION_IDS="registration_ids";

    public static HashMap<String,String> remoteMsgHeaders=null;
    public static HashMap<String,String>getRemoteMsgHeaders() {
        if (remoteMsgHeaders == null) {
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(
                    REMOTE_MSG_AUTHORIZATION,
                    "key=AAAAskkmFL0:APA91bG7fvrWAy_tK3R0pxIBUYWGH8SM0QOPsXi8XqpKM0UHuhq_pmnWzDDKoC1EdNLWYxe-RPlMJIVPca0GWSeMNjZ0lD7p89aaM819dmBuj4JYmcmrR_C2DtQP3Eb3L59WS8sRBRKb"
            );
            remoteMsgHeaders.put(
                    REMOTE_MSG_CONTENT_TYPE,
                    "application/json"
            );
        }
        return remoteMsgHeaders;

    }


}

