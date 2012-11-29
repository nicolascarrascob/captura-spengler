package cl.vendomatica.spengler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cl.vendomatica.vendroid.contadores.spengler.Bos;

public class MainActivity extends ListActivity {
	public static final UUID SERIAL_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	String TAG = "MainActivity";
	int REQUEST_ENABLE_BT = 1;
	int REQUEST_DISCOVERABLE_BT = 1;
	final static int ACTION_ENVIAR = 1;
	final static int ACTION_OBTENER = 2;
	public static final int MESSAGE_READ = 4;
	int action;

	TextView textView;
	Button mButtonIniciarServicio;
	Button mButtonConectarse;
	// Button mButtonEnviar;
	Button mButtonObtener;
	Button mButtonDesconectar;
	// Button mButtonPresencia;

	BluetoothAdapter mBluetoothAdapter;
	BluetoothSocket mConnectedSocket;
	ConnectedThread mConnectedThread;
	Set<BluetoothDevice> pairedDevices;
	ArrayList<BluetoothDevice> bluetoothDevices;

	String ticket = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView = (TextView) findViewById(R.id.textView1);
		mButtonConectarse = (Button) findViewById(R.id.btn_conectar);
		// mButtonEnviar = (Button) findViewById(R.id.btn_enviar);
		mButtonDesconectar = (Button) findViewById(R.id.btn_desconectar);
		// mButtonPresencia = (Button) findViewById(R.id.btn_presencia);
		getBT();
		openBT();
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		pairedDevices = mBluetoothAdapter.getBondedDevices();
		bluetoothDevices = new ArrayList<BluetoothDevice>();

		ArrayList<String> s = new ArrayList<String>();
		for (BluetoothDevice bt : pairedDevices) {
			s.add(bt.getName());
			bluetoothDevices.add(bt);
		}

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, s));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		desconectar();
		new ConnectThread(bluetoothDevices.get(position)).run();
		super.onListItemClick(l, v, position, id);
	}

	private void getBT() {
		/*
		 * Obtiene el adaptador del dispositivo y verifica si existe
		 */
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "El dispositivo no tiene bluetooth",
					Toast.LENGTH_SHORT).show();
			Log.d(TAG, "El dispositivo no tiene bluetooth");
		} else {
			Toast.makeText(
					this,
					"El dispositivo si tiene bluetooth: "
							+ mBluetoothAdapter.getName() + " - "
							+ mBluetoothAdapter.getAddress(),
					Toast.LENGTH_SHORT).show();
			Log.d(TAG,
					"El dispositivo si tiene bluetooth: "
							+ mBluetoothAdapter.getName() + " - "
							+ mBluetoothAdapter.getAddress());
		}
	}

	private void openBT() {
		/*
		 * Verifica si el bluetooth esta activado, en caso contrario solicita
		 * activarlo
		 */
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
	}

	public void desconectar() {
		try {
			if (mConnectedSocket != null) {
				mConnectedSocket.close();
				mConnectedSocket = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		mConnectedThread = null;
		mButtonConectarse.setEnabled(true);
		// mButtonEnviar.setEnabled(false);
		mButtonDesconectar.setEnabled(false);
	}

	public void desconectar(View v) {
		try {
			if (mConnectedSocket != null) {
				mConnectedSocket.close();
				mConnectedSocket = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		mConnectedThread = null;
		mButtonConectarse.setEnabled(true);
		mButtonDesconectar.setEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;

		public ConnectThread(BluetoothDevice device) {
			Log.d(TAG, "Iniciando ConnectThread");
			BluetoothSocket tmp = null;
			mmDevice = device;

			// Crea y abre un socket Bluetooth basado en el perfil Serial
			try {
				tmp = device.createRfcommSocketToServiceRecord(SERIAL_UUID);
				Log.d(TAG, "Creado Socket");
			} catch (IOException e) {
			}
			mmSocket = tmp;
		}

		public void run() {
			mBluetoothAdapter.cancelDiscovery();

			try {
				// Conecta el socket con el dispositivo remoto (ITAGII)
				Log.d(TAG, "Iniciando intento de conexion");
				mmSocket.connect();
			} catch (IOException connectException) {
				try {
					mmSocket.close();
				} catch (IOException closeException) {

				}
				return;
			}
			// Maneja el retorno del socket conectado
			Log.d(TAG, "Socket conectado");
			manageConnectedSocket(mmSocket);
		}

	}

	public synchronized void manageConnectedSocket(BluetoothSocket socket) {
		// Con el socket conectado, inicia un nuevo hilo para obtener los
		// canales de comunicaci�n
		mConnectedSocket = socket;
		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();

		Bos bos = new Bos(mConnectedThread.mmInStream,
				mConnectedThread.mmOutStream);
//		Toast.makeText(this, "ejecuantodo getfile", Toast.LENGTH_SHORT).show();
		if (bos.getFile("OPNCASH.DAT")) {
			
			for (int j = 0; j < bos.file.length; j = j + 44) {
				int i = 0;
				int n = bos.file[j + i] + 256 * bos.file[j + i + 1] + 256 * 256
						* bos.file[j + i + 2] + 256 * 256 * 256
						* bos.file[j + i + 3];

				String moneda = "M" + String.valueOf(n);

				i = 4;
				n = bos.file[j + i] + 256 * bos.file[j + i + 1] + 256 * 256
						* bos.file[j + i + 2] + 256 * 256 * 256
						* bos.file[j + i + 3];

				String cantidad = String.valueOf(n);
				Log.d(TAG, moneda + " " + cantidad);
			}
		}

		mButtonConectarse.setEnabled(false);
		// mButtonEnviar.setEnabled(true);
		mButtonDesconectar.setEnabled(true);
	}

	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		public final InputStream mmInStream;
		public final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			try {
				// Obtiene los canales de comunicacion desde el socket conectado
				// al ITAGII
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			byte[] buffer = new byte[1024]; // buffer store for the stream
			int bytes; // bytes returned from read()
			// Mientras se mantiene la conexion, el canal de recepcion esta
			// siempre escuchando
			while (true) {
				try {
					// Lee el mensaje desde el canal
					bytes = mmInStream.read(buffer);
					String readMessage = new String(buffer, 0, bytes);

					Log.d(TAG, "Mensaje obtenido: " + readMessage);
					// Envia el mensaje para manejarlo en la UI
					mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
							.sendToTarget();
				} catch (IOException e) {
					break;
				}
			}
		}

		// Envia los datos (bytes) hacia el dispositivo remoto
		public void write(byte[] bytes) {
			try {
				// if(mmSocket.isConnected()){
				Log.d(TAG, "Enviando: " + bytes);
				mmOutStream.write(bytes);
				mmOutStream.flush();
				// }else{
				// Log.d(TAG, "Socket no conectado");
				// }
			} catch (IOException e) {

			}
		}
	}

	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
				ticket += readMessage;

				Log.d(TAG, "Mensaje obtenido: " + readMessage);
				textView.setText("Mensaje obtenido: " + ticket);
				break;
			}
		}
	};
}
