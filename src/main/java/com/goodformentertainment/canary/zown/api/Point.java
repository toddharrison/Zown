package com.goodformentertainment.canary.zown.api;

import net.canarymod.api.world.position.Location;

/**
 * A Point is a discrete block location within a World.
 *
 * @author Todd Harrison
 */
public class Point implements Cloneable {
    /**
     * The block x location of this Point.
     */
    public int x;

    /**
     * The block y location of this Point.
     */
    public int y;

    /**
     * The block z location of this Point.
     */
    public int z;

    /**
     * Parse a valid String into a Point. Will throw an IllegalArgumentException for an invalid Point
     * String.
     *
     * @param sPoint The String value of the Point.
     * @return A Point derived from the String.
     */
    public static Point parse(final String sPoint) {
        if (sPoint == null) {
            throw new IllegalArgumentException("Null String Point");
        }
        final String[] values = sPoint.split(":");
        if (values.length == 1 && values[0].trim().equalsIgnoreCase("null")) {
            return null;
        }
        if (values.length != 4) {
            throw new IllegalArgumentException("Invalid Point String format");
        }

        final Point point;
        if ("point".equals(values[0])) {
            point = new Point(Integer.parseInt(values[1]), Integer.parseInt(values[2]),
                    Integer.parseInt(values[3]));
        } else {
            throw new IllegalArgumentException("Not a serialized Point");
        }
        return point;
    }

    /**
     * Create a new Point from the specified x, y and z block location.
     *
     * @param x The x location.
     * @param y The y location.
     * @param z The z location.
     */
    public Point(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Create a new Point from the specified Location.
     *
     * @param location The Location.
     */
    public Point(final Location location) {
        x = location.getBlockX();
        y = location.getBlockY();
        z = location.getBlockZ();
    }

    /**
     * Get the distance squared between this Point and another Point.
     *
     * @param p The target Point.
     * @return The distance squared between this Point and the target Point.
     */
    public int distanceSquared(final Point p) {
        final int dX = x - p.x;
        final int dY = y - p.y;
        final int dZ = z - p.z;
        return dX * dX + dY * dY + dZ * dZ;
    }

    @Override
    public Point clone() {
        return new Point(x, y, z);
    }

    @Override
    public boolean equals(final Object o) {
        boolean equal = false;
        if (o instanceof Point) {
            final Point p = (Point) o;
            if (p.x == x && p.y == y && p.z == z) {
                equal = true;
            }
        }
        return equal;
    }

    /**
     * Serialize this Point into a String that can be reconstructed later using the parse method.
     *
     * @return The Point String.
     */
    @Override
    public String toString() {
        return "point:" + x + ":" + y + ":" + z;
    }
}
