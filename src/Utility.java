public class Utility {
    public static float[] normalize(float[] v) {
        float length = VectorLength(v);
        v[0] /= length;
        v[1] /= length;
        v[2] /= length;
        return v;
    }

    public static float VectorDotVector(float[] u, float[] v) {
        return (float)(u[0] * v[0] + u[1] * v[1] + u[2] * v[2]);
    }

    public static float[] VectorCrossVector(float[] v1, float[] v2) {
        float[] product = {
                v1[1] * v2[2] - v1[2] * v2[1],
                v1[2] * v2[0] - v1[0] * v2[2],
                v1[0] * v2[1] - v1[1] * v2[0]
        };
        return product;
    }

    public static float[] TriangleNormal(Triangle q) {
        float[] v1 = {q.position[1].vertex[0] - q.position[0].vertex[0],
                q.position[1].vertex[1] - q.position[0].vertex[1],
                q.position[1].vertex[2] - q.position[0].vertex[2]
        };
        float[] v2 = {q.position[2].vertex[0] - q.position[0].vertex[0],
                q.position[2].vertex[1] - q.position[0].vertex[1],
                q.position[2].vertex[2] - q.position[0].vertex[2]
        };
        return normalize(VectorCrossVector(v1, v2));
    }

    public static float angleBetweenTwoVectors(float[] u, float[] v) {
        return (float)Math.toDegrees(Math.acos(VectorDotVector(u, v)/(VectorLength(u)*VectorLength(v))));
    }

    public static float VectorLength(float[] v) {
        return (float)Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
    }

    public static float twoPointsDistance(float[] p1, float[] p2) {
        return (float)(Math.sqrt((p1[0] - p2[0])*(p1[0] - p2[0]) + (p1[1] - p2[1])*(p1[1] - p2[1]) + (p1[2] - p2[2])*(p1[2] - p2[2])));
    }
}
