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
	boolean debug = true;

	public Message(Comm comm) {
		_comm = comm;
	}

	public void send() {
		Log.d(TAG, "Ejecutando send");
		header.len[0] = (byte) body.data.length;
		header.chk = header.Checksum();
		body.chk = body.Checksum();
		if(debug)
		Log.d(TAG, "Enviando Comm.START_CHAR: " + Comm.START_CHAR);
		_comm.write(Comm.START_CHAR);
		if(debug)
		Log.d(TAG, "Enviando header.dst: " + Arrays.toString(header.dst));
		_comm.write(header.dst);
		if(debug)
		Log.d(TAG, "Enviando header.src: " + Arrays.toString(header.src));
		_comm.write(header.src);
		if(debug)
		Log.d(TAG, "Enviando header.tsk: " + (int)header.tsk);
		_comm.write(header.tsk);
		if(debug)
		Log.d(TAG, "Enviando header.typ: " + (int)header.typ);
		_comm.write(header.typ);
		if(debug)
		Log.d(TAG, "Enviando header.seq: " + (int)header.seq);
		_comm.write(header.seq);
		if(debug)
		Log.d(TAG, "Enviando header.len: " + Arrays.toString(header.len));
		_comm.write(header.len);
		if(debug)
		Log.d(TAG, "Enviando header.chk: " + (int)header.chk);
		_comm.write(header.chk);
		if(debug)
		Log.d(TAG, "Enviando body.data: " + Arrays.toString(body.data));
		_comm.write(body.data);
		if(debug)
		Log.d(TAG, "Enviando body.chk: " + (int)body.chk);
		_comm.write(body.chk);
		if(debug)
		Log.d(TAG, "Enviando Comm.STOP_CHAR: " + Comm.STOP_CHAR);
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

		//L 60
		byte b = 0x00;
		
		CommReadReturn commReadReturn = new CommReadReturn();
		//L 62
		commReadReturn = _comm.read( b);
		if (!commReadReturn.isResult()){
			if(debug)
			Log.d(TAG, "retorna falso");
            return (false);
		}
		//L 65
		b = commReadReturn.getB();
		if(debug)
		Log.d(TAG, "Receive b: " + b);
        if (b != Comm.START_CHAR)
            return (false);

//        commReadReturn = _comm.read( b);
        if (!_comm.read( header.dst))
            return (false);
        if(debug)
		Log.d(TAG, "Receive header.dst: " + Arrays.toString(header.dst));

        if (!_comm.read( header.src))
            return (false);
        if(debug)
        Log.d(TAG, "Receive header.src: " + Arrays.toString(header.src));

        commReadReturn = _comm.read( header.tsk);
        if (!commReadReturn.isResult())
            return (false);
        header.tsk = commReadReturn.getB();
        if(debug)
        Log.d(TAG, "Receive header.tsk: " + header.tsk);

        commReadReturn = _comm.read(header.typ);
        if (!commReadReturn.isResult())
            return (false);
        header.typ = commReadReturn.getB();
        if(debug)
        Log.d(TAG, "Receive header.typ: " + header.typ);

        commReadReturn = _comm.read(header.seq);
        if (!commReadReturn.isResult())
            return (false);
        header.seq = commReadReturn.getB();
        if(debug)
        Log.d(TAG, "Receive header.seq: " + header.seq);

        if (!_comm.read( header.len))
            return (false);
        if(debug)
        Log.d(TAG, "Receive header.len: " + Arrays.toString(header.len));

        commReadReturn = _comm.read( header.chk);
        if (!commReadReturn.isResult())
            return (false);
        header.chk = commReadReturn.getB();
        if(debug)
        Log.d(TAG, "Receive  header.chk: " + header.chk);

        int len = (0x000000FF & ((int)header.len[0]))  + 256 * (0x000000FF & ((int)header.len[1]));
//        int len = header.len[0] + 256 * header.len[1];

        if (body.data != null)
            body.data = null;

        body.data = new byte[len];

        if (!_comm.read( body.data))
            return (false);
        if(debug)
        Log.d(TAG, "Receive body.data: " + Arrays.toString(body.data));

        commReadReturn = _comm.read( body.chk);
        if (!commReadReturn.isResult())
            return (false);
        body.chk = commReadReturn.getB();
        if(debug)
        Log.d(TAG, "Receive  body.chk: " + body.chk);

        commReadReturn = _comm.read( b);
        if (!commReadReturn.isResult())
            return (false);
        b = commReadReturn.getB();
        if(debug)
        Log.d(TAG, "Receive b: " + b);

        //if (b != Comm.STOP_CHAR)
        //    return (false);


        Log.d(TAG, "Retorna true" );
        return (true);
		
	}

}
