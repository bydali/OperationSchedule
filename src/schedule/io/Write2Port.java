package schedule.io;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import schedule.model.TimeTable;
import schedule.viewmodel.TimeTableVM;

public class Write2Port {
	public static void sendTimeTable(String server, String port, int typeCode) {
		try {
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						ByteArrayOutputStream buffers = new ByteArrayOutputStream();
						ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(buffers));
						out.writeObject(TimeTableVM.timeTable);
						out.close();

						DatagramSocket datagramSocket = new DatagramSocket();
						InetAddress address0 = InetAddress.getByName(server);
						byte[] data = buffers.toByteArray();
						buffers.close();
						DatagramPacket datagramPacket2 = new DatagramPacket(data, data.length, address0,
								Integer.parseInt(port));
						byte[] head = BigLittleConverter.intToByteArr(data.length);
						DatagramPacket datagramPacket1 = new DatagramPacket(head, head.length, address0,
								Integer.parseInt(port));
						byte[] type = BigLittleConverter.intToByteArr(typeCode);
						DatagramPacket datagramPacket0 = new DatagramPacket(type, type.length, address0,
								Integer.parseInt(port));

						datagramSocket.send(datagramPacket0);
						datagramSocket.send(datagramPacket1);
						datagramSocket.send(datagramPacket2);

						datagramSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static void sendTimeTable(String server, String port, TimeTable timeTable) {
		try {
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						ByteArrayOutputStream buffers = new ByteArrayOutputStream();
						ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(buffers));
						out.writeObject(timeTable);
						out.close();

						DatagramSocket datagramSocket = new DatagramSocket();
						InetAddress address0 = InetAddress.getByName(server);
						byte[] data = buffers.toByteArray();
						buffers.close();
						DatagramPacket datagramPacket2 = new DatagramPacket(data, data.length, address0,
								Integer.parseInt(port));
						byte[] head = BigLittleConverter.intToByteArr(data.length);
						DatagramPacket datagramPacket1 = new DatagramPacket(head, head.length, address0,
								Integer.parseInt(port));
						byte[] type = BigLittleConverter.intToByteArr(3);
						DatagramPacket datagramPacket0 = new DatagramPacket(type, type.length, address0,
								Integer.parseInt(port));

						datagramSocket.send(datagramPacket0);
						datagramSocket.send(datagramPacket1);
						datagramSocket.send(datagramPacket2);

						datagramSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
