package com.dm.system;

import com.dm.service.LocationService;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class LocationBean implements Serializable {

  private String province;

  private String city;

  private String country;

  private Map<String, String> provinces = new HashMap<>();
  private Map<String, String> cities = new HashMap<>();
  private Map<String, String> countries = new HashMap<>();

  @Inject
  private LocationService locationService;

  @PostConstruct
  public void init() {
    provinces = locationService.getProvinceData();
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Map<String, String> getProvinces() {
    return provinces;
  }

  public void setProvinces(Map<String, String> provinces) {
    this.provinces = provinces;
  }

  public Map<String, String> getCities() {
    return cities;
  }

  public void setCities(Map<String, String> cities) {
    this.cities = cities;
  }

  public Map<String, String> getCountries() {
    return countries;
  }

  public void setCountries(Map<String, String> countries) {
    this.countries = countries;
  }

  public void handleProvinceChange() {
    countries = new HashMap<>();
    if (province != null && !province.equals("")) {
      cities = locationService.getCityData(province);
    } else {
      cities = new HashMap<>();
    }
  }

  public void handleCityChange() {
    if (city != null && !city.equals("")) {
      countries = locationService.getCountryData(city);
    } else {
      countries = new HashMap<>();
    }
  }

  public void displayLocation() {
    FacesMessage msg =
        new FacesMessage("Selected", "Province:" + province + ",City:" + city + ", Country: "
            + country);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }
}
