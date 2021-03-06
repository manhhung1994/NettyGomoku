package Sourcegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoManager;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
public class CaroGraphics extends JPanel {

	
	
	private static final long serialVersionUID = 1L;
	public final static int sizeCell = 30;
	public final static int row = 18;
	public final static int col = 18;
	public final static int width = sizeCell * col + 1;
	public final static int height = sizeCell * row + 1;

	private int sizeImg = sizeCell - 2;
	public boolean player, playerRoot;
	private Process process;

	private MyImage myImage = new MyImage();
	private Icon iconActive;
	private UndoManager undoManager = new UndoManager();
	protected Vector<Point> pointVector;

	public JTextField ipAddress = new JTextField("127.0.0.1");
	public JTextField PORT = new JTextField("9696");
	public JTextField playerName = new JTextField("Tu Manh Hung");
	
	private Channel channel;

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
		repaint();
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
		        
		    } else {
		        System.out.println("login failed");
		        final JOptionPane optionPane = new JOptionPane(
		        	    "The only way to close this dialog is by\n"
		        	    + "pressing one of the following buttons.\n"
		        	    + "Do you understand?",
		        	    JOptionPane.QUESTION_MESSAGE,
		        	    JOptionPane.YES_NO_OPTION);
		    }
		} else {
		    System.out.println("Login canceled");
		}
		
	}
	
	
	class ClientRunning extends Thread {
		public void run() {
			EventLoopGroup group = new NioEventLoopGroup();
			try {
				//client.start();
				Bootstrap bootStrap = new Bootstrap()
				.group(group)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel sockChannel) throws Exception {
						// TODO Auto-generated method stub
						ChannelPipeline pipeline = sockChannel.pipeline();
						pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
				        pipeline.addLast("decoder", new StringDecoder());
				        pipeline.addLast("encoder", new StringEncoder());
				        pipeline.addLast("handler", new SimpleChannelInboundHandler<String>() {

							@Override
							public void channelRead0(ChannelHandlerContext channel, String data) throws Exception {
								
								if(!pointVector.contains(StringToPoint(data)))
								{
									pointVector.addElement(StringToPoint(data));
									repaint();
								}
								System.out.println("[channelread0]" +data);
							}
				        	
						});

					}
										
				});
				System.out.println("Connected");
				channel = bootStrap.connect(ipAddress.getText(), Integer.parseInt(PORT.getText())).sync().channel();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	public Point StringToPoint(String data)
	{
		String[] parts = data.split("-");
		String part1 = parts[0];
		String part2 = parts[1];
		System.out.println(part1 );
		int x = (int) Float.parseFloat(part1);
		int y = (int) Float.parseFloat(part2);
		
		return new Point(x,y);
	}
	public void Send(String data)
	{
		channel.writeAndFlush(data +"\n");
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
		//System.out.println("Draw image");
		//client.sendMessage(new ChatMessage(ChatMessage.MESSAGE,"Draw image"));
		boolean player = playerRoot;
		for (int i = 0; i < pointVector.size(); i++) {
			//Image image = player ? myImage.imgCross : myImage.imgNought;
			Image image = myImage.imgCross;
			Point point = convertPointToCaro(convertPoint(pointVector.get(i)));
			g.drawImage(image, point.x, point.y, null);
			player = !player;
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

	void actionClick(Point point) {
		// repaint();


		Point pointTemp = convertPoint(point);
		Send(pointTemp.getX() + "-"+ pointTemp.getY());
		if (process.updateMatrix(player, convertPointToMaxtrix(pointTemp))) {
			pointVector.addElement(point);
			
			undoManager.undoableEditHappened(new UndoableEditEvent(this,
					new UndoablePaintSquare(point, pointVector)));
			
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
