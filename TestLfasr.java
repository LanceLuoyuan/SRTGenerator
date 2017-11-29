package com.iflytek.msp.lfasr;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.ArrayList;
import java.nio.file.Files;
import java.util.Scanner;

import com.alibaba.fastjson.JSON;
import com.iflytek.msp.cpdb.lfasr.client.LfasrClientImp;
import com.iflytek.msp.cpdb.lfasr.exception.LfasrException;
import com.iflytek.msp.cpdb.lfasr.model.LfasrType;
import com.iflytek.msp.cpdb.lfasr.model.Message;
import com.iflytek.msp.cpdb.lfasr.model.ProgressStatus;
import org.apache.log4j.Logger;

public class TestLfasr
{
	// 原始音频存放地址 youtube-dl --extract-audio --audio-format mp3
	//scdc: 6a20342e3ecf365f330d9b60ff2e6fb7  5a181cc4
	// 97a3b03dee0aa7a5da560052dfd5852c  5a13b007 D:\Bruce\Budda\HuiYan\IT\lotus.4.nov.flac
	//private static final String local_file = "D:\\Bruce\\Budda\\HuiYan\\IT\\yulanpeng.mp3";
	final static Logger logger = Logger.getLogger(TestLfasr.class);
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	private static long startTimingInSeconds = 0;
	private static int startSequence = 0;

	/*
     * 转写类型选择：标准版和电话版分别为：
     * LfasrType.LFASR_STANDARD_RECORDED_AUDIO 和 LfasrType.LFASR_TELEPHONY_RECORDED_AUDIO
     * */
	private static final LfasrType type = LfasrType.LFASR_STANDARD_RECORDED_AUDIO;
	// 等待时长（秒）
	private static int sleepSecond = 20;

	public static void main(String[] args) {

	    /*
        PrintWithGreenColor("Mission complete, enjoy the srt file. :)");
        logger.info("\nMission complete, enjoy the srt file. :)");
        PrintWithGreenColor("Press enter to exit.");
        */

		String fileName = "";
		if(args.length < 1)
		{
			System.out.println("Please specify audio file name.");
			Scanner scanner = new Scanner(System.in);
			fileName = scanner.next();
			startTimingInSeconds = scanner.nextLong();
			startSequence = scanner.nextInt();
		}else
		{
			fileName = args[0];
			if(args.length >1)
			{
				startTimingInSeconds = Long.parseLong(args[1]);
				startSequence = Integer.parseInt(args[2]);
			}
		}
		logger.info("Audio file path: " + fileName);
		logger.info("startTimingInSeconds: " + startTimingInSeconds);
		logger.info("startSequence: " + startSequence);
		TestLfasr test = new TestLfasr();
		//test.GetMessageContent();
		test.Transcribe(fileName);
		test.ParseMessage(fileName);


	}

	public String GetMessageFileName(String filePath)
	{
		String name = filePath.substring(filePath.lastIndexOf("\\")+1)+".txt";
		return name;
	}

