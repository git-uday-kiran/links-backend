package links.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.StringJoiner;

record Link(
	@NotNull
	int id,
	@NotNull
	String url,

	@JsonProperty(value = "topic_tags")
	@NotNull
	List<TopicTag> topicTags,
	@NotNull
	List<TechTag> techTags,
	@NotNull
	Importance importance,
	@NotNull
	String description
) {
	String toLine() {
		var joiner = new StringJoiner(";");
		joiner.add(String.valueOf(id));
		joiner.add(url);
		joiner.add(String.join(",", topicTags.stream().map(TopicTag::toString).toList()));
		joiner.add(String.join(",", techTags.stream().map(TechTag::toString).toList()));
		joiner.add(importance.toString());
		joiner.add(description);
		return joiner.toString();
	}
}
