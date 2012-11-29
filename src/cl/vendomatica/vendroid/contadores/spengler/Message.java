package cl.vendomatica.vendroid.contadores.spengler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

import cl.vendomatica.spengler.Comm;

public class Message {

	public Header header = new Header();
	public Body body = new Body();
	OutputStream outputStream;
	InputStream inputStream;


	public Message(OutputStream mmOutStream) {
		outputStream = mmOutStream;
	}

	public Message(InputStream mmInStream) {
		inputStream = mmInStream;
	}

	public void send() {
		header.len[0] = (byte) body.data.length;
		header.chk = header.Checksum();
		body.chk = body.Checksum();

		try {
			outputStream.write(Comm.START_CHAR);
			outputStream.write(header.dst);
			outputStream.write(header.src);
			outputStream.write(header.tsk);
			outputStream.write(header.typ);
			outputStream.write(header.seq);
			outputStream.write(header.len);
			outputStream.write(header.chk);

			outputStream.write(body.data);
			outputStream.write(body.chk);
			outputStream.write(Comm.STOP_CHAR);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		header.seq++;

	}

	public boolean receiveUntilStop() {
		byte[] b = {0x00};

		do
		{
			try {
				inputStream.read(b);
				Log.d("Message", b+"");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} while (b[0] != Comm.STOP_CHAR);

		return (true);

	}
	public boolean receive() {

		try {

			byte b = 0x00;
			byte[] bytes = new byte[0];
			inputStream.read(bytes);

			b= bytes[0];

			if (b != Comm.START_CHAR)
				return (false);

			inputStream.read(header.dst);

			inputStream.read(header.src);

			inputStream.read(bytes);
			header.tsk = bytes[0];

			inputStream.read(bytes);
			header.typ = bytes[0];

			inputStream.read(bytes);
			header.seq = bytes[0];

			inputStream.read(header.len);

			inputStream.read(bytes);
			header.chk = bytes[0];

			int len = header.len[0] + 256 * header.len[1];

			if (body.data != null)
				body.data = null;

			body.data = new byte[len];

			inputStream.read(body.data);

			inputStream.read(bytes);
			body.chk = bytes[0];

			inputStream.read(bytes);
			b = bytes[0];

			if (b != Comm.STOP_CHAR)
				return (false);

			return (true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (false);
		}

	}

}
