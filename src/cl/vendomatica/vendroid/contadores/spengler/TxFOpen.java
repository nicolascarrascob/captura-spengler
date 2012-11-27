package cl.vendomatica.vendroid.contadores.spengler;

public class TxFOpen {
    public byte[] Mode = new byte[] { 0x00 };
    public byte[] Func = new byte[] { 0x3D };
    public byte[] Name;

    public byte[] buffer;
    
    public TxFOpen(String fileName)
    {
        int i;
        char c;

        Name = new byte[fileName.length() + 1];

        for (i = 0; i < fileName.length(); i++)
        {
            c = (char) fileName.substring(i, 1).charAt(0);
            Name[i] = (byte)c;
        }

        Name[i] = 0x00;
    }

    public byte[] ToByteArray()
    {
        int i, j = 0;
        int Length = Mode.length + Func.length + Name.length;

        buffer = new byte[Length];

        for (i = 0; i < Mode.length; i++)
        {
            buffer[j] = Mode[i];
            j++;
        }

        for (i = 0; i < Func.length; i++)
        {
            buffer[j] = Func[i];
            j++;
        }

        for (i = 0; i < Name.length; i++)
        {
            buffer[j] = Name[i];
            j++;
        }

        return (buffer);
    }

}
