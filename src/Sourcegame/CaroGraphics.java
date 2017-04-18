package Sourcegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.undo.UndoManager;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import DataPackage.TypeData;
import Event.*;
import Jackson.JacksonBean;
import Jackson.JacksonDecoder;
import Jackson.JacksonEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;

public class CaroGraphics extends JPanel {

	private static final long serialVersionUID = 1L;
	public final static int sizeCell = 30;
	public final static int row = 18;
	public final static int col = 18;
	public final static int width = sizeCell * col + 1;
	public final static int height = sizeCell * row + 1;

	private int sizeImg = sizeCell - 2;
	public boolean player, playerRoot;
	public boolean myTurn;
	private Process process;

	private MyImage myImage = new MyImage();
	private Icon iconActive;

	private UndoManager undoManager = new UndoManager();
	protected Vector<Point> pointVector;
	
	protected Vector<MaxtrixPoint> pointVector2;
	public JTextField ipAddress = new JTextField("127.0.0.1");
	public JTextField PORT = new JTextField("9696");
	public JTextField playerName = new JTextField("Tu Manh Hung");
	
	
	public static Channel channel;

	private int winer = 0;

	public int getWiner() {
		return winer;
	}

	public void setWiner(int winer) {
		this.winer = winer;
	}

	public CaroGraphics() {
		makeIcon();
		setPreferredSize(new Dimension(width, height));
		connectDialog();
		init();
	}

