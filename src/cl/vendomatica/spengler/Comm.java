package cl.vendomatica.spengler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

public class Comm {

	private static final byte ESC_CHAR = 0x2F;
	public static final byte START_CHAR = 0x3A;
	public static final byte STOP_CHAR = 0x0D;
	private final byte START_CHAR2 = 0x3B;
	private final byte START_CHAR3 = 0x23;
	private int TIMEOUT = 30;
	private byte ENDCHAR = 0x03;

	OutputStream outputStream;
	InputStream inputStream;

	public Comm(InputStream mmInStream, OutputStream mmOutStream) {
		outputStream = mmOutStream;
		inputStream = mmInStream;
	}

	public void write(byte data)
	{
		byte[] buffer = new byte[] { data };

		try
		{
			int i = 0x000000FF & ((int) buffer[0]);
			Log.d("COMM","Envia: " + i +"");
			outputStream.write(i);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			buffer = null;
		}
	}

	public void write(byte[] data)
	{
		int i; 

		for (i = 0; i < data.length; i++)
		{
			switch (data[i])
			{
			case ESC_CHAR:
				write(ESC_CHAR);
				write((byte)0x30);
				break;
			case START_CHAR:
				write(ESC_CHAR);
				write((byte)0x31);
				break;
			case STOP_CHAR:
				write(ESC_CHAR);
				write((byte)0x32);
				break;
			case START_CHAR2:
				write(ESC_CHAR);
				write((byte)0x33);
				break;
			case START_CHAR3:
				write(ESC_CHAR);
				write((byte)0x34);
				break;
			default:
				write(data[i]);
				break;
			}
		}
	}

	public CommReadReturn read( byte data)
	{
		CommReadReturn resultado = new CommReadReturn();
		int n;
		byte[] buffer = new byte[1];

		try {

			if(inputStream.available() == 0){
				Thread.sleep(5000); // emula timeout
				if(inputStream.available() == 0){
					Log.d("COMM","no vienen mas datos");
					resultado.setResult(false);
					return resultado;
				}
			}

			n = inputStream.read(buffer, 0, 1);
			data = buffer[0];

			Log.d("COMM","Lee: " + data +"");
			resultado.setB(data);
			resultado.setResult(n!= 0);
			return resultado;
		} catch (IOException e) {
			e.printStackTrace();
			resultado.setResult(false);
			return resultado;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultado.setResult(false);
			return resultado;
		}

		finally
		{
			buffer = null;
		}
	}

	public boolean read( byte[] data)
	{

		int i;
		byte b = 0x00;

		for (i = 0; i < data.length; i++)
		{
			CommReadReturn retornoRead = read( b);
			if ( !retornoRead.isResult()) {

				Log.d("COMM","Lee: retorna false");
				return (false);
			}

			b = retornoRead.getB();
			if (b != ESC_CHAR)
				data[i] = b;
			else
			{
				retornoRead = read( b);
				if (!retornoRead.isResult() )
					return (false);

				switch (b)
				{
				case 0x30:                                
					data[i] = ESC_CHAR;                                
					break;
				case 0x31:                                
					data[i] = START_CHAR;                                
					break;
				case 0x32:                                
					data[i] = STOP_CHAR;                                
					break;
				case 0x33:                                
					data[i] = START_CHAR2;                                
					break;
				case 0x34:                                
					data[i] = START_CHAR3;                           
					break;
				default:                                
					return (false);
				}                        
			}
		}

		return (true);
	}
}
