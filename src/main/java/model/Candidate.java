package model;

import java.util.Objects;

public class Candidate {
    private int id;
    private String name;
    private int cityId;
    private String city;

    public Candidate(int id, String name, int cityId, String city) {
        this.id = id;
        this.name = name;
        this.cityId = cityId;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Candidate candidate = (Candidate) o;
        return id == candidate.id
                && Objects.equals(name, candidate.name)
                && Objects.equals(cityId, candidate.cityId)
                && Objects.equals(city, candidate.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cityId, city);
    }
}
