import static com.jogamp.opengl.math.FloatUtil.cos;
import static com.jogamp.opengl.math.FloatUtil.sin;

public class CameraMovement {
    public static void translate (float[] eye, float[] center, float[] up, float[] dv) {
        float[] u = {center[0] - eye[0], center[1] - eye[1], center[2] - eye[2]};
        u = Utility.normalize(u);
        float[] v = Utility.VectorCrossVector(u, up);
        v = Utility.normalize(v);

        eye[0] += dv[0] * u[0] + dv[1] * v[0] + dv[2] * up[0];
        eye[1] += dv[0] * u[1] + dv[1] * v[1] + dv[2] * up[1];
        eye[2] += dv[0] * u[2] + dv[1] * v[2] + dv[2] * up[2];
        center[0] += dv[0] * u[0] + dv[1] * v[0] + dv[2] * up[0];
        center[1] += dv[0] * u[1] + dv[1] * v[1] + dv[2] * up[1];
        center[2] += dv[0] * u[2] + dv[1] * v[2] + dv[2] * up[2];
    }

    public static void rotate (float[] eye, float[] center, float[] up, float[] dt) {
        float[] u = {center[0] - eye[0], center[1] - eye[1], center[2] - eye[2]};
        u = Utility.normalize(u);
        float[] v = Utility.VectorCrossVector(u, up);
        v = Utility.normalize(v);
        // rotate up / down
        if (dt[1] != 0) {
            float[] temp = {
                    u[0] * (cos(dt[1]) + v[0] * v[0] * (1 - cos(dt[1]))) + u[1] * (v[0] * v[1] * (1 - cos(dt[1])) + v[2] * sin(dt[1])) + u[2] * (v[0] * v[2] * (1 - cos(dt[1])) - v[1] * sin(dt[1])),
                    u[0] * (v[0] * v[1] * (1 - cos(dt[1])) - v[2] * sin(dt[1])) + u[1] * (cos(dt[1]) + v[1] * v[1] * (1 - cos(dt[1]))) + u[2] * (v[1] * v[2] * (1 - cos(dt[1])) + v[0] * sin(dt[1])),
                    u[0] * (v[0] * v[2] * (1 - cos(dt[1])) + v[1] * sin(dt[1])) + u[1] * (v[1] * v[2] * (1 - cos(dt[1])) - v[0] * sin(dt[1])) + u[2] * (cos(dt[1]) + v[2] * v[2] * (1 - cos(dt[1])))
            };
            center[0] = temp[0] + eye[0];
            center[1] = temp[1] + eye[1];
            center[2] = temp[2] + eye[2];
            up = Utility.VectorCrossVector(v, temp); // doesn't work when looking straight up/down
            up[0] = v[1] * temp[2] - v[2] * temp[1];
            up[1] = v[2] * temp[0] - v[0] * temp[2];
            up[2] = v[0] * temp[1] - v[1] * temp[0];
        }

        // rotate left / right
        if (dt[0] != 0) {
            up = Utility.normalize(up);
            float[] temp = {
                    u[0] * (cos(dt[0]) + up[0] * up[0] * (1 - cos(dt[0]))) + u[1] * (up[0] * up[1] * (1 - cos(dt[0])) + up[2] * sin(dt[0])) + u[2] * (up[0] * up[2] * (1 - cos(dt[0])) - up[1] * sin(dt[0])),
                    u[0] * (up[0] * up[1] * (1 - cos(dt[0])) - up[2] * sin(dt[0])) + u[1] * (cos(dt[0]) + up[1] * up[1] * (1 - cos(dt[0]))) + u[2] * (up[1] * up[2] * (1 - cos(dt[0])) + up[0] * sin(dt[0])),
                    u[0] * (up[0] * up[2] * (1 - cos(dt[0])) + up[1] * sin(dt[0])) + u[1] * (up[1] * up[2] * (1 - cos(dt[0])) - up[0] * sin(dt[0])) + u[2] * (cos(dt[0]) + up[2] * up[2] * (1 - cos(dt[0])))
            };
            center[0] = temp[0] + eye[0];
            center[1] = temp[1] + eye[1];
            center[2] = temp[2] + eye[2];
        }

        // rotate clockwise / counterclockwise
        if (dt[2] != 0) {
            u = Utility.normalize(u);
            float[] temp = {
                    up[0] * (cos(dt[2]) + u[0] * u[0] * (1 - cos(dt[2]))) + up[1] * (u[0] * u[1] * (1 - cos(dt[2])) + u[2] * sin(dt[2])) + up[2] * (u[0] * u[2] * (1 - cos(dt[2])) - u[1] * sin(dt[2])),
                    up[0] * (u[0] * u[1] * (1 - cos(dt[2])) - u[2] * sin(dt[2])) + up[1] * (cos(dt[2]) + u[1] * u[1] * (1 - cos(dt[2]))) + up[2] * (u[1] * u[2] * (1 - cos(dt[2])) + u[0] * sin(dt[2])),
                    up[0] * (u[0] * u[2] * (1 - cos(dt[2])) + u[1] * sin(dt[2])) + up[1] * (u[1] * u[2] * (1 - cos(dt[2])) - u[0] * sin(dt[2])) + up[2] * (cos(dt[2]) + u[2] * u[2] * (1 - cos(dt[2])))
            };
            up[0] = temp[0];
            up[1] = temp[1];
            up[2] = temp[2];
        }
    }
}
