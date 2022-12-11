package meuhedet.com.temitestappl.dto;

import androidx.annotation.NonNull;

import java.util.List;

public class ResponseDtoNews {
	
	String status;
	Integer totalResults;
	List<ArticleDto> articles;

	public String getStatus() {
		return status;
	}

	public Integer getTotalResults() {
		return totalResults;
	}

	public List<ArticleDto> getArticles() {
		return articles;
	}

	@NonNull
	@Override
	public String toString() {
		return "ResponseDtoNews{" +
				"status='" + status + '\'' +
				", totalResults=" + totalResults +
				", articles=" + articles +
				'}';
	}
}
