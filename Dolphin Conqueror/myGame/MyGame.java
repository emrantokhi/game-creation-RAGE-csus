package myGame;

import java.awt.*;
import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import myGameEngine.*;
import net.java.games.input.Controller;
import ray.input.*;
import ray.rage.*;
import ray.rage.asset.material.Material;
import ray.rage.asset.texture.Texture;
import ray.rage.game.*;
import ray.rage.rendersystem.*;
import ray.rage.rendersystem.Renderable.*;
import ray.rage.scene.*;
import ray.rage.scene.Camera.Frustum.*;
import ray.rage.scene.controllers.*;
import ray.rage.util.BufferUtil;
import ray.rml.*;
import ray.rage.rendersystem.gl4.GL4RenderSystem;
import ray.rage.rendersystem.shader.GpuShaderProgram;
import ray.rage.rendersystem.states.FrontFaceState;
import ray.rage.rendersystem.states.RenderState;
import ray.rage.rendersystem.states.TextureState;

public class MyGame extends VariableFrameRateGame {

	// to minimize variable allocation in update()
	private GL4RenderSystem rs;
	private InputManager im;
	private float elapsTime = 0.0f;
	private String elapsTimeStr, dispStr, dispStr2;
	private ArrayList<SceneNode> planets; 
	private ArrayList<SceneNode> planets2; 
	private ArrayList<SceneNode> allSporks;
	private int allSporkCollectiblesP1;
	private int allSporkCollectiblesP2;
	private int elapsTimeSec, scoreP1, scoreP2;
	private int asteroidCount;
	private SceneNode dolphinCam;
	private SceneNode dolphin2Cam;
	//private SceneNode cameraNode;
	private SceneNode dolphinN;
	private SceneNode dolphinN2;
	//private RenderWindow renderWindow;
	//private SceneManager sceneManager;


    public MyGame() {
        super();
        planets = new ArrayList<SceneNode>();
        allSporks = new ArrayList<SceneNode>();
        planets2 = new ArrayList<SceneNode>();
		System.out.println("Visit all the planets and collect all of the All Sporks!");
		System.out.println("Don't stray too far from your mount, and points are only");
		System.out.println("Collected by unmounting and running into the planet/All Sporks!");
		System.out.println("Player1 Controls----");
        System.out.println("press W to move forward");
		System.out.println("press S to move backward");
		System.out.println("press A to move left");
		System.out.println("press D to move right");
		System.out.println("Player2 Controls----");
		System.out.println("R2/RT is to move forward");
		System.out.println("L2/LT is to move backward");
		System.out.println("Controlling the dolphin is the left analog stick");
    }

    public static void main(String[] args) {
        Game game = new MyGame();
        try {
            game.startup();
            game.run();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            game.shutdown();
            game.exit();
        }
    }
	
	@Override
	protected void setupWindow(RenderSystem rs, GraphicsEnvironment ge) {
		rs.createRenderWindow(new DisplayMode(1000, 700, 24, 60), false);
	}

    @Override
    protected void setupCameras(SceneManager sm, RenderWindow rw) {
        SceneNode rootNode = sm.getRootSceneNode();
        Camera playerCamera = sm.createCamera("MainCamera", Projection.PERSPECTIVE);
        Camera player2Camera = sm.createCamera("MainCamera2", Projection.PERSPECTIVE);
        rw.getViewport(0).setCamera(playerCamera);
        rw.getViewport(1).setCamera(player2Camera);
        
        //renderWindow = rw;
		//sceneManager = sm;
		
       /* playerCamera.setRt((Vector3f)Vector3f.createFrom(1.0f, 0.0f, 0.0f));
		playerCamera.setUp((Vector3f)Vector3f.createFrom(0.0f, 1.0f, 0.0f));
		playerCamera.setFd((Vector3f)Vector3f.createFrom(0.0f, 0.0f, 1.0f));
		
		playerCamera.setPo((Vector3f)Vector3f.createFrom(5.0f, 5.0f, 5.0f));
		*/
		playerCamera.setMode('n'); //keep camera in node mode
		player2Camera.setMode('n');
   
		dolphinCam = rootNode.createChildSceneNode("DolphinCameraNode");  //Make dolphinCam and make it a scene node
		dolphinCam.attachObject(playerCamera);
		
		dolphin2Cam = rootNode.createChildSceneNode("Dolphin2CameraNode");
		dolphin2Cam.attachObject(player2Camera);

		dolphinCam.setLocalPosition(0.0f, 0.2f, -3.0f);
		dolphin2Cam.setLocalPosition(0.0f, 0.2f, -3.0f);
    }
    
