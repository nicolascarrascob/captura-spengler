package cl.vendomatica.vendroid.contadores.spengler;

import android.util.Log;

public class TxFirst {
    public byte[] Func = new byte[] { 0x00, 0x4E };
    public byte[] Attrib = new byte[] { 0x00, 0x00 };
    public byte[] FName;

    public byte[] buffer;

    
    public TxFirst(String fileName)
    {
        int i;
        char c;

        FName = new byte[fileName.length() + 1];

        Log.d("TxFirst", "TxFirst: " + fileName);
        for (i = 0; i < fileName.length(); i++)
        {
            c = (char) fileName.substring(i, i+1).charAt(0);
            FName[i] = (byte)c;
        }

        FName[i] = 0x00;
    }
    
    public byte[] ToByteArray()
    {
        int i, j = 0;
        int length = Func.length + Attrib.length + FName.length;

        buffer = new byte[length];

        for (i = 0; i < Func.length; i++)
        {
            buffer[j] = Func[i];
            j++;
        }

        for (i = 0; i < Attrib.length; i++)
        {
            buffer[j] = Attrib[i];
            j++;
        }

        for (i = 0; i < FName.length; i++)
        {
            buffer[j] = FName[i];
            j++;
        }

        return (buffer);
    }
    
}
