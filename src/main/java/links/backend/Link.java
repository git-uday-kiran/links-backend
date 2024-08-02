package links.backend;

import java.util.List;
import java.util.StringJoiner;

record Link(
	int id,
	String url,
	List<TopicTag> topicTags,
	List<TechTag> techTags,
	Importance importance,
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
