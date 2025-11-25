package com.danrus.pas.config;

public class DummyConfig extends PasConfig {
    @Override
    public boolean isEnableMod() {
        return true;
    }

    @Override
    public int getDownloadThreads() {
        return 3;
    }

    @Override
    public DownloadStatusDisplay getDownloadStatusDisplay() {
        return DownloadStatusDisplay.NONE;
    }

    @Override
    public boolean isHideParamsOnLabel() {
        return true;
    }

    @Override
    public String getDefaultSkin() {
        return "";
    }

    @Override
    public boolean isShowArmorStandWhileDownloading() {
        return true;
    }

    @Override
    public boolean isShowEasterEggs() {
        return true;
    }

    @Override
    public boolean isTryApplyFromServerPlayer() {
        return true;
    }

    @Override
    public boolean isPossessiveShowDefaultHand() {
        return false;
    }
}
