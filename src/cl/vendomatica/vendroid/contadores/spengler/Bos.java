package cl.vendomatica.vendroid.contadores.spengler;

import java.io.InputStream;
import java.io.OutputStream;

import cl.vendomatica.spengler.Comm;
import cl.vendomatica.spengler.MainActivity;

import android.util.Log;

public class Bos {
private static final String TAG = "Bos";
//    public Comm comm;
	public byte[] size;
	public byte[] handle;
	public byte[] file;
	public Transaction transaction;
	InputStream inputStream;
	OutputStream outputStream;
	private Comm comm;
	/**
	 * @param mmOutStream 
	 * @param mmInStream 
	 * 
	 */
	public Bos(InputStream mmInStream, OutputStream mmOutStream) {
		Log.d(TAG, "Creando Bos");
		comm = new Comm(mmInStream, mmOutStream);
        size = new byte[4];
        handle = new byte[2];

        transaction = new Transaction(comm);
	}
	
	public boolean connect(String commPort, boolean capturaItagII)
    {
		Log.d(TAG, "Connect");

//        if (!transaction.RemoteXCount())
//            if (!transaction.RemoteXCount())
//                return (false);
            
        return (true);
    }
	
	public boolean getFile(String fileName)
    {
		Log.d(TAG, "getFile");
		
        int n;

        if (!transaction.RemoteFirst(fileName, size))
            return (false);

        n = MainActivity.convertirByte(size[0]) + 256 * MainActivity.convertirByte(size[1]);

        if (n==0)
            return (false);

        if (file != null)
            file = null;

        file = new byte[n];

        if (!transaction.RemoteOpen(fileName, handle))
            return (false);

        if (!transaction.RemoteRead(handle, size, file))
            return (false);

        if (!transaction.RemoteClose(handle))
            return (false);

        return (true);
    }
	
	
	public void disconnect(boolean capturaItagII)
    {
        transaction = null;
    }
	
	

}
