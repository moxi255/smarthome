package com.example.abc.smarthome;


//public class Util {

	/***********解析单个状态 *********************************
	 * 解析单个状态
	 * 接收到的数据有可能是:
	 * #SERVERSIGN#B#ON#
	 * *********************************************************************/
//	public static void analyseSingleStatus(String str) {
//
//		String sub = str.substring(str.indexOf('#'), str.lastIndexOf('#') + 1);
//		System.out.println(sub);
//
//		String[] strs = sub.split("#");
//
//		if (strs[1].equals("SERVERSIGN")) {
//
//			if (strs[2].charAt(0) == 'A') {				//继电器，模拟门禁
//				if (strs[3].equals("ON")) {
//					HomeConfig.RELAY_STATUE = true;
//				} else if (strs[2].equals("OFF")) {
//					HomeConfig.RELAY_STATUE = false;
//				}
//			} else if (strs[2].charAt(0) == 'B') {		//led，模拟主卧灯
//				if (strs[3].equals("ON")) {
//					HomeConfig.ROOMLIGHT_STATUE = true;
//				} else if (strs[3].equals("OFF")) {
//					HomeConfig.ROOMLIGHT_STATUE = false;
//				}
//			} else if (strs[2].charAt(0) == 'C') {			//led，模拟客卧灯
//				if (strs[3].equals("ON")) {
//					HomeConfig.CUSTOMERRIGHT_STATUE = true;
//				} else if (strs[3].equals("OFF")) {
//					HomeConfig.CUSTOMERRIGHT_STATUE = false;
//				}
//			} else if (strs[2].charAt(0) == 'D') {			//步进电机，模拟窗帘控制
//				if (strs[3].equals("ON")) {
//					HomeConfig.CURTAIN_STATUE = true;
//				} else if (strs[3].equals("OFF")) {
//					HomeConfig.CURTAIN_STATUE = false;
//				}
//			} else if (strs[2].charAt(0) == 'E') {			//直流电机，模拟空调
//				if (strs[3].equals("ON")) {
//					HomeConfig.AIRCONDITIONING_STATUE = true;
//				} else if (strs[3].equals("OFF")) {
//					HomeConfig.AIRCONDITIONING_STATUE = false;
//				}
//			} else if (strs[2].charAt(0) == 'F') {			//模拟报警
//				if (strs[3].equals("ON")) {
//					HomeConfig.ALARM_STATUE = true;
//				} else if (strs[3].equals("OFF")) {
//					HomeConfig.ALARM_STATUE = false;
//				}
//			} else {
//				System.out.println("数据接收出错");
//			}
//		}
//	}

	/**************************************************************
	 * 接收服务器的状态: A 为继电器状态 ,B 为房间灯状态 ,C 为客厅灯状态 ,D 为窗帘状态, E 为空调状态，F 为报警状态
	 * 解析多个状态一起
	 * 接收到的数据有可能是:
	 * #SERVERSIGN#A0#B0#C0#D0#E0#F1#  等等
	 *************************************************************/
//	public static void getStatus(String str) {
//
//		String sub = str.substring(str.indexOf('#'), str.lastIndexOf('#') + 1);
//		System.out.println("str:"+str);
//		System.out.println("sub:"+sub);
//
//		String[] strs = sub.split("#");
//
//		if (strs[1].equals("SERVERSIGN")) {
//			System.out.println("字符串用来获取状态");
//			for (int i = 2; i < strs.length; i++) {// 循环每隔strs子串
//				if (strs[i].charAt(0) == 'A') {//strs[i]:  A0或者A1
//					if (strs[i].substring(1).equals("1")) {
//						HomeConfig.RELAY_STATUE = true;
//					} else if (strs[i].substring(1).equals("0")) {
//						HomeConfig.RELAY_STATUE = false;
//					}
//				} else if (strs[i].charAt(0) == 'C') {//strs[i]:  B0或者B1  下同
//					if (strs[i].substring(1).equals("1")) {
//						HomeConfig.ROOMLIGHT_STATUE = true;
//					} else if (strs[i].substring(1).equals("0")) {
//						HomeConfig.ROOMLIGHT_STATUE = false;
//					}
//				} else if (strs[i].charAt(0) == 'B') {
//					if (strs[i].substring(1).equals("1")) {
//						HomeConfig.CUSTOMERRIGHT_STATUE = true;
//					} else if (strs[i].substring(1).equals("0")) {
//						HomeConfig.CUSTOMERRIGHT_STATUE = false;
//					}
//				} else if (strs[i].charAt(0) == 'D') {
//					if (strs[i].substring(1).equals("1")) {
//						HomeConfig.CURTAIN_STATUE = true;
//					} else if (strs[i].substring(1).equals("0")) {
//						HomeConfig.CURTAIN_STATUE = false;
//					}
//				} else if (strs[i].charAt(0) == 'E') {
//					if (strs[i].substring(1).equals("1")) {
//						HomeConfig.AIRCONDITIONING_STATUE = true;
//					} else if (strs[i].substring(1).equals("0")) {
//						HomeConfig.AIRCONDITIONING_STATUE = false;
//					}
//				}else if (strs[i].charAt(0) == 'F') {
//					if (strs[i].substring(1).equals("1")) {
//						HomeConfig.ALARM_STATUE = true;
//					} else if (strs[i].substring(1).equals("0")) {
//						HomeConfig.ALARM_STATUE = false;
//					}
//					System.out.println("HomeConfig.ALERM_STATUE");
//					System.out.println(HomeConfig.ALARM_STATUE);
//				}  else {
//					System.out.println("数据接收出错");
//					break;
//				}
//			}
//		}
//
//	}
//}