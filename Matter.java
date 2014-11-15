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

import java.awt.*;
import java.awt.Rectangle;
import java.awt.geom.*;
import java.util.*;

public class Matter extends BaseVectorShape {
	//random number generator
	Random rand = new Random();

	private Color color;
	public Color getColor() {return color;}
	public void setColor(Color color) {this.color = color;}

	private double mass;
	public double getMass() {return mass;}
	public void setMass(double mass) {this.mass = mass;}

	//rotation speed
	protected double rotVel;
	public double getRotationVelocity() {return rotVel;}
	public void setRotationVelocity(double v) {rotVel = v;}

	public Rectangle getBounds() {
		Rectangle r;
		r = new Rectangle((int)getX()-2, (int)getY()-2, 4, 4);
		return r;
	}

	Matter() {
		setShape(new Line2D.Double(-2, 0, 2, 0));
		setColor(Color.DARK_GRAY);
		if(rand.nextBoolean()) {
			setRotationVelocity(rand.nextInt(30)+15);
		} else {
			setRotationVelocity(rand.nextInt(30)-45);
		}
	}
}