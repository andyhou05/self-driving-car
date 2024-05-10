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
 * Road Object where the cars will move.
 *
 * @author 2273410
 */
public class Road {

    //Physical properties of the road
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
    
    /**
     * ArrayList of Lines visualizing the border of the road.
     */
    public static ArrayList<Line> borderLines = new ArrayList<>();
    Line leftBorder;
    Line rightBorder;

    /**
     * ArrayList of Lines visualizing the road
     */
    public ArrayList<Line> lines = new ArrayList<>();

    /**
     * Creates empty Road object.
     */
    public Road() {
    }

    /**
     * Creates a Road object with a center position and a certain width.
     *
     * @param roadCenter
     * @param width
     */
    public Road(double roadCenter, double width) {
        this.roadCenter = roadCenter;
        this.width = width;
        
        // Positions of the left and right most border lines.
        leftBorderPosition = roadCenter - (width / 2);
        rightBorderPosition = roadCenter + (width / 2);
        
        // Positions of the first second and third lane.
        x_position_lane_one = MathUtils.lerp(-leftBorderPosition, width, 1.0f / 6.0f);
        x_position_lane_two = MathUtils.lerp(-leftBorderPosition, width, 3.0f / 6.0f);
        x_position_lane_three = MathUtils.lerp(-leftBorderPosition, width, 5.0f / 6.0f);
        
        // Create the Line objects for the left and right borders.
        rightBorder = new Line(rightBorderPosition, bottomBorderPosition, rightBorderPosition, topBorderPosition);
        leftBorder = new Line(leftBorderPosition, bottomBorderPosition, leftBorderPosition, topBorderPosition);
        borderLines.add(leftBorder);
        borderLines.add(rightBorder);
        lines.add(rightBorder);
        lines.add(leftBorder);
        
        // Create new Line objects for the number of lanes.
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
    
    //Setters & Getters

    /**
     *
     */
    public void resetLinePositions() {
        for (Line roadLine : lines) {
            roadLine.setTranslateY(0);
        }
    }

    /**
     *
     * @return
     */
    public double getWidth() {
        return width;
    }

    /**
     *
     * @return
     */
    public double getRoadCenter() {
        return roadCenter;
    }

    /**
     *
     * @return
     */
    public double getLeftBorderPosition() {
        return leftBorderPosition;
    }

    /**
     *
     * @return
     */
    public double getRightBorderPosition() {
        return rightBorderPosition;
    }

    /**
     *
     * @return
     */
    public double getTopBorderPosition() {
        return topBorderPosition;
    }

    /**
     *
     * @return
     */
    public double getBottomBorderPosition() {
        return bottomBorderPosition;
    }

    /**
     *
     * @return
     */
    public int getLaneCount() {
        return this.laneCount;
    }

    /**
     *
     * @return
     */
    public ArrayList<Line> getLines() {
        return lines;
    }

    /**
     *
     * @param lines
     */
    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }

    /**
     *
     * @return
     */
    public double getX_position_lane_one() {
        return x_position_lane_one;
    }

    /**
     *
     * @param x_position_lane_one
     */
    public void setX_position_lane_one(double x_position_lane_one) {
        this.x_position_lane_one = x_position_lane_one;
    }

    /**
     *
     * @return
     */
    public double getX_position_lane_two() {
        return x_position_lane_two;
    }

    /**
     *
     * @param x_position_lane_two
     */
    public void setX_position_lane_two(double x_position_lane_two) {
        this.x_position_lane_two = x_position_lane_two;
    }

    /**
     *
     * @return
     */
    public double getX_position_lane_three() {
        return x_position_lane_three;
    }

    /**
     *
     * @param x_position_lane_three
     */
    public void setX_position_lane_three(double x_position_lane_three) {
        this.x_position_lane_three = x_position_lane_three;
    }

    /**
     *
     * @return
     */
    public Line getLeftBorder() {
        return leftBorder;
    }

    /**
     *
     * @param leftBorder
     */
    public void setLeftBorder(Line leftBorder) {
        this.leftBorder = leftBorder;
    }

    /**
     *
     * @return
     */
    public Line getRightBorder() {
        return rightBorder;
    }

    /**
     *
     * @param rightBorder
     */
    public void setRightBorder(Line rightBorder) {
        this.rightBorder = rightBorder;
    }

}
