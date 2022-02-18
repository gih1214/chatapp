package site.metacoding.chatapp.test1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ScrollPane;
import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChatClientProgram extends JFrame {

	// GUI ����
	// ���(northPanel), ����(scroll), �ϴ�(southPanel)
	JPanel northPanel, southPanel;
	TextArea chatList; // ���� ���� ���ڿ��� �Է� ���� �� �ִ� â
	ScrollPane scroll; // ������Ʈ�� ��ũ�� ��� ����
	JTextField txtHost, txtPort, txtMsg; // 1�� �ؽ�Ʈ �ڽ�
	JButton btnConnect, btnSend; // ��ư

	// ��� ����
	Socket socket;
	BufferedWriter writer;
	BufferedReader reader;

	// �����ڿ��� ����
	public ChatClientProgram() {
		initUI(); // ������
		initListener(); // ���� ����, �޽��� ����

		setVisible(true);
	}

	private void initUI() {
		// 1. ��� ������
		northPanel = new JPanel();
		// ip �ּ�
		txtHost = new JTextField(20); // txt�ڽ� ������ 20
		txtHost.setText("127.0.0.1");
		// ��Ʈ��ȣ
		txtPort = new JTextField(5);
		txtPort.setText("2000");
		// ���������ư
		btnConnect = new JButton("Connect");

		northPanel.add(txtHost);
		northPanel.add(txtPort);
		northPanel.add(btnConnect);

		// 2. ���� ������
		chatList = new TextArea(10, 30); // ����, ���α���
		chatList.setBackground(Color.PINK); // ��� ����
		chatList.setForeground(Color.DARK_GRAY); // �ؽ�Ʈ ����
		scroll = new ScrollPane();

		scroll.add(chatList); // ��ũ�� �ȿ� �ؽ�Ʈ �ڽ� �ֱ�

		// 3. �ϴ� ������
		southPanel = new JPanel();
		txtMsg = new JTextField(25);
		btnSend = new JButton("Send");

		southPanel.add(txtMsg);
		southPanel.add(btnSend);

		// 4. ������ ����
		// ������ �������� ������ ������ ���� �������� ������ �߻��ؼ� 4���� ������ �� ������.
		setTitle("MyChat1.0");
		setSize(400, 500);
		setLocationRelativeTo(null); // ���� �� â�� ȭ�� ��� ��ġ���ִ� �޼���
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // â�ݱ� -> �����ڵ�����

		add(northPanel, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
	}

	private void initListener() {
		btnConnect.addActionListener((e) -> {

			connect(); // ���� ����
		});

		btnSend.addActionListener((e) -> {
			send(); // �޽��� ����
		});
	}

	private void connect() {
		try {
			// 1. host�ּ�(ip �ּ�)�� port ��������
			String host = txtHost.getText();
			// txtPort�� String Ÿ���̶� int Ÿ������ ��ȯ����� �Ѵ�.
			// Integer.parseInt(String s)
			// ���� int�� �� �ٲٳ� �;��µ� socket�� ��Ʈ��ȣ Ÿ���� int���� ����.
			int port = Integer.parseInt(txtPort.getText());

			// 2. ���� �����ϰ� ���۴ޱ�
			socket = new Socket(host, port);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// 3. �б� ���� ������ ����� (���°� ���� �����尡)
			new Thread(() -> {
				read();
			}).start();

			System.out.println("���� ������ �Ϸ�Ǿ����ϴ�.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �޽��� ���� : ���ν�����
	private void send() {
		try {
			String msg = txtMsg.getText();
			chatList.append(msg + "\n");
			// ������� �ߴ�..
			// txtMsg.setText(""); // ����
			// txtMsg.requestFocus(); // Ŀ�� �α�

			writer.write(msg + "\n");
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �޽��� �б� : ���ο� ������
	private void read() {

	}

	public static void main(String[] args) {
		new ChatClientProgram();
	}

}
