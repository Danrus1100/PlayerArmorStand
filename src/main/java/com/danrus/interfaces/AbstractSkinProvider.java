package com.danrus.interfaces;

import com.danrus.PASModelData;
import com.danrus.enums.DownloadStatus;
import com.danrus.managers.OverlayMessageManger;
import com.danrus.managers.SkinManger;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class AbstractSkinProvider {
    protected String literal;

    public abstract void load(String name);

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }

    protected void doFail(String name) {
        PASModelData data = SkinManger.getInstance().getData(Text.of(name));
        if (data == null) {
            data = new PASModelData(name);
        }
        OverlayMessageManger.getInstance().showFailMessage(name);
        data.setStatus(DownloadStatus.FAILED);
        SkinManger.getInstance().getDataManager().store(name, data);
    }

    protected void updateStatus(String name, DownloadStatus status) {
        PASModelData data = getOrCreateModelData(name);
        data.setStatus(status);
        SkinManger.getInstance().getDataManager().store(name, data);
    }

    protected void updateModelData(String name, Identifier textureId, boolean isSkin) {
        PASModelData data = getOrCreateModelData(name);
        if (isSkin) {
            data.setSkinTexture(textureId);
        } else {
            data.setCapeTexture(textureId);
        }
        SkinManger.getInstance().getDataManager().store(name, data);
    }

    protected PASModelData getOrCreateModelData(String name) {
        PASModelData data = SkinManger.getInstance().getData(Text.of(name));
        return data != null ? data : new PASModelData(name);
    }

}
