/*
 *  Tiled Map Editor, (c) 2004
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 * 
 *  Adam Turk <aturk@biggeruniverse.com>
 *  Bjorn Lindeijer <b.lindeijer@xs4all.nl>
 */

package tiled.core;

import java.awt.Rectangle;
import java.util.*;

public class MultilayerPlane {
	private Vector layers;
	protected int widthInTiles = 0, heightInTiles = 0;
	
	public MultilayerPlane() {
		init();
	}
	
	public MultilayerPlane(int width, int height) {
		init();
		widthInTiles = width;
		heightInTiles = height;
	}
	
	private void init() {
			
		layers = new Vector();
	}
	
	public int getTotalLayers() {
		int totalLayers=0;
		Iterator itr = layers.iterator();
		while(itr.hasNext()) {
			if(itr.next() != null) {
				totalLayers++;
			}
		}
		return totalLayers;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(0,0,widthInTiles,heightInTiles);
	}
	
	/**
	 * Adds a layer to the map.
	 */
	public MapLayer addLayer(MapLayer l) {
		layers.add(l);
		return l;
	}

	/**
	 * Adds the MapLayer <code>l</code> after the MapLayer <code>after</code>.
	 * 
	 * @param l
	 * @param after
	 */
	public void addLayerAfter(MapLayer l, MapLayer after) {
		layers.add(layers.indexOf(after)+1,l);
	}

	public void addAllLayers(Collection c) {
		layers.addAll(c);
	}
	
	/**
	 * Removes the layer at the specified index. Layers above this layer
	 * will move down to fill the gap.
	 *
	 * @param index Index of layer to be removed.
	 */
	public MapLayer removeLayer(int index) {
		MapLayer layer = (MapLayer)layers.remove(index);
		return layer;
	}
	
	public void removeAllLayers() {
		layers.removeAllElements();
	}
	
	public void swapLayerUp(int index) throws Exception {
		if (index + 1 == layers.size()) {
			throw new Exception(
					"Can't swap up when already at the top.");
		}

		MapLayer hold = (MapLayer)layers.get(index + 1);
		layers.set(index + 1, getLayer(index));
		layers.set(index, hold);
	}

	public void swapLayerDown(int index) throws Exception {
		if (index - 1 < 0) {
			throw new Exception(
					"Can't swap down when already at the bottom.");
		}

		MapLayer hold = (MapLayer)layers.get(index - 1);
		layers.set(index - 1, getLayer(index));
		layers.set(index, hold);
	}

	public void mergeLayerDown(int index) throws Exception {
		if (index - 1 < 0) {
			throw new Exception(
					"Can't merge down bottom layer.");
		}

		getLayer(index).mergeOnto(getLayer(index - 1));
		removeLayer(index);
	}
	
	/**
	 * Get a MapLayer by its id. 
	 * 
	 * @deprecated
	 * @param i the id to search for
	 * @return a MapLayer, or null if none were found
	 */
	public MapLayer getLayerById(int i) {
		MapLayer temp = null;
		Iterator li = layers.iterator();

		while (li.hasNext()) {
			temp = (MapLayer)li.next();
			if (temp.getId() == i) {
				break;
			}
		}
		return temp;
	}

	public MapLayer getLayer(int i) {
		try {
			return (MapLayer)layers.get(i);
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		return null;
	}

	public ListIterator getLayers() {
		return layers.listIterator();
	}
	
	/**
	 * Resizes this plane. The (dx, dy) pair determines where the original plane
	 * should be positioned on the new area. It only affects layers with dimensions 
	 * equal to that of the MutlilayerPlane.
	 *
	 * @see MapLayer#resize
	 *
	 * @param width  The new width of the map.
	 * @param height The new height of the map.
	 * @param dx     The shift in x direction in tiles.
	 * @param dy     The shift in y direction in tiles.
	 */
	public void resize(int width, int height, int dx, int dy) {
		
		
		
		ListIterator itr = getLayers();
		while(itr.hasNext()) {
			MapLayer l = (MapLayer) itr.next();
			if(l.getBounds().equals(getBounds())) {
				l.resize(width, height, dx, dy);
			}
		}
		
		widthInTiles = width;
		heightInTiles= height;
	}
	
}
