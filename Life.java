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
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

//primary game class
public class Life extends JFrame implements Runnable, MouseListener, KeyListener {

	//main thread becomes the game loop
	Thread gameloop;
	//back buffer
	BufferedImage backbuffer;
	//main drawing object for backbuffer
	Graphics2D g2d;

	//width/height of frame
	int width = 900;
	int height = 600;

	//create creature arraylist
	int CREATURES = 2;
	List<Creature> creature = Collections.synchronizedList(new ArrayList<Creature>());
	//create carnivore arraylist
	List<Carnivore> carnivore = Collections.synchronizedList(new ArrayList<Carnivore>());
	//create scavenger arraylist
	List<Scavenger> scavenger = Collections.synchronizedList(new ArrayList<Scavenger>());
	//create photon arraylist
	int PHOTONS = 2000;
	List<Photon> photon = Collections.synchronizedList(new ArrayList<Photon>());
	//create matter arraylist
	List<Matter> matter = Collections.synchronizedList(new ArrayList<Matter>());

	//create identity transform
	AffineTransform identity = new AffineTransform();

	//create random number generator
	Random rand = new Random();

	//number of frames past
	int dataNum = 0;

	//MouseListener variables
	int clickX, clickY;
	int mouseButton;

	//font for displaying data
	Font font = new Font("Courier", Font.PLAIN, 12);
	//best creature/carnivore
	Creature bestC = new Creature();
	Carnivore bestCa = new Carnivore();

	//frame rate counters and other timing variables
	int frameCount = 0, frameRate = 0;
	long startTime = System.currentTimeMillis();

	//toggles
	boolean data = false;
	boolean photonDraw = false;
	boolean sightCircle = false;
	boolean keyToggleOne = false;
	boolean keyToggleTwo = false;
	boolean keyToggleThree = false;

	public static void main(String[] args) {
		new Life();
	}

	//default constructor
	public Life() {
		super("Life_2.2");
		setSize(900,632); //32 is for JFrame top bar
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameloop = new Thread(this);
		gameloop.start();
		init();
	}

	//application init event
	public void init() {
		//populate arraylists
		for(int n=0; n<CREATURES; n++) {
			Creature c = new Creature();
			//set up creature variables
			c.setX((width/4)+(n*(width/2))); //separates them nicely
			c.setY(height/2);
			c.setAlive(true);
			c.setSpeed(1);
			c.setEnergy(150);
			creature.add(c);
		}
		for(int n=0; n<PHOTONS; n++) {
			Photon p = new Photon();
			//set up photon variables
			p.setX(rand.nextInt(width));
			p.setY(rand.nextInt(height));
			p.setAlive(true);
			p.setSpeed(rand.nextInt(2)+1);
			p.setEnergy(120);
			photon.add(p);
		}
		//create the backbuffer for smooth-ass graphics
		backbuffer = new BufferedImage(width+200, height, BufferedImage.TYPE_INT_RGB);
		g2d = backbuffer.createGraphics();
		//for mouse input
		addMouseListener(this);
		//for keyboard input
		addKeyListener(this);
	}

