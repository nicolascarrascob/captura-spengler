package cl.vendomatica.vendroid.contadores.spengler;

public class RxFirst {
    public byte[] Func = new byte[2];
    public byte[] Error = new byte[2];
    public byte[] Reserved = new byte[21];
    public byte[] Attrib = new byte[1];
    public byte[] Time = new byte[2];
    public byte[] Date = new byte[2];
    public byte[] Size = new byte[4];
    public byte[] NameExt = new byte[13];
    
    public RxFirst(byte[] data)
    {
        int i, j = 0;

        for (i = 0; i < Func.length; i++)
        {
            Func[i] = data[j];
            j++;
        }

        for (i = 0; i < Error.length; i++)
        {
            Error[i] = data[j];
            j++;
        }

        for (i = 0; i < Reserved.length; i++)
        {
            Reserved[i] = data[j];
            j++;
        }

        for (i = 0; i < Attrib.length; i++)
        {
            Attrib[i] = data[j];
            j++;
        }

        for (i = 0; i < Time.length; i++)
        {
            Time[i] = data[j];
            j++;
        }

        for (i = 0; i < Date.length; i++)
        {
            Date[i] = data[j];
            j++;
        }

        for (i = 0; i < Size.length; i++)
        {
            Size[i] = data[j];
            j++;
        }

        for (i = 0; i < NameExt.length; i++)
        {
            NameExt[i] = data[j];
            j++;
        }
    }

}
