import com.sun.net.httpserver.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import java.lang.*;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.util.stream.*;

abstract class ElementInterface {
  protected int _posX;
  protected int _posY;

  public ElementInterface(int x, int y) {
    _posX=x;
    _posY=y;
  }

  public abstract int getWidth();
  public abstract int getHeight();
  public abstract void rotateCW();
  public abstract void rotateCCW();
  public abstract boolean getPixel(int x, int y);
  public abstract ElementInterface clone();
  
  public int getPosX()
  {
    return _posX;
  }

  public int getPosY()
  {
    return _posY;
  }

  void setPosX(int x) {
    _posX = x;
  } 

  void setPosY(int y) {
    _posY = y;
  }
};

abstract class BlockBase extends ElementInterface {
  protected int _rotation;
  protected int _maxRotation;
  protected int _width[] ;
  protected int _height[];
  protected boolean _pattern[][][];

  BlockBase(int x, int y, int rotation, int maxRotation, int width[], int height[], boolean pattern[][][]) {
    super(x,y);
    _rotation = rotation;
    _maxRotation = maxRotation - 1;
    _width = width;
    _height = height;
    _pattern = pattern;
  }

  public int getWidth()
  {
    return _width[_rotation];
  }

  public int getHeight()
  {
    return _height[_rotation];
  }

  public void rotateCCW()
  {
    _rotation +=1;
    if (_rotation > _maxRotation)
      _rotation = 0;
  }

  public void rotateCW()
  {
    _rotation -=1;
    if (_rotation < 0)
      _rotation = _maxRotation;
  }

  public boolean getPixel(int x, int y) {
    if ((x >= _posX) && ( x < _posX + getWidth()) && (y >= _posY) && (y < _posY + getHeight()))
    {
      boolean ret = _pattern[_rotation][y-_posY][x-_posX];
      return ret;
    }
    return false;
  }
}

class SquareBlock extends BlockBase {
  protected static int maxRotation = 1;
  protected static int width[] = {2};
  protected static int height[] = {2};
  protected static boolean pattern[][][] = { { {true, true}, 
                                           {true, true} } };

  SquareBlock(int x, int y, int rotation) {
    super(x,y, rotation, maxRotation, width, height, pattern);
  }

  SquareBlock(int x, int y) {
    this(x,y, 0);
  }

  public ElementInterface clone(){
    return new SquareBlock(_posX, _posY, _rotation);
  }
}

class LLBlock extends BlockBase {
  protected static int maxRotation = 4;
  protected static int width[] = { 3, 2, 3, 2};
  protected static int height[] = { 2, 3, 2, 3 };
  protected static boolean pattern[][][] =  { { { true, false, false },
                                                 { true, true, true} },
                                               { {true, true},
                                                 {true, false},
                                                  {true, false} }, 
                                              { { true, true, true },
                                                { false, false, true} },
                                              { {false, true},
                                                {false, true},
                                                {true, true} } };

  LLBlock(int x, int y, int rotation) {
    super(x,y, rotation, maxRotation, width, height, pattern);
  }

  LLBlock(int x, int y) {
    this(x, y, 0);
  }

  public ElementInterface clone(){
    return new LLBlock(_posX, _posY, _rotation);
  }
}

class RLBlock extends BlockBase {
  protected static int maxRotation = 4;
  protected static int width[] = { 3, 2, 3, 2};
  protected static int height[] = { 2, 3, 2, 3 };
  protected static boolean pattern[][][] =  { { { false, false, true },
                                                 { true, true, true} },
                                               { {true, false},
                                                 {true, false},
                                                  {true, true} }, 
                                              { { true, true, true },
                                                { true, false, false} },
                                              { {true, true},
                                                {false, true},
                                                {false, true} } };

  RLBlock(int x, int y, int rotation) {
    super(x,y, rotation, maxRotation, width, height, pattern);
  }

  RLBlock(int x, int y) {
    this(x, y, 0);
  }

  public ElementInterface clone(){
    return new RLBlock(_posX, _posY, _rotation);
  }
}

