package com.nantian.sjty_Android.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.zip.DataFormatException;

import org.apache.cordova.CallbackContext;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gznt.dev.BluetoothComm;
import com.gznt.dev.BluetoothComm.DiscoverEvent;
import com.gznt.dev.CompoundDevice;
import com.gznt.pub.CharConvert;
import com.ivsign.android.IDCReader.IDCReaderSDK;
import com.nantian.sjty_Android.R;

/**
 * 蓝牙模块类
 * 
 * @author Administrator
 * 
 */
public class InputMachine implements BluetoothComm.DiscoverListener,
		BluetoothComm.LostConnectListener {

	public static BluetoothComm cBtComm = null;
	public static CompoundDevice cDev = null;
	public static InputMachine inputMachine;
	public static boolean connetion = false;
	private Context context;
	private CallbackContext callbackContext;
	public static int done = 0;
	// public static ImageButton mbtn;
	public byte byCurTarget = CompoundDevice.CONTACT_CpuCard;
	public byte[] pAdpu = new byte[1024];
	private ProgressDialog dialog;
	private String ICresult = "";
	private String IDCardResult = "";
	private boolean IsFignerOK = false;
	private final String ACTION_NAME = "sendbro";

	public InputMachine(Context context, CallbackContext callbackContext) {
		this.context = context;
		this.callbackContext = callbackContext;
		connectCbtComm(context);

		if (cDev == null) {
			cDev = new CompoundDevice(cBtComm);
		}

		cBtComm.StartDiscover("HC-06");
		// cBtComm.StartDiscover("H-C-2010-06-01");
		dialog = new ProgressDialog(context);
		dialog.setMessage("正在连接蓝牙设备，请稍后。。。");
		// dialog.setCancelable(false);
		dialog.show();
	}

	public static InputMachine newInstance(Context context,
			CallbackContext callbackContext) {
		if (inputMachine == null || cBtComm == null || cDev == null) {
			inputMachine = new InputMachine(context, callbackContext);

		}
		return inputMachine;
	}

	public void connectCbtComm(Context context) {
		if (cBtComm == null) {
			cBtComm = new BluetoothComm(context,
					BluetoothComm.SerialPortServiceClass_UUID);
			cBtComm.addDiscoverListener(this);
			System.out.println("yohoooooooooooooo");
			cBtComm.addLostConnectListener(this);
		}
	}

	public void Disconnect() {
		try {
			cBtComm.Disconnect();
			cBtComm.Close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			cDev = null;
			cBtComm = null;
			inputMachine = null;
			connetion = false;
			// Toast.makeText(context, "蓝牙设备连接已断开", Toast.LENGTH_SHORT).show();
			// mbtn.setBackgroundResource(R.drawable.home_bluetooth_off1);
		}
	}

	@Override
	public void OnLostConnect() {
		Toast.makeText(context, "蓝牙设备连接异常中断", Toast.LENGTH_SHORT).show();
		Disconnect();
		Intent mIntent = new Intent(ACTION_NAME);
		mIntent.putExtra("blue", "0");
		context.sendBroadcast(mIntent);
	}

	@Override
	public void OnDiscoverFinished(DiscoverEvent event) {

		// BluetoothDevice node = null;
		// if (!event.listDevList.isEmpty()) {
		// node = event.listDevList.get(0);
		// // Toast.makeText(context, "连接" + node.getName(),
		// Toast.LENGTH_LONG).show();
		// }
		// try {
		// cBtComm.Connect(node);
		// Toast.makeText(context, "连接" + node.getName(),
		// Toast.LENGTH_LONG).show();
		// } catch (Exception e) {
		// // Toast.makeText(context, "设备未连接" + node.getName(),
		// Toast.LENGTH_LONG).show();
		// e.printStackTrace();
		// }

		BluetoothDevice node = null;
		if (!event.listDevList.isEmpty()) {
			node = event.listDevList.get(0);
			// Toast.makeText(context, "����" + node.getName(),
			// Toast.LENGTH_LONG).show();

			try {
				boolean iscon = false;
				cBtComm.Connect(node);
				Toast.makeText(context, "连接" + node.getName(),
						Toast.LENGTH_LONG).show();
				// mbtn.setBackgroundResource(R.drawable.home_bluetooth_on);
				dialog.dismiss();
				connetion = true;
				Intent mIntent = new Intent(ACTION_NAME);
				mIntent.putExtra("blue", "1");
				context.sendBroadcast(mIntent);
				callbackContext.success("1");
			} catch (Exception e) {
				Toast.makeText(context, "设备未连接" + node.getName(),
						Toast.LENGTH_LONG).show();
				Disconnect();
				callbackContext.success("0");
				dialog.dismiss();
				e.printStackTrace();
			}

		} else {
			dialog.dismiss();
			Toast.makeText(context, "找不到附近的蓝牙设备", Toast.LENGTH_LONG).show();
			Disconnect();
		}

	}

	public String InputCard(final CallbackContext callbackContext) {
		final String res = "62145601000016122";

		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("请插入IC卡");
		builder.setTitle("银行卡信息录入");
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				inputICCard(callbackContext);

			}

		});
		builder.create().show();
		return ICresult;
	}

	public String InputID(final CallbackContext callbackContext) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("放入二代身份证到外设后，点击确定按钮进行读取");
		builder.setTitle("读取身份证信息");
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				initIDCard(callbackContext);
			}

		});
		builder.create().show();

		return IDCardResult;
	}

	public boolean InputFinger(final CallbackContext callbackContext)
			throws TimeoutException {
		// AlertDialog.Builder builder = new Builder(context);
		// builder.setMessage("请按手指");
		// builder.setTitle("读取指纹信息");
		// Dialog dialog = builder.create();
		// dialog.show();
		//
		// dialog.setOnDismissListener(new OnDismissListener() {
		//
		// @Override
		// public void onDismiss(DialogInterface dialog) {
		// // txt1.setText("1");
		// // txt2.setText("123456");
		// IsFignerOK();
		// }
		//
		// });

		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("手指放到感应区，点击确定按钮进行读取");
		builder.setTitle("读取指纹");
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				IsFignerOK(callbackContext);
			}

		});
		builder.create().show();
		return IsFignerOK;
	}

	public String InputPin(CallbackContext callbackContext) {
		Dialog dialog = new Dialog(context);
		dialog.setCancelable(false);
		dialog.show();
		String res = "";
		try {

			res = cDev.cPinPad.Input(20);
			callbackContext.success(res);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			Utils.shortToast(context, "输入超时！");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// pw.setText(res);
		dialog.dismiss();
		return res;
		// AlertDialog.Builder builder = new Builder(context);
		// builder.setMessage("请输入密码");
		// builder.setTitle("密码输入");
		// builder.setCancelable(false);
		// builder.setPositiveButton("确定", new OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		//
		// }
		// });
		// builder.show();

	}

	private void inputICCard(CallbackContext callbackContext) {
		String strATR = null;
		int nLen, nLen1, nLen2;
		String strResp = "";
		try {
			byte[] pATR = cDev.cContactCpuCard.PowerOn(byCurTarget,
					CompoundDevice.CONTACT_Baud_9600,
					CompoundDevice.CONTACT_Voltage_3_3);
			strATR = CharConvert.Bin2Hex(pATR, 0, pATR.length);
		} catch (Exception e) {
			Toast.makeText(context, "上电失败", Toast.LENGTH_LONG).show();
		}
		if (strATR != null) {
			try {
				String code1 = "00a404000e315041592e5359532e4444463031";
				String code2 = "00a4040008A000000333010101";
				String code3 = "00b2010c00";
				nLen = CharConvert.Hex2Bin(pAdpu, 0, code1);

				byte[] pResp = cDev.cContactCpuCard.ChipIO(byCurTarget, pAdpu,
						nLen);
				if (pResp != null) {
					nLen1 = CharConvert.Hex2Bin(pAdpu, 0, code2);
					pResp = cDev.cContactCpuCard.ChipIO(byCurTarget, pAdpu,
							nLen1);
					if (pResp != null) {
						nLen2 = CharConvert.Hex2Bin(pAdpu, 0, code3);
						pResp = cDev.cContactCpuCard.ChipIO(byCurTarget, pAdpu,
								nLen2);
						if (pResp != null) {
							strResp = CharConvert.Bin2Hex(pResp, 0,
									pResp.length);
							int begin = strResp.indexOf("57");
							strResp = strResp.substring(begin + 4,
									strResp.indexOf("D"));
						}
					}
					System.out.println("strRE=====" + strResp);
					ICresult = strResp;
					String ss = "{\"scantype\":\"creditCard\",\"cardnum\":\""
							+ ICresult + "\",\"cardtype\":\"1\"}";
					callbackContext.success(ss);
				}
				// etResp.setText(strResp);
			} catch (Exception e) {
				Toast.makeText(context, "指令失败", Toast.LENGTH_LONG).show();
			}

			// if (txt1.getId() == txt2.getId()) {
			// txt1.setText(strResp);
			// txt1.setError(null);
			// } else {
			// txt1.setText(strResp);
			// txt2.setText("刘芳");
			// txt1.setError(null);
			// txt2.setError(null);
			// }

		}
		ICresult = strResp;
	}

	private void initIDCard(CallbackContext callbackContext) {
		try {
			cDev.cCN2IdCard.Active();
			String title = "读取身份证信息";
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			if (!cDev.cCN2IdCard.SelectCard()) {
				Toast.makeText(context, "无卡", Toast.LENGTH_LONG).show();
				return;
			}
			if (!cDev.cCN2IdCard.ReadData()) {
				Toast.makeText(context, "读卡失败", Toast.LENGTH_LONG).show();
				return;
			}
			Toast.makeText(context, "读卡完成", Toast.LENGTH_LONG).show();

			File f = new File(FileUtil.IMAGE_PATH + "/wltlib/zp.bmp");
			if (f.exists()) {
				f.delete();
			}
			int ret = IDCReaderSDK.Init();
			if (ret == 0) {
				byte[] datawlt = new byte[1384];
				byte[] byLicData = { (byte) 0x05, (byte) 0x00, (byte) 0x01,
						(byte) 0x00, (byte) 0x5B, (byte) 0x03, (byte) 0x33,
						(byte) 0x01, (byte) 0x5A, (byte) 0xB3, (byte) 0x1E,
						(byte) 0x00 };
				for (int i = 0; i < 1295; i++) {
					datawlt[i] = cDev.cCN2IdCard.AllData[i];
				}
				int t = IDCReaderSDK.unpack(datawlt, byLicData);

			}
			Bitmap bitmap = BitmapFactory.decodeFile(FileUtil.IMAGE_PATH
					+ "/wltlib/zp.bmp");

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] ffff=baos.toByteArray();
			
			byte[] output = Base64.encode(baos.toByteArray(), Base64.NO_WRAP);
			String js_out = new String(output);

			// byte[] output = Base64.encode(cDev.cCN2IdCard.PhotoData,
			// Base64.NO_WRAP);
			String photo = "";
			String ss = "{\"photo\":\""
					+ js_out
					+ "\",\"scantype\":\"idCard\",\"name\":\""
					+ cDev.cCN2IdCard.Name
					+ "\",\"sex\":\""
					+ cDev.cCN2IdCard.Sex
					+ "\",\"nation\":\""
					+ cDev.cCN2IdCard.Nation
					+ "\",\"year\":\"%@\",\"month\":\"%@\",\"day\":\"%@\",\"address\":\""
					+ cDev.cCN2IdCard.Addr + "\",\"id\":\""
					+ cDev.cCN2IdCard.Id + "\",\"fullDeadLine\":\""
					+ cDev.cCN2IdCard.ExpiryDate + "\"}";
			IDCardResult = ss;
			callbackContext.success(IDCardResult);

		} catch (IOException e) {
		} catch (InterruptedException e) {
		} catch (TimeoutException e) {
		} catch (DataFormatException e) {
		}
	}

	private void IsFignerOK(CallbackContext callbackContext) {
		Boolean ok = false;
		try {
			try {
				cDev.cFingerPrint.Init();
				byte[] pData = cDev.cFingerPrint.Read(5);
				StringBuilder sb = new StringBuilder();
				for (byte b : pData) {
					sb.append(b).append(",");
				}
				String l = sb.toString();
				callbackContext.success(l);
				ok = true;

			} catch (TimeoutException e) {
				Utils.shortToast(context, "输入超时！");
				e.printStackTrace();
			}
			if (ok) {
				Log.i("ABC", "ok");
				// txt1.setText("1");
				// txt2.setText("1234567");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		IsFignerOK = ok;
	}
}