    @Override
    protected void setupWindowViewports(RenderWindow rw) {
    	rw.addKeyListener(this);
    	Viewport topViewPort = rw.getViewport(0);
    	topViewPort.setDimensions(.51f, .01f, .99f, .49f);
    	//topViewPort.setClearColor(new Color(1.0f, .7f, .7f));
    	
    	Viewport botViewport = rw.createViewport(.01f, .01f, .99f, .49f);
    	//botViewport.setClearColor(new Color(.5f, 1.0f, .5f));
    }
	
    @Override
    protected void setupScene(Engine eng, SceneManager sm) throws IOException {
    	//Calling entities
        Entity dolphinE = sm.createEntity("myDolphin", "dolphinHighPoly.obj");
        Entity dolphinE2 = sm.createEntity("dolphinPlayer2", "dolphinHighPoly.obj");
        Entity earthE = sm.createEntity("theEarth", "earth.obj");
        Entity sunE = sm.createEntity("theSun", "sphere.obj");
        Entity moonE = sm.createEntity("theMoon", "sphere.obj");
        Entity chainPlanetE = sm.createEntity("chainPlanet", "sphere.obj");
        ManualObject baseTriangle1E = createBaseTriangle1(eng, sm);
        ManualObject baseTriangle2E = createBaseTriangle2(eng, sm);
        ManualObject allSporks1E = createAllSporks(eng, sm);
        asteroidCount++;
        ManualObject allSporks2E = createAllSporks(eng, sm);
        asteroidCount++;
        ManualObject allSporks3E = createAllSporks(eng, sm);
        asteroidCount++;
        
        //Set the primitives of each type
        dolphinE.setPrimitive(Primitive.TRIANGLES);
        dolphinE2.setPrimitive(Primitive.TRIANGLES);
        earthE.setPrimitive(Primitive.TRIANGLES);
        sunE.setPrimitive(Primitive.TRIANGLES);
        moonE.setPrimitive(Primitive.TRIANGLES);
        chainPlanetE.setPrimitive(Primitive.TRIANGLES);
        baseTriangle1E.setPrimitive(Primitive.TRIANGLES);
        baseTriangle2E.setPrimitive(Primitive.TRIANGLES);
        allSporks1E.setPrimitive(Primitive.TRIANGLES);
        allSporks2E.setPrimitive(Primitive.TRIANGLES);
        allSporks3E.setPrimitive(Primitive.TRIANGLES);
        
        
        //TEXTURE SETTINGS
        Texture moonText = eng.getTextureManager().getAssetByPath("moon.jpeg");
        TextureState moonTextState = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
        moonTextState.setTexture(moonText);
        moonE.setRenderState(moonTextState);
        
        Texture sunText = eng.getTextureManager().getAssetByPath("yellow.jpg");
        TextureState sunTextState = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
        sunTextState.setTexture(sunText);
        sunE.setRenderState(sunTextState);
        
        Texture chainText = eng.getTextureManager().getAssetByPath("chain-fence.jpeg");
        TextureState chainTextState = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
        chainTextState.setTexture(chainText);
        chainPlanetE.setRenderState(chainTextState); 
        
        Texture baseTriangle1Text = eng.getTextureManager().getAssetByPath("red.jpeg");
        TextureState baseTriangle1Tstate = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
        baseTriangle1Tstate.setTexture(baseTriangle1Text);
        baseTriangle1E.setRenderState(baseTriangle1Tstate);
        baseTriangle2E.setRenderState(baseTriangle1Tstate);
        
        Texture allSporksText = eng.getTextureManager().getAssetByPath("moon.jpeg");
    	TextureState allSporksTState = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
    	allSporksTState.setTexture(allSporksText);
    	allSporks1E.setRenderState(allSporksTState);
    	allSporks2E.setRenderState(allSporksTState);
    	allSporks3E.setRenderState(allSporksTState);
        
        //Create the nodes that the entity objects will be attached too
        dolphinN = sm.getRootSceneNode().createChildSceneNode(dolphinE.getName() + "Node");
        dolphinN2 = sm.getRootSceneNode().createChildSceneNode(dolphinE2.getName() + "Node");
        SceneNode earthN = sm.getRootSceneNode().createChildSceneNode(earthE.getName() + "Node");
        SceneNode sunN = sm.getRootSceneNode().createChildSceneNode(sunE.getName() + "Node");
        SceneNode moonN = sm.getRootSceneNode().createChildSceneNode(moonE.getName() + "Node");
        SceneNode chainPlanetN = sm.getRootSceneNode().createChildSceneNode(chainPlanetE.getName() + "Node");
        SceneNode baseTriangle1N = sm.getRootSceneNode().createChildSceneNode(baseTriangle1E.getName() + "Node");
        SceneNode baseTriangle2N = sm.getRootSceneNode().createChildSceneNode(baseTriangle2E.getName() + "Node");
        SceneNode allSporks1N = sm.getRootSceneNode().createChildSceneNode(allSporks1E.getName() + "Node");
        SceneNode allSporks2N = sm.getRootSceneNode().createChildSceneNode(allSporks2E.getName() + "Node");
        SceneNode allSporks3N = sm.getRootSceneNode().createChildSceneNode(allSporks3E.getName() + "Node");
        
        
        //Set the position of the nodes
        dolphinN.moveBackward(2.0f);
        dolphinN2.moveForward(5.0f);
        earthN.setLocalPosition(10.0f, 3.0f, 20.0f);
        moonN.setLocalPosition(-15.0f, 3.0f, -30.0f);
        sunN.setLocalPosition(10.0f, 12.0f, 0.0f);
        chainPlanetN.setLocalPosition(65.0f, 3.0f, 10.0f);
        baseTriangle1N.setLocalPosition(0.0f, 0.0f, 0.0f);
        baseTriangle2N.setLocalPosition(0.0f, 0.0f, 0.0f);
        allSporks1N.setLocalPosition(-83.0f, 1.0f, 20.0f);
        allSporks2N.setLocalPosition(30.0f, 1.0f, 70.0f);
        allSporks3N.setLocalPosition(95.0f, 1.0f, 50.0f);
     
        
        //Attach the entities to the created and set nodes
        dolphinN.attachObject(dolphinE);
        dolphinN2.attachObject(dolphinE2);
        earthN.attachObject(earthE);
        moonN.attachObject(moonE);
        sunN.attachObject(sunE);
        chainPlanetN.attachObject(chainPlanetE);
        baseTriangle1N.attachObject(baseTriangle1E);
        baseTriangle2N.attachObject(baseTriangle2E);
        allSporks1N.attachObject(allSporks1E);
        allSporks2N.attachObject(allSporks2E);
        allSporks3N.attachObject(allSporks3E);
        
        
        //Add the planets to the ArrayList of planets to check collision based on distance
        planets.add(earthN);
        planets.add(moonN);
        planets.add(chainPlanetN);
        
        planets2.add(earthN);
        planets2.add(moonN);
        planets2.add(chainPlanetN);
        
        //Add allSporks to the arraylist of asteroids for collision check
        allSporks.add(allSporks1N);
        allSporks.add(allSporks2N);
        allSporks.add(allSporks3N);
        
        //Scale the planets to a size
        earthN.scale(2.0f, 2.0f, 2.0f);
        moonN.scale(2.5f, 2.5f, 2.5f);
        sunN.scale(5.0f, 5.0f, 5.0f);
        baseTriangle1N.scale(500.0f, 1.0f, 500.0f);
        baseTriangle2N.scale(500.0f, 1.0f, 500.0f);
        chainPlanetN.scale(5.0f, 5.0f, 5.0f);
        allSporks1N.scale(0.5f, 0.5f, 0.5f);
        allSporks2N.scale(0.5f, 0.5f, 0.5f);
        allSporks3N.scale(0.5f, 0.5f, 0.5f);
        
        
        dolphinN.attachChild(dolphinCam); //Attach the dolphin cam node as a child to the dolphin
        dolphinN2.attachChild(dolphin2Cam);
        //dolphinN.detachAllChildren(); //detatch it just so you can have an initial position behind the dolphin
        
        //Add an ambient light to the scene manager
        sm.getAmbientLight().setIntensity(new Color(.4f, .4f, .4f));
		
        //Add a point light to the scene manager
		Light plight = sm.createLight("testLamp1", Light.Type.POINT);
		plight.setAmbient(new Color(.3f, .3f, .3f));
        plight.setDiffuse(new Color(.7f, .7f, .7f));
		plight.setSpecular(new Color(1.0f, 1.0f, 1.0f));
        plight.setRange(5f);
		
		SceneNode plightNode = sm.getRootSceneNode().createChildSceneNode("plightNode");
        plightNode.attachObject(plight);
        //setupOrbitCamera(eng, sm);
        setupInputs();
    }
    
