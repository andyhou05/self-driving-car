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
    int laneCount = 3;
    double leftBorderPosition;
    double rightBorderPosition;
    double topBorderPosition = 10000000;
    double bottomBorderPosition = -10000000;
    double x_position_lane_one;
    double x_position_lane_two;
    double x_position_lane_three;
    public static ArrayList<Line> borderLines = new ArrayList<>();
    Line leftBorder;
    Line rightBorder;
    public ArrayList<Line> lines = new ArrayList<>();

    public Road() {
    }

    public Road(double roadCenter, double width) {
        this.roadCenter = roadCenter;
        this.width = width;
        leftBorderPosition = roadCenter - (width / 2);
        rightBorderPosition = roadCenter + (width / 2);
        x_position_lane_one = MathUtils.lerp(-leftBorderPosition, width, 1.0f / 6.0f);
        x_position_lane_two = MathUtils.lerp(-leftBorderPosition, width, 3.0f / 6.0f);
        x_position_lane_three = MathUtils.lerp(-leftBorderPosition, width, 5.0f / 6.0f);
        rightBorder = new Line(rightBorderPosition, bottomBorderPosition, rightBorderPosition, topBorderPosition);
        leftBorder = new Line(leftBorderPosition, bottomBorderPosition, leftBorderPosition, topBorderPosition);
        borderLines.add(leftBorder);
        borderLines.add(rightBorder);
        lines.add(rightBorder);
        lines.add(leftBorder);

        for (int i = 0; i < laneCount; i++) {
            double currentX = MathUtils.lerp(leftBorderPosition, rightBorderPosition, (double) i / laneCount);
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
    
    public void resetLinePositions(){
        for(Line roadLine:lines){
            roadLine.setTranslateY(0);
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

    public double getX_position_lane_one() {
        return x_position_lane_one;
    }

    public void setX_position_lane_one(double x_position_lane_one) {
        this.x_position_lane_one = x_position_lane_one;
    }

    public double getX_position_lane_two() {
        return x_position_lane_two;
    }

    public void setX_position_lane_two(double x_position_lane_two) {
        this.x_position_lane_two = x_position_lane_two;
    }

    public double getX_position_lane_three() {
        return x_position_lane_three;
    }

    public void setX_position_lane_three(double x_position_lane_three) {
        this.x_position_lane_three = x_position_lane_three;
    }

    public Line getLeftBorder() {
        return leftBorder;
    }

    public void setLeftBorder(Line leftBorder) {
        this.leftBorder = leftBorder;
    }

    public Line getRightBorder() {
        return rightBorder;
    }

    public void setRightBorder(Line rightBorder) {
        this.rightBorder = rightBorder;
    }

}
