/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.models;

import edu.vanier.selfdriving.neuralnetwork.NeuralNetwork;
import edu.vanier.selfdriving.utils.MathUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 * Car object and properties.
 *
 * @author 2273410
 */
public class Car {

    // JavaFX Car properties
    StackPane carStack = new StackPane();
    Rectangle hitBox;
    Image carImage;
    ImageView carImageView = new ImageView();

    //Transition properties
    double speedX = 0.0;
    double speedY = 0.0;
    double speed = 0.0;
    double maxSpeed = 8;
    boolean carMoving = false;

    // General properties
    double carWidth = 60;
    double carLength = 120;
    double widthHitboxOffset = 16;
    double lengthHitboxOffset = 20;
    double accelerationValue = 0.3;
    double deccelerationValue = accelerationValue;
    int sensorCount = 5;
    double sensorSpread = Math.PI / 2;
    int direction = 0;
    boolean accelerating = false;
    boolean turningRight = false;
    boolean turningLeft = false;
    boolean flipRotate = false;
    boolean dead = false;
    boolean userControlled;
    Sensor[] sensors = new Sensor[5];
    Road road;
    NeuralNetwork neuralNetwork = new NeuralNetwork(5, 6, 4);
    double[] inputs = new double[sensorCount];

    /**
     * Creates a basic car object with base properties.
     */
    public Car() {
        carImageView.setFitHeight(carLength);
        carImageView.setFitWidth(carWidth);
        hitBox = new Rectangle(carWidth - widthHitboxOffset, carLength - lengthHitboxOffset);
        hitBox.setVisible(false);
        carStack.setPrefHeight(carLength);
        carStack.setPrefWidth(carWidth);
        carStack.getChildren().addAll(carImageView, hitBox);
    }

    /**
     * Creates a Car Object with a certain x and y position, an Image, and a
     * control mechanism (User or AI).
     *
     * @param x
     * @param y
     * @param carImage
     * @param userControlled
     */
    public Car(double x, double y, Image carImage, boolean userControlled) {
        this();
        this.carImage = carImage;
        this.userControlled = userControlled;
        carImageView.setImage(carImage);
        carStack.setLayoutX(x);
        carStack.setLayoutY(y);
        if (!userControlled) {
            initSensors();
        }
    }

    /**
     * Initialize the position of the Sensors based on the Car.
     *
     */
    public void initSensors() {
        for (int i = 0; i < sensorCount; i++) {
            Sensor sensor = new Sensor(this);
            sensors[i] = sensor;
        }
        for (int i = 0; i < sensorCount; i++) {
            double rayAngle = MathUtils.lerp(sensorSpread / 2, -sensorSpread / 2, (double) i / (sensorCount - 1));

            // Start the Sensor in the middle of the Car.
            double startX = 0.5 * carWidth;
            double startY = 0.5 * carLength;

            // Trig to direct Sensor in the correct direction
            double endX = startX - Math.sin(rayAngle) * Sensor.getSensorLength();
            double endY = startY - Math.cos(rayAngle) * Sensor.getSensorLength();
            Line sensorLine = new Line(startX, startY, endX, endY);
            sensorLine.setStrokeWidth(2);
            sensors[i].setSensorLine(sensorLine);
        }
    }

    /**
     * Accelerates the car in the correct direction.
     *
     * @param direction
     */
    public void acceleration(int direction) {
        carMoving = true;
        boolean atMaxSpeed = Math.abs(speed) < maxSpeed;
        speed = atMaxSpeed ? speed + accelerationValue * direction : maxSpeed * direction;
        double angle = 90 - carStack.getRotate();
        speedY = speed * Math.sin(angle * (Math.PI / 180));
        speedX = -speed * Math.cos(angle * (Math.PI / 180));

    }

    /**
     * Decelerates the car in the correct direction
     *
     * @param direction
     */
    public void decceleration(int direction) {
        // Make sure to stop car when deccelerating
        // If direction is 1, speed will deccelerate by going down (3 m/s -> 0 m/s), once speed is negative, we know to stop deccelerating, and vice-versa
        speed = (carMoving && speed * direction > 0) ? speed - (deccelerationValue * direction) : 0;
        carMoving = speed != 0;

        double angle = 90 - carStack.getRotate();
        speedY = speed * Math.sin(angle * (Math.PI / 180));
        speedX = -speed * Math.cos(angle * (Math.PI / 180));
    }

