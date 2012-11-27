package cl.vendomatica.vendroid.contadores.spengler;

import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

public class Transaction {
private static final String TAG = "Transaction";
//    private Comm _comm;
//
    public Message request;
    public Message response;

	public Transaction(InputStream mmInStream, OutputStream mmOutStream/*Comm comm*/)
    {
		Log.d(TAG, "Creando objeto");
//        _comm = comm;
//
        request = new Message(mmOutStream);
        response = new Message(mmInStream);
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
            return (false);
        }
    }

    public boolean RemoteXCount()
    {
        int i;

        TxXCount txXCount = new TxXCount();

        request.body.data = txXCount.ToByteArray();

        try
        {
            if (Execute())
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

        TxFirst txFirst = new TxFirst(fileName);

        request.body.data = txFirst.ToByteArray();

        try
        {
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
