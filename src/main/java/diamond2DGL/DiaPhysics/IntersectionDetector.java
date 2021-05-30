package diamond2DGL.DiaPhysics;

import diamond2DGL.DiaPhysics.primitives.*;
import diamond2DGL.renderer.Line;
import diamond2DGL.utils.DiaMath;
import org.joml.Vector2f;

public class IntersectionDetector {

    // =================================================================================================================
    //  POINT TO PRIMITIVE COLLISIONS
    // =================================================================================================================
    public static boolean pointLine(Vector2f point, Line line) {
        float dx = line.getTo().x - line.getFrom().x;
        float dy = line.getTo().y - line.getFrom().y;
        if (dx == 0f) {
            return DiaMath.compareFloat(point.x, line.getFrom().x);
        }
        float m = dy / dx;

        float b = line.getTo().y - (m * line.getTo().x);

        return point.y == m * point.x + b;
    }

    public static boolean pointCircle(Vector2f point, Circle circle) {
        Vector2f center = circle.getCenter();
        Vector2f centerToPoint = new Vector2f(point).sub(center);

        return centerToPoint.lengthSquared() <= circle.getRadius() * circle.getRadius();
    }

    public static boolean pointAABB(Vector2f point, AABB aabb) {
        Vector2f min = aabb.getMin();
        Vector2f max = aabb.getMax();
        return point.x <= max.x && min.x <= point.x &&
                point.y <= max.y && min.y <= point.y;
    }

    public static boolean pointBox(Vector2f point, Box box) {
        Vector2f pointBoxSpace = new Vector2f(point);
        Vector2f min = box.getMin();
        Vector2f max = box.getMax();
        DiaMath.rotate(pointBoxSpace, box.getRigidBody().getRotation(), box.getRigidBody().getPos());
        return pointBoxSpace.x <= max.x && min.x <= pointBoxSpace.x &&
                pointBoxSpace.y <= max.y && min.y <= pointBoxSpace.y;
    }

    // =================================================================================================================
    //  LINE TO PRIMITIVE COLLISION
    // =================================================================================================================
    public static boolean lineCircle(Line line, Circle circle) {
        if (pointCircle(line.getFrom(), circle) || pointCircle(line.getTo(), circle)) {
            return true;
        }
        Vector2f ab = new Vector2f(line.getTo()).sub(line.getFrom());
        Vector2f circleCenter = circle.getCenter();
        Vector2f centerToLineStart = new Vector2f(circleCenter).sub(line.getFrom());
        float t = centerToLineStart.dot(ab) / ab.dot(ab);

        if (t < 0.0f || t > 1.0f) {
            return false;
        }

        Vector2f closestPoint = new Vector2f(line.getFrom().add(ab.mul(t)));

        return pointCircle(closestPoint, circle);
    }

    public static boolean lineAABB(Line line, AABB box) {
        if (pointAABB(line.getFrom(), box) || pointAABB(line.getTo(), box)) {
            return true;
        }
        Vector2f unitVector = new Vector2f(line.getTo().sub(line.getFrom()));
        unitVector.normalize();
        unitVector.x = (unitVector.x != 0) ? 1.0f / unitVector.x : 0f;
        unitVector.y = (unitVector.y != 0) ? 1.0f / unitVector.y : 0f;

        Vector2f min = box.getMin();
        min.sub(line.getFrom()).mul(unitVector);
        Vector2f max = box.getMax();
        max.sub(line.getFrom()).mul(unitVector);

        float tmin = Math.max(Math.min(min.x, max.x), Math.min(min.x, max.y));
        float tmax = Math.min(Math.max(min.x, max.x), Math.max(min.y, max.y));
        if (tmax < 0 || tmin > tmax) {
            return false;
        }

        float t = (tmin < 0f) ? tmax : tmin;
        return t > 0f && t * t < line.lengthSquared();
    }

