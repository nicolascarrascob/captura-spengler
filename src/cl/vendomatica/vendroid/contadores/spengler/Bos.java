package cl.vendomatica.vendroid.contadores.spengler;

import android.util.Log;

public class Bos {
private static final String TAG = "Bos";
//    public Comm comm;
	public byte[] size;
	public byte[] handle;
	public byte[] file;
	public Transaction transaction;
	/**
	 * 
	 */
	public Bos() {
		Log.d(TAG, "Creando objeto");
//		comm = new Comm();
        size = new byte[4];
        handle = new byte[2];

//        transaction = new Transaction(comm);
        transaction = new Transaction();
	}
	
	public boolean connect(String commPort, boolean capturaItagII)
    {
		//Ya deberia estar conectado
//        if (!comm.Open(commPort,capturaItagII))
//        {
//            Thread.Sleep(100);
//            if (!comm.Open(commPort, capturaItagII))
//                return (false);
//        }
            
        if (capturaItagII)
            //se conecta con el itagII y configura el puerto
            if (!this.configurarItagII())
                return (false);

        if (!transaction.RemoteXCount())
            if (!transaction.RemoteXCount())
                return (false);
            
        return (true);
    }
	
	public boolean getFile(String fileName)
    {
		
        int n;

        if (!transaction.RemoteFirst(fileName, size))
            return (false);

        n = size[0] + 256 * size[1];

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
	
	private boolean configurarItagII()
    {
        try
        {
        	boolean configuracionItagII = false;
//            comm.RTS = true;
            Thread.sleep(100);

            configuracionItagII = configurarPuertoItagII(2);
            if (configuracionItagII)
            {
//                comm.RTS = false;
                return (true);
            }
            else
                return (false);
        }
        catch (Exception e) {
            return (false);
        }
    }
	
	public void disconnect(boolean capturaItagII)
    {
        transaction = null;
//        comm.Close(capturaItagII);
    }
	
	private boolean configurarPuertoItagII(int reintentos)
    {
        int count = 0;
        String buff = "";
        byte[] ETX = { 0x03 };
        do
        {
//            comm.Write("\x02" + "80420319" + "\x03");
//            buff = comm.Leer();
            count++;
//            if(buff.IndexOf(Encoding.ASCII.GetString(ETX,0,1)) != 0)
//                return (true);
        }
        while (count < reintentos);

        return (false);
    }
	

}