class LineBlock extends BlockBase {
  protected static int maxRotation = 2;
  protected static int width[] = { 4, 1};
  protected static int height[] = { 1, 4};
  protected static boolean pattern[][][] =  { { { true, true, true, true } },
                                              { {true},
                                                {true},
                                                {true},
                                                {true} } };  
                                                 
                                               
  LineBlock(int x, int y, int rotation) {
    super(x,y, rotation, maxRotation, width, height, pattern);
  }

  LineBlock(int x, int y) {
    this(x, y, 0);
  }

  public ElementInterface clone(){
    return new LineBlock(_posX, _posY, _rotation);
  }
}

class LZagBlock extends BlockBase {
  protected static int maxRotation = 2;
  protected static int width[] = { 3, 2};
  protected static int height[] = { 2, 3};
  protected static boolean pattern[][][] =  { { { true, true, false},
                                                { false, true, true} },
                                              { {false, true},
                                                {true, true},
                                                {true, false} } };
                                                
                                               
  LZagBlock(int x, int y, int rotation) {
    super(x,y, rotation, maxRotation, width, height, pattern);
  }

  LZagBlock(int x, int y) {
    this(x, y, 0);
  }

  public ElementInterface clone(){
    return new LZagBlock(_posX, _posY, _rotation);
  }
}

class RZagBlock extends BlockBase {
  protected static int maxRotation = 2;
  protected static int width[] = { 3, 2};
  protected static int height[] = { 2, 3};
  protected static boolean pattern[][][] =  { { { false, true, true},
                                                { true, true, false} },
                                              { {true, false},
                                                {true, true},
                                                {false, true} } };
                                                
                                               
  RZagBlock(int x, int y, int rotation) {
    super(x,y, rotation, maxRotation, width, height, pattern);
  }

  RZagBlock(int x, int y) {
    this(x, y, 0);
  }

  public ElementInterface clone(){
    return new RZagBlock(_posX, _posY, _rotation);
  }
}

class TBlock extends BlockBase {
  protected static int maxRotation = 4;
  protected static int width[] = { 3, 2, 3, 2};
  protected static int height[] = { 2, 3, 2, 3};
  protected static boolean pattern[][][] =  { { { false, true, false},
                                                { true, true, true} },
                                              { {true, false},
                                                {true, true},
                                                {true, false} },
                                              { { true, true, true},
                                                { false, true, false} },
                                              { {false, true},
                                                {true, true},
                                                {false, true} } };
                                                
                                               
  TBlock(int x, int y, int rotation) {
    super(x,y, rotation, maxRotation, width, height, pattern);
  }

  TBlock(int x, int y) {
    this(x, y, 0);
  }

  public ElementInterface clone(){
    return new TBlock(_posX, _posY, _rotation);
  }
}

class Game {
  private String _userId;
  private int _statCnt;
  private int _keyPress;
  private boolean _frozeStack[][];
  private boolean _renderBuffer[][];

  private ElementInterface _element;
  private ElementInterface _nextMove;

  private ElementInterface  generateElement() {
    Random ran = new Random();
    int choice = ran.nextInt(7);
    System.out.println("Choice: " + choice);
    switch (choice) {
      case 0:
        return new SquareBlock(4,0);
      case 1:
        return new LLBlock(4, 0);
      case 2:
        return new RLBlock(4, 0);
      case 3:
        return new LineBlock(4, 0);
      case 4:
        return new LZagBlock(4, 0);
      case 5:
        return new RZagBlock(4, 0);
      case 6:
        return new TBlock(4, 0);
      default:
        break;
    }
    return new SquareBlock(4, 0);
  }


  Game(String userId) {
    _userId = userId;
    _statCnt = 0; 
    _keyPress = 0;
    _frozeStack = new boolean[20][10];
    _renderBuffer = new boolean[20][10];
    _element = generateElement();
  }

  private ElementInterface freezeElement() {
    for (int y = _element.getPosY(); y < _element.getPosY() + _element.getHeight(); y++)
        for (int x = _element.getPosX(); x < _nextMove.getPosX() + _element.getWidth(); x++)
          _frozeStack[y][x] = (_frozeStack[y][x] || _element.getPixel(x, y));
    
    return _element;
  }