	public void init() {
		winer = 0;
		process = new Process();
		player = playerRoot;
		pointVector = new Vector<Point>();
		pointVector2 = new Vector<MaxtrixPoint>();
		repaint();
	}
	public EventGame newEvent(int eventType,String data)
	{
		EventGame event = new EventGame();
		event.setData(data);
		event.setEventType(eventType);
		return event;
	}
	private void connectDialog()
	{

		Object[] message = {
		    "IP Address:", ipAddress,
		    "PORT:", PORT,
		    "Name",playerName
		};

		int option = JOptionPane.showConfirmDialog(null, message, "Connect", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {
			
		    if (true) {
		    	new ClientRunning().start();
		        String input = (String) JOptionPane.showInputDialog(null, "Choose now...",
		            "[Gomoku]Choose Room ", JOptionPane.QUESTION_MESSAGE, null, // Use
		                                                                            // default
		                                                                            // icon
		            TypeData.ROOM_NAME, // Array of choices
		            TypeData.ROOM_NAME[0]); // Initial choice	

		        switch(input)
		        {
			        case "Hải Dương Room 1" : 
						System.out.println("ban chon " + TypeData.ROOM_NAME[0]);
						CheckRoom(1);
						break;
			        case "Hải Dương Room 2" : 
						System.out.println("ban chon " + TypeData.ROOM_NAME[1]);
						CheckRoom(2);
						break;
			        case "Hải Dương Room 3" : 
						System.out.println("ban chon " + TypeData.ROOM_NAME[2]);
						CheckRoom(3);
						break;
			        case "Bắc Giang Room 1" : 
						System.out.println("ban chon " + TypeData.ROOM_NAME[3]);
						CheckRoom(4);
						break;
			        case "Bắc Giang Room 2" : 
						System.out.println("ban chon " + TypeData.ROOM_NAME[4]);
						CheckRoom(5);
						break;
			        case "Bắc Giang Room 3" : 
						System.out.println("ban chon " + TypeData.ROOM_NAME[5]);
						CheckRoom(6);
						break;
			        default : System.out.println("Default");
		        		break;
		        }
		    } else {
		        System.out.println("login failed");
		        
		    }
		} else {
		    System.out.println("Login canceled");
		}
		
	}
	
	public void CheckRoom(int index)
	{
		
	}
	class ClientRunning extends Thread {
		public void run() {
			EventLoopGroup group = new NioEventLoopGroup();
			try {
				Bootstrap bootStrap = new Bootstrap()
				.group(group)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel sockChannel) throws Exception {
						// TODO Auto-generated method stub
						ChannelPipeline pipeline = sockChannel.pipeline();
				        pipeline.addLast(new JacksonEncoder());
				        pipeline.addLast(new JacksonDecoder<EventGame>(EventGame.class));
				        pipeline.addLast(new SimpleChannelInboundHandler<Object>() {

							@Override
							public void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {
								ChannelReadController(ctx.channel(), obj);
								EventGame event = (EventGame)obj;
								System.out.println("[Type: " + event.getEventType() + "]" + "\n" +
												   "[Data: " + event.getData()+ "]" 
								);
							}			        	
						});
						
					}										
				});
				channel = bootStrap.connect(ipAddress.getText(), Integer.parseInt(PORT.getText())).sync().channel();
				
				
				SendEventToServer(newEvent(EventType.CONNECT,""));
						
			} catch (Exception e) {

				e.printStackTrace();
			}			
		}
	}
	public void ChannelReadController(Channel channel, Object obj)
	{
		int eventType = checkEventType(obj);
		switch (eventType) {
		case EventType.LOG_IN_SUCCESS:
			onLoginSuccess();
			break;

		default:
			break;
		}
	}
	private void onLoginSuccess() {
		System.out.println("Ve box chon room");
		
	}

	public int checkEventType(Object obj)
	{
		EventGame event = (EventGame)obj;
		return event.getEventType();
	}
	public Point StringToPoint(String data)
	{
		
		String[] parts = data.split("-");
		String part1 = parts[0];
		String part2 = parts[1];
		String part3 = parts[2];
		System.out.println(part1 );
		int x = (int) Float.parseFloat(part2);
		int y = (int) Float.parseFloat(part3);
		
		return new Point(x,y);
	}
	public boolean isPlayerRoot(String data)
	{		
		String[] parts = data.split("-");
		
		int x = (int) Float.parseFloat(parts[1]);
		
		if(x==1) 
			playerRoot = true;
		else playerRoot = false;
		return playerRoot;
	}
	public void SendEventToServer(EventGame event)
	{		
		channel.write(event);
		channel.flush();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(new Color(238, 238, 238));
		for (int i = 0; i <= row; i++) {
			g.drawLine(i * sizeCell, 0, i * sizeCell, height - 1);
			g.drawLine(0, i * sizeCell, width - 1, i * sizeCell);
		}
		drawImg(g);
		//System.out.println("a");
	}

	private void drawImg(Graphics g) {
		
		for (int i = 0; i < pointVector2.size(); i++) {
			Image image = null;
			if(playerRoot)
			{
				image = myImage.imgCross;
				if(pointVector2.get(i).myPoint)
					image = myImage.imgCross;
				else image = myImage.imgNought;
			}
			else 
			{
				image = myImage.imgNought;
				if(pointVector2.get(i).myPoint)
					image = myImage.imgNought;
				else image = myImage.imgCross;
				
			}
			//Point point = convertPointToCaro(convertPoint(pointVector.get(i)));
			Point point = convertPointToCaro(convertPoint(pointVector2.get(i).point));
			g.drawImage(image, point.x, point.y, null);
			//player = !player;
			
			 
			
				
		}
		
	}

	private Point convertPoint(Point point) {
		int x, y;
		int deviation = 1;
		x = (point.x % sizeCell > deviation) ? (point.x / sizeCell * sizeCell + sizeCell / 2)
				: (point.x / sizeCell * sizeCell - sizeCell / 2);
		y = (point.y % sizeCell > deviation) ? (point.y / sizeCell * sizeCell + sizeCell / 2)
				: (point.y / sizeCell * sizeCell - sizeCell / 2);
		//System.out.println(x  +" ||" + y );
		return new Point(x, y);
		
	}

	private Point convertPointToMaxtrix(Point point) {
		return new Point(point.y / sizeCell, point.x / sizeCell);
	}

	private Point convertPointToCaro(Point point) {
		return new Point(point.x - sizeImg / 2, point.y - sizeImg / 2);
	}

	public void setStatus() {
		CaroFrame.lbStatusO.setIcon(iconActive);
		CaroFrame.lbStatusX.setIcon(iconActive);
		if (player) {
			CaroFrame.lbStatusX.setEnabled(true);
			CaroFrame.lbStatusO.setEnabled(false);
		} else {
			CaroFrame.lbStatusX.setEnabled(false);
			CaroFrame.lbStatusO.setEnabled(true);
		}
	}

	private void makeIcon() {
		iconActive = new ImageIcon(myImage.reSizeImage(
				myImage.getMyImageIcon("active.png"), 20, 20));
	}

	void actionClick(Point point) throws JsonGenerationException, JsonMappingException, IOException {
		// repaint();
		//----------------------
		
		// -----------------------
		Point pointTemp = convertPoint(point);
		//Send(TypeData.MATRIX +"-" +pointTemp.getX() + "-"+ pointTemp.getY());
		myTurn = false;
		if (process.updateMatrix(player, convertPointToMaxtrix(pointTemp))) {
			//pointVector.addElement(point);
			pointVector2.addElement(new MaxtrixPoint(point,true));
			/*undoManager.undoableEditHappened(new UndoableEditEvent(this,
					new UndoablePaintSquare(point, pointVector)));
			*/
			repaint();
			player = !player;
			setStatus();
			if (process.getWin() > 0) {
				winer = process.getWin();
			}
		}
		//System.out.println("action click " + convertPoint(point));
	}

	public void undo() {
		player = !player;
		Point point = pointVector.get(pointVector.size() - 1);
		point = convertPointToMaxtrix(convertPoint(point));
		process.undoMatrix(point);
		undoManager.undo();
		setStatus();
		repaint();
	}

	public boolean canUndo() {
		return undoManager.canUndo();
	}
}
