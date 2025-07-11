package com.danrus.utils.providers;

import com.danrus.PASClient;
import com.danrus.interfaces.AbstractSkinProvider;
import com.danrus.utils.MojangApiUtils;
import org.figuramc.figura.avatar.AvatarManager;
import org.figuramc.figura.avatar.UserData;
import org.figuramc.figura.backend2.AuthHandler;
import org.figuramc.figura.backend2.HttpAPI;
import org.figuramc.figura.backend2.NetworkStuff;
import org.figuramc.figura.backend2.websocket.FiguraWebSocketAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class FiguraSkinProvider extends AbstractSkinProvider {
    @Override
    public void load(String name) {
        try {

            CompletableFuture<String> uuidFuture = MojangApiUtils.getUUID(name);
            uuidFuture.exceptionally(throwable -> {
                PASClient.LOGGER.error("Failed to get UUID for " + name, throwable);
                this.doFail(name);
                return null;
            }).thenAccept(uuidString -> {
                if (uuidString == null) {
                    PASClient.LOGGER.warn("Could not find UUID for player " + name);
                    this.doFail(name);
                    return;
                }
                try {
                    String formattedUUID = String.format(
                            "%s-%s-%s-%s-%s",
                            uuidString.substring(0, 8),
                            uuidString.substring(8, 12),
                            uuidString.substring(12, 16),
                            uuidString.substring(16, 20),
                            uuidString.substring(20)
                    );
                    UUID uuid = UUID.fromString(formattedUUID);
                    var avatar = AvatarManager.getAvatarForPlayer(uuid);
                    if (avatar != null && avatar.nbt != null) {
                        PASClient.LOGGER.debug(avatar.nbt.get("model").asString());
                    }
                } catch (Exception e) {
                    PASClient.LOGGER.error("Error processing avatar data for " + name, e);
                    this.doFail(name);
                }
            });
        } catch (Exception e) {
            PASClient.LOGGER.error("FiguraSkinProvider: Failed to initialize Figura API for " + name, e);
            this.doFail(name);
        }
    }

}
