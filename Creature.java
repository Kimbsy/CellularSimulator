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

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Point2D.*;
import java.util.*;

public class Creature extends BaseVectorShape {

	//random number generator
	Random rand = new Random();

	//give the creature a color
	private Color color;
	public Color getColor() {return color;}
	public void setColor(Color color) {this.color = color;}

	//define creature shape as arraylist of line2D.Double
	private ArrayList<Line2D> shapeList = new ArrayList<Line2D>();
	public ArrayList<Line2D> getShapeList() {return shapeList;}
	public void setShapeList(ArrayList<Line2D> shapeList) {this.shapeList = shapeList;}

	//set square shape
	public void setShapeBasic() {
		Line2D.Double one = new Line2D.Double(-2.0, 0, 2.0, 0);
		shapeList.add(one);
	}

	//mutate shape
	 public ArrayList<Line2D> mutateShape() {
	 	ArrayList<Line2D> newList = new ArrayList<Line2D>(this.getShapeList());
	 	int lineIndex = rand.nextInt(newList.size());
	 	boolean pIndex = rand.nextBoolean();
	 	int direction = rand.nextInt(4);
	 	double x, y;
	 	Point2D startPoint;
	 	Point2D newPoint;
	 	Line2D newLine = null;
	 	if(pIndex) {
	 		startPoint = newList.get(lineIndex).getP1();
	 		x = startPoint.getX();
	 		y = startPoint.getY();
	 	} else {
	 		startPoint = newList.get(lineIndex).getP2();
	 		x = startPoint.getX();
	 		y = startPoint.getY();
	 	}
	 	switch(direction) {
	 	case 0:
	 		if(rand.nextInt(2) < 1) {
	 			newPoint = new Point2D.Double(startPoint.getX(), startPoint.getY()-4);
	 		} else {
	 			newPoint = new Point2D.Double(startPoint.getX()-4, startPoint.getY()-4);
	 		}
	 		newLine = new Line2D.Double(startPoint, newPoint);
	 		break;
	 	case 1:
	 		if(rand.nextInt(2) < 1) {
	 			newPoint = new Point2D.Double(startPoint.getX(), startPoint.getY()+4);
	 		} else {
	 			newPoint = new Point2D.Double(startPoint.getX()+4, startPoint.getY()+4);
	 		}
	 		newLine = new Line2D.Double(startPoint, newPoint);
	 		break;
	 	case 2:
	 		if(rand.nextInt(2) < 1) {
	 			newPoint = new Point2D.Double(startPoint.getX()-4, startPoint.getY());
	 		} else {
	 			newPoint = new Point2D.Double(startPoint.getX()-4, startPoint.getY()+4);
	 		}
	 		newLine = new Line2D.Double(startPoint, newPoint);
	 		break;
	 	case 3:
	 		if(rand.nextInt(2) < 1) {
	 			newPoint = new Point2D.Double(startPoint.getX()+4, startPoint.getY());
	 		} else {
	 			newPoint = new Point2D.Double(startPoint.getX()+4, startPoint.getY()-4);
	 		}
	 		newLine = new Line2D.Double(startPoint, newPoint);
	 		break;
	 	}
	 	newList.add(newLine);
	 	return newList;
	 }

	//to check if the creature has a weird shape
	private boolean mutatedShapeCheck;
	public boolean getMutatedShapeCheck() {return mutatedShapeCheck;}
	public void setMutatedShapeCheck(boolean mutatedShapeCheck) {this.mutatedShapeCheck = mutatedShapeCheck;}

	//hunting/fleeing ability
	private double sight;
	private double si;
	public double getSight() {return sight;}
	public double getSi() {return si;}
	public void setSight(double sight) {this.sight = sight;}
	public void setSi(double si) {this.si = si;}
	public void incSight(double sight) {this.sight += sight;}
	public void incSi(double si) {this.si += si;}

	//motion biases
	private double lrB;
	private double udB;
	public double getlrB() {return lrB;}
	public double getudB() {return udB;}
	public void setlrB(double bias) {this.lrB = bias;}
	public void setudB(double bias) {this.udB = bias;}
	public void inclrB(double bias) {this.lrB += bias;}
	public void incudB(double bias) {this.udB += bias;}


	//sp is like si for speed
	private double sp;
	public double getSp() {return sp;}
	public void setSp(double sp) {this.sp = sp;}
	public void incSp(double sp) {this.sp += sp;}

	//rotation speed
	protected double rotVel;
	public double getRotationVelocity() {return rotVel;}
	public void setRotationVelocity(double v) {rotVel = v;}

	//number of mutations
	private int mutateNum;
	public int getMutateNum() {return mutateNum;}
	public void setMutateNum(int n) {this.mutateNum = n;}
	public void incMutateNum(int n) {this.mutateNum += n;}

	//generation number
	private int generation;
	public int getGeneration() {return generation;}
	public void setGeneration(int generation) {this.generation = generation;}

	//mass to be set after shape is mutated
	private double mass;
	public double getMass() {return mass;}
	public void setMass(double mass) {this.mass = mass;}

	//bounding rectangle
	//only used for basic shape creatures and canivore collisions
	public Rectangle getBounds() {
		Rectangle r;
		r = new Rectangle((int)getX()-1, (int)getY()-1, 4, 4);
		return r;
	}

	//default constructor
	Creature() {
		setColor(Color.WHITE);
		setShapeBasic();
		setRotationVelocity(0.0);
		setMutatedShapeCheck(false);
		setSight(1);
		setSi(1);
		setlrB(0);
		setudB(0);
		setSpeed(1);
		setSp(1);
		setMutateNum(0);
		setGeneration(0);
		setMass(1);
	}
}