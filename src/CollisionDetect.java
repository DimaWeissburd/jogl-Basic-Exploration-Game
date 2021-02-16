public class CollisionDetect {
    private static double EPSILON = 0.0001;
    public static boolean PointVSTriangle(float[] point, float[] direction, Triangle p) {
        // Möller–Trumbore algorithm
        float[] edge1 = {p.position[1].getVertex()[0] - p.position[0].getVertex()[0],
                         p.position[1].getVertex()[1] - p.position[0].getVertex()[1],
                         p.position[1].getVertex()[2] - p.position[0].getVertex()[2]
        };
        float[] edge2 = {p.position[2].getVertex()[0] - p.position[0].getVertex()[0],
                         p.position[2].getVertex()[1] - p.position[0].getVertex()[1],
                         p.position[2].getVertex()[2] - p.position[0].getVertex()[2]
        };
        float[] h = Utility.VectorCrossVector(direction, edge2);
        float a = Utility.VectorDotVector(edge1, h);
        if (a > -EPSILON && a < EPSILON) {
            return false;    // This ray is parallel to this triangle.
        }
        float f = 1/a;
        float[] s = {point[0] - p.position[0].getVertex()[0],
                     point[1] - p.position[0].getVertex()[1],
                     point[2] - p.position[0].getVertex()[2]
        };
        float u = f * Utility.VectorDotVector(s, h);
        if (u < 0.0 || u > 1.0) {
            return false;
        }
        float[] q = Utility.VectorCrossVector(s, edge1);
        float v = f * Utility.VectorDotVector(direction, q);
        if (v < 0.0 || u + v > 1.0) {
            return false;
        }
        // At this stage we can compute t to find out where the intersection point is on the line.
        float t = f * Utility.VectorDotVector(edge2, q);
        float[] intersection = {0, 0, 0}; // intersection point when collision occurs
        if (t > EPSILON) { // ray intersection
            intersection[0] = point[0] + t * direction[0];
            intersection[1] = point[1] + t * direction[1];
            intersection[2] = point[2] + t * direction[2];
            if (Utility.twoPointsDistance(point, intersection) < 10) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
