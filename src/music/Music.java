package music;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Music {

	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("�������ѡ����ѡ����������˳�(quit)��\n\t������գ���˹�");
		System.out.print(">>>");
		
		String s = "quit";
		try {
			s = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (!s.equals("quit")) {
			
			System.out.println("�������ѡ����ѡ����������˳�(quit)��\n\t������գ���˹�");
			System.out.print(">>>");
			
			try {
				s = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Quitting");
		
	}

}
