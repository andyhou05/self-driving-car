/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.models;

import edu.vanier.selfdriving.utils.MathUtils;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author 2273410
 */
public class Road {

    double width;
    double roadCenter;
    int laneCount;
    double left;
    double right;
    double top;
    double bottom;
    ArrayList<Line> lines = new ArrayList<>();

    public Road() {
    }

    public Road(double roadCenter, double width, int laneCount) {
        this.roadCenter = roadCenter;
        this.width = width;
        this.laneCount = laneCount;
        this.top = 10000000;
        this.bottom = -10000000;
        left = roadCenter - (width / 2);
        right = roadCenter + (width / 2);
        lines.add(new Line(right, bottom, right, top));
        lines.add(new Line(left, bottom, left, top));

        for (int i = 0; i < laneCount; i++) {
            double currentX = MathUtils.lerp(left, right, (double)i/laneCount); 
            lines.add(new Line(currentX, bottom, currentX, top));
        }
        for (int i = 0; i < lines.size(); i++) {
            // We want the lines on both edges to be solid
            if (i > 1) {
                lines.get(i).getStrokeDashArray().addAll(40d, 40d);
            }
            lines.get(i).setStroke(Color.WHITE);
            lines.get(i).setStrokeWidth(5);
        }
    }

    public double getWidth() {
        return width;
    }

    public double getRoadCenter() {
        return roadCenter;
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

    public int getLaneCount() {
        return this.laneCount;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }

}
