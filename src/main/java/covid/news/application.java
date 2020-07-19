package covid.news;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import covid.news.core.covid;
import covid.news.entity.SomeInfo;


import org.springframework.boot.SpringApplication;

//启动类
@SpringBootApplication(exclude=
{DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@EnableScheduling   //定时任务
public class application {	
	@Autowired
	private SomeInfo word=new SomeInfo();
	private covid c;
	
	public static void main(String[] args) {

		SpringApplication.run(application.class, args);
	}
	
	@Scheduled(initialDelay=1000,fixedRate = 24*3600*1000)//24h执行一次这个方法
    public void process() {		
    	System.setProperty("webdriver.chrome.driver","C:/chromedriver.exe");
//		System.setProperty("webdriver.chrome.driver","/usr/local/bin/chromedriver");

		String source[]=word.source.split(",");
		String url = "https://voice.baidu.com/act/newpneumonia/newpneumonia/?from=osari_pc_1";
		
		ChromeOptions chromeOptions=new ChromeOptions();
		chromeOptions.addArguments("--headless");
		chromeOptions.addArguments("--disable-dev-shm-usage");
		chromeOptions.addArguments("--no-sandbox");
		
		c=new covid(url,source,chromeOptions);
//		c.athome();
		c.outside();
	}
	
}
