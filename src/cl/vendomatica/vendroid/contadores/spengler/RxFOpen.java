package cl.vendomatica.vendroid.contadores.spengler;

public class RxFOpen {
    public byte[] Func = new byte[2];
    public byte[] Error = new byte[2];
    public byte[] hFile = new byte[2];
    
    public RxFOpen(byte[] data)
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

        for (i = 0; i < hFile.length; i++)
        {
            hFile[i] = data[j];
            j++;
        }
    }

}
