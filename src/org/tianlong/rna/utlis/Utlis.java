package org.tianlong.rna.utlis;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.tianlong.rna.pojo.Experiment;
import org.tianlong.rna.pojo.Step;

import android.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

@SuppressLint({ "SimpleDateFormat", "HandlerLeak", "DefaultLocale" })
@SuppressWarnings("deprecation")
public class Utlis {

	public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	public static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static SimpleDateFormat systemFormat = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");
	private static List<Byte> bytes = new ArrayList<Byte>();
	private static List<String> strings = new ArrayList<String>();

	public static String getSystemTime() {
		Date curDate = new Date(System.currentTimeMillis());
		String bdate = systemFormat.format(curDate);
		return bdate;
	}

	// 回复检查
	public static boolean checkReceive(String str, int num) {
		boolean flag = false;
		strings.removeAll(strings);
		for (int i = 0; i < str.length(); i += 3) {
			strings.add(str.substring(i, i + 2));
		}
		String allLength = Integer.toHexString(strings.size());
		String length = Integer.toHexString(strings.size() - 9);
		if (allLength.length() == 1) {
			allLength = "0" + allLength;
		}
		if (length.length() == 1) {
			length = "0" + length;
		}
		if (strings.get(0).equals("ff") && strings.get(1).equals("ff")
				&& strings.get(strings.size() - 1).equals("fe")) {
			try {
				if (num == 0) {
					if (strings.get(2).equals(allLength)
							&& strings.get(3).equals("d1")
							&& strings.get(4).equals(length)
							&& strings.get(5).equals("0f")
							&& strings.get(6).equals("00")) {
						if (strings.get(7).equals("01")) {
							flag = true;
						}
					}
				} else {
					if (strings.get(2).equals(allLength)
							&& strings.get(3).equals("d1")
							&& strings.get(4).equals(length)
							&& strings.get(5).equals("0f")
							&& strings.get(6).equals("01")) {
						if (strings.get(7).equals("01")) {
							flag = true;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	// 紫光灯开启
	public static byte[] ultravioletOpenMessage() {
		byte[] byteList = new byte[9];
		byteList[0] = (byte) Integer.parseInt("FF", 16);
		byteList[1] = (byte) Integer.parseInt("FF", 16);
		byteList[2] = (byte) Integer.parseInt("09", 16);
		byteList[3] = (byte) Integer.parseInt("8A", 16);
		byteList[4] = (byte) Integer.parseInt("00", 16);
		byteList[5] = (byte) Integer.parseInt("07", 16);
		byteList[6] = (byte) Integer.parseInt("01", 16);
		byteList[7] = (byte) Integer.parseInt("99", 16);
		byteList[8] = (byte) Integer.parseInt("FE", 16);
		return byteList;
	}

	// 紫光灯关闭
	public static byte[] ultravioletCloseMessage() {
		byte[] byteList = new byte[9];
		byteList[0] = (byte) Integer.parseInt("FF", 16);
		byteList[1] = (byte) Integer.parseInt("FF", 16);
		byteList[2] = (byte) Integer.parseInt("09", 16);
		byteList[3] = (byte) Integer.parseInt("8A", 16);
		byteList[4] = (byte) Integer.parseInt("00", 16);
		byteList[5] = (byte) Integer.parseInt("07", 16);
		byteList[6] = (byte) Integer.parseInt("00", 16);
		byteList[7] = (byte) Integer.parseInt("98", 16);
		byteList[8] = (byte) Integer.parseInt("FE", 16);
		return byteList;
	}

	// 发送开始命令
	public static byte[] sendRunMessage(int num) {
	
		bytes.removeAll(bytes);
		byte[] byteList = new byte[9];
		byteList[0] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[0]);
		byteList[1] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[1]);
		byteList[2] = (byte) Integer.parseInt("09", 16);
		bytes.add(byteList[2]);
		byteList[3] = (byte) Integer.parseInt("8A", 16);
		bytes.add(byteList[3]);
		byteList[4] = (byte) Integer.parseInt("00", 16);
		bytes.add(byteList[4]);
		byteList[5] = (byte) Integer.parseInt("0A", 16);
		bytes.add(byteList[5]);
		byteList[6] = (byte) Integer.parseInt(Integer.toHexString(num + 1), 16);
		bytes.add(byteList[6]);
		byteList[7] = getCheckCode(bytes);
		byteList[8] = (byte) Integer.parseInt("FE", 16);
		return byteList;
	}

	// 发送停止命令
	public static byte[] sendStopMessage() {
		byte[] byteList = new byte[9];
		byteList[0] = (byte) Integer.parseInt("FF", 16);
		byteList[1] = (byte) Integer.parseInt("FF", 16);
		byteList[2] = (byte) Integer.parseInt("09", 16);
		byteList[3] = (byte) Integer.parseInt("8A", 16);
		byteList[4] = (byte) Integer.parseInt("00", 16);
		byteList[5] = (byte) Integer.parseInt("0D", 16);
		byteList[6] = (byte) Integer.parseInt("FF", 16);
		byteList[7] = (byte) Integer.parseInt("9D", 16);
		byteList[8] = (byte) Integer.parseInt("FE", 16);
		return byteList;
	}

	// 发送暂停命令
	public static byte[] sendPauseMessage() {
		byte[] byteList = new byte[9];
		byteList[0] = (byte) Integer.parseInt("FF", 16);
		byteList[1] = (byte) Integer.parseInt("FF", 16);
		byteList[2] = (byte) Integer.parseInt("09", 16);
		byteList[3] = (byte) Integer.parseInt("8A", 16);
		byteList[4] = (byte) Integer.parseInt("00", 16);
		byteList[5] = (byte) Integer.parseInt("0B", 16);
		byteList[6] = (byte) Integer.parseInt("FF", 16);
		byteList[7] = (byte) Integer.parseInt("9B", 16);
		byteList[8] = (byte) Integer.parseInt("FE", 16);
		return byteList;
	}

	// 发送继续命令
	public static byte[] sendContinueMessage() {
		byte[] byteList = new byte[9];
		byteList[0] = (byte) Integer.parseInt("FF", 16);
		byteList[1] = (byte) Integer.parseInt("FF", 16);
		byteList[2] = (byte) Integer.parseInt("09", 16);
		byteList[3] = (byte) Integer.parseInt("8A", 16);
		byteList[4] = (byte) Integer.parseInt("00", 16);
		byteList[5] = (byte) Integer.parseInt("0C", 16);
		byteList[6] = (byte) Integer.parseInt("FF", 16);
		byteList[7] = (byte) Integer.parseInt("9C", 16);
		byteList[8] = (byte) Integer.parseInt("FE", 16);
		return byteList;
	}

	// 系统复位命令
	public static byte[] sendSystemResetMessage() {
		byte[] byteList = new byte[9];
		byteList[0] = (byte) Integer.parseInt("FF", 16);
		byteList[1] = (byte) Integer.parseInt("FF", 16);
		byteList[2] = (byte) Integer.parseInt("09", 16);
		byteList[3] = (byte) Integer.parseInt("8A", 16);
		byteList[4] = (byte) Integer.parseInt("00", 16);
		byteList[5] = (byte) Integer.parseInt("00", 16);
		byteList[6] = (byte) Integer.parseInt("FF", 16);
		byteList[7] = (byte) Integer.parseInt("90", 16);
		byteList[8] = (byte) Integer.parseInt("FE", 16);
		return byteList;
	}

	// 发送通量设置命令
	public static byte[] sendSettingflux(String fluxNum) {
		bytes.removeAll(bytes);
		byte[] byteList = new byte[9];
		byteList[0] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[0]);
		byteList[1] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[1]);
		byteList[2] = (byte) Integer.parseInt("09", 16);
		bytes.add(byteList[2]);
		byteList[3] = (byte) Integer.parseInt("8A", 16);
		bytes.add(byteList[3]);
		byteList[4] = (byte) Integer.parseInt("00", 16);
		bytes.add(byteList[4]);
		byteList[5] = (byte) Integer.parseInt("12", 16);
		bytes.add(byteList[5]);
		switch (fluxNum) {
		case "15":
			byteList[6] = (byte) Integer.parseInt("0F", 16);
			break;
		case "32":
			byteList[6] = (byte) Integer.parseInt("20", 16);
			break;
		case "48":
			byteList[6] = (byte) Integer.parseInt("30", 16);
			break;
		}
		bytes.add(byteList[6]);
		byteList[7] = getCheckCode(bytes);
		byteList[8] = (byte) Integer.parseInt("FE", 16);
		return byteList;
	}

	// 移动电机命令
	public static byte[] moveMotorMessage(int num, float info) {
		bytes.removeAll(bytes);
		byte[] byteList = new byte[11];
		byteList[0] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[0]);
		byteList[1] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[1]);
		byteList[2] = (byte) (byteList.length);
		bytes.add(byteList[2]);
		byteList[3] = (byte) Integer.parseInt("8A", 16);
		bytes.add(byteList[3]);
		byteList[4] = (byte) Integer.parseInt("02", 16);
		bytes.add(byteList[4]);
		byteList[5] = (byte) Integer.parseInt("09", 16);
		bytes.add(byteList[5]);
		switch (num) {
		case 0:
			byteList[6] = (byte) Integer.parseInt("00", 16);
			break;
		case 1:
			byteList[6] = (byte) Integer.parseInt("01", 16);
			break;
		case 2:
			byteList[6] = (byte) Integer.parseInt("02", 16);
			break;
		default:
			break;
		}
		bytes.add(byteList[6]);
		//设置 数值 需要*100
		String str = Integer.toHexString((int) (info * 100));
		if (str.length() == 1) {
			byteList[7] = (byte) Integer.parseInt("00", 16);
			byteList[8] = (byte) Integer.parseInt("0" + str, 16);
		}
		if (str.length() == 2) {
			byteList[7] = (byte) Integer.parseInt("00", 16);
			byteList[8] = (byte) Integer.parseInt(str, 16);
		} else if (str.length() == 3) {
			byteList[7] = (byte) Integer.parseInt(str.substring(0, 1), 16);
			byteList[8] = (byte) Integer.parseInt(str.substring(1, 3), 16);
		} else {
			byteList[7] = (byte) Integer.parseInt(str.substring(0, 2), 16);
			byteList[8] = (byte) Integer.parseInt(str.substring(2, 4), 16);
		}
		bytes.add(byteList[7]);
		bytes.add(byteList[8]);
		byteList[9] = getCheckCode(bytes);
		byteList[10] = (byte) Integer.parseInt("FE", 16);
		return byteList;
	}