	//repaint event draws the backbuffer
	public void paint(Graphics g) {
		//draw the backbuffer to the window
		g.drawImage(backbuffer, 0, 29, this);
		//start off transforms at identity
		g2d.setTransform(identity);
		//erase the background
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, width, height);
		g2d.setColor(Color.WHITE);
		g2d.fillRect(900, 0, 0, height);
		//draw the things
		drawMatter();
		if(photonDraw) {
			drawPhotons();
		}
		drawCreatures();
		drawCarnivores();
		drawScavengers();
		if(data) {
			printData();
		}
	}

		//drawMatter called by update
	public void drawMatter() {
		if(matter.size() > 0) {
			for(int n=0;n<matter.size();n++) {
				Matter m = matter.get(n);
				if(m.isAlive()) {
					//draw the matter
					g2d.setTransform(identity);
					g2d.translate(m.getX(), m.getY());
					g2d.rotate(Math.toRadians(m.getFaceAngle()));
					g2d.setColor(m.getColor());
					g2d.draw(m.getShape());
				}
			}
		}
	}

	//drawPhotons called by update
	public void drawPhotons() {
		for(int n=0; n<photon.size(); n++) {
			Photon p = photon.get(n);
			if(p.isAlive()) {
				//draw the photon
				g2d.setTransform(identity);
				g2d.translate(p.getX(), p.getY());
				g2d.setColor(Color.GREEN);
				g2d.draw(p.getShape());
			}
		}
	}

	//drawCreatures called by update
	public void drawCreatures() {
		for(int n=0; n<creature.size(); n++) {
			Creature c = creature.get(n);
			if(c.isAlive()) {
				//draw the creature
				g2d.setTransform(identity);
				g2d.translate(c.getX(), c.getY());
				g2d.rotate(Math.toRadians(c.getMoveAngle()));
				g2d.setColor(c.getColor());
				//draw creature line by line
				for(int i=0;i<c.getShapeList().size();i++) {
					g2d.draw(c.getShapeList().get(i));
				}
			}
		}
	}

	//drawCarnivores called by update
	public void drawCarnivores() {
		if(carnivore.size() > 0) {
			for(int n=0; n<carnivore.size(); n++) {
				Carnivore ca = carnivore.get(n);
				if(ca.isAlive()) {
					//draw the carnivore
					g2d.setTransform(identity);
					g2d.translate(ca.getX(), ca.getY());
					g2d.rotate(Math.toRadians(ca.getMoveAngle()));
					if(sightCircle) {
						//sightRadius circle with R, G, B, Transparency
						g2d.setColor(new Color(0, 0, 1, 0.3f));
						g2d.fill(new Ellipse2D.Double(-ca.getSi()/2, -ca.getSi()/2, ca.getSi(), ca.getSi()));
					}
					g2d.setColor(ca.getColor());
					//draw carnivore line by line
					for(int i=0;i<ca.getShapeList().size();i++) {
						g2d.draw(ca.getShapeList().get(i));
					}
				}
			}
		}
	}

	public void drawScavengers() {
		if(scavenger.size() > 0) {
			for(int n=0;n<scavenger.size();n++) {
				Scavenger s = scavenger.get(n);
				if(s.isAlive()) {
					//draw the scavenger
					g2d.setTransform(identity);
					g2d.translate(s.getX(), s.getY());
					if(sightCircle) {
						//sightRadius circle with R, G, B, Transparency
						g2d.setColor(new Color(0, 0, 1, 0.3f));
						g2d.fill(new Ellipse2D.Double(-s.getSi()/2, -s.getSi()/2, s.getSi(), s.getSi()));
					}
					g2d.setColor(s.getColor());
					for(int i=0;i<s.getShapeList().size();i++) {
						g2d.draw(s.getShapeList().get(i));
					}
				}
			}
		}
	}

	public void printData() {
		g2d.setTransform(identity);
		//indent
		g2d.translate(15, 10);
		g2d.setColor(new Color(0, 0, 1, 0.5f));
		g2d.setFont(font);
		//creature/carnivore ratio bar
		g2d.setColor(new Color(1, 1, 1, 0.5f));
		Rectangle2D white = new Rectangle2D.Double(0, 0, creature.size()*(200/(creature.size()+carnivore.size())), 20);
		g2d.fill(white);
		g2d.setColor(new Color(1, 0, 0, 0.5f));
		Rectangle2D red = new Rectangle2D.Double(creature.size()*(200/(creature.size()+carnivore.size())), 0, carnivore.size()*(200/(creature.size()+carnivore.size())), 20);
		g2d.fill(red);
		//set font
		g2d.setColor(new Color(0, 1, 0.5f, 0.5f));
		g2d.setFont(font);
		//populations
		g2d.translate(0, 30);
		g2d.drawString("Creatures: "+creature.size(), 5, 0);
		g2d.drawString("Carnivores: "+carnivore.size(), 5, 15);
		g2d.drawString("Scavengers: "+scavenger.size(), 5, 30);
		g2d.drawString("Photons: "+photon.size(), 5, 45);
		//frameRate
		g2d.drawString("FPS: "+frameRate, 5, 60);
		//best creature
		g2d.translate(0, 90);
		g2d.drawString("Best Creature:", 5, 0);
		g2d.drawString("Position: "+bestC.getX()+", "+bestC.getY(), 5, 15);
		g2d.drawString("MutatedShapeCheck: "+bestC.getMutatedShapeCheck(), 5, 30);
		g2d.drawString("Sight: "+bestC.getSi(), 5, 45);
		g2d.drawString("speed: "+bestC.getSp(), 5, 60);
		g2d.drawString("Up/Down Bias: "+bestC.getudB(), 5, 75);
		g2d.drawString("Left/Right Bias: "+bestC.getlrB(), 5, 90);
		g2d.drawString("Number of Mutations: "+bestC.getMutateNum(), 5, 105);
		 //draw best creature
		 g2d.translate(60, 165);
		 g2d.setColor(new Color(0, 1, 0.5f, 1.0f));
		for(int n=0;n<bestC.getShapeList().size();n++) {
			Line2D temp = bestC.getShapeList().get(n);
			double x1 = temp.getP1().getX()*4;
			double y1 = temp.getP1().getY()*4;
			double x2 = temp.getP2().getX()*4;
			double y2 = temp.getP2().getY()*4;
			Line2D big = new Line2D.Double(new Point2D.Double(x1, y1), new Point2D.Double(x2, y2));
			g2d.draw(big);
		}

		//best carnivore
		g2d.translate(-60, 75);
		g2d.setColor(new Color(0, 1, 0.5f, 0.5f));
		g2d.drawString("Best Carnivore:", 5, 0);
		g2d.drawString("Position: "+bestCa.getX()+", "+bestCa.getY(), 5, 15);
		g2d.drawString("MutatedShapeCheck: "+bestCa.getMutatedShapeCheck(), 5, 30);
		g2d.drawString("Sight: "+bestCa.getSi(), 5, 45);
		g2d.drawString("speed: "+bestCa.getSp(), 5, 60);
		g2d.drawString("Up/Down Bias: "+bestCa.getudB(), 5, 75);
		g2d.drawString("Left/Right Bias: "+bestCa.getlrB(), 5, 90);
		g2d.drawString("Number of Mutations: "+bestCa.getMutateNum(), 5, 105);
		//draw best carnivore
		g2d.translate(60, 165);
		g2d.setColor(new Color(0, 1, 0.5f, 1.0f));
		for(int n=0;n<bestCa.getShapeList().size();n++) {
			Line2D temp = bestCa.getShapeList().get(n);
			double x1 = temp.getP1().getX()*4;
			double y1 = temp.getP1().getY()*4;
			double x2 = temp.getP2().getX()*4;
			double y2 = temp.getP2().getY()*4;
			Line2D big = new Line2D.Double(new Point2D.Double(x1, y1), new Point2D.Double(x2, y2));
			g2d.draw(big);
		}
	}

	public void run() {
		//aquire the current thread
		Thread t = Thread.currentThread();
		//keep going as long as the thread is alive
		while(t == gameloop) {
			try {
				gameUpdate();
				//target framerate is 50fps
				Thread.sleep(20);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			repaint();
		}
	}

	//stop thread event
	public void stop() {
		//kill the gameloop thread
		gameloop = null;
	}

	//move and animate objects in the game
	public void gameUpdate() {
		updatePhotons();
		updateMatter();
		updateCreatures();
		updateCarnivores();
		updateScavengers();
		checkCollisions();
		deadCollect();
		spawnPhotons();
		spawnCreatureCheck();
		resetCheck();
		dataNum++;
		calcFrameRate();
	}

	//update photon positions
	public void updatePhotons() {
		for(int n=0; n<photon.size(); n++) {
			Photon p = photon.get(n);
			if(p.isAlive()) {
				//update Y
				p.incY(p.getSpeed());
				//wrap top/bottom
				if(p.getY() <= 0) {
					p.setY(height);
				} else if(p.getY() >= height) {
					p.setY(0);
				}
			}
		}
	}

	//update matter energy
	public void updateMatter() {
		for(int n=0;n<matter.size();n++) {
			Matter m = matter.get(n);
			if(m.isAlive()) {
				//rotate
				m.setFaceAngle(m.getFaceAngle()+m.getRotationVelocity());
				//drift down
				m.incY(1);
				//wrap top/bottom
				if(m.getY() <= 0) {
					m.setY(height);
				} else if(m.getY() >= height) {
					m.setY(0);
				}
				//wrap left/right
				if(m.getX() <= 0) {
					m.setX(width);
				} else if(m.getX() >= width) {
					m.setX(0);
				}
				//decrease energy
				if(m.isAlive()) {
					m.decEnergy(m.getMass()/3);
				}
				//kill
				if(m.getEnergy() <= 0) {
					m.setAlive(false);
				}
			}
		}
	}

	//update creature positions
	public void updateCreatures() {
		for(int n=0; n<creature.size(); n++) {
			Creature c = creature.get(n);
			if(c.isAlive()) {
				//update Y
				if(rand.nextInt(31)+1 < 9+c.getudB()) {
					//if bias is enough
					c.incY(c.getSp());
				} else if(rand.nextInt(31)+1 > 20+c.getudB()) {
					//if bias is enough
					c.incY(-c.getSp());
				} else {
					//random move
					if(rand.nextBoolean()) {
						c.incY(c.getSp());
					} else {
						c.incY(-c.getSp());
					}
				}
				//update X
				if(rand.nextInt(31)+1 < 9+c.getlrB()) {
					//if bias is enough
					c.incX(c.getSp());
				} else if(rand.nextInt(31)+1 > 20+c.getlrB()) {
					//if bias is enough
					c.incX(-c.getSp());
				} else {
					//random move
					if(rand.nextBoolean()) {
						c.incX(c.getSp());
					} else {
						c.incX(-c.getSp());
					}
				}
				//wrap top/bottom
				if(c.getY() <=0) {
					c.setY(height);
				} else if(c.getY() >= height) {
					c.setY(0);
				}
				//wrap left/right
				if(c.getX() <= 0) {
					c.setX(width);
				} else if(c.getX() >= width) {
					c.setX(0);
				}
				//decrease energy
				if(!c.isDebug()) {
					c.decEnergy(1+(0.3*c.getMass())+(0.1*c.getSight()));
				}
				//kill
				if(c.getEnergy() <= 0) {
					c.setAlive(false);
					leaveMatter(c.getX(), c.getY(), c.getMass());
				}
			}
		}
	}

	//update carnivore positions
	public void updateCarnivores() {
		double targetX = 0;
		double targetY = 0;
		for(int i=0;i<carnivore.size();i++) {
			Carnivore ca = carnivore.get(i);
			//some random motion in carnivore movement
			if(rand.nextInt(4) <=2 || ca.isDebug()) {
				double best = ca.getSi();
				int otherwise = 0;
				for(int j=0;j<creature.size();j++) {
					Creature c = creature.get(j);
					double distance = getDistance(ca.getX(), ca.getY(), c.getX(), c.getY());
					if(distance < best) {
						ca.setXRef(calcRef(ca.getX(), c.getX(), width));
						ca.setYRef(calcRef(ca.getY(), c.getY(), height));
						targetX = c.getX();
						targetY = c.getY();
						best = distance;
					} else {
						otherwise++;
					}
				}
				if(otherwise < creature.size()) {
					if(Math.abs(ca.getX() - targetX) >= 3) {
						ca.incX(ca.getXRef()*ca.getSp());
					}
					if(Math.abs(ca.getY() - targetY) >= 3) {
						ca.incY(ca.getYRef()*ca.getSp());
					}
				} else if(!ca.isDebug()) {
					randomMove(ca);
				}
			} else if(!ca.isDebug()) {
				randomMove(ca);
			}

			//wrap top/bottom
			if(ca.getY() <=0) {
				ca.setY(height);
			} else if(ca.getY() >= height) {
				ca.setY(0);
			}
			//wrap left/right
			if(ca.getX() <= 0) {
				ca.setX(width);
			} else if(ca.getX() >= width) {
				ca.setX(0);
			}
			//decrease energy
			ca.decEnergy(1+(0.3*ca.getMass())+(0.1*ca.getSight()));
			if(ca.getEnergy() <= 0 && !ca.isDebug()) {
				ca.setAlive(false);
				leaveMatter(ca.getX(), ca.getY(), ca.getMass());
			}
		}
	}

	//update scavenger positions
	public void updateScavengers() {
		double targetX = 0;
		double targetY = 0;
		for(int i=0;i<scavenger.size();i++) {
			Scavenger s = scavenger.get(i);
			//some random motion in scavenger movement
			if(rand.nextInt(4) <=2 || s.isDebug()) {
				double best = s.getSi();
				int otherwise = 0;
				for(int j=0;j<matter.size();j++) {
					Matter m = matter.get(j);
					double distance = getDistance(s.getX(), s.getY(), m.getX(), m.getY());
					if(distance < best) {
						s.setXRef(calcRef(s.getX(), m.getX(), width));
						s.setYRef(calcRef(s.getY(), m.getY(), height));
						targetX = m.getX();
						targetY = m.getY();
						best = distance;
					} else {
						otherwise++;
					}
				}
				if(otherwise < matter.size()) {
					if(Math.abs(s.getX() - targetX) >= 3) {
						s.incX(s.getXRef()*s.getSp());
					}
					if(Math.abs(s.getY() - targetY) >= 3) {
						s.incY(s.getYRef()*s.getSp());
					}
				} else if(!s.isDebug()) {
					randomMove(s);
				}
			} else if(!s.isDebug()) {
				randomMove(s);
			}

			//wrap top/bottom
			if(s.getY() <=0) {
				s.setY(height);
			} else if(s.getY() >= height) {
				s.setY(0);
			}
			//wrap left/right
			if(s.getX() <= 0) {
				s.setX(width);
			} else if(s.getX() >= width) {
				s.setX(0);
			}
			//decrease energy
			s.decEnergy(1+(0.3*s.getMass())+(0.1*s.getSight()));
			if(s.getEnergy() <= 0 && !s.isDebug()) {
				s.setAlive(false);
			}
		}
	}

	//do random motion for carnivores
	public void randomMove(Carnivore ca) {
		//update Y randomly
		if(rand.nextInt(31)+1 < 9+ca.getudB()) {
			//if bias is enough
			ca.incY(ca.getSp());
		} else if(rand.nextInt(31)+1 > 20+ca.getudB()){
			//if bias is enough
			ca.incY(-ca.getSp());
		} else {
			//random move
			if(rand.nextBoolean()) {
				ca.incX(ca.getSp());
			} else {
				ca.incX(-ca.getSp());
			}
		}
		//update X randomly
		if(rand.nextInt(31)+1 < 9+ca.getlrB()) {
			//if bias is enough
			ca.incX(ca.getSpeed());
		} else if(rand.nextInt(31)+1 > 20+ca.getlrB()) {
			//if bias is enough
			ca.incX(-ca.getSpeed());
		} else {
			//random move
			if(rand.nextBoolean()) {
				ca.incX(ca.getSpeed());
			} else {
				ca.incX(-ca.getSpeed());
			}
		}
	}
	//do random motion for scavengers
	public void randomMove(Scavenger s) {
		//update Y randomly
		if(rand.nextInt(31)+1 < 9+s.getudB()) {
			//if bias is enough
			s.incY(s.getSp());
		} else if(rand.nextInt(31)+1 > 20+s.getudB()){
			//if bias is enough
			s.incY(-s.getSp());
		} else {
			//random move
			if(rand.nextBoolean()) {
				s.incX(s.getSp());
			} else {
				s.incX(-s.getSp());
			}
		}
		//update X randomly
		if(rand.nextInt(31)+1 < 9+s.getlrB()) {
			//if bias is enough
			s.incX(s.getSpeed());
		} else if(rand.nextInt(31)+1 > 20+s.getlrB()) {
			//if bias is enough
			s.incX(-s.getSpeed());
		} else {
			//random move
			if(rand.nextBoolean()) {
				s.incX(s.getSpeed());
			} else {
				s.incX(-s.getSpeed());
			}
		}
	}

	//method to calculate distance between two things
	public double getDistance(double x1, double y1, double x2, double y2) { //1=Ca, 2=C
		double xSeparation = Math.abs(x1 - x2);
		double ySeparation = Math.abs(y1 - y2);
		//look through walls
		if(Math.abs(x1 - x2) > width/2) {
			xSeparation = width - xSeparation;
		}
		if(Math.abs(y1 - y2) > height/2) {
			ySeparation = height - ySeparation;
		}
		double totalSeparation = Math.sqrt(Math.pow(xSeparation, 2) + Math.pow(ySeparation, 2));
		return totalSeparation;
	}
	//method to figure out proper directions
	public int calcRef(double ca, double c, double size) {
		if((ca < c && Math.abs(ca-c) < size/2) || (ca > c && Math.abs(ca-c) > size/2)) {
			return 1;
		} else if((ca < c && Math.abs(ca-c) > size/2) || (ca > c && Math.abs(ca-c) < size/2)) {
			return -1;
		} else {
			return 0;
		}
	}

	//when a creature/carnivore dies it leaves behind matter
	public void leaveMatter(double x, double y, double mass) {
		Matter m = new Matter();
		m.setX(x);
		m.setY(y);
		m.setMass(mass);
		m.setEnergy(75*mass);
		m.setAlive(true);
		matter.add(m);
	}

	//check collisions
	public void checkCollisions() {
		//creatures vs photons
		synchronized (creature) {
			Iterator<Creature> it = creature.iterator();
			while(it.hasNext()) {
				Creature c = (Creature)it.next();
				if(c.isAlive()) {
					synchronized (photon) {
						Iterator<Photon> itToo = photon.iterator();
						while(itToo.hasNext()) {
							Photon p = (Photon)itToo.next();
							if(p.isAlive()) {
								//only check photon if it is within 20px of creature
								if(Math.abs(c.getX()-p.getX()) < 20 && Math.abs(c.getY()-p.getY()) < 20) {
									if(edible(c, p)) {
										//remove the photon and absorb the energy
										p.setAlive(false);
										c.incEnergy(p.getEnergy());
										itToo.remove();
									}
								}
							}
						}
					}
				}
			}
		}
		//carnivores vs creatures
		synchronized (carnivore) {
			Iterator<Carnivore> carn = carnivore.iterator();
			while(carn.hasNext()) {
				Carnivore ca = (Carnivore)carn.next();
				if(ca.isAlive()) {
					synchronized (creature) {
						Iterator<Creature> prey = creature.iterator();
						while(prey.hasNext()) {
							Creature cr = (Creature)prey.next();
							if(cr.isAlive()) {
								if(ca.getBounds().intersects(cr.getBounds())) {
									//remove creature and absorb energy
									cr.setAlive(false);
									ca.incEnergy(cr.getEnergy());
									prey.remove();
								}
							}
						}
					}
				}
			}
		}
		//scanvengers vs matter
		synchronized (scavenger) {
			Iterator<Scavenger> scav = scavenger.iterator();
			while(scav.hasNext()) {
				Scavenger s = (Scavenger)scav.next();
				if(s.isAlive()) {
					synchronized (matter) {
						Iterator<Matter> matt = matter.iterator();
						while(matt.hasNext()) {
							Matter m = (Matter)matt.next();
							if(m.isAlive()) {
								if(s.getBounds().intersects(m.getBounds())) {
									//eat some energy
									m.setAlive(false);
									s.incEnergy(m.getEnergy());
									matt.remove();
								}
							}
						}
					}
				}
			}
		}
	}

	//custom method for checking if photons can be eaten by creatures
	public boolean edible(Creature c, Photon p) {
		boolean answer = false;
		for(int n=0;n<c.getShapeList().size();n++) {
			Line2D line = c.getShapeList().get(n);
			Point2D p1 = new Point2D.Double(line.getP1().getX()+c.getX(), line.getP1().getY()+c.getY());
			Point2D p2 = new Point2D.Double(line.getP2().getX()+c.getX(), line.getP2().getY()+c.getY());
			Line2D tempLine = new Line2D.Double(p1, p2);
			if(tempLine.intersects(p.getBounds())) {
				answer = true;
				break;
			}
		}
		return answer;
	}

	public void deadCollect() {
		synchronized (creature) {
			Iterator<Creature> it = creature.iterator();
			while(it.hasNext()) {
				Creature c = (Creature)it.next();
				if(!c.isAlive()) {
					it.remove();
				}
			}
		}
		synchronized (carnivore) {
			Iterator<Carnivore> itToo = carnivore.iterator();
			while(itToo.hasNext()) {
				Carnivore ca = (Carnivore)itToo.next();
				if(!ca.isAlive()) {
					itToo.remove();
				}
			}
		}
		synchronized (matter) {
			Iterator<Matter> itThree = matter.iterator();
			while(itThree.hasNext()) {
				Matter m = (Matter)itThree.next();
				if(!m.isAlive()) {
					itThree.remove();
				}
			}
		}
		synchronized (scavenger) {
			Iterator<Scavenger> itFour = scavenger.iterator();
			while(itFour.hasNext()) {
				Scavenger s = (Scavenger)itFour.next();
				if(!s.isAlive()) {
					itFour.remove();
				}
			}
		}
	}

	public void spawnPhotons() {
		double r = ((Math.sin(Math.toRadians(dataNum/5)))+3)*4;
		//if(r == 20) {System.out.println("Summer");}
		//if(r == 0) {System.out.println("Winter");}
		//System.out.println(r);
		//System.out.println(photon.size());
		if(photon.size() < 2000) {
			for(int n=0;n<r;n++) {
				Photon p = new Photon();
				//set up photon variables
				p.setX(rand.nextInt(width));
				p.setY(rand.nextInt(height));
				p.setAlive(true);
				p.setSpeed(rand.nextInt(2)+1);
				p.setEnergy(120);
				photon.add(p);
			}
		}
	}

	public void spawnCreatureCheck() {
		for(int n=0;n<creature.size();n++) {
			Creature c = creature.get(n);
			if(c.getEnergy() > 250) {
				spawnChild(c);
			}
		}
		for(int n=0;n<carnivore.size();n++) {
			Carnivore ca = carnivore.get(n);
			if(ca.getEnergy() > 300) {
				spawnChild(ca);
			}
		}
		for(int n=0;n<scavenger.size();n++) {
			Scavenger s = scavenger.get(n);
			if(s.getEnergy() > 300) {
				spawnChild(s);
			}
		}
	}

	public void spawnChild(Creature c_) {
		if(rand.nextInt(500) <= 1) {
			Carnivore ca = new Carnivore();
			//standard genetics
			ca.setX(c_.getX());
			ca.setY(c_.getY());
			ca.setAlive(true);
			ca.setSpeed(c_.getSpeed());
			ca.setEnergy(150);
			ca.setSight(c_.getSight());
			ca.setSi(c_.getSi());
			ca.setShapeList(c_.getShapeList());
			ca.setMutatedShapeCheck(c_.getMutatedShapeCheck());
			ca.setlrB(c_.getlrB());
			ca.setudB(c_.getudB());
			ca.setMutateNum(c_.getMutateNum());
			ca.setGeneration(c_.getGeneration()+1);
			//calculate mass based on size
			ca.setMass(ca.getShapeList().size());
			//check if this is the best carnivore
			if(ca.getMutateNum() > bestC.getMutateNum()) {
				updateBest(ca);
			}
			carnivore.add(ca);
		} else {
			Creature c = new Creature();
			c.setColor(c_.getColor());
			//standard genetics
			c.setX(c_.getX());
			c.setY(c_.getY());
			c.setAlive(true);
			c.setSpeed(c_.getSpeed());
			c.setEnergy(150);
			c.setSight(c_.getSight());
			c.setShapeList(c_.getShapeList());
			c.setMutatedShapeCheck(c_.getMutatedShapeCheck());
			c.setlrB(c_.getlrB());
			c.setudB(c_.getudB());
			c.setMutateNum(c_.getMutateNum());
			c.setGeneration(c_.getGeneration()+1);
			//mutations
			//shape
			if(rand.nextInt(50) <= 1) {
				c.setShapeList(c_.mutateShape());
				c.setMutatedShapeCheck(true);
				c.incMutateNum(1);
			}
			//calculate mass based on size
			c.setMass(c.getShapeList().size());
			// //speed
			// if(rand.nextInt(60) <= 1) {
			// 	c.incSpeed(1);
			// 	c.setSp(3-(2/Math.sqrt(c_.getSpeed())));
			// 	c.incMutateNum(1);
			// }
			//sight
			if(rand.nextInt(15) <= 1) {
				c.incSight(1);
				c.setSi(30-(29/Math.sqrt(c_.getSight())));
				c.incMutateNum(1);
			}
			// movement biases
			if(rand.nextInt(10) <= 1) {
				//left/right
				if(rand.nextBoolean() && c.getlrB() < 10) {
					c.inclrB(1);
					c.incMutateNum(1);
				} else if(c.getlrB() > -10) {
					c.inclrB(-1);
					c.incMutateNum(1);
				}
			}
			if(rand.nextInt(10) <= 1) {
				//up/down
				if(rand.nextBoolean() && c.getudB() < 10) {
					c.incudB(1);
					c.incMutateNum(1);
				} else if(c.getudB() > -10) {
					c.incudB(-1);
					c.incMutateNum(1);
				}
			}
			//check if this is the best creature
			if(c.getMutateNum() > bestC.getMutateNum()) {
				updateBest(c);
			}
			creature.add(c);
		}
		//reduce parent energy
		c_.setEnergy(150);
	}

	public void spawnChild(Carnivore ca_) {
		if(rand.nextInt(100) <= 1) {
			Scavenger s = new Scavenger();
			//standard genetics
			s.setX(ca_.getX());
			s.setY(ca_.getY());
			s.setAlive(true);
			s.setSpeed(ca_.getSpeed());
			s.setEnergy(150);
			s.setSight(ca_.getSight());
			s.setSi(ca_.getSi());
			s.setShapeList(ca_.getShapeList());
			s.setMutatedShapeCheck(ca_.getMutatedShapeCheck());
			s.setlrB(ca_.getlrB());
			s.setudB(ca_.getudB());
			s.setMutateNum(ca_.getMutateNum());
			s.setGeneration(ca_.getGeneration()+1);
			scavenger.add(s);
		} else {
			Carnivore ca = new Carnivore();
			//standard genetics
			ca.setX(ca_.getX());
			ca.setY(ca_.getY());
			ca.setAlive(true);
			ca.setSpeed(ca_.getSpeed());
			ca.setEnergy(150);
			ca.setSight(ca_.getSight());
			ca.setSi(ca_.getSi());
			ca.setShapeList(ca_.getShapeList());
			ca.setMutatedShapeCheck(ca_.getMutatedShapeCheck());
			ca.setlrB(ca_.getlrB());
			ca.setudB(ca_.getudB());
			ca.setMutateNum(ca_.getMutateNum());
			ca.setGeneration(ca_.getGeneration()+1);
			//mutations
			//shape
			if(rand.nextInt(25) <= 1) {
				ca.setShapeList(ca_.mutateShape());
				ca.setMutatedShapeCheck(true);
				ca.incMutateNum(1);
			}
			//calculate mass based on size
			ca.setMass(ca.getShapeList().size());
			// //speed
			// if(rand.nextInt(60) <= 1) {
			// 	ca.incSpeed(1);
			// 	ca.incMutateNum(1);
			// 	ca.setSp(3-(2/Math.sqrt(ca_.getSpeed())));
			// }
			//sight
			if(rand.nextInt(15) <= 1) {
				ca.incSight(1);
				ca.incMutateNum(1);
				ca.setSi(30-(29/Math.sqrt(ca_.getSight())));
			}
			//movement biases
			if(rand.nextInt(10) <= 1) {
				if(rand.nextBoolean() && ca.getlrB() < 10) {
					ca.inclrB(1);
					ca.incMutateNum(1);
				} else if(ca.getlrB() > -10) {
					ca.inclrB(1);
					ca.incMutateNum(1);
				}
			}
			if(rand.nextInt(10) <= 1) {
				if(rand.nextBoolean() && ca.getudB() < 10) {
					ca.incudB(1);
					ca.incMutateNum(1);
				} else if(ca.getudB() > -10) {
					ca.incudB(-1);
					ca.incMutateNum(1);
				}
			}
			//check if this is the best carnivore
			if(ca.getMutateNum() > bestCa.getMutateNum()) {
				updateBest(ca);
			}
			carnivore.add(ca);
		}
		//reduce parent energy
		ca_.setEnergy(150);
	}

	public void spawnChild(Scavenger s_) {
		if(rand.nextInt(100) <= 1) {
			Creature c = new Creature();
			//make it obvious it has gone full circle
			c.setColor(new Color(0.3f, 0.3f, 1, 1));
			//standard genetics
			c.setX(s_.getX());
			c.setY(s_.getY());
			c.setAlive(true);
			c.setSpeed(s_.getSpeed());
			c.setEnergy(150);
			c.setSight(s_.getSight());
			c.setShapeList(s_.getShapeList());
			c.setMutatedShapeCheck(s_.getMutatedShapeCheck());
			c.setlrB(s_.getlrB());
			c.setudB(s_.getudB());
			c.setMutateNum(s_.getMutateNum());
			c.setGeneration(s_.getGeneration()+1);
			creature.add(c);
		} else {
			Scavenger s = new Scavenger();
			//standard genetics
			s.setX(s_.getX());
			s.setY(s_.getY());
			s.setAlive(true);
			s.setSpeed(s_.getSpeed());
			s.setEnergy(150);
			s.setSight(s_.getSight());
			s.setSi(s_.getSi());
			s.setShapeList(s_.getShapeList());
			s.setMutatedShapeCheck(s_.getMutatedShapeCheck());
			s.setlrB(s_.getlrB());
			s.setudB(s_.getudB());
			s.setMutateNum(s_.getMutateNum());
			s.setGeneration(s_.getGeneration()+1);
			//mutations
			//shape
			if(rand.nextInt(25) <= 1) {
				s.setShapeList(s_.mutateShape());
				s.setMutatedShapeCheck(true);
				s.incMutateNum(1);
			}
			//calculate mass based on size
			s.setMass(s.getShapeList().size());
			// //speed
			// if(rand.nextInt(60) <= 1) {
			// 	s.incSpeed(1);
			// 	s.incMutateNum(1);
			// 	s.setSp(3-(2/Math.sqrt(s_.getSpeed())));
			// }
			//sight
			if(rand.nextInt(15) <= 1) {
				s.incSight(1);
				s.incMutateNum(1);
				s.setSi(30-(29/Math.sqrt(s_.getSight())));
			}
			//movement biases
			if(rand.nextInt(10) <= 1) {
				if(rand.nextBoolean() && s.getlrB() < 10) {
					s.inclrB(1);
					s.incMutateNum(1);
				} else if(s.getlrB() > -10) {
					s.inclrB(1);
					s.incMutateNum(1);
				}
			}
			if(rand.nextInt(10) <= 1) {
				if(rand.nextBoolean() && s.getudB() < 10) {
					s.incudB(1);
					s.incMutateNum(1);
				} else if(s.getudB() > -10) {
					s.incudB(-1);
					s.incMutateNum(1);
				}
			}
			scavenger.add(s);
		}
		//reduce parent energy
		s_.setEnergy(150);
	}

	 //update the best creature/carnivore
	 public void updateBest(Creature c) {
		bestC.setX(c.getX());
		bestC.setY(c.getY());
		bestC.setAlive(false);
		bestC.setSpeed(c.getSpeed());
		bestC.setEnergy(150);
		bestC.setSight(c.getSight());
		bestC.setShapeList(c.getShapeList());
		bestC.setMutatedShapeCheck(c.getMutatedShapeCheck());
		bestC.setlrB(c.getlrB());
		bestC.setudB(c.getudB());
		bestC.setMutateNum(c.getMutateNum());
		bestC.setGeneration(c.getGeneration());
		bestC.setMass(c.getMass());
	 }
	 public void updateBest(Carnivore ca) {
		bestCa.setX(ca.getX());
		bestCa.setY(ca.getY());
		bestCa.setAlive(false);
		bestCa.setSpeed(ca.getSpeed());
		bestCa.setEnergy(150);
		bestCa.setSight(ca.getSight());
		bestCa.setSi(ca.getSi());
		bestCa.setShapeList(ca.getShapeList());
		bestCa.setMutatedShapeCheck(ca.getMutatedShapeCheck());
		bestCa.setlrB(ca.getlrB());
		bestCa.setudB(ca.getudB());
		bestCa.setMutateNum(ca.getMutateNum());
		bestCa.setGeneration(ca.getGeneration());
		bestCa.setMass(ca.getMass());
	 }

	//check if there are no more creatures and restart if so
	public void resetCheck() {
		if(creature.size() == 0 && carnivore.size() == 0) {
			Creature c = new Creature();
			//set up creature variables
			c.setX(rand.nextInt(width));
			c.setY(rand.nextInt(height));
			c.setAlive(true);
			c.setSpeed(1);
			c.setEnergy(150);
			creature.add(c);
		}
	}

	public void calcFrameRate() {
		//calculate frame rate
		frameCount++;
		if(System.currentTimeMillis() > startTime+1000) {
			startTime = System.currentTimeMillis();
			frameRate = frameCount;
			frameCount = 0;
		}
	}

	//custom method to get button status
	public void checkButton(MouseEvent e) {
		switch(e.getButton()) {
			case MouseEvent.BUTTON1:
				mouseButton = 1;
				break;
			case MouseEvent.BUTTON2:
				mouseButton = 2;
				break;
			case MouseEvent.BUTTON3:
				mouseButton = 3;
				break;
			default:
				mouseButton = 0;
		}
	}

	//MouseListener methods
	public void mouseClicked(MouseEvent e) {
		clickX = e.getX();
		clickY = e.getY();
		checkButton(e);
		if(mouseButton == 1) {
			if(keyToggleOne) {
				Creature c = new Creature();
				//set up variables
				c.setX(clickX);
				c.setY(clickY-32);
				c.setAlive(true);
				c.setEnergy(150);
				creature.add(c);
			}
			if(keyToggleTwo) {
				Carnivore ca = new Carnivore();
				//set up variables
				ca.setX(clickX);
				ca.setY(clickY-32);
				ca.setAlive(true);
				ca.setSp(3);
				ca.setEnergy(299);
				ca.setSight(1);
				ca.setSi(width);
				ca.setDebug(true);
				carnivore.add(ca);
			}
			if(keyToggleThree) {
				Scavenger s = new Scavenger();
				//set up variables
				s.setX(clickX);
				s.setY(clickY-32);
				s.setAlive(true);
				s.setEnergy(150);
				scavenger.add(s);
			}
		} else if(mouseButton == 2) {
			creature.clear();
			carnivore.clear();
			scavenger.clear();
		} else if(mouseButton == 3) {
			
		}
	}
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public void mouseEntered(MouseEvent e) { }

	public void keyPressed(KeyEvent k) {
		int keycode = k.getKeyCode();
		switch(keycode) {
		case KeyEvent.VK_D:
			data = !data;
			break;
		case KeyEvent.VK_P:
			photonDraw = !photonDraw;
			break;
		case KeyEvent.VK_S:
			sightCircle = !sightCircle;
			break;
		case KeyEvent.VK_1:
			keyToggleOne = true;
			break;
		case KeyEvent.VK_2:
			keyToggleTwo = true;
			break;
		case KeyEvent.VK_3:
			keyToggleThree = true;
			break;
		}
	}
	public void keyReleased(KeyEvent k) {
		int keycode = k.getKeyCode();
		switch(keycode) {
		case KeyEvent.VK_1:
			keyToggleOne = false;
			break;
		case KeyEvent.VK_2:
			keyToggleTwo = false;
			break;
		case KeyEvent.VK_3:
			keyToggleThree = false;
			break;
		}
	}
	public void keyTyped(KeyEvent k) { }
}