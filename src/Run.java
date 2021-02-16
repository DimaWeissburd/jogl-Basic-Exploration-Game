import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import com.jogamp.newt.Window;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.event.awt.AWTKeyAdapter;
import com.jogamp.newt.event.awt.AWTMouseAdapter;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class Run implements GLEventListener, KeyListener, MouseListener {
    TextRenderer renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 32));
    float velocity = 0.5f, angularVelocity = 0.01f;
//    float[] gravity = {0, 0, -0.001f};
    private float[] eye = {-5, 5, -5}; // camera location
    private float[] center = {0, 5, 0}; // a point that the camera looks at
    private float[] up = {0, 1, 0}; // up vector
    float[] dv = {0, 0, 0}, dt = {0, 0, 0}, u = {0, 0, 0}, v = {0, 0, 0};
    float rotor_angle = 0;
    private int bark, tree, rotor, fox; // objects
    private Texture grass, water, ground, snow, wood, leaves;
    boolean fox1 = true, fox2 = true, safe = false, help = false;

    static GLU glu = new GLU();
    static GLCanvas canvas = new GLCanvas();
    static Frame frame = new Frame("Exercise 3");
    static Animator animator = new Animator(canvas);

    World world = new World();

    public static void main(String[] args) {
        canvas.addGLEventListener(new Run());
        frame.add(canvas);
        frame.setSize(1920, 1080);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        frame.setVisible(true);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);              // Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);    // Black Background
        gl.glClearDepth(1.0f);                      // Depth Buffer Setup
        gl.glEnable(GL2.GL_DEPTH_TEST);              // Enables Depth Testing
        gl.glDepthFunc(GL2.GL_LEQUAL);               // The Type Of Depth Testing To Do

        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glEnable(GL2.GL_TEXTURE_2D);
        try {
            grass=TextureIO.newTexture(new File( "textures/grass.jpg" ),true);
            water=TextureIO.newTexture(new File( "textures/water.jpg" ),true);
            ground=TextureIO.newTexture(new File( "textures/ground.jpg" ),true);
            snow=TextureIO.newTexture(new File( "textures/snow.jpg" ),true);
            wood=TextureIO.newTexture(new File( "textures/wood.jpg" ),true);
            leaves=TextureIO.newTexture(new File( "textures/leaves.jpg" ),true);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);

        bark = WavefrontObjectLoader.loadWavefrontObjectAsDisplayList(gl, "objects/bark.obj");
        tree = WavefrontObjectLoader.loadWavefrontObjectAsDisplayList(gl, "objects/tree.obj");
        rotor = WavefrontObjectLoader.loadWavefrontObjectAsDisplayList(gl, "objects/rotor.obj");
        fox = WavefrontObjectLoader.loadWavefrontObjectAsDisplayList(gl, "objects/fox.obj");

        float	ambient[] = {1.0f,0.95f,0.87f,1.0f};
        float	diffuse[] = {1.0f,0.95f,0.87f,1.0f}; // Halogen light

        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glEnable(GL2.GL_LIGHT0);

        if (drawable instanceof Window) {
            Window window = (Window) drawable;
            window.addKeyListener(this);
            window.addMouseListener(this);
        } else if (GLProfile.isAWTAvailable() && drawable instanceof java.awt.Component) {
            java.awt.Component comp = (java.awt.Component) drawable;
            new AWTKeyAdapter(this, drawable).addTo(comp);
            new AWTMouseAdapter(this, drawable).addTo(comp);
        }
    }

    @Override
    public void dispose(GLAutoDrawable arg) {
        GL2 gl = arg.getGL().getGL2();
        gl.glDeleteLists(bark, bark);
        gl.glDeleteLists(tree, tree);
        gl.glDeleteLists(rotor, rotor);
    }

    public void display(GLAutoDrawable drawable) {
        float ambientMaterial[] = {0.4f, 0.4f, 0.4f, 1.0f};
        float diffusiveMaterial[] = {0.8f, 0.8f, 0.8f, 1.0f};
        float fox_mat[] = {0.8f, 0.61f, 0.41f, 1.0f};
        float rotor_mat[] = {0.0f, 0.0f, 0.5f, 1.0f};
        float position[] = {0, 100, 0, 1.0f};

        GL2 gl = drawable.getGL().getGL2();

        gl.glClearColor(0.2f, 0.8f, 1.0f, 0.0f);

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();


        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambientMaterial, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffusiveMaterial, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position, 0);
        gl.glTexParameteri ( GL2.GL_TEXTURE_2D,GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT );
        gl.glTexParameteri( GL2.GL_TEXTURE_2D,GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT );


//        float x1 = MouseInfo.getPointerInfo().getLocation().x;
//        float y1 = MouseInfo.getPointerInfo().getLocation().y;
//        float x2 = MouseInfo.getPointerInfo().getLocation().x;
//        float y2 = MouseInfo.getPointerInfo().getLocation().y;
//        if (x1 != x2) {
//            if (x1 > x2) {
//                dt[0] = -angularVelocity;
//            }
//            if (x1 < x2) {
//                dt[0] = angularVelocity;
//            }
//        }
//        if (y1 != y2) {
//            if (y1 > y2) {
//                dt[1] = -angularVelocity;
//            }
//            if (y1 < y2) {
//                dt[1] = angularVelocity;
//            }
//        }
//
//        try {
//            Robot robot = new Robot();
//            robot.mouseMove((int)(frame.getX() + frame.getWidth()/2), (int)(frame.getY() + frame.getHeight()/2));
//        }
//        catch (java.awt.AWTException e) {
//            e.printStackTrace();
//        }

        float[] direction = {0, 0, 0};
        float[] u = {center[0] - eye[0], center[1] - eye[1], center[2] - eye[2]};
        u = Utility.normalize(u);
        float[] v = Utility.VectorCrossVector(u, up);
        v = Utility.normalize(v);
        if (dv[0] > 0) {
            direction[0] = u[0];
            direction[1] = u[1];
            direction[2] = u[2];
        }
        if (dv[0] < 0) {
            direction[0] = -u[0];
            direction[1] = -u[1];
            direction[2] = -u[2];
        }
        if (dv[1] > 0) {
            direction[0] = v[0];
            direction[1] = v[1];
            direction[2] = v[2];
        }
        if (dv[1] < 0) {
            direction[0] = -v[0];
            direction[1] = -v[1];
            direction[2] = -v[2];
        }
        if (dv[2] > 0) {
            direction[0] = up[0];
            direction[1] = up[1];
            direction[2] = up[2];
        }
        if (dv[2] < 0) {
            direction[0] = -up[0];
            direction[1] = -up[1];
            direction[2] = -up[2];
        }

        for (int i = 0; i < world.getBlueN(); i++) {
            if (CollisionDetect.PointVSTriangle(center, direction, world.getBlueTerrain()[i])) {
                CollisionResponse.PointVSTriangle(eye, center, direction, velocity, world.getBlueTerrain()[i]);
            }
        }
        for (int i = 0; i < world.getBrownN(); i++) {
            if (CollisionDetect.PointVSTriangle(center, direction, world.getBrownTerrain()[i])) {
                CollisionResponse.PointVSTriangle(eye, center, direction, velocity, world.getBrownTerrain()[i]);
            }
        }
        for (int i = 0; i < world.getGreenN(); i++) {
            if (CollisionDetect.PointVSTriangle(center, direction, world.getGreenTerrain()[i])) {
                CollisionResponse.PointVSTriangle(eye, center, direction, velocity, world.getGreenTerrain()[i]);
            }
        }
        for (int i = 0; i < world.getWhiteN(); i++) {
            if (CollisionDetect.PointVSTriangle(center, direction, world.getWhiteTerrain()[i])) {
                CollisionResponse.PointVSTriangle(eye, center, direction, velocity, world.getWhiteTerrain()[i]);
            }
        }

        CameraMovement.translate(eye, center, up, dv);

        if (dt[0] != 0 || dt[1] != 0 || dt[2] != 0) { // skip movement calculations when keyboard not pressed
            CameraMovement.rotate(eye, center, up, dt);
        }

        glu.gluLookAt(eye[0], eye[1], eye[2], center[0], center[1], center[2], up[0], up[1], up[2]);

        water.bind(gl);
        gl.glBegin(GL2.GL_TRIANGLES);
        for (int i = 0; i < world.getBlueN(); i++) {
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(world.getBlueTerrain()[i].getTriangle()[0].getVertex()[0], world.getBlueTerrain()[i].getTriangle()[0].getVertex()[1], world.getBlueTerrain()[i].getTriangle()[0].getVertex()[2]);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(world.getBlueTerrain()[i].getTriangle()[1].getVertex()[0], world.getBlueTerrain()[i].getTriangle()[1].getVertex()[1], world.getBlueTerrain()[i].getTriangle()[1].getVertex()[2]);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(world.getBlueTerrain()[i].getTriangle()[2].getVertex()[0], world.getBlueTerrain()[i].getTriangle()[2].getVertex()[1], world.getBlueTerrain()[i].getTriangle()[2].getVertex()[2]);
        }
        gl.glEnd();

        ground.bind(gl);
        gl.glBegin(GL2.GL_TRIANGLES);
        for (int i = 0; i < world.getBrownN(); i++) {
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(world.getBrownTerrain()[i].getTriangle()[0].getVertex()[0], world.getBrownTerrain()[i].getTriangle()[0].getVertex()[1], world.getBrownTerrain()[i].getTriangle()[0].getVertex()[2]);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(world.getBrownTerrain()[i].getTriangle()[1].getVertex()[0], world.getBrownTerrain()[i].getTriangle()[1].getVertex()[1], world.getBrownTerrain()[i].getTriangle()[1].getVertex()[2]);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(world.getBrownTerrain()[i].getTriangle()[2].getVertex()[0], world.getBrownTerrain()[i].getTriangle()[2].getVertex()[1], world.getBrownTerrain()[i].getTriangle()[2].getVertex()[2]);
        }
        gl.glEnd();

        grass.bind(gl);
        gl.glBegin(GL2.GL_TRIANGLES);
        for (int i = 0; i < world.getGreenN(); i++) {
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(world.getGreenTerrain()[i].getTriangle()[0].getVertex()[0], world.getGreenTerrain()[i].getTriangle()[0].getVertex()[1], world.getGreenTerrain()[i].getTriangle()[0].getVertex()[2]);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(world.getGreenTerrain()[i].getTriangle()[1].getVertex()[0], world.getGreenTerrain()[i].getTriangle()[1].getVertex()[1], world.getGreenTerrain()[i].getTriangle()[1].getVertex()[2]);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(world.getGreenTerrain()[i].getTriangle()[2].getVertex()[0], world.getGreenTerrain()[i].getTriangle()[2].getVertex()[1], world.getGreenTerrain()[i].getTriangle()[2].getVertex()[2]);
        }
        gl.glEnd();

        snow.bind(gl);
        gl.glBegin(GL2.GL_TRIANGLES);
        for (int i = 0; i < world.getWhiteN(); i++) {
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(world.getWhiteTerrain()[i].getTriangle()[0].getVertex()[0], world.getWhiteTerrain()[i].getTriangle()[0].getVertex()[1], world.getWhiteTerrain()[i].getTriangle()[0].getVertex()[2]);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(world.getWhiteTerrain()[i].getTriangle()[1].getVertex()[0], world.getWhiteTerrain()[i].getTriangle()[1].getVertex()[1], world.getWhiteTerrain()[i].getTriangle()[1].getVertex()[2]);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(world.getWhiteTerrain()[i].getTriangle()[2].getVertex()[0], world.getWhiteTerrain()[i].getTriangle()[2].getVertex()[1], world.getWhiteTerrain()[i].getTriangle()[2].getVertex()[2]);
        }
        gl.glEnd();

        gl.glTranslatef(200, 45, 200);
        gl.glScalef(5, 5, 5);
        leaves.bind(gl);
        gl.glCallList(tree);
        wood.bind(gl);
        gl.glCallList(bark);
        gl.glScalef(0.2f, 0.2f, 0.2f);
        gl.glTranslatef(-200, -45, -200);

        gl.glTranslatef(-110, 15, 335);
        leaves.bind(gl);
        gl.glCallList(tree);
        wood.bind(gl);
        gl.glCallList(bark);
        gl.glTranslatef(110, -15, -335);

        gl.glTranslatef(0, 55, -290);
        leaves.bind(gl);
        gl.glCallList(tree);
        wood.bind(gl);
        gl.glCallList(bark);
        gl.glTranslatef(0, -55, 290);

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);


        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, fox_mat, 0);
        if (fox1 && !safe) {
            gl.glTranslatef(210, 400, 750);
            gl.glScalef(0.1f, 0.1f, 0.1f);
            gl.glCallList(fox);
            gl.glScalef(10, 10, 10);
            gl.glTranslatef(-210, -400, -750);
        }
        if (fox2 && !safe) {
            gl.glTranslatef(710, 360, -590);
            gl.glScalef(0.1f, 0.1f, 0.1f);
            gl.glCallList(fox);
            gl.glScalef(10, 10, 10);
            gl.glTranslatef(-710, -360, 590);
        }

        gl.glTranslatef(eye[0], eye[1] + 5, eye[2]);
        gl.glScalef(10, 10, 10);
        gl.glRotatef(90, 0, 0, 1);
        gl.glRotatef(rotor_angle, 1, 0, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, rotor_mat, 0);
        gl.glCallList(rotor);
        gl.glRotatef(-rotor_angle, 1, 0, 0);
        gl.glRotatef(-90, 0, 0, 1);
        gl.glScalef(0.1f, 0.1f, 0.1f);
        gl.glTranslatef(-eye[0], -eye[1] - 5, -eye[2]);
        rotor_angle += 1;

        if (eye[0] > 190 && eye[0] < 230 && eye[1] > 380 && eye[1] < 420 && eye[2] > 730 && eye[2] < 770) {
            fox1 = false;
        }
        if (eye[0] > 690 && eye[0] < 730 && eye[1] > 340 && eye[1] < 380 && eye[2] > -610 && eye[2] < -570) {
            fox2 = false;
        }
        if (!fox1 && !fox2) {
            if (eye[0] > 150 && eye[0] < 250 && eye[1] > 0 && eye[1] < 100 && eye[2] > 150 && eye[2] < 250) {
                safe = true;
            }
        }

        if  (safe) {
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, fox_mat, 0);
            gl.glTranslatef(200, 37, 240);
            gl.glScalef(0.1f, 0.1f, 0.1f);
            gl.glCallList(fox);
            gl.glScalef(10, 10, 10);
            gl.glTranslatef(-200, -37, -240);

            gl.glTranslatef(200, 35, 260);
            gl.glScalef(0.1f, 0.1f, 0.1f);
            gl.glRotatef(180, 0, 1, 0);
            gl.glCallList(fox);
            gl.glRotatef(-180, 0, 1, 0);
            gl.glScalef(10, 10, 10);
            gl.glTranslatef(-200, -35, -260);
        }