    public static boolean lineBox(Line line, Box box) {
        float theta = -box.getRigidBody().getRotation();
        Vector2f center = box.getRigidBody().getPos();
        Vector2f localStart = new Vector2f(line.getFrom());
        Vector2f localEnd = new Vector2f(line.getTo());
        DiaMath.rotate(localStart, theta, center);
        DiaMath.rotate(localEnd, theta, center);

        Line localLine = new Line(localStart, localEnd);
        AABB aabb = new AABB(box.getMin(), box.getMax());

        return lineAABB(localLine, aabb);
    }

    // =================================================================================================================
    //  RAY CASTING
    // =================================================================================================================
    public static boolean rayCast(Circle circle, Ray ray, RayCast rayCast) {
        RayCast.reset(rayCast);
        Vector2f originToCircle = new Vector2f(circle.getCenter()).sub(ray.getOrigin());
        float radiusSquared = circle.getRadius() * circle.getRadius();
        float originToCircleLengthSquared = originToCircle.lengthSquared();

        // Project the vector form the ray origin onto the direction of the ray
        float a = originToCircle.dot(ray.getDirection());
        float bSq = originToCircleLengthSquared - (a * a);
        if (radiusSquared - bSq < 0.0f) {
            return false;
        }

        float f = (float) Math.sqrt(radiusSquared - bSq);
        float t = 0;
        if (originToCircleLengthSquared < radiusSquared) {
            t = a + f;
        } else {
            t = a - f;
        }

        if (rayCast != null) {
            Vector2f point = new Vector2f(ray.getOrigin()).add(new Vector2f(ray.getDirection()).mul(t));
            Vector2f normal = new Vector2f(point).sub(circle.getCenter());
            normal.normalize();
            rayCast.init(point, normal, t, true);
        }

        return true;
    }

    public static boolean rayCast(AABB box, Ray ray, RayCast rayCast) {
        RayCast.reset(rayCast);
        Vector2f unitVector = ray.getDirection();
        unitVector.normalize();
        unitVector.x = (unitVector.x != 0) ? 1.0f / unitVector.x : 0f;
        unitVector.y = (unitVector.y != 0) ? 1.0f / unitVector.y : 0f;

        Vector2f min = box.getMin();
        min.sub(ray.getOrigin()).mul(unitVector);
        Vector2f max = box.getMax();
        max.sub(ray.getOrigin()).mul(unitVector);

        float tmin = Math.max(Math.min(min.x, max.x), Math.min(min.x, max.y));
        float tmax = Math.min(Math.max(min.x, max.x), Math.max(min.y, max.y));
        if (tmax < 0 || tmin > tmax) {
            return false;
        }

        float t = (tmin < 0f) ? tmax : tmin;
        boolean hit =  t > 0f;
        if (!hit) {
            return false;
        }

        if (rayCast != null) {
            Vector2f point = new Vector2f(ray.getOrigin()).add(new Vector2f(ray.getDirection()).mul(t));
            Vector2f normal = new Vector2f(ray.getOrigin()).sub(point);
            normal.normalize();
            rayCast.init(point, normal, t, true);
        }

        return true;
    }

