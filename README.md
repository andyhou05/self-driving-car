# JavaFX Self‑Driving Car Simulator

A **2D self‑driving car simulation** built with **JavaFX**, where cars learn to navigate a track and avoid collisions using a **Neural Network** trained via a **Genetic Algorithm**. The application allows users to visually train, observe, and evolve autonomous agents in real time.

![Image](https://github.com/user-attachments/assets/d9565307-c27a-4e8b-9961-53aa9d48b941)

---

## Features

* **2D Self‑Driving Cars** rendered with JavaFX
* **Neural Network Controller** for decision‑making
* **Genetic Algorithm Training** (selection, crossover, mutation)
* **Real‑time Visualization** of training progress
* **Collision Detection** with track boundaries and obstacles
* **Generation‑based Evolution** with fitness scoring
* **User Controls** to start, pause, reset, and observe training

---

## How It Works

### Neural Network

Each car is controlled by a feed‑forward neural network that:

* Takes sensor inputs (distance to walls)
* Produces outputs such as steering direction and acceleration

The neural network weights define the "brain" of each car.

### Genetic Algorithm

Cars improve over generations using a genetic algorithm:

1. **Initialization** – Start with a population of random cars
2. **Evaluation** – Each car is assigned a fitness score based on:

   * Distance traveled
   * Time alive
3. **Selection** – Top‑performing cars are selected
4. **Crossover** – Neural networks are combined
6. **Next Generation** – A new population is created and trained

Over time, cars learn to avoid collisions and drive more efficiently.

---

## Setup

1. Clone this repository
2. Remove the `.git` directory 
3. Add the cloned directory structure to your local repo (if you already have one)
4. Open the project with NetBeans
5. Using NetBeans, build the project to resolve the dependencies
6. Change the project name in `settings.gradle`
7. Rename the default Java package in `MainApp.java` and `MainAppController.java`.
8. Change the value of the `mainClass` class property in `build.gradle`. A fully-qualified name of the class containing the main method that you want to run must be supplied.

## NetBeans

This project template was tested with `NetBeans 17` and `JDK 18`.