//        System.out.println(eye[0] + " " + eye[1] + " " + eye[2]);
        if (help) {
            renderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
            renderer.draw("You are a rescue pilot.", (drawable.getSurfaceWidth() / 2) - 500, drawable.getSurfaceHeight() / 2 + 50);
            renderer.draw("Objective: Find the two foxes that were lost on the snowy mountains,", (drawable.getSurfaceWidth() / 2) - 500, drawable.getSurfaceHeight() / 2);
            renderer.draw("and bring them to safety near the biggest tree.", (drawable.getSurfaceWidth() / 2) - 500, (drawable.getSurfaceHeight() / 2) - 50);
            renderer.draw("[Pressing F2 will skip the search.]", (drawable.getSurfaceWidth() / 2) - 500, (drawable.getSurfaceHeight() / 2) - 100);
            renderer.endRendering();
        }
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        if(height <= 0) {
            height = 1;
        }
        float h = (float)width / (float)height;
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 1000.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glFlush();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 27: // 'Escape'
                exit();
                break;
            case 73: // 'i' - look up
                dt[1] = -angularVelocity;
                break;
            case 75: // 'k' - look down
                dt[1] = angularVelocity;
                break;
            case 76: // 'l' - look right
                dt[0] = angularVelocity;
                break;
            case 74: // 'j' - look left
                dt[0] = -angularVelocity;
                break;
