package cl.vendomatica.vendroid.contadores.spengler;

public class RxFRead {
    public byte[] Func = new byte[2];
    public byte[] Error = new byte[2];
    public byte[] Count = new byte[2];
    public byte[] dta;

    public RxFRead(byte[] data)
    {
        int i, j = 0;

        int cnt;

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

        for (i = 0; i < Count.length; i++)
        {
            Count[i] = data[j];
            j++;
        }

        cnt = (0x000000FF & ((int)Count[1]))  * 256 + (0x000000FF & ((int)Count[0]));
//        cnt = Count[1] * 256 + Count[0];
        dta = new byte[cnt];

        for (i = 0; i < cnt; i++)
        {
            dta[i] = data[j];
            j++;
        }
    }

}
