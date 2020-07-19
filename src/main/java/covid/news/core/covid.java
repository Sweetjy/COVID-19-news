package covid.news.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;

import covid.news.entity.NewsInfo;
import redis.clients.jedis.Jedis;
import covid.news.duplicate.RedisUtil;

public class covid{
	@Autowired
	private NewsInfo info = new NewsInfo();
	
	private WebDriver driver;
    private ChromeOptions chromeOptions;
	private String[] source;
	private String url;

    private static Jedis jedis; 
	
	
	public covid(String url, String[] source,ChromeOptions chromeOptions) {
		// TODO Auto-generated constructor stub
		this.source = source;
		this.url = url;
		this.chromeOptions = chromeOptions;
	}
	
	public void athome(){
		driver = new ChromeDriver(chromeOptions); 
		driver.get(url);
		int count = 1;
		driver.findElement(By.cssSelector(".Sub_1-1-279_2v_FRK:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".Virus_1-1-279_2SKAfr")).click();
		driver.findElement(By.cssSelector(".Common_1-1-279_3lDRV2:nth-child(11) > span")).click();
		while(true){
				//国内疫情
				boolean end = reptile(driver,count);
				if(end)
					count++;
				else
					break;
		}
		driver.close();
		driver.quit();
		
	}
	public void outside() {
		driver = new ChromeDriver(chromeOptions); 
		driver.get(url);
		int count = 1;
		driver.findElement(By.cssSelector(".Sub_1-1-279_2v_FRK:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".Virus_1-1-279_2SKAfr")).click();
		driver.findElement(By.cssSelector(".Common_1-1-279_3lDRV2:nth-child(11) > span")).click();
		while(true){
				//国外疫情
				boolean end = reptile(driver,count);
				if(end)
					count++;
				else
					break;
		}
		driver.close();
		driver.quit();
	}
	/*
	 * 爬虫主函数
	 */
	public boolean reptile(WebDriver driver, int count){
//		jedis = RedisUtil.getJedis();//redis池
		try{
			WebElement a = driver.findElement(By.cssSelector("div:nth-child("+count+") > .Virus_1-1-279_TB6x3k >a"));
			String art_url = a.getAttribute("href");
//			if (!jedis.sismember("covid", art_url))
//			{
//				jedis.sadd("covid", art_url);
				info.setUrl(art_url);
			    WebElement t = driver.findElement(By.cssSelector("div:nth-child("+count+") > .Virus_1-1-279_TB6x3k .Virus_1-1-279_2CVyXP"));
			    info.setTitle(t.getText());			
				WebElement ti = driver.findElement(By.cssSelector("div:nth-child("+count+") >.Virus_1-1-279_2myqYf"));
				String time = ti.getText();
				String publishTime = "2020"+time.replace("月", "").replace("日", " ")+":00";
				if(!publishTime.contains("20200716")) {
					info.setPublishTime(publishTime);
					boolean s = this.articleContent(art_url, info);
					String filename = publishTime.substring(0, 8) + "no";
					if (s)
						saveJSON(toJSON.getJSON(info), filename);
				}
//			}
		}catch(Exception e){
//			RedisUtil.returnResource(jedis);
			return false;
		}
		RedisUtil.returnResource(jedis);
		return true;
	}
	/*
     * 提取正文
     */
    public boolean articleContent(String art_url, NewsInfo info) throws Exception{
		WebDriver driver = new ChromeDriver(chromeOptions); 
    	driver.get(art_url);
    	WebElement w=driver.findElement(By.cssSelector(".author-name"));
    	String webname = w.getText();

    	if(Arrays.asList(source).contains(webname)){
			WebElement c=driver.findElement(By.cssSelector(".article-content"));  
			String content = c.getText();
			info.setWebName(webname);
			info.setContent(content); 
			driver.close();
			driver.quit();
			return true;
    	}
		driver.close();
		driver.quit();
		return false;
    }
    
    /*
     * 将新闻存成json文件，用日期做名字，一次写一条，追加写入
     */
	int j=0;
    private void saveJSON(String jsonString, String filename) throws IOException {
    	System.out.println(filename);
		File file=new File("E:/IntelliJ IDEA 2020.1/workspace/data/"+filename+".json");
//    	File file=new File("/root/jyreptile/covid19/data/"+filename+".json");
    	if(j==0){
    		if (!file.exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
    	}
    	Writer write = new OutputStreamWriter(new FileOutputStream(file,true), "UTF-8");//true表示追加
        write.write(jsonString+"\n");
        write.flush();
        write.close();
        
    	j++;
    	if(j==20){
    		j=0;
    	}
	}
}
