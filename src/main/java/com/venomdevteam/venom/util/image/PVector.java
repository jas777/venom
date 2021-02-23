package com.venomdevteam.venom.util.image;

import java.io.Serializable;

public class PVector implements Serializable {
    /**
     * ( begin auto-generated from PVector_x.xml )
     * <p>
     * The x component of the vector. This field (variable) can be used to both
     * get and set the value (see above example.)
     * <p>
     * ( end auto-generated )
     *
     * @webref pvector:field
     * @usage web_application
     * @brief The x component of the vector
     */
    public float x;

    /**
     * ( begin auto-generated from PVector_y.xml )
     * <p>
     * The y component of the vector. This field (variable) can be used to both
     * get and set the value (see above example.)
     * <p>
     * ( end auto-generated )
     *
     * @webref pvector:field
     * @usage web_application
     * @brief The y component of the vector
     */
    public float y;

    /**
     * ( begin auto-generated from PVector_z.xml )
     * <p>
     * The z component of the vector. This field (variable) can be used to both
     * get and set the value (see above example.)
     * <p>
     * ( end auto-generated )
     *
     * @webref pvector:field
     * @usage web_application
     * @brief The z component of the vector
     */
    public float z;

    /**
     * Array so that this can be temporarily used in an array context
     */
    transient protected float[] array;


    /**
     * Constructor for an empty vector: x, y, and z are set to 0.
     */
    public PVector() {
    }


    /**
     * Constructor for a 3D vector.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param z the z coordinate.
     */
    public PVector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    /**
     * Constructor for a 2D vector: z coordinate is set to 0.
     */
    public PVector(float x, float y) {
        this.x = x;
        this.y = y;
    }


