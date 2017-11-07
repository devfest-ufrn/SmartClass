/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartclass;

import java.io.File;

/**
 *
 * @author Pedro
 */
public class Professor {
    private String name;
    private Short temperature;
    private int time;
    private File slides;
    
    public Professor(String name, Short temperature, int time, File slides){
        this.name = name;
        this.temperature = temperature;
        this.time = time;
        this.slides = slides;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the temperature
     */
    public Short getTemperature() {
        return temperature;
    }

    /**
     * @param temperature the temperature to set
     */
    public void setTemperature(Short temperature) {
        this.temperature = temperature;
    }

    /**
     * @return the time
     */
    public int getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * @return the slides
     */
    public File getSlides() {
        return slides;
    }

    /**
     * @param slides the slides to set
     */
    public void setSlides(File slides) {
        this.slides = slides;
    }
    
}
