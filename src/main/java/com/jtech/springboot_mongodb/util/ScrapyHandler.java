package com.jtech.springboot_mongodb.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.springframework.stereotype.Service;

import com.jtech.springboot_mongodb.svc.BizServiceImpl;

@Service("scrapyHandler")
public class ScrapyHandler extends BizServiceImpl {

	class SyncPipe implements Runnable {
		public SyncPipe(InputStream istrm, OutputStream ostrm) {
			istrm_ = istrm;
			ostrm_ = ostrm;
		}

		public void run() {
			try {
				final byte[] buffer = new byte[1024];
				for (int length = 0; (length = istrm_.read(buffer)) != -1;) {
					ostrm_.write(buffer, 0, length);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private final OutputStream ostrm_;
		private final InputStream istrm_;
	}

	public void runScrapy(String crawlerPath, String crawlerName, String etc_args) throws Exception {
		log.info(">>>>> runScrapy start");

		String osName = System.getProperty("os.name");
		log.info(">>>>> OS Name: " + osName);

		Process p = null;

		log.info(">>>>> OS: " + osName);
		if (osName.contains("Windows")) {
			String[] cmd = { "cmd.exe", "/c",
					"cd \"" + crawlerPath + "\" && " + "scrapy" + " crawl " + crawlerName + " " + etc_args };
			p = Runtime.getRuntime().exec(cmd);
		} else if ("Linux".equals(osName)) {
//			String proxyCommand = "export http_proxy=http://jmko79:hist123@us-dc.proxymesh.com:31280;";
			String[] cmd = { "/bin/sh", "-c",  
					" cd " + crawlerPath + ";" + "scrapy" + " crawl " + crawlerName + " " + etc_args };
			for(int i = 0; i <cmd.length; i++) {
				log.info(">>>>> cmd[" + i + "]: " + cmd[i]);
			}
			p = Runtime.getRuntime().exec(cmd);
		} else {
			throw new CustomedExceptionImpl("Other OS is not yet supported");
		}

		new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
		new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
		PrintWriter stdin = new PrintWriter(p.getOutputStream());
		stdin.close();
		int returnCode = p.waitFor();
		log.info("Return code = " + returnCode);

		log.info(">>>>> runScrapy end");
	}

}
