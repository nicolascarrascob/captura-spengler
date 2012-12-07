package cl.vendomatica.vendroid.contadores.spengler;

import java.util.Arrays;

import android.util.Log;
import cl.vendomatica.spengler.Comm;
import cl.vendomatica.spengler.CommReadReturn;

public class Message {

	private static final String TAG = "Message";
	public Header header = new Header();
	public Body body = new Body();
	private Comm _comm;

	public Message(Comm comm) {
		_comm = comm;
	}

	public void send() {

		Log.d(TAG, "Ejecutando send");
		header.len[0] = (byte) body.data.length;
		header.chk = header.Checksum();
		body.chk = body.Checksum();

//		Log.d(TAG, "Enviando Comm.START_CHAR: " + Comm.START_CHAR);
		_comm.write(Comm.START_CHAR);
		
//		Log.d(TAG, "Enviando header.dst: " + Arrays.toString(header.dst));
		_comm.write(header.dst);
		
//		Log.d(TAG, "Enviando header.src: " + Arrays.toString(header.src));
		_comm.write(header.src);
		
//		Log.d(TAG, "Enviando header.tsk: " + (int)header.tsk);
		_comm.write(header.tsk);
		
//		Log.d(TAG, "Enviando header.typ: " + (int)header.typ);
		_comm.write(header.typ);
		
//		Log.d(TAG, "Enviando header.seq: " + (int)header.seq);
		_comm.write(header.seq);
		
//		Log.d(TAG, "Enviando header.len: " + Arrays.toString(header.len));
		_comm.write(header.len);
		
//		Log.d(TAG, "Enviando header.chk: " + (int)header.chk);
		_comm.write(header.chk);

//		Log.d(TAG, "Enviando body.data: " + Arrays.toString(body.data));
		_comm.write(body.data);
		
//		Log.d(TAG, "Enviando body.chk: " + (int)header.chk);
		_comm.write(body.chk);
		
//		Log.d(TAG, "Enviando Comm.STOP_CHAR: " + Comm.STOP_CHAR);
		_comm.write(Comm.STOP_CHAR);

		header.seq++;

	}

	public boolean receiveUntilStop() {
		Log.d(TAG, "Ejecutando receiveUntilStop");
		byte b = 0x00;

		do
		{	
			CommReadReturn commReadReturn = new CommReadReturn();
			commReadReturn = _comm.read(b);
			if (!commReadReturn.isResult()){
				Log.d(TAG, "Retorna false");
                return (false);
			}
			b = commReadReturn.getB();
//			Log.d(TAG, "Recibe: " + (0x000000FF & ((int)b)) + ""); 

		} while (b != Comm.STOP_CHAR);

		Log.d(TAG, "Retorna true");
		return (true);

	}
	public boolean receive() {
		Log.d(TAG, "Ejecutando receive");

		byte b = 0x00;
		
		CommReadReturn commReadReturn = new CommReadReturn();
		
		commReadReturn = _comm.read( b);
		if (!commReadReturn.isResult()){
			Log.d(TAG, "retorna falso");
            return (false);
		}
		
		b = commReadReturn.getB();
		Log.d(TAG, "Receive b: " + b);
        if (b != Comm.START_CHAR)
            return (false);

        commReadReturn = _comm.read( b);
        if (!_comm.read( header.dst))
            return (false);
        

		Log.d(TAG, "Receive header.dst: " + Arrays.toString(header.dst));

        if (!_comm.read( header.src))
            return (false);
        
        Log.d(TAG, "Receive header.src: " + Arrays.toString(header.src));

        commReadReturn = _comm.read( header.tsk);
        if (!commReadReturn.isResult())
            return (false);
        header.tsk = commReadReturn.getB();
        Log.d(TAG, "Receive header.tsk: " + header.tsk);

        commReadReturn = _comm.read(header.typ);
        if (!commReadReturn.isResult())
            return (false);
        header.typ = commReadReturn.getB();
        Log.d(TAG, "Receive header.typ: " + header.typ);

        commReadReturn = _comm.read(header.seq);
        if (!commReadReturn.isResult())
            return (false);

        header.seq = commReadReturn.getB();
        Log.d(TAG, "Receive header.seq: " + header.seq);

        if (!_comm.read( header.len))
            return (false);
        Log.d(TAG, "Receive header.len: " + Arrays.toString(header.len));

        commReadReturn = _comm.read( header.chk);
        if (!commReadReturn.isResult())
            return (false);
        header.chk = commReadReturn.getB();
        Log.d(TAG, "Receive  header.chk: " + header.chk);

        int len = (0x000000FF & ((int)header.len[0]))  + 256 * (0x000000FF & ((int)header.len[1]));

        if (body.data != null)
            body.data = null;

        body.data = new byte[len];

        if (!_comm.read( body.data))
//            return (false);

        Log.d(TAG, "Receive body.data: " + Arrays.toString(body.data));

        commReadReturn = _comm.read( body.chk);
        if (!commReadReturn.isResult())
//            return (false);
        body.chk = commReadReturn.getB();
        Log.d(TAG, "Receive  body.chk: " + body.chk);

//        commReadReturn = _comm.read( b);
//        if (!commReadReturn.isResult())
//            return (false);
//        b = commReadReturn.getB();
//
//        Log.d(TAG, "Receive b: " + b);

        //if (b != Comm.STOP_CHAR)
        //    return (false);


        Log.d(TAG, "Retorna true" );
        return (true);
		
	}

}