//            case 79: // 'o' - rotate view clockwise
//                dt[2] = -angularVelocity;
//                break;
//            case 85: // 'u' - rotate view counter clockwise
//                dt[2] = angularVelocity;
//                break;
            case 87: // 'w' - move forward in direction of view
                dv[0] = velocity;
                break;
            case 83: // 's' - move backwards
                dv[0] = -velocity;
                break;
            case 68: // 'd' - strafe right
                dv[1] = velocity;
                break;
            case 65: // 'a' - strafe left
                dv[1] = -velocity;
                break;
            case 69: // 'e' - ascend
                dv[2] = velocity;
                break;
            case 81: // 'q' - descend
                dv[2] = -velocity;
                break;
            case KeyEvent.VK_F1: // 'f1' - info
                    help = true;
                break;
            case KeyEvent.VK_F2: // 'f2' - skip stage
                    safe = true;
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 73: // 'i' - look up
            case 75: // 'k' - look down
                dt[1] = 0;
                break;
            case 76: // 'l' - look right
            case 74: // 'j' - look left
                dt[0] = 0;
                break;
//            case 79: // 'o' - rotate view clockwise
//            case 85: // 'u' - rotate view counter clockwise
//                dt[2] = 0;
//                break;
            case 87: // 'w' - move forward in direction of view
            case 83: // 's' - move backwards
                dv[0] = 0;
                break;
            case 68: // 'd' - strafe right
            case 65: // 'a' - strafe left
                dv[1] = 0;
                break;
            case 69: // 'e' - ascend
            case 81: // 'q' - descend
                dv[2] = 0;
                break;
            case KeyEvent.VK_F1: // 'f1' - info
                help = false;
                break;
        }
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mouseWheelMoved(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}

    public static void exit(){
        animator.stop();
        frame.dispose();
        System.exit(0);
    }
}