    /**
     * Rotates the car by one degree either clockwise or counter clockwise.
     *
     * @param direction
     */
    public void rotate(int direction) {
        carStack.setRotate(carStack.getRotate() - 5 * direction);
    }

    /**
     * Sets the visibility of the sensors.
     *
     * @param visible
     */
    public void setSensorsVisible(boolean visible) {
        for (Line sensorLine : getSensorsLines()) {
            sensorLine.setVisible(visible);
        }
    }

    /**
     * Sets the visibility of the car.
     *
     * @param visible
     */
    public void setCarVisible(boolean visible) {
        double opacity = 0;
        opacity = visible ? 1.0 : 0.2;
        carImageView.setOpacity(opacity);
    }

    /**
     * Sets the visibility of the car and the sensors based on the control type
     * of the car (AI or User)
     *
     * @param visible
     */
    public void setVisible(boolean visible) {
        setCarVisible(visible);
        if (!userControlled) {
            setSensorsVisible(visible);
        }
    }

    //Getters and Setters
    /**
     * Image object of the Car.
     *
     * @return
     */
    public Image getCarImage() {
        return carImage;
    }

    /**
     *
     * @param carImage
     */
    public void setCarImage(Image carImage) {
        this.carImage = carImage;
    }

    /**
     * Width of the car.
     *
     * @return
     */
    public double getCarWidth() {
        return carWidth;
    }

    /**
     * Speed of the car
     *
     * @return
     */
    public double getSpeed() {
        return speed;
    }

    /**
     *
     * @param speed
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     *
     * @param carWidth
     */
    public void setCarWidth(double carWidth) {
        carImageView.setFitWidth(carWidth);
        carStack.setPrefWidth(carWidth);
        hitBox.setWidth(carWidth - widthHitboxOffset);
        this.carWidth = carWidth;
    }

    /**
     * Length of the car.
     *
     * @return
     */
    public double getCarLength() {
        return carLength;
    }

    /**
     *
     * @param carLength
     */
    public void setCarLength(double carLength) {
        carImageView.setFitHeight(carLength);
        carStack.setPrefHeight(carLength);
        hitBox.setHeight(carLength - lengthHitboxOffset);
        this.carLength = carLength;
    }

    /**
     * Acceleration of the car.
     *
     * @return
     */
    public double getAccelerationValue() {
        return accelerationValue;
    }

    /**
     *
     * @param accelerationValue
     */
    public void setAccelerationValue(double accelerationValue) {
        this.accelerationValue = accelerationValue;
    }

    /**
     * X position of the car.
     *
     * @return
     */
    public double getxPosition() {
        return carStack.getLayoutX();
    }

    /**
     *
     * @param xPosition
     */
    public void setxPosition(double xPosition) {
        carStack.setLayoutX(xPosition);
    }

    /**
     * Y position of the car.
     *
     * @return
     */
    public double getyPosition() {
        return carStack.getLayoutY();
    }

    /**
     *
     * @param yPosition
     */
    public void setyPosition(double yPosition) {
        carStack.setLayoutY(yPosition);
    }

    /**
     * X component of the speed of the car.
     *
     * @return
     */
    public double getSpeedX() {
        return speedX;
    }

    /**
     *
     * @param speedX
     */
    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    /**
     * Y component of the speed of the car.
     *
     * @return
     */
    public double getSpeedY() {
        return speedY;
    }

    /**
     *
     * @param speedY
     */
    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    /**
     * Max speed of the car.
     *
     * @return
     */
    public double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     *
     * @param maxSpeed
     */
    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Returns true if the car is moving.
     *
     * @return
     */
    public boolean isCarMoving() {
        return carMoving;
    }

    /**
     *
     * @param carMoving
     */
    public void setCarMoving(boolean carMoving) {
        this.carMoving = carMoving;
    }

    /**
     * Sensor list of the car
     *
     * @return
     */
    public Sensor[] getSensors() {
        return this.sensors;
    }

    /**
     * Sensor Line Object list
     *
     * @return
     */
    public Line[] getSensorsLines() {
        Line[] sensorLines = new Line[sensorCount];
        for (int i = 0; i < sensorCount; i++) {
            Sensor sensor = sensors[i];
            sensorLines[i] = sensor.getSensorLine();
        }
        return sensorLines;
    }

    /**
     * Deceleration value of the car
     *
     * @return
     */
    public double getDeccelerationValue() {
        return deccelerationValue;
    }

