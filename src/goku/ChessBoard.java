package goku;

import java.awt.Color;  
import java.awt.Cursor;  
import java.awt.Dimension;  
import java.awt.Graphics;  
import java.awt.Graphics2D;  
import java.awt.Image;  
import java.awt.RadialGradientPaint;  
import java.awt.RenderingHints;  
import java.awt.Toolkit;  
import java.awt.event.MouseEvent;  
import java.awt.event.MouseListener;  
import java.awt.event.MouseMotionListener;  
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import javax.print.attribute.standard.PrinterMessageFromOperator;
import javax.swing.*;  
/** 
 * 五子棋--棋盘类  
 */  
  
public class ChessBoard extends JPanel implements MouseListener {  
   public static final int MARGIN=30;//边距  
   public static final int GRID_SPAN=35;//网格间距  
   public static final int ROWS=15;//棋盘行数  
   public static final int COLS=15;//棋盘列数  
   public static final int BLACK=2;
   public static final int WHITE=1;
   public static final int BLANK=0;
   public static final int DEPTH=1;
   int[][] chessBoard = new int[COLS+1][ROWS+1];
     
//   Point[] chessList=new Point[(ROWS+1)*(COLS+1)];//初始每个数组元素为null  
   List<Point> chessList = new ArrayList<Point>();
   boolean isBlack=true;//默认开始是黑棋先  
   boolean gameOver=false;//游戏是否结束  
   int chessCount;//当前棋盘棋子的个数  
   int xIndex,yIndex;//当前刚下棋子的索引 
   //just for  test github
   boolean flag=true;
   boolean flag1=true;
     
   Image img;  
   Image shadows;  
   Color colortemp;  
   public ChessBoard(){  
        
      // setBackground(Color.blue);//设置背景色为橘黄色  
       img=Toolkit.getDefaultToolkit().getImage("board.jpg");  
       shadows=Toolkit.getDefaultToolkit().getImage("shadows.jpg");  
       addMouseListener(this);  
       addMouseMotionListener(new MouseMotionListener(){  
           public void mouseDragged(MouseEvent e){  
                 
           }  
             
           public void mouseMoved(MouseEvent e){  
             int x1=(e.getX()-MARGIN+GRID_SPAN/2)/GRID_SPAN;  
             //将鼠标点击的坐标位置转成网格索引  
             int y1=(e.getY()-MARGIN+GRID_SPAN/2)/GRID_SPAN;  
             //游戏已经结束不能下  
             //落在棋盘外不能下  
             //x，y位置已经有棋子存在，不能下  
             if(x1<0||x1>ROWS||y1<0||y1>COLS||gameOver||findChess(x1,y1))  
                 setCursor(new Cursor(Cursor.DEFAULT_CURSOR));  
             //设置成默认状态  
             else setCursor(new Cursor(Cursor.HAND_CURSOR));  
               
           }  
       });  
   }   
     
    
  
//绘制  
   public void paintComponent(Graphics g){  
       
       super.paintComponent(g);//画棋盘  
       
       int imgWidth= img.getWidth(this);  
       int imgHeight=img.getHeight(this);//获得图片的宽度与高度  
       int FWidth=getWidth();  
       int FHeight=getHeight();//获得窗口的宽度与高度  
       int x=(FWidth-imgWidth)/2;  
       int y=(FHeight-imgHeight)/2;  
       g.drawImage(img, x, y, null);  
      
         
       for(int i=0;i<=ROWS;i++){//画横线  
           g.drawLine(MARGIN, MARGIN+i*GRID_SPAN, MARGIN+COLS*GRID_SPAN, MARGIN+i*GRID_SPAN);  
       }  
       for(int i=0;i<=COLS;i++){//画竖线  
           g.drawLine(MARGIN+i*GRID_SPAN, MARGIN, MARGIN+i*GRID_SPAN, MARGIN+ROWS*GRID_SPAN);  
             
       }  
         
       //画棋子  
       for(int i=0;i<chessCount;i++){  
           //网格交叉点x，y坐标  
           int xPos=chessList.get(i).getX()*GRID_SPAN+MARGIN;  
           int yPos=chessList.get(i).getY()*GRID_SPAN+MARGIN;  
           g.setColor(chessList.get(i).getColor());//设置颜色  
          // g.fillOval(xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2,  
                           //Point.DIAMETER, Point.DIAMETER);  
           //g.drawImage(shadows, xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2, Point.DIAMETER, Point.DIAMETER, null);  
           colortemp=chessList.get(i).getColor();  
           if(colortemp==Color.black){  
               RadialGradientPaint paint = new RadialGradientPaint(xPos-Point.DIAMETER/2+25, yPos-Point.DIAMETER/2+10, 20, new float[]{0f, 1f}  
               , new Color[]{Color.WHITE, Color.BLACK});  
               ((Graphics2D) g).setPaint(paint);  
               ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  
               ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);  
  
           }  
           else if(colortemp==Color.white){  
               RadialGradientPaint paint = new RadialGradientPaint(xPos-Point.DIAMETER/2+25, yPos-Point.DIAMETER/2+10, 70, new float[]{0f, 1f}  
               , new Color[]{Color.WHITE, Color.BLACK});  
               ((Graphics2D) g).setPaint(paint);  
               ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  
               ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);  
  
           }  
           