   /* protected void setupOrbitCamera(Engine eng, SceneManager sm) {
    	SceneNode cameraNode = sm.getSceneNode("DolphinCameraNode");
    	Camera camera = sm.getCamera("MainCamera");
    	String gamepadName = im.getFirstGamepadName();
    	//Camera3Pcontroller orbit
        
        
    }*/

    protected ManualObject createBaseTriangle1(Engine eng, SceneManager sm) throws IOException{
    	ManualObject baseTriangle1 = sm.createManualObject("baseTriangle1");
        ManualObjectSection baseTriangle1Sec = baseTriangle1.createManualSection("baseTriangleSection");
        
      //FOR THE WORLD AXES
        Material baseTriangle1Mat = sm.getMaterialManager().getAssetByPath("default.mtl");
        Texture baseTriangle1Text = eng.getTextureManager().getAssetByPath("default.png");
        
        TextureState baseTriangle1Tstate = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
        baseTriangle1Tstate.setTexture(baseTriangle1Text);
        baseTriangle1Sec.setRenderState(baseTriangle1Tstate);
        baseTriangle1Sec.setMaterial(baseTriangle1Mat);
        
        baseTriangle1.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
        
        FrontFaceState faceState = (FrontFaceState) sm.getRenderSystem().createRenderState(RenderState.Type.FRONT_FACE);
        faceState.setVertexWinding(FrontFaceState.VertexWinding.CLOCKWISE);
        
      //vertices for the triangle
        float[] baseTriangle1Vertices = new float[]{
    			2.0f, -2.0f, 2.0f, -2.0f, -2.0f, -2.0f, 2.0f, -2.0f, -2.0f  //RR
        };
        
        float[] baseTriangle1TextCoord = new float[]{   
    			2.0f, 2.0f, 0.0f, 0.0f, 2.0f, 0.0f
    	};

        float[] normals = new float[]{   
    			0.0f, -2.0f, 0.0f, 0.0f, -2.0f, 0.0f, 0.0f, -2.0f, 0.0f
    	}; 
        
        int[] indices = new int[] { 0,1,2,3,4,5,6,7,8};
        
        FloatBuffer verticesBuffer = BufferUtil.directFloatBuffer(baseTriangle1Vertices);
    	FloatBuffer textBuffer = BufferUtil.directFloatBuffer(baseTriangle1TextCoord);
    	FloatBuffer normBuffer = BufferUtil.directFloatBuffer(normals);
    	IntBuffer indexBuffer = BufferUtil.directIntBuffer(indices);
    	
    	//Setting the buffers
    	baseTriangle1Sec.setVertexBuffer(verticesBuffer);
    	baseTriangle1Sec.setTextureCoordsBuffer(textBuffer);
    	baseTriangle1Sec.setNormalsBuffer(normBuffer);
    	baseTriangle1Sec.setIndexBuffer(indexBuffer);
    	
    	baseTriangle1.setDataSource(DataSource.INDEX_BUFFER);
    	baseTriangle1.setRenderState(baseTriangle1Tstate);
    	baseTriangle1.setRenderState(faceState);
    	return baseTriangle1; 
    } 
    