  private boolean doesNextMoveCollide() {
    for (int y = Math.max(_nextMove.getPosY(), 0); y < _nextMove.getPosY() + _nextMove.getHeight(); y++)
      for (int x = Math.max(_nextMove.getPosX(), 0); x < _nextMove.getPosX() + _nextMove.getWidth(); x++)
        if (_frozeStack[y][x] && _nextMove.getPixel(x, y))
          return true;

    return false;
  }

  private ElementInterface evaluateNextMove() {
    boolean bottomReached = (_element.getPosY() + _element.getHeight()  >= 20);
    if (bottomReached)
      return freezeElement();

    boolean leftReached = (_nextMove.getPosX() < 0);
    boolean rightReached = (_nextMove.getPosX() + _nextMove.getWidth() > 10);   
    boolean xMove = (_nextMove.getPosX() != _element.getPosX());
    boolean collide = doesNextMoveCollide();
    
    System.out.println("BACSEK: " + _nextMove.getPosX());
    if ((collide || leftReached || rightReached) && xMove)
      return _element;
    
    if (collide)
      return freezeElement();
    
    return _nextMove;
  }

  void eliminateFullRow() {
    boolean frozeStackNew[][] = new boolean[20][10];
    boolean renderBufferNew[][] = new boolean[20][10];
    int yNew = 19;
    for (int y = 19; y >=0; --y)
    {
      boolean fullRow = true;
      for (int x = 0; x < 10; ++x)
        fullRow &= _frozeStack[y][x]; 

      if (!fullRow)
      {
        for (int x = 0; x < 10; ++x)
        {
          frozeStackNew[yNew][x] = _frozeStack[y][x];
          renderBufferNew[yNew][x] = _renderBuffer[y][x];
        }

        yNew--;
      } 
    }

    _renderBuffer = renderBufferNew;
    _frozeStack = frozeStackNew;
  }

  private void handleNewElementCreate()
  {
    eliminateFullRow();
    _element = generateElement();
    _nextMove = _element.clone();
    if (doesNextMoveCollide())
    {
      System.out.println("Game Over!");
    }
  }

  private void handleMove() {
    _element = evaluateNextMove();
    boolean generateNewElement = false;
    for (int y = 0; y < 20; y++)
      for (int x = 0; x < 10; x++)
      {
        boolean fStackVal = _frozeStack[y][x];
        boolean ePixel =  _element.getPixel(x, y);
        generateNewElement |= (fStackVal && ePixel);
        _renderBuffer[y][x] = fStackVal || ePixel;
      }
    if (generateNewElement)
      handleNewElementCreate();
  }

  private void handleGravity() {
    _nextMove = _element.clone();
    _nextMove.setPosY(_element.getPosY() + 1);
    handleMove();
  }

  private String printStack()
  {
    ArrayList<String> allLines = new ArrayList<String>();
    for (int y = 0; y < 20; y++)
    {
      ArrayList<String> oneLine = new ArrayList<String>();
      for (int x = 0; x < 10; x++)
      {
        if (_renderBuffer[y][x])
          oneLine.add("true");
        else
          oneLine.add("false");
      }
      String oneLineStr = String.join(",", oneLine);
      allLines.add("[" + oneLineStr + "]");
    }

    String ret = String.join(",", allLines);
    return "[" + ret + "]";
  }

  public String getStack() {
    ++_statCnt;
    System.out.println(_userId + ": getStack() " + _statCnt + " : " + _keyPress);
    handleGravity();
    String response = "{ \"screen\" : " + printStack() + "}";
    return response;
  }

  public void keyPressed(char c) {
    ++_keyPress;
    System.out.println(_userId + ": keyPressed() " + _statCnt + " : " + _keyPress + " pressed button: " + c);
    _nextMove = _element.clone();
    switch (c) {
      case 'A':
        _nextMove.rotateCCW();
        break;
      case 'S':
        _nextMove.rotateCW();
        break;
      case 'J':
        _nextMove.setPosX(_element.getPosX() - 1);
        break;
      case 'L':
        _nextMove.setPosX(_element.getPosX() + 1);
        break;
      case 'K':
        _nextMove.setPosY(_element.getPosY() +  1);
        break;
      default:
        System.out.println("No possible move");
    }
    _element = evaluateNextMove();
  }
};

