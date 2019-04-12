import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class BreakoutPanel1 extends JPanel {
 /** Width and height of application window in pixels */
 public static final int APPLICATION_WIDTH = 400;
 public static final int APPLICATION_HEIGHT = 600;
 public static final int DELAY = 6;

 /** Dimensions of game board (usually the same) */
 private static final int WIDTH = APPLICATION_WIDTH;
 private static final int HEIGHT = APPLICATION_HEIGHT;

 /** Dimensions of the paddle */
 private static final int PADDLE_WIDTH = 60;
 private static final int PADDLE_HEIGHT = 10;

 /** Offset of the paddle up from the bottom */
 private static final int PADDLE_Y_OFFSET = 30;

 /** The coordinate of the ball */
 private int ballX, ballY;
 /** Radius of the ball in pixels */
 private static final int BALL_RADIUS = 10;
 /** The coordinate of the paddle */
 private int paddleX, paddleY;

 // 关于砖块要加入的内容
 /** Number of bricks per row */
 private static final int NBRICKS_PER_ROW = 10;

 /** Number of rows of bricks */
 private static final int NBRICK_ROWS = 10;

 /** Separation between bricks */
 private static final int BRICK_SEP = 4;

 /** Width of a brick */
 private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW - 1)
   * BRICK_SEP)
   / NBRICKS_PER_ROW;

 /** Height of a brick */
 private static final int BRICK_HEIGHT = 8;

 /** Offset of the top brick row from the top */
 private static final int BRICK_Y_OFFSET = 70;

 private boolean[][] brickExists = new boolean[NBRICK_ROWS][NBRICKS_PER_ROW];

 private Timer timer;
 private int moveX, moveY, brickLeft = NBRICK_ROWS * NBRICKS_PER_ROW;

 /** Constructor */
 public BreakoutPanel1() {

  // 球最开始时，在窗口的中间位置，ballX,ballY分别代表球所在正方形的左上角坐标
  ballX = 190;
  ballY = 290;

  // paddle最开始时，横坐标位于窗口的中间位置，纵坐标离窗口底端PADDLE_Y_OFFSET。paddleX,paddleY分别代表paddle左上角坐标
  paddleX = 170;
  paddleY = 560;

  timer = new Timer(DELAY, new ReboundListener());
  moveX = moveY = 2;

  setPreferredSize(new Dimension(APPLICATION_WIDTH, APPLICATION_HEIGHT));
  setBackground(Color.white);

  addMouseMotionListener(new BreakoutListener());
  for (int i = 0; i < NBRICK_ROWS; i++) {
   for (int j = 0; j < NBRICKS_PER_ROW; j++)
    brickExists[i][j] = true;

  }

  timer.start();
 }

 
 
 
 
 public void paintComponent(Graphics page) {
  super.paintComponent(page);
  drawBall(page);
  drawPaddle(page);
  drawAllBricks(page);
 }

 // 根据ballX,ballY,BALL_RADIUS,画球
 private void drawBall(Graphics page) {
  page.setColor(Color.cyan);
  page.fillOval(ballX, ballY, 2 * BALL_RADIUS, 2 * BALL_RADIUS);

 }

 // 根据paddleX,paddleY,PADDLE_WIDTH,PADDLE_HEIGHT,画paddle
 private void drawPaddle(Graphics page) {
  page.setColor(Color.BLACK);
  page.fillRect(paddleX, paddleY, PADDLE_WIDTH, PADDLE_HEIGHT);
 }

 private Color[] brickColor = { Color.red, Color.red, Color.orange,
   Color.orange, Color.yellow, Color.yellow, Color.green, Color.green,
   Color.blue, Color.blue };

 private void drawAllBricks(Graphics page) {// 利用两重循环绘制砖块
  int i = 0, j = 0;
  int x = 0, y = BRICK_Y_OFFSET;// the (x,y) of each brick

  
  for (i = 0; i < 10; i++) {
   page.setColor(brickColor[i]);
   for (j = 0; j < 10; j++) {

   if (brickExists[i][j] == true) {
    page.fillRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
     
   }
   x += BRICK_WIDTH + BRICK_SEP;
   }
   x = 0;
   y += BRICK_SEP + BRICK_HEIGHT;
   // (11月11日)在循环中，每次绘制砖块前判断该位置对应的brickExists数组元素是否为true,为true才绘制。
  }
 }


 
 
 
 private class BreakoutListener implements MouseMotionListener {
  public void mouseMoved(MouseEvent event) {
   paddleX = event.getX() - 30;

   repaint();
  }

  public void mouseDragged(MouseEvent event) {
  }

 }

 private void getCollidingObject() {
  int allBricksWidth = NBRICK_ROWS * BRICK_HEIGHT + (NBRICK_ROWS - 1)
    * BRICK_SEP;

  if (ballY >= BRICK_Y_OFFSET && ballY <= BRICK_Y_OFFSET + allBricksWidth) {
   getCollidingBricks();
  } else if (collidingWithPaddle() && moveY > 0)
   moveY = moveY * -1;
 }

 private void getCollidingBricks() {
  /** four corner points on the square in which the ball is inscribed. */
  Point[] ballCorner = { new Point(ballX, ballY),
    new Point(ballX + 2 * BALL_RADIUS, ballY),
    new Point(ballX, ballY + 2 * BALL_RADIUS),
    new Point(ballX + 2 * BALL_RADIUS, ballY + 2 * BALL_RADIUS) };

  for (Point p : ballCorner) {
   int brickColumn = p.x / (BRICK_WIDTH + BRICK_SEP);
   int brickRow = (p.y - BRICK_Y_OFFSET) / (BRICK_HEIGHT + BRICK_SEP);

   int brickColumnOffset = p.x % (BRICK_WIDTH + BRICK_SEP);
   int brickRowOffset = p.y % (BRICK_HEIGHT + BRICK_SEP);

   if (brickRow < NBRICK_ROWS && brickColumn < NBRICKS_PER_ROW
     && brickExists[brickRow][brickColumn]
     && brickColumnOffset <= BRICK_WIDTH
     && brickRowOffset <= BRICK_HEIGHT) {
    brickExists[brickRow][brickColumn] = false;
    brickLeft--;
    moveY = -moveY;
    break;
   }

  }

 }

 /** check if the ball collide with the paddle */
 private boolean collidingWithPaddle() {
  boolean result = false;
  /**
   * the two bottom corner points on the square in which the ball is
   * inscribed.
   */
  Point[] ballDownCorner = { new Point(ballX, ballY + 2 * BALL_RADIUS),
    new Point(ballX + 2 * BALL_RADIUS, ballY + 2 * BALL_RADIUS) };

  for (Point p : ballDownCorner) {
   if (p.x >= paddleX && p.x <= paddleX + PADDLE_WIDTH
     && p.y >= paddleY && p.y <= paddleY + PADDLE_HEIGHT)
    result = true;
  }
  return result;

 }

 private class ReboundListener implements ActionListener {
  public void actionPerformed(ActionEvent event) {
   ballX += moveX;
   ballY += moveY;
   if (ballX <= 0 || ballX >= APPLICATION_WIDTH - 2 * BALL_RADIUS)
    moveX = moveX * -1;
   if (ballY < 0 || ballY >= APPLICATION_HEIGHT - 2 * BALL_RADIUS)
    moveY = moveY * -1;

   if (ballY >= HEIGHT - 2 * BALL_RADIUS) {
    timer.stop();
   }
   getCollidingObject();
   if (brickLeft == 0) {
    timer.stop();
   }
   repaint();
  }
 }

}