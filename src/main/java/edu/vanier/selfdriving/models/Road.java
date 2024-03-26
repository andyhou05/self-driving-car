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
    double leftBorder;
    double rightBorder;
    double topBorder;
    double bottomBorder;
    ArrayList<Line> lines = new ArrayList<>();

    public Road() {
    }

    public Road(double roadCenter, double width, int laneCount) {
        this.roadCenter = roadCenter;
        this.width = width;
        this.laneCount = laneCount;
        this.topBorder = 10000000;
        this.bottomBorder = -10000000;
        leftBorder = roadCenter - (width / 2);
        rightBorder = roadCenter + (width / 2);
        lines.add(new Line(rightBorder, bottomBorder, rightBorder, topBorder));
        lines.add(new Line(leftBorder, bottomBorder, leftBorder, topBorder));

        for (int i = 0; i < laneCount; i++) {
            double currentX = MathUtils.lerp(leftBorder, rightBorder, (double)i/laneCount); 
            lines.add(new Line(currentX, bottomBorder, currentX, topBorder));
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

    public double getLeftBorder() {
        return leftBorder;
    }

    public double getRightBorder() {
        return rightBorder;
    }

    public double getTopBorder() {
        return topBorder;
    }

    public double getBottomBorder() {
        return bottomBorder;
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
