package com.example.cindywang.rewardfitbit;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 * Created by cindywang on 2/4/16.
 */
public class FitBitApi extends DefaultApi10a {
    private static final String AUTHORIZE_URL = "https://www.fitbit.com/oauth2/authorize?oauth_token=%s";

    public String getAccessTokenEndpoint(){
        return "https://api.fitbit.com/oauth2/access_token";
    }
    public String getRequestTokenEndpoint(){
        return "https://api.fitbit.com/oauth/request_token";
    }
    public String getAuthorizationUrl(Token requestToken){
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }
}