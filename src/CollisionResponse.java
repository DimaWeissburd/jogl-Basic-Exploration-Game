public class CollisionResponse {
    public static void PointVSTriangle(float[] eye, float[] center, float[] direction, float velocity, Triangle p) {
        float[] normal = Utility.TriangleNormal(p);
        if (Utility.angleBetweenTwoVectors(direction, normal) < 90) { // for collision from both sides
            normal[0] *= -1;
            normal[1] *= -1;
            normal[2] *= -1;
        }
        normal[0] *= velocity;
        normal[1] *= velocity;
        normal[2] *= velocity;
        center[0] += normal[0];
        center[1] += normal[1];
        center[2] += normal[2];
        eye[0] += normal[0];
        eye[1] += normal[1];
        eye[2] += normal[2];
    }
}