	//孔间距设置命令
	public static byte[] saveHoldSpaceMessage(int info) {
		bytes.removeAll(bytes);
		byte[] byteList = new byte[11];
		byteList[0] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[0]);
		byteList[1] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[1]);
		byteList[2] = (byte) (byteList.length);
		bytes.add(byteList[2]);
		byteList[3] = (byte) Integer.parseInt("8A", 16);
		bytes.add(byteList[3]);
		byteList[4] = (byte) Integer.parseInt("02", 16);
		bytes.add(byteList[4]);
		byteList[5] = (byte) Integer.parseInt("09", 16);
		bytes.add(byteList[5]);
		byteList[6] = (byte) Integer.parseInt("03", 16);
		bytes.add(byteList[6]);
		String str = Integer.toHexString(info);
		if (str.length() == 1) {
			byteList[7] = (byte) Integer.parseInt("00", 16);
			byteList[8] = (byte) Integer.parseInt("0" + str, 16);
		}
		if (str.length() == 2) {
			byteList[7] = (byte) Integer.parseInt("00", 16);
			byteList[8] = (byte) Integer.parseInt(str, 16);
		} else if (str.length() == 3) {
			byteList[7] = (byte) Integer.parseInt(str.substring(0, 1), 16);
			byteList[8] = (byte) Integer.parseInt(str.substring(1, 3), 16);
		} else {
			byteList[7] = (byte) Integer.parseInt(str.substring(0, 2), 16);
			byteList[8] = (byte) Integer.parseInt(str.substring(2, 4), 16);
		}
		bytes.add(byteList[7]);
		bytes.add(byteList[8]);
		byteList[9] = getCheckCode(bytes);
		byteList[10] = (byte) Integer.parseInt("FE", 16);
		return byteList;
	}

	// 电机复位命令
	public static byte[] resetMotorMessage(int num) {
		bytes.removeAll(bytes);
		byte[] byteList = new byte[9];
		byteList[0] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[0]);
		byteList[1] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[1]);
		byteList[2] = (byte) (byteList.length);
		bytes.add(byteList[2]);
		byteList[3] = (byte) Integer.parseInt("8A", 16);
		bytes.add(byteList[3]);
		byteList[4] = (byte) Integer.parseInt("00", 16);
		bytes.add(byteList[4]);
		byteList[5] = (byte) Integer.parseInt("01", 16);
		bytes.add(byteList[5]);
		switch (num) {
		case 0:
			byteList[6] = (byte) Integer.parseInt("00", 16);
			break;
		case 1:
			byteList[6] = (byte) Integer.parseInt("01", 16);
			break;
		case 2:
			byteList[6] = (byte) Integer.parseInt("02", 16);
			break;
		default:
			break;
		}
		bytes.add(byteList[6]);
		byteList[7] = getCheckCode(bytes);
		byteList[8] = (byte) Integer.parseInt("FE", 16);
		return byteList;
	}

	// 发送检测命令
	public static byte[] sendDetectionMessage(int num) {
		bytes.removeAll(bytes);
		byte[] byteList = new byte[9];
		byteList[0] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[0]);
		byteList[1] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[1]);
		byteList[2] = (byte) (byteList.length);
		bytes.add(byteList[2]);
		if (num > 2) {
			byteList[3] = (byte) Integer.parseInt("8A", 16);
		} else {
			byteList[3] = (byte) Integer.parseInt("0A", 16);
		}
		bytes.add(byteList[3]);
		byteList[4] = (byte) Integer.parseInt("00", 16);
		bytes.add(byteList[4]);
		switch (num) {
		case 1:
			byteList[5] = (byte) Integer.parseInt("01", 16);
			byteList[6] = (byte) Integer.parseInt("FF", 16);
			break;
		case 2:
			byteList[5] = (byte) Integer.parseInt("05", 16);
			byteList[6] = (byte) Integer.parseInt("FF", 16);
			break;
		case 3:
			byteList[5] = (byte) Integer.parseInt("10", 16);
			byteList[6] = (byte) Integer.parseInt("FF", 16);
			break;
		case 4:
			byteList[5] = (byte) Integer.parseInt("01", 16);
			byteList[6] = (byte) Integer.parseInt("00", 16);
			break;
		case 5:
			byteList[5] = (byte) Integer.parseInt("01", 16);
			byteList[6] = (byte) Integer.parseInt("01", 16);
			break;
		case 6:
			byteList[5] = (byte) Integer.parseInt("01", 16);
			byteList[6] = (byte) Integer.parseInt("02", 16);
			break;
		default:
			break;
		}
		bytes.add(byteList[5]);
		bytes.add(byteList[6]);
		byteList[7] = getCheckCode(bytes);
		byteList[8] = (byte) Integer.parseInt("FE", 16);
		return byteList;
	}

	// 所有查询命令
	// TODO
	public static byte[] getseleteMessage(int num) {
		bytes.removeAll(bytes);
		byte[] byteList = new byte[9];
		byteList[0] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[0]);
		byteList[1] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[1]);
		byteList[2] = (byte) (byteList.length);
		bytes.add(byteList[2]);
		byteList[3] = (byte) Integer.parseInt("0A", 16);
		bytes.add(byteList[3]);
		byteList[4] = (byte) Integer.parseInt("00", 16);
		bytes.add(byteList[4]);
		switch (num) {
		// 编号及版本号查询
		case 0:
			byteList[5] = (byte) Integer.parseInt("00", 16);
			break;
		// 供电查询
		case 1:
			byteList[5] = (byte) Integer.parseInt("01", 16);
			break;
		// 电机状态查询
		case 2:
			byteList[5] = (byte) Integer.parseInt("02", 16);
			break;
		// 单条温度查询
		case 3:
			byteList[5] = (byte) Integer.parseInt("03", 16);
			break;
		// 开关查询
		case 4:
			byteList[5] = (byte) Integer.parseInt("04", 16);
			break;
		// 所有温度查询
		case 5:
			byteList[5] = (byte) Integer.parseInt("05", 16);
			break;
		// 查询步骤及运行状态
		case 6:
			byteList[5] = (byte) Integer.parseInt("06", 16);
			break;
		// 通量查询
		case 7:
			byteList[5] = (byte) Integer.parseInt("07", 16);
			break;
		// 紫光灯查询
		case 8:
			byteList[5] = (byte) Integer.parseInt("08", 16);
			break;
		// 电机查询
		case 9:
			byteList[5] = (byte) Integer.parseInt("09", 16);
			break;
		// 查询下位机所有试验
		case 10:
			byteList[5] = (byte) Integer.parseInt("0A", 16);
		default:
			break;
		}
		bytes.add(byteList[5]);
		byteList[6] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[6]);
		byteList[7] = getCheckCode(bytes);
		byteList[8] = (byte) Integer.parseInt("FE", 16);
		return byteList;
	}

	// 发送试验ID
	public static byte[] sendExperimentId(int id) {
		bytes.removeAll(bytes);
		byte[] byteList = new byte[9];
		byteList[0] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[0]);
		byteList[1] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[1]);
		byteList[2] = (byte) (byteList.length);
		bytes.add(byteList[2]);
		byteList[3] = (byte) Integer.parseInt("0A", 16);
		bytes.add(byteList[3]);
		byteList[4] = (byte) Integer.parseInt("00", 16);
		bytes.add(byteList[4]);
		byteList[5] = (byte) Integer.parseInt("0B", 16);
		bytes.add(byteList[5]);
		byteList[6] = (byte) Integer.parseInt(Integer.toHexString(id), 16);
		bytes.add(byteList[6]);
		byteList[7] = getCheckCode(bytes);
		byteList[8] = (byte) Integer.parseInt("FE", 16);
		return byteList;
	}

	// 得到实验内容byte列表
	public static byte[] getbyteList(String str, int num) {
		bytes.removeAll(bytes);
		byte[] byteList = null;
		try {
			byteList = new byte[str.getBytes("GBK").length + 9];
			byteList[0] = (byte) Integer.parseInt("FF", 16);
			bytes.add(byteList[0]);
			byteList[1] = (byte) Integer.parseInt("FF", 16);
			bytes.add(byteList[1]);
			byteList[2] = (byte) (byteList.length);
			bytes.add(byteList[2]);
			byteList[3] = (byte) Integer.parseInt("8A", 16);
			bytes.add(byteList[3]);
			byteList[4] = (byte) (str.getBytes("GBK").length);
			bytes.add(byteList[4]);
			byteList[5] = (byte) Integer.parseInt("0F", 16);
			bytes.add(byteList[5]);
			if (num == 0) {
				byteList[6] = (byte) Integer.parseInt("00", 16);
			} else {
				byteList[6] = (byte) Integer.parseInt("01", 16);
			}
			bytes.add(byteList[6]);
			byte[] strbyte = str.getBytes("GBK");
			for (int j = 0; j < strbyte.length; j++) {
				byteList[j + 7] = strbyte[j];
				bytes.add(strbyte[j]);
			}
			byteList[str.getBytes("GBK").length + 7] = getCheckCode(bytes);
			byteList[str.getBytes("GBK").length + 8] = (byte) Integer.parseInt(
					"FE", 16);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return byteList;
	}

	// 效验码计算
	public static byte getCheckCode(List<Byte> bytes) {
		int num = 0;
		for (int i = 0; i < bytes.size(); i++) {
			num += bytes.get(i);
		}
		num = num & 255;
		return (byte) num;
	}

	// 转换位置
	public static String changeInfo(String info) {
		String changeStr = null;
		changeStr = info.substring(2, 4) + info.substring(0, 2);
		return changeStr;
	}

	// 得到发送文件信息的发送命令
	public static byte[] getMessageByte(String info, String stepInfo, int num) {
		bytes.removeAll(bytes);
		byte[] byteList = null;
		int infoNum = info.length() / 2;
		try {
			byteList = new byte[infoNum + stepInfo.getBytes("GBK").length + 9];
			byteList[0] = (byte) Integer.parseInt("FF", 16);
			bytes.add(byteList[0]);
			byteList[1] = (byte) Integer.parseInt("FF", 16);
			bytes.add(byteList[1]);
			byteList[2] = (byte) (byteList.length);
			bytes.add(byteList[2]);
			byteList[3] = (byte) Integer.parseInt("8A", 16);
			bytes.add(byteList[3]);
			byteList[4] = (byte) (infoNum + stepInfo.getBytes("GBK").length);
			bytes.add(byteList[4]);
			byteList[5] = (byte) Integer.parseInt("0F", 16);
			bytes.add(byteList[5]);
			if (num == 0) {
				byteList[6] = (byte) Integer.parseInt("00", 16);
			} else {
				byteList[6] = (byte) Integer.parseInt("01", 16);
			}
			bytes.add(byteList[6]);
			int endNum = 0;
			for (int i = 0; i < infoNum; i++) {
				endNum += 2;
				byteList[i + 7] = (byte) Integer.parseInt(
						info.substring(endNum - 2, endNum), 16);
				bytes.add(byteList[i + 7]);
			}
			byte[] strbyte = stepInfo.getBytes("GBK");
			for (int j = 0; j < strbyte.length; j++) {
				byteList[j + 7 + infoNum] = strbyte[j];
				bytes.add(strbyte[j]);
			}
			byteList[infoNum + stepInfo.getBytes("GBK").length + 7] = getCheckCode(bytes);
			byteList[infoNum + stepInfo.getBytes("GBK").length + 8] = (byte) Integer
					.parseInt("FE", 16);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return byteList;
	}

	// 发送试验ID
	public static byte[] sendDeleteExperiment(int id) {
		bytes.removeAll(bytes);
		byte[] byteList = new byte[9];
		byteList[0] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[0]);
		byteList[1] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[1]);
		byteList[2] = (byte) (byteList.length);
		bytes.add(byteList[2]);
		byteList[3] = (byte) Integer.parseInt("8A", 16);
		bytes.add(byteList[3]);
		byteList[4] = (byte) Integer.parseInt("00", 16);
		bytes.add(byteList[4]);
		byteList[5] = (byte) Integer.parseInt("11", 16);
		bytes.add(byteList[5]);
		byteList[6] = (byte) Integer.parseInt(Integer.toHexString(id), 16);
		bytes.add(byteList[6]);
		byteList[7] = getCheckCode(bytes);
		byteList[8] = (byte) Integer.parseInt("FE", 16);
		return byteList;
	}

	// 补位
	public static String addByte(int info) {
		String message = null;
		if (Integer.toHexString(info).length() == 1) {
			message = "000" + Integer.toHexString(info);
		} else if (Integer.toHexString(info).length() == 2) {
			message = "00" + Integer.toHexString(info);
		} else if (Integer.toHexString(info).length() == 3) {
			message = "0" + Integer.toHexString(info);
		}
		return message;
	}

	// 得到温度和温度开关
	public static String getTempAndSwitch(int temp, int sSwitch) {
		String info = null;
		int f = 0;
		if (sSwitch != 0) {
			f = 1;
		} else {
			f = 0;
		}
		f = ((f << 7) & 0x80) | (temp & 0x7F);

		if (Integer.toHexString(f).length() == 1) {
			info = "0" + Integer.toHexString(f);
		} else {
			info = Integer.toHexString(f);
		}
		return info;
	}

	// 得到时间信息
	public static int getTimeinfo(String time) {
		int info = 0;
		try {
			int hour = timeFormat.parse(time).getHours();
			int mins = timeFormat.parse(time).getMinutes();
			int sec = timeFormat.parse(time).getSeconds();
			info = ((hour << 12) & 0xF000) | ((mins << 6) & 0x0FC0)
					| (sec & 0x003F);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * 得到模板
	 */
	public static List<String> getTemplate(Context context) {
		List<String> list = new ArrayList<String>();
		String string[] = context.getResources().getStringArray(
				org.tianlong.rna.activity.R.array.Templet);
		for (int i = 0; i < string.length; i++) {
			list.add(string[i]);
		}
		return list;
	}

	// 设置语言
	public static void setLanguage(Activity activity, int lag) {
		String languageToLocal = "";
		switch (lag) {
		case 0:
			languageToLocal = "zh";
			break;
		case 1:
			languageToLocal = "kr";
			break;
		case 2:
			languageToLocal = "en";
			break;
		default:
			break;
		}
		Locale locale = new Locale(languageToLocal);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		activity.getBaseContext().getResources()
				.updateConfiguration(config, null);
	}

	/**
	 * 
	 * Title: getReceive Description: 得到所有回复信息
	 * 
	 * @param receive
	 * @return
	 */
	// TODO
	public static List<String> getReceive(byte[] receive) {
		List<String> strings = new ArrayList<String>();
		String info = CHexConver.bytes2HexStr(receive, receive.length);
		while (info.indexOf("ff ff ") == 0) {
			int length = Integer.parseInt(info.substring(6, 8), 16); // 得到命令信息长度
			strings.add(info.substring(0, length * 3)); // 得到所有命令
			info = info.substring(length * 3, info.length()); // 将剩余String赋给info
			if (info.equals("ff ff 0a 51 01 0a ff 01 64 fe ")) {
				break;
			}
		}
		return strings;
	}

	// 得到下位机实验列表
	@SuppressLint("DefaultLocale")
	public static List<Map<String, Object>> getExperimentList(
			List<String> receive) {
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < receive.size(); i++) {
			String infos = receive.get(i).replace(" ", "").substring(14, receive.get(i).replace(" ", "").length() - 4); 
			infos = CHexConver.hexStr2Str(infos.toUpperCase());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", Integer.valueOf(infos.substring(0, 1)));
			map.put("info", infos.substring(2, infos.indexOf(",", 2)));
			map.put("user", infos.substring(infos.indexOf(",", 2) + 1,
					infos.length() - 1));
			maps.add(map);
		}
		return maps;
	}

	// 得到下位机实验信息
	public static List<String> getExperimentInfoList(List<String> receive) {
		List<String> strings = new ArrayList<String>();
		for (int i = 0; i < receive.size(); i++) {
			String infos = null;
			if (i + 1 == receive.size()) {
				infos = receive.get(i).replace(" ", "");
			} else {
				infos = receive
						.get(i)
						.replace(" ", "")
						.substring(14,
								receive.get(i).replace(" ", "").length() - 4);
			}
			if (i < 3 || (i > receive.size() - 3 && (i + 1) != receive.size())) {
				infos = CHexConver.hexStr2Str(infos.toUpperCase());
			}
			strings.add(infos);
		}
		return strings;
	}

	// 得到上位机实验信息
	public static List<String> getPadExperimentInfoList(Experiment experiment,
			List<Step> steps, String userName) {
		List<String> sendInfo = new ArrayList<String>();
		sendInfo.add("#FILE:" + experiment.getEname() + "\r\n");
		String dates = dateFormat.format(new Date());
		sendInfo.add("#DATE:" + dates + "\r\n");
		sendInfo.add("#INFORMATION:Examples Experiment\r\n");
		for (int i = 0; i < steps.size(); i++) {
			if ((i + 1 == steps.size())) {
				sendInfo.add(Utlis.changeInfo(Utlis.addByte(Utlis
						.getTimeinfo(steps.get(i).getSwait())))
						+ Utlis.changeInfo(Integer
								.toHexString(((steps.get(i).getShole() << 13) & 0xE000)
										| (((steps.get(i).getSspeed() - 1) << 10) & 0x1C00)
										| (steps.get(i).getSvol() & 0x03FF)))
						+ Utlis.changeInfo(Utlis.addByte(Utlis
								.getTimeinfo("00:00:00")))
						+ Utlis.changeInfo(Utlis.addByte(Utlis
								.getTimeinfo(steps.get(i).getSblend())))
						+ Utlis.getTempAndSwitch(steps.get(i).getStemp(), steps
								.get(i).getSswitch()));
			} else {
				sendInfo.add(Utlis.changeInfo(Utlis.addByte(Utlis
						.getTimeinfo(steps.get(i).getSwait())))
						+ Utlis.changeInfo(Integer
								.toHexString(((steps.get(i).getShole() << 13) & 0xE000)
										| (((steps.get(i).getSspeed() - 1) << 10) & 0x1C00)
										| (steps.get(i).getSvol() & 0x03FF)))
						+ Utlis.changeInfo(Utlis.addByte(Utlis
								.getTimeinfo(steps.get(i).getSmagnet())))
						+ Utlis.changeInfo(Utlis.addByte(Utlis
								.getTimeinfo(steps.get(i).getSblend())))
						+ Utlis.getTempAndSwitch(steps.get(i).getStemp(), steps
								.get(i).getSswitch()));
			}
			sendInfo.add(steps.get(i).getSname() + "\r\n");
		}
		if (userName.equals("")) {
			sendInfo.add("#END_FILE\r\n");
		} else {
			sendInfo.add("#END_FILE" + userName + "\r\n");
		}
		return sendInfo;
	}

	// 从得到的实验命令中获取Step信息
	public static Step getStepFromInfo(String info, int E_id) {
		Step step = new Step();
		int h = 0, m = 0, s = 0;
		String time = null;
		step.setSname(CHexConver.hexStr2Str(info.substring(18, info.length())
				.toUpperCase()));
		step.setE_id(E_id);
		// 孔位
		step.setShole(Integer.parseInt(info.substring(6, 7), 16) >> 1);
		// 混合速度
		step.setSspeed((Integer.parseInt(info.substring(6, 8), 16) & 0x1c) >> 2);
		// 体积
		step.setSvol((Integer.parseInt(info.substring(6, 8), 16) & 0x03) * 256
				+ Integer.parseInt(info.substring(4, 6), 16));
		// 等待时间
		h = Integer.parseInt(info.substring(2, 3), 16);
		m = Integer.parseInt(info.substring(3, 4), 16) * 4
				+ (Integer.parseInt(info.substring(0, 1), 16) >> 2);
		s = Integer.parseInt(info.substring(0, 2), 16) & 0x3F;
		if (h < 10) {
			time = "0" + h + ":";
		} else {
			time = h + ":";
		}
		if (m < 10) {
			time = time + "0" + m + ":";
		} else {
			time = time + m + ":";
		}
		if (s < 10) {
			time = time + "0" + s;
		} else {
			time = time + s;
		}
		step.setSwait(time);

		// 混合时间
		h = Integer.parseInt(info.substring(14, 15), 16);
		m = Integer.parseInt(info.substring(15, 16), 16) * 4
				+ (Integer.parseInt(info.substring(12, 13), 16) >> 2);
		s = Integer.parseInt(info.substring(12, 14), 16) & 0x3F;
		if (h < 10) {
			time = "0" + h + ":";
		} else {
			time = h + ":";
		}
		if (m < 10) {
			time = time + "0" + m + ":";
		} else {
			time = time + m + ":";
		}
		if (s < 10) {
			time = time + "0" + s;
		} else {
			time = time + s;
		}
		step.setSblend(time);

		// 磁吸时间
		h = Integer.parseInt(info.substring(10, 11), 16);
		m = Integer.parseInt(info.substring(11, 12), 16) * 4
				+ (Integer.parseInt(info.substring(8, 9), 16) >> 2);
		s = Integer.parseInt(info.substring(8, 10), 16) & 0x3F;
		if (h < 10) {
			time = "0" + h + ":";
		} else {
			time = h + ":";
		}
		if (m < 10) {
			time = time + "0" + m + ":";
		} else {
			time = time + m + ":";
		}
		if (s < 10) {
			time = time + "0" + s;
		} else {
			time = time + s;
		}
		step.setSmagnet(time);

		// 温度开关
		if (Integer.parseInt(info.substring(16, 18), 16) >> 7 == 0) {
			step.setSswitch(0);
		} else {
			if (step.getShole() != 1) {
				step.setSswitch(2);
			} else {
				step.setSswitch(1);
			}
		}
		// 温度
		step.setStemp(Integer.parseInt(info.substring(16, 18), 16) & 0x7F);
		return step;
	}

	public static List<Map<String, Object>> getApkByPath(String path) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 根据path创建File对象
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		// 将此目录转变成File数组
		File[] subFiles = file.listFiles();
		// 遍历此数组
		for (int i = 0; i < subFiles.length; i++) {
			// 取出此数组中的每个元素
			File sFile = subFiles[i];
			// 判断是否为文件夹
			if (isBin(sFile)) {
				// 创建Map集合来存放文件信息
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("title", sFile.getName());
				map.put("path", sFile.getAbsolutePath());
				list.add(map);
			}
		}
		return list;
	}

	// 判断是否为升级文档
	private static boolean isBin(File file) {
		String picPath = file.getName();
		if ("NP968IV.bin".compareToIgnoreCase(picPath) == 0) {
			return true;
		} else {
			return false;
		}
	}

	// 发送升级文件
	public static byte[] sendUpdateFile(ArrayList<Byte> bList, int start,
			int end) {
		byte[] sendBytes = new byte[end - start + 1];
		int id = 0;
		for (int i = start; i <= end; i++) {
			sendBytes[id] = bList.get(i);
			id++;
		}
		bytes.removeAll(bytes);
		byte[] byteList = new byte[sendBytes.length + 9];
		byteList[0] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[0]);
		byteList[1] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[1]);
		byteList[2] = (byte) (byteList.length);
		bytes.add(byteList[2]);
		byteList[3] = (byte) Integer.parseInt("8A", 16);
		bytes.add(byteList[3]);
		byteList[4] = (byte) (sendBytes.length);
		bytes.add(byteList[4]);
		byteList[5] = (byte) Integer.parseInt("0F", 16);
		bytes.add(byteList[5]);
		byteList[6] = (byte) Integer.parseInt("02", 16);
		bytes.add(byteList[6]);
		for (int i = 0; i < sendBytes.length; i++) {
			byteList[i + 7] = sendBytes[i];
			bytes.add(byteList[i + 7]);
		}
		byteList[byteList.length - 2] = getCheckCode(bytes);
		byteList[byteList.length - 1] = (byte) Integer.parseInt("FE", 16);
		return byteList;
	}

	// 升级文件发送完成命令
	public static byte[] sendUpdateFinsh() {
		bytes.removeAll(bytes);
		byte[] byteList = new byte[9];
		byteList[0] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[0]);
		byteList[1] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[1]);
		byteList[2] = (byte) Integer.parseInt("09", 16);
		bytes.add(byteList[2]);
		byteList[3] = (byte) Integer.parseInt("8A", 16);
		bytes.add(byteList[3]);
		byteList[4] = (byte) Integer.parseInt("00", 16);
		bytes.add(byteList[4]);
		byteList[5] = (byte) Integer.parseInt("0F", 16);
		bytes.add(byteList[5]);
		byteList[6] = (byte) Integer.parseInt("03", 16);
		bytes.add(byteList[6]);
		byteList[7] = getCheckCode(bytes);
		byteList[8] = (byte) Integer.parseInt("FE", 16);
		return byteList;
	}

	// 得到下位机运行日志列表
	@SuppressLint("DefaultLocale")
	public static List<Map<String, Object>> getRunFileList(List<String> receive) {
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < (receive.size() - 1); i++) {
			String infos = receive
					.get(i)
					.replace(" ", "")
					.substring(14, receive.get(i).replace(" ", "").length() - 4);
			infos = CHexConver.hexStr2Str(infos.toUpperCase());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("num", Integer.valueOf(infos.substring(infos.length() - 2, infos.length() - 1)));
			map.put("info", infos.substring(0, infos.indexOf(",", 0)));
			map.put("id", Integer.valueOf(infos.substring(infos.length() - 5, infos.length() - 3)));
			maps.add(map);
		}
		return maps;
	}

	// 查询下位机运行日志列表
	public static byte[] sendSelectRunfileList() {
		bytes.removeAll(bytes);
		byte[] byteList = new byte[9];
		byteList[0] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[0]);
		byteList[1] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[1]);
		byteList[2] = (byte) Integer.parseInt("09", 16);
		bytes.add(byteList[2]);
		byteList[3] = (byte) Integer.parseInt("0A", 16);
		bytes.add(byteList[3]);
		byteList[4] = (byte) Integer.parseInt("00", 16);
		bytes.add(byteList[4]);
		byteList[5] = (byte) Integer.parseInt("0C", 16);
		bytes.add(byteList[5]);
		byteList[6] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[6]);
		byteList[7] = getCheckCode(bytes);
		byteList[8] = (byte) Integer.parseInt("FE", 16);
		return byteList;
	}

	// 得到下位机运行日志列表
	@SuppressLint("DefaultLocale")
	public static String getRunFileInfo(List<String> receive) {
		String maps = null;
		for (int i = 0; i < (receive.size() - 1); i++) {
			String infos = receive
					.get(i)
					.replace(" ", "")
					.substring(14, receive.get(i).replace(" ", "").length() - 4);
			infos = CHexConver.hexStr2Str(infos.toUpperCase());
			if (maps == null) {
				maps = infos + "\n";
			} else {
				maps = maps + infos + "\n";
			}
		}
		return maps;
	}

	// 查询下位机运行日志
	public static byte[] sendSelectRunfileInfo(int num) {
		bytes.removeAll(bytes);
		byte[] byteList = new byte[9];
		byteList[0] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[0]);
		byteList[1] = (byte) Integer.parseInt("FF", 16);
		bytes.add(byteList[1]);
		byteList[2] = (byte) Integer.parseInt("09", 16);
		bytes.add(byteList[2]);
		byteList[3] = (byte) Integer.parseInt("0A", 16);
		bytes.add(byteList[3]);
		byteList[4] = (byte) Integer.parseInt("00", 16);
		bytes.add(byteList[4]);
		byteList[5] = (byte) Integer.parseInt("0C", 16);
		bytes.add(byteList[5]);
		byteList[6] = (byte) num;
		bytes.add(byteList[6]);
		byteList[7] = getCheckCode(bytes);
		byteList[8] = (byte) Integer.parseInt("FE", 16);
		return byteList;
	}
}
