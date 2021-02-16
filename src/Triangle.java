public class Triangle {
    Vertex[] position = new Vertex[3];

    public Triangle(Vertex p1, Vertex p2, Vertex p3) {
        this.position[0] = p1;
        this.position[1] = p2;
        this.position[2] = p3;
    }

    public Vertex[] getTriangle () {
        return this.position;
    }
}
