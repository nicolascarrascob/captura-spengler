package cl.vendomatica.vendroid.contadores.spengler;

import android.util.Log;

public class TxXCount {
    private static final String TAG = "TxXCount";
	public byte[] Func;
    public byte[] Count;

    public byte[] buffer;

	public TxXCount() {
		Log.d(TAG, "Creado objeto");
        Func = new byte[] { 0x03,  (byte)0xFF };
        Count = new byte[] { 0x00, 0x00, 0x00, 0x00 };
        buffer = new byte[Func.length + Count.length];
	}
    
	public byte[] ToByteArray()
    {
        int i, j = 0;
        
        for (i = 0; i < Func.length; i++)
        {
            buffer[j] = Func[i];
            j++;
        }

        for (i = 0; i < Count.length; i++)
        {
            buffer[j] = Count[i];
            j++;
        }

        return (buffer);
    }

}
