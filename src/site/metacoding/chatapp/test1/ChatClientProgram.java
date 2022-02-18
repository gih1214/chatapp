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

	// GUI 관련
	// 상단(northPanel), 센터(scroll), 하단(southPanel)
	JPanel northPanel, southPanel;
	TextArea chatList; // 여러 줄의 문자열을 입력 받을 수 있는 창
	ScrollPane scroll; // 컴포넌트에 스크롤 기능 제공
	JTextField txtHost, txtPort, txtMsg; // 1줄 텍스트 박스
	JButton btnConnect, btnSend; // 버튼

	// 통신 관련
	Socket socket;
	BufferedWriter writer;
	BufferedReader reader;

	// 생성자에서 실행
	public ChatClientProgram() {
		initUI(); // 디자인
		initListener(); // 서버 연결, 메시지 전송

		setVisible(true);
	}

	private void initUI() {
		// 1. 상단 디자인
		northPanel = new JPanel();
		// ip 주소
		txtHost = new JTextField(20); // txt박스 사이즈 20
		txtHost.setText("127.0.0.1");
		// 포트번호
		txtPort = new JTextField(5);
		txtPort.setText("2000");
		// 서버연결버튼
		btnConnect = new JButton("Connect");

		northPanel.add(txtHost);
		northPanel.add(txtPort);
		northPanel.add(btnConnect);

		// 2. 센터 디자인
		chatList = new TextArea(10, 30); // 세로, 가로길이
		chatList.setBackground(Color.PINK); // 배경 색상
		chatList.setForeground(Color.DARK_GRAY); // 텍스트 색상
		scroll = new ScrollPane();

		scroll.add(chatList); // 스크롤 안에 텍스트 박스 넣기

		// 3. 하단 디자인
		southPanel = new JPanel();
		txtMsg = new JTextField(25);
		btnSend = new JButton("Send");

		southPanel.add(txtMsg);
		southPanel.add(btnSend);

		// 4. 프레임 세팅
		// 디자인 설정보다 프레임 세팅을 먼저 적었더니 오류가 발생해서 4번은 디자인 후 해주자.
		setTitle("MyChat1.0");
		setSize(400, 500);
		setLocationRelativeTo(null); // 실행 시 창을 화면 가운데 배치해주는 메서드
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창닫기 -> 실행자동종료

		add(northPanel, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
	}

	private void initListener() {
		btnConnect.addActionListener((e) -> {

			connect(); // 서버 연결
		});

		btnSend.addActionListener((e) -> {
			send(); // 메시지 전송
		});
	}

	private void connect() {
		try {
			// 1. host주소(ip 주소)와 port 가져오기
			String host = txtHost.getText();
			// txtPort가 String 타입이라 int 타입으로 반환해줘야 한다.
			// Integer.parseInt(String s)
			// 굳이 int로 왜 바꾸나 싶었는데 socket에 포트번호 타입이 int여서 였다.
			int port = Integer.parseInt(txtPort.getText());

			// 2. 서버 연결하고 버퍼달기
			socket = new Socket(host, port);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// 3. 읽기 전용 스레드 만들기 (쓰는건 메인 스레드가)
			new Thread(() -> {
				read();
			}).start();

			System.out.println("서버 연결이 완료되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 메시지 전송 : 메인스레드
	private void send() {
		try {
			String msg = txtMsg.getText();
			chatList.append(msg + "\n");
			// 여기까지 했당..
			// txtMsg.setText(""); // 비우기
			// txtMsg.requestFocus(); // 커서 두기

			writer.write(msg + "\n");
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 메시지 읽기 : 새로운 스레드
	private void read() {

	}

	public static void main(String[] args) {
		new ChatClientProgram();
	}

}