    public static boolean rayCast(Box box, Ray ray, RayCast rayCast) {
        RayCast.reset(rayCast);

        Vector2f size = box.getHalfSize();
        Vector2f xAxis = new Vector2f(1,0);
        Vector2f yAxis = new Vector2f(0,1);

        DiaMath.rotate(xAxis, -box.getRigidBody().getRotation(), new Vector2f(0,0));
        DiaMath.rotate(yAxis, -box.getRigidBody().getRotation(), new Vector2f(0,0));

        Vector2f p = new Vector2f(box.getRigidBody().getPos()).sub(ray.getOrigin());
        // Project direction of the ray onto each axis of the box
        Vector2f f = new Vector2f(xAxis.dot(ray.getDirection()), yAxis.dot(ray.getDirection()));
        Vector2f e = new Vector2f(xAxis.dot(p), yAxis.dot(p));

        float[] tArray = {0, 0, 0, 0};
        for (int i = 0; i < 2; i++) {
            if (DiaMath.compareFloat(f.get(i), 0)) {
                // If the ray is parallel to the current axis, and the origin of the ray is not inside, we have no hit
                if (-e.get(i) - size.get(i) > 0 || -e.get(i) + size.get(i) < 0) {
                    return false;
                }
                // To avoid division by 0
                f.setComponent(i, 0.00001f);
            }
            tArray[i * 2] = (e.get(i) + size.get(i)) / f.get(i);     // tmax for the axis
            tArray[i * 2 + 1] = (e.get(i) - size.get(i)) / f.get(i); // tmin for the axis
        }

        float tmin = Math.max(Math.min(tArray[0], tArray[1]), Math.min(tArray[2], tArray[3]));
        float tmax = Math.min(Math.max(tArray[0], tArray[1]), Math.max(tArray[2], tArray[3]));

        float t = (tmin < 0f) ? tmax : tmin;
        boolean hit =  t > 0f;
        if (!hit) {
            return false;
        }

        if (rayCast != null) {
            Vector2f point = new Vector2f(ray.getOrigin()).add(new Vector2f(ray.getDirection()).mul(t));
            Vector2f normal = new Vector2f(ray.getOrigin()).sub(point);
            normal.normalize();
            rayCast.init(point, normal, t, true);
        }

        return true;
    }

    // =================================================================================================================
    //  CIRCLE COLLISIONS
    // =================================================================================================================
    public static boolean circleCircle(Circle c1, Circle c2) {
        Vector2f centerToCenter = new Vector2f(c1.getCenter()).sub(c2.getCenter());
        float radiiSum = c1.getRadius() + c2.getRadius();
        return centerToCenter.lengthSquared() <= radiiSum * radiiSum;
    }

    public static boolean cirlceAABB(Circle circle, AABB box) {
        Vector2f min = box.getMin();
        Vector2f max = box.getMax();

        Vector2f closestPointToCircle = new Vector2f(circle.getCenter());
        if (closestPointToCircle.x < min.x) {
            closestPointToCircle.x = min.x;
        } else if (closestPointToCircle.x > max.x) {
            closestPointToCircle.x = max.x;
        }

        if (closestPointToCircle.y < min.y) {
            closestPointToCircle.y = min.y;
        } else if (closestPointToCircle.y > max.y) {
            closestPointToCircle.y = max.y;
        }

        Vector2f circleToBox = new Vector2f(circle.getCenter()).sub(closestPointToCircle);
        return circleToBox.lengthSquared() <= circle.getRadius() * circle.getRadius();
    }

    public static boolean cirlceBox(Circle circle, Box box) {
        // Treat box as AABB, though rotated to set the sides as axis (set box local space)
        Vector2f min = new Vector2f();
        Vector2f max = new Vector2f(box.getHalfSize()).mul(2.0f);

        // Create circle in box local space
        Vector2f r = new Vector2f(circle.getCenter()).sub(box.getRigidBody().getPos());
        DiaMath.rotate(r, -box.getRigidBody().getRotation(), new Vector2f(0, 0));
        Vector2f localCirclePos = new Vector2f(r).add(box.getHalfSize());

        Vector2f closestPointToCircle = new Vector2f(localCirclePos);
        if (closestPointToCircle.x < min.x) {
            closestPointToCircle.x = min.x;
        } else if (closestPointToCircle.x > max.x) {
            closestPointToCircle.x = max.x;
        }

        if (closestPointToCircle.y < min.y) {
            closestPointToCircle.y = min.y;
        } else if (closestPointToCircle.y > max.y) {
            closestPointToCircle.y = max.y;
        }

        Vector2f circleToBox = new Vector2f(localCirclePos).sub(closestPointToCircle);
        return circleToBox.lengthSquared() <= circle.getRadius() * circle.getRadius();
    }
}
