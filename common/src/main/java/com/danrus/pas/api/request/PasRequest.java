package com.danrus.pas.api.request;

import com.danrus.pas.api.types.RequestStatus;

import java.util.UUID;

public class PasRequest {
    private final UUID id;
    private final RequestData data;

    private RequestStatus status;

    public PasRequest(RequestData data) {
        this.id = UUID.randomUUID();
        this.data = data;
        this.status = RequestStatus.PENDING;
    }

    public UUID getId() {
        return id;
    }

    public RequestData getData() {
        return data;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
