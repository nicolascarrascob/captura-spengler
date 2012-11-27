package cl.vendomatica.vendroid.contadores.spengler;

public class Body {
    public byte[] data = null;
    public byte chk;
    
    public byte Checksum()
    {
        int i = 0;
        int sum = 0;

        for (i = 0; i < data.length; i++)
        {
            sum ^= data[i];

            if ((sum & 0x01) == 0x01)
                sum = (sum >> 1) | 0x80;
            else
                sum = sum >> 1;
        }

        return ((byte)sum);
    }

}
