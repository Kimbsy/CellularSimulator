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
import java.awt.geom.*;
import java.util.*;

public class Carnivore extends Creature {
	//details of prey
	private int xRef;
	private int yRef;

	//accessor/mutator
	public int getXRef() {return xRef;}
	public void setXRef(int xRef) {this.xRef = xRef;}
	public int getYRef() {return yRef;}
	public void setYRef(int yRef) {this.yRef = yRef;}

	//sight radius ellipse
	private Ellipse2D sightRadius;
	public Ellipse2D getSightRadius() {return sightRadius;}
	public void setSightRadius(Ellipse2D sightRadius) {this.sightRadius = sightRadius;}

	//default constructor
	Carnivore() {
		setColor(Color.RED);
		setXRef(1);
		setYRef(1);
		setSightRadius(new Ellipse2D.Double(this.getX(), this.getY(), this.getSi()/2, this.getSi()/2));
	}
}