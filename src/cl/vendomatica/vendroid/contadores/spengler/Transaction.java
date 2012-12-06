package cl.vendomatica.vendroid.contadores.spengler;

import android.util.Log;
import cl.vendomatica.spengler.Comm;

public class Transaction {
private static final String TAG = "Transaction";
    public Message request;
    public Message response;
    private Comm _comm;

    
	
	public Transaction(Comm comm) {
		Log.d(TAG, "Creando objeto");
		_comm = comm;
      request = new Message(_comm);
      response = new Message(_comm);
	}

	private boolean Execute()
    {
        try
        {
        	response.receiveUntilStop();

            Thread.sleep(100);

            request.send();

            Thread.sleep(100);

            if (!response.receiveUntilStop())
                return (false);

            Thread.sleep(100);

            if (!response.receive())
                return (false);

            return (true);
        }
        catch (Exception e) {
        	e.printStackTrace();
            return (false);
        }
    }

    public boolean RemoteXCount()
    {
    	Log.d(TAG, "Ejecutando RemoteXCount");
        int i;

        TxXCount txXCount = new TxXCount();

        request.body.data = txXCount.ToByteArray();

        try
        {
        	Log.d(TAG, "Ejecutando RemoteXCount Execute");
        	boolean exe = Execute();
            if (exe)
            {
                for (i = 0; i < 4; i++)
                    request.header.dst[i] = response.header.src[i];

                return (true);
            }
            else
                return (false);
        }
        finally
        {    
            request.body.data = null;
            response.body.data = null;
        }
		
    }

    public boolean RemoteFirst(String fileName, byte[] size)
    {
        int i;
        Log.d(TAG, "Ejecutando RemoteFirst: " + fileName);
        TxFirst txFirst = new TxFirst(fileName);

        request.body.data = txFirst.ToByteArray();

        try
        {
        	Log.d(TAG, "Ejecutando RemoteFirst Execute");
            if (!Execute())
                return (false);

            RxFirst rxFirst = new RxFirst(response.body.data);

            try
            {
                for (i = 0; i < rxFirst.Size.length; i++)
                    size[i] = rxFirst.Size[i];

                return (true);
            }
            finally
            {
                rxFirst = null;
            }
        }
        finally
        {
            txFirst = null;
            request.body.data = null;
            response.body.data = null;
        } 
    }

    public boolean RemoteOpen(String fileName, byte[] handle)
    {

    	Log.d(TAG, "Ejecutando RemoteOpen");
        int i;

        TxFOpen txFOpen = new TxFOpen(fileName);

        request.body.data = txFOpen.ToByteArray();

        try
        {
            if (!Execute())
                return (false);

            RxFOpen rxFOpen = new RxFOpen(response.body.data);

            try
            {
                for (i = 0; i < rxFOpen.hFile.length; i++)
                    handle[i] = rxFOpen.hFile[i];

                return (true);
            }
            finally
            {
                rxFOpen = null;
            }
        }
        finally
        {
            txFOpen = null;
            request.body.data = null;
            response.body.data = null;
        } 
    }

    public boolean RemoteRead(byte[] handle, byte[] size, byte[] file)
    {

    	Log.d(TAG, "Ejecutando RemoteOpen");
        int i;

        TxFRead txFRead = new TxFRead(handle, size);

        request.body.data = txFRead.ToByteArray();

        try
        {
            if (!Execute())
                return (false);

            RxFRead rxFRead = new RxFRead(response.body.data);

            try
            {
                for (i = 0; i < rxFRead.dta.length; i++)
                    file[i] = rxFRead.dta[i];

                return (true);
            }
            finally
            {
                rxFRead = null;
            }
        }
        finally
        {
            txFRead = null;
            request.body.data = null;
            response.body.data = null;
        }
    }

    public boolean RemoteClose(byte[] handle)
    {

    	Log.d(TAG, "Ejecutando RemoteOpen");
        TxFClose txFClose = new TxFClose(handle);

        request.body.data = txFClose.ToByteArray();

        try
        {
            if (!Execute())
                return (false);

            return (true);
        }
        finally
        {
            txFClose = null;
            request.body.data = null;
            response.body.data = null;
        }
    }
}
