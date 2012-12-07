package cl.vendomatica.vendroid.contadores.spengler;

public class Header {
	public byte[] dst;
	public byte[] src;
	public byte tsk;
	public byte typ;
	public byte seq;
	public byte[] len;
	public byte chk;
	
	public Header()
    {
        dst = new byte[] { 0x00, 0x00, 0x00, 0x00 };
        src = new byte[] { (byte) 0xFE, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
        tsk = 0x01;
        typ = 0x01;
        seq = 0x01;
        len = new byte[] { 0x00, 0x00 };
        chk = 0x00;
    }
	
	private int Checksum(int sum, byte data)
    {                            
//        sum ^= (0x000000FF & (data));
        sum = 0x000000FF & (sum ^ data);
//        sum ^= data;
            
        if ((sum & 0x01) == 0x01)                    
//            sum = (sum >> 1) | 0x80;                
        sum = (sum >> 1) | (0x000000FF & (0x80));                
        else                    
            sum = sum >> 1;

        return (sum);
    }

    private int Checksum(int sum, byte[] data)
    {
        int i = 0;

        for (i = 0; i < data.length; i++)
            sum = Checksum(sum, data[i]);

        return (sum);
    }

    public byte Checksum()
    {
        int sum = 0;

        sum = Checksum(sum, dst);
        sum = Checksum(sum, src);
        sum = Checksum(sum, tsk);
        sum = Checksum(sum, typ);
        sum = Checksum(sum, seq);
        sum = Checksum(sum, len);

//        return (System.Convert.ToByte(sum));
        return ((byte) sum);
    }

}