	public void ParseMessage(String filePath)
	{
		try
		{
			Path path = FileSystems.getDefault().getPath(System.getProperty("user.dir"), filePath+".txt");
			String message = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

			MessageParser parser = new MessageParser();
			ArrayList<MessageXunFei> list = parser.Parse(message);
			logger.info("list count: " + list.size());
			FileWriter fileWriter = new FileWriter(filePath.substring(filePath.lastIndexOf("\\")+1)+".srt");
			for (MessageXunFei line : list)
			{
				//if(!line.si.equals("0"))
				{
					fileWriter.append(String.valueOf(Integer.parseInt(line.si)+1+startSequence));
					fileWriter.append("\n");
					fileWriter.append(String.format("%s --> %s", ConvertSeconds(line.bg), ConvertSeconds(line.ed)));
					fileWriter.append("\n");
					fileWriter.append(String.valueOf(line.onebest));
					fileWriter.append("\n\n");
				}
			}
			fileWriter.flush();
			fileWriter.close();
			System.out.println();
			PrintWithGreenColor("Mission complete, enjoy the srt file. :)");
			logger.info("\nMission complete, enjoy the srt file. :)");
			PrintWithGreenColor("Press enter to exit.");
			System.in.read();

		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void PrintWithGreenColor(String message)
	{
		System.out.println("**********\n");
		//System.out.println(ANSI_GREEN_BACKGROUND + message + ANSI_RESET);
		System.out.println(message);
		System.out.println("\n**********");

	}
	public static String ConvertSeconds(String totalMilliSeconds) {
		long total = Long.parseLong(totalMilliSeconds)+ (startTimingInSeconds*1000);

		int sec  = (int)(total/ 1000) % 60 ;
		int min  = (int)((total/ (1000*60)) % 60);
		int hr   = (int)((total/ (1000*60*60)) % 24);
		String output = String.format("%d:%02d:%02d.%03d", hr, min, sec, total % 1000);

		return output;
	}

	public void Transcribe(String filePath) {


		// 加载配置文件
		//PropertyConfigurator.configure("D:\\Bruce\\Budda\\HuiYan\\IT\\log4j.properties");

		// 初始化LFASR实例
		LfasrClientImp lc = null;
		try {
			lc = LfasrClientImp.initLfasrClient();
		} catch (LfasrException e) {
			// 初始化异常，解析异常描述信息
			Message initMsg = JSON.parseObject(e.getMessage(), Message.class);
			System.out.println("ecode=" + initMsg.getErr_no());
			System.out.println("failed=" + initMsg.getFailed());
		}

		// 获取上传任务ID
		String task_id = "";
		HashMap<String, String> params = new HashMap<>();
		params.put("has_participle", "true");
		//params.put("max_alternatives", "3");
		try {
			// 上传音频文件
			Message uploadMsg = lc.lfasrUpload(filePath, type, params);

			// 判断返回值
			int ok = uploadMsg.getOk();
			if (ok == 0) {
				// 创建任务成功
				task_id = uploadMsg.getData();
				System.out.println("task_id=" + task_id);
			} else {
				// 创建任务失败-服务端异常
				System.out.println("ecode=" + uploadMsg.getErr_no());
				System.out.println("failed=" + uploadMsg.getFailed());
			}
		} catch (LfasrException e) {
			// 上传异常，解析异常描述信息
			Message uploadMsg = JSON.parseObject(e.getMessage(), Message.class);
			System.out.println("ecode=" + uploadMsg.getErr_no());
			System.out.println("failed=" + uploadMsg.getFailed());
		}

		// 循环等待音频处理结果
		while (true) {
			try {
				// 睡眠1min。另外一个方案是让用户尝试多次获取，第一次假设等1分钟，获取成功后break；失败的话增加到2分钟再获取，获取成功后break；再失败的话加到4分钟；8分钟；……
				Thread.sleep(sleepSecond * 1000);
				System.out.println("waiting ...");
			} catch (InterruptedException e) {
			}
			try {
				// 获取处理进度
				Message progressMsg = lc.lfasrGetProgress(task_id);

				// 如果返回状态不等于0，则任务失败
				if (progressMsg.getOk() != 0) {
					System.out.println("task was fail. task_id:" + task_id);
					System.out.println("ecode=" + progressMsg.getErr_no());
					System.out.println("failed=" + progressMsg.getFailed());

					// 服务端处理异常-服务端内部有重试机制（不排查极端无法恢复的任务）
					// 客户端可根据实际情况选择：
					// 1. 客户端循环重试获取进度
					// 2. 退出程序，反馈问题
					continue;
				} else {
					ProgressStatus progressStatus = JSON.parseObject(progressMsg.getData(), ProgressStatus.class);
					if (progressStatus.getStatus() == 9) {
						// 处理完成
						System.out.println("task was completed. task_id:" + task_id);
						break;
					} else {
						// 未处理完成
						logger.info("task was incomplete. task_id:" + task_id + ", status:" + progressStatus.getDesc());
						continue;
					}
				}
			} catch (LfasrException e) {
				// 获取进度异常处理，根据返回信息排查问题后，再次进行获取
				Message progressMsg = JSON.parseObject(e.getMessage(), Message.class);
				System.out.println("ecode=" + progressMsg.getErr_no());
				System.out.println("failed=" + progressMsg.getFailed());
			}
		}

		// 获取任务结果
		try {
			Message resultMsg = lc.lfasrGetResult(task_id);
			//System.out.println(resultMsg.getData());
			// 如果返回状态等于0，则任务处理成功
			if (resultMsg.getOk() == 0) {
				// 打印转写结果
				String data = resultMsg.getData();
				System.out.println("Data length: " +data.length());
				int length = data.length() > 100 ? 100 : data.length();
				logger.info(data.substring(0, length));
				FileWriter fileWriter = new FileWriter(GetMessageFileName(filePath));
				fileWriter.append(resultMsg.getData());

				fileWriter.flush();
				fileWriter.close();
				//PrintWithGreenColor("Get data from XunFei successfully.");
				logger.info("Get data from XunFei successfully.");

			} else {
				// 转写失败，根据失败信息进行处理
				System.out.println("ecode=" + resultMsg.getErr_no());
				System.out.println("failed=" + resultMsg.getFailed());
			}
		} catch (LfasrException e) {
			// 获取结果异常处理，解析异常描述信息
			Message resultMsg = JSON.parseObject(e.getMessage(), Message.class);
			System.out.println("ecode=" + resultMsg.getErr_no());
			System.out.println("failed=" + resultMsg.getFailed());
		}catch (IOException io)
		{
			io.printStackTrace();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void GetMessageContent(String filePath)
	{
// 加载配置文件
		//PropertyConfigurator.configure("D:\\Bruce\\Budda\\HuiYan\\IT\\log4j.properties");

		// 初始化LFASR实例
		LfasrClientImp lc = null;
		try {
			lc = LfasrClientImp.initLfasrClient();
		} catch (LfasrException e) {
			// 初始化异常，解析异常描述信息
			Message initMsg = JSON.parseObject(e.getMessage(), Message.class);
			System.out.println("ecode=" + initMsg.getErr_no());
			System.out.println("failed=" + initMsg.getFailed());
		}
		// 获取任务结果
		try {
			String taskId = "88fd5f0b061142d3a922ae92c5e21172";
			Message resultMsg = lc.lfasrGetResult(taskId);
			System.out.println(resultMsg.getData());
			// 如果返回状态等于0，则任务处理成功
			if (resultMsg.getOk() == 0) {
				// 打印转写结果
				System.out.println(resultMsg.getData());
				FileWriter fileWriter = new FileWriter(GetMessageFileName(filePath));
				fileWriter.append(resultMsg.getData());

				fileWriter.flush();
				fileWriter.close();

			} else {
				// 转写失败，根据失败信息进行处理
				System.out.println("ecode=" + resultMsg.getErr_no());
				System.out.println("failed=" + resultMsg.getFailed());
			}
		} catch (LfasrException e) {
			// 获取结果异常处理，解析异常描述信息
			Message resultMsg = JSON.parseObject(e.getMessage(), Message.class);
			System.out.println("ecode=" + resultMsg.getErr_no());
			System.out.println("failed=" + resultMsg.getFailed());
		}catch (IOException io)
		{
			io.printStackTrace();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