    /**
     * ( begin auto-generated from PVector_set.xml )
     * <p>
     * Sets the x, y, and z component of the vector using two or three separate
     * variables, the data from a PVector, or the values from a float array.
     * <p>
     * ( end auto-generated )
     *
     * @param x the x component of the vector
     * @param y the y component of the vector
     * @param z the z component of the vector
     * @webref pvector:method
     * @brief Set the components of the vector
     */
    public PVector set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }


    /**
     * @param x the x component of the vector
     * @param y the y component of the vector
     */
    public PVector set(float x, float y) {
        this.x = x;
        this.y = y;
        this.z = 0;
        return this;
    }


    /**
     * @param v any variable of type PVector
     */
    public PVector set(PVector v) {
        x = v.x;
        y = v.y;
        z = v.z;
        return this;
    }


    /**
     * Set the x, y (and maybe z) coordinates using a float[] array as the source.
     *
     * @param source array to copy from
     */
    public PVector set(float[] source) {
        if (source.length >= 2) {
            x = source[0];
            y = source[1];
        }
        if (source.length >= 3) {
            z = source[2];
        } else {
            z = 0;
        }
        return this;
    }

    /**
     * ( begin auto-generated from PVector_sub.xml )
     * <p>
     * Make a new 2D unit vector from an angle.
     * <p>
     * ( end auto-generated )
     *
     * @param angle the angle in radians
     * @return the new unit PVector
     * @webref pvector:method
     * @usage web_application
     * @brief Make a new 2D unit vector from an angle
     */
    static public PVector fromAngle(float angle) {
        return fromAngle(angle, null);
    }


    /**
     * Make a new 2D unit vector from an angle
     *
     * @param target the target vector (if null, a new vector will be created)
     * @return the PVector
     */
    static public PVector fromAngle(float angle, PVector target) {
        if (target == null) {
            target = new PVector((float) Math.cos(angle), (float) Math.sin(angle), 0);
        } else {
            target.set((float) Math.cos(angle), (float) Math.sin(angle), 0);
        }
        return target;
    }


    /**
     * ( begin auto-generated from PVector_copy.xml )
     * <p>
     * Gets a copy of the vector, returns a PVector object.
     * <p>
     * ( end auto-generated )
     *
     * @webref pvector:method
     * @usage web_application
     * @brief Get a copy of the vector
     */
    public PVector copy() {
        return new PVector(x, y, z);
    }


    @Deprecated
    public PVector get() {
        return copy();
    }


    /**
     * @param target
     */
    public float[] get(float[] target) {
        if (target == null) {
            return new float[]{x, y, z};
        }
        if (target.length >= 2) {
            target[0] = x;
            target[1] = y;
        }
        if (target.length >= 3) {
            target[2] = z;
        }
        return target;
    }


    /**
     * ( begin auto-generated from PVector_mag.xml )
     * <p>
     * Calculates the magnitude (length) of the vector and returns the result
     * as a float (this is simply the equation <em>sqrt(x*x + y*y + z*z)</em>.)
     * <p>
     * ( end auto-generated )
     *
     * @return magnitude (length) of the vector
     * @webref pvector:method
     * @usage web_application
     * @brief Calculate the magnitude of the vector
     * @see PVector#magSq()
     */
    public float mag() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }


    /**
     * ( begin auto-generated from PVector_mag.xml )
     * <p>
     * Calculates the squared magnitude of the vector and returns the result
     * as a float (this is simply the equation <em>(x*x + y*y + z*z)</em>.)
     * Faster if the real length is not required in the
     * case of comparing vectors, etc.
     * <p>
     * ( end auto-generated )
     *
     * @return squared magnitude of the vector
     * @webref pvector:method
     * @usage web_application
     * @brief Calculate the magnitude of the vector, squared
     * @see PVector#mag()
     */
    public float magSq() {
        return (x * x + y * y + z * z);
    }


    /**
     * ( begin auto-generated from PVector_add.xml )
     * <p>
     * Adds x, y, and z components to a vector, adds one vector to another, or
     * adds two independent vectors together. The version of the method that
     * adds two vectors together is a static method and returns a PVector, the
     * others have no return value -- they act directly on the vector. See the
     * examples for more context.
     * <p>
     * ( end auto-generated )
     *
     * @param v the vector to be added
     * @webref pvector:method
     * @usage web_application
     * @brief Adds x, y, and z components to a vector, one vector to another, or two independent vectors
     */
    public PVector add(PVector v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }


    /**
     * @param x x component of the vector
     * @param y y component of the vector
     */
    public PVector add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }


    /**
     * @param z z component of the vector
     */
    public PVector add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }


    /**
     * Add two vectors
     *
     * @param v1 a vector
     * @param v2 another vector
     */
    static public PVector add(PVector v1, PVector v2) {
        return add(v1, v2, null);
    }


    /**
     * Add two vectors into a target vector
     *
     * @param target the target vector (if null, a new vector will be created)
     */
    static public PVector add(PVector v1, PVector v2, PVector target) {
        if (target == null) {
            target = new PVector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
        } else {
            target.set(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
        }
        return target;
    }


    /**
     * ( begin auto-generated from PVector_sub.xml )
     * <p>
     * Subtracts x, y, and z components from a vector, subtracts one vector
     * from another, or subtracts two independent vectors. The version of the
     * method that subtracts two vectors is a static method and returns a
     * PVector, the others have no return value -- they act directly on the
     * vector. See the examples for more context.
     * <p>
     * ( end auto-generated )
     *
     * @param v any variable of type PVector
     * @webref pvector:method
     * @usage web_application
     * @brief Subtract x, y, and z components from a vector, one vector from another, or two independent vectors
     */
    public PVector sub(PVector v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }


    /**
     * @param x the x component of the vector
     * @param y the y component of the vector
     */
    public PVector sub(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }


    /**
     * @param z the z component of the vector
     */
    public PVector sub(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }


    /**
     * Subtract one vector from another
     *
     * @param v1 the x, y, and z components of a PVector object
     * @param v2 the x, y, and z components of a PVector object
     */
    static public PVector sub(PVector v1, PVector v2) {
        return sub(v1, v2, null);
    }


    /**
     * Subtract one vector from another and store in another vector
     *
     * @param target PVector in which to store the result
     */
    static public PVector sub(PVector v1, PVector v2, PVector target) {
        if (target == null) {
            target = new PVector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
        } else {
            target.set(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
        }
        return target;
    }


    /**
     * ( begin auto-generated from PVector_mult.xml )
     * <p>
     * Multiplies a vector by a scalar or multiplies one vector by another.
     * <p>
     * ( end auto-generated )
     *
     * @param n the number to multiply with the vector
     * @webref pvector:method
     * @usage web_application
     * @brief Multiply a vector by a scalar
     */
    public PVector mult(float n) {
        x *= n;
        y *= n;
        z *= n;
        return this;
    }


    /**
     * @param v the vector to multiply by the scalar
     */
    static public PVector mult(PVector v, float n) {
        return mult(v, n, null);
    }


    /**
     * Multiply a vector by a scalar, and write the result into a target PVector.
     *
     * @param target PVector in which to store the result
     */
    static public PVector mult(PVector v, float n, PVector target) {
        if (target == null) {
            target = new PVector(v.x * n, v.y * n, v.z * n);
        } else {
            target.set(v.x * n, v.y * n, v.z * n);
        }
        return target;
    }


    /**
     * ( begin auto-generated from PVector_div.xml )
     * <p>
     * Divides a vector by a scalar or divides one vector by another.
     * <p>
     * ( end auto-generated )
     *
     * @param n the number by which to divide the vector
     * @webref pvector:method
     * @usage web_application
     * @brief Divide a vector by a scalar
     */
    public PVector div(float n) {
        x /= n;
        y /= n;
        z /= n;
        return this;
    }


    /**
     * Divide a vector by a scalar and return the result in a new vector.
     *
     * @param v the vector to divide by the scalar
     * @return a new vector that is v1 / n
     */
    static public PVector div(PVector v, float n) {
        return div(v, n, null);
    }


    /**
     * Divide a vector by a scalar and store the result in another vector.
     *
     * @param target PVector in which to store the result
     */
    static public PVector div(PVector v, float n, PVector target) {
        if (target == null) {
            target = new PVector(v.x / n, v.y / n, v.z / n);
        } else {
            target.set(v.x / n, v.y / n, v.z / n);
        }
        return target;
    }


    /**
     * ( begin auto-generated from PVector_dist.xml )
     * <p>
     * Calculates the Euclidean distance between two points (considering a
     * point as a vector object).
     * <p>
     * ( end auto-generated )
     *
     * @param v the x, y, and z coordinates of a PVector
     * @webref pvector:method
     * @usage web_application
     * @brief Calculate the distance between two points
     */
    public float dist(PVector v) {
        float dx = x - v.x;
        float dy = y - v.y;
        float dz = z - v.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }


    /**
     * @param v1 any variable of type PVector
     * @param v2 any variable of type PVector
     * @return the Euclidean distance between v1 and v2
     */
    static public float dist(PVector v1, PVector v2) {
        float dx = v1.x - v2.x;
        float dy = v1.y - v2.y;
        float dz = v1.z - v2.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }


    /**
     * ( begin auto-generated from PVector_dot.xml )
     * <p>
     * Calculates the dot product of two vectors.
     * <p>
     * ( end auto-generated )
     *
     * @param v any variable of type PVector
     * @return the dot product
     * @webref pvector:method
     * @usage web_application
     * @brief Calculate the dot product of two vectors
     */
    public float dot(PVector v) {
        return x * v.x + y * v.y + z * v.z;
    }


    /**
     * @param x x component of the vector
     * @param y y component of the vector
     * @param z z component of the vector
     */
    public float dot(float x, float y, float z) {
        return this.x * x + this.y * y + this.z * z;
    }


    /**
     * @param v1 any variable of type PVector
     * @param v2 any variable of type PVector
     */
    static public float dot(PVector v1, PVector v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }


    /**
     * ( begin auto-generated from PVector_cross.xml )
     * <p>
     * Calculates and returns a vector composed of the cross product between
     * two vectors.
     * <p>
     * ( end auto-generated )
     *
     * @param v the vector to calculate the cross product
     * @webref pvector:method
     * @brief Calculate and return the cross product
     */
    public PVector cross(PVector v) {
        return cross(v, null);
    }


    /**
     * @param v      any variable of type PVector
     * @param target PVector to store the result
     */
    public PVector cross(PVector v, PVector target) {
        float crossX = y * v.z - v.y * z;
        float crossY = z * v.x - v.z * x;
        float crossZ = x * v.y - v.x * y;

        if (target == null) {
            target = new PVector(crossX, crossY, crossZ);
        } else {
            target.set(crossX, crossY, crossZ);
        }
        return target;
    }


    /**
     * @param v1     any variable of type PVector
     * @param v2     any variable of type PVector
     * @param target PVector to store the result
     */
    static public PVector cross(PVector v1, PVector v2, PVector target) {
        float crossX = v1.y * v2.z - v2.y * v1.z;
        float crossY = v1.z * v2.x - v2.z * v1.x;
        float crossZ = v1.x * v2.y - v2.x * v1.y;

        if (target == null) {
            target = new PVector(crossX, crossY, crossZ);
        } else {
            target.set(crossX, crossY, crossZ);
        }
        return target;
    }


    /**
     * ( begin auto-generated from PVector_normalize.xml )
     * <p>
     * Normalize the vector to length 1 (make it a unit vector).
     * <p>
     * ( end auto-generated )
     *
     * @webref pvector:method
     * @usage web_application
     * @brief Normalize the vector to a length of 1
     */
    public PVector normalize() {
        float m = mag();
        if (m != 0 && m != 1) {
            div(m);
        }
        return this;
    }


    /**
     * @param target Set to null to create a new vector
     * @return a new vector (if target was null), or target
     */
    public PVector normalize(PVector target) {
        if (target == null) {
            target = new PVector();
        }
        float m = mag();
        if (m > 0) {
            target.set(x / m, y / m, z / m);
        } else {
            target.set(x, y, z);
        }
        return target;
    }


    /**
     * ( begin auto-generated from PVector_limit.xml )
     * <p>
     * Limit the magnitude of this vector to the value used for the <b>max</b> parameter.
     * <p>
     * ( end auto-generated )
     *
     * @param max the maximum magnitude for the vector
     * @webref pvector:method
     * @usage web_application
     * @brief Limit the magnitude of the vector
     */
    public PVector limit(float max) {
        if (magSq() > max * max) {
            normalize();
            mult(max);
        }
        return this;
    }


    /**
     * ( begin auto-generated from PVector_setMag.xml )
     * <p>
     * Set the magnitude of this vector to the value used for the <b>len</b> parameter.
     * <p>
     * ( end auto-generated )
     *
     * @param len the new length for this vector
     * @webref pvector:method
     * @usage web_application
     * @brief Set the magnitude of the vector
     */
    public PVector setMag(float len) {
        normalize();
        mult(len);
        return this;
    }


    /**
     * Sets the magnitude of this vector, storing the result in another vector.
     *
     * @param target Set to null to create a new vector
     * @param len    the new length for the new vector
     * @return a new vector (if target was null), or target
     */
    public PVector setMag(PVector target, float len) {
        target = normalize(target);
        target.mult(len);
        return target;
    }


    /**
     * ( begin auto-generated from PVector_setMag.xml )
     * <p>
     * Calculate the angle of rotation for this vector (only 2D vectors)
     * <p>
     * ( end auto-generated )
     *
     * @return the angle of rotation
     * @webref pvector:method
     * @usage web_application
     * @brief Calculate the angle of rotation for this vector
     */
    public float heading() {
        float angle = (float) Math.atan2(y, x);
        return angle;
    }


    @Deprecated
    public float heading2D() {
        return heading();
    }


    /**
     * ( begin auto-generated from PVector_rotate.xml )
     * <p>
     * Rotate the vector by an angle (only 2D vectors), magnitude remains the same
     * <p>
     * ( end auto-generated )
     *
     * @param theta the angle of rotation
     * @webref pvector:method
     * @usage web_application
     * @brief Rotate the vector by an angle (2D only)
     */
    public PVector rotate(float theta) {
        float temp = x;
        // Might need to check for rounding errors like with angleBetween function?
        x = (float) (x * Math.cos(theta) - y * Math.sin(theta));
        y = (float) (temp * Math.sin(theta) + y * Math.cos(theta));
        return this;
    }


    /**
     * ( begin auto-generated from PVector_angleBetween.xml )
     * <p>
     * Calculates and returns the angle (in radians) between two vectors.
     * <p>
     * ( end auto-generated )
     *
     * @param v1 the x, y, and z components of a PVector
     * @param v2 the x, y, and z components of a PVector
     * @webref pvector:method
     * @usage web_application
     * @brief Calculate and return the angle between two vectors
     */
    static public float angleBetween(PVector v1, PVector v2) {

        // We get NaN if we pass in a zero vector which can cause problems
        // Zero seems like a reasonable angle between a (0,0,0) vector and something else
        if (v1.x == 0 && v1.y == 0 && v1.z == 0) return 0.0f;
        if (v2.x == 0 && v2.y == 0 && v2.z == 0) return 0.0f;

        double dot = v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
        double v1mag = Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z);
        double v2mag = Math.sqrt(v2.x * v2.x + v2.y * v2.y + v2.z * v2.z);
        // This should be a number between -1 and 1, since it's "normalized"
        double amt = dot / (v1mag * v2mag);
        // But if it's not due to rounding error, then we need to fix it
        // http://code.google.com/p/processing/issues/detail?id=340
        // Otherwise if outside the range, acos() will return NaN
        // http://www.cppreference.com/wiki/c/math/acos
        if (amt <= -1) {
            return (float) Math.PI;
        } else if (amt >= 1) {
            // http://code.google.com/p/processing/issues/detail?id=435
            return 0;
        }
        return (float) Math.acos(amt);
    }


    @Override
    public String toString() {
        return "[ " + x + ", " + y + ", " + z + " ]";
    }


    /**
     * ( begin auto-generated from PVector_array.xml )
     * <p>
     * Return a representation of this vector as a float array. This is only
     * for temporary use. If used in any other fashion, the contents should be
     * copied by using the <b>PVector.get()</b> method to copy into your own array.
     * <p>
     * ( end auto-generated )
     *
     * @webref pvector:method
     * @usage: web_application
     * @brief Return a representation of the vector as a float array
     */
    public float[] array() {
        if (array == null) {
            array = new float[3];
        }
        array[0] = x;
        array[1] = y;
        array[2] = z;
        return array;
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PVector)) {
            return false;
        }
        final PVector p = (PVector) obj;
        return x == p.x && y == p.y && z == p.z;
    }


    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + Float.floatToIntBits(x);
        result = 31 * result + Float.floatToIntBits(y);
        result = 31 * result + Float.floatToIntBits(z);
        return result;
    }
}