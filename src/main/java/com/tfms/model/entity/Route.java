package com.tfms.model.entity;

public class Route {

    private int routeId;
    private String routeName;

    public Route() {}

    public Route(int routeId, String routeName) {
        this.routeId = routeId;
        this.routeName = routeName;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }
}