    protected ManualObject createBaseTriangle2(Engine eng, SceneManager sm) throws IOException{
    	ManualObject baseTriangle2 = sm.createManualObject("baseTriangle2");
        ManualObjectSection baseTriangle2Sec = baseTriangle2.createManualSection("baseTriangleSection2");
        
      //FOR THE WORLD AXES
        Material baseTriangle2Mat = sm.getMaterialManager().getAssetByPath("default.mtl");
        Texture baseTriangle2Text = eng.getTextureManager().getAssetByPath("default.png");
        
        TextureState baseTriangle2Tstate = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
        baseTriangle2Tstate.setTexture(baseTriangle2Text);
        baseTriangle2Sec.setRenderState(baseTriangle2Tstate);
        baseTriangle2Sec.setMaterial(baseTriangle2Mat);
        
        baseTriangle2.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
        
        FrontFaceState faceState = (FrontFaceState) sm.getRenderSystem().createRenderState(RenderState.Type.FRONT_FACE);
        faceState.setVertexWinding(FrontFaceState.VertexWinding.CLOCKWISE);
        
      //vertices for the triangle
        float[] baseTriangle2Vertices = new float[]{
        		-2.0f, -2.0f, -2.0f, 2.0f, -2.0f, 2.0f, -2.0f, -2.0f, 2.0f //LF
        };
        
        float[] baseTriangle2TextCoord = new float[]{   
    			0.0f, 0.0f, 2.0f, 2.0f, 0.0f, 2.0f
    	};

        float[] normals = new float[]{   
    			0.0f, -2.0f, 0.0f, 0.0f, -2.0f, 0.0f, 0.0f, -2.0f, 0.0f
    	}; 
        
        int[] indices = new int[] { 0,1,2,3,4,5,6,7,8};
        
        FloatBuffer verticesBuffer = BufferUtil.directFloatBuffer(baseTriangle2Vertices);
    	FloatBuffer textBuffer = BufferUtil.directFloatBuffer(baseTriangle2TextCoord);
    	FloatBuffer normBuffer = BufferUtil.directFloatBuffer(normals);
    	IntBuffer indexBuffer = BufferUtil.directIntBuffer(indices);
    	
    	//Setting the buffers
    	baseTriangle2Sec.setVertexBuffer(verticesBuffer);
    	baseTriangle2Sec.setTextureCoordsBuffer(textBuffer);
    	baseTriangle2Sec.setNormalsBuffer(normBuffer);
    	baseTriangle2Sec.setIndexBuffer(indexBuffer);
    	
    	baseTriangle2.setDataSource(DataSource.INDEX_BUFFER);
    	baseTriangle2.setRenderState(baseTriangle2Tstate);
    	baseTriangle2.setRenderState(faceState);
    	return baseTriangle2; 
    } 
    
