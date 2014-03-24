/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/. */

package com.cburch.logisim.data;

import com.cburch.logisim.util.Cache;

/**
 * Represents an immutable rectangular bounding box. This is analogous to
 * java.awt's <code>Point</code> class, except that objects of this type
 * are immutable.
 */
public class Location implements Comparable<Location> {
	private static final Cache cache = new Cache();
	
	private final int hashCode;
	private final int x;
	private final int y;

	private Location(int hashCode, int x, int y) {
		this.hashCode = hashCode;
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int manhattanDistanceTo(Location o) {
		return Math.abs(o.x - this.x) + Math.abs(o.y - this.y);
	}
	
	public int manhattanDistanceTo(int x, int y) {
		return Math.abs(x - this.x) + Math.abs(y - this.y);
	}

	public Location translate(int dx, int dy) {
		if (dx == 0 && dy == 0) return this;
		return Location.create(x + dx, y + dy);
	}
	
	public Location translate(Direction dir, int dist) {
		return translate(dir, dist, 0);
	}
	
	public Location translate(Direction dir, int dist, int right) {
		if (dist == 0 && right == 0) return this;
		if (dir == Direction.EAST) return Location.create(x + dist, y + right);
		if (dir == Direction.WEST) return Location.create(x - dist, y - right);
		if (dir == Direction.SOUTH) return Location.create(x - right, y + dist);
		if (dir == Direction.NORTH) return Location.create(x + right, y - dist);
		return Location.create(x + dist, y + right);
	}
	
	// rotates this around (xc,yc) assuming that this is facing in the
	// from direction and the returned bounds should face in the to direction.
	public Location rotate(Direction from, Direction to, int xc, int yc) {
		int degrees = to.toDegrees() - from.toDegrees();
		while (degrees >= 360) degrees -= 360;
		while (degrees < 0) degrees += 360;
		
		int dx = x - xc;
		int dy = y - yc;
		if (degrees == 90) {
			return create(xc + dy, yc - dx);
		} else if (degrees == 180) {
			return create(xc - dx, yc - dy);
		} else if (degrees == 270) {
			return create(xc - dy, yc + dx);
		} else {
			return this;
		}
	}

	@Override
	public boolean equals(Object other_obj) {
		if (!(other_obj instanceof Location)) return false;
		Location other = (Location) other_obj;
		return this.x == other.x && this.y == other.y;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	public int compareTo(Location other) {
		if (this.x != other.x) return this.x - other.x;
		else                  return this.y - other.y;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public static Location create(int x, int y) {
		int hashCode = 31 * x + y;
		Object ret = cache.get(hashCode);
		if (ret != null) {
			Location loc = (Location) ret;
			if (loc.x == x && loc.y == y) return loc;
		}
		Location loc = new Location(hashCode, x, y);
		cache.put(hashCode, loc);
		return loc;
	}

	public static Location parse(String value) {
		String base = value;

		value = value.trim();
		if (value.charAt(0) == '(') {
			int len = value.length();
			if (value.charAt(len - 1) != ')') {
				throw new NumberFormatException("invalid point '"
					+ base + "'");
			}
			value = value.substring(1, len - 1);
		}
		value = value.trim();
		int comma = value.indexOf(',');
		if (comma < 0) {
			comma = value.indexOf(' ');
			if (comma < 0) {
				throw new NumberFormatException("invalid point '"
					+ base + "'");
			}
		}
		int x = Integer.parseInt(value.substring(0, comma).trim());
		int y = Integer.parseInt(value.substring(comma + 1).trim());
		return Location.create(x, y);
	}
}
