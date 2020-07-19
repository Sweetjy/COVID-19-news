package covid.news.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.context.annotation.Configuration;

@Entity
@Configuration
public class NewsInfo {
    private String title;
    private String content;
    @Id
    private String url;
    private String webName;
    private String publishTime;
        
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getWebName() {
		return webName;
	}
	public void setWebName(String webName) {
		this.webName = webName;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

}