    protected ManualObject createAllSporks(Engine eng, SceneManager sm) throws IOException {
    	ManualObject allSporks = sm.createManualObject("Asteroids" + asteroidCount);
    	ManualObjectSection allSporksSec = allSporks.createManualSection("AllSporksSection" + asteroidCount);
    	
    	Material allSporksMat = sm.getMaterialManager().getAssetByPath("asteroid_1.mtl");
    	Texture allSporksText = eng.getTextureManager().getAssetByPath("moon.jpeg");
    	TextureState allSporksTState = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
    	allSporksTState.setTexture(allSporksText);
    	allSporksSec.setRenderState(allSporksTState);
    	allSporksSec.setMaterial(allSporksMat);
    	FrontFaceState faceState = (FrontFaceState) sm.getRenderSystem().createRenderState(RenderState.Type.FRONT_FACE);
    	
    	allSporks.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
    	
    	//vertices for the all sporks
    	float[] allSporksVertices = new float[] {
    			-2.0f, -2.0f, 2.0f, 2.0f, -2.0f, 2.0f, 0.0f, 2.0f, 0.0f,    //Front
    			2.0f, -2.0f, 2.0f, 2.0f, -2.0f, -2.0f, 0.0f, 2.0f, 0.0f,    //Right side
    			2.0f, -2.0f, -2.0f, -2.0f, -2.0f, -2.0f, 0.0f, 2.0f, 0.0f,  //Back side
    			-2.0f, -2.0f, -2.0f, -2.0f, -2.0f, 2.0f, 0.0f, 2.0f, 0.0f,  //Left side
    			-2.0f, -2.0f, -2.0f, 2.0f, -2.0f, 2.0f, -2.0f, -2.0f, 2.0f, //LF
    			2.0f, -2.0f, 2.0f, -2.0f, -2.0f, -2.0f, 2.0f, -2.0f, -2.0f  //RR
    	};
    	
    	float[] allSporkTextCoord = new float[]{   
    			0.0f, 0.0f, 2.0f, 0.0f, 1.0f, 2.0f,
    			0.0f, 0.0f, 2.0f, 0.0f, 1.0f, 2.0f,
    			0.0f, 0.0f, 2.0f, 0.0f, 1.0f, 2.0f,
    			0.0f, 0.0f, 2.0f, 0.0f, 1.0f, 2.0f,
    			0.0f, 0.0f, 2.0f, 2.0f, 0.0f, 2.0f,
    			2.0f, 2.0f, 0.0f, 0.0f, 2.0f, 0.0f
    	};
    	
    	float[] normals = new float[]{   
    			0.0f, 2.0f, 2.0f, 0.0f, 2.0f, 2.0f, 0.0f, 2.0f, 2.0f,
    			2.0f, 2.0f, 0.0f, 2.0f, 2.0f, 0.0f, 2.0f, 2.0f, 0.0f,
    			0.0f, 2.0f, -2.0f, 0.0f, 2.0f, -2.0f, 0.0f, 2.0f, -2.0f,
    			-2.0f, 2.0f, 0.0f, -2.0f, 2.0f, 0.0f, -2.0f, 2.0f, 0.0f,
    			0.0f, -2.0f, 0.0f, 0.0f, -2.0f, 0.0f, 0.0f, -2.0f, 0.0f,
    			0.0f, -2.0f, 0.0f, 0.0f, -2.0f, 0.0f, 0.0f, -2.0f, 0.0f
    	}; 
    	int[] indices = new int[] { 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17};
    	
    	FloatBuffer verticesBuffer = BufferUtil.directFloatBuffer(allSporksVertices);
    	FloatBuffer textBuffer = BufferUtil.directFloatBuffer(allSporkTextCoord);
    	FloatBuffer normBuffer = BufferUtil.directFloatBuffer(normals);
    	IntBuffer indexBuffer = BufferUtil.directIntBuffer(indices);
    	
    	allSporksSec.setVertexBuffer(verticesBuffer);
    	allSporksSec.setTextureCoordsBuffer(textBuffer);
    	allSporksSec.setNormalsBuffer(normBuffer);
    	allSporksSec.setIndexBuffer(indexBuffer);
    	
    	allSporks.setDataSource(DataSource.INDEX_BUFFER);
    	allSporks.setRenderState(allSporksTState);
    	allSporks.setRenderState(faceState);
    	return allSporks;
    }
    
    
    protected void gameOver(Node theSun, SceneManager sm) {
    	if(planets.isEmpty()) {
    		for(SceneNode planets : planets2) {
    			theSun.attachChild(planets);
    		}
    		OrbitController oc = new OrbitController(theSun, 2.0f);
    		oc.addNode(theSun);
    		sm.addController(oc);
    	}
    }
    
