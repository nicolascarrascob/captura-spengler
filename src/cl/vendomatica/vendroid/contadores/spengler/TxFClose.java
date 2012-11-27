package cl.vendomatica.vendroid.contadores.spengler;

public class TxFClose {
    public byte[] Func = new byte[] { 0x00, 0x3E };
    public byte[] hFile = new byte[] { 0x00, 0x00 };

    public byte[] buffer;

    public TxFClose(byte[] handle)
    {
        int i;

        for (i = 0; i < hFile.length; i++)
            hFile[i] = handle[i];
    }

    public byte[] ToByteArray()
    {
        int i, j = 0;
        int Length = Func.length + hFile.length;

        buffer = new byte[Length];

        for (i = 0; i < Func.length; i++)
        {
            buffer[j] = Func[i];
            j++;
        }

        for (i = 0; i < hFile.length; i++)
        {
            buffer[j] = hFile[i];
            j++;
        }

        return (buffer);
    }

}