           Ellipse2D e = new Ellipse2D.Float(xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2, 34, 35);  
           ((Graphics2D) g).fill(e);  
           //标记最后一个棋子的红矩形框  
             
           if(i==chessCount-1){//如果是最后一个棋子  
               g.setColor(Color.red);  
               g.drawRect(xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2,  
                           34, 35);  
           }  
       }  
   }  
     
   public void mousePressed(MouseEvent e){//鼠标在组件上按下时调用  
         
       //游戏结束时，不再能下  
       if(gameOver) return;  
         
       String colorName=isBlack?"黑棋":"白棋";  
         
       //将鼠标点击的坐标位置转换成网格索引  
       xIndex=(e.getX()-MARGIN+GRID_SPAN/2)/GRID_SPAN;  
       yIndex=(e.getY()-MARGIN+GRID_SPAN/2)/GRID_SPAN;  
         
       //落在棋盘外不能下  
       if(xIndex<0||xIndex>ROWS||yIndex<0||yIndex>COLS)  
           return;  
         
       //如果x，y位置已经有棋子存在，不能下  
       if(findChess(xIndex,yIndex))return;  
         
       //可以进行时的处理  
       Point ch=new Point(xIndex,yIndex,isBlack?Color.black:Color.white);  
       chessList.add(ch); 
       chessCount++;
       chessBoard[yIndex][xIndex]=BLACK;
//	   drawChessBoard();
//	   System.out.println(chessCount);
	   isBlack=!isBlack;
       repaint();//通知系统重新绘制 
       if(isWin(xIndex,yIndex,Color.black)){  
           String msg=String.format("恭喜，%s赢了！", colorName);  
           JOptionPane.showMessageDialog(this, msg);  
           gameOver=true;  
       }
       else{
    	   Point AIch = nextPoint(chessList, DEPTH);//电脑找到最优落子点
    	   chessList.add(AIch);
    	   chessCount++;
    	   chessBoard[AIch.getY()][AIch.getX()]=WHITE;
    	   repaint();
    	   if(isWin(AIch.getX(), AIch.getY(),Color.white)){
               String msg=String.format("恭喜，%s赢了！", "白棋");  
               JOptionPane.showMessageDialog(this, msg);  
               gameOver=true;    		   
    	   }
       }
//	   drawChessBoard();
       isBlack=!isBlack;
     }  
   //覆盖mouseListener的方法  
   public void mouseClicked(MouseEvent e){  
       //鼠标按键在组件上单击时调用  
   }  
     
   public void mouseEntered(MouseEvent e){  
       //鼠标进入到组件上时调用  
   }  
   public void mouseExited(MouseEvent e){  
       //鼠标离开组件时调用  
   }  
   public void mouseReleased(MouseEvent e){  
       //鼠标按钮在组件上释放时调用  
   }  
   //在棋子数组中查找是否有索引为x，y的棋子存在  
   private boolean findChess(int x,int y){  
	   int length=chessList.size();
       for(int i=0;i<length;i++){ 
    	   Point c=chessList.get(i);
           if(c!=null&&c.getX()==x&&c.getY()==y)  
               return true;  
       }  
       return false;  
   }  
   private boolean isWin(int xIndex,int yIndex,Color color){
	   Point point = new Point(xIndex, yIndex,color);
	   int [][] s=generate(point, color);
	   for(int i=0;i<4;i++){
			String input ="";
			for(int j = 0;j<9;j++)
				input+=s[i][j];
		   if(color == color.black){			   
			   if(Pattern.matches(".*2{5}.*",input))
				   return true;

		   }else{
			   if(Pattern.matches(".*1{5}.*", input))
				   return true;
		   }
	   }
	   return false;	   
   }
   private Point getChess(int xIndex,int yIndex,Color color){ 
	   int length = chessList.size();
       for(int i=0;i<length;i++){  
    	   Point p =chessList.get(i);
           if(p!=null&&p.getX()==xIndex&&p.getY()==yIndex  
                   &&p.getColor()==color)  
               return p;  
       }  
       return null;  
   }  
     
     
   public void restartGame(){  
       //清除棋子  
	   chessList.clear();
       //恢复游戏相关的变量值  
       for(int i=0;i<=ROWS;i++){
    	   for(int j=0;j<=ROWS;j++){
    		   chessBoard[i][j]=BLANK;
    	   }
    	   
       }
       isBlack=true;  
       gameOver=false; //游戏是否结束  
       chessCount =0; //当前棋盘棋子个数
     
       repaint();  
   }  
     
   //悔棋  
   public void goback(){  
       if(chessCount==0)  
           return ;  

       chessBoard[chessList.get(chessCount-1).getY()][chessList.get(chessCount-1).getX()]=BLANK;
       chessList.remove(chessCount-1); 

       chessBoard[chessList.get(chessCount-2).getY()][chessList.get(chessCount-2).getX()]=BLANK;
       chessList.remove(chessCount-2);//电脑无法悔棋，只能悔两步
       chessCount--;  
       chessCount--;
       if(chessCount>0){  
           xIndex=chessList.get(chessCount-1).getX();  
           yIndex=chessList.get(chessCount-1).getY();  
       }  
       repaint();  
   }  
     
   //矩形Dimension  
  
   public Dimension getPreferredSize(){  
       return new Dimension(MARGIN*2+GRID_SPAN*COLS,MARGIN*2  
                            +GRID_SPAN*ROWS);  
   }