    @Override
    protected void update(Engine engine) {
		// build and set HUD
		rs = (GL4RenderSystem) engine.getRenderSystem();
		//renderWindow = rs.getRenderWindow();
	    //SceneManager sm = engine.getSceneManager();
		elapsTime += engine.getElapsedTimeMillis();
		im.update(elapsTime);
		elapsTimeSec = Math.round(elapsTime/1000.0f);
		elapsTimeStr = Integer.toString(elapsTimeSec);
		//gameOver(sm.getRootSceneNode().getChild("theSunNode"), sm);
		planetPoints(engine);
		allSporkPoints(engine);
		dispStr = "Time = " + elapsTimeStr + "  --   Score: " + scoreP1 + "   --   All Sporks!: " 
				+ allSporkCollectiblesP1;
		dispStr2 = "Time = " + elapsTimeStr + "  --   Score: " + scoreP2 + "   --   All Sporks!: " 
				+ allSporkCollectiblesP2;
		int p2HudLocation = rs.getRenderWindow().getViewport(1).getActualHeight() * 2 - 2;
		rs.setHUD(dispStr, 20, 15);
		rs.setHUD2(dispStr2, 20, p2HudLocation);
	}
    
    
    protected void planetPoints(Engine engine) {
    	ArrayList<SceneNode> toRemove = new ArrayList<SceneNode>();
    	SceneManager sm = engine.getSceneManager();
    	RotationController rc = new RotationController(Vector3f.createUnitVectorY(), .02f);
    	BounceController bc = new BounceController();
    	for(SceneNode planet : planets) {
    		Vector3f a = (Vector3f) dolphinN.getLocalPosition();
    		Vector3f c = (Vector3f) dolphinN2.getLocalPosition();
    		Vector3f b = (Vector3f) planet.getLocalPosition();
    		float distance1 = (float) Math.sqrt((b.x() - a.x()) * (b.x() - a.x()) + (b.y() - a.y()) *
    				(b.y() - a.y()) + (b.z() - a.z()) * (b.z() - a.z())); //literally the distance formula
    		float distance2 = (float) Math.sqrt((b.x() - c.x()) * (b.x() - c.x()) + (b.y() - c.y()) *
    				(b.y() - c.y()) + (b.z() - c.z()) * (b.z() - c.z()));
    		if(distance1 <= 4.0f) {
    			scoreP1++;
    			rc.addNode(planet);
    			sm.addController(rc); //rotate the planet when Player 1 gets the planet
    			toRemove.add(planet);
    		}
    		if(distance2 <= 4.0f) {
    			scoreP2++;
    			bc.addNode(planet);
    			sm.addController(bc);
    			toRemove.add(planet);
    		}
    	}
    	planets.removeAll(toRemove);
    }
    
