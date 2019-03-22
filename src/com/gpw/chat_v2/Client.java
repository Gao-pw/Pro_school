package com.gpw.chat_v2;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame {	
	private static final long serialVersionUID = 1L;
		private JTextArea ta_info;
	    private JTextField tf_send;
	    PrintWriter pw;// 声明输出流对象
	    public int flag_text = 0;
	    /**
	     * 客户端_界面model
	     * 
	     * @author Gpw
	     * @param args
	     */
	    public static void main(String args[]) {
	        EventQueue.invokeLater(new Runnable() {
	            public void run() {
	                try {
	                    Client frame = new Client();
	                    frame.setVisible(true);
	                    frame.createClientSocket();
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        });
	    }
	    
	    public void createClientSocket() {
	        try {
	            Socket socket = new Socket("154.92.18.193", 8088);// 创建套接字对象
	            OutputStream out 
		   			= socket.getOutputStream();
	            OutputStreamWriter osw 
		   				= new OutputStreamWriter(out,"UTF-8");
	            pw = new PrintWriter(osw, true);// 创建输出流对象
	            new ClientThread(socket).start();// 创建并启动线程对象
	            ta_info.append("chat starting"+"\n");
	            ta_info.append("输入一个昵称："+"\n");
	        } catch (UnknownHostException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    class ClientThread extends Thread {
	        Socket socket;
	        
	        public ClientThread(Socket socket) {
	            this.socket = socket;
	        }
//	        bug 修正标记点。。。
	        public void run() {
	            try {
	            	
	            	InputStream in = socket.getInputStream();
					InputStreamReader isr
						= new InputStreamReader(in,"UTF-8");
					BufferedReader br = new BufferedReader(isr);
					
					String message = null;
					while((message=br.readLine())!=null) {
						if(flag_text==0) {
							ta_info.append(message + "\n");
							flag_text = 1;
						}else {
						String[] message_sp = message.split("：");
						//message_sp = message.split(":");
						String usr_name = message_sp[0].trim();
						String usr_message = message_sp[1].trim();
						ta_info.append(usr_name+"：" + "\n");
						ta_info.append(usr_message + "\n");// 在文本域中显示信息
					}
					}
//	                while (true) {
//	                	message = br.readLine();// 读取信息
//	                    ta_info.append(message + "\n");// 在文本域中显示信息
//	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    private void send() {
	        String info = tf_send.getText();// 获得输入的信息
	        if (info.equals("")) {
	            return;// 如果没输入信息则返回，即不发送
	        }
	        
	        pw.println(info);// 发送信息
	        pw.flush();// 刷新输出缓冲区
	        tf_send.setText(null);// 清空文本框
	    }
	    public void browse(String url) throws Exception {
	        Desktop desktop = Desktop.getDesktop();
	        if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
	            URI uri = new URI(url);
	            desktop.browse(uri);
	        }
	    }
	    
	    /**
	     * Create the frame
	     */
	    public Client() {
	        super();
	        setTitle("let us chatting!");
	        //setBounds(100, 100, 385, 266);
	        setBounds(100, 100, 385, 385);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        Container c = getContentPane();
	       final JPanel panel = new JPanel();
	       final JPanel pane2 = new JPanel(new GridLayout(2,1,10,10));
	        c.add(panel, BorderLayout.SOUTH);
	        c.add(pane2, BorderLayout.EAST);
	        final JLabel label = new JLabel();
	        label.setText("输入聊天内容：");
	        panel.add(label);
	        
	        tf_send = new JTextField();
	        tf_send.addActionListener(new ActionListener() {
	            public void actionPerformed(final ActionEvent e) {
	                send();// 调用方法发送信息
	            }
	        });
	        tf_send.setPreferredSize(new Dimension(190, 25));
	        panel.add(tf_send);
	        
	        final JButton button = new JButton();
	        button.addActionListener(new ActionListener() {
	            public void actionPerformed(final ActionEvent e) {
	                send();// 调用方法发送信息
	            }
	        });
	        button.setText("发  送");
	        panel.add(button);
	        
	        final JButton button2 = new JButton();
	        button2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Object[] ob = {"个人主页","联系方式"};
					int m = JOptionPane.showOptionDialog(null,
								"copyright@gpwxxf.top ", "Most Important",
								JOptionPane.YES_NO_OPTION, 
								JOptionPane.QUESTION_MESSAGE,null,ob,ob[0]);
					if(m==0) {
						try {
							browse("http://gpwxxf.top/");
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}else {
						JOptionPane.showMessageDialog(null, "mrgaopw@hotmail.com ", "联系方式", JOptionPane.PLAIN_MESSAGE);
					}
					
				}
			}
	        );
	        button2.setText("版 权");
	        pane2.add(button2);
	        
	        final JScrollPane scrollPane = new JScrollPane();
	        getContentPane().add(scrollPane, BorderLayout.CENTER);
	        
	        ta_info = new JTextArea();
	        scrollPane.setViewportView(ta_info);
	        //滚动信息条
	        
	    }
	    
	}

