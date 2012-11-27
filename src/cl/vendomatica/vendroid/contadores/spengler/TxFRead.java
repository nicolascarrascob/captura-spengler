package cl.vendomatica.vendroid.contadores.spengler;

public class TxFRead {
    public byte[] Func = new byte[] { 0x00, 0x3F };
    public byte[] hFile = new byte[] { 0x00, 0x00 };
    public byte[] Bufsize = new byte[] { 0x00, 0x00 };

    public byte[] buffer;

    public TxFRead(byte[] handle, byte[] cnt)
    {
        int i;

        for (i = 0; i < hFile.length; i++)
            hFile[i] = handle[i];

        for (i = 0; i < Bufsize.length; i++)
            Bufsize[i] = cnt[i];
    }

    public byte[] ToByteArray()
    {
        int i, j = 0;
        int Length = Func.length + hFile.length + Bufsize.length;

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

        for (i = 0; i < Bufsize.length; i++)
        {
            buffer[j] = Bufsize[i];
            j++;
        }

        return (buffer);
    }

}