    protected void allSporkPoints(Engine engine) {
    	ArrayList<SceneNode> toRemove = new ArrayList<SceneNode>();
    	SceneManager sm = engine.getSceneManager();
    	BounceController bc = new BounceController();
    	RotationController rc = new RotationController(Vector3f.createUnitVectorY(), .02f);
    	for(SceneNode allSpork : allSporks) {
    		Vector3f a = (Vector3f) dolphinN.getLocalPosition();
    		Vector3f c = (Vector3f) dolphinN2.getLocalPosition();
    		Vector3f b = (Vector3f) allSpork.getLocalPosition();
    		float distance1 = (float) Math.sqrt((b.x() - a.x()) * (b.x() - a.x()) + (b.y() - a.y()) *
    				(b.y() - a.y()) + (b.z() - a.z()) * (b.z() - a.z())); //literally the distance formula
    		float distance2 = (float) Math.sqrt((b.x() - c.x()) * (b.x() - c.x()) + (b.y() - c.y()) *
    				(b.y() - c.y()) + (b.z() - c.z()) * (b.z() - c.z()));
    		if(distance1 <= 2.0f) {
    			allSporkCollectiblesP1++;
    			rc.addNode(allSpork);
    			sm.addController(rc); //rotate the allspork if player1
    			toRemove.add(allSpork);
    		}
    		if(distance2 <= 2.0f) {
    			allSporkCollectiblesP2++;
    			bc.addNode(allSpork);
    			sm.addController(bc);
    			toRemove.add(allSpork);
    			
    		}
    	}
    	allSporks.removeAll(toRemove);
    }