    /**
     *
     * @param deccelerationValue
     */
    public void setDeccelerationValue(double deccelerationValue) {
        this.deccelerationValue = deccelerationValue;
    }

    /**
     * Road object of the car.
     *
     * @return
     */
    public Road getRoad() {
        return road;
    }

    /**
     *
     * @param road
     */
    public void setRoad(Road road) {
        this.road = road;
    }

    /**
     * ImageView of the car.
     *
     * @return
     */
    public ImageView getCarImageView() {
        return carImageView;
    }

    /**
     *
     * @param carImageView
     */
    public void setCarImageView(ImageView carImageView) {
        this.carImageView = carImageView;
    }

    /**
     * StackPane object of the car.
     *
     * @return
     */
    public StackPane getCarStack() {
        return carStack;
    }

    /**
     *
     * @param carStack
     */
    public void setCarStack(StackPane carStack) {
        this.carStack = carStack;
    }

    /**
     * Hit box Rectangle of the car.
     *
     * @return
     */
    public Rectangle getHitBox() {
        return hitBox;
    }

    /**
     *
     * @param hitBox
     */
    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    /**
     * Neural Network of the car.
     *
     * @return
     */
    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

    /**
     *
     * @param neuralNetwork
     */
    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    /**
     * Offset width of the hit box.
     *
     * @return
     */
    public double getWidthHitboxOffset() {
        return widthHitboxOffset;
    }

    /**
     *
     * @param widthHitboxOffset
     */
    public void setWidthHitboxOffset(double widthHitboxOffset) {
        this.widthHitboxOffset = widthHitboxOffset;
    }

    /**
     * Offset length of the hit box.
     *
     * @return
     */
    public double getLengthHitboxOffset() {
        return lengthHitboxOffset;
    }

    /**
     *
     * @param lengthHitboxOffset
     */
    public void setLengthHitboxOffset(double lengthHitboxOffset) {
        this.lengthHitboxOffset = lengthHitboxOffset;
    }

    /**
     * Number of sensors of the car.
     *
     * @return
     */
    public int getSensorCount() {
        return sensorCount;
    }

    /**
     *
     * @param sensorCount
     */
    public void setSensorCount(int sensorCount) {
        this.sensorCount = sensorCount;
    }

    /**
     * The spread angle of the sensors.
     *
     * @return
     */
    public double getSensorSpread() {
        return sensorSpread;
    }

    /**
     *
     * @param sensorSpread
     */
    public void setSensorSpread(double sensorSpread) {
        this.sensorSpread = sensorSpread;
    }

    /**
     * Direction of the car.
     *
     * @return
     */
    public int getDirection() {
        return direction;
    }

    /**
     *
     * @param direction
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Returns true if the car is accelerating.
     *
     * @return
     */
    public boolean isAccelerating() {
        return accelerating;
    }

    /**
     *
     * @param accelerating
     */
    public void setAccelerating(boolean accelerating) {
        this.accelerating = accelerating;
    }

    /**
     * Returns true if the car is turning right.
     *
     * @return
     */
    public boolean isTurningRight() {
        return turningRight;
    }

    /**
     *
     * @param turningRight
     */
    public void setTurningRight(boolean turningRight) {
        this.turningRight = turningRight;
    }

    /**
     * Returns true if the car is turning left.
     *
     * @return
     */
    public boolean isTurningLeft() {
        return turningLeft;
    }

    /**
     *
     * @param turningLeft
     */
    public void setTurningLeft(boolean turningLeft) {
        this.turningLeft = turningLeft;
    }

    /**
     * Returns true if the direction of the car needs to be flipped when
     * turning.
     *
     * @return
     */
    public boolean isFlipRotate() {
        return flipRotate;
    }

    /**
     *
     * @param flipRotate
     */
    public void setFlipRotate(boolean flipRotate) {
        this.flipRotate = flipRotate;
    }

    /**
     * Returns true if the car is dead.
     *
     * @return
     */
    public boolean isDead() {
        return dead;
    }

    /**
     *
     * @param dead
     */
    public void setDead(boolean dead) {
        this.dead = dead;
    }

    /**
     * The input values of the car sensors.
     *
     * @return
     */
    public double[] getInputs() {
        return inputs;
    }

    /**
     *
     * @param inputs
     */
    public void setInputs(double[] inputs) {
        this.inputs = inputs;
    }

}
