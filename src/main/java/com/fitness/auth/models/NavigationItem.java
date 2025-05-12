package com.fitness.auth.models;

public class NavigationItem {
    private final String name;
    private final NavigationType type;
    private final String fxmlPath;
    private final String iconPath;

    public NavigationItem(String name, NavigationType type, String fxmlPath, String iconPath) {
        this.name = name;
        this.type = type;
        this.fxmlPath = fxmlPath;
        this.iconPath = iconPath;
    }

    public String getName() {
        return name;
    }

    public NavigationType getType() {
        return type;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }

    public String getIconPath() {
        return iconPath;
    }

    @Override
    public String toString() {
        return name;
    }
}