    protected void setupInputs() {
    	im = new GenericInputManager();
    	ArrayList<Controller> controllers = im.getControllers();
    	
    	//KEYBOARD COMMANDS 
    	MoveForward moveForwardAction = new MoveForward(dolphinN);
    	MoveBackward moveBackwardAction = new MoveBackward(dolphinN);
    	//MoveLeft moveLeftAction = new MoveLeft(dolphinN);
    	//MoveRight moveRightAction = new MoveRight(dolphinN);
    	GlobalYawPositive globalYawPositive = new GlobalYawPositive(dolphinN);
    	GlobalYawNegative globalYawNegative = new GlobalYawNegative(dolphinN);
    	//LocalPitchPositive localPitchPositive = new LocalPitchPositive(dolphinN);
    	//LocalPitchNegative localPitchNegative = new LocalPitchNegative(dolphinN);
    
    	//GAMEPAD CMDS
    	//GamepadX xLeftAnalog = new GamepadX(dolphinN);
    	GamepadY yLeftAnalog = new GamepadY(dolphinN2);
    	GamepadYaw yawRightAnalog = new GamepadYaw(dolphinN2);
    	//GamepadPitch pitchRightAnalog = new GamepadPitch(dolphinN);
    	
    	for(Controller c: controllers) {
    		//KEYBOARD CONTROLS------------------------------------------------------------------------------------------------
    		if(c.getType() == Controller.Type.KEYBOARD) {
    			//Moving forward w/ W
    			im.associateAction(c.getName(), net.java.games.input.Component.Identifier.Key.W, moveForwardAction,
    					InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    			//Moving Backward w/ S
    			im.associateAction(c.getName(), net.java.games.input.Component.Identifier.Key.S, moveBackwardAction,
    					InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    			//Moving Right w/ D
    			im.associateAction(c.getName(), net.java.games.input.Component.Identifier.Key.D, globalYawNegative,
    					InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    			//Moving Left w/ A
    			im.associateAction(c.getName(), net.java.games.input.Component.Identifier.Key.A, globalYawPositive,
    					InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    			//Rotate about the V axis positive
    			/*im.associateAction(c.getName(), net.java.games.input.Component.Identifier.Key.LEFT, globalYawPositive,
    					InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    			//Rotate about the V axis negative
    			im.associateAction(c.getName(), net.java.games.input.Component.Identifier.Key.RIGHT, globalYawNegative,
    					InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN); */
    			//Rotate about the U axis positive
    			/*im.associateAction(c.getName(), net.java.games.input.Component.Identifier.Key.UP, localPitchPositive,
    					InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    			//Rotate about the U axis positive
    			im.associateAction(c.getName(), net.java.games.input.Component.Identifier.Key.DOWN, localPitchNegative,
    					InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN); */
    			
    		//GAMEPAD CONTROLS MODE-------------------------------------------------------------------------------------------------
    		} else if((c.getType() == Controller.Type.GAMEPAD || c.getType() == Controller.Type.STICK)) {
    			//Move Left and Right
    			im.associateAction(c.getName(), net.java.games.input.Component.Identifier.Axis.X, yawRightAnalog,
    					InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    			//Move Foward and Backward
    			im.associateAction(c.getName(), net.java.games.input.Component.Identifier.Axis.Z, yLeftAnalog,
    					InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    			//Move Yaw
    			//im.associateAction(c.getName(), net.java.games.input.Component.Identifier.Axis.RX, xLeftAnalog,
    					//InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    			//Move Pitch
    			//im.associateAction(c.getName(), net.java.games.input.Component.Identifier.Axis.Y, pitchRightAnalog,
    					//InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    		}
    }
    	
   }
}
