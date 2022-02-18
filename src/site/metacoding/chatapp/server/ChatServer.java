package site.metacoding.chatapp.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

// v2 - �ٴ�� ���
public class ChatServer {

	// ������ - ���ν�����(���� ���� ������ �𸣴� ���� �ٻ�)
	// ������ �޽��� �޾Ƽ� ������ (Ŭ���̾�Ʈ ������) - ���ο� ������

	ServerSocket serverSocket; // ������
	List<�����㽺����> ������Ʈ; // socket ��Ƶα�

	// �����ڿ��� ����
	public ChatServer() {
		// ��� ����� ���ܹ߻� -> try catch ���
		try {
			serverSocket = new ServerSocket(2000);
			������Ʈ = new Vector<>(); // ����ȭ ó���� ArrayList
			// Ŭ���̾�Ʈ ������ ������ �ʿ� -> while
			while (true) {
				// socket�� ���Ϲ� ����Ǹ� ���󰡴� �������� -> �÷��ǿ� ��Ƶα�
				Socket socket = serverSocket.accept();
				System.out.println("Ŭ���̾�Ʈ �����");
				�����㽺���� t = new �����㽺����(socket);
				������Ʈ.add(t);
				System.out.println("������Ʈ ũ�� : " + ������Ʈ.size());
				new Thread(t).start(); // t�� ����
			}

		} catch (Exception e) {
			System.out.println("��� ���� �߻� : " + e.getMessage());
		}
	}

	// ���� Ŭ����
	class �����㽺���� implements Runnable {

		Socket socket; // ���� ����
		BufferedReader reader;
		BufferedWriter writer;
		boolean isLogin = true;

		public �����㽺����(Socket socket) {
			this.socket = socket;

			// BR, BW �����
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			while (isLogin) {
				try {
					// �޽��� �б�
					String inputData = reader.readLine();
					System.out.println("from Ŭ���̾�Ʈ : " + inputData);

					// �÷��ǿ� ��� ���� �޽����� ��� Ŭ���̾�Ʈ���� ������(BW)
					for (�����㽺���� t : ������Ʈ) { // �� : �÷��� Ÿ��, �� : �÷���
						t.writer.write(inputData + "\n");
						t.writer.flush();
					}
				} catch (Exception e) {
					try {
						System.out.println("��� ���� : " + e.getMessage());
						isLogin = false;
						������Ʈ.remove(this); // �� �̻� ������ ����Ű�� �ּ� X -> ������ �÷��� ���
						// ������ �÷����� �����̶� �� ���� �����. (����� ���ϸ� ���̱� ����)
						reader.close();
						writer.close();
						socket.close();
					} catch (Exception e1) {
						System.out.println("�������� ���μ��� ����" + e1.getMessage());
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		new ChatServer();
	}
}
