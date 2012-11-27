package cl.vendomatica.spengler;

public class Comm {
	
	private static byte ESC_CHAR = 0x2F;
    public static byte START_CHAR = 0x3A;
    public static byte STOP_CHAR = 0x0D;
    private final byte START_CHAR2 = 0x3B;
    private final byte START_CHAR3 = 0x23;
    private int TIMEOUT = 30;
    private byte ENDCHAR = 0x03;

}