/*   private Point nextPoint(List<Point> curArray,int depth){
	   return new Point(5,5,Color.white);
   }*/
   private Point nextPoint(List<Point> curArray,int depth){
	   List<Point> bestPoint =new ArrayList<Point>();
	   List<Point> tempList=curArray;
	   int bestValue =  Integer.MIN_VALUE;
	   List<Point> searchList = this.getAllPointToSearch(tempList,Color.white);//获取可以添加的子节点
//	   System.out.println("length="+searchList.size());
//	   List<TreeNode> subList = root.newsubNode(searchList);//根节点的所有子节点
	   int length =searchList.size();
	   for(int i=0;i<length;i++){
		   Point p =searchList.get(i);
		   tempList.add(p);
		   chessBoard[p.getY()][p.getX()]=WHITE;
//		   System.out.println(tempList.size());
		   int tempValue=min(tempList,depth-1,p,Integer.MAX_VALUE,Integer.MIN_VALUE);
//		   System.out.println(p.getX()+"---"+p.getY()+"value="+tempValue);
		   if(tempValue==bestValue){
			   bestPoint.add(p);
		   }
		   else if(tempValue>bestValue){
			   bestValue=tempValue;
			   bestPoint.clear();
			   bestPoint.add(p);			   
		   }
		   tempList.remove(p);
		   chessBoard[p.getY()][p.getX()]=BLANK;
	   }
	   Random r = new Random () ;   
	   Point result= bestPoint.get(r.nextInt(bestPoint.size()));//随机选取一个元素返回
//	   System.out.println(result.getX()+"---"+result.getY());
	   return result;
	   
   }
   private int min(List<Point> curList,int depth,Point p,int alpha,int beta){
	   List<Point> tempList=curList;
	   int tempValue = judge(p,Color.white)+judge(p, Color.black);
//	   int tempValue = evalute(tempList);
	   if(depth<=0||isWin(p.getX(), p.getY(),Color.white)){
		   return tempValue;
	   }
	   int best =Integer.MAX_VALUE;
	   List<Point> searchList = getAllPointToSearch(tempList,Color.black);//获取可以添加的子节点
	   int length=searchList.size();
	   for(int i=0;i<length;i++){
		   Point T = searchList.get(i);
		   tempList.add(T);
		   chessBoard[T.getY()][T.getX()]=BLACK;
		   tempValue = max(tempList,depth-1,T,best<alpha?best:alpha,beta);
		   tempList.remove(T);
		   chessBoard[T.getY()][T.getX()]=BLANK;
		   if(tempValue<best){
			   best=tempValue;
		   }
		   if(tempValue<beta){
			   break;
		   }
	   }
	   return best;
   }
   private int max(List<Point> curList,int depth,Point p,int alpha,int beta){
	   List<Point> tempList = curList;

	   int tempValue = judge(p,Color.white)+judge(p, Color.black);
//	   int tempValue = evalute(tempList);
	   if(depth<=0||isWin(p.getX(),p.getY(),Color.black)){
		   return tempValue;
	   }
	   int best = Integer.MIN_VALUE;
	   List<Point> searchList = getAllPointToSearch(tempList,Color.white);
	   int length =searchList.size();
	   for(int i=0;i<length;i++){
		   Point T= searchList.get(i);
		   tempList.add(T);
		   chessBoard[T.getY()][T.getX()]=WHITE;
		   tempValue=min(tempList, depth-1, T,alpha,best>beta?best:beta);
		   tempList.remove(T);
		   chessBoard[T.getY()][T.getX()]=BLANK;
		   if(tempValue>best){
			   best=tempValue; 
		   }
		   if(tempValue>alpha){
			   break;
		   }
	   }
	   return best;
   }
   public ArrayList<Point> getAllPointToSearch(List<Point> list,Color color)
	{
		int[][] chessBoardtemp = new int[COLS+1][ROWS+1];
		for(int i = 0;i<=this.COLS;i++)
	    	   for(int j = 0;j<=this.ROWS;j++) 
	    		   chessBoardtemp[i][j] = chessBoard[i][j];
		ArrayList<Point> list1 = new ArrayList<Point>();
		int length=list.size();
		for(int i=0;i<length;i++)
		{
			Point p=list.get(i);
			int x = p.getX();
			int y = p.getY();
			
			if(x-1>=0&&x-1<=ROWS&&
			   y-1>=0&&y-1<=COLS&&
			   chessBoardtemp[y-1][x-1]==0)
			{
				chessBoardtemp[y-1][x-1]=-1;
				list1.add(new Point(x-1,y-1,color));
				
			}
			if(x-2>=0&&x-2<=ROWS&&
			   y-2>=0&&y-2<=COLS&&
			   chessBoardtemp[y-2][x-2]==0)
			{
				chessBoardtemp[y-2][x-2]=-1;
				list1.add(new Point(x-2,y-2,color));
				
			}
			if(x+1>=0&&x+1<=ROWS&&
			   y+1>=0&&y+1<=COLS&&
			   chessBoardtemp[y+1][x+1]==0)
			{
				chessBoardtemp[y+1][x+1]=-1;
				list1.add(new Point(x+1,y+1,color));
				
			}
			if(x+2>=0&&x+2<=ROWS&&
			   y+2>=0&&y+2<=COLS&&
			   chessBoardtemp[y+2][x+2]==0)
			{
				chessBoardtemp[y+2][x+2]=-1;
				list1.add(new Point(x+2,y+2,color));
				
			}
			if(x>=0&&x<=ROWS&&
			   y-1>=0&&y-1<=COLS&&
			   chessBoardtemp[y-1][x]==0)
			{
				chessBoardtemp[y-1][x]=-1;
				list1.add(new Point(x,y-1,color));
				
			}			
			if(x>=0&&x<=ROWS&&
			   y-2>=0&&y-2<=COLS&&
			   chessBoardtemp[y-2][x]==0)
			{
				chessBoardtemp[y-2][x]=-1;
				list1.add(new Point(x,y-2,color));
				
			}
			if(x>=0&&x<=ROWS&&
			   y+1>=0&&y+1<=COLS&&
			   chessBoardtemp[y+1][x]==0)
			{
				chessBoardtemp[y+1][x]=-1;
				list1.add(new Point(x,y+1,color));
				
			}
			if(x>=0&&x<=ROWS&&
			   y+2>=0&&y+2<=COLS&&
			   chessBoardtemp[y+2][x]==0)
			{
				chessBoardtemp[y+2][x]=-1;
				list1.add(new Point(x,y+2,color));
				
			}
			if(x-1>=0&&x-1<=ROWS&&
			   y>=0&&y<=COLS&&
			   chessBoardtemp[y][x-1]==0)
			{
				chessBoardtemp[y][x-1]=-1;
				list1.add(new Point(x-1,y,color));
				
			}
			if(x-2>=0&&x-2<=ROWS&&
			   y>=0&&y<=COLS&&
			   chessBoardtemp[y][x-2]==0)
			{
				chessBoardtemp[y][x-2]=-1;
				list1.add(new Point(x-2,y,color));
				
			}
			if(x+1>=0&&x+1<=ROWS&&
			   y>=0&&y<=COLS&&
			   chessBoardtemp[y][x+1]==0)
			{
				chessBoardtemp[y][x+1]=-1;
				list1.add(new Point(x+1,y,color));
				
			}
			if(x+2>=0&&x+2<=ROWS&&
			   y>=0&&y<=COLS&&
			   chessBoardtemp[y][x+2]==0)
			{
				chessBoardtemp[y][x+2]=-1;
				list1.add(new Point(x+2,y,color));
				
			}
			if(x-1>=0&&x-1<=ROWS&&
			   y+1>=0&&y+1<=COLS&&
			   chessBoardtemp[y+1][x-1]==0)
			{
				chessBoardtemp[y+1][x-1]=-1;
				list1.add(new Point(x-1,y+1,color));
				
			}
			if(x-2>=0&&x-2<=ROWS&&
			   y+2>=0&&y+2<=COLS&&
			   chessBoardtemp[y+2][x-2]==0)
			{
				chessBoardtemp[y+2][x-2]=-1;
				list1.add(new Point(x-2,y+2,color));
				
			}
			if(x+1>=0&&x+1<=ROWS&&
			   y-1>=0&&y-1<=COLS&&
			   chessBoardtemp[y-1][x+1]==0)
			{
				chessBoardtemp[y-1][x+1]=-1;
				list1.add(new Point(x+1,y-1,color));
				
			}
			if(x+2>=0&&x+2<=ROWS&&
			   y-2>=0&&y-2<=COLS&&
			   chessBoardtemp[y-2][x+2]==0)
			{
				chessBoardtemp[y-2][x+2]=-1;
				list1.add(new Point(x+2,y-2,color));
				
			}
		}
		return list1;
	}
   public int evalute(List<Point> list){
	   int length =list.size();
	   int count= 0;
	   for(int i=0;i<length;i++){
		   int [][] fourLine = new int [4][9];
		   int whiteScore=0;
		   int blackScore=0;
		   if(list.get(i).getColor()==Color.black){
			   fourLine=generate(list.get(i), Color.black);
			   for(int j=0;j<4;j++){
				   blackScore+=this.judgeLine(fourLine[j],Color.black);
			   }   
		   }
		   else{
			   fourLine=generate(list.get(i), Color.white);
			   for(int j=0;j<4;j++){
				   whiteScore+=this.judgeLine(fourLine[j],Color.white);
			   } 			   
		   }
		   count=count+whiteScore-blackScore;
	   }
	   return count;
   }
   public int judge(Point p,Color color){
  		int Score = 0;
  		int[][] fourLine = new int[4][9];
  		fourLine = this.generate(p,color);
  		for(int i = 0;i<4;i++)
  			Score+=this.judgeLine(fourLine[i],color);
  		return Score;
  	}
	private int judgeLine(int[] is,Color color) {
		// TODO Auto-generated method stub
		String black5 = ".*2{5}.*";
		int five = 100000;
		String white5 = ".*1{5}.*";
		String black4 = ".*02{4}0.*";
		String white4 = ".*01{4}0.*";
		int four = 10000;
		String blackGO4 = ".*(12{4}0|02{4}1|32{4}0|02{4}3|20222|22022|22202).*";
		String whiteGO4 = ".*(21{4}0|01{4}2|31{4}0|01{4}3|10111|11011|11101).*";
		int gofour = 500;
		String black3 = ".*02{3}0.*";
		String white3 = ".*01{3}0.*";
		int three = 100;
		String blackjump3 = ".*022020.*|.*020220.*";
		String whitejump3 = ".*011010.*|.*010110.*";
		int jumpthree = 90;
		String blacksleep3 = ".*((1|3)2{3}00|002{3}(1|3)|(1|3)22020|(1|3)20220|02022(1|3)|02202(1|3)).*";
		String whitesleep3 = ".*((2|3)1{3}00|001{3}(2|3)|(2|3)11010|(2|3)10110|01011(2|3)|01101(2|3)).*";
		int sleep3 = 50;
		String black2 = ".*(002200|02020|020020).*";
		String white2 = ".*(001100|01010|010010).*";
		int two = 10;
		String blacksleep2 = ".*((1|3)22000|00022(1|3)|(1|3)20200|00202(1|3)|(1|3)20020|02002(1|3)|20002).*";
		String whitesleep2 = ".*((2|3)11000|00011(2|3)|(2|3)10100|00101(2|3)|(2|3)10010|01001(2|3)|10001).*";
		int sleep2 = 2;
		String input ="";
		for(int i = 0;i<9;i++)
			input+=is[i];
//		System.out.println(input);	
		if(color==Color.black)
		{
			
			if(Pattern.matches(black5,input))
				return five;
			if(Pattern.matches(black4,input))
				return four;
			if(Pattern.matches(blackGO4,input))
				return gofour;
			if(Pattern.matches(black3,input))
				return three;
			if(Pattern.matches(blackjump3,input))
				return jumpthree;
			if(Pattern.matches(blacksleep3,input))
				return sleep3;
			if(Pattern.matches(black2,input))
				return two;
			if(Pattern.matches(blacksleep2,input))
				return sleep2;
		}
		else if(color==Color.white)
		{
			if(Pattern.matches(white5,input))
				return five;
			if(Pattern.matches(white4,input))
				return four;
			if(Pattern.matches(whiteGO4,input))
				return gofour;
			if(Pattern.matches(white3,input))
				return three;
			if(Pattern.matches(whitejump3,input))
				return jumpthree;
			if(Pattern.matches(whitesleep3,input))
				return sleep3;
			if(Pattern.matches(white2,input))
				return two;
			if(Pattern.matches(whitesleep2,input))
				return sleep2;
		}
		return 0;
	}
	private int[][] generate(Point p,Color color) {
		
		// TODO Auto-generated method stub
		int x = p.getX();
		int y = p.getY();
		int[][] fourLine = new int[4][9];
		int index = 0;
		for(int j = -4;j<5;j++)//×óÉÏÍùÓÒÏÂ
		{
			if(y+j<=COLS&&y+j>=0&&x+j<=ROWS&&x+j>=0)
			{
				if(chessBoard[y+j][x+j]==2)
					fourLine[0][index++] = 2;
				else if(chessBoard[y+j][x+j]==1)
					fourLine[0][index++] = 1;
				else 
					fourLine[0][index++] = 0;
					
			}
			else
				fourLine[0][index++] = 3;
				
			}
		index = 0;
		
		for(int j = 4;j>-5;j--)//×óÏÂÍùÓÒÉÏ
		{
			if(y+j<=COLS&&y+j>=0&&x-j<=ROWS&&x-j>=0)
			{
				if(chessBoard[y+j][x-j]==2)
					fourLine[1][index++] = 2;
				else if(chessBoard[y+j][x-j]==1)
					fourLine[1][index++] = 1;
				else 
					fourLine[1][index++] = 0;
					
			}
			else
				fourLine[1][index++] = 3;
			
		}
		index = 0;
		
		for(int i = 4;i>-5;i--)//ÏÂÍùÉÏ
		{
			if(y+i<=COLS&&y+i>=0)
			{
				if(chessBoard[y+i][x]==2)
					fourLine[2][index++] = 2;
				else if(chessBoard[y+i][x]==1)
					fourLine[2][index++] = 1;
				else 
					fourLine[2][index++] = 0;
					
			}
			else
				fourLine[2][index++] = 3;
		}
		index = 0;
		
		for(int i = -4;i<5;i++)//×óÍùÓÒ
		{
			if(x+i<=COLS&&x+i>=0)
			{
				if(chessBoard[y][x+i]==2)
					fourLine[3][index++] = 2;
				else if(chessBoard[y][x+i]==1)
					fourLine[3][index++] = 1;
				else 
					fourLine[3][index++] = 0;
					
			}
			else
				fourLine[3][index++] = 3;
		}	
		int i = color==Color.black?2:1;
		fourLine[0][4] = i;
		fourLine[1][4] = i;
		fourLine[2][4] = i;
		fourLine[3][4] = i;
		return fourLine;
	} 
	public void drawChessBoard(){
		for(int i=0;i<ROWS;i++){
			for(int j=0;j<ROWS;j++){
				System.out.print(chessBoard[i][j]);
			}
			System.out.println("");
		}
	}
} 