class GameHttpHandlerBase {
  protected void writeResponse(HttpExchange exchange, String httpResponse) throws IOException {
      Headers respHeaders = exchange.getResponseHeaders();
      respHeaders.add("Access-Control-Allow-Headers","x-prototype-version,x-requested-with");
      respHeaders.add("Access-Control-Allow-Methods","GET,POST");
      respHeaders.add("Access-Control-Allow-Origin","*");
      respHeaders.add("content-type", "application/json");
      exchange.sendResponseHeaders(200, httpResponse.getBytes().length);//response code and length
      OutputStream os = exchange.getResponseBody();
      os.write(httpResponse.getBytes());
      os.close();
  }
};

class RootHttpHandler extends GameHttpHandlerBase implements HttpHandler {
  public void handle(HttpExchange exchange) throws IOException {
    String response = "Hello, World!\n";
    exchange.sendResponseHeaders(200, response.length());
    OutputStream os = exchange.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }
}

class GameHttpHandler extends GameHttpHandlerBase {
  protected TreeMap<String, Game> _games;
  
  protected String getGameKey(String reqQuery) {
    String retKey = "";
    if (Pattern.matches("userId=[a-zA-Z_]+", reqQuery))
      retKey = reqQuery.split("=")[1];
    return retKey;
  }

  public GameHttpHandler(TreeMap<String, Game> games) {
    _games = games;
  }

  Game getGame(String gameKey) {
    Game game;
    if (!_games.containsKey(gameKey)) {
      game = new Game(gameKey);
      _games.put(gameKey, game);
    }
    else
      game = _games.get(gameKey);

    return game;
  }

};

class MoveHandler extends GameHttpHandler implements HttpHandler {
  
  MoveHandler(TreeMap<String, Game> games) {
    super(games);
  }

  protected String extractRequest(HttpExchange exchange) {
    InputStream iSR = exchange.getRequestBody();
    BufferedReader buff = new BufferedReader(new InputStreamReader(iSR));
    String requestStr = buff.lines().collect(Collectors.joining(" -@- "));
    return requestStr;
  }

  protected char getChar(String iRequest) {
    if (Pattern.matches("\\{\"type\":\"keypress\",\"code\":\"Key.\"\\}", iRequest))
      return iRequest.charAt(iRequest.length()-3);

    return '@';
  }

  public void handle(HttpExchange exchange) throws IOException {
      Game game = getGame(getGameKey(exchange.getRequestURI().getQuery()));
      game.keyPressed(getChar(extractRequest(exchange)));
      String response = "{}";
      writeResponse(exchange, response);  
    }
}

class ScreenHandler extends GameHttpHandler implements HttpHandler {
  private TreeMap<String, Game> _games;

  public ScreenHandler(TreeMap<String, Game> games) {
    super(games);
  }

  public void handle(HttpExchange exchange) throws IOException {
      Game game = getGame(getGameKey(exchange.getRequestURI().getQuery()));
      writeResponse(exchange, game.getStack());  
  }
}


public class TetrisServer {
  private HttpServer _server;
  private TreeMap<String, Game> _games;

  TetrisServer() throws IOException {
    _games = new TreeMap<String, Game>();
     _server = HttpServer.create(new InetSocketAddress(8888), 0);
     _server.createContext("/", new RootHttpHandler());
     _server.createContext("/screen", new ScreenHandler(_games));
     _server.createContext("/move", new MoveHandler(_games));
     
  }

  void run() {
    _server.start();
  }

  
  public static void main(String[] args) throws IOException {
      TetrisServer t = new TetrisServer();
      t.run();
  }   
}

/*
*public static void main(String[] args) throws Exception {
*
*    HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
*    //Create the context for the server.
*    server.createContext("/", new BaseHandler());
*    server.setExecutor(Executors.newCachedThreadPool());
*    server.start();
*}
*/
