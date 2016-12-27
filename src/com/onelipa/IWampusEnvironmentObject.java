package com.onelipa;

/**
 * Created by Oleksandr on 22.12.2016.
 */
public interface IWampusEnvironmentObject {
    String getObjectName();
    void onEnvironmentUpdated(WampusEnvironment env);
    void die();
}
