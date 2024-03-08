/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.models;

import java.util.ArrayList;

/**
 *
 * @author 2273410
 */
public class Road {
    double width;
    double x;
    int laneCount;
    double left=x-(width/2);
    double right=x+(width/2);
    double top;
    double bottom;
    ArrayList<Double> lanesValueX = new ArrayList<Double>();

    public double getWidth() {
        return width;
    }

    public double getX() {
        return x;
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }

    public double getTop() {
        return top;
    }

    public double getBottom() {
        return bottom;
    }
    
    public ArrayList<Double> getLanesValueX(){
        return this.lanesValueX;
    }
    public int getLaneCount(){
        return this.laneCount;
    }
    
    public Road(){}
    
    public Road(double x, double width, int laneCount){
        this.x = x;
        this.width = width;
        this.laneCount = laneCount;
        this.top = 10000000;
        this.bottom = -10000000;
        
        for(int i = 0; i<laneCount; i++){
            lanesValueX.add(((i+1)*width)/laneCount);
        
        }
    }
}
