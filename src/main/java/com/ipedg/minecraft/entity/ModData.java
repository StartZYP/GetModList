package com.ipedg.minecraft.entity;

import java.util.Objects;

public class ModData {
    private String Name;
    private String Version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModData modData = (ModData) o;
        return Objects.equals(Name, modData.Name) &&
                Objects.equals(Version, modData.Version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Name, Version);
    }

    @Override
    public String toString() {
        return "ModData{" +
                "Name='" + Name + '\'' +
                ", Version='" + Version + '\'' +
                '}';
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public ModData(String name, String version) {
        Name = name;
        Version = version;
    }
}