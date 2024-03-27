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
    double leftBorderPosition;
    double rightBorderPosition;
    double topBorderPosition;
    double bottomBorderPosition;
    ArrayList<Line> lines = new ArrayList<>();

    public Road() {
    }

    public Road(double roadCenter, double width, int laneCount) {
        this.roadCenter = roadCenter;
        this.width = width;
        this.laneCount = laneCount;
        this.topBorderPosition = 10000000;
        this.bottomBorderPosition = -10000000;
        leftBorderPosition = roadCenter - (width / 2);
        rightBorderPosition = roadCenter + (width / 2);
        lines.add(new Line(rightBorderPosition, bottomBorderPosition, rightBorderPosition, topBorderPosition)); // right border has index 0
        lines.add(new Line(leftBorderPosition, bottomBorderPosition, leftBorderPosition, topBorderPosition)); // left border has index 1

        for (int i = 0; i < laneCount; i++) {
            double currentX = MathUtils.lerp(leftBorderPosition, rightBorderPosition, (double)i/laneCount); 
            lines.add(new Line(currentX, bottomBorderPosition, currentX, topBorderPosition));
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

    public double getLeftBorderPosition() {
        return leftBorderPosition;
    }

    public double getRightBorderPosition() {
        return rightBorderPosition;
    }

    public double getTopBorderPosition() {
        return topBorderPosition;
    }

    public double getBottomBorderPosition() {
        return bottomBorderPosition;
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
