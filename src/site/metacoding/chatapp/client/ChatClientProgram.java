package site.metacoding.chatapp.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClientProgram {

	Socket socket; // Ŭ���̾�Ʈ ����
	// ��ĳ�� ���� ��ư ������ ���۵ǰ�,,
	Scanner sc; // Ű����κ��� �Է� �޾Ƽ�
	BufferedWriter writer; // ������ �޽��� ������
	BufferedReader reader; // �������� ���� �޽��� �б�

	// �����ڿ��� ����
	public ChatClientProgram() {
		// ��� ����� ���ܹ߻� -> try catch
		try {
			socket = new Socket("localhost", 2000); // ip�ּ�, ��Ʈ

			// ���� ���� (������)
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			sc = new Scanner(System.in);
			// ���� ���� (�б�)
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// �޽����� �б� ����(BR) ���ο� ������
			new Thread(new �б����㽺����()).start();

			// Ű����� �Է� ���� �޽��� ������ - ���� ������
			while (true) {
				String keyboardInputData = sc.nextLine();
				writer.write(keyboardInputData + "\n"); // ���ۿ� ���
				writer.flush(); // ���� �� ������ (��Ʈ������ ��� ������)
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ���� Ŭ����
	class �б����㽺���� implements Runnable {

		@Override
		public void run() {
			try {
				while (true) {
					String inputData = reader.readLine();
					System.out.println("���� �޽��� : " + inputData);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new ChatClientProgram();
	}

}
