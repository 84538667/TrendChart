package com.example.trendchart;

public class ScoreListConst {
	/**
	 * 天外天用户名标签
	 */
	public static final String TWT_USERNAME = "username";
	/**
	 * 天外天密码标签
	 */
	public static final String TWT_PASSWORD = "pwd";
	/**
	 * 天外天服务器获取分数信息
	 */
	public static final String TWT_SCOREINF_URL = "http://service.twtstudio.com/phone/android/gpa.php";
	/**
	 * 获取成绩信息错误标签
	 */
	public static final String TWT_GET_INF_ERROR = "TWT_GET_INF_ERROR";
	/**
	 * 解析报错
	 */
	public static final String TWT_CONVERT_ERROR = "TWT_CONVERT_ERROR";
	/**
	 * 校园网账号/密码错误提示
	 */
	public static final String TWT_TJU_LOGIN_ERROR = "账号/密码错误";
	/**
	 * 网络连接不可用提示信息
	 */
	public static final String TWT_CONNECT_UNABLE = "网络连接不可用，请连接后重试";
	/**
	 * 获取分数信息错误时的返回字符串
	 */
	public static final String TWT_GET_SCORE_ERROR_STRING = "<br /><b>Warning</b>:  Division by zero in <b>/home/ftp2/1520/service_twt-20120517-HaV/service.twtstudio.com/phone/android/gpa.php</b> on line <b>106</b><br />[{\"gpa\":4,\"totalGpa\":0,\"every\":[]}]";
}
