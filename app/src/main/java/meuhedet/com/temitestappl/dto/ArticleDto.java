package meuhedet.com.temitestappl.dto;


import androidx.annotation.NonNull;

public class ArticleDto {

	String author;
	String title;
	String description;
	String url;
	String urlToImage;
	String publishedAt;
	String content;

	public String getAuthor() {
		return author;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getUrl() {
		return url;
	}

	public String getUrlToImage() {
		return urlToImage;
	}

	public String getPublishedAt() {
		return publishedAt;
	}

	public String getContent() {
		return content;
	}

	@NonNull
	@Override
	public String toString() {
		return "ArticleDto{" +
				"author='" + author + '\'' +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", url='" + url + '\'' +
				", urlToImage='" + urlToImage + '\'' +
				", publishedAt='" + publishedAt + '\'' +
				", content='" + content + '\'' +
				'}';
	}
}
