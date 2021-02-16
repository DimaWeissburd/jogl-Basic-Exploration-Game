public class Vertex {
    float[] vertex = new float[3];

    public Vertex(float x, float y, float z) {
        this.vertex[0] = x;
        this.vertex[1] = y;
        this.vertex[2] = z;
    }

    public float[] getVertex () {
        return this.vertex;
    }
}
