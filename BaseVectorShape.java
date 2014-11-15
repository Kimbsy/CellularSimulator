/***************************************************************************
* Copyright 2013 Dave Kimber
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*     http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
***************************************************************************/

import java.awt.Shape;
//Base vector shape class for polygonal shapes
public class BaseVectorShape {
	//variables
	private Shape shape;
	private boolean alive;
	private boolean debug;
	private double speed;
	private double energy;
	private double x, y;
	private double velX, velY;
	private double moveAngle, faceAngle;

	//accessor methods
	public Shape getShape() {return shape;}
	public boolean isAlive() {return alive;}
	public boolean isDebug() {return debug;}
	public double getSpeed() {return speed;}
	public double getEnergy() {return energy;}
	public double getX() {return x;}
	public double getY() {return y;}
	public double getVelX() {return velX;}
	public double getVelY() {return velY;}
	public double getMoveAngle() {return moveAngle;}
	public double getFaceAngle() {return faceAngle;}

	//mutator and helper methods
	public void setShape(Shape shape) {this.shape = shape;}
	public void setAlive(boolean alive) {this.alive = alive;}
	public void setDebug(boolean debug) {this.debug = debug;}
	public void setSpeed(double speed) {this.speed = speed;}
	public void incSpeed(double speed) {this.speed += speed;}
	public void setEnergy(double energy) {this.energy = energy;}
	public void incEnergy(double energy) {this.energy += energy;}
	public void decEnergy(double energy) {this.energy -= energy;}
	public void setX(double x) {this.x = x;}
	public void incX(double i) {this.x += i;}
	public void setY(double y) {this.y = y;}
	public void incY(double i) {this.y += i;}
	public void setVelX(double velX) {this.velX = velX;}
	public void incVelX(double i) {this.velX += i;}
	public void setVelY(double velY) {this.velY = velY;}
	public void incVelY(double i) {this.velY += i;}
	public void setFaceAngle(double angle) {this.faceAngle = angle;}
	public void incFaceAngle(double i) {this.faceAngle += i;}
	public void setMoveAngle(double angle) {this.moveAngle = angle;}
	public void	incMoveAngle(double i) {this.moveAngle += i;}

	//default constructor
	BaseVectorShape() {
		setShape(null);
		setAlive(false);
		setDebug(false);
		setSpeed(0);
		setEnergy(0);
		setX(0.0);
		setY(0.0);
		setVelX(0.0);
		setVelY(0.0);
		setMoveAngle(0.0);
		setFaceAngle(0.0);
	}